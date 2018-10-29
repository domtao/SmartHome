package com.bluecam.api.bean;

import java.util.ArrayList;
import java.util.List;


public class TimeRowBean
{
    private int index;
    private List<TimeItemBean> itemList = new ArrayList<TimeItemBean>();
    private TimeItemBean timeItemBean;


    public TimeRowBean(int index) {
        this.index = index;
    }

    public TimeRowBean() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addItem(TimeItemBean itemBean){
        itemList.add(itemBean);
        if(itemList.size() == 4){
            this.timeItemBean = itemList.get(0);
        }
    }


    public List<TimeItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<TimeItemBean> itemList) {
        this.itemList = itemList;

    }

    public TimeItemBean getTimeItemBean() {
        return timeItemBean;
    }

    public void setTimeItemBean(TimeItemBean timeItemBean) {
        this.timeItemBean = timeItemBean;
    }
}
