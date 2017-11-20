/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "content_info")
public class ContentEntity {


    @PrimaryKey(autoGenerate = true)
    private int id;
    /*
    // content info
    */
    private int scen_num; //foreign key
    //private int format_num;
    private int pane_num; //foreign key

    private String filePaht="";
    private int fileType =0;      //fm_nType
    private int opRunTime=0;    //003, m_nOpRunTime
    private int opNotPlay=0;    //005, m_nOpNotPlay
    private int opEffect=0;      //007, m_nOpEffect
    private int opAutoResize=0; //006, m_nOpAutoResize
   // private int op_pane_num;    //008,
    private int opMSGType=0;    //011, m_nOpMSGType
    private int opMSGPlayTime=0;    //012, m_nOpMSGPlayTime
    private int opNotDownload=0;    //030, m_nOpNotDownload
    private String opStartD="";  //001, m_strOpStartDT
    private String opEndDT="";    //002, m_strOpEndDT
    private String opBGMFile="";     //004, m_strOpBGMFile
    private String opMSGFile="";     //009, m_strOpMSGFile
    private int opStartX=0;  //010, mHashContnesPlaySize
    private int opStartY=0;
    private int opWidth=0;
    private int opHeight=0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScen_num() {
        return scen_num;
    }

    public void setScen_num(int scen_num) {
        this.scen_num = scen_num;
    }

    public int getPane_num() {
        return pane_num;
    }

    public void setPane_num(int pane_num) {
        this.pane_num = pane_num;
    }

    public String getFilePaht() {
        return filePaht;
    }

    public void setFilePaht(String filePaht) {
        this.filePaht = filePaht;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getOpRunTime() {
        return opRunTime;
    }

    public void setOpRunTime(int opRunTime) {
        this.opRunTime = opRunTime;
    }

    public int getOpNotPlay() {
        return opNotPlay;
    }

    public void setOpNotPlay(int opNotPlay) {
        this.opNotPlay = opNotPlay;
    }

    public int getOpEffect() {
        return opEffect;
    }

    public void setOpEffect(int opEffect) {
        this.opEffect = opEffect;
    }

    public int getOpAutoResize() {
        return opAutoResize;
    }

    public void setOpAutoResize(int opAutoResize) {
        this.opAutoResize = opAutoResize;
    }

    public int getOpMSGType() {
        return opMSGType;
    }

    public void setOpMSGType(int opMSGType) {
        this.opMSGType = opMSGType;
    }

    public int getOpMSGPlayTime() {
        return opMSGPlayTime;
    }

    public void setOpMSGPlayTime(int opMSGPlayTime) {
        this.opMSGPlayTime = opMSGPlayTime;
    }

    public int getOpNotDownload() {
        return opNotDownload;
    }

    public void setOpNotDownload(int opNotDownload) {
        this.opNotDownload = opNotDownload;
    }

    public String getOpStartD() {
        return opStartD;
    }

    public void setOpStartD(String opStartD) {
        this.opStartD = opStartD;
    }

    public String getOpEndDT() {
        return opEndDT;
    }

    public void setOpEndDT(String opEndDT) {
        this.opEndDT = opEndDT;
    }

    public String getOpBGMFile() {
        return opBGMFile;
    }

    public void setOpBGMFile(String opBGMFile) {
        this.opBGMFile = opBGMFile;
    }

    public String getOpMSGFile() {
        return opMSGFile;
    }

    public void setOpMSGFile(String opMSGFile) {
        this.opMSGFile = opMSGFile;
    }

    public int getOpStartX() {
        return opStartX;
    }

    public void setOpStartX(int opStartX) {
        this.opStartX = opStartX;
    }

    public int getOpStartY() {
        return opStartY;
    }

    public void setOpStartY(int opStartY) {
        this.opStartY = opStartY;
    }

    public int getOpWidth() {
        return opWidth;
    }

    public void setOpWidth(int opWidth) {
        this.opWidth = opWidth;
    }

    public int getOpHeight() {
        return opHeight;
    }

    public void setOpHeight(int opHeight) {
        this.opHeight = opHeight;
    }
}
