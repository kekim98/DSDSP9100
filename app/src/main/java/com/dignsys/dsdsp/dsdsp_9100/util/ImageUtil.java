package com.dignsys.dsdsp.dsdsp_9100.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.model.AnimInfo;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;

import java.util.Random;

/**
 * Created by bawoori on 17. 12. 15.
 */

public class ImageUtil {

    public static AnimInfo getAnim(Context context, int type) {
        AnimInfo animInfo = new AnimInfo();
        Animation in = null;
        Animation out = null;

        if (type == Definer.DEF_PIC_EFFECT_NONE) {
            animInfo.in = null;
            animInfo.out = null;

        } else if (type == Definer.DEF_PIC_EFFECT_CROSS_FADE) {
            in = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            out = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        } else if (type == Definer.DEF_PIC_EFFECT_RIGHT) {

            in = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
            out = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);

        } else if (type == Definer.DEF_PIC_EFFECT_LEFT) {
            in = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
            out = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);

        } else if (type == Definer.DEF_PIC_EFFECT_UP) {
            in = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
            out = AnimationUtils.loadAnimation(context, R.anim.slide_out_down);

        } else if (type == Definer.DEF_PIC_EFFECT_DOWN) {
            in = AnimationUtils.loadAnimation(context, R.anim.slide_in_down);
            out = AnimationUtils.loadAnimation(context, R.anim.slide_out_up);

        } else if (type == Definer.DEF_PIC_EFFECT_GROWING) {
            in = AnimationUtils.loadAnimation(context, R.anim.grow_fade_in_center);
            out = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        }

        animInfo.in = in;
        animInfo.out = out;

        return animInfo;
    }

    public static void setPicEffect(Context context, ImageSwitcher view) {
        int type = ConfigHelper.getInstance(context).getPicChangeEffect();

        if (type == 7) {
            Random random = new Random();

            type = random.nextInt(7);
        }

        AnimInfo imageAnim = ImageUtil.getAnim(context, type);

        view.setInAnimation(imageAnim.in);
        view.setOutAnimation(imageAnim.out);
    }

    public static int getPicChangeEffectValue(String str)
    {
        int n = 0;

        if(str.equals("disable")) 	n = 0;
        if(str.equals("downward")) 	n = 1;
        if(str.equals("crossfade")) n = 2;
        if(str.equals("leftward")) 	n = 3;
        if(str.equals("rightward")) n = 4;
        if(str.equals("upward")) 	n = 5;
        if(str.equals("increase")) 	n = 6;
        if(str.equals("random")) 	n = 7;

        return n;

    }

}
