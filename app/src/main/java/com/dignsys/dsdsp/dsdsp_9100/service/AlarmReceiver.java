package com.dignsys.dsdsp.dsdsp_9100.service;

import android.arch.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.CommandHelper;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        ConfigHelper config = ConfigHelper.getInstance(context);
        Calendar cal = Calendar.getInstance();

        if (intent.getAction().equals(Definer.DEF_OFF_DAY_WEEK_ACTION)) {

            if (config.getAutoOnOffMode() == 0) { //0=ON, 1=OFF
                int offDay = config.getOffDay();
                Log.d(TAG, "applyOnOffTime: offDay=" + offDay);

                if(offDay != 0) {

                    int nDoM 	= cal.get(Calendar.DAY_OF_MONTH);
                    int nDate	= Integer.valueOf(String.format("%02d%02d", cal.get(Calendar.MONTH)+1, nDoM));

                    if(offDay == nDate){
                        MutableLiveData<Integer> cmd = CommandHelper.getInstance(context).getPlayCommand();
                        cmd.postValue(Definer.DEF_SLEEP_IN_COMMAND);
                    }
                }

                String offWeek = config.getOffWeek();
                if(!offWeek.equals("1111111"))	{

                    char[] cbOpW = offWeek.toCharArray();

                    int nDoW 	= cal.get(Calendar.DAY_OF_WEEK)-2;	// Sunday = 1;

                    if(nDoW == -1)	nDoW = 6; // Because WE start Monday.

                    if(cbOpW[nDoW] != '1')	{
                        MutableLiveData<Integer> cmd = CommandHelper.getInstance(context).getPlayCommand();
                        cmd.postValue(Definer.DEF_SLEEP_IN_COMMAND);
                    }
                }
            }

        }

        if (intent.getAction().equals(Definer.DEF_OFF_TIME_ACTION)) {
            if (config.getAutoOnOffMode() == Definer.DEF_USE) { //0=ON, 1=OFF
                MutableLiveData<Integer> cmd = CommandHelper.getInstance(context).getPlayCommand();
                cmd.postValue(Definer.DEF_SLEEP_IN_COMMAND);
            }
        }

        if (intent.getAction().equals(Definer.DEF_ON_TIME_ACTION)) {
            if (config.getAutoOnOffMode() == Definer.DEF_USE) { //0=ON, 1=OFF
                MutableLiveData<Integer> cmd = CommandHelper.getInstance(context).getPlayCommand();
             //   if (cmd.getValue() == Definer.DEF_SLEEP_IN_COMMAND) {
                    cmd.postValue(Definer.DEF_SLEEP_OUT_COMMAND);
             //   }

            }
        }

    }
}
