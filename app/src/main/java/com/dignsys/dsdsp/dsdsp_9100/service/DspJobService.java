package com.dignsys.dsdsp.dsdsp_9100.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.text.TextUtils;
import android.util.Log;

import com.dignsys.dsdsp.dsdsp_9100.Definer;


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

        String value = params.getExtras().getString(Definer.DEF_DSP_JOB_KEY);
        Log.i(TAG, "onStartJob:value= " + value);

        if (!TextUtils.isEmpty(value) && value.equals(Definer.DEF_FW_UPGRADE_VALUE)) {

            final String file = params.getExtras().getString(Definer.DEF_VALUE_SUB_KEY);

            final String upgradeCmd = "pm install -r -d " + file;

            Log.e(TAG, "onStartJob: ....................................." );
            Process p = null;
            try {
                p = Runtime.getRuntime().exec(upgradeCmd);
                p.waitFor();

                //	Toast.makeText(this, "SD카드가 포맷되었습니다", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;

        }

        if (!TextUtils.isEmpty(value) && value.equals(Definer.DEF_WATCH_DOG_VALUE)) {

            final String checkDSDSPStatus = "ps | grep 'com.dignsys.dsdsp.dsdsp_9100";
            final String startCmd = "am start -a android.intent.action.MAIN -n com.dignsys.dsdsp.dsdsp_9100/.ui.main.MainActivity";
            Process p = null;
            try {
                p = Runtime.getRuntime().exec(checkDSDSPStatus);
                p.waitFor();
                int exitStatus = p.exitValue();

                if (exitStatus != 0) {
                    p = Runtime.getRuntime().exec(startCmd);
                    p.waitFor();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;


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

      /*  // Stop tracking these job parameters, as we've 'finished' executing.
        sendMessage(MSG_COLOR_STOP, params.getJobId());
        Log.i(TAG, "on stop job: " + params.getJobId());
*/
        // Return false to drop the job.
        Log.i(TAG, "onStopJob:........................... " + params.getJobId());
        return false;
    }


}
