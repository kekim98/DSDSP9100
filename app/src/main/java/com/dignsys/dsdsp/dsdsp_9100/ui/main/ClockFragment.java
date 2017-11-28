package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;

import com.dignsys.dsdsp.dsdsp_9100.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClockFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClockFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String START_X = "param1";
    private static final String START_Y = "param2";
    private static final String P_WIDTH = "param3";
    private static final String P_HEIGHT = "param4";

    // TODO: Rename and change types of parameters
    private int mStartX;
    private int mStartY;
    private int mPWidth;
    private int mPHeight;

    private OnFragmentInteractionListener mListener;
    private TextClock mClockView;

    public ClockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClockFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClockFragment newInstance(int param1, int param2, int param3, int param4) {
        ClockFragment fragment = new ClockFragment();
        Bundle args = new Bundle();
        args.putInt(START_X, param1);
        args.putInt(START_Y, param2);
        args.putInt(P_WIDTH, param3);
        args.putInt(P_HEIGHT, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStartX = getArguments().getInt(START_X);
            mStartY = getArguments().getInt(START_Y);
            mPWidth = getArguments().getInt(P_WIDTH);
            mPHeight = getArguments().getInt(P_HEIGHT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clock, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = mPWidth;
        layoutParams.height = mPHeight;

        view.setLayoutParams(layoutParams);
        view.setX(mStartX);
        view.setY(mStartY);
        view.setBackgroundColor(android.R.color.white);

        mClockView = view.findViewById(R.id.textClock);
        mClockView.setFormat12Hour(null);
        //textClock.setFormat24Hour("dd/MM/yyyy hh:mm:ss a");
        mClockView.setFormat24Hour("hh:mm:ss a  EEE MMM d");
        mClockView.setBackgroundColor(android.R.color.white);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
