package com.zunder.smart.activity.timmer;

import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import com.zunder.smart.R;
import com.zunder.smart.model.Device;

public class DimmerTimer {
    private volatile static DimmerTimer install;

    private Device device;
    int powerState;
    int dimmerStyle;
    int color;
    static Boolean[] booleans;
    private OnDimmerIndexSureListener onDimmerIndexSureListener;
    public static DimmerTimer getInstance() {
        if (null == install) {
            install = new DimmerTimer();
        }
        booleans = new Boolean[] { false, false, false, false, false, false,
                false, false, false, false };
        return install;
    }
    public void setBackeCode(Device _device,OnDimmerIndexSureListener onDimmerIndexSureListener){
        if(_device!=null) {
            this.onDimmerIndexSureListener = onDimmerIndexSureListener;
            this.device = _device;
            powerState=device.getState();
            color = device.getDeviceAnalogVar2();
            int index = device.getDeviceAnalogVar3() - 1;
            for (int i = 0; i < booleans.length; i++) {
                if (index == i) {
                    booleans[i] = true;
                } else {
                    booleans[i] = false;
                }
            }
            if(color==3){
                dimmerStyle=0;
            }else if(color==2){
                dimmerStyle=1;
            }else{
                dimmerStyle=2;
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
                    if(onDimmerIndexSureListener!=null){
                        onDimmerIndexSureListener.onState(powerState,booleans,color,dimmerStyle);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };


    public interface OnDimmerIndexSureListener {
        public void onState(int powerState,Boolean[] booleans,int color,int dimmerStyle);
    }

}
