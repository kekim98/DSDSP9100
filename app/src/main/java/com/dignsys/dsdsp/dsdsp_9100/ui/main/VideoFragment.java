package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.graphics.drawable.Drawable;
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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.VideoView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.GlideApp;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = VideoFragment.class.getSimpleName();

    private VideoView mVideoView;
    private ContentEntity mContent;
  //  private MainViewModel mViewModel;
    private ImageSwitcher mImageSW;

    public VideoFragment() {
        // Required empty public constructor
    }

    static VideoFragment newInstance(int pane_num) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video, container, false);

        makeLayout(view);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Rename and change types and number of view

        mVideoView = view.findViewById(R.id.videoView);
        //mVideoView.setZOrderMediaOverlay(true);
        mVideoView.setZOrderOnTop(true);
        mImageSW = view.findViewById(R.id.imageSW);


/*        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        subscribe();*/

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

        mImageSW.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView iv = new ImageView(VideoFragment.this.getContext());
                return iv;
            }
        });

        // Set animations
        // https://danielme.com/2013/08/18/diseno-android-transiciones-entre-activities/
        Animation in = AnimationUtils.loadAnimation(VideoFragment.this.getContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(VideoFragment.this.getContext(), android.R.anim.slide_out_right);
        mImageSW.setInAnimation(in);
        mImageSW.setOutAnimation(out);
       /* Animation fadeIn = AnimationUtils.loadAnimation(VideoFragment.this.getContext(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(VideoFragment.this.getContext(), R.anim.fade_out);
        mImageSW.setInAnimation(fadeIn);
        mImageSW.setOutAnimation(fadeOut);*/

        run();

    }

    @Override
    void stop() {
        //TODO:......
        Log.d(TAG, "stop: .....");
        if (mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }
    }

    @Override
    void run() {
        Log.d(TAG, "bawoori1 run:........");

        mContent = mViewModel.getContent(mPaneNum); //for first content
        if (mContent == null) {
            Log.d(TAG, "bawoori1 run:mContent null");
            return;
        }
        // Log.d(TAG, "run: mContent.path=" + mContent.getFilePath());


        if (mContent.getFileType() == Definer.DEF_CONTENTS_TYPE_VIDEO) {
            mImageSW.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);

            try {
                String UrlPath = IOUtils.getDspPlayContent(this.getContext(), mContent.getFilePath());
                Log.d(TAG, "video run: path=" + UrlPath);
                mVideoView.setVideoURI(Uri.parse(UrlPath));
                mVideoView.start();

            } catch (NullPointerException e) {

            }


        } else if (mContent.getFileType() == Definer.DEF_CONTENTS_TYPE_IMAGE) {

            mVideoView.setVisibility(View.GONE);
            mImageSW.setVisibility(View.VISIBLE);

            try {
                String UrlPath = IOUtils.getDspPlayContent(this.getContext(), mContent.getFilePath());
                Log.d(TAG, "image run: path=" + UrlPath);

                GlideApp.with(this)
                        .load(new File(UrlPath))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                mImageSW.setImageDrawable(resource);
                            }
                        });


            } catch (NullPointerException e) {
                Log.e(TAG, "run: NullPointException");
            }

        }


    }


}
