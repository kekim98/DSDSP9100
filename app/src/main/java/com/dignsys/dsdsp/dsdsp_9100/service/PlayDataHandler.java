/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dignsys.dsdsp.dsdsp_9100.service;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;


import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.service.handler.BasicHandler;
import com.dignsys.dsdsp.dsdsp_9100.service.handler.FormatHandler;
import com.dignsys.dsdsp.dsdsp_9100.service.handler.PlayListHandler;
import com.turbomanage.httpclient.ConsoleRequestLogger;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.RequestLogger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;



/**
 * Helper class that parses conference data and imports them into the app's
 * Content Provider.
 */
public class PlayDataHandler {
    private static final String TAG = PlayDataHandler.class.getSimpleName();

    // Shared settings_prefs key under which we store the timestamp that corresponds to
    // the data we currently have in our content provider.
    private static final String SP_KEY_DATA_TIMESTAMP = "data_timestamp";

    // symbolic timestamp to use when we are missing timestamp data (which means our data is
    // really old or nonexistent)
    private static final String DEFAULT_TIMESTAMP = "Sat, 1 Jan 2000 00:00:00 GMT";

    private static final String DATA_KEY_PLAYLIST = "play_list";
    private static final String DATA_KEY_FORMAT = "format";
    private static final String DATA_KEY_CONFIG = "config";
    private static final String DATA_KEY_CONTROL = "control";
    
    private static final String DATA_KEY_RSS= "rss";


    private static final String[] DATA_KEYS_IN_ORDER = {
            DATA_KEY_PLAYLIST,
            DATA_KEY_FORMAT,
            DATA_KEY_CONFIG,
            DATA_KEY_CONTROL,
            
         //   DATA_KEY_RSS,
    };

    private final Context mContext;
   // private final Gson mGson = new Gson();

    // Handlers for each entity type:
    private PlayListHandler mPlayListHandler;
    private FormatHandler mFormatHandler;

    /*
    private ConfigHandler mConfigHandler;
    private ControlHandler mControlHandler;
    private RssHandler mRssHandler;*/


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
       /* mHandlerForKey.put(DATA_KEY_CONFIG, mControlHandler = new ConfigHandler(mContext));
        mHandlerForKey.put(DATA_KEY_CONTROL, mRssHandler = new CorntrolHandler(mContext));
        mHandlerForKey.put(DATA_KEY_CONTROL, mRssHandler = new RssHandler(mContext));*/


        // process the playlist.txt and format.txt. This will call each of the handlers when appropriate to deal
        // with the objects we see in the data.
        Log.d(TAG, "Processing " + dataBodies.length + " Operation Object.");
        for (int i = 0; i < dataBodies.length; i++) {
            Log.d(TAG, "Processing json object #" + (i + 1) + " of " + dataBodies.length);
            processDataBody(dataBodies[i], DATA_KEYS_IN_ORDER[i]);
        }

        // the sessions handler needs to know the tag and speaker maps to process sessions
        /*mSessionsHandler.setTagMap(mControlHandler.getTagMap());
        mSessionsHandler.setSpeakerMap(mRssHandler.getSpeakerMap());*/

        // produce the necessary content provider operations
        ArrayList<ContentProviderOperation> batch = new ArrayList<>();
        for (String key : DATA_KEYS_IN_ORDER) {
            Log.i(TAG, "Building content provider operations for: " + key);
            mHandlerForKey.get(key).makeContentProviderOperations(batch);
            Log.i(TAG, "Content provider operations so far: " + batch.size());
        }
        Log.d(TAG, "Total content provider operations: " + batch.size());

        /*// download or process local map tile overlay files (SVG files)
        Log.d(TAG, "Processing map overlay files");
        processMapOverlayFiles(mMapPropertyHandler.getTileOverlays(), downloadsAllowed);*/

        // finally, push the changes into the Content Provider
        Log.i(TAG, "Applying " + batch.size() + " content provider operations.");
        /*try {
            int operations = batch.size();
            if (operations > 0) {
                mContext.getContentResolver().applyBatch(ScheduleContract.CONTENT_AUTHORITY, batch);
            }
            Log.d(TAG, "Successfully applied " + operations + " content provider operations.");
            mContentProviderOperationsDone += operations;
        } catch (RemoteException ex) {
            Log.e(TAG, "RemoteException while applying content provider operations.");
            throw new RuntimeException("Error executing content provider batch operation", ex);
        } catch (OperationApplicationException ex) {
            Log.e(TAG, "OperationApplicationException while applying content provider operations.");
            throw new RuntimeException("Error executing content provider batch operation", ex);
        }

        // notify all top-level paths
        Log.d(TAG, "Notifying changes on all top-level paths on Content Resolver.");
        ContentResolver resolver = mContext.getContentResolver();
        for (String path : ScheduleContract.TOP_LEVEL_PATHS) {
            Uri uri = ScheduleContract.BASE_CONTENT_URI.buildUpon().appendPath(path).build();
            resolver.notifyChange(uri, null);
        }*/

