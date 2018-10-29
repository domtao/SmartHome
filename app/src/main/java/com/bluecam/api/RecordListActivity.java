package com.bluecam.api;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.bluecam.api.adapter.RecordListAdapter;
import com.bluecam.api.bean.RecordBean;
import com.bluecam.bluecamlib.BCamera;
import com.zunder.smart.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class RecordListActivity extends BaseActivity implements RecordListAdapter.OnItemClickListener{
    private DatePicker datePicker;
    private Button query_btn;
    private RecyclerView record_list_view;
    private RecordListAdapter recordListAdapter;
    private List<RecordBean> recordList = new ArrayList<RecordBean>();
    private String camID;
    private BCamera camera;
    private int recordCount = 0;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        initView();
        camID = getIntent().getStringExtra("camID");
        camera = cameraManager.getCamera(camID);
        Calendar calendar = Calendar.getInstance();
        //默认查询当天录像
        camera.getTfVideoList(calendar.get(Calendar.YEAR),(calendar.get(Calendar.MONTH)+1),calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.YEAR),(calendar.get(Calendar.MONTH)+1),calendar.get(Calendar.DAY_OF_MONTH));
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
        setTitle_txt("录像列表");
        record_list_view    = (RecyclerView)findViewById(R.id.record_list_view);
        recordListAdapter  = new RecordListAdapter(this,recordList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        record_list_view.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        record_list_view.setAdapter(recordListAdapter);

        datePicker = (DatePicker)findViewById(R.id.datePicker);
        query_btn = (Button)findViewById(R.id.query_btn);
        query_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryRecordList();
            }
        });
    }

    private void queryRecordList(){
        int day = datePicker.getDayOfMonth();
        int month= datePicker.getMonth()+1;
        int year = datePicker.getYear();
        camera.getTfVideoList(year,month,day,year,month,day);
        recordList.clear();
        recordListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecordFileList(long camHandler, int filecount, String fileName, String strDate, int size) {
        if(camera == null){
            return;
        }
        if(filecount == 0){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            recordCount = recordCount+1;
            String date = "";
            try
            {
                Date time = format.parse(strDate);
                date = format.format(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            RecordBean recordBean = new RecordBean(camID,fileName,date,size);
            recordList.add(recordBean);
            if(filecount == recordCount){
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recordListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public void onItemClick(RecordBean recordBean) {
        System.out.println("------------------------------");
        Intent intent = new Intent(this,TfVideoPlayActivity.class);
        intent.putExtra("recordbean",recordBean);
        startActivity(intent);
    }
}
