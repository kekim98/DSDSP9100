package com.dignsys.dsdsp.dsdsp_9100;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseInitUtil;

/**
 * Created by bawoori on 17. 11. 15.
 */

public class DSDSPApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate() {
        super.onCreate();

        final AppDatabase db = DatabaseCreator.getInstance(this);

        new AsyncTask<Context, Void, Void>() {

            @Override
            protected Void doInBackground(Context... params) {
                if(db.configDao().configCount() == 0) DatabaseInitUtil.initializeDb(db);
                return null;
            }


        }.execute(this);

      //  CommandHelper.startWatchDog(this);


    }

}
