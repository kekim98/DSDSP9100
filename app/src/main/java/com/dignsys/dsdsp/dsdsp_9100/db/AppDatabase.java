/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.dignsys.dsdsp.dsdsp_9100.db.converter.DateConverter;
import com.dignsys.dsdsp.dsdsp_9100.db.dao.ConfigDao;
import com.dignsys.dsdsp.dsdsp_9100.db.dao.DspFormatDao;
import com.dignsys.dsdsp.dsdsp_9100.db.dao.DspPlayListDao;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.DspFormatEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.DspPlayListEntity;


@Database(entities = {ConfigEntity.class, DspFormatEntity.class, DspPlayListEntity.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "dsdsp-9100-db";

    public abstract ConfigDao configDao();

    public abstract DspPlayListDao dspPlayListDao();

    public abstract DspFormatDao dspFormatDao();
}
