package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.CommandEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.service.SyncService;

import java.io.File;
import java.util.TimeZone;


/**
 * Created by bawoori on 17. 11. 24.
 */

public class LogHelper {

    private static final String TAG = LogHelper.class.getSimpleName();
    private static LogHelper sInstance;

    // private static final MutableLiveData ABSENT = new MutableLiveData();

    private final MutableLiveData<Integer> mPlayCommand = new MutableLiveData<>();

    private Context _context;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private ConfigHelper mConfig;

    private LogHelper(Context context) {
        _context = context;
        mConfig = ConfigHelper.getInstance(_context);
    }

    public synchronized static LogHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new LogHelper(context);
                }
            }
        }
        return sInstance;
    }


}

    