package com.bluecam.api;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluecam.api.utils.AudioPlayer;
import com.bluecam.api.utils.CustomAudioRecorder;
import com.bluecam.api.utils.CustomBuffer;
import com.bluecam.api.utils.CustomBufferData;
import com.bluecam.api.utils.CustomBufferHead;
import com.bluecam.api.view.ShaderRender;
import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraContants;
import com.bluecam.bluecamlib.CameraManager;
import com.door.activity.DoorPlayActivity;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.MediaFactory;
import com.zunder.smart.gateway.GateWayPlayActivity;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.service.aduio.AduioService;
import com.zunder.smart.utils.ListNumBer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hsl.p2pipcam.nativecaller.DeviceSDK;

/**
 * Created by Administrator on 2017/8/1.
 */

public class CameraPlayActivity extends BaseActivity implements ShaderRender.RenderListener ,CustomAudioRecorder.AudioRecordResult,View.OnClickListener{
    private static final int      AUDIO_BUFFER_START_CODE = 0xff00ff;
    public final static String IMAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cameraDemo/image/";
    public final static String VIDEO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cameraDemo/video/";
    private GLSurfaceView gls_view;
    private ShaderRender  render;
    private Button audio_btn,ircut_btn;
    TextView listnerTxt,record_btn;
    private CameraManager cameraManager;
    private BCamera camera;
    private boolean       isLoaded = false;
    private int           videoW;
    private int           videoH;
    private AudioPlayer audioPlayer;
    private CustomBuffer audioBuffer ;
    private CustomAudioRecorder audioRecorder = null;
    private float mPosX;
    private float mPosY;
    private float mCurrentPosX;
    private float mCurrentPosY;
    private boolean startFlag;
    private Activity activity;
    private boolean isOpenAudio  = false;
    private boolean isStartRecord = false;
    private int flip;  //镜像状态
    private int ircut; //红外状态
    public static int start=0;
    private boolean isVideo = true;
    RelativeLayout cloundBtn, videoBtn;
    TextView cloundTxt, videoTxt;
    private ImageButton clarityVideo, listnerVideo;
    private ImageButton switchVideo, changeVideo, pointVideo, allscreen;
    private Button tempButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        activity=this;
        start=1;
        initView();
        String camID  = getIntent().getStringExtra("camID");
        int type  = getIntent().getIntExtra("type",0);
        cameraManager = CameraManager.getDeviceManager(this.getApplicationContext());
        cameraManager.registerEventListener(this);
        camera  = cameraManager.getCamera(camID);
        if(camera == null){
            finish();
        }
        audioBuffer = new CustomBuffer();
        audioRecorder = new CustomAudioRecorder(this);
        if(render!=null) {
            camera.setStreamCallBack(render);
        }
        camera.playStream(CameraContants.Resolution.MID);
        camera.getCameraParam(CameraContants.ParamKey.GET_CAMERA_PARAM_KEY);
        if(type==3){
            setTitle_txt("安防报警");
            MediaFactory.getInstance(activity).init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        start=0;
        camera.closeStream();
        stopPlayAudio();
    }

