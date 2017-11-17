package com.dignsys.dsdsp.dsdsp_9100.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.DspFormatEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.DspPlayListEntity;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ScheduleViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.dignsys.dsdsp.dsdsp_9100.Definer.SCHEDULE_PERIOD;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private List<DspFormatEntity> mFormatList;
    private List<DspPlayListEntity> mPlayList;
    private TimerTask mTask;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ScheduleViewModel viewModel =
                ViewModelProviders.of(this).get(ScheduleViewModel.class);

        subscribe(viewModel);

    }

    private void subscribe(ScheduleViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFormatList().observe(this, new Observer<List<DspFormatEntity>>() {
            @Override
            public void onChanged(@Nullable List<DspFormatEntity> formatList) {
                if (formatList != null) {
                    mFormatList = formatList;
                    runSchedule();

                    Log.d(TAG, "onChanged: DspFormatEntity loaded!!!!");

                } else {
                    Log.d(TAG, "onChanged: DspFormatEntity NOT loaded!!!!");

                }

            }
        });

        viewModel.getPlayList().observe(this, new Observer<List<DspPlayListEntity>>() {
            @Override
            public void onChanged(@Nullable List<DspPlayListEntity> playList) {
                if (playList != null) {
                    mPlayList = playList;
                    runSchedule();
                    Log.d(TAG, "onChanged: DspPlayListEntity loaded!!!!");

                } else {
                    Log.d(TAG, "onChanged: DspPlayListEntity NOT loaded!!!!");

                }

            }
        });
    }

    private void runSchedule() {
        if (mFormatList == null || mPlayList == null) {
            Log.d(TAG, "runSchedule:  returned!!!!");
            return;
        }

        if (mFormatList.size() == 0 || mPlayList.size() == 0) {
            stopSchedule();
        }


        if (mTask == null) {
            //  mGeoFenceServiceHelper.init();
            mTask = new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG, "running timer task........");
                    procScen();
                }
            };

            mTimer = new Timer();
            mTimer.schedule(mTask, 0, SCHEDULE_PERIOD);

        }

    }

    private void procScen() {

    }

    private void createViews() {
    }

    private void stopSchedule() {
        deleteViews();
        Log.d(TAG, "stopSchedule: entered!!!!");
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private void deleteViews() {

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
