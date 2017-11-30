package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ScheduleViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TempleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TempleFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = TempleFragment.class.getSimpleName();

    private int mPaneNum = 0;

    private VideoView mVideoView;
    private ContentEntity mContent;
    private ScheduleViewModel mViewModel;
    private PaneEntity mPaneEntity;
    private View mImageSW;

    public TempleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragment.
     */
    public static TempleFragment newInstance(int pane_num) {
        TempleFragment fragment = new TempleFragment();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPaneNum = getArguments().getInt(PANE_NUM);
            mPaneEntity = ((MainActivity) getActivity()).getPaneEntity(mPaneNum);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video, container, false);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = mPaneEntity.getPaneWidth();
        layoutParams.height = mPaneEntity.getPaneHeight();

        view.setLayoutParams(layoutParams);
        view.setX(mPaneEntity.getPaneX());
        view.setY(mPaneEntity.getPaneY());

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Rename and change types and number of view

        mVideoView = view.findViewById(R.id.videoView);
        mImageSW = view.findViewById(R.id.imageSW);
        /*String UrlPath = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.kkk;
        mVideoView.setVideoURI(Uri.parse(UrlPath));
        mVideoView.start();*/


        mViewModel = ViewModelProviders.of(getActivity()).get(ScheduleViewModel.class);
        subscribe();

        run();

    }

    private void stop() {
        //TODO:......
    }

    private void run() {
        Log.d(TAG, "run:........");
        mContent = mViewModel.getContent(mPaneNum); //for first content

        if (mContent == null) return;

        if (mContent.getFileType() == Definer.DEF_CONTENTS_TYPE_VIDEO) {
            mImageSW.setVisibility(View.GONE);

        } else if (mContent.getFileType() == Definer.DEF_CONTENTS_TYPE_IMAGE) {
            mVideoView.setVisibility(View.GONE);

            //TODO:.....
        }


    }

    private void subscribe() {
        // Update the list when the data changes

        mViewModel.getContentPlayDone().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer pane_num) {
                Log.d(TAG, "onChanged: getScheduleDone pane_num =" + Integer.valueOf(pane_num));

                if (pane_num == mPaneNum) {
                    stop();
                    run();

                }
            }
        });

        mViewModel.getConfig().observe(getActivity(), new Observer<ConfigEntity>() {
            @Override
            public void onChanged(@Nullable ConfigEntity pane_num) {
                Log.d(TAG, "onChanged: getScheduleDone pane_num =");
                //TODO:apply dsdsp config to each play content

            }
        });

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
