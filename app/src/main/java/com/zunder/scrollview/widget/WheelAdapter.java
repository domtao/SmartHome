package com.zunder.scrollview.widget;

import java.util.List;

public class WheelAdapter extends WheelView.WheelAdapter {

    private List<String> list;
    public WheelAdapter(List<String> list){
        this.list=list;
    }
    @Override
    protected int getItemCount() {
        return list.size();
    }

    @Override
    protected String getItem(int index) {
        return list.get(index);
    }
}
