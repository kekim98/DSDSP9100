package com.dignsys.dsdsp.dsdsp_9100.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.CommandHelper;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;

import java.io.File;

/**
 * Created by bawoori on 17. 12. 15.
 */

public class MessageUtil {

    private static final String TAG = MessageUtil.class.getSimpleName();




    public static Typeface getTypeface(Context context, int type)
    {
        Typeface tf = null;
        ConfigHelper DSLibIF = ConfigHelper.getInstance(context);
        CommandHelper DSComIF = CommandHelper.getInstance(context);
        String custFontPath = Definer.DEF_COMMAND_CONTENT_PATH + File.separator;

        int index = type==0?DSLibIF.getCapFont():DSLibIF.getTimeFont();

        switch(index)	{
            case 0:
                tf = Typeface.create(Typeface.DEFAULT,Typeface.NORMAL );
                break;
            case 1:
                tf = Typeface.createFromAsset(context.getAssets(), "fonts/NANUMGOTHIC.TTF");
                break;
            case 2:
                tf = Typeface.createFromAsset(context.getAssets(), "fonts/NANUMMYEONGJO.TTF");
                break;
            case 3:
                tf = Typeface.createFromFile(custFontPath + DSComIF.getUserFont(1));
                break;
            case 4:
                tf = Typeface.createFromFile(custFontPath + DSComIF.getUserFont(2));
                break;
            case 5:
                tf = Typeface.createFromFile(custFontPath + DSComIF.getUserFont(3));
                break;

        }

        return tf;
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



}
