/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;

import java.util.List;

public class ScheduleViewModel extends AndroidViewModel {



    private static final String TAG = ScheduleViewModel.class.getSimpleName();


    private final LiveData<SceneEntity> mScene;

    private final LiveData<List<PaneEntity>> mPaneList;
    private final LiveData<List<ContentEntity>> mContentList;
    private final MutableLiveData<Integer> mScheduleDone;

    //private final LiveData<List<ContentEntity>> mContentList;



    public ScheduleViewModel(@NonNull final Application application){
        super(application);


        mScene = ScheduleHelper.getInstance(application.getApplicationContext()).getScene();
        mPaneList = ScheduleHelper.getInstance(application.getApplicationContext()).getPaneList();

        mContentList = ScheduleHelper.getInstance(application.getApplicationContext()).getContentList();

        mScheduleDone = ScheduleHelper.getInstance(application.getApplicationContext()).getScheduleDone();

    }
    /**
     * Expose the LiveData Schedule ID so the UI can observe it.
     */

    public LiveData<List<PaneEntity>> getNextPaneList() { return mPaneList;}

    public LiveData<SceneEntity> getScene(){ return  mScene;}

    public LiveData<List<PaneEntity>> getPaneList() { return  mPaneList;}

    public LiveData<List<ContentEntity>> getContentList() { return mContentList;}

    public MutableLiveData<Integer> getScheduleDone() { return mScheduleDone;}

    public ContentEntity getContent(int paneNum) {
        return ScheduleHelper.getInstance(this.getApplication().getApplicationContext()).getContent(paneNum);
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
}
