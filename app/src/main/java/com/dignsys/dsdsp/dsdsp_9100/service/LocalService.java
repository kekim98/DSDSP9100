package com.dignsys.dsdsp.dsdsp_9100.service;

import android.app.Service;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bawoori on 17. 11. 23.
 */

public class LocalService extends Service {
    private  final int DSDSP_TICK = 1 * 1000;
    // Binder given to clients
   final IBinder mBinder = new LocalBinder();

    // PlayData Scheduler
    TimerTask mTimerTask;
    Timer mTimer;
    int mTickCount =0;



    private final MutableLiveData<Integer> mLiveTick = new MutableLiveData<>();


    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        runDSPTimer();



    }

    private void runDSPTimer() {
        if (mTimerTask != null) {
            mTimer.cancel();
        }

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mTickCount++;
                mLiveTick.postValue(mTickCount);

                //for play data download
                if ((mTickCount*DSDSP_TICK % getPlayDataDownloadInterval()) == 0) {
                    SyncService.startDownload(LocalService.this, null, null);
                }

                //for playlist schedule
                ScheduleHelper.getInstance(LocalService.this.getApplicationContext()).updateScheduleTick();
            }
        };

        mTimer.schedule(mTimerTask, 0, DSDSP_TICK);
    }

    private long getPlayDataDownloadInterval() {

        //TODO : have to change
        return 6*1000;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopDSPTimer();

    }

    private void stopDSPTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimer.cancel();

            mTimer = null;
            mTimerTask = null;

        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public MutableLiveData<Integer> getLiveTick() {
        return mLiveTick;
    }

}