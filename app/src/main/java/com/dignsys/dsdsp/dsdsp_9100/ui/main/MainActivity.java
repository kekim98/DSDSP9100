package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ScheduleViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private List<PaneEntity> mFormatList;
    private List<ContentEntity> mPlayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ScheduleViewModel viewModel =
                ViewModelProviders.of(this).get(ScheduleViewModel.class);

        subscribe(viewModel);


/*
        final ConfigViewModel viewModel =
                ViewModelProviders.of(this).get(ConfigViewModel.class);

        subscribe2(viewModel);
*/

    }

   /* private void subscribe2(ConfigViewModel viewModel) {
        viewModel.getConfig().observe(this, new Observer<ConfigEntity>() {
            @Override
            public void onChanged(@Nullable ConfigEntity configEntity) {
                if (configEntity != null) {
                    run_test(configEntity);
                }
            }
        });
    }*/

    private void run_test(ScheduleViewModel configEntity) {

        return;
    }

    private void subscribe(ScheduleViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFormatList().observe(this, new Observer<List<PaneEntity>>() {
            @Override
            public void onChanged(@Nullable List<PaneEntity> formatList) {
                if (formatList != null) {
                    mFormatList = formatList;
                 //   runSchedule();

                    Log.d(TAG, "onChanged: PaneEntity loaded!!!!");

                } else {
                    Log.d(TAG, "onChanged: PaneEntity NOT loaded!!!!");

                }

            }
        });

        viewModel.getPlayList().observe(this, new Observer<List<ContentEntity>>() {
            @Override
            public void onChanged(@Nullable List<ContentEntity> playList) {
                if (playList != null) {
                    mPlayList = playList;
                 //   runSchedule();
                    Log.d(TAG, "onChanged: ContentEntity loaded!!!!");

                } else {
                    Log.d(TAG, "onChanged: ContentEntity NOT loaded!!!!");

                }

            }
        });
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
