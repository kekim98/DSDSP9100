package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;


/**
 * Created by bawoori on 17. 11. 16.
 */

public class ConfigViewModel extends AndroidViewModel {
    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private final LiveData<ConfigEntity> mObservableConfig;

    public ConfigViewModel(@NonNull Application application) {
        super(application);

        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());
        mObservableConfig = Transformations.switchMap(databaseCreator.isDatabaseCreated(), new Function<Boolean, LiveData<ConfigEntity>>() {
            @Override
            public LiveData<ConfigEntity> apply(Boolean isDbCreated) {
                if (!isDbCreated) {
                    //noinspection unchecked
                    return ABSENT;
                } else {
                    //noinspection ConstantConditions
                    return databaseCreator.getDatabase().configDao().loadConfig();
                }
            }
        });

        databaseCreator.createDb(this.getApplication());
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<ConfigEntity> getConfig() {
        return mObservableConfig;
    }
}
