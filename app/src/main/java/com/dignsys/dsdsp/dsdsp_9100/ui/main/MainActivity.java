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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;
import com.dignsys.dsdsp.dsdsp_9100.service.LocalService;
import com.dignsys.dsdsp.dsdsp_9100.ui.Resize;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ScheduleViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean mBound;
    private LocalService mService;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private int mMainPane;
    private List<PaneEntity> mPaneEntityList;
    private int m_nScreenWidth;
    private int m_nScreenHeight;
    private ScheduleViewModel mViewModel;
    private View mDefaultImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDefaultImageView = findViewById(R.id.imageView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);

        m_nScreenWidth = displayMetrics.widthPixels;
        m_nScreenHeight = displayMetrics.heightPixels;

        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        mViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);

        subscribe();
    }

    private void subscribe() {
        // Update the list when the data changes

        mViewModel.getScene().observe(this, new Observer<SceneEntity>() {
            @Override
            public void onChanged(@Nullable SceneEntity sceneEntity) {
                Log.d(TAG, "onChanged: sceneEntity id =" + Integer.valueOf(sceneEntity.getId()));
            }
        });

        mViewModel.getPaneList().observe(this, new Observer<List<PaneEntity>>() {
            @Override
            public void onChanged(@Nullable List<PaneEntity> paneEntities) {
                Log.d(TAG, "onChanged: paneEntities size =" + Integer.valueOf(paneEntities.size()));
                mPaneEntityList = paneEntities;
                stopDSDSP();
                playDSDSP();
            }
        });


        mViewModel.getScheduleDone().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer pane_num) {
                if (pane_num == mMainPane) mViewModel.requestNextScene();
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

    private void playDSDSP() {

        if(mPaneEntityList == null) return;
        findMainPane(mPaneEntityList);

        mFragmentList.clear();

        for (PaneEntity pe : mPaneEntityList) {
            if (pe.getPaneType().equals("D")) {

            }
            if (pe.getPaneType().equals("V")) {
                VideoFragment videoFragment =
                        VideoFragment.newInstance(pe.getPane_id());
                mFragmentList.add(videoFragment);
            }
            if (pe.getPaneType().equals("P")) {

            }

            if (pe.getPaneType().equals("W")) {

            }
            if (pe.getPaneType().equals("C")) {
                /*ClockFragment clockFragment =
                        ClockFragment.newInstance(pe.getPaneX(), pe.getPaneY(), pe.getPaneWidth(), pe.getPaneWidth());
                mFragmentList.add(clockFragment);*/

            }
            if (pe.getPaneType().equals("M")) {

            }
            if (pe.getPaneType().equals("B")) {

            }

            if (pe.getPaneType().equals("T")) {

            }
        }

        if (mFragmentList.size() > 0) {

            mDefaultImageView.setVisibility(View.GONE);

            View rootView = findViewById(R.id.main_layout);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            for (Fragment f : mFragmentList) {
                fragmentTransaction.add(rootView.getId(),f);
            }
            fragmentTransaction.commit();

        }else{

            mDefaultImageView.setVisibility(View.VISIBLE);
        }
    }

    private void stopDSDSP() {
        if (mFragmentList.size() > 0) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            for (Fragment f : mFragmentList) {
                fragmentTransaction.remove(f);
            }

            fragmentTransaction.commit();
        }
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



    public PaneEntity getPaneEntity(int paneNum) {

        if (mPaneEntityList != null) {
            for (PaneEntity pe : mPaneEntityList) {
                if (pe.getPane_id() == paneNum) {
                    return pe;
                }
            }
        }
        return null;
    }


    public void getResize(Resize resize) {

       /* int nX = (int)((((double)m_nX * (double)nScreenX)/(double)nFullZoneW)+0.5);
        int nY = (int)((((double)m_nY * (double)nScreenH)/(double)nFullZoneH)+0.5);
        int nW = (int)((((double)m_nW * (double)nScreenX)/(double)nFullZoneW)+0.5);
        int nH = (int)((((double)m_nH * (double)nScreenH)/(double)nFullZoneH)+0.5);*/

    }
}
