package com.zunder.smart.activity.timmer;

import android.os.Handler;
import android.os.Message;

import com.zunder.smart.model.Device;

public class PeopleTimer {
    private volatile static PeopleTimer install;

    private Device device;

    int powerState;
    int peopleNumber;
    private OnPeopleIndexSureListener onPeopleIndexSureListener;
    public static PeopleTimer getInstance() {
        if (null == install) {
            install = new PeopleTimer();
        }
        return install;
    }
    public void setBackeCode(Device _device,OnPeopleIndexSureListener onPeopleIndexSureListener) {
        if(_device!=null) {
            this.onPeopleIndexSureListener = onPeopleIndexSureListener;
            this.device = _device;
            handler.sendEmptyMessage(0);
        }
    }


    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    if(onPeopleIndexSureListener!=null){
                        onPeopleIndexSureListener.onState(powerState,peopleNumber);
                        }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };



    public interface OnPeopleIndexSureListener {
        public void onState(int powerState, int peopleNumber);
    }

}
