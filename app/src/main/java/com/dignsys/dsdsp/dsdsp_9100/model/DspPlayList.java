/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.model;

import java.util.Date;

public interface DspPlayList {
    int getId();
    int getProductId();
    String getText();
    Date getPostedAt();
}
