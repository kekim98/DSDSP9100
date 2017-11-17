/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "dsp_format")
public class DspFormatEntity  {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int format_num;
    private int pane_number;

    //V:video/picture, P:picture, M:message, C:clock, B:background color
    //D:DTV, W:webview(picture), T:webview(text), S:screen size
    private int pane_type;

    private int pane_startX;
    private int pane_startY;
    private int pane_width;
    private int pane_height;

    private int op_direction_mode;
    private int op_direction;
    private int op_font_size;
    private int op_msg_speed;
    private int op_modulation_type;
    private int op_receive_type;
    private int op_transparency;
    private int op_font_color;
    private int op_back_color;
    private String op_time_format;
    private String op_align;
    private String op_channel_num; //ex, DTV:11-1

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getFormat_num() {
        return format_num;
    }

    public void setFormat_num(int format_num) {
        this.format_num = format_num;
    }



    public int getPane_number() {
        return pane_number;
    }

    public void setPane_number(int pane_number) {
        this.pane_number = pane_number;
    }

    public int getPane_type() {
        return pane_type;
    }

    public void setPane_type(int pane_type) {
        this.pane_type = pane_type;
    }

    public int getPane_startX() {
        return pane_startX;
    }

    public void setPane_startX(int pane_startX) {
        this.pane_startX = pane_startX;
    }

    public int getPane_startY() {
        return pane_startY;
    }

    public void setPane_startY(int pane_startY) {
        this.pane_startY = pane_startY;
    }

    public int getPane_width() {
        return pane_width;
    }

    public void setPane_width(int pane_width) {
        this.pane_width = pane_width;
    }

    public int getPane_height() {
        return pane_height;
    }

    public void setPane_height(int pane_height) {
        this.pane_height = pane_height;
    }

    public int getOp_direction_mode() {
        return op_direction_mode;
    }

    public void setOp_direction_mode(int op_direction_mode) {
        this.op_direction_mode = op_direction_mode;
    }

    public int getOp_direction() {
        return op_direction;
    }

    public void setOp_direction(int op_direction) {
        this.op_direction = op_direction;
    }

    public int getOp_font_size() {
        return op_font_size;
    }

    public void setOp_font_size(int op_font_size) {
        this.op_font_size = op_font_size;
    }

    public int getOp_msg_speed() {
        return op_msg_speed;
    }

    public void setOp_msg_speed(int op_msg_speed) {
        this.op_msg_speed = op_msg_speed;
    }

    public int getOp_modulation_type() {
        return op_modulation_type;
    }

    public void setOp_modulation_type(int op_modulation_type) {
        this.op_modulation_type = op_modulation_type;
    }

    public int getOp_receive_type() {
        return op_receive_type;
    }

    public void setOp_receive_type(int op_receive_type) {
        this.op_receive_type = op_receive_type;
    }

    public int getOp_transparency() {
        return op_transparency;
    }

    public void setOp_transparency(int op_transparency) {
        this.op_transparency = op_transparency;
    }

    public int getOp_font_color() {
        return op_font_color;
    }

    public void setOp_font_color(int op_font_color) {
        this.op_font_color = op_font_color;
    }

    public int getOp_back_color() {
        return op_back_color;
    }

    public void setOp_back_color(int op_back_color) {
        this.op_back_color = op_back_color;
    }

    public String getOp_time_format() {
        return op_time_format;
    }

    public void setOp_time_format(String op_time_format) {
        this.op_time_format = op_time_format;
    }

    public String getOp_align() {
        return op_align;
    }

    public void setOp_align(String op_align) {
        this.op_align = op_align;
    }

    public String getOp_channel_num() {
        return op_channel_num;
    }

    public void setOp_channel_num(String op_channel_num) {
        this.op_channel_num = op_channel_num;
    }
}
