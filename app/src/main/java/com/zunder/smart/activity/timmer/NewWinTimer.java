package com.zunder.smart.activity.timmer;

import android.os.Handler;
import android.os.Message;

import com.zunder.smart.R;
import com.zunder.smart.model.Device;

public class NewWinTimer {
    private volatile static NewWinTimer install;

    private Device device;
    int powerState;
    int speedIndex;
    int temp;
    int side;

    private OnNewWinIndexSureListener onNewWinIndexSureListener;
    public static NewWinTimer getInstance() {
        if (null == install) {
            install = new NewWinTimer();
        }
        return install;
    }
    public void setBackeCode(Device _device,OnNewWinIndexSureListener onNewWinIndexSureListener){
        if(_device!=null) {
            this.onNewWinIndexSureListener = onNewWinIndexSureListener;
            this.device = _device;
            powerState=device.getState();
            speedIndex=device.getDeviceAnalogVar2() / 16;
                temp=device.getDeviceAnalogVar3()&0x003F;//溫度
                 side=device.getDeviceAnalogVar3()>>6;//閥門

             handler.sendEmptyMessage(0);

        }

    }


    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    if(onNewWinIndexSureListener!=null){
                        onNewWinIndexSureListener.onState(powerState,temp,side,speedIndex);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };


    public interface OnNewWinIndexSureListener {
        public void onState(int powerState,int temp,int side,int speedIndex);
    }

}
