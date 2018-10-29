package com.bluecam.api;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraContants;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.model.GateWay;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/3.
 */

public class Setting_UserActivity extends BaseActivity {
    private EditText admin_username_et;
    private EditText admin_passswd_et;
    private EditText optusername_et;
    private EditText opt_passswd_et;
    private Button done_btn;
    private BCamera camera;
    private JSONObject paramObj;
    String camID="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        initView();
         camID = getIntent().getStringExtra("camID");
        camera = cameraManager.getCamera(camID);
        camera.getCameraParam(CameraContants.ParamKey.GET_USERPWD_PARAM_KEY);
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
        setTitle_txt("用户设置");
        admin_username_et = (EditText)findViewById(R.id.admin_username_et);
        admin_passswd_et  = (EditText)findViewById(R.id.admin_passswd_et);
        optusername_et    = (EditText)findViewById(R.id.optusername_et);
        opt_passswd_et    = (EditText)findViewById(R.id.opt_passswd_et);
        done_btn          = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }
    private void done() {
        String adminName = admin_username_et.getText().toString();
        String adminPwd  = admin_passswd_et.getText().toString();
        String optName   = optusername_et.getText().toString();
        String optPwd    = opt_passswd_et.getText().toString();
        if(TextUtils.isEmpty(adminName))
        {
            showToast("请输入管理员用户名");
            return;
        }
        else  if(TextUtils.isEmpty(adminPwd)){
            showToast("请输入管理员密码");
            return;
        }
        if(adminName.equals(optName))
        {
            showToast("管理员和访问者不能同名");
            return;
        }
        if(optName.toLowerCase().equals("admin")){
            showToast("访问者不能用admin帐户");
            return;
        }
        try
        {
            paramObj.put("user3", adminName);
            paramObj.put("pwd3", adminPwd);
            paramObj.put("user2", optName);
            paramObj.put("pwd2", optPwd);
            camera.getCameraBean().setUsername(adminName);
            camera.getCameraBean().setPassword(adminPwd);
            camera.setCameraParam(CameraContants.ParamKey.SET_USERPWD_PARAM_KEY,paramObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, final String params) {
        if(camera == null){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.GET_USERPWD_PARAM_KEY) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            paramObj = new JSONObject(params);
                            admin_username_et.setText(paramObj.getString("user3"));
                            admin_passswd_et.setText(paramObj.getString("pwd3"));
                            optusername_et.setText(paramObj.getString("user2"));
                            opt_passswd_et.setText(paramObj.getString("pwd2"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onSetParamsEvent(long camHandler, long paramKey, final int nResult) {
        if(camera == null){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.SET_USERPWD_PARAM_KEY){
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(nResult == 1){
                            showToast("设置成功");
                            GateWay gateWay= GateWayFactory.getInstance().getGateWay(camID);
                            if(gateWay!=null){
                                gateWay.setUserName(admin_username_et.getText().toString());
                                gateWay.setUserPassWord(admin_passswd_et.getText().toString());
                                MyApplication.getInstance().getWidgetDataBase().updateGateWay(gateWay,gateWay.getGatewayName());
                                MainActivity.getInstance().freshFindDevice();
                            }
                        }
                        else{
                            showToast("设置失败");
                        }
                    }
                });

            }
        }
    }
}
