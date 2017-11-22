package com.dignsys.dsdsp.dsdsp_9100.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by bawoori on 17. 11. 20.
 */

@Entity(tableName = "schedule_info")
public class ScheduleEntity {

    @PrimaryKey
    private int id; //m_nSceneIndex

    /*
    // schedule info
    */

   // private int format_num=0;
    private int point=0; //m_nPoint
    private int opS=0; //m_nOps
    private String opW=""; //m_strOpW
    private String opStartDate=""; //m_strOpStartDate
    private String opEndDate=""; //m_strOpEndDate
    private String opStartTime=""; //m_strOpStartTime
    private String opEndTime=""; //m_strOpEndTime


    public void setOpW(String opW) {
        this.opW = opW;
        this.point +=30;
    }

    public void setOpDate(String str)
    {
        this.opStartDate = str.substring(0, str.indexOf("-"));
        this.opEndDate = str.substring(str.indexOf("-") + 1);
        this.point += 10;

    }
    public void setOpTime(String str)
    {
        this.opStartTime = str.substring(0, str.indexOf("-")).replace(":", "");

        this.opEndTime		= str.substring(str.indexOf("-") + 1).replace(":", "");
        this.point += 6;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getOpS() {
        return opS;
    }

    public void setOpS(int opS) {
        this.opS = opS;
        this.point +=30;
    }

    public String getOpW() {
        return opW;
    }



    public String getOpStartDate() {
        return opStartDate;
    }

    public void setOpStartDate(String opStartDate) {
        this.opStartDate = opStartDate;
    }

    public String getOpEndDate() {
        return opEndDate;
    }

    public void setOpEndDate(String opEndDate) {
        this.opEndDate = opEndDate;
    }

    public String getOpStartTime() {
        return opStartTime;
    }

    public void setOpStartTime(String opStartTime) {
        this.opStartTime = opStartTime;
    }

    public String getOpEndTime() {
        return opEndTime;
    }

    public void setOpEndTime(String opEndTime) {
        this.opEndTime = opEndTime;
    }
}
