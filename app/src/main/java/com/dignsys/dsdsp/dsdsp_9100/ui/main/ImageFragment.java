package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ScheduleHelper;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ScheduleViewModel;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = ImageFragment.class.getSimpleName();

    private int mPaneNum = 0;

    //private VideoView mVideoView;
    private ContentEntity mContent;
    private ScheduleViewModel mViewModel;
    private PaneEntity mPaneEntity;
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
    public static ImageFragment newInstance(int pane_num) {
        ImageFragment fragment = new ImageFragment();
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

        View view = inflater.inflate(R.layout.fragment_image, container, false);

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

      //  mVideoView = view.findViewById(R.id.videoView);
        mImageSW = view.findViewById(R.id.imageSW);

        mViewModel = ViewModelProviders.of(getActivity()).get(ScheduleViewModel.class);
        subscribe();

        mImageSW.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView iv = new ImageView(ImageFragment.this.getContext());
                return iv;
            }
        });

        Animation in = AnimationUtils.loadAnimation(ImageFragment.this.getContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(ImageFragment.this.getContext(), android.R.anim.slide_out_right);
        mImageSW.setInAnimation(in);
        mImageSW.setOutAnimation(out);


        run();

    }

    private void stop() {
        //TODO:......
    }

    private void run() {
        Log.d(TAG, "run:........");
        mContent = mViewModel.getContent(mPaneNum); //for first content
        if (mContent == null){
            Log.d(TAG, "mContent null");
            return;
        }

        try {
            String UrlPath = IOUtils.getDspPlayContent(this.getContext(), mContent.getFilePath());
            Log.d(TAG, "image run: path=" + UrlPath);

            Glide.with(this)
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
                    }).into((ImageView) mImageSW.getCurrentView());

        } catch (NullPointerException e) {
            Log.e(TAG, "run: NullPointException");
        }

    }

    private void subscribe() {
        // Update the list when the data changes


        ScheduleHelper.getInstance(getContext()).getContentPlayDone().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer pane_num) {
                Log.d(TAG, "onChanged: bawoori getContentPlayDone pane_num =" + String.valueOf(pane_num));
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
                Log.d(TAG, "onChanged: getScheduleDone pane_num =");
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
