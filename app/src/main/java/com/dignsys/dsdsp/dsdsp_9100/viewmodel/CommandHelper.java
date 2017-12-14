package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PersistableBundle;
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
import com.dignsys.dsdsp.dsdsp_9100.service.DspJobService;
import com.dignsys.dsdsp.dsdsp_9100.service.SyncService;

import java.io.File;
import java.util.TimeZone;


/**
 * Created by bawoori on 17. 11. 24.
 */

public class CommandHelper {

    private static final String TAG = CommandHelper.class.getSimpleName();
    private static CommandHelper sInstance;

    // private static final MutableLiveData ABSENT = new MutableLiveData();
    private AppDatabase mDB;
    private final LiveData<CommandEntity> mCommand;

    private final MutableLiveData<Integer> mPlayCommand = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsSyncDone = new MutableLiveData<>();


    private Context _context;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private CommandEntity _command;

    private CommandHelper(Context context) {
        _context = context;

        mDB = DatabaseCreator.getInstance(context);
        mCommand = mDB.commandDao().loadCommand();
       

        // Create the observer which updates the schedule list .
        final Observer<CommandEntity> commandObserver = new Observer<CommandEntity>() {
            @Override
            public void onChanged(@Nullable final CommandEntity command) {
                if (command != null) {
                    _command = command;
                    processCommand(_command);
                }

            }
        };
        mCommand.observeForever(commandObserver);


    }

    private void processCommand(CommandEntity command) {

        /*if (!TextUtils.isEmpty(command.getFontFilePath_1())) {
            Log.d(TAG, "processCommand: font proc command...");

            command.setFontFilePath_1(null);
            updateCommand();
        }

        if (!TextUtils.isEmpty(command.getFontFilePath_2())) {
            Log.d(TAG, "processCommand: font proc command...");

            command.setFontFilePath_2(null);
            updateCommand();
        }

        if (!TextUtils.isEmpty(command.getFontFilePath_3())) {
            Log.d(TAG, "processCommand: font proc command...");

            command.setFontFilePath_3(null);
            updateCommand();
        }*/


        if (command.isPowerOff()) {
            Log.d(TAG, "processCommand: isPowerOff proc command...");
            MutableLiveData<Integer> cmd = CommandHelper.getInstance(_context).getPlayCommand();
            cmd.setValue(Definer.DEF_SLEEP_IN_COMMAND);
            command.setPowerOff(false); //clear value for restart app
            updateCommand();
        }

        if (command.isPowerOn()) {
            Log.d(TAG, "processCommand: isPowerOn proc command...");

            MutableLiveData<Integer> cmd = CommandHelper.getInstance(_context).getPlayCommand();
            cmd.setValue(Definer.DEF_SLEEP_OUT_COMMAND);
            command.setPowerOn(false);
            updateCommand();
        }

        if (!TextUtils.isEmpty(command.getFwFilePath())) {
            MutableLiveData<Integer> cmd = CommandHelper.getInstance(_context).getPlayCommand();
            cmd.setValue(Definer.DEF_PLAY_IDLE_COMMAND);

            Log.d(TAG, "processCommand: getFwFilePath=" + command.getFwFilePath());

            String url = Definer.DEF_COMMAND_CONTENT_PATH + File.separator + command.getFwFilePath();

            command.setFwFilePath(null);
            updateCommand();

            upgrade(url);

        }


        if (command.isReboot()) {
            Log.d(TAG, "processCommand: isReboot proc command...");
            command.setReboot(false); //clear value for restart app
            updateCommand();
            MutableLiveData<Integer> cmd = CommandHelper.getInstance(_context).getPlayCommand();
            cmd.setValue(Definer.DEF_REBOOT_COMMAND);
        }

    }


