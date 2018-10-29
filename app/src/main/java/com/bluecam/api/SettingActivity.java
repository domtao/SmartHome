package com.bluecam.api;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bluecam.api.adapter.SettingListAdapter;
import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraContants;
import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.model.GateWay;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/3.
 */

public class SettingActivity extends BaseActivity implements SettingListAdapter.SettingClickListener{
    private String[] itemSetting = {"设备信息","用户设置","WIFI设置","报警设置","SD卡设置","时间设置","邮件设置","其它设置"};
    private RecyclerView setting_list_view;
    private SettingListAdapter settingListAdapter;
    private String camID;
    private BCamera camera;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        CameraPlayActivity.start=1;
        camID = getIntent().getStringExtra("camID");
        camera = cameraManager.getCamera(camID);
        camera.getCameraParam(CameraContants.ParamKey.GET_USERPWD_PARAM_KEY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraPlayActivity.start=0;
        if(cameraManager != null){
            cameraManager.unRegisterEventListener(this);
        }
    }

    private void initView(){
        initHeaderBar();
        setTitle_txt("设置");
        setting_list_view    = (RecyclerView)findViewById(R.id.setting_list_view);
        settingListAdapter  = new SettingListAdapter(this,itemSetting,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        setting_list_view.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        setting_list_view.setAdapter(settingListAdapter);
    }

    @Override
    public void onClick(int position) {
        if(position == 0){
            Intent intent = new Intent(this,Setting_CamInfoActivity.class);
            intent.putExtra("camID",camID);
            startActivity(intent);
        }
        else if(position == 1){
            Intent intent = new Intent(this,Setting_UserActivity.class);
            intent.putExtra("camID",camID);
            startActivity(intent);
        }
        else if(position == 2){
            Intent intent = new Intent(this,Setting_WiFiActivity.class);
            intent.putExtra("camID",camID);
            startActivity(intent);
        }
        else if(position == 3){
            Intent intent = new Intent(this,Setting_AlarmActivity.class);
            intent.putExtra("camID",camID);
            startActivity(intent);
        }
        else if(position == 4){
            Intent intent = new Intent(this,Setting_SDcardActivity.class);
            intent.putExtra("camID",camID);
            startActivity(intent);
        }
        else if(position == 5){
            Intent intent = new Intent(this,Setting_TimeActivity.class);
            intent.putExtra("camID",camID);
            startActivity(intent);
        }
        else if(position == 6){
            Intent intent = new Intent(this,Setting_MailActivity.class);
            intent.putExtra("camID",camID);
            startActivity(intent);
        }
        else if(position == 7){
            Intent intent = new Intent(this,Setting_OtherActivity.class);
            intent.putExtra("camID",camID);
            startActivity(intent);
        }
    }
    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, final String params) {
        if(camera == null){
            ToastUtils.ShowError(MyApplication.getInstance(),"您不是管理员", Toast.LENGTH_SHORT,true);
            finish();
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.GET_USERPWD_PARAM_KEY) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            JSONObject   paramObj = new JSONObject(params);
                            GateWay gateWay= GateWayFactory.getInstance().getGateWay(camID);
                            if(gateWay!=null) {
                                if (!gateWay.getUserName().equals(paramObj.getString("user3"))) {
                                    ToastUtils.ShowError(MyApplication.getInstance(),"您不是管理员,没有权限设置", Toast.LENGTH_SHORT,true);
                                    finish();
                                }
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
