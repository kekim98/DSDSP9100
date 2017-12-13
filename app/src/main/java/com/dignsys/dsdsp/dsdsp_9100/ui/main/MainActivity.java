package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.ui.config.ConfigActivity;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private List<PaneEntity> mPaneEntityList;
    private MainViewModel mViewModel;
    private ConfigHelper DSLibIF;

    ImageView mDefaultImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDefaultImageView = findViewById(R.id.imageView);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        DSLibIF = mViewModel.getConfigHelper();


        subscribe();


    }


    @Override
    protected void onDestroy()
    {
        stopDSDSP();
        super.onDestroy();

    }



    private void subscribe() {
        // Update the list when the data changes

    /*    mViewModel.getScene().observe(this, new Observer<SceneEntity>() {
            @Override
            public void onChanged(@Nullable SceneEntity sceneEntity) {
                Log.d(TAG, "onChanged: sceneEntity id =" + String.valueOf(sceneEntity.getId()));
            }
        });*/



        mViewModel.getPlayCommand().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer command) {
                Log.d(TAG, "onChanged:bawoori1 command=" + String.valueOf(command));
               // mPlayStart =1;

                if (command == 1) {
                    stopDSDSP();
                    playDSDSP();
                }
                if (command == 0) {
                    stopDSDSP();
                }

            }
        });

    }




    //V:video/picture, P:picture, M:message, C:clock, B:background color
    //D:DTV, W:webview(picture), T:webview(text), S:screen size

    private void playDSDSP() {

        mPaneEntityList = mViewModel.getPaneList();

        if(mPaneEntityList == null || mPaneEntityList.size() <= 0 ) return;

        mFragmentList.clear();

        for (PaneEntity pe : mPaneEntityList) {

            if (pe.getPaneType().equals("B")) {
                BackgroundFragmentMain backgroundFragment =
                        BackgroundFragmentMain.newInstance(pe.getPane_id());
                mFragmentList.add(backgroundFragment);

            }
            if (pe.getPaneType().equals("D")) {
                DTvFragmentMain dTvFragment =
                        DTvFragmentMain.newInstance(pe.getPane_id());
                mFragmentList.add(dTvFragment);

            }
            if (pe.getPaneType().equals("V")) {
                VideoFragmentMain videoFragment =
                        VideoFragmentMain.newInstance(pe.getPane_id());
                mFragmentList.add(videoFragment);
            }
            if (pe.getPaneType().equals("P")) {
                PictureFragmentMain pictureFragment =
                        PictureFragmentMain.newInstance(pe.getPane_id());
                mFragmentList.add(pictureFragment);

            }
            if (pe.getPaneType().equals("W")) {
                WeatherFragmentMain weatherFragment =
                        WeatherFragmentMain.newInstance(pe.getPane_id());
                mFragmentList.add(weatherFragment);

            }
            if (pe.getPaneType().equals("C")) {
                ClockFragmentMain clockFragment =
                        ClockFragmentMain.newInstance(pe.getPane_id());
                mFragmentList.add(clockFragment);

            }
            if (pe.getPaneType().equals("M")) {
                MessageFragmentMain messageFragment =
                        MessageFragmentMain.newInstance(pe.getPane_id());
                mFragmentList.add(messageFragment);

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

        Log.d(TAG, "playDSDSP: bawoori");
    }

    private void stopDSDSP() {

        Log.d(TAG, "stopDSDSP: bawoori");
        if (mFragmentList.size() > 0) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            for (Fragment f : mFragmentList) {
                fragmentTransaction.remove(f);
            }

            fragmentTransaction.commit();
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

    public PaneEntity getSpaneEntity() {
        if (mPaneEntityList != null) {
            for (PaneEntity pe : mPaneEntityList) {
                if (pe.getPaneType().equals("S")) {
                    return pe;
                }
            }
        }
        return null;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode == Definer.DEF_KEY_CODE_MENU)	{
            ConfigActivity.startConfigActivity(this);
        }

        if(keyCode == KeyEvent.KEYCODE_BACK)	{
            return false;
        }

        return super.onKeyUp(keyCode, event);
    }


}
