package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.model.AnimInfo;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;
import com.dignsys.dsdsp.dsdsp_9100.util.MessageUtil;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;

import java.io.IOException;
import java.util.HashMap;


public class MessageFragmentMain extends MainBaseFragment implements ViewSwitcher.ViewFactory, Animation.AnimationListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = MessageFragmentMain.class.getSimpleName();
    private static final String MESSAGE_KEY = "message-key";
    private static final String RSS_KEY = "rss-key";

    private static final String[] MESSAGE_LIST_ORDER = {
            MESSAGE_KEY,
            RSS_KEY
    };


    private ContentEntity mContent;
    private TextSwitcher mTextSW;
    private ImageView mImageView;
    private View mView;
    private TextView mTv;


    private HashMap<String, String> messageMap = new HashMap<>();
    private int messageIndex = 0;
    private ConfigHelper DSLibIF;


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

        DSLibIF = ConfigHelper.getInstance(getContext());

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
            String rssTitle = DSLibIF.getRssTitle();
            String rssDesc = DSLibIF.getRssDesc();
            int type = DSLibIF.getRSSCaptionMode();

            if (type == Definer.DEF_CFG_MESSAGE_TYPE_SCROLL) {
               // MessageUtil.setCaptionEffect(getContext(), mTextSW, true, this);
               // getScrollAnimation
                if (DSLibIF.getCapMode() == Definer.DEF_CFG_ITEM_VALUE_CAP_TEXT_RSS) {
                    if (DSLibIF.getCapRSSMode() == Definer.DEF_MESSAGE_RSS_MODE_DESC) {
                        messageMap.put(RSS_KEY, rssDesc);
                    } else if (DSLibIF.getCapRSSMode() == Definer.DEF_MESSAGE_RSS_MODE_TITLE) {
                        messageMap.put(RSS_KEY, rssTitle);
                    } else {
                        messageMap.put(RSS_KEY, rssTitle + rssDesc);
                    }
                } else {
                    messageMap.remove(RSS_KEY);
                }
            }

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
        messageMap.clear();


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

                    messageMap.put(MESSAGE_KEY, message);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

            }

            mTextSW.setText(messageMap.get(MESSAGE_LIST_ORDER[++messageIndex % messageMap.size()]));
            float width = getTextLength(message);
            Animation ani= getScrollAnimation(width);
            mTextSW.startAnimation(ani);
            ani.setAnimationListener(this);




        } else if (fileType == Definer.DEF_CONTENTS_TYPE_IMAGE) {
            mTextSW.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);

            //TODO:.....
        }

    }

    private float getTextLength(String text) {
        int fontSize = DSLibIF.getCapSize();
        Paint paint = new Paint();

        paint.setTypeface(Typeface.DEFAULT);// your preference here
        paint.setTextSize(fontSize);// have this the same as your text size


        return paint.measureText(text);
    }


    @Override
    public View makeView() {
        mTv = new TextView(this.getContext());
        setTextProperty();

        return mTv;
    }

    private void setTextProperty() {

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

        mTv.setSingleLine();
        mTv.setTextSize(fontSize);
        mTv.setTypeface(MessageUtil.getTypeface(getContext(), 0));
        mTv.setTextColor(MessageUtil.getColor(DSLibIF.getCapColor()));
        mView.setBackgroundColor(MessageUtil.getColor(DSLibIF.getCapBGColor()));
    }



    public  Animation getScrollAnimation(float width) {
        Animation in;
        int speed = DSLibIF.getCapSpeed();

        float toX =  (-1.0f * width/getW());
        Log.d(TAG, "getScrollAnimation: toX=" + toX);

        in = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, toX,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);

        in.setInterpolator(new LinearInterpolator());
        in.setFillEnabled(true);
        in.setFillAfter(true); // 애니메이션 후 이동한좌표에

        if (speed == Definer.DEF_CAP_SPEED_FAST) {
            in.setDuration(10000 );
        } else if (speed == Definer.DEF_CAP_SPEED_NORMAL) {
            in.setDuration(15000);
        } else {
            in.setDuration(20000);
        }

        in.setAnimationListener(this);

        return in;
    }


    public  void setCaptionEffect(Context context, View view, boolean isText) {
        if (view == null) return;

        int type = DSLibIF.getRSSCaptionMode();
        int speed = DSLibIF.getCapSpeed();
        AnimInfo anim = getAnim(context, type);

        if (isText) {

            if (anim.out == null) {
                if (type == Definer.DEF_CFG_MESSAGE_TYPE_SCROLL) {
                    // Animation animation = AnimationUtils.loadAnimation(context, R.anim.text_scroll_right);

                    if (speed == Definer.DEF_CAP_SPEED_FAST) {
                        anim.in.setDuration(10000);
                    } else if (speed == Definer.DEF_CAP_SPEED_NORMAL) {
                        anim.in.setDuration(15000);
                    } else {
                        anim.in.setDuration(20000);
                    }
                }
                // anim.in.set
                view.startAnimation(anim.in);
            } else {
                ((TextSwitcher) view).setInAnimation(anim.in);
                ((TextSwitcher) view).setOutAnimation(anim.out);
            }

        } else {
            AnimInfo imageAnim = getAnim(context, type);
            ((ImageSwitcher)view).setInAnimation(imageAnim.in);
            ((ImageSwitcher)view).setOutAnimation(imageAnim.out);
        }

        anim.in.setAnimationListener(MessageFragmentMain.this);


    }

    public static AnimInfo getAnim(Context context, int type) {
        AnimInfo animInfo = new AnimInfo();
        Animation in = null;
        Animation out = null;

        /*if (type == Definer.DEF_MESSAGE_TYPE_SCROLL) {
            //  in = AnimationUtils.loadAnimation(context, R.anim.text_scroll_right);
            in = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, -0.3f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);

            in.setInterpolator(new LinearInterpolator());
            in.setFillEnabled(true);
            in.setFillAfter(true); // 애니메이션 후 이동한좌표에
            in.setDuration(10000); //지속시간]
            //in.setToX
            out = null;

        } else */if (type == Definer.DEF_MESSAGE_TYPE_WRAP_UP) {

            in = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
            out = null;

        } else if (type == Definer.DEF_MESSAGE_TYPE_WRAP_DOWN) {
            in = AnimationUtils.loadAnimation(context, R.anim.push_down_in);
            out = null;

        } else if (type == Definer.DEF_MESSAGE_TYPE_WRAP_STOP_UP) {
            in = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
            out = AnimationUtils.loadAnimation(context, R.anim.push_down_out);

        } else if (type == Definer.DEF_MESSAGE_TYPE_WRAP_STOP_DOWN) {
            in = AnimationUtils.loadAnimation(context, R.anim.push_down_in);
            out = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
        }

        animInfo.in = in;
        animInfo.out = out;

        return animInfo;
    }


    @Override
    public void onAnimationStart(Animation animation) {
        Log.d(TAG, "onAnimationStart: ");
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d(TAG, "onAnimationEnd: ");

        String msg = messageMap.get(MESSAGE_LIST_ORDER[++messageIndex % messageMap.size()]);
        float width = getTextLength(msg);
        Animation ani= getScrollAnimation(width);
        ani.setAnimationListener(this);

       // mTextSW.setText(msg);
        mTextSW.setCurrentText(msg);

        mTextSW.startAnimation(ani);

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        Log.d(TAG, "onAnimationRepeat: ");

    }
}
