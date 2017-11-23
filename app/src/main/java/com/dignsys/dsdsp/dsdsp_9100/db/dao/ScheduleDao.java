/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ScheduleEntity;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Query("SELECT * FROM schedule_info")
    LiveData<List<ScheduleEntity>> loadAllSchedule();

    @Insert
    void insertAll(List<ScheduleEntity> schedules);

    @Query("DELETE FROM schedule_info")
    void deleteAllSchedule();

}
