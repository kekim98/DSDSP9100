package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.graphics.drawable.Drawable;
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
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ImageFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = ImageFragment.class.getSimpleName();

    ContentEntity mContent;

    //   private ImageView mImageSW;
    private ImageSwitcher mImageSW;


    public ImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragment.
     */

    static ImageFragment newInstance(int pane_num) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image, container, false);

        makeLayout(view);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Rename and change types and number of view

        //  mVideoView = view.findViewById(R.id.videoView);
        mImageSW = view.findViewById(R.id.imageSW);


        mImageSW.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView iv = new ImageView(ImageFragment.this.getContext());
                return iv;
            }
        });

      /*  Animation in = AnimationUtils.loadAnimation(ImageFragment.this.getContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(ImageFragment.this.getContext(), android.R.anim.slide_out_right);
      Animation in = AnimationUtils.loadAnimation(ImageFragment.this.getContext(), android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(ImageFragment.this.getContext(), android.R.anim.fade_out);*/
        Animation in = AnimationUtils.loadAnimation(ImageFragment.this.getContext(), R.anim.slide_up);
        Animation out = AnimationUtils.loadAnimation(ImageFragment.this.getContext(), R.anim.slide_down);

        mImageSW.setInAnimation(in);
        mImageSW.setOutAnimation(out);


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
            String UrlPath = IOUtils.getDspPlayContent(this.getContext(), mContent.getFilePath());
            Log.d(TAG, "image run: path=" + UrlPath);

            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .format(DecodeFormat.PREFER_RGB_565))
                    .load(new File(UrlPath))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            mImageSW.setImageDrawable(resource);
                        }
                    });

           /*Glide.with(this)
                        .load(new File(UrlPath))
                        .asBitmap()
                        .listener(new RequestListener<File, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, File model, Target<Bitmap> target, boolean isFirstResource) {
                                Log.d(TAG, "onException: glide Exception...");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, File model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d(TAG, "onResourceReady: isFirstResource=" + isFirstResource);
                                mImageSW.setImageDrawable(new BitmapDrawable(getResources(), resource));
                                return true;
                                //   return false;
                            }
                        }).into((ImageView) mImageSW.getCurrentView());*/


        } catch (NullPointerException e) {
            Log.e(TAG, "run: NullPointException");
        }

    }

}
