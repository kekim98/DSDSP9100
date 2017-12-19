package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.dignsys.dsdsp.dsdsp_9100.GlideApp;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.MainViewModel;

/**
 * Created by bawoori on 17. 12. 6.
 */

abstract class MainBaseFragment extends Fragment {
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = "MainBaseFragment";


    Observer<Integer> mContentPlayDoneObserver;
    Observer<ConfigEntity> mConfigObserver;
    MainViewModel mViewModel;

    int mPaneNum = 0;
    private PaneEntity mPaneEntity;
    private PaneEntity mSpaneEntity;
    private int nX;
    private int nY;
    private int nW;
    private int nH;


    abstract void run();
    abstract void stop();
    protected abstract void applyConfig(ConfigEntity config);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPaneNum = getArguments().getInt(PANE_NUM);
            mPaneEntity = ((MainActivity) getActivity()).getPaneEntity(mPaneNum);
            mSpaneEntity =  ((MainActivity) getActivity()).getSpaneEntity();
        }
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        subscribe();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mViewModel.getContentPlayDone().removeObserver(mContentPlayDoneObserver);
        mViewModel.getConfig().removeObserver(mConfigObserver);

        GlideApp.get(getActivity()).clearMemory();
        //TODO: release resource
    }

    void subscribe() {
        // Update the list when the data changes

        mContentPlayDoneObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer pane_num) {
                Log.d(TAG, "onChanged: bawoori1 getContentPlayDone pane_num =" + String.valueOf(pane_num));

                if ((pane_num > 0) && (pane_num == mPaneNum)) {
                    stop();
                    run();
                }
            }
        };
        mViewModel.getContentPlayDone().observe(getActivity(), mContentPlayDoneObserver);


        mConfigObserver = new Observer<ConfigEntity>() {
            @Override
            public void onChanged(@Nullable ConfigEntity config) {
                applyConfig(config);
            }
        };
        mViewModel.getConfig().observe(getActivity(), mConfigObserver);

    }



    void makeLayout(View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);

        //int nX, nY,  nW, nH;
        nX=nY=0;
        nW=1920;
        nH=1080;

        if (mPaneEntity != null && mSpaneEntity != null) {
            final int nScreenWidth 		= displayMetrics.widthPixels;
            final int nScreenHeight 	= displayMetrics.heightPixels;

            final int nSceneWidth = mSpaneEntity.getPaneWidth();
            final int nSceneHeight = mSpaneEntity.getPaneHeight();

             nX = (int)((((float)mPaneEntity.getPaneX()* (float)nScreenWidth)/(float)nSceneWidth)+0.5);
             nY = (int)((((float)mPaneEntity.getPaneY() * (float)nScreenHeight)/(float)nSceneHeight)+0.5);
             nW = (int)((((float)mPaneEntity.getPaneWidth() * (float)nScreenWidth)/(float)nSceneWidth)+0.5);
             nH = (int)((((float)mPaneEntity.getPaneHeight() * (float)nScreenHeight)/(float)nSceneHeight)+0.5);
        }


        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = nW ;
        layoutParams.height = nH ;

        view.setLayoutParams(layoutParams);
        view.setX(nX);
        view.setY(nY);
    }


    public int getX() {
        return nX;
    }

    public int getY() {
        return nY;
    }

    public int getW() {
        return nW;
    }

    public int getH() {
        return nH;
    }



}
