/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.dignsys.dsdsp.dsdsp_9100.model.DspFormat;


@Entity(tableName = "dsp_format")
public class DspFormatEntity implements DspFormat {
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private int price;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public DspFormatEntity() {
    }

    public DspFormatEntity(DspFormat dspFormat) {
        this.id = dspFormat.getId();
        this.name = dspFormat.getName();
        this.description = dspFormat.getDescription();
        this.price = dspFormat.getPrice();
    }
}
