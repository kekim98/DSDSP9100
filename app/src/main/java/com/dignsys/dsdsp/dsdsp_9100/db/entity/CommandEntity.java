package com.dignsys.dsdsp.dsdsp_9100.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "command_info")
public class CommandEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String fwFilePath;
    private String fontFilePath;
    private boolean powerOff;
    private boolean powerOn;
    private boolean reboot;

    private boolean sdFormat;
    private boolean sdDelete;
    private boolean usbSync;
    private boolean usbCopy;


    public boolean isSdFormat() {
        return sdFormat;
    }

    public void setSdFormat(boolean sdFormat) {
        this.sdFormat = sdFormat;
    }

    public boolean isSdDelete() {
        return sdDelete;
    }

    public void setSdDelete(boolean sdDelete) {
        this.sdDelete = sdDelete;
    }

    public boolean isUsbSync() {
        return usbSync;
    }

    public void setUsbSync(boolean usbSync) {
        this.usbSync = usbSync;
    }

    public boolean isUsbCopy() {
        return usbCopy;
    }

    public void setUsbCopy(boolean usbCopy) {
        this.usbCopy = usbCopy;
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

    public String getFontFilePath() {
        return fontFilePath;
    }

    public void setFontFilePath(String fontFilePath) {
        this.fontFilePath = fontFilePath;
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
