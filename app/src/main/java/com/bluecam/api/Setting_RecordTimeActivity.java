package com.bluecam.api;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bluecam.api.adapter.TimeSettingListAdapter;
import com.bluecam.api.bean.TimeItemBean;
import com.bluecam.api.bean.TimeRowBean;
import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraContants;
import com.zunder.smart.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class Setting_RecordTimeActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView alarm_time_list_view;
    private TimeSettingListAdapter timeAdapter;
    private List<TimeRowBean> timeList = new ArrayList<TimeRowBean>();
    private Button done_btn;
    private BCamera camera;
    private JSONArray alarmTimeArr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm_time);
        initView();
        String camID = getIntent().getStringExtra("camID");
        camera = cameraManager.getCamera(camID);
        camera.getCameraParam(CameraContants.ParamKey.GET_RECORD_TIME_PARAM_KEY);

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
        setTitle_txt("录像定时设置");
        alarm_time_list_view    = (RecyclerView)findViewById(R.id.alarm_time_list_view);
        timeAdapter  = new TimeSettingListAdapter(this,timeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        alarm_time_list_view.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        alarm_time_list_view.setAdapter(timeAdapter);
        done_btn = (Button)findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    private void done() {
        getNewTimeArr();
        System.out.println("alarmTimeArr == "+alarmTimeArr.toString());
        camera.setCameraParam(CameraContants.ParamKey.SET_RECORD_TIME_PARAM_KEY,alarmTimeArr.toString());
        finish();
    }
    private void getNewTimeArr(){
        alarmTimeArr = new JSONArray();
        int len = timeList.size();
        for(int i=0;i<len;i++){
            JSONArray itemArr = new JSONArray();
            int itemLen = timeList.get(i).getItemList().size();
            for(int j=0;j<itemLen;j++){
                TimeItemBean itemBean = timeList.get(i).getItemList().get(j);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("starthour",itemBean.getStarthour());
                    obj.put("endhour",itemBean.getEndhour());
                    obj.put("startmin",itemBean.getStartmin());
                    obj.put("endmin",itemBean.getEndmin());
                    itemArr.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            alarmTimeArr.put(itemArr);
        }
    }

    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, final String params) {
        if(camera == null){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.GET_RECORD_TIME_PARAM_KEY) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeList.clear();
                        try {
                            alarmTimeArr = new JSONArray(params);
                            for(int i = 0; i< alarmTimeArr.length(); i++){
                                JSONArray itemArr = alarmTimeArr.getJSONArray(i);
                                TimeRowBean rowBean = new TimeRowBean(i);
                                for(int j=0; j<itemArr.length(); j++){
                                    JSONObject obj = itemArr.getJSONObject(j);
                                    TimeItemBean itemBean = new TimeItemBean(j,obj.getInt("starthour"),obj.getInt("endhour"),obj.getInt("startmin"),obj.getInt("endmin"));
                                    rowBean.addItem(itemBean);
                                }
                                timeList.add(rowBean);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        timeAdapter.notifyDataSetChanged();
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
            if(paramKey == CameraContants.ParamKey.SET_RECORD_TIME_PARAM_KEY){
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

    }

}
