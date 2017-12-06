package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;


public class MessageFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = MessageFragment.class.getSimpleName();


    private ContentEntity mContent;
    private TextView mTextView;
    private ImageView mImageView;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragment.
     */
    public static MessageFragment newInstance(int pane_num) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message, container, false);

        makeLayout(view);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Rename and change types and number of view

        mTextView = view.findViewById(R.id.textView);
        mImageView = view.findViewById(R.id.imageView);
        /*String UrlPath = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.kkk;
        mVideoView.setVideoURI(Uri.parse(UrlPath));
        mVideoView.start();*/



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
        if (mContent == null){
            Log.d(TAG, "mContent null");
            return;
        }
       // Log.d(TAG, "run: mContent.path=" + mContent.getFilePath());


        if (mContent.getFileType() == Definer.DEF_CONTENTS_TYPE_TEXT) {
            mImageView.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);

            Animation bottomToTop = AnimationUtils.loadAnimation(MessageFragment.this.getContext(), R.anim.slide_in_left);
            mTextView.setText("Bawoori Test~~~");
            mTextView.startAnimation(bottomToTop);
           // bottomToTop.setRepeatCount(Animation.INFINITE);

        } else if (mContent.getFileType() == Definer.DEF_CONTENTS_TYPE_IMAGE) {
           mTextView.setVisibility(View.GONE);
           mImageView.setVisibility(View.VISIBLE);

            //TODO:.....
        }


    }


}
