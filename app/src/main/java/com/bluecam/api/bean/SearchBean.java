package com.bluecam.api.bean;


import com.bluecam.bluecamlib.BCamera;

/**
 * Created by Administrator on 2017/7/1.
 */

public class SearchBean {
    private BCamera camera;
    private boolean isAdd;

    public BCamera getCamera() {
        return camera;
    }

    public void setCamera(BCamera camera) {
        this.camera = camera;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