    private void initView() {
        initHeaderBar();
        setTitle_txt("视频播放");
        gls_view = (GLSurfaceView)findViewById(R.id.gls_view);
        render   = new ShaderRender(gls_view);
        gls_view.setRenderer(render);

        render.setListener(this);
        gls_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MediaFactory.getInstance(activity).stop();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        mPosX = event.getX();
                        mPosY = event.getY();
                        startFlag = false;
                        break;
                    // �ƶ�
                    case MotionEvent.ACTION_MOVE:
                        if (!startFlag) {
                            mCurrentPosX = event.getX();
                            mCurrentPosY = event.getY();

                            if (mPosY - mCurrentPosY > 25) {
                                new ControlCameraTask(CameraContants.Control.CMD_PTZ_UP).execute();
                                Log.e("move", "向上滑");
                                startFlag = true;
                            } else if (mCurrentPosY - mPosY > 25) {
                                new ControlCameraTask(CameraContants.Control.CMD_PTZ_DOWN).execute();
                                Log.e("move", "向下滑");
                                startFlag = true;
                            } else if (mPosX - mCurrentPosX > 25) {
                                new ControlCameraTask(CameraContants.Control.CMD_PTZ_RIGHT).execute();
                                Log.e("move", "向左滑");
                                startFlag = true;
                            } else if (mCurrentPosX - mPosX > 25) {
                                new ControlCameraTask(CameraContants.Control.CMD_PTZ_LEFT).execute();
                                Log.e("move", "向右滑");
                                startFlag = true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!startFlag) {
                            if ((Math.abs(mCurrentPosX - mPosX) < 25)
                                    && (Math.abs(mCurrentPosY - mPosY) < 25)) {
                                // showbtoom();
                            }
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        cloundBtn = (RelativeLayout) findViewById(R.id.cloudBtn);
        videoBtn = (RelativeLayout) findViewById(R.id.videoBtn);
        cloundTxt = (TextView) findViewById(R.id.cloudTxt);
        videoTxt = (TextView) findViewById(R.id.videoTxt);
        listnerTxt = (TextView) findViewById(R.id.listnerTxt);
        clarityVideo = (ImageButton) findViewById(R.id.clarityVideo);
        changeVideo = (ImageButton) findViewById(R.id.changeVideo);
        switchVideo = (ImageButton) findViewById(R.id.switchVideo);
        pointVideo = (ImageButton) findViewById(R.id.pointVideo);
        listnerVideo = (ImageButton) findViewById(R.id.listnerVideo);
        tempButton = (Button) findViewById(R.id.temp_aduio);

        record_btn=(TextView)findViewById(R.id.record_btn);
        allscreen = (ImageButton) findViewById(R.id.allscreen);
        listnerVideo.setOnClickListener(this);
        changeVideo.setOnClickListener(this);
        switchVideo.setOnClickListener(this);
        clarityVideo.setOnClickListener(this);
        pointVideo.setOnClickListener(this);
        cloundTxt.setOnClickListener(this);
        videoTxt.setOnClickListener(this);
        allscreen.setOnClickListener(this);
        tempButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (isVideo) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            AduioService aduioService = AduioService.getInstance();
                            aduioService.setInit(activity);
                            break;
                        default:
                            break;
                    }
                } else {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        virbate();
                        stopPlayAudio();
                        camera.openTalk();
                        if (audioRecorder != null) {
                            audioRecorder.startRecord();
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        //结束说话
                        playAudio();
                        camera.closeTalk();
                        if (audioRecorder != null) {
                            audioRecorder.stopRecord();
                        }
                    }
                }
                return true;
            }
        });

        cloundBtn.setBackgroundColor(R.color.green);
    }
    /*
        红外开关事件
     */
    public void openIrcut(View view){
        if(ircut == 0){
            camera.setCameraParam(14,1); //打开
        }
        else{
            camera.setCameraParam(14,0); //关闭
        }
    }





    public void playAudio()
    {
        if(camera == null) {
            return;
        }
        if(camera.getStatus() != CameraContants.DeviceStatus.DEVICE_STATUS_ON_LINE)
            return;
        audioBuffer.ClearAll();
        if(audioPlayer == null)
        {
            audioPlayer = new AudioPlayer(audioBuffer);
        }
        audioPlayer.AudioPlayStart();
        camera.openAudio();
    }

    public void stopPlayAudio()
    {
        audioBuffer.ClearAll();
        if(audioPlayer != null)
        {
            if(camera != null)
                camera.closeAudio();
            audioPlayer.AudioPlayStop();
            audioPlayer = null;
        }
    }
    @Override
    public void loadComplete(int size, int width, int height) {
        if(!isLoaded){
            isLoaded = true;
            videoW = width;
            videoH = height;
        }
    }

    @Override
    public void AudioRecordData(byte[] data, int len) {
        if(camera == null)
            return;
        //发送说话数据
        camera.sendTalkData(data,len);
    }

    @Override
    public void onAudioDataEvent(long camHandler, byte[] pcm, int size) {
        if(camera == null)
            return;
        if(camHandler == camera.getCamHandler())
        {
            CustomBufferHead head = new CustomBufferHead();
            CustomBufferData data = new CustomBufferData();
            head.length = size;
            head.startcode = AUDIO_BUFFER_START_CODE;
            data.head = head;
            data.data = pcm;
            if(audioPlayer != null) {
                if(audioPlayer.isAudioPlaying())
                    audioBuffer.addData(data);
            }
        }
    }

    @Override
    public void onGetParamsEvent(long camHandler, long paramKey, String params) {
        if(camera == null){
            return;
        }
        if(camHandler == camera.getCamHandler()){
            if(paramKey == CameraContants.ParamKey.GET_CAMERA_PARAM_KEY){
                JSONObject obj = null;
                try {
                    obj = new JSONObject(params);
                    flip = obj.getInt("flip");   // 镜像属性
                    ircut = obj.getInt("ircut"); //红外开关属性
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(ircut == 0){
//                                ircut_btn.setText("打开红外");
                            }
                            else{
//                                ircut_btn.setText("关闭红外");
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switchVideo:
                showVideoWindow();
                break;
            case R.id.changeVideo:
                showChangeWindow();
                break;
            case R.id.clarityVideo:
                showClarityWindow();
                break;

            case R.id.pointVideo:
                if(camera == null){
                    return;
                }
                if(!isStartRecord){
                    SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String strDate = f.format(new Date());
                    File videoDir = new File(VIDEO_PATH);
                    if(!videoDir.exists())
                        videoDir.mkdirs();
                    String fileName = VIDEO_PATH + strDate+"_"+camera.getCameraBean().getDevID()+".mp4";
                    System.out.println("----------------fileName == "+fileName);
                    camera.startRecordVideo(fileName, videoW, videoH);
                    record_btn.setText("正在录像");
                    record_btn.setTextColor(Color.RED);
                    isStartRecord=true;
                }
                else{
                    camera.stopRecordVideo();
                    record_btn.setText("录像");
                    record_btn.setTextColor(Color.BLACK);
                    isStartRecord=false;
                }

                break;
            case R.id.listnerVideo:
                if(!isOpenAudio){
                    isOpenAudio = true;
                    playAudio();
                    listnerTxt.setText("静音");
                    listnerTxt.setTextColor(Color.RED);
                }
                else{
                    isOpenAudio = false;
                    stopPlayAudio();
                    listnerTxt.setText("声音");
                    listnerTxt.setTextColor(Color.BLACK);
                }
                break;
            case  R.id.allscreen:
                SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String strDate = f.format(new Date());
                String fileName = strDate+"_"+camera.getCameraBean().getDevID()+".jpg";
                File imageDir = new File(IMAGE_PATH);
                if(!imageDir.exists())
                    imageDir.mkdirs();
                String path = IMAGE_PATH + fileName;
                System.out.println("path =="+path);
                camera.takePicture(path);showToast("拍照成功");
                break;
            case R.id.cloudTxt:
                isVideo = true;
                videoBtn.setBackgroundColor(Color.WHITE);
                cloundBtn.setBackgroundColor(R.color.green);
                break;
            case R.id.videoTxt:
                isVideo = false;
                videoBtn.setBackgroundColor(R.color.green);
                cloundBtn.setBackgroundColor(Color.WHITE);
                break;
            default:
                break;
        }
    }

    /**
     * 处理上下左右控制
     */
    private class ControlCameraTask extends AsyncTask<Void, Void, Integer>
    {
        private int type;
        public ControlCameraTask(int type) {
            this.type = type;
        }
        @Override
        protected Integer doInBackground(Void... arg0)
        {

            if(type == CameraContants.Control.CMD_PTZ_RIGHT)
            {
                camera.controlCamera(CameraContants.Control.CMD_PTZ_RIGHT);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                camera.controlCamera(CameraContants.Control.CMD_PTZ_RIGHT_STOP);
            }
            else if(type == CameraContants.Control.CMD_PTZ_LEFT)
            {
                camera.controlCamera(CameraContants.Control.CMD_PTZ_LEFT);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                camera.controlCamera(CameraContants.Control.CMD_PTZ_LEFT_STOP);
            }
            else if(type == CameraContants.Control.CMD_PTZ_UP)
            {
                camera.controlCamera(CameraContants.Control.CMD_PTZ_UP);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                camera.controlCamera(CameraContants.Control.CMD_PTZ_UP_STOP);
            }
            else if(type == CameraContants.Control.CMD_PTZ_DOWN)
            {
                camera.controlCamera(CameraContants.Control.CMD_PTZ_DOWN);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                camera.controlCamera(CameraContants.Control.CMD_PTZ_DOWN_STOP);
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

        }
    }

    private void showVideoWindow() {
        final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.videoswitch),getGateWay(),null,0);
        alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
            @Override
            public void onItem(int pos, String itemName) {
                GateWay gateWay = getGateWay(itemName);
                if (gateWay != null) {
                    if (gateWay.getTypeId() == 3) {

                        finish();
                        overridePendingTransition(0, 0);
                        Intent intent = new Intent(MyApplication.getInstance(), DoorPlayActivity.class);
                        intent.putExtra("Id", gateWay.getGatewayID());
                        intent.putExtra("Pwd", gateWay.getUserPassWord());
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }   if (gateWay.getTypeId() == 6) {

                        finish();
                        overridePendingTransition(0, 0);
                        Intent intent = new Intent(MyApplication.getInstance(), CameraPlayActivity.class);
                        intent.putExtra("camID", gateWay.getGatewayID());
                        startActivity(intent);
                        overridePendingTransition(0, 0);

                    } else {
                        finish();
                        overridePendingTransition(0,0);
                        String s = gateWay.getTypeId() + "";
                        String did = gateWay.getGatewayID();
                        Intent intent = new Intent(activity, GateWayPlayActivity.class);
                        intent.putExtra("cid",
                                Integer.parseInt(GateWayService.mp.get(gateWay.getGatewayID())));
                        intent.putExtra("Cam_name", gateWay.getGatewayName());
                        intent.putExtra("Cam_did", did);
                        intent.putExtra("type", s);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                    alertViewWindow.dismiss();
                }
            }
        });

        alertViewWindow.show();
    }
    private void showClarityWindow() {
        final AlertViewWindow alertViewWindow = new AlertViewWindow(activity, getString(R.string.definition), ListNumBer.getClarity(),null,0);
        alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {

            @Override
            public void onItem(int pos, String itemName) {
                camera.closeStream();
                stopPlayAudio();
                camera.playStream(pos);
                alertViewWindow.dismiss();
            }
        });
        alertViewWindow.show();
    }
    private void showChangeWindow() {
        final AlertViewWindow alertViewWindow = new AlertViewWindow(activity, getString(R.string.videochange), ListNumBer.getChange(),null,0);
        alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {

            @Override
            public void onItem(int pos, String itemName) {
                try {
                    camera.setCameraParam(5,pos);
                    alertViewWindow.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertViewWindow.show();
    }

    public GateWay getGateWay(String gateWayName) {
        GateWay gateWay=null;
        List<GateWay> list = GateWayService.list;
        for (int i = 0; i < list.size(); i++) {
            gateWay = list.get(i);
            if (gateWay.getGatewayName().equals(gateWayName)) {
                break;
            }
        }
        return gateWay;
    }
    public List<String> getGateWay() {
        List<String> resultLlist = new ArrayList<String>();
        List<GateWay> list = GateWayService.list;
        for (int i = 0; i < list.size(); i++) {
            GateWay gateWay = list.get(i);
            if (gateWay.getTypeId() < 4||gateWay.getTypeId()==6) {
                resultLlist.add(gateWay.getGatewayName());
            }
        }
        return resultLlist;
    }
}
