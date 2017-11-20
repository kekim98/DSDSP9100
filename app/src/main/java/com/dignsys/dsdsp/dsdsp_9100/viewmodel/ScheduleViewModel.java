/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;


import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.dignsys.dsdsp.dsdsp_9100.Definer.SCHEDULE_PERIOD;

public class ScheduleViewModel extends AndroidViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();
    private static final String TAG = ScheduleViewModel.class.getSimpleName();

    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private final LiveData<List<PaneEntity>> mObservableDspFormatList;

    private final LiveData<List<ContentEntity>> mObservableDspPlayList;

    private final TimerTask mTask;
    private final Timer mTimer;

    public ScheduleViewModel(@NonNull Application application){
        super(application);

        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());

        mObservableDspPlayList = Transformations.switchMap(databaseCreator.isDatabaseCreated(), new Function<Boolean, LiveData<List<ContentEntity>>>() {
            @Override
            public LiveData<List<ContentEntity>> apply(Boolean isDbCreated) {
                if (!isDbCreated) {
                    //noinspection unchecked
                    return ABSENT;
                } else {
                    //noinspection ConstantConditions
                   // return databaseCreator.getDatabase().dspPlayListDao().loadAllPlayList();
                    return ABSENT;
                }
            }
        });

        mObservableDspFormatList = Transformations.switchMap(databaseCreator.isDatabaseCreated(), new Function<Boolean, LiveData<List<PaneEntity>>>() {
            @Override
            public LiveData<List<PaneEntity>> apply(Boolean isDbCreated) {
                if (!isDbCreated) {
                    //noinspection unchecked
                    return ABSENT;
                } else {
                    //noinspection ConstantConditions
                  //  return databaseCreator.getDatabase().dspFormatDao().loadAllDspFormat();
                    return ABSENT;
                }
            }
        });

        databaseCreator.createDb(this.getApplication());

        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "running timer task........");
               // procScen();
            }
        };
        mTimer.schedule(mTask, 0, SCHEDULE_PERIOD);

    }
    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<List<ContentEntity>> getPlayList() {
        return mObservableDspPlayList;
    }

    public LiveData<List<PaneEntity>> getFormatList() {
        return mObservableDspFormatList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    /* *//**
     * A creator is used to inject the dspFormat ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the dspFormat ID can be passed in a public method.
     *//*
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
}
