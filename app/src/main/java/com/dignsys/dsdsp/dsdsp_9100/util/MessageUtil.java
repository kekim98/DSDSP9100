package com.dignsys.dsdsp.dsdsp_9100.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.TextSwitcher;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.model.AnimInfo;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;

/**
 * Created by bawoori on 17. 12. 15.
 */

public class MessageUtil {

    public static AnimInfo getAnim(Context context, int type) {
        AnimInfo animInfo = new AnimInfo();
        Animation in = null;
        Animation out = null;

        if (type == Definer.DEF_MESSAGE_TYPE_SCROLL) {
            in = AnimationUtils.loadAnimation(context, R.anim.text_scroll_right);
            out = null;

        } else if (type == Definer.DEF_MESSAGE_TYPE_WRAP_UP) {

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

    public static void setCaptionEffect(Context context, View view, boolean isText) {
        int type = ConfigHelper.getInstance(context).getRSSCaptionMode();
        int speed = ConfigHelper.getInstance(context).getCapSpeed();
        AnimInfo anim = MessageUtil.getAnim(context, type);

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

                  //  view.startAnimation(animation);
                }
                view.startAnimation(anim.in);
            } else {
                ((TextSwitcher) view).setInAnimation(anim.in);
                ((TextSwitcher) view).setOutAnimation(anim.out);
            }

        } else {
            AnimInfo imageAnim = MessageUtil.getAnim(context, type);
            ((ImageSwitcher)view).setInAnimation(imageAnim.in);
            ((ImageSwitcher)view).setOutAnimation(imageAnim.out);
        }

    }

    public static  int getColor(int color)
    {
        switch(color)	{
            case 0:	// Red
               return Color.RED;
            case 1:	// Orange
                return (Color.parseColor("#FFA500"));
            case 2:	// Yellow
                return Color.YELLOW;
            case 3:	// Green
                return Color.GREEN;
            case 4:	// Blue
                return Color.BLUE;
            case 5:	// Navy
                return (Color.parseColor("#000080"));
            case 6:	// Purple
                return (Color.parseColor("#800080"));
            case 7:	// Black
                return Color.BLACK;
            case 8:	// White
                return Color.WHITE;
            case 9:	// Trans
                return Color.TRANSPARENT;
        }
            return Color.BLACK;
    }


    public static int getRssCaptionModeValue(String str)
    {

        int n = 0;

        if(str.equals("scroll")) 			n = Definer.DEF_MESSAGE_TYPE_SCROLL;
        if(str.equals("left")) 		        n = Definer.DEF_MESSAGE_TYPE_STATIC_LEFT;
        if(str.equals("right")) 		    n = Definer.DEF_MESSAGE_TYPE_STATIC_RIGHT;
        if(str.equals("top")) 		        n = Definer.DEF_MESSAGE_TYPE_STATIC_TOP;
        if(str.equals("bottom")) 	    	n = Definer.DEF_MESSAGE_TYPE_STATIC_BOTTOM;
        if(str.equals("center")) 	    	n = Definer.DEF_MESSAGE_TYPE_STATIC_MIDDLE;
        if(str.equals("wrapupward")) 		n = Definer.DEF_MESSAGE_TYPE_WRAP_UP;
        if(str.equals("wrapdownward")) 		n = Definer.DEF_MESSAGE_TYPE_WRAP_DOWN;
        if(str.equals("wrapupwardstop")) 	n = Definer.DEF_MESSAGE_TYPE_WRAP_STOP_UP;
        if(str.equals("wrapdownwardstop")) 	n = Definer.DEF_MESSAGE_TYPE_WRAP_STOP_DOWN;

        return n;
    }

    public static int getColorValue(String str)
    {
        int n = 0;

        if(str.equals("red")) 			n = 0;
        if(str.equals("orange")) 		n = 1;
        if(str.equals("yellow")) 		n = 2;
        if(str.equals("green")) 		n = 3;
        if(str.equals("blue")) 			n = 4;
        if(str.equals("deepblue")) 		n = 5;
        if(str.equals("voilet")) 		n = 6;
        if(str.equals("black")) 		n = 7;
        if(str.equals("white")) 		n = 8;
        if(str.equals("transparent")) 	n = 9;
        return n;
    }

    public static int getCapSpeedValue(String str)
    {

        int n = 0;

        if(str.equals("quick")) 	n = 0;
        if(str.equals("normal")) 	n = 1;
        if(str.equals("slow")) 		n = 2;

        return n;
    }

    public static int getTimePositionValue(String str)
    {
        int n = 0;

        if(str.equals("disable")) 		n = 0;
        if(str.equals("lefttop")) 		n = 1;
        if(str.equals("righttop")) 		n = 2;
        if(str.equals("leftbottom")) 	n = 3;
        if(str.equals("rightbottom")) 	n = 4;

        return n;
    }



}
