package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.VideoView;
import android.widget.ImageSwitcher;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ScheduleViewModel;

import java.io.File;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = VideoFragment.class.getSimpleName();

    private int mPaneNum = 0;

    private VideoView mVideoView;
    private ContentEntity mContent;
    private ScheduleViewModel mViewModel;
    private PaneEntity mPaneEntity;
    private ImageView mImageSW;

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragment.
     */
    public static VideoFragment newInstance(int pane_num) {
        VideoFragment fragment = new VideoFragment();
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

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
               run();
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                run();
                return true;
            }
        });

       /* mImageSW.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                return new ImageView(VideoFragment.this.getContext());
            }
        });

        // Set animations
        // https://danielme.com/2013/08/18/diseno-android-transiciones-entre-activities/
        Animation fadeIn = AnimationUtils.loadAnimation(VideoFragment.this.getContext(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(VideoFragment.this.getContext(), R.anim.fade_out);
        mImageSW.setInAnimation(fadeIn);
        mImageSW.setOutAnimation(fadeOut);*/

        run();

    }

    private void stop() {
        //TODO:......
      /*  Log.d(TAG, "stop: .....");
        if (mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }*/
    }

    private void run() {
        Log.d(TAG, "run:........");

        mContent = mViewModel.getContent(mPaneNum); //for first content
        if (mContent == null){
            Log.d(TAG, "mContent .....................................................................: ");
            return;
        }
        Log.d(TAG, "run: mContent.path=" + mContent.getFilePath());


        if (mContent.getFileType() == Definer.DEF_CONTENTS_TYPE_VIDEO) {
            mImageSW.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);

          /*  String fileName = IOUtils.getFilename(mContent.getFilePath());
            File file = IOUtils.getContentFile(this.getContext(), fileName);
            String UrlPath = file.getAbsolutePath();*/

            String UrlPath = IOUtils.getDspPlayContent(this.getContext(), mContent.getFilePath());
            Log.d(TAG, "video run: path=" + UrlPath);
            mVideoView.setVideoURI(Uri.parse(UrlPath));
            mVideoView.start();


        } else if (mContent.getFileType() == Definer.DEF_CONTENTS_TYPE_IMAGE) {

            mVideoView.setVisibility(View.GONE);
            mImageSW.setVisibility(View.VISIBLE);
/*
              String fileName = IOUtils.getFilename(mContent.getFilePath());
            File file = IOUtils.getContentFile(this.getContext(), fileName);
           // String UrlPath = file.getAbsolutePath();*/
            String UrlPath = IOUtils.getDspPlayContent(this.getContext(), mContent.getFilePath());

           /* Glide.with(this)
                    .load(UrlPath)
                    .asBitmap()
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            mImageSW.setImageDrawable(new BitmapDrawable(getResources(), resource));
                            return true;
                        }
                    }).into((ImageView) mImageSW.getCurrentView());*/
            Log.d(TAG, "image run: path=" + UrlPath);
            Glide.with(this)
                    .load(Uri.parse("file:///data/user/0/com.dignsys.dsdsp.dsdsp_9100/files/test-content/라이트.jpg"))
                    .override(mPaneEntity.getPaneWidth(), mPaneEntity.getPaneHeight())
                    .into(mImageSW);
/*
            Glide.with(this)
                    .load(Uri.parse(UrlPath))
                    .override(mPaneEntity.getPaneWidth(), mPaneEntity.getPaneHeight())
                    .into(mImageSW);*/

            //TODO:.....
        }


    }

    private void subscribe() {
        // Update the list when the data changes

        mViewModel.getContentPlayDone().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer pane_num) {
                Log.d(TAG, "onChanged: getContentPlayDone pane_num =" + String.valueOf(pane_num));
                if(pane_num <= 0) return;

                if (pane_num == mPaneNum) {
                    stop();
                    run();
                }
            }
        });

        mViewModel.getConfig().observe(getActivity(), new Observer<ConfigEntity>() {
            @Override
            public void onChanged(@Nullable ConfigEntity pane_num) {
                //TODO:apply dsdsp config to each play content

            }
        });

    }


    @Override
    public void onDetach() {
        super.onDetach();

        //TODO: release resource

    }

}
