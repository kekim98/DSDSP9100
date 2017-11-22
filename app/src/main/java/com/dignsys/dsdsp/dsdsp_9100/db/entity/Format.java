package com.dignsys.dsdsp.dsdsp_9100.db.entity;

/**
 * Created by bawoori on 17. 11. 20.
 */

public class Format {
    private int scene_id; //for mapping with SceneEntity

    private int sceneType; // scene format type
    private int sceneWidth; //scene width resolution
    private int sceneHeight; //scene height resolution


    public int getScene_id() {
        return scene_id;
    }

    public void setScene_id(int scene_id) {
        this.scene_id = scene_id;
    }

    public int getSceneType() {
        return sceneType;
    }

    public void setSceneType(int sceneType) {
        this.sceneType = sceneType;
    }

    public int getSceneWidth() {
        return sceneWidth;
    }

    public void setSceneWidth(int sceneWidth) {
        this.sceneWidth = sceneWidth;
    }

    public int getSceneHeight() {
        return sceneHeight;
    }

    public void setSceneHeight(int sceneHeight) {
        this.sceneHeight = sceneHeight;
    }
}
