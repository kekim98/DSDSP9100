package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;
import com.dignsys.dsdsp.dsdsp_9100.util.MessageUtil;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;

import java.io.IOException;


public class MessageFragmentMain extends MainBaseFragment implements ViewSwitcher.ViewFactory {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = MessageFragmentMain.class.getSimpleName();


    private ContentEntity mContent;
    private TextSwitcher mTextSW;
    private ImageView mImageView;
    private View mView;
    private TextView mTv;

    public MessageFragmentMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragmentMain.
     */
    public static MessageFragmentMain newInstance(int pane_num) {
        MessageFragmentMain fragment = new MessageFragmentMain();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_message, container, false);

        makeLayout(mView);

        return mView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        // TODO: Rename and change types and number of view

        mTextSW = view.findViewById(R.id.textSW);
        mImageView = view.findViewById(R.id.imageView);

        mTextSW.setFactory(this);


        run();

    }

    @Override
    void stop() {
        //TODO:......
    }

    @Override
    protected void applyConfig(ConfigEntity config) {
        if (mTextSW != null /*&& mTextSW.isActivated()*/) {
            MessageUtil.setCaptionEffect(getContext(), mTextSW, true);
          setTextProperty();
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
        // Log.d(TAG, "run: mContent.path=" + mContent.getFilePath());


        int fileType = mContent.getFileType();
        if (fileType == Definer.DEF_CONTENTS_TYPE_TEXT
                || fileType == Definer.DEF_CONTENTS_TYPE_RSS) {
            mImageView.setVisibility(View.GONE);
            mTextSW.setVisibility(View.VISIBLE);

            String UrlPath = "";
            String message = "";
            if (fileType == Definer.DEF_CONTENTS_TYPE_TEXT) {
                UrlPath = IOUtils.getDspPlayContent(this.getContext(), mContent.getFilePath());
                try {
                    message = IOUtils.readFileAsString(IOUtils.getContentFile(this.getContext(), IOUtils.getFilename(UrlPath)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

            }

            mTextSW.setText(message);

            if (mTextSW != null) {
                MessageUtil.setCaptionEffect(getContext(), mTextSW, true);
            }


        } else if (fileType == Definer.DEF_CONTENTS_TYPE_IMAGE) {
            mTextSW.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);

            //TODO:.....
        }


    }


    @Override
    public View makeView() {
        mTv = new TextView(this.getContext());
        setTextProperty();

        return mTv;
    }

    private void setTextProperty() {
        ConfigHelper DSLibIF = ConfigHelper.getInstance(getContext());
        int pos = DSLibIF.getCapPosition();
        int fontSize = DSLibIF.getCapSize();

        //TODO : miss match caption message type and position
     /*   if (pos == Definer.DEF_MESSAGE_TYPE_STATIC_LEFT) {
            tv.setGravity(Gravity.LEFT | Gravity.CENTER_HORIZONTAL);
        } else if (pos == Definer.DEF_MESSAGE_TYPE_STATIC_RIGHT) {
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_HORIZONTAL);
        } else if (pos == Definer.DEF_MESSAGE_TYPE_STATIC_TOP) {
            tv.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        } else if (pos == Definer.DEF_MESSAGE_TYPE_STATIC_BOTTOM) {
            tv.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        } else if(pos == Definer.DEF_MESSAGE_TYPE_STATIC_MIDDLE){
            tv.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        }*/

        Log.d(TAG, "makeView: pos=" + pos);

        mTv.setTextSize(fontSize);
        mTv.setTextColor(MessageUtil.getColor(DSLibIF.getCapColor()));
        mView.setBackgroundColor(MessageUtil.getColor(DSLibIF.getCapBGColor()));
    }
}
