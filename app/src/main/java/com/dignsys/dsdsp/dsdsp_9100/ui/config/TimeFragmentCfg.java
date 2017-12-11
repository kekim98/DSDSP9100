package com.dignsys.dsdsp.dsdsp_9100.ui.config;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dignsys.dsdsp.dsdsp_9100.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TimeFragmentCfg#newInstance} factory method to
 * create an instance of this fragment.
 */


public class TimeFragmentCfg extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = TimeFragmentCfg.class.getSimpleName();


    public TimeFragmentCfg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragmentMain.
     */

    static TimeFragmentCfg newInstance(int pane_num) {
        TimeFragmentCfg fragment = new TimeFragmentCfg();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
    //    fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cfg_time, container, false);

       // makeLayout(view);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Rename and change types and number of view

    }



}
