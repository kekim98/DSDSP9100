/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


import com.dignsys.dsdsp.dsdsp_9100.model.DspPlayList;

import java.util.Date;

@Entity(tableName = "dsp_play_list", foreignKeys = {
        @ForeignKey(entity = DspFormatEntity.class,
                parentColumns = "id",
                childColumns = "productId",
                onDelete = ForeignKey.CASCADE)}, indices = {
        @Index(value = "productId")
})
public class DspPlayListEntity implements DspPlayList {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int productId;
    private String text;
    private Date postedAt;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
    }

    public DspPlayListEntity() {
    }

    public DspPlayListEntity(DspPlayList comment) {
        id = comment.getId();
        productId = comment.getProductId();
        text = comment.getText();
        postedAt = comment.getPostedAt();
    }
}
