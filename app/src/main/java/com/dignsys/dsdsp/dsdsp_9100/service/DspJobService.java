package com.dignsys.dsdsp.dsdsp_9100.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;


/**
 * Service to handle callbacks from the JobScheduler. Requests scheduled with the JobScheduler
 * ultimately land on this service's "onStartJob" method. It runs jobs for a specific amount of time
 * and finishes them. It keeps the activity updated with changes via a Messenger.
 */
public class DspJobService extends JobService {

    private static final String TAG = DspJobService.class.getSimpleName();

    // private Messenger mActivityMessenger;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    /**
     * When the app's MainActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCallback()"
     */
   /* @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //    mActivityMessenger = intent.getParcelableExtra(MESSENGER_INTENT_KEY);
        return START_NOT_STICKY;
    }*/

    @Override
    public boolean onStartJob(final JobParameters params) {


        final String pmCmd = "pm install -r -d /mnt/external_sd/test.apk";
        final String startCmd = "am start -a android.intent.action.MAIN -n com.dignsys.dsdsp.dsdsp_9100/.ui.main.MainActivity";

        Log.e(TAG, "onStartJob: ....................................." );
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(pmCmd);
            p.waitFor();

            p = Runtime.getRuntime().exec(startCmd);
            p.waitFor();

            //	Toast.makeText(this, "SD카드가 포맷되었습니다", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // The work that this service "does" is simply wait for a certain duration and finish
        // the job (on another thread).

      /*  sendMessage(MSG_COLOR_START, params.getJobId());

        long duration = params.getExtras().getLong(WORK_DURATION_KEY);

        // Uses a handler to delay the execution of jobFinished().
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMessage(MSG_COLOR_STOP, params.getJobId());
                jobFinished(params, false);
            }
        }, duration);
        Log.i(TAG, "on start job: " + params.getJobId());*/

        // Return true as there's more work to be done with this job.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "onStopJob: ....................................." );
        Process p = null;

        final String startCmd = "am start -a android.intent.action.MAIN -n com.dignsys.dsdsp.dsdsp_9100/.ui.main.MainActivity";

        try {
            p = Runtime.getRuntime().exec(startCmd);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  // Stop tracking these job parameters, as we've 'finished' executing.
        sendMessage(MSG_COLOR_STOP, params.getJobId());
        Log.i(TAG, "on stop job: " + params.getJobId());
*/
        // Return false to drop the job.
        return false;
    }

    /*private void sendMessage(int messageID, @Nullable Object params) {
        // If this service is launched by the JobScheduler, there's no callback Messenger. It
        // only exists when the MainActivity calls startService() with the callback in the Intent.
        if (mActivityMessenger == null) {
            Log.d(TAG, "Service is bound, not started. There's no callback to send a message to.");
            return;
        }
        Message m = Message.obtain();
        m.what = messageID;
        m.obj = params;
        try {
            mActivityMessenger.send(m);
        } catch (RemoteException e) {
            Log.e(TAG, "Error passing service object back to activity.");
        }
    }*/
}
