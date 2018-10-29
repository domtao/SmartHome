package com.bluecam.api;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.bluecam.api.adapter.SettingLanListAdapter;
import com.bluecam.api.bean.LanBean;
import com.bluecam.api.view.MyListView;
import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraContants;
import com.zunder.smart.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class Setting_OtherActivity extends BaseActivity implements AdapterView.OnItemClickListener{



    private MyListView lan_view;
    private SettingLanListAdapter lanListAdapter;
    private List<LanBean> lanList = new ArrayList<LanBean>();
    private String[] lan = null;
    private int language=0;  //0表示静音 1 表示中文 2 表示英文
    private Button done_btn;


    private String camID;

    private BCamera camera;
    private JSONObject paramObj;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_other);
        lan =this.getResources().getStringArray(R.array.lan_arr);
        initView();
        initData();
        String camID = getIntent().getStringExtra("camID");
        camera = cameraManager.getCamera(camID);
        camera.getCameraParam(CameraContants.ParamKey.GET_VOICE_LANGUAGE_PARAM_KEY);

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
        setTitle_txt("其它设置");

        lan_view = (MyListView)findViewById(R.id.lan_view);
        lanListAdapter = new SettingLanListAdapter(this,lanList);
        lan_view.setAdapter(lanListAdapter);
        lan_view.setOnItemClickListener(this);
        done_btn = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });

    }
    private void initData(){
        LanBean bean = null;
        for(int i=0;i<lan.length;i++){
            bean = new LanBean(lan[i]);
            lanList.add(bean);
        }
        lanListAdapter.notifyDataSetChanged();
    }

    private void done() {
        camera.setCameraParam(CameraContants.ParamKey.SET_VOICE_LANGUAGE_PARAM_KEY,paramObj.toString());
    }

    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, final String params) {
        if(camera == null){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.GET_VOICE_LANGUAGE_PARAM_KEY) {
                try {
                    paramObj = new JSONObject(params);
                    language = paramObj.getInt("language");
                    int len = lanList.size();
                    for(int i=0;i<len;i++){
                        if(language == i){
                            LanBean bean = lanList.get(i);
                            bean.setSelected(true);
                        }
                    }
                    Setting_OtherActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDialog();
                            lanListAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            if(paramKey == CameraContants.ParamKey.SET_VOICE_LANGUAGE_PARAM_KEY){
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LanBean preBean = lanList.get(language);
        if(preBean != null){
            preBean.setSelected(false);
        }

        LanBean bean = (LanBean) parent.getItemAtPosition(position);
        language = position;
        bean.setSelected(true);
        lanListAdapter.notifyDataSetChanged();
        try {
            paramObj.put("language",language);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
