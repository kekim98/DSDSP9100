package com.dignsys.dsdsp.dsdsp_9100.db.entity;

/**
 * Created by bawoori on 17. 11. 20.
 */

public class Format {
   // private int format_num; //foreign key

    private int formatType; //m_nPanelType
    private int foramtWidth; //m_nResolutionW
    private int formatHeight; //m_nResolutionH

    public int getFormatType() {
        return formatType;
    }

    public void setFormatType(int formatType) {
        this.formatType = formatType;
    }

    public int getForamtWidth() {
        return foramtWidth;
    }

    public void setForamtWidth(int foramtWidth) {
        this.foramtWidth = foramtWidth;
    }

    public int getFormatHeight() {
        return formatHeight;
    }

    public void setFormatHeight(int formatHeight) {
        this.formatHeight = formatHeight;
    }
}
