package com.bluecam.api;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraContants;
import com.zunder.smart.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/3.
 */

public class Setting_CamInfoActivity extends BaseActivity {
    private TextView cam_sys_version;
    private TextView cam_name_txt;
    private TextView cam_id_txt;
    private TextView cam_mac_txt;
    private TextView cam_wifi_mac_txt;
    private TextView cam_wifi_status_txt;
    private BCamera camera;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_caminfo);
        initView();
        String camID = getIntent().getStringExtra("camID");
        camera = cameraManager.getCamera(camID);
        camera.getCameraParam(CameraContants.ParamKey.GET_STATUS_PARAM_KEY);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraManager != null){
            cameraManager.unRegisterEventListener(this);
        }
    }

    private void initView() {
        initHeaderBar();
        setTitle_txt("设备信息");

        cam_sys_version = (TextView)findViewById(R.id.cam_sys_version);
        cam_name_txt    = (TextView)findViewById(R.id.cam_name_txt);
        cam_id_txt      = (TextView)findViewById(R.id.cam_id_txt);
        cam_mac_txt     = (TextView)findViewById(R.id.cam_mac_txt);
        cam_wifi_mac_txt = (TextView)findViewById(R.id.cam_wifi_mac_txt);
        cam_wifi_status_txt = (TextView)findViewById(R.id.cam_wifi_status_txt);
    }

    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, final String params) {
        if(camera == null){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.GET_STATUS_PARAM_KEY) {

                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject paramObj = new JSONObject(params);
                            cam_sys_version.setText(paramObj.getString("sys_ver"));
                            cam_name_txt.setText(paramObj.getString("alias"));
                            cam_id_txt.setText(paramObj.getString("deviceid"));
                            cam_mac_txt.setText(paramObj.getString("mac"));
                            cam_wifi_mac_txt.setText(paramObj.getString("wifimac"));
                            if (paramObj.getInt("wifi_status") == 1) {
                                cam_wifi_status_txt.setText("WIFI已连接");
                            } else {
                                cam_wifi_status_txt.setText("未连接");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
