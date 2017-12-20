/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dignsys.dsdsp.dsdsp_9100.db.entity.RssEntity;

@Dao
public interface RssDao {

    @Query("SELECT * FROM rss_info LIMIT 1")
    RssEntity loadRssSync();

    @Update
    void update(RssEntity rssEntity);

    @Insert
    void insert(RssEntity rssEntity);

    @Query("SELECT * FROM rss_info LIMIT 1")
    LiveData<RssEntity> loadRss();
}
