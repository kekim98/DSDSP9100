/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;
import com.dignsys.dsdsp.dsdsp_9100.service.SyncService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleViewModel extends AndroidViewModel {

    TimerTask mTimerTask;
    Timer mTimer;
    int mTickCount =0;

    private  final int DSDSP_TICK = 1 * 1000;

    private static final String TAG = ScheduleViewModel.class.getSimpleName();


   // private final LiveData<SceneEntity> mScene;

   // private final LiveData<List<PaneEntity>> mPaneList;
   // private final LiveData<List<ContentEntity>> mContentList;
   // private final MutableLiveData<Integer> mScheduleDone;
   // private final MutableLiveData<Integer> mContentPlayDone;
   // private LiveData<ConfigEntity> mConfig;
   // private MutableLiveData<Integer> mPlayStart;

    //private final LiveData<List<ContentEntity>> mContentList;



    public ScheduleViewModel(@NonNull final Application application){
        super(application);

        mTimer = new Timer();
        runDSPTimer();


      //  mScene = ScheduleHelper.getInstance(application.getApplicationContext()).getScene();
      //  mPaneList = ScheduleHelper.getInstance(application.getApplicationContext()).getPaneList();

      //  mContentList = ScheduleHelper.getInstance(application.getApplicationContext()).getContentList();

     //   mScheduleDone = ScheduleHelper.getInstance(application.getApplicationContext()).getScheduleDone();

     //   mContentPlayDone = ScheduleHelper.getInstance(application.getApplicationContext()).getContentPlayDone();
     //   mConfig = ScheduleHelper.getInstance(application.getApplicationContext()).getConfig();
     //   mPlayStart = ScheduleHelper.getInstance(application.getApplicationContext()).getPlayStart();

    }
    /**
     * Expose the LiveData Schedule ID so the UI can observe it.
     */

   // public LiveData<List<PaneEntity>> getNextPaneList() { return mPaneList;}

   // public LiveData<SceneEntity> getScene(){ return  mScene;}

    public List<PaneEntity> getPaneList() { return  ScheduleHelper.getInstance(this.getApplication().getApplicationContext()).getPaneList();}

    public LiveData<Integer> getPlayStart() { return ScheduleHelper.getInstance(this.getApplication().getApplicationContext()).getPlayStart();}

    //public LiveData<List<ContentEntity>> getContentList() { return mContentList;}

  //  public MutableLiveData<Integer> getScheduleDone() { return mScheduleDone;}

    public ContentEntity getContent(int paneNum) {
        return ScheduleHelper.getInstance(this.getApplication().getApplicationContext()).getContent(paneNum);
    }

   // public LiveData<Integer> getContentPlayDone() { return mContentPlayDone; }

  /*  public void requestNextScene() {
        ScheduleHelper.getInstance(this.getApplication().getApplicationContext()).requestNextScene();
    }*/

    public LiveData<ConfigEntity> getConfig() {
        return ScheduleHelper.getInstance(this.getApplication().getApplicationContext()).getConfig();
    }

    public LiveData<Integer> getContentPlayDone() {
        return ScheduleHelper.getInstance(this.getApplication().getApplicationContext()).getContentPlayDone();
    }


    /**
     * A creator is used to inject the dspFormat ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the dspFormat ID can be passed in a public method.
     */
    /*
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mDspFormatId;

        public Factory(@NonNull Application application, int dspFormatId) {
            mApplication = application;
            mDspFormatId = dspFormatId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ScheduleViewModel(mApplication, mDspFormatId);
        }
    }*/


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
                    SyncService.startDownload(ScheduleViewModel.this.getApplication(), null, null);
                }

                //for playlist schedule
                ScheduleHelper.getInstance(ScheduleViewModel.this.getApplication()).updateScheduleTick();
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
