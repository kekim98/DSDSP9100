package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.service.LocalService;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ScheduleViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private List<PaneEntity> mFormatList;
    private List<ContentEntity> mPlayList;
    private boolean mBound;
    private LocalService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        final ScheduleViewModel viewModel =
                ViewModelProviders.of(this).get(ScheduleViewModel.class);

        subscribe(viewModel);

    }

    private void subscribe(ScheduleViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getCurrentScheduleId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer scheduleId) {

                Log.d(TAG, "onChanged: schedule id =" + Integer.valueOf(scheduleId));

                if (scheduleId == 0) {
                    stopPlay();

                } else {
                    runPlay(scheduleId);
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



    private void runPlay(Integer scheduleId) {

    }

    private void stopPlay() {

    }


    private void init() {
        View rootView = findViewById(R.id.main_layout);
        //ImageFragment imageFragment1 = ImageFragment.newInstance(0, 0);
        //ImageFragment imageFragment2 = ImageFragment.newInstance(960, 0);
        //VideoFragment videoFragment = VideoFragment.newInstance(960, 0);
        ClockFragment clockFragment = ClockFragment.newInstance(0, 0);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction
              //  .add(rootView.getId(), imageFragment1)
                .add(rootView.getId(), clockFragment)
                .commit();

    }
}