    @SuppressLint("StaticFieldLeak")
    private void updateCommand() {
        new AsyncTask<Context, Void, Void>() {
            @Override
            protected Void doInBackground(Context... contexts) {
                mDB.commandDao().update(_command);
                return null;
            }
        }.execute(_context);
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

    public void rebootSystem() {
        PowerManager pm = (PowerManager) _context.getSystemService(Context.POWER_SERVICE);
        pm.reboot(null);
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

    public void screenOnOff(boolean onOff) {
        final String	onCmd = "ds7000_screen_on.sh";
        final String 	offCmd = "ds7000_screen_off.sh";

        String cmd = onOff?onCmd:offCmd;

        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    public  String getUserFont(int n)	{

        String strPath = "";

      //  String basePath = Definer.DEF_COMMAND_CONTENT_PATH + File.separator;
        switch(n)	{
            case 1:
                if (!TextUtils.isEmpty(_command.getFontFilePath_1())) {
                  //  strPath = basePath + _command.getFontFilePath_1();
                    strPath = _command.getFontFilePath_1();
                }

                break;
            case 2:
                if (!TextUtils.isEmpty(_command.getFontFilePath_2())) {
                   // strPath = basePath + _command.getFontFilePath_2();
                    strPath = _command.getFontFilePath_2();
                }
                break;
            case 3:
                if (!TextUtils.isEmpty(_command.getFontFilePath_3())) {
                   // strPath = basePath + _command.getFontFilePath_3();
                    strPath = _command.getFontFilePath_3();
                }
                break;
        }

        return strPath;
    }


    public void upgrade(String strDestFilePath) {


        if (!TextUtils.isEmpty(strDestFilePath)) {

            final String upgradeCmd = "pm install -r -d " + strDestFilePath;

            Log.e(TAG, "firmware upgrade: ....................................." );
            Process p = null;
            try {
                p = Runtime.getRuntime().exec(upgradeCmd);
                p.waitFor();

                //	Toast.makeText(this, "SD카드가 포맷되었습니다", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



      /*  JobInfo.Builder builder = new JobInfo.Builder(0, new ComponentName(_context, DspJobService.class));

        builder.setMinimumLatency(500);

        builder.setOverrideDeadline(2 * 1000);



        // Extras, work duration.
        PersistableBundle extras = new PersistableBundle();
        *//*String workDuration = mDurationTimeEditText.getText().toString();
        if (TextUtils.isEmpty(workDuration)) {
            workDuration = "1";
        }*//*
        extras.putString(Definer.DEF_DSP_JOB_KEY, Definer.DEF_FW_UPGRADE_VALUE);
        extras.putString(Definer.DEF_VALUE_SUB_KEY, strDestFilePath);

        builder.setExtras(extras);

        // Schedule job
        Log.d(TAG, "Scheduling job");
        JobScheduler tm = (JobScheduler) _context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.schedule(builder.build());*/
    }


    public static void startWatchDog(Context context) {

       /* JobInfo.Builder builder = new JobInfo.Builder(0, new ComponentName(context, DspJobService.class));

     //   builder.setMinimumLatency(100);
        builder.setPeriodic(3 * 1000);
        builder.setPersisted(true);

        //builder.setOverrideDeadline(10 * 1000);

        // Extras, work duration.
        PersistableBundle extras = new PersistableBundle();
        *//*String workDuration = mDurationTimeEditText.getText().toString();
        if (TextUtils.isEmpty(workDuration)) {
            workDuration = "1";
        }*//*
        extras.putString(Definer.DEF_DSP_JOB_KEY, Definer.DEF_WATCH_DOG_VALUE);

        builder.setExtras(extras);

        // Schedule job
        Log.d(TAG, "Scheduling job");
        JobScheduler tm = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.schedule(builder.build());*/
    }

    /** Used to observe when the database initialization is done */
    public MutableLiveData<Integer> getPlayCommand() {
        return mPlayCommand;
    }


    public void removeObservers() {

    }

    public MutableLiveData<Boolean> getIsSyncDone() {
        return mIsSyncDone;
    }
}

    