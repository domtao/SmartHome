package com.bluecam.api.bean;

/**
 * Created by Administrator on 2017/7/21.
 */

public class TimeItemBean
{
    private int index;
    private int starthour;
    private int endhour;
    private int startmin;
    private int endmin;

    public TimeItemBean(int index, int starthour, int endhour, int startmin, int endmin) {
        this.index = index;
        this.starthour = starthour;
        this.endhour = endhour;
        this.startmin = startmin;
        this.endmin = endmin;
    }

    public TimeItemBean() {

    }

    public int getStarthour() {
        return starthour;
    }

    public void setStarthour(int starthour) {
        this.starthour = starthour;
    }

    public int getEndhour() {
        return endhour;
    }

    public void setEndhour(int endhour) {
        this.endhour = endhour;
    }

    public int getStartmin() {
        return startmin;
    }

    public void setStartmin(int startmin) {
        this.startmin = startmin;
    }

    public int getEndmin() {
        return endmin;
    }

    public void setEndmin(int endmin) {
        this.endmin = endmin;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSelected(){
        if(starthour ==0 && startmin ==0 && endhour==23 && endmin == 59){
            return  true;
        }
        return  false;
    }
    public boolean isChecked(){
        if(starthour ==0 && startmin ==0 && endhour==0 && endmin == 0){
            return  false;
        }
        return  true;
    }
}
