/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.transition.Scene;


@Entity(tableName = "pane_info", primaryKeys = {"scene_id", "pane_id"})
public class PaneEntity { //AreaInfo

    private int scene_id; //foreign key
    private int pane_id; //m_nAreaNum

    //V:video/picture, P:picture, M:message, C:clock, B:background color
    //D:DTV, W:webview(picture), T:webview(text), S:screen size
    private String paneType=""; //m_strType

    private int paneX=0;
    private int paneY=0;
    private int paneWidth=0;
    private int paneHeight=0;

    private int opDirectionMode=0; //m_nOpDirectionMode
    private int opDirection=0; //m_nOpDirection
    private int opFontSize=0; //m_nOpFontSize
    private int opMsgSpeed=0; //m_nOpSpeed
    private int opModulationType=0; //m_nOpModulationType
    private int opReceiveType=0; //m_nOpReceiveType
    private int opTransparency=-1; //m_strOpTransparency
    private int opFontColor=-1; //m_strOpFontColor
    private int opBackColor=-1; //m_strOpBackColor
    private String opTimeFormat=""; //m_strOpTime
    private String opAlign="";
    private String opChannelNum=""; //ex, DTV:11-1

    public int getScene_id() {
        return scene_id;
    }

    public void setScene_id(int scene_id) {
        this.scene_id = scene_id;
    }

    public int getPane_id() {
        return pane_id;
    }

    public void setPane_id(int pane_id) {
        this.pane_id = pane_id;
    }

    public String getPaneType() {
        return paneType;
    }

    public void setPaneType(String paneType) {
        this.paneType = paneType;
    }

    public int getPaneX() {
        return paneX;
    }

    public void setPaneX(int paneX) {
        this.paneX = paneX;
    }

    public int getPaneY() {
        return paneY;
    }

    public void setPaneY(int paneY) {
        this.paneY = paneY;
    }

    public int getPaneWidth() {
        return paneWidth;
    }

    public void setPaneWidth(int paneWidth) {
        this.paneWidth = paneWidth;
    }

    public int getPaneHeight() {
        return paneHeight;
    }

    public void setPaneHeight(int paneHeight) {
        this.paneHeight = paneHeight;
    }

    public int getOpDirectionMode() {
        return opDirectionMode;
    }

    public void setOpDirectionMode(int opDirectionMode) {
        this.opDirectionMode = opDirectionMode;
    }

    public int getOpDirection() {
        return opDirection;
    }

    public void setOpDirection(int opDirection) {
        this.opDirection = opDirection;
    }

    public int getOpFontSize() {
        return opFontSize;
    }

    public void setOpFontSize(int opFontSize) {
        this.opFontSize = opFontSize;
    }

    public int getOpMsgSpeed() {
        return opMsgSpeed;
    }

    public void setOpMsgSpeed(int opMsgSpeed) {
        this.opMsgSpeed = opMsgSpeed;
    }

    public int getOpModulationType() {
        return opModulationType;
    }

    public void setOpModulationType(int opModulationType) {
        this.opModulationType = opModulationType;
    }

    public int getOpReceiveType() {
        return opReceiveType;
    }

    public void setOpReceiveType(int opReceiveType) {
        this.opReceiveType = opReceiveType;
    }

    public int getOpTransparency() {
        return opTransparency;
    }

    public void setOpTransparency(int opTransparency) {
        this.opTransparency = opTransparency;
    }

    public int getOpFontColor() {
        return opFontColor;
    }

    public void setOpFontColor(int opFontColor) {
        this.opFontColor = opFontColor;
    }

    public int getOpBackColor() {
        return opBackColor;
    }

    public void setOpBackColor(int opBackColor) {
        this.opBackColor = opBackColor;
    }

    public String getOpTimeFormat() {
        return opTimeFormat;
    }

    public void setOpTimeFormat(String opTimeFormat) {
        this.opTimeFormat = opTimeFormat;
    }

    public String getOpAlign() {
        return opAlign;
    }

    public void setOpAlign(String opAlign) {
        this.opAlign = opAlign;
    }

    public String getOpChannelNum() {
        return opChannelNum;
    }

    public void setOpChannelNum(String opChannelNum) {
        this.opChannelNum = opChannelNum;
    }
}
