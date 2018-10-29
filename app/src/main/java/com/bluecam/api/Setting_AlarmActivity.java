package com.bluecam.api;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bluecam.api.adapter.SettingCommonAdapter;
import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraContants;
import com.zunder.smart.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/3.
 */

public class Setting_AlarmActivity extends BaseActivity implements View.OnClickListener{
    private CheckBox move_cb;
    private FrameLayout move_strong_view;
    private TextView move_strong_tv;

    private CheckBox io_cb;
    private FrameLayout io_strong_view;
    private TextView io_strong_tv;

    private CheckBox voice_cb;
    private FrameLayout voice_strong_view;
    private TextView voice_strong_tv;

    private CheckBox pir_cb;
    private CheckBox record_cb;
    private CheckBox mail_cb;

    private FrameLayout alarm_time_set_view;

    private String[] moveParams = {"1","2","3","4","5","6","7","8","9","10"};
    private String[] ioParams = {"低电平","高电平"};
    private String[] audioParams = {"禁止检测","高灵敏度","中灵敏度","低灵敏度"};
    private Button done_btn;
    private BCamera camera;
    private JSONObject paramObj;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);
        initView();
        String camID = getIntent().getStringExtra("camID");
        camera = cameraManager.getCamera(camID);
        camera.getCameraParam(CameraContants.ParamKey.GET_ALARM_PARAM_KEY);

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
        setTitle_txt("报警设置");

        move_cb = (CheckBox)findViewById(R.id.move_cb);
        move_strong_view = (FrameLayout)findViewById(R.id.move_strong_view);
        move_strong_tv = (TextView)findViewById(R.id.move_strong_tv);

        io_cb = (CheckBox)findViewById(R.id.io_cb);
        io_strong_view = (FrameLayout)findViewById(R.id.io_strong_view);
        io_strong_tv = (TextView)findViewById(R.id.io_strong_tv);

        voice_cb = (CheckBox)findViewById(R.id.voice_cb);
        voice_strong_view = (FrameLayout)findViewById(R.id.voice_strong_view);
        voice_strong_tv = (TextView)findViewById(R.id.voice_strong_tv);

        pir_cb = (CheckBox)findViewById(R.id.pir_cb);
        record_cb = (CheckBox)findViewById(R.id.record_cb);
        mail_cb = (CheckBox)findViewById(R.id.mail_cb);
        alarm_time_set_view = (FrameLayout) findViewById(R.id.alarm_time_set_view);
        done_btn = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });

        move_cb.setOnClickListener(this);
        io_cb.setOnClickListener(this);
        voice_cb.setOnClickListener(this);
        pir_cb.setOnClickListener(this);
        record_cb.setOnClickListener(this);
        mail_cb.setOnClickListener(this);

        move_strong_view.setOnClickListener(this);
        io_strong_view.setOnClickListener(this);
        voice_strong_view.setOnClickListener(this);
        alarm_time_set_view.setOnClickListener(this);

    }

    private void done() {
        camera.setCameraParam(CameraContants.ParamKey.SET_ALARM_PARAM_KEY,paramObj.toString());
    }

    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, final String params) {
        if(camera == null){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.GET_ALARM_PARAM_KEY) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            paramObj = new JSONObject(params);
                            int motion_armed       = paramObj.getInt("motion_armed"); //移动侦测
                            int motion_sensitivity = paramObj.getInt("motion_sensitivity");
                            int input_armed        = paramObj.getInt("input_armed"); //输入布防
                            int ioin_level         = paramObj.getInt("ioin_level");
                            int record             = paramObj.getInt("record");
                            int alarm_audio        = paramObj.getInt("alarm_audio");
                            int pirenable          = paramObj.getInt("pirenable");
                            int mail               = paramObj.getInt("mail");
                            //移动侦测
                            if(motion_armed == 0)
                            {
                                //move_strong_view.setVisibility(View.GONE);
                                move_cb.setChecked(false);
                            }
                            else
                            {
                                //move_strong_view.setVisibility(View.VISIBLE);
                                move_cb.setChecked(true);
                            }
                            move_strong_tv.setText(motion_sensitivity+"");
                            //输入布防
                            if (input_armed==0)
                            {
                                //io_strong_view.setVisibility(View.GONE);
                                io_cb.setChecked(false);
                            }
                            else
                            {
                                //io_strong_view.setVisibility(View.VISIBLE);
                                io_cb.setChecked(true);
                            }
                            if (ioin_level == 0) {
                                io_strong_tv.setText("低电平");
                            } else {
                                io_strong_tv.setText("高电平");
                            }

                            //PIR报警
                            if(pirenable == 1)
                            {
                                pir_cb.setChecked(true);
                            }
                            else
                            {
                                pir_cb.setChecked(false);
                            }

                            //声音侦测
                            if(alarm_audio == 0)
                            {
                                voice_cb.setChecked(false);
                                voice_strong_tv.setText("禁止检测");
                                //voice_strong_view.setVisibility(View.GONE);
                            }
                            else
                            {
                                voice_cb.setChecked(true);
                                //voice_strong_view.setVisibility(View.VISIBLE);
                                switch(alarm_audio)
                                {
                                    case 1:
                                        voice_strong_tv.setText("高灵敏度");
                                        break;
                                    case 2:
                                        voice_strong_tv.setText("中灵敏度");
                                        break;
                                    case 3:
                                        voice_strong_tv.setText("低灵敏度");
                                        break;
                                    default:break;
                                }
                            }
                            //报警录像
                            if(record == 1)
                            {
                                record_cb.setChecked(true);
                            }
                            else
                            {
                                record_cb.setChecked(false);
                            }
                            //报警邮件开关
                            if(mail == 1){
                                mail_cb.setChecked(true);
                            }
                            else{
                                mail_cb.setChecked(false);
                            }
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
            if(paramKey == CameraContants.ParamKey.SET_ALARM_PARAM_KEY){
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
        //移动报警
        if(id == R.id.move_cb) {
            if(paramObj == null) {
                move_cb.setChecked(false);
                return;
            }
            try
            {
                if(move_cb.isChecked()) {
                    paramObj.put("motion_armed",1);
                }
                else {
                    paramObj.put("motion_armed",0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //输入报警
        else if(id == R.id.io_cb) {
            if(paramObj == null) {
                io_cb.setChecked(false);
                return;
            }
            try
            {
                if(io_cb.isChecked()) {
                    paramObj.put("input_armed",1);
                }
                else {
                    paramObj.put("input_armed",0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //声音报警
        else if(id == R.id.voice_cb) {
            if(paramObj == null) {
                voice_cb.setChecked(false);
                return;
            }
            try
            {
                if(voice_cb.isChecked()) {
                    paramObj.put("alarm_audio",1);
                    voice_strong_tv.setText("高灵敏度");
                }
                else {
                    paramObj.put("alarm_audio",0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //PIR报警
        else if(id == R.id.pir_cb) {
            if(paramObj == null) {
                pir_cb.setChecked(false);
                return;
            }
            try
            {
                if(pir_cb.isChecked()) {
                    paramObj.put("pirenable",1);
                }
                else {
                    paramObj.put("pirenable",0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //报警录像
        else if(id == R.id.record_cb) {
            if(paramObj == null) {
                record_cb.setChecked(false);
                return;
            }
            try
            {
                if(record_cb.isChecked()) {
                    paramObj.put("record",1);
                }
                else {
                    paramObj.put("record",0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //报警邮件开关
        else if(id == R.id.mail_cb){
            if(paramObj == null) {
                mail_cb.setChecked(false);
                return;
            }
            try
            {
                if(mail_cb.isChecked()) {
                    paramObj.put("mail",1);
                }
                else {
                    paramObj.put("mail",0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //移动灵敏度
        else if(id == R.id.move_strong_view){
            showMoveParamWindow();
        }
        //触发电平
        else if(id == R.id.io_strong_view){
            showIOParamWindow();
        }
        //声音灵敏度
        else if(id == R.id.voice_strong_view){
            showVoiceParamWindow();
        }
        //定时报警设置
        else if(id == R.id.alarm_time_set_view){
            Intent intent = new Intent(this,Setting_AlarmTimeActivity.class);
            intent.putExtra("camID",camera.getCameraBean().getDevID());
            startActivity(intent);
        }
    }
    //移动灵敏度
    private void showMoveParamWindow(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.setting_alarm_param_dialog, null);
        TextView cancel_item = (TextView)view.findViewById(R.id.cancel_item);
        ListView param_list_view = (ListView)view.findViewById(R.id.param_list_view);
        SettingCommonAdapter adapter = new SettingCommonAdapter(this,moveParams);
        param_list_view.setAdapter(adapter);

        final PopupWindow editWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        editWindow.setFocusable(true);
        editWindow.setOutsideTouchable(true);
        editWindow.setBackgroundDrawable(new ColorDrawable(0));
        editWindow.showAtLocation(done_btn, Gravity.BOTTOM,0, 0);
        cancel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWindow.dismiss();
            }
        });
        param_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int motion_sensitivity = position+1;
                if(paramObj != null)
                {
                    try {
                        paramObj.put("motion_sensitivity",motion_sensitivity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                move_strong_tv.setText((String)parent.getItemAtPosition(position));
                editWindow.dismiss();
            }
        });
    }

    //触发电平
    private void showIOParamWindow(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.setting_alarm_param_dialog, null);
        TextView cancel_item = (TextView)view.findViewById(R.id.cancel_item);
        ListView param_list_view = (ListView)view.findViewById(R.id.param_list_view);
        SettingCommonAdapter adapter = new SettingCommonAdapter(this,ioParams);
        param_list_view.setAdapter(adapter);

        final PopupWindow editWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        editWindow.setFocusable(true);
        editWindow.setOutsideTouchable(true);
        editWindow.setBackgroundDrawable(new ColorDrawable(0));
        editWindow.showAtLocation(done_btn, Gravity.BOTTOM,0, 0);
        cancel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWindow.dismiss();
            }
        });
        param_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int ioin_level = position;
                if(paramObj != null)
                {
                    try {
                        paramObj.put("ioin_level",ioin_level);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                io_strong_tv.setText((String)parent.getItemAtPosition(position));
                editWindow.dismiss();
            }
        });
    }
    //声音灵敏度
    private void showVoiceParamWindow(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.setting_alarm_param_dialog, null);
        TextView cancel_item = (TextView)view.findViewById(R.id.cancel_item);
        ListView param_list_view = (ListView)view.findViewById(R.id.param_list_view);
        SettingCommonAdapter adapter = new SettingCommonAdapter(this,audioParams);
        param_list_view.setAdapter(adapter);

        final PopupWindow editWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        editWindow.setFocusable(true);
        editWindow.setOutsideTouchable(true);
        editWindow.setBackgroundDrawable(new ColorDrawable(0));
        editWindow.showAtLocation(done_btn, Gravity.BOTTOM,0, 0);
        cancel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWindow.dismiss();
            }
        });
        param_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int alarm_audio = position;
                if(paramObj != null)
                {
                    try {
                        paramObj.put("alarm_audio",alarm_audio);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(alarm_audio>0){
                    voice_cb.setChecked(true);
                }
                else{
                    voice_cb.setChecked(false);
                }
                voice_strong_tv.setText((String)parent.getItemAtPosition(position));
                editWindow.dismiss();
            }
        });
    }
}
