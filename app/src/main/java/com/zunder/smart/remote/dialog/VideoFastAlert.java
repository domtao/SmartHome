package com.zunder.smart.remote.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.socket.info.ISocketCode;

//ȷ����ʾ��
public class VideoFastAlert extends Dialog implements OnClickListener {

	private Activity activity;
	private TextView titleTxt;
	private ImageView closeImg;
	private int type=0;
	private LinearLayout allScreen,back,forward;
	public VideoFastAlert(Activity _context,int type) {
		super(_context, R.style.MyDialog);
		this.activity = _context;
		this.type=type;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_video_fast_verify);
		titleTxt = (TextView) findViewById(R.id.title_tv);
		closeImg = (ImageView) findViewById(R.id.closeImg);
		allScreen=(LinearLayout)findViewById(R.id.allscreen);
		back=(LinearLayout)findViewById(R.id.back);
		forward=(LinearLayout)findViewById(R.id.forward);
		closeImg.setOnClickListener(this);
		allScreen.setOnClickListener(this);
		back.setOnClickListener(this);
		forward.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.closeImg:
			dismiss();
			break;
			case R.id.allscreen:{
				String result = ISocketCode.setForwardRemoteControl("allScreen|全屏播放",
						RemoteMainActivity.deviceID,type);
				MainActivity.getInstance().sendCode(result);
				ToastUtils.ShowSuccess(activity, "全屏播放", Toast.LENGTH_SHORT, true);
			}
				break;
			case R.id.back:{
				String result = ISocketCode.setForwardRemoteControl("down|后退",
						RemoteMainActivity.deviceID,type);
				MainActivity.getInstance().sendCode(result);
				ToastUtils.ShowSuccess(activity, "后退", Toast.LENGTH_SHORT, true);
			}
				break;
			case R.id.forward:{
				String result = ISocketCode.setForwardRemoteControl("up|快进",
						RemoteMainActivity.deviceID,type);
				MainActivity.getInstance().sendCode(result);
				ToastUtils.ShowSuccess(activity, "快进", Toast.LENGTH_SHORT, true);
			}
				break;
		default:
			break;
		}
	}
}
