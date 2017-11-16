/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import com.dignsys.dsdsp.dsdsp_9100.db.entity.DspPlayListEntity;

import java.util.List;

@Dao
public interface DspPlayListDao {
    @Query("SELECT * FROM dsp_play_list where productId = :productId")
    LiveData<List<DspPlayListEntity>> loadComments(int productId);

    @Query("SELECT * FROM dsp_play_list where productId = :productId")
    List<DspPlayListEntity> loadCommentsSync(int productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DspPlayListEntity> products);
}
