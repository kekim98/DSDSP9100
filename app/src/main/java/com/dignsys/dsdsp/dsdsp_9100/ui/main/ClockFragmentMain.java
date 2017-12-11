package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;

import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;


public class ClockFragmentMain extends MainBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = ClockFragmentMain.class.getSimpleName();

    ContentEntity mContent;

    private TextClock mClockView;

    public ClockFragmentMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClockFragmentMain.
     */
    // TODO: Rename and change types and number of parameters
    public static ClockFragmentMain newInstance(int pane_num) {
        ClockFragmentMain fragment = new ClockFragmentMain();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clock, container, false);

        makeLayout(view);

        return view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setBackgroundColor(android.R.color.white);

        mClockView = view.findViewById(R.id.textClock);
        mClockView.setFormat12Hour(null);
        //textClock.setFormat24Hour("dd/MM/yyyy hh:mm:ss a");
       // mClockView.setFormat24Hour("hh:mm:ss a  EEE MMM d");
        mClockView.setFormat24Hour("hh:mm:ss a");
        mClockView.setBackgroundColor(android.R.color.white);

    }

    @Override
    void run() {

    }

    @Override
    void stop() {

    }

}
