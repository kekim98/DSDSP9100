/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.service.SyncService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainViewModel extends AndroidViewModel {

    TimerTask mTimerTask;
    Timer mTimer;
    int mTickCount =0;

    private  final int DSDSP_TICK = 1 * 1000;

    private static final String TAG = MainViewModel.class.getSimpleName();

    private final ScheduleHelper mScheduleHelper = ScheduleHelper.getInstance(this.getApplication());

    private final CommandHelper mCommandHelper = CommandHelper.getInstance(this.getApplication());
    private ConfigHelper mConfigHelper = ConfigHelper.getInstance(this.getApplication());


    public MainViewModel(@NonNull final Application application){
        super(application);

        mTimer = new Timer();
        runDSPTimer();


    }
    /**
     * Expose the LiveData Schedule ID so the UI can observe it.
     */



    public List<PaneEntity> getPaneList() { return  mScheduleHelper.getPaneList();}

    public LiveData<Integer> getPlayCommand() { return mScheduleHelper.getPlayCommand();}


    public ContentEntity getContent(int paneNum) {
        return mScheduleHelper.getContent(paneNum);
    }


    public ConfigHelper getConfigHelper() {
        return mConfigHelper;
    }

    public LiveData<ConfigEntity> getConfig(){ return mConfigHelper.getConfig();}

    public LiveData<Integer> getContentPlayDone() {
        return mScheduleHelper.getContentPlayDone();
    }
    public CommandHelper getCommandHelper() { return mCommandHelper;}



    /**
     * A creator is used to inject the dspFormat ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the dspFormat ID can be passed in a public method.
     */



    private void runDSPTimer() {
        if (mTimerTask != null) {
            mTimer.cancel();
        }

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mTickCount++;
                //   mLiveTick.postValue(mTickCount);

                //for play data download
                if ((mTickCount*DSDSP_TICK % getPlayDataDownloadInterval()) == 0) {
                    SyncService.startDownload(MainViewModel.this.getApplication(), null, null);
                }

                //for playlist schedule
                mScheduleHelper.updateScheduleTick();
            }
        };

        mTimer.schedule(mTimerTask, 0, DSDSP_TICK);
    }

    private long getPlayDataDownloadInterval() {

        //TODO : have to change
        return 6*1000;
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
    protected void onCleared() {
        super.onCleared();
        stopDSPTimer();
    }



}
