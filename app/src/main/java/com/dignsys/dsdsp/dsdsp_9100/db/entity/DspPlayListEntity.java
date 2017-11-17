/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;



@Entity(tableName = "dsp_play_list")
public class DspPlayListEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    /*
    // schedule info
    */

    private int scen_num;
    private int format_num;
    private int scen_duration; //unit:second
    private int scen_bgm_mode;
    private String op_spec; //special qualifier(????)
    private String op_week; //week qualifier
    private String op_start_date;
    private String op_end_date;
    private String op_start_time;
    private String op_end_time;


    /*
    // content info
    */
    private String file_path;
    private String type;        //file type
    private int op_duration;    //003
    private int op_not_play;    //005
    private int op_effect;      //007
    private int op_auto_resize; //006
    private int op_pane_num;    //008
    private int op_msg_type;    //011
    private int op_msg_duration;    //012
    private int op_not_download;    //030
    private String op_start_dtime;  //001
    private String op_end_dtime;    //002
    private String op_bgm_file;     //004
    private String op_msg_file;     //009
    private int op_display_startX;  //010
    private int op_display_startY;
    private int op_display_width;
    private int op_display_height;

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

    public int getFormat_num() {
        return format_num;
    }

    public void setFormat_num(int format_num) {
        this.format_num = format_num;
    }

    public int getScen_duration() {
        return scen_duration;
    }

    public void setScen_duration(int scen_duration) {
        this.scen_duration = scen_duration;
    }

    public int getScen_bgm_mode() {
        return scen_bgm_mode;
    }

    public void setScen_bgm_mode(int scen_bgm_mode) {
        this.scen_bgm_mode = scen_bgm_mode;
    }

    public String getOp_spec() {
        return op_spec;
    }

    public void setOp_spec(String op_spec) {
        this.op_spec = op_spec;
    }

    public String getOp_week() {
        return op_week;
    }

    public void setOp_week(String op_week) {
        this.op_week = op_week;
    }

    public String getOp_start_date() {
        return op_start_date;
    }

    public void setOp_start_date(String op_start_date) {
        this.op_start_date = op_start_date;
    }

    public String getOp_end_date() {
        return op_end_date;
    }

    public void setOp_end_date(String op_end_date) {
        this.op_end_date = op_end_date;
    }

    public String getOp_start_time() {
        return op_start_time;
    }

    public void setOp_start_time(String op_start_time) {
        this.op_start_time = op_start_time;
    }

    public String getOp_end_time() {
        return op_end_time;
    }

    public void setOp_end_time(String op_end_time) {
        this.op_end_time = op_end_time;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOp_duration() {
        return op_duration;
    }

    public void setOp_duration(int op_duration) {
        this.op_duration = op_duration;
    }

    public int getOp_not_play() {
        return op_not_play;
    }

    public void setOp_not_play(int op_not_play) {
        this.op_not_play = op_not_play;
    }

    public int getOp_effect() {
        return op_effect;
    }

    public void setOp_effect(int op_effect) {
        this.op_effect = op_effect;
    }

    public int getOp_auto_resize() {
        return op_auto_resize;
    }

    public void setOp_auto_resize(int op_auto_resize) {
        this.op_auto_resize = op_auto_resize;
    }

    public int getOp_pane_num() {
        return op_pane_num;
    }

    public void setOp_pane_num(int op_pane_num) {
        this.op_pane_num = op_pane_num;
    }

    public int getOp_msg_type() {
        return op_msg_type;
    }

    public void setOp_msg_type(int op_msg_type) {
        this.op_msg_type = op_msg_type;
    }

    public int getOp_msg_duration() {
        return op_msg_duration;
    }

    public void setOp_msg_duration(int op_msg_duration) {
        this.op_msg_duration = op_msg_duration;
    }

    public int getOp_not_download() {
        return op_not_download;
    }

    public void setOp_not_download(int op_not_download) {
        this.op_not_download = op_not_download;
    }

    public String getOp_start_dtime() {
        return op_start_dtime;
    }

    public void setOp_start_dtime(String op_start_dtime) {
        this.op_start_dtime = op_start_dtime;
    }

    public String getOp_end_dtime() {
        return op_end_dtime;
    }

    public void setOp_end_dtime(String op_end_dtime) {
        this.op_end_dtime = op_end_dtime;
    }

    public String getOp_bgm_file() {
        return op_bgm_file;
    }

    public void setOp_bgm_file(String op_bgm_file) {
        this.op_bgm_file = op_bgm_file;
    }

    public String getOp_msg_file() {
        return op_msg_file;
    }

    public void setOp_msg_file(String op_msg_file) {
        this.op_msg_file = op_msg_file;
    }

    public int getOp_display_startX() {
        return op_display_startX;
    }

    public void setOp_display_startX(int op_display_startX) {
        this.op_display_startX = op_display_startX;
    }

    public int getOp_display_startY() {
        return op_display_startY;
    }

    public void setOp_display_startY(int op_display_startY) {
        this.op_display_startY = op_display_startY;
    }

    public int getOp_display_width() {
        return op_display_width;
    }

    public void setOp_display_width(int op_display_width) {
        this.op_display_width = op_display_width;
    }

    public int getOp_display_height() {
        return op_display_height;
    }

    public void setOp_display_height(int op_display_height) {
        this.op_display_height = op_display_height;
    }
}
