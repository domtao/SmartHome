package com.bluecam.api;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bluecam.api.bean.RecordBean;
import com.bluecam.api.utils.AudioPlayer;
import com.bluecam.api.utils.CustomBuffer;
import com.bluecam.api.utils.CustomBufferData;
import com.bluecam.api.utils.CustomBufferHead;
import com.bluecam.api.view.ShaderRender;
import com.bluecam.bluecamlib.BCamera;
import com.zunder.smart.R;

/**
 * Created by Administrator on 2017/8/7.
 */

public class TfVideoPlayActivity extends BaseActivity {
    private GLSurfaceView gls_view;
    private ShaderRender render;
    private TextView pause_btn;
    private SeekBar play_record_seekbar;
    private BCamera camera;
    private String camID;
    private RecordBean bean;
    private boolean  isPause = false;
    private int progress = 0;

    private static final int      AUDIO_BUFFER_START_CODE = 0xff00ff;
    private AudioPlayer audioPlayer;
    private CustomBuffer audioBuffer ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_play_tfvideo);
        bean = (RecordBean) getIntent().getSerializableExtra("recordbean");
        if(bean == null){
            System.out.println("+++++++++++++++++++++++++++++++++");
            finish();
            return;
        }
        camID = bean.getCamID();
        camera = cameraManager.getCamera(camID);
        if(camera == null){
            finish();
            return;
        }
        initView();

        audioBuffer = new CustomBuffer();
        audioPlayer = new AudioPlayer(audioBuffer);
        audioPlayer.AudioPlayStart();
        camera.setRecordCallBack(render);
        camera.openTfRecord(bean.getFileName(),progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.closeTfRecord(bean.getFileName());
        audioBuffer.ClearAll();
        if(audioPlayer != null)
        {
            audioPlayer.AudioPlayStop();
            audioPlayer = null;
        }
    }

    private void initView() {
        gls_view          = (GLSurfaceView)findViewById(R.id.gls_view);
        render            = new ShaderRender(gls_view);
        gls_view.setRenderer(render);
        play_record_seekbar = (SeekBar)findViewById(R.id.play_record_seekbar);

        play_record_seekbar.setMax(bean.getSize());
        pause_btn         = (TextView) findViewById(R.id.pause_btn);
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isPause){
                    isPause = true;
                    camera.closeTfRecord(bean.getFileName());
                    pause_btn.setText("开始");
                }
                else {
                    isPause = false;
                    camera.openTfRecord(bean.getFileName(),progress);
                    pause_btn.setText("暂停");
                }
            }
        });
        play_record_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0)
            {
                progress = arg0.getProgress();
                camera.playTfPos(bean.getFileName(),progress);
                isPause = false;
            }
            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                isPause = true;
            }
            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2)
            {}
        });
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
    public void onRecordPlayPos(long camHandler, int pos) {
        if(!isPause) {
            System.out.println("pos ====="+pos);
            this.progress = pos;
        }
    }
}
