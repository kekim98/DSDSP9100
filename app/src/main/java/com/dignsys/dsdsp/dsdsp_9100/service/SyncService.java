package com.dignsys.dsdsp.dsdsp_9100.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dignsys.dsdsp.dsdsp_9100.Definer;

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
    private static final String ACTION_BAZ = "com.dignsys.dsdsp.service.action.BAZ";

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
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_BAZ);
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
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDownload(String param1, String param2) {
        // TODO: Handle action Download
        Log.d(TAG, "handleActionDownload: KKKKKKKKKKKKKKKKKKKk");

        SyncHelper syncHelper = new SyncHelper(this);
        syncHelper.performSync(null,Definer.SYNC_PLAY_ONLY);

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
