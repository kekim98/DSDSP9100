package com.dignsys.dsdsp.dsdsp_9100.service;

import android.app.IntentService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.util.DaulUtils;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.CommandHelper;

import java.io.File;
import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SyncService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_DOWNLOAD = "com.dignsys.dsdsp.service.action.DOWNLOAD";
    private static final String ACTION_UMS_SYNC = "com.dignsys.dsdsp.service.action.UMS_SYNC";
    private static final String ACTION_UMS_COPY = "com.dignsys.dsdsp.service.action.UMS_COPY";
    private static final String ACTION_DELETE_ALL = "com.dignsys.dsdsp.service.action.DELETE_ALL";
    private static final String ACTION_SD_FORMAT = "com.dignsys.dsdsp.service.action.SD_FORMAT";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.dignsys.dsdsp.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.dignsys.dsdsp.service.extra.PARAM2";
    private static final String TAG = SyncService.class.getSimpleName();

    public SyncService() {
        super("SyncService");
    }

    /**
     * Starts this service to perform action DOWNLOAD with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startDownload(Context context, String param1, String param2) {
        Log.d(TAG, "startDownload: ..............................");
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startUmsSync(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_UMS_SYNC);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startUmsCopy(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_UMS_COPY);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startDeletAll(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_DELETE_ALL);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startSDFormat(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_SD_FORMAT);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: xxxxxxxxxxxxxxxxxxxxxxxx");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionDownload(param1, param2);
            } else if (ACTION_UMS_SYNC.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionUmsSync(param1, param2);
            }else if (ACTION_UMS_COPY.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionUmsCopy(param1, param2);
            } else if (ACTION_DELETE_ALL.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionDeleteAll(param1, param2);
            }else if (ACTION_SD_FORMAT.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionSDFormat(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */



    private void handleActionDownload(String param1, String param2) {
        // TODO: Handle action Download
        Log.d(TAG, "handleActionDownload: ");

        SyncHelper syncHelper = new SyncHelper(this);
        syncHelper.performSync(null, Definer.SYNC_PLAY_ONLY);

    }



    private void handleActionUmsCopy(String param1, String param2) {


            try {
                File fRoot = new File(Definer.DEF_ROOT_PATH);
                File fUMS = new File(Definer.DEF_UMS_PATH);

                String strSRCs[] = fUMS.list();

                for (String strSRC: strSRCs) {

                    File fSRC = new File(fUMS, strSRC);

                    if(!fSRC.isDirectory())	{

                        String strDest = fRoot.getAbsolutePath() + File.separator + fSRC.getName();

                        DaulUtils.copyFile(fSRC, strDest);

                    }

                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }finally {
                MutableLiveData<Boolean> isSyncDone = CommandHelper.getInstance(this).isSyncDone();
                isSyncDone.postValue(true);
            }

    }


    private void handleActionUmsSync(String param1, String param2) {

        try {

            File fRoot = new File(Definer.DEF_ROOT_PATH);
            File fUMS = new File(Definer.DEF_UMS_PATH);

            String strSRCs[] = fUMS.list();


            DaulUtils.deleteDirectory(fRoot);
            fRoot.mkdirs();

            for (String strSRC : strSRCs) {

                File fSRC = new File(fUMS, strSRC);

                if (!fSRC.isDirectory()) {

                    String strDest = fRoot.getAbsolutePath() + File.separator + fSRC.getName();

                    DaulUtils.copyFile(fSRC, strDest);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MutableLiveData<Boolean> isSyncDone = CommandHelper.getInstance(this).isSyncDone();
            isSyncDone.postValue(true);
        }
    }

    private void handleActionDeleteAll(String param1, String param2) {
        try {
            DaulUtils.deleteFilesInDirectory(Definer.DEF_ROOT_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            MutableLiveData<Boolean> isSyncDone = CommandHelper.getInstance(this).isSyncDone();
            isSyncDone.postValue(true);
        }


    }

    private void handleActionSDFormat(String param1, String param2) {

        final String 	preCmd = 	"sm unmount public:179,1";
        final String	cmd = 		"sm format public:179,1";
        final String	postCmd = 	"sm mount public:179,1";

        Process p = null;
        try {
            p = Runtime.getRuntime().exec(preCmd);
            int result = p.waitFor();
            if (result != 0) {
            //    Toast.makeText(this, "SD카드가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
           // Toast.makeText(this, "SD카드가 포맷되었습니다", Toast.LENGTH_SHORT).show();



            p = Runtime.getRuntime().exec(postCmd);
            p.waitFor();
            //	Toast.makeText(this, "SD카드가 포맷되었습니다", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            MutableLiveData<Boolean> isSyncDone = CommandHelper.getInstance(this).isSyncDone();
            isSyncDone.postValue(true);
        }

    }

}
