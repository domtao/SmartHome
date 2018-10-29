package com.zunder.smart.activity.timmer;

import android.os.Handler;
import android.os.Message;

import com.zunder.smart.model.Device;

public class SensorTimer {
    private volatile static SensorTimer install;

    private Device device;

    int powerState;
    int sensorNumber;
    private OnSensorIndexSureListener onSensorIndexSureListener;
    public static SensorTimer getInstance() {
        if (null == install) {
            install = new SensorTimer();
        }
        return install;
    }
    public void setBackeCode(Device _device,OnSensorIndexSureListener onSensorIndexSureListener) {
        if(_device!=null) {
            this.onSensorIndexSureListener = onSensorIndexSureListener;
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
                    if(onSensorIndexSureListener!=null){
                        onSensorIndexSureListener.onState(powerState,sensorNumber);
                        }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };



    public interface OnSensorIndexSureListener {
        public void onState(int powerState, int sensorNumber);
    }

}
