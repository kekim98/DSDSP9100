package com.dignsys.dsdsp.dsdsp_9100.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;

import java.util.List;

/**
 * Created by bawoori on 17. 11. 20.
 */

@Dao
public interface PaneDao {
    @Query("SELECT * FROM pane_info")
    LiveData<List<PaneEntity>> loadAllPane();

    @Insert
    void insertAll(List<PaneEntity> panes);

    @Query("select * from pane_info where scene_num = :scene_num AND pane_num = :pane_num")
    LiveData<PaneEntity> loadSceneById(int scene_num, int pane_num );
}
