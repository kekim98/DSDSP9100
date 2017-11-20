/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;

import java.util.List;

@Dao
public interface SceneDao {

    @Query("SELECT * FROM scene_info")
    LiveData<List<SceneEntity>> loadAllScene();

    @Insert
    void insertAll(List<SceneEntity> scenes);

    @Query("select * from scene_info where scene_num = :sceneId")
    LiveData<SceneEntity> loadSceneById(int sceneId);


}
