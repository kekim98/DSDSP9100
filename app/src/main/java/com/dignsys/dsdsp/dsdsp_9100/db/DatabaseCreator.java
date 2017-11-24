/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import static com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase.DATABASE_NAME;


/**
 */
public class DatabaseCreator {

    private static AppDatabase sInstance;


    // For Singleton instantiation
    private static final Object LOCK = new Object();

    public synchronized static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = // Build the database!
                            Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

}
