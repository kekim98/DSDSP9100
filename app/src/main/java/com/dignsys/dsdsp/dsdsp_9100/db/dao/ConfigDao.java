package com.dignsys.dsdsp.dsdsp_9100.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;

/**
 * Created by bawoori on 17. 11. 16.
 */

@Dao
public interface ConfigDao {

    @Query("SELECT * FROM config_info LIMIT 1")
    LiveData<ConfigEntity> loadConfig();

    @Query("SELECT * FROM config_info LIMIT 1")
    ConfigEntity loadConfigSync();


    @Query("SELECT count(*) from config_info")
    int configCount();


    /**
     * Update the cheese. The cheese is identified by the row ID.
     *
     * @param config The config to update.
     * @return A number of cheeses updated. This should always be {@code 1}.
     */

    @Update
    int update(ConfigEntity config);

    @Insert
    void insertOne(ConfigEntity configEntity);


}
