package com.bluecam.api;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class Setting_MailActivity extends BaseActivity implements View.OnClickListener{
    private EditText sender_et;
    private EditText smtp_et;
    private EditText smtp_port_et;
    private TextView ssl_et;

    private EditText username_et;
    private EditText passwd_et;
    private EditText receiver1_et;
    private EditText receiver2_et;
    private EditText receiver3_et;
    private EditText receiver4_et;

    private View select_smtp_view;
    private View ssl_view;
    private View user_view;
    private CheckBox safe_cb;
    private Button done_btn;

    private String[] sslArr = {"NONE","SSL","TLS"};
    private String[] smtpServer = {"smtp.gmail.com","smtp.mail.yahoo.com","smtp.yeah.net","smtp.tom.com","smtp.21cn.com","mx.eyou.com","smtp.qq.com","smtp.163.com","smtp.126.com","smtp.sina.com","smtp.sohu.com"
            ,"smtp.263.net"};
    private int[] port = {465,25,25,25,25,25,25,25,25,25,25,25};


    private String camID;

    private BCamera camera;
    private JSONObject paramObj;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_mail);
        initView();
        String camID = getIntent().getStringExtra("camID");
        camera = cameraManager.getCamera(camID);
        camera.getCameraParam(CameraContants.ParamKey.GET_MAIL_PARAM_KEY);

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
        setTitle_txt("邮件设置");

        sender_et    = (EditText)findViewById(R.id.sender_et);
        smtp_et      = (EditText)findViewById(R.id.smtp_et);
        smtp_port_et = (EditText)findViewById(R.id.smtp_port_et);
        ssl_et       = (TextView)findViewById(R.id.ssl_et);
        passwd_et    = (EditText)findViewById(R.id.passwd_et);
        username_et  = (EditText)findViewById(R.id.username_et);

        receiver1_et = (EditText)findViewById(R.id.receiver1_et);
        receiver2_et = (EditText)findViewById(R.id.receiver2_et);
        receiver3_et = (EditText)findViewById(R.id.receiver3_et);
        receiver4_et = (EditText)findViewById(R.id.receiver4_et);

        select_smtp_view = findViewById(R.id.select_smtp_view);
        ssl_view         = findViewById(R.id.ssl_view);
        user_view        = findViewById(R.id.user_view);

        safe_cb  = (CheckBox)findViewById(R.id.safe_cb);
        done_btn = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    done();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        select_smtp_view.setOnClickListener(this);
        ssl_view.setOnClickListener(this);
        safe_cb.setOnClickListener(this);

    }

    private void done() throws JSONException {
        if(paramObj == null){
            return;
        }
        System.out.println("paramObj == "+paramObj.toString());
        String sender  = sender_et.getText().toString().trim();
        String servier = smtp_et.getText().toString().trim();
        String port    = smtp_port_et.getText().toString().trim();
        String userName = username_et.getText().toString().trim();
        String passwd   = passwd_et.getText().toString().trim();
        String receiver1 = receiver1_et.getText().toString().trim();
        String receiver2 = receiver2_et.getText().toString().trim();
        String receiver3 = receiver3_et.getText().toString().trim();
        String receiver4 = receiver4_et.getText().toString().trim();
        int nPort = 0;
        if(TextUtils.isEmpty(port)){
            nPort = 0;
        }else{
            nPort = Integer.parseInt(port);
        }

        if(safe_cb.isChecked()){
            paramObj.put("user",userName);
            paramObj.put("pwd", passwd);
        }
        else{
            paramObj.put("user","");
            paramObj.put("pwd", "");
        }
        paramObj.put("server", servier);
        paramObj.put("sender", sender);
        paramObj.put("receiver1", receiver1);
        paramObj.put("receiver2", receiver2);
        paramObj.put("receiver3", receiver3);
        paramObj.put("receiver4", receiver4);
        paramObj.put("port",nPort);
        camera.setCameraParam(CameraContants.ParamKey.SET_MAIL_PARAM_KEY,paramObj.toString());
    }

    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, final String params) {
        if(camera == null){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.GET_MAIL_PARAM_KEY) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            paramObj       = new JSONObject(params);
                            String sender     = paramObj.getString("sender");
                            String smtpServer = paramObj.getString("server");
                            int port          = paramObj.getInt("port");
                            String userName   = paramObj.getString("user");
                            String passwd     = paramObj.getString("pwd");
                            int ssl           = paramObj.getInt("ssl");
                            String receiver1  = paramObj.getString("receiver1");
                            String receiver2  = paramObj.getString("receiver2");
                            String receiver3  = paramObj.getString("receiver3");
                            String receiver4  = paramObj.getString("receiver4");

                            sender_et.setText(sender);
                            smtp_et.setText(smtpServer);
                            smtp_port_et.setText(port+"");
                            ssl_et.setText(sslArr[ssl]);


                            receiver1_et.setText(receiver1);
                            receiver2_et.setText(receiver2);
                            receiver3_et.setText(receiver3);
                            receiver4_et.setText(receiver4);
                            if (!TextUtils.isEmpty(userName)){
                                safe_cb.setChecked(true);
                                user_view.setVisibility(View.VISIBLE);
                                username_et.setText(userName);
                                passwd_et.setText(passwd);
                            }
                            else {
                                safe_cb.setChecked(false);
                                user_view.setVisibility(View.GONE);
                                username_et.setText("");
                                passwd_et.setText("");
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
        System.out.println("nResult == "+nResult);
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.SET_MAIL_PARAM_KEY){
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
        if(id == R.id.select_smtp_view){
            showSmtpServerDialog();
        }
        else if(id == R.id.ssl_view){
            showSSlDialog();
        }
        else if(id == R.id.safe_cb){
            if(safe_cb.isChecked()){
                user_view.setVisibility(View.VISIBLE);
            }
            else
            {
                user_view.setVisibility(View.GONE);
                username_et.setText("");
                passwd_et.setText("");
            }
        }
    }

    private void showSSlDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.setting_alarm_param_dialog, null);
        TextView cancel_item = (TextView)view.findViewById(R.id.cancel_item);
        ListView param_list_view = (ListView)view.findViewById(R.id.param_list_view);
        SettingCommonAdapter adapter = new SettingCommonAdapter(this,sslArr);
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
                String ssl = (String) parent.getItemAtPosition(position);
                ssl_et.setText(ssl);
                if(paramObj != null)
                {
                    try {
                        paramObj.put("ssl",position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                editWindow.dismiss();
            }
        });
    }

    private void showSmtpServerDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.setting_alarm_param_dialog, null);
        TextView cancel_item = (TextView)view.findViewById(R.id.cancel_item);
        ListView param_list_view = (ListView)view.findViewById(R.id.param_list_view);
        SettingCommonAdapter adapter = new SettingCommonAdapter(this,smtpServer);
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
                String smtp = (String) parent.getItemAtPosition(position);
                if(paramObj != null)
                {
                    try {
                        paramObj.put("server",smtp);
                        paramObj.put("port",port[position]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                smtp_et.setText(smtp);
                smtp_port_et.setText(port[position]+"");
                editWindow.dismiss();
            }
        });
    }


}
