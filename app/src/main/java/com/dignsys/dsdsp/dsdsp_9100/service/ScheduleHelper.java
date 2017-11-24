package com.dignsys.dsdsp.dsdsp_9100.service;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ScheduleEntity;

import java.util.Calendar;
import java.util.List;



/**
 * Created by bawoori on 17. 11. 24.
 */

public class ScheduleHelper {
    private static ScheduleHelper sInstance;

    private final MutableLiveData<Integer> mScheduleId = new MutableLiveData<>();
    private final LiveData<List<ScheduleEntity>> scheduleEntityList;

    private List<ScheduleEntity> _scheduleList;
    private int _scheduleId; //only to be used internally
    private Context _context;

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    private ScheduleHelper(Context context) {
        _scheduleId = 0;
        mScheduleId.setValue(0);
        _context = context;

        AppDatabase db = DatabaseCreator.getInstance(context);
        scheduleEntityList = db.scheduleDao().loadAllSchedule();

        // Create the observer which updates the schedule list .
        final Observer<List<ScheduleEntity>> scheduleObserver = new Observer<List<ScheduleEntity>>() {
            @Override
            public void onChanged(@Nullable final List<ScheduleEntity> schedules) {
                _scheduleList = schedules;
                _scheduleId = 0;
                mScheduleId.postValue(0);
            }
        };
        scheduleEntityList.observeForever(scheduleObserver);
    }

    public synchronized static ScheduleHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new ScheduleHelper(context);
                }
            }
        }
        return sInstance;
    }

    public LiveData<Integer> getCurrSchduleId() {
        return mScheduleId;
    }


    @SuppressLint("StaticFieldLeak")
    public void updateScheduleTick() {

        if(_scheduleList == null) return;

        new AsyncTask<Context, Void, Void>() {


            @Override
            protected Void doInBackground(Context... params) {
                Calendar cal = Calendar.getInstance();

                int nDoM 	= cal.get(Calendar.DAY_OF_MONTH);
                int nDoW 	= cal.get(Calendar.DAY_OF_WEEK)-2;	// Sunday = 1;
                // Because WE start Monday.
                if(nDoW == -1)	nDoW = 6;

                int nDate	= Integer.valueOf(String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, nDoM));
                int nTime	= Integer.valueOf(String.format("%02d%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));


                for(ScheduleEntity se : _scheduleList)	{
                    if(checkOpS(nDoM, se.getOpS())
                            && checkOpDate(nDate,se.getOpStartDate(), se.getOpEndDate())
                            && checkOpW(nDoW, se.getOpW())
                            && checkOpTime(nTime, se.getOpStartTime(), se.getOpEndTime()))
                    {
                        if(_scheduleId != se.getId()){
                            _scheduleId = se.getId();
                            mScheduleId.postValue(_scheduleId);
                        }

                        break;
                    }
                }
                return null;
            }


        }.execute(_context.getApplicationContext());
    }

    private boolean checkOpS(int n, int opS)	{

        boolean nResult = false;

        if(opS == n || opS == 0) nResult = true;

        return nResult;
    }

    private boolean checkOpW(int n, String opW)	{

        boolean nResult = false;

        if(opW.isEmpty()) nResult = true;
        else {
            char[] cbOpW = opW.toCharArray();

            if(cbOpW[n] == '1') nResult = true;

        }

        return nResult;
    }


    private boolean checkOpDate(int n, String startData, String endDate)	{

        boolean nResult = false;

        if(startData.isEmpty() && endDate.isEmpty() ) nResult = true;
        else {
            long lSDate	= Long.valueOf(startData);
            long lEDate	= Long.valueOf(endDate);

            if(lSDate <= n && n <= lEDate) nResult = true;
        }

        return nResult;
    }

    private boolean checkOpTime(int n, String startTime, String endTime)	{

        boolean nResult = false;

        if(startTime.isEmpty() && endTime.isEmpty() ) nResult = true;
        else {
            int nSTime	= Integer.valueOf(startTime);
            int nETime	= Integer.valueOf(endTime);

            if(nSTime < n && n < nETime) nResult = true;
        }

        return nResult;
    }


}
