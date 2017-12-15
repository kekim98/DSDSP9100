package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by bawoori on 17. 12. 15.
 */

public class MyVideoView extends VideoView  {
    private int m_nVideoW;
    private int m_nVideoH;
    private boolean m_bRatio;
    private int m_nW;
    private int m_nH;

    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

         if(m_nVideoW != 0 && m_nVideoH != 0)	{

            if(m_bRatio) {
                setMeasuredDimension(m_nW, m_nH);
            }else {
                int width = getDefaultSize(m_nVideoW, widthMeasureSpec);
                int height = getDefaultSize(m_nVideoH, heightMeasureSpec);

                if (m_nVideoW > 0 && m_nVideoH > 0) {
                    if (m_nVideoW * height > width * m_nVideoH) {
                        height = width * m_nVideoH / m_nVideoW;
                    }
                    else if (m_nVideoW * height < width * m_nVideoH) {
                        width = height * m_nVideoW / m_nVideoH;
                    }
                }

                setMeasuredDimension(width, height);
            }

        }
        else setMeasuredDimension(m_nW, m_nH);

    }


    public void setResizeMode(boolean mode) {
        m_bRatio = mode;
        requestLayout();
        invalidate();
    }

    public void setSize(int w, int h) {
        m_nW = w;
        m_nH = h;
    }

    public void setContentSize(int w, int h) {
        m_nVideoW = w;
        m_nVideoH = h;
    }

}
