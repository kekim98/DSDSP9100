package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.GlideApp;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class WeatherFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = WeatherFragment.class.getSimpleName();

    ContentEntity mContent;

    //   private ImageView mImageSW;
    private ImageView mImageView;


    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragment.
     */

    static WeatherFragment newInstance(int pane_num) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        makeLayout(view);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Rename and change types and number of view

        //  mVideoView = view.findViewById(R.id.videoView);
        mImageView = view.findViewById(R.id.imageView);

        run();

    }


    @Override
    void stop() {

        //TODO:......
    }

    @Override
    void run() {
        Log.d(TAG, "run:........");
        mContent = mViewModel.getContent(mPaneNum); //for first content
        if (mContent == null) {
            Log.d(TAG, "mContent null");
            return;
        }

        if (mContent.getFileType() != Definer.DEF_CONTENTS_TYPE_IMAGE) return;

        try {
            final String host = IOUtils.getHostAddress(this.getContext());
            final String weatherPath = Definer.DEF_WEB_CONTENTS_URL;
            final String fileName = IOUtils.getFilename(mContent.getFilePath());
            //TODO : port check

            String UrlPath = String.format("%s:%d%s/%s", host,80,weatherPath, fileName);

           // String UrlPath = IOUtils.getDspPlayContent(this.getContext(), mContent.getFilePath());

            Log.d(TAG, "image run: path=" + UrlPath);

            GlideApp.with(this)
                    .load(UrlPath)
                    .into(mImageView);


        } catch (NullPointerException e) {
            Log.e(TAG, "run: NullPointException");
        }

    }


}
