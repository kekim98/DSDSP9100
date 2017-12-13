package com.dignsys.dsdsp.dsdsp_9100.service;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.CommandEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.RssEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ScheduleEntity;
import com.dignsys.dsdsp.dsdsp_9100.model.PlayContent;
import com.dignsys.dsdsp.dsdsp_9100.service.handler.BasicHandler;
import com.dignsys.dsdsp.dsdsp_9100.service.handler.CommandHandler;
import com.dignsys.dsdsp.dsdsp_9100.service.handler.ConfigHandler;
import com.dignsys.dsdsp.dsdsp_9100.service.handler.FormatHandler;
import com.dignsys.dsdsp.dsdsp_9100.service.handler.PlayListHandler;
import com.dignsys.dsdsp.dsdsp_9100.service.handler.RssHandler;
import com.dignsys.dsdsp.dsdsp_9100.util.DaulUtils;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.CommandHelper;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.ConsoleRequestLogger;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.RequestLogger;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Helper class that parses conference data and imports them into the app's
 * Content Provider.
 */
public class PlayDataHandler {
    private static final String TAG = PlayDataHandler.class.getSimpleName();

     static final String DATA_KEY_PLAYLIST = "play_list";
     static final String DATA_KEY_FORMAT = "format";
     static final String DATA_KEY_CONFIG = "config";
     static final String DATA_KEY_COMMAND = "command";
     static final String DATA_KEY_RSS= "rss";


     static final String[] DATA_KEYS_IN_ORDER = {
            DATA_KEY_PLAYLIST,
            DATA_KEY_FORMAT,
            DATA_KEY_RSS,
            DATA_KEY_CONFIG,
            DATA_KEY_COMMAND
    };

    private final Context mContext;
   // private final Gson mGson = new Gson();

    // Handlers for each entity type:
    private PlayListHandler mPlayListHandler;
    private FormatHandler mFormatHandler;
    private ConfigHandler mConfigHandler;
    private CommandHandler mCommandHandler;
    private RssHandler mRssHandler;

    // Convenience map that maps the key name to its corresponding handler (e.g.
    // "blocks" to mFormatHandler (to avoid very tedious if-elses)
    private final HashMap<String, BasicHandler> mHandlerForKey = new HashMap<>();

    // Tally of total content provider operations we carried out (for statistical purposes)
    private int mContentProviderOperationsDone = 0;


    public PlayDataHandler(Context ctx) {
        mContext = ctx;
    }

    /**
     * Parses the conference data in the given objects and imports the data into the
     * content provider. The format of the data is documented at https://code.google.com/p/iosched.
     *
     * @param dataBodies       The collection of JSON objects to parse and import.
     * @param downloadsAllowed Whether or not we are supposed to download data from the internet if
     *                         needed.
     * @throws IOException If there is a problem parsing the data.
     */
    public void applyPlayData(String[] dataBodies, boolean downloadsAllowed) throws IOException {
        Log.d(TAG, "Applying data from " + dataBodies.length + " files");

        // create handlers for each data type
        mHandlerForKey.put(DATA_KEY_PLAYLIST, mPlayListHandler = new PlayListHandler(mContext));
        mHandlerForKey.put(DATA_KEY_FORMAT, mFormatHandler = new FormatHandler(mContext));
        mHandlerForKey.put(DATA_KEY_RSS, mRssHandler = new RssHandler(mContext));
        mHandlerForKey.put(DATA_KEY_CONFIG, mConfigHandler = new ConfigHandler(mContext));
        mHandlerForKey.put(DATA_KEY_COMMAND, mCommandHandler = new CommandHandler(mContext));


        // process the playlist.txt and format.txt. This will call each of the handlers when appropriate to deal
        // with the objects we see in the data.
        Log.d(TAG, "Processing " + dataBodies.length + " Operation Object.");
        for (int i = 0; i < dataBodies.length; i++) {
            Log.d(TAG, "Processing json object #" + (i + 1) + " of " + dataBodies.length);
            processDataBody(dataBodies[i], DATA_KEYS_IN_ORDER[i]);
        }

        // the playListHandler needs to know the formatHandler to map scene with format
        mPlayListHandler.setFormat(mFormatHandler.getFormatList());

      // download content files (media files)
        Log.d(TAG, "Processing content files");

        //TODO : have to separate playcontent dir and command content dir
        processPlayContentFiles(mPlayListHandler.getContentFiles(), downloadsAllowed);
        processCommandContentFiles(mCommandHandler.getContentFiles(), downloadsAllowed);

        // finally, push the changes into the ROOM

        applyPlayDataToDB();


        Log.d(TAG, "Done applying conference data.");
    }

    private void notifyPlayStart() {

        /*MutableLiveData<Integer> command = CommandHelper.getInstance(mContext).getPlayCommand();
        command.postValue(Definer.DEF_PLAY_START_COMMAND);*/

    }

    private void notifyPlayStop() {

        MutableLiveData<Integer> command = CommandHelper.getInstance(mContext).getPlayCommand();
      //  command.postValue(Definer.DEF_PLAY_STOP_COMMAND);
        command.postValue(Definer.DEF_PLAY_IDLE_COMMAND);

    }

