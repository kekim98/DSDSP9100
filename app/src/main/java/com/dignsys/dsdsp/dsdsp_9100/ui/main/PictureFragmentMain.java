package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.graphics.drawable.Drawable;
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
 * Use the {@link PictureFragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */


public class PictureFragmentMain extends MainBaseFragment implements ViewSwitcher.ViewFactory {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = PictureFragmentMain.class.getSimpleName();

    ContentEntity mContent;

    //   private ImageView mImageSW;
    private ImageSwitcher mImageSW;
    private View mView;
    private RequestOptions mPicOptions;


    public PictureFragmentMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragmentMain.
     */

    static PictureFragmentMain newInstance(int pane_num) {
        PictureFragmentMain fragment = new PictureFragmentMain();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_picture, container, false);

        makeLayout(mView);
        mImageSW = mView.findViewById(R.id.imageSW);
        mImageSW.setFactory(this);

        return mView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        run();

    }



    @Override
    void stop() {

        //TODO:......
    }

    private void setPicResize(boolean mode) {
        if (mode) {
            mPicOptions = new RequestOptions()
                    .centerCrop()
                    .override(getW(), getH());

        } else {
            mPicOptions = new RequestOptions();
        }
    }

    @Override
    protected void applyConfig(ConfigEntity config) {

        if (config.getUseAutoResizeImage() == Definer.DEF_USE) {
            setPicResize(true);
        } else {
            setPicResize(false);
        }


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

    @Override
    public View makeView() {
        ImageView iv = new ImageView(getContext());
        return iv;
    }
}
