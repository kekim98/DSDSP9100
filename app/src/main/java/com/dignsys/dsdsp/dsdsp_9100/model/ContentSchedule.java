
package com.dignsys.dsdsp.dsdsp_9100.model;

import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;

import java.util.ArrayList;
import java.util.List;

public class ContentSchedule {
    public int pane_num;

    public String pane_type;
  //  public ContentEntity contentEntity;
    public int idx=0;

    public int count=0;
    public int bgm_count=0;
    public boolean is_bgm_runnig;

    public long opRunTimeTick;

    public long opMSGPlayTick;

    public int isMain=0;

    public List<ContentEntity> contents = new ArrayList<>();
    public List<ContentEntity> bgms = new ArrayList<>();

}
