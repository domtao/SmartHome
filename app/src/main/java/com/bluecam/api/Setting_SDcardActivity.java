package com.bluecam.api;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraContants;
import com.zunder.smart.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/3.
 */

public class Setting_SDcardActivity extends BaseActivity implements View.OnClickListener{

    private TextView capacity_tv;
    private TextView sdcard_status_tv;
    private CheckBox record_fg_cb;
    private CheckBox record_voice_cb;
    private CheckBox record_time_cb;
    private View record_tiems_set_view;
    private Button format_btn;
    private Button done_btn;


    private String camID;
    private boolean isFormat = false;
    private BCamera camera;
    private JSONObject paramObj;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_sdcard);
        initView();
        String camID = getIntent().getStringExtra("camID");
        camera = cameraManager.getCamera(camID);
        camera.getCameraParam(CameraContants.ParamKey.GET_RECORDSCH_PARAM_KEY);

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
        setTitle_txt("SD卡设置");


        done_btn = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });

        capacity_tv = (TextView)findViewById(R.id.capacity_tv);
        sdcard_status_tv = (TextView)findViewById(R.id.sdcard_status_tv);
        record_fg_cb = (CheckBox)findViewById(R.id.record_fg_cb);
        record_voice_cb = (CheckBox)findViewById(R.id.record_voice_cb);
        record_time_cb = (CheckBox)findViewById(R.id.record_time_cb);

        format_btn = (Button)findViewById(R.id.format_btn);
        record_tiems_set_view = findViewById(R.id.record_tiems_set_view);
        record_fg_cb.setOnClickListener(this);
        record_voice_cb.setOnClickListener(this);
        record_time_cb.setOnClickListener(this);
        format_btn.setOnClickListener(this);
        record_tiems_set_view.setOnClickListener(this);
    }

    private void done() {
        camera.setCameraParam(CameraContants.ParamKey.SET_RECORDSCH_PARAM_KEY,paramObj.toString());
    }

    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, final String params) {
        if(camera == null){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.GET_RECORDSCH_PARAM_KEY) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            paramObj = new JSONObject(params);
                            Setting_SDcardActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressDialog();
                                    try
                                    {
                                        int sdcard_totalsize = paramObj.getInt("sdcard_totalsize");
                                        int sdcard_status = paramObj.getInt("sdcard_status");
                                        int record_cover = paramObj.getInt("record_cover");
                                        int record_audio = paramObj.getInt("record_audio");
                                        int time_schedule_enable = paramObj.getInt("time_schedule_enable");

                                        capacity_tv.setText(sdcard_totalsize+"M");
                                        showSdCardStatus(sdcard_status);
                                        if(record_cover == 1) {
                                            record_fg_cb.setChecked(true);
                                        }
                                        else{
                                            record_fg_cb.setChecked(false);
                                        }
                                        if(record_audio == 1){
                                            record_voice_cb.setChecked(true);
                                        }
                                        else{
                                            record_voice_cb.setChecked(false);
                                        }
                                        if(time_schedule_enable == 1){
                                            record_time_cb.setChecked(true);
                                        }
                                        else{
                                            record_time_cb.setChecked(false);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private void showSdCardStatus(int status)
    {

        if (status == 1) { //已插入
            //0 没插入 1 已插入 2录像中 3sd文件系统错误，4 sd卡格式化中
            sdcard_status_tv.setText("SD卡已经插入");
        }
        else if(status == 2)
        {
            sdcard_status_tv.setText("正在录像...");
        }
        else if(status == 3)
        {
            sdcard_status_tv.setText("文件系统错误");
            //showErrDialog();
        }
        else if(status == 4)
        {
            sdcard_status_tv.setText("SD卡正在格式化中...");
        }
        else {
            sdcard_status_tv.setText("没有插SD卡");
        }
    }
    @Override
    public void onSetParamsEvent(long camHandler, long paramKey, final int nResult) {
        if(camera == null){
            return;
        }
        System.out.println("nResult == "+nResult);
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.SET_SDFORMAT_PARAM_KEY){
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(nResult == 1){
                            showToast("设置成功");
                        }
                        else{
                            showToast("设置失败");
                        }
                    }
                });

            }
            else if(paramKey == CameraContants.ParamKey.SET_RECORDSCH_PARAM_KEY){
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(nResult == 1){
                            showToast("设置成功");
                        }
                        else{
                            showToast("设置失败");
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(paramObj == null)
        {
            return;
        }
        //格式化
        if(id == R.id.format_btn)
        {
            if(paramObj != null) {
                camera.setCameraParam(CameraContants.ParamKey.SET_SDFORMAT_PARAM_KEY,"");
            }
        }
        //录像覆盖
        else if(id == R.id.record_fg_cb){
            try {
                if(record_fg_cb.isChecked()) {
                    paramObj.put("record_cover", 1);
                }
                else {
                    paramObj.put("record_cover", 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //声音录像
        else if(id == R.id.record_voice_cb){
            try {
                if(record_voice_cb.isChecked()) {
                    paramObj.put("record_audio", 1);
                }
                else {
                    paramObj.put("record_audio", 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //定时录像
        else if(id == R.id.record_time_cb){
            try {
                if(record_time_cb.isChecked()) {
                    paramObj.put("time_schedule_enable", 1);
                }
                else {
                    paramObj.put("time_schedule_enable", 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //录像定时设置
        else if(id == R.id.record_tiems_set_view){
            Intent intent = new Intent(this,Setting_RecordTimeActivity.class);
            intent.putExtra("camID",camera.getCameraBean().getDevID());
            startActivity(intent);
            //showRecordTimeSetWindow();
            //openActicity(camID,CommonActivity.class,"com.camera.fragment.Setting_RecordTimeSetFragment");
        }
    }

}
