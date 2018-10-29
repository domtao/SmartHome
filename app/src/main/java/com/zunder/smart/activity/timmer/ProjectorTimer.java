package com.zunder.smart.activity.timmer;

import android.os.Handler;
import android.os.Message;

import com.zunder.smart.R;
import com.zunder.smart.model.Device;

public class ProjectorTimer {
    private volatile static ProjectorTimer install;

    private Device device;
    int powerState;

    private OnProjectorIndexSureListener onProjectorIndexSureListener;
    public static ProjectorTimer getInstance() {
        if (null == install) {
            install = new ProjectorTimer();
        }
        return install;
    }
    public void setBackeCode(Device _device,OnProjectorIndexSureListener onProjectorIndexSureListener){
        if(_device!=null) {
            this.onProjectorIndexSureListener = onProjectorIndexSureListener;
            this.device = _device;
            powerState=device.getState();
             handler.sendEmptyMessage(0);

        }

    }


    public  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0) {
                    if(onProjectorIndexSureListener!=null){
                        onProjectorIndexSureListener.onState(powerState);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };


    public interface OnProjectorIndexSureListener {
        public void onState(int powerState);
    }

}
