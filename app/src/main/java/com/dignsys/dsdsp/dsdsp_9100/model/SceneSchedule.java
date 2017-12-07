
package com.dignsys.dsdsp.dsdsp_9100.model;

import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.PlayerBGM;

public class SceneSchedule {

  //  public int scene_id;

    public int idx = 0;

    public int count =0;

    public long expire_time =0;

    public boolean isBGMMode = false;

    public PlayerBGM BGMPlayer;

    public SceneEntity scene ;

}
