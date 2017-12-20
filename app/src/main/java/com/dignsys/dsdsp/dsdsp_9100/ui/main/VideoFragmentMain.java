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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.GlideApp;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;
import com.dignsys.dsdsp.dsdsp_9100.util.ImageUtil;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link VideoFragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragmentMain extends MainBaseFragment implements ViewSwitcher.ViewFactory {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = VideoFragmentMain.class.getSimpleName();

    private MyVideoView mVideoView;
    private ContentEntity mContent;
  //  private MainViewModel mViewModel;
    private ImageSwitcher mImageSW;
    private RequestOptions mPicOptions;




    public VideoFragmentMain() {
        // Required empty public constructor
    }

    static VideoFragmentMain newInstance(int pane_num) {
        VideoFragmentMain fragment = new VideoFragmentMain();
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

        mVideoView = view.findViewById(R.id.videoView);
        mVideoView.setZOrderOnTop(true);
        mImageSW = view.findViewById(R.id.imageSW);
        mVideoView.setSize(getW(), getH());
        mImageSW.setFactory(this);
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
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "onPrepared: ");
                mVideoView.setContentSize(mp.getVideoWidth(), mp.getVideoHeight());
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        run();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mContent != null && mContent.getFileType() == Definer.DEF_CONTENTS_TYPE_VIDEO) {
            run();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mContent != null && mContent.getFileType() == Definer.DEF_CONTENTS_TYPE_VIDEO) {
            stop();
        }

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
    protected void applyConfig(ConfigEntity config) {

        if (config.getUseAutoResizeImage() == Definer.DEF_USE) {
            setPicOptions(true);
        } else {
            setPicOptions(false);
        }

        if (config.getUseAutoResizeMovie() == Definer.DEF_USE) {
            if (mVideoView != null) {

                mVideoView.setResizeMode(true);
            }

        } else{
            if (mVideoView != null) {

                mVideoView.setResizeMode(false);
            }
        }

    }

    private void setPicOptions(boolean mode) {
        if (mode) {
            mPicOptions = new RequestOptions()
                    .centerCrop()
                    .override(getW(), getH());

        } else {
            mPicOptions = new RequestOptions();
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

                ImageUtil.setPicEffect(getContext(), mImageSW);

                GlideApp.with(this)
                        .load(new File(UrlPath))
                        .apply(mPicOptions)
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

    @Override
    public View makeView() {
        ImageView iv = new ImageView(this.getContext());
        return iv;
    }
}
