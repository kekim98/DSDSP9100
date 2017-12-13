package com.dignsys.dsdsp.dsdsp_9100.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "command_info")
public class CommandEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String fwFilePath;
    private String fontFilePath_1;
    private String fontFilePath_2;
    private String fontFilePath_3;
    private boolean powerOff;
    private boolean powerOn;
    private boolean reboot;

    public String getFontFilePath_1() {
        return fontFilePath_1;
    }

    public void setFontFilePath_1(String fontFilePath_1) {
        this.fontFilePath_1 = fontFilePath_1;
    }

    public String getFontFilePath_2() {
        return fontFilePath_2;
    }

    public void setFontFilePath_2(String fontFilePath_2) {
        this.fontFilePath_2 = fontFilePath_2;
    }

    public String getFontFilePath_3() {
        return fontFilePath_3;
    }

    public void setFontFilePath_3(String fontFilePath_3) {
        this.fontFilePath_3 = fontFilePath_3;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFwFilePath() {
        return fwFilePath;
    }

    public void setFwFilePath(String fwFilePath) {
        this.fwFilePath = fwFilePath;
    }



    public boolean isPowerOff() {
        return powerOff;
    }

    public void setPowerOff(boolean powerOff) {
        this.powerOff = powerOff;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public void setPowerOn(boolean powerOn) {
        this.powerOn = powerOn;
    }

    public boolean isReboot() {
        return reboot;
    }

    public void setReboot(boolean reboot) {
        this.reboot = reboot;
    }

}
