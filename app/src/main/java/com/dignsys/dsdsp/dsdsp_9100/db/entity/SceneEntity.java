package com.dignsys.dsdsp.dsdsp_9100.db.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "scene_info")
public class SceneEntity {
    @PrimaryKey
    private int scene_num=0;

    private int schedule_id; //foreign key
    private int formatNum=0;
    private boolean opBGMMode = false;
    private String opPlayTime="";

    @Embedded
    Format format;


    public int getScene_num() {
        return scene_num;
    }

    public void setScene_num(int scene_num) {
        this.scene_num = scene_num;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public int getFormatNum() {
        return formatNum;
    }

    public void setFormatNum(int formatNum) {
        this.formatNum = formatNum;
    }

    public boolean isOpBGMMode() {
        return opBGMMode;
    }

    public void setOpBGMMode(boolean opBGMMode) {
        this.opBGMMode = opBGMMode;
    }

    public String getOpPlayTime() {
        return opPlayTime;
    }

    public void setOpPlayTime(String opPlayTime) {
        this.opPlayTime = opPlayTime;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
}
