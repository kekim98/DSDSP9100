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
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;
import com.dignsys.dsdsp.dsdsp_9100.service.LocalService;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ScheduleViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Fragment> mFormatList;
    private List<ContentEntity> mPlayList;
    private boolean mBound;
    private LocalService mService;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private int mMainPane;


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


        viewModel.getScene().observe(this, new Observer<SceneEntity>() {
            @Override
            public void onChanged(@Nullable SceneEntity sceneEntitie) {
                Log.d(TAG, "onChanged: SceneEntity id =" );
            }
        });

        viewModel.getPaneList().observe(this, new Observer<List<PaneEntity>>() {
            @Override
            public void onChanged(@Nullable List<PaneEntity> paneEntities) {
                Log.d(TAG, "onChanged: paneEntities id =" );
                stopDSDSP();
                playDSDSP(paneEntities);
            }
        });


        viewModel.getContentList().observe(this, new Observer<List<ContentEntity>>() {
            @Override
            public void onChanged(@Nullable List<ContentEntity> contentEntities) {
                Log.d(TAG, "onChanged: contentEntities id =" );
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



    //V:video/picture, P:picture, M:message, C:clock, B:background color
    //D:DTV, W:webview(picture), T:webview(text), S:screen size

    private void playDSDSP(List<PaneEntity> panes) {

        if(panes == null) return;
        findMainPane(panes);

        for (PaneEntity pe : panes) {
            if (pe.getPaneType().equals("V")) {
                VideoFragment videoFragment =
                        VideoFragment.newInstance(pe.getPane_id());
                mFragmentList.add(videoFragment);
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                View rootView = findViewById(R.id.main_layout);
                fragmentTransaction
                        //  .add(rootView.getId(), imageFragment1)
                        .add(rootView.getId(), videoFragment);

                fragmentTransaction.commit();


            }
            if (pe.getPaneType().equals("P")) {

            }
            if (pe.getPaneType().equals("M")) {

            }
            if (pe.getPaneType().equals("C")) {
                ClockFragment clockFragment =
                        ClockFragment.newInstance(pe.getPaneX(), pe.getPaneY(), pe.getPaneWidth(), pe.getPaneWidth());
                mFragmentList.add(clockFragment);

            }
            if (pe.getPaneType().equals("B")) {

            }
            if (pe.getPaneType().equals("D")) {

            }
            if (pe.getPaneType().equals("W")) {

            }
            if (pe.getPaneType().equals("T")) {

            }
        }

        View rootView = findViewById(R.id.main_layout);
        //ImageFragment imageFragment1 = ImageFragment.newInstance(0, 0);
        //ImageFragment imageFragment2 = ImageFragment.newInstance(960, 0);
        //VideoFragment videoFragment = VideoFragment.newInstance(960, 0);
      //  ClockFragment clockFragment = ClockFragment.newInstance(0, 0);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        /*fragmentTransaction
              //  .add(rootView.getId(), imageFragment1)
                .add(rootView.getId(), clockFragment);

        fragmentTransaction.commit();*/

    }

    private void findMainPane(List<PaneEntity> panes) {

        for (PaneEntity pe : panes) {
            if(pe.getPaneType().equals("I") || pe.getPaneType().equals("D"))	{
                mMainPane = pe.getPane_id();
            }
            else if(pe.getPaneType().equals("V")) {
                mMainPane = pe.getPane_id();
            }
            else if(pe.getPaneType().equals("P")) {
                mMainPane = pe.getPane_id();
            }
            else if(pe.getPaneType().equals("T")) {
                mMainPane = pe.getPane_id();
            }else{
                mMainPane = 1;
            }
        }
    }

    private void stopDSDSP(){

    }

    public void paneScheduleDone(int paneNum) {

        if (paneNum == mMainPane) {
            //TODO : have to implement SCENE CHANGE
        }

    }
}
