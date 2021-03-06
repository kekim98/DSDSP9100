package com.dignsys.dsdsp.dsdsp_9100.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;

import java.util.List;

/**
 * Created by bawoori on 17. 11. 20.
 */

@Dao
public interface ContentDao {
    @Query("SELECT * FROM content_info")
    LiveData<List<ContentEntity>> loadAllContent();

    @Insert
    void insertAll(List<ContentEntity> contents);

    @Query("select * from content_info where id = :contentId")
    LiveData<ContentEntity> loadContentById(int contentId);

    @Query("DELETE FROM content_info")
    void deleteAllContent();


    @Query("SELECT * FROM content_info WHERE scene_id = :sceneId AND opNotPlay != 1  AND fileType IN(:types) order by id")
    LiveData<List<ContentEntity>> loadContentListById(Integer sceneId, int[] types);

}
