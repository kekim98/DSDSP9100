/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dignsys.dsdsp.dsdsp_9100.service.ScheduleHelper;

public class ScheduleViewModel extends AndroidViewModel {

    private static final String TAG = ScheduleViewModel.class.getSimpleName();


    private final LiveData<Integer> mCurrScheduleId;




    public ScheduleViewModel(@NonNull Application application){
        super(application);

        /*final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());
        databaseCreator.createDb(this.getApplication());
*/
        mCurrScheduleId = ScheduleHelper.getInstance(application.getApplicationContext()).getCurrSchduleId();



    }
    /**
     * Expose the LiveData Schedule ID so the UI can observe it.
     */
    public LiveData<Integer> getCurrentScheduleId() {
        return mCurrScheduleId;
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