        // update our data timestamp
      //  setDataTimestamp(dataTimestamp);
        Log.d(TAG, "Done applying conference data.");
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
     * Synchronise the map overlay files either from the local assets (if available) or from a
     * remote url.
     *
     * collection Set of tiles containing a local filename and remote url.
     */
  /*  private void processMapOverlayFiles(Collection<Tile> collection, boolean downloadAllowed)
            throws IOException, SVGParseException {
        // clear the tile cache on disk if any tiles have been updated
        boolean shouldClearCache = false;
        // keep track of used files, unused files are removed
        ArrayList<String> usedTiles = new ArrayList<>();
        for (Tile tile : collection) {
            final String filename = tile.filename;
            final String url = tile.url;

            usedTiles.add(filename);

            if (!MapUtils.hasTile(mContext, filename)) {
                shouldClearCache = true;
                // copy or download the tile if it is not stored yet
                if (MapUtils.hasTileAsset(mContext, filename)) {
                    // file already exists as an asset, copy it
                    MapUtils.copyTileAsset(mContext, filename);
                } else if (downloadAllowed && !TextUtils.isEmpty(url)) {
                    try {
                        // download the file only if downloads are allowed and url is not empty
                        File tileFile = MapUtils.getTileFile(mContext, filename);
                        BasicHttpClient httpClient = new BasicHttpClient();
                        httpClient.setRequestLogger(mQuietLogger);
                        IOUtils.authorizeHttpClient(mContext, httpClient);
                        HttpResponse httpResponse = httpClient.get(url, null);
                        IOUtils.writeToFile(httpResponse.getBody(), tileFile);

                        // ensure the file is valid SVG
                        InputStream is = new FileInputStream(tileFile);
                        SVG svg = new SVGBuilder().readFromInputStream(is).build();
                        is.close();
                    } catch (IOException ex) {
                        Log.e(TAG, "FAILED downloading map overlay tile " + url +
                                ": " + ex.getMessage(), ex);
                    } catch (SVGParseException ex) {
                        Log.e(TAG, "FAILED parsing map overlay tile " + url +
                                ": " + ex.getMessage(), ex);
                    }
                } else {
                    Log.d(TAG, "Skipping download of map overlay tile" +
                            " (since downloadsAllowed=false)");
                }
            }
        }

        if (shouldClearCache) {
            MapUtils.clearDiskCache(mContext);
        }

        MapUtils.removeUnusedTiles(mContext, usedTiles);
    }*/

    // Returns the timestamp of the data we have in the content provider.
    public static String getPlayTimestamp(Context ctx, int idx) {


        String key="";

        if(idx == Definer.ORDER_PLAYLIST_DOWNLOAD) key = SP_KEY_DATA_TIMESTAMP+"_PLAY";
        if(idx == Definer.ORDER_FORMAT_DOWNLOAD) key = SP_KEY_DATA_TIMESTAMP+"_FORMAT";
        if(idx == Definer.ORDER_CONFIG_DOWNLOAD) key = SP_KEY_DATA_TIMESTAMP+"_CONFIG";
        if(idx == Definer.ORDER_CONTROL_DOWNLOAD) key = SP_KEY_DATA_TIMESTAMP+"_CONTROL";

        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(
                SP_KEY_DATA_TIMESTAMP+ key , DEFAULT_TIMESTAMP);
    }


    // Sets the timestamp of the data we have in the content provider.
    public static void setDataTimestamp(Context ctx, String timestamp, int idx) {

        String key="";

        if(idx == Definer.ORDER_PLAYLIST_DOWNLOAD) key = SP_KEY_DATA_TIMESTAMP+"_PLAY";
        if(idx == Definer.ORDER_FORMAT_DOWNLOAD) key = SP_KEY_DATA_TIMESTAMP+"_FORMAT";
        if(idx == Definer.ORDER_CONFIG_DOWNLOAD) key = SP_KEY_DATA_TIMESTAMP+"_CONFIG";
        if(idx == Definer.ORDER_CONTROL_DOWNLOAD) key = SP_KEY_DATA_TIMESTAMP+"_CONTROL";

        Log.d(TAG, "Setting data timestamp to: " + timestamp);
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(
                SP_KEY_DATA_TIMESTAMP+ key, timestamp).apply();
    }

    // Reset the timestamp of the data we have in the content provider
    public static void resetDataTimestamp(final Context context) {
        Log.d(TAG, "Resetting data timestamp to default (to invalidate our synced data)");
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(
                SP_KEY_DATA_TIMESTAMP).apply();
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