    private void applyPlayDataToDB() {
        AppDatabase db = DatabaseCreator.getInstance(mContext);

        List<ScheduleEntity> scheduleEntityList = mPlayListHandler.getScheduleList();
        List<SceneEntity> sceneEntities = mPlayListHandler.getSceneList();
        List<PaneEntity> paneEntityList = mFormatHandler.getPaneList();
        List<ContentEntity> contentEntities = mPlayListHandler.getContentList();

        if (scheduleEntityList.size() > 0 && sceneEntities.size() > 0
                && paneEntityList.size() > 0 && contentEntities.size() > 0) {

            notifyPlayStop();
            db.updatePlayDataTransaction(scheduleEntityList,
                    sceneEntities,
                    paneEntityList,
                    contentEntities);
            notifyPlayStart();
        }


        CommandEntity commandEntity = mCommandHandler.getCommand();

        if (commandEntity != null) {
            db.updateCommandTransaction(commandEntity);
        }


        ConfigEntity config = mConfigHandler.getConfig();
        if (config != null) {
            db.configDao().update(config);
        }

        RssEntity rss = mRssHandler.getRss();
        if (rss != null) {
            db.rssDao().update(rss);
        }

    }

    public int getContentProviderOperationsDone() {
        return mContentProviderOperationsDone;
    }

    /**
     * Processes a conference data body and calls the appropriate data type handlers
     * to process each of the objects represented therein.
     *
     * @param dataBody The body of data to process
     * @throws IOException If there is an error parsing the data.
     */
    private void processDataBody(String dataBody, String key) throws IOException {
        if (dataBody == null) {
            return;
        }
        final BasicHandler handler = mHandlerForKey.get(key);
        handler.process(dataBody);

    }

    /**
     * Synchronise the content files either from the local assets (if available) or from a
     * remote url.
     *
     * collection Set of contents containing a local filename and remote url.
     */
    private void processPlayContentFiles(ArrayList<PlayContent> contents, boolean downloadAllowed)
            throws IOException{
        // clear the content cache on disk if any contents have been updated
       // boolean shouldClearCache = false;
        // keep track of used files, unused files are removed
        ArrayList<String> usedContents = new ArrayList<>();
        for (PlayContent content : contents) {
            final String filename = content.filename;
            final String url = content.url;

            usedContents.add(filename);

            if (!IOUtils.hasContent(mContext, filename)) {
         //       shouldClearCache = true;
                // copy or download the content if it is not stored yet
              if (downloadAllowed && !TextUtils.isEmpty(url)) {
                    try {
                        // download the file only if downloads are allowed and url is not empty
                        File contentFile = IOUtils.getContentFile(mContext, filename);
                        BasicHttpClient httpClient = new BasicHttpClient();
                        httpClient.setRequestLogger(mQuietLogger);
                      //  IOUtils.authorizeHttpClient(mContext, httpClient);
                        String encURL = DaulUtils.encodeURL(url);
                        HttpResponse httpResponse = httpClient.get(encURL, null);
                        IOUtils.writeToFile(httpResponse.getBody(), contentFile);

                    } catch (IOException ex) {
                        Log.e(TAG, "FAILED downloading map overlay content " + url +
                                ": " + ex.getMessage(), ex);
                    } 
                } else {
                    Log.d(TAG, "Skipping download of map overlay content" +
                            " (since downloadsAllowed=false)");
                }
            }
        }

        /*if (shouldClearCache) {
            IOUtils.clearDiskCache(mContext);
        }*/

        if (contents.size() > 0) {
            IOUtils.removeUnusedContents(mContext, usedContents);
        }
    }



    private void processCommandContentFiles(ArrayList<PlayContent> contents, boolean downloadAllowed)
            throws IOException{
        // clear the content cache on disk if any contents have been updated
        // boolean shouldClearCache = false;
        // keep track of used files, unused files are removed
        ArrayList<String> usedContents = new ArrayList<>();
        for (PlayContent content : contents) {
            final String filename = content.filename;
            final String url = content.url;

            usedContents.add(filename);

            if (!IOUtils.hasContent(mContext, filename)) {
                //       shouldClearCache = true;
                // copy or download the content if it is not stored yet
                if (downloadAllowed && !TextUtils.isEmpty(url)) {
                    try {
                        // download the file only if downloads are allowed and url is not empty
                        File contentFile = IOUtils.getCommandContentFile(mContext, filename);
                        BasicHttpClient httpClient = new BasicHttpClient();
                        httpClient.setRequestLogger(mQuietLogger);
                        //  IOUtils.authorizeHttpClient(mContext, httpClient);
                        String encURL = DaulUtils.encodeURL(url);
                        HttpResponse httpResponse = httpClient.get(encURL, null);
                        IOUtils.writeToFile(httpResponse.getBody(), contentFile);

                    } catch (IOException ex) {
                        Log.e(TAG, "FAILED downloading map overlay content " + url +
                                ": " + ex.getMessage(), ex);
                    }
                } else {
                    Log.d(TAG, "Skipping download of map overlay content" +
                            " (since downloadsAllowed=false)");
                }
            }
        }

        /*if (shouldClearCache) {
            IOUtils.clearDiskCache(mContext);
        }*/

        if (contents.size() > 0) {
            IOUtils.removeUnusedCommandContents(mContext, usedContents);
        }
    }

    /**
     * A type of ConsoleRequestLogger that does not log requests and responses.
     */
    private final RequestLogger mQuietLogger = new ConsoleRequestLogger() {
        @Override
        public void logRequest(HttpURLConnection uc, Object content) throws IOException {
        }

        @Override
        public void logResponse(HttpResponse res) {
        }
    };

}
