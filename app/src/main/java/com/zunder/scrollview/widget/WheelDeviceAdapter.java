package com.zunder.scrollview.widget;


import com.zunder.smart.model.Device;

import java.util.List;

public class WheelDeviceAdapter extends WheelView.WheelAdapter {

    private List<Device> list;
    public WheelDeviceAdapter(List<Device> list){
        this.list=list;
    }
    @Override
    protected int getItemCount() {
        return list.size();
    }

    @Override
    protected String getItem(int index) {
        return  list.get(index).getDeviceName();
    }
}
