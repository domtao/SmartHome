package com.zunder.smart.activity.timmer;

import android.os.Handler;
import android.os.Message;

import com.zunder.smart.R;
import com.zunder.smart.model.Device;

public class EmptyTimer {
    private volatile static EmptyTimer install;

    private Device device;
    int powerState;
    int[] states=new int[8];

    private OnEmptyIndexSureListener onEmptyIndexSureListener;
    public static EmptyTimer getInstance() {
        if (null == install) {
            install = new EmptyTimer();
        }
        return install;
    }
    public void setBackeCode(Device _device,OnEmptyIndexSureListener onEmptyIndexSureListener){
        if(_device!=null) {
            this.onEmptyIndexSureListener = onEmptyIndexSureListener;
            this.device = _device;
            powerState=device.getState();
            int value = device.getDeviceAnalogVar2();
            int disable = device.getDeviceAnalogVar3();
                if((disable& (1 << 0)) > 0){
                    states[0]=2;
                }else{
                    if ((value & (1 << 0)) > 0) {
                        states[0]=1;
                    } else {
                        states[0]=0;
                    }
                }
                if((disable& (1 << 1)) > 0){
                    states[1]=2;
                }else {
                    if ((value & (1 << 1)) > 0) {
                        states[1]=1;
                    } else {
                        states[1]=0;
                    }
                }
                if((disable& (1 << 2)) > 0){
                    states[2]=2;
                }else {
                    if ((value & (1 << 2)) > 0) {
                        states[2]=1;
                    } else {
                        states[2]=0;
                    }
                }
                if((disable& (1 << 3)) > 0){
                    states[3]=2;
                }else {
                    if ((value & (1 << 3)) > 0) {
                        states[3]=1;
                    } else {
                        states[3]=0;
                    }
                }
                if((disable& (1 << 4)) > 0){
                    states[4]=2;
                }else {
                    if ((value & (1 << 4)) > 0) {
                        states[4]=1;
                    } else {
                        states[4]=0;
                    }
                }
                if((disable& (1 << 5)) > 0){
                    states[5]=2;
                }else {
                    if ((value & (1 << 5)) > 0) {
                        states[5]=1;
                    } else {
                        states[5]=0;
                    }
                }
                if((disable& (1 << 6)) > 0){
                    states[6]=2;
                }else {
                    if ((value & (1 << 6)) > 0) {
                        states[6]=1;
                    } else {
                        states[6]=0;
                    }
                }
                if((disable& (1 << 7)) > 0){
                    states[7]=2;
                }else {
                    if ((value & (1 << 7)) > 0) {
                        states[7]=1;
                    } else {
                        states[7]=0;
                    }
                }
            handler.sendEmptyMessage(0);

        }

    }


    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    if(onEmptyIndexSureListener!=null){
                        onEmptyIndexSureListener.onState(powerState,states);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };


    public interface OnEmptyIndexSureListener {
        public void onState(int powerState,int[]   states);
    }
}
