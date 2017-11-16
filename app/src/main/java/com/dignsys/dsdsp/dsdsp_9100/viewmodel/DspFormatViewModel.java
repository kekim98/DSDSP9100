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
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;


import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.DspFormatEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.DspPlayListEntity;

import java.util.List;

public class DspFormatViewModel extends AndroidViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private final LiveData<DspFormatEntity> mObservableDspFormat;

    public ObservableField<DspFormatEntity> dspFormat = new ObservableField<>();

    private final int mDspFormatId;

    private final LiveData<List<DspPlayListEntity>> mObservableDspPlayList;

    public DspFormatViewModel(@NonNull Application application,
                              final int dspFormatId) {
        super(application);
        mDspFormatId = dspFormatId;

        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());

        mObservableDspPlayList = Transformations.switchMap(databaseCreator.isDatabaseCreated(), new Function<Boolean, LiveData<List<DspPlayListEntity>>>() {
            @Override
            public LiveData<List<DspPlayListEntity>> apply(Boolean isDbCreated) {
                if (!isDbCreated) {
                    //noinspection unchecked
                    return ABSENT;
                } else {
                    //noinspection ConstantConditions
                    return databaseCreator.getDatabase().dspPlayListDao().loadComments(mDspFormatId);
                }
            }
        });

        mObservableDspFormat = Transformations.switchMap(databaseCreator.isDatabaseCreated(), new Function<Boolean, LiveData<DspFormatEntity>>() {
            @Override
            public LiveData<DspFormatEntity> apply(Boolean isDbCreated) {
                if (!isDbCreated) {
                    //noinspection unchecked
                    return ABSENT;
                } else {
                    //noinspection ConstantConditions
                    return databaseCreator.getDatabase().dspFormatDao().loadDspFormat(mDspFormatId);
                }
            }
        });

        databaseCreator.createDb(this.getApplication());

    }
    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<List<DspPlayListEntity>> getComments() {
        return mObservableDspPlayList;
    }

    public LiveData<DspFormatEntity> getObservableProduct() {
        return mObservableDspFormat;
    }

    public void setDspFormat(DspFormatEntity dspFormat) {
        this.dspFormat.set(dspFormat);
    }

    /**
     * A creator is used to inject the dspFormat ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the dspFormat ID can be passed in a public method.
     */
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
            return (T) new DspFormatViewModel(mApplication, mDspFormatId);
        }
    }
}
