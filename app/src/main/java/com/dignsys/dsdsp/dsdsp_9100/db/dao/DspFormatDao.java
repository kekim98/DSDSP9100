/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import com.dignsys.dsdsp.dsdsp_9100.db.entity.DspFormatEntity;

import java.util.List;

@Dao
public interface DspFormatDao {
    @Query("SELECT * FROM dsp_format")
    LiveData<List<DspFormatEntity>> loadAllDspFormats();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DspFormatEntity> dspFormats);

    @Query("select * from dsp_format where id = :dspFormatId")
    LiveData<DspFormatEntity> loadDspFormat(int dspFormatId);

    @Query("select * from dsp_format where id = :dspFormatId")
    DspFormatEntity loadDspFormatSync(int dspFormatId);
}
