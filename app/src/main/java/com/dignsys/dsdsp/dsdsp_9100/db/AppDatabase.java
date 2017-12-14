/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.TypeConverters;

import com.dignsys.dsdsp.dsdsp_9100.db.converter.DateConverter;
import com.dignsys.dsdsp.dsdsp_9100.db.dao.CommandDao;
import com.dignsys.dsdsp.dsdsp_9100.db.dao.ConfigDao;
import com.dignsys.dsdsp.dsdsp_9100.db.dao.ContentDao;
import com.dignsys.dsdsp.dsdsp_9100.db.dao.PaneDao;
import com.dignsys.dsdsp.dsdsp_9100.db.dao.RssDao;
import com.dignsys.dsdsp.dsdsp_9100.db.dao.SceneDao;
import com.dignsys.dsdsp.dsdsp_9100.db.dao.ScheduleDao;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.CommandEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.RssEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ScheduleEntity;

import java.util.List;


@Database(entities = {ConfigEntity.class, CommandEntity.class, RssEntity.class, ScheduleEntity.class, SceneEntity.class,
        PaneEntity.class, ContentEntity.class, }, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "dsdsp-9100-db";

    public abstract ConfigDao configDao();

    public abstract CommandDao commandDao();

    public abstract RssDao rssDao();

    public abstract ScheduleDao scheduleDao();

    public abstract SceneDao sceneDao();

    public abstract PaneDao paneDao();

    public abstract ContentDao contentDao();

    @Transaction
    public void updatePlayDataTransaction(List<ScheduleEntity> scheduls,
                                          List<SceneEntity> scenes,
                                          List<PaneEntity> panes,
                                          List<ContentEntity> contents)
    {
        // Anything inside this method runs in a single transaction.
        scheduleDao().deleteAllSchedule();
        sceneDao().deleteAllScene();
        paneDao().deleteAllPane();
        contentDao().deleteAllContent();

        scheduleDao().insertAll(scheduls);
        sceneDao().insertAll(scenes);
        paneDao().insertAll(panes);
        contentDao().insertAll(contents);
    }

    @Transaction
    public void updateCommandTransaction(CommandEntity commandEntity) {
        commandDao().deleteAll();
        commandDao().insertOne(commandEntity);
    }

    @Transaction
    public void deletePlayDataTransaction() {
        scheduleDao().deleteAllSchedule();
        sceneDao().deleteAllScene();
        paneDao().deleteAllPane();
        contentDao().deleteAllContent();
        commandDao().deleteAll();
    }
}
