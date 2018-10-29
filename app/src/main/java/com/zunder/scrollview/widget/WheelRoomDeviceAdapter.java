package com.zunder.scrollview.widget;


import com.zunder.smart.model.Device;

import java.util.List;

public class WheelRoomDeviceAdapter extends WheelView.WheelAdapter {

    private List<Device> list;
    public WheelRoomDeviceAdapter(List<Device> list){
        this.list=list;
    }
    @Override
    protected int getItemCount() {
        return list.size();
    }

    @Override
    protected String getItem(int index) {
        return  list.get(index).getRoomName()+list.get(index).getDeviceName();
    }
}
