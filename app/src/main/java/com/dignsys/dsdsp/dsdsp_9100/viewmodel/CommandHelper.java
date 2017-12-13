package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.CommandEntity;
import com.dignsys.dsdsp.dsdsp_9100.service.DspJobService;
import com.dignsys.dsdsp.dsdsp_9100.service.SyncService;

import java.util.TimeZone;


/**
 * Created by bawoori on 17. 11. 24.
 */

public class CommandHelper {

    private static final String TAG = CommandHelper.class.getSimpleName();
    private static CommandHelper sInstance;

    // private static final MutableLiveData ABSENT = new MutableLiveData();
    private final LiveData<CommandEntity> mCommand;

    private final MutableLiveData<Boolean> mIsSyncDone = new MutableLiveData<>();


    private Context _context;

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    private CommandHelper(Context context) {
        _context = context;

        AppDatabase db = DatabaseCreator.getInstance(context);
        mCommand = db.commandDao().loadCommand();
       


        // Create the observer which updates the schedule list .
        final Observer<CommandEntity> commandObserver = new Observer<CommandEntity>() {
            @Override
            public void onChanged(@Nullable final CommandEntity command) {
                if (command != null) {
                    processCommand(command);
                }

            }
        };
        mCommand.observeForever(commandObserver);


    }

    private void processCommand(CommandEntity command) {

        if (command.getFontFilePath() != null) {
            Log.d(TAG, "processCommand: font proc command...");
        }

        if (command.getFwFilePath() != null) {
            Log.d(TAG, "processCommand: firmware proc command...");
        }

        if (command.isReboot()) {
            Log.d(TAG, "processCommand: isReboot proc command...");
        }

        if (command.isPowerOff()) {
            Log.d(TAG, "processCommand: isPowerOff proc command...");
        }

        if (command.isPowerOn()) {
            Log.d(TAG, "processCommand: isPowerOn proc command...");
        }

        if (command.isSdFormat()) {
            Log.d(TAG, "processCommand: isSdFormat proc command...");
        }

        if (command.isSdDelete()) {
            Log.d(TAG, "processCommand: isSdDelete proc command...");
        }

        if (command.isUsbSync()) {
            Log.d(TAG, "processCommand: isUsbSync proc command...");
        }

        if (command.isUsbCopy()) {
            Log.d(TAG, "processCommand: isUsbCopy proc command...");
        }


    }



    public synchronized static CommandHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new CommandHelper(context);
                }
            }
        }
        return sInstance;
    }

    public void setScreenRotation(int mode) {
        if(mode >= 0 && mode < 4) {
            Display display = ((WindowManager) _context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            if(display.getRotation() == mode) return;

            // You can get ContentResolver from the Context
            Settings.System.putInt(_context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
            Settings.System.putInt(_context.getContentResolver(), Settings.System.USER_ROTATION, mode);

        }else{
            Log.e(TAG, "setScreenMode: not support mode");

        }

    }

    public void setTimeZone(String zone) {
        if(zone.equals(TimeZone.getDefault().getID())) return;

        AlarmManager alarm = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
        alarm.setTimeZone(zone);
    }

    public void umsSync() {
        SyncService.startUmsSync(_context, null, null );
    }

    public void umsCopy() {
        SyncService.startUmsCopy(_context, null, null );
    }

    public void sdFormat() {
        SyncService.startSDFormat(_context, null, null );
    }

    public void deleteAll() {
        SyncService.startDeletAll(_context, null, null );

    }


    public void upgrade(String strDestFilePath) {

        JobInfo.Builder builder = new JobInfo.Builder(0, new ComponentName(_context, DspJobService.class));

        builder.setMinimumLatency(1000);

        builder.setOverrideDeadline(10 * 1000);



        // Extras, work duration.
        PersistableBundle extras = new PersistableBundle();
        /*String workDuration = mDurationTimeEditText.getText().toString();
        if (TextUtils.isEmpty(workDuration)) {
            workDuration = "1";
        }*/
        extras.putString("FW-UPGRADE", strDestFilePath);

        builder.setExtras(extras);

        // Schedule job
        Log.d(TAG, "Scheduling job");
        JobScheduler tm = (JobScheduler) _context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.schedule(builder.build());
    }

    /** Used to observe when the database initialization is done */
    public MutableLiveData<Boolean> isSyncDone() {
        return mIsSyncDone;
    }


}

    