package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.util.MessageUtil;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;


public class ClockFragmentMain extends MainBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = ClockFragmentMain.class.getSimpleName();

    ContentEntity mContent;

    private TextClock mClockView;
    private View mView;

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
        mView = inflater.inflate(R.layout.fragment_clock, container, false);

        makeLayout(mView);

        return mView;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      //  view.setBackgroundColor(android.R.color.white);

        mClockView = view.findViewById(R.id.textClock);
        mClockView.setFormat12Hour(null);
        //textClock.setFormat24Hour("dd/MM/yyyy hh:mm:ss a");
       // mClockView.setFormat24Hour("hh:mm:ss a  EEE MMM d");
        mClockView.setFormat24Hour("hh:mm:ss a");
       // mClockView.setBackgroundColor(android.R.color.white);

        setTextProperty();

    }

    @Override
    void run() {

    }

    @Override
    void stop() {

    }

    @Override
    protected void applyConfig(ConfigEntity config) {

        setTextProperty();

    }

    private void setTextProperty() {

        if(mClockView == null || mView == null) return;

        ConfigHelper DSLibIF = ConfigHelper.getInstance(getContext());
        int pos = DSLibIF.getTimePosition();
        int fontSize = DSLibIF.getTimeFontSize();

        //TODO : miss match caption message type and position
        if (pos == Definer.DEF_TIME_DISABLE_POS) {
            mClockView.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        } else if (pos == Definer.DEF_TIME_LEFT_TOP_POS) {
            mClockView.setGravity(Gravity.LEFT | Gravity.TOP);
        } else if (pos == Definer.DEF_TIME_RIGHT_TOP_POS) {
            mClockView.setGravity(Gravity.RIGHT | Gravity.TOP);
        } else if (pos == Definer.DEF_TIME_LEFT_BOTTOM_POS) {
            mClockView.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        } else if(pos == Definer.DEF_TIME_RIGHT_BOTTOM_POS){
            mClockView.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        }

        Log.d(TAG, "makeView: pos=" + pos);

        mClockView.setTextSize(fontSize);
        mClockView.setTextColor(MessageUtil.getColor(DSLibIF.getTimeColor()));
        mView.setBackgroundColor(MessageUtil.getColor(DSLibIF.getTimeBGColor()));
    }

}
