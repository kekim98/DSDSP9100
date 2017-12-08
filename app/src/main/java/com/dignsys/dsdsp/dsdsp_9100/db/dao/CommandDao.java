package com.dignsys.dsdsp.dsdsp_9100.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dignsys.dsdsp.dsdsp_9100.db.entity.CommandEntity;

/**
 * Created by bawoori on 17. 11. 16.
 */

@Dao
public interface CommandDao {

    @Query("SELECT * FROM command_info LIMIT 1")
    LiveData<CommandEntity> loadCommand();

    @Query("SELECT * FROM command_info LIMIT 1")
    CommandEntity loadCommandSync();


   @Query("DELETE FROM command_info")
    void deleteAll();

    @Insert
    void insertOne(CommandEntity CommandEntity);

    @Update
    void update(CommandEntity command);
}
