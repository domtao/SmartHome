package com.zunder.smart.aiui.activity;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.ApkAlert;
import com.zunder.smart.dialog.ButtonAlert;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.DownAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.listener.DownListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.popwindow.SensorPopupWindow;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.JSONHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AiuiMainActivity extends Activity implements OnClickListener ,DownListener {
	public static AiuiMainActivity activity;
	public static String deviceID = "";

	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, AiuiMainActivity.class);
		activity.startActivity(intent);
	}
	TextView backTxt;
	TextView editeTxt;
	LinearLayout deviceImage, bookImage, answerImage, chartImage, storyImag,
			musicImage, modeImage, downImage, poetryImage, anHong,bluetoothLayout,gatewayLayout;
	LinearLayout sensorLayout;
	private RightMenu rightButtonMenuAlert;
	public static GateWay gateWay = null;
	SeekBar volSeek;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_aiui_main);
		activity = this;
		backTxt = (TextView) findViewById(R.id.backTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		deviceImage = (LinearLayout) findViewById(R.id.device);
		bookImage = (LinearLayout) findViewById(R.id.book);
		answerImage = (LinearLayout) findViewById(R.id.answer);
		chartImage = (LinearLayout) findViewById(R.id.chart);
		storyImag = (LinearLayout) findViewById(R.id.story);
		musicImage = (LinearLayout) findViewById(R.id.music);
		modeImage = (LinearLayout) findViewById(R.id.mode);
		poetryImage = (LinearLayout) findViewById(R.id.poetry);
		anHong = (LinearLayout) findViewById(R.id.anHong);
		sensorLayout=(LinearLayout)findViewById(R.id.sensorLayout);
		bluetoothLayout=(LinearLayout)findViewById(R.id.bluetoothLayout);
		bluetoothLayout.setOnClickListener(this);
		gatewayLayout=(LinearLayout)findViewById(R.id.gatewayLayout);
		gatewayLayout.setOnClickListener(this);
		volSeek=(SeekBar)findViewById(R.id.volSeek);
		volSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				int value = seekBar.getProgress();
				String result = ISocketCode.setForwardSong("vol:"+value,
						AiuiMainActivity.deviceID);
				MainActivity.getInstance().sendCode(result);
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			public void onProgressChanged(SeekBar seekBar, int progress,
										  boolean fromUser) {
			}
		});
		backTxt.setOnClickListener(this);
		deviceImage.setOnClickListener(this);
		bookImage.setOnClickListener(this);
		answerImage.setOnClickListener(this);
		chartImage.setOnClickListener(this);
		storyImag.setOnClickListener(this);
		musicImage.setOnClickListener(this);
		modeImage.setOnClickListener(this);
		poetryImage.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		anHong.setOnClickListener(this);
		sensorLayout.setOnClickListener(this);
		gateWay = GateWayFactory.getInstance().getGateWay(AiuiMainActivity.deviceID);
		if (gateWay!=null&&gateWay.getUserName().equals("admin")) {
			initRightButtonMenuAlert();
		} else {
			editeTxt.setVisibility(View.GONE);
		}
		ReceiverBroadcast.setDownListener(this);
		String result = ISocketCode.setVersionApk("GetVersion",
				AiuiMainActivity.deviceID);
		MainActivity.getInstance().sendCode(result);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return false;
	}

	@SuppressLint("ResourceAsColor")
	private void initRightButtonMenuAlert() {

		rightButtonMenuAlert = new RightMenu(activity, R.array.right_aiui,
				R.drawable.right_aiui_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						DialogAlert alert = new DialogAlert(activity);
						alert.init(activity.getString(R.string.tip), getString(R.string.aiui1)
								+ ProjectUtils.rootPath.getRootName()
								+ getString(R.string.aiui2));
						alert.setSureListener(new DialogAlert.OnSureListener() {
							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								String result = ISocketCode.setDelAllDevice("del", deviceID);
								MainActivity.getInstance().sendCode(result);
								DownAlert downAlert = new DownAlert(activity);
								downAlert.show();
							}
							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						alert.show();
						break;
					case 1:
					clear();
					break;
					case 2:
						ReceiverBroadcast.setDownListener(activity);
						String result = ISocketCode.setVersionApk("GetVersion",
								AiuiMainActivity.deviceID);
						MainActivity.getInstance().sendCode(result);
						showDialog();
						break;

					case 3:
						final ButtonAlert openAlert = new ButtonAlert(activity);
						openAlert.setTitle(R.mipmap.info_systemset,getString(R.string.tip), getString(R.string.speech_play));
						openAlert.setButton(getString(R.string.open_1), getString(R.string.close_1), getString(R.string.cancle));
						openAlert.setVisible(View.VISIBLE, View.VISIBLE,
								View.VISIBLE);
						openAlert
								.setOnSureListener(new ButtonAlert.OnSureListener() {
									@Override
									public void onSure() {
										// TODO Auto-generated method stub
										String result = ISocketCode
												.setForwardVersion("0", deviceID);
										MainActivity.getInstance().sendCode(result);
										openAlert.dismiss();
									}
									@Override
									public void onSearch() {
										// TODO Auto-generated method stub
										String result = ISocketCode
												.setForwardVersion("1", deviceID);
										MainActivity.getInstance().sendCode(result);
										openAlert.dismiss();
									}
									@Override
									public void onCancle() {
										// TODO Auto-generated method stub
									}
								});
						openAlert.show();
						break;
				}
				rightButtonMenuAlert.dismiss();
			}
		});
	}
	WarnDialog warnDialog = null;
	public void showDialog(){
		if (warnDialog == null) {
			warnDialog = new WarnDialog(activity, getString(R.string.search));
			warnDialog.setMessage("正在获取软件版本信息 5");
			warnDialog.setSureListener(new WarnDialog.OnSureListener() {
				@Override
				public void onCancle() {
					searchflag = false;
					warnDialog.dismiss();
				}
			});
		}
		warnDialog.show();
		startTime();
	}
	private boolean searchflag = false;
	private int startCount = 0;

	private void startTime() {
		startCount=0;
		searchflag=true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(1000);
						startCount++;
						if (startCount >= 5) {
							searchflag = false;
							if ((warnDialog != null) && warnDialog.isShowing()) {
								warnDialog.dismiss();
								handler.sendEmptyMessage(-1);
							}
						}else{
							Message message = handler.obtainMessage();
							message.what = -2;
							message.obj = startCount;
							handler.sendMessage(message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ReceiverBroadcast.setDownListener(null);


	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
		case R.id.editeTxt:
			rightButtonMenuAlert.show(editeTxt);
			break;
		case R.id.device:
			CusDeviceActivity.startActivity(activity);
			break;
		case R.id.book:
			SubscribeActivity.startActivity(activity);
			break;
		case R.id.answer:
			VoiceActivity.startActivity(activity);
			break;
		case R.id.poetry:
			SongActivity.startActivity(activity, "poetry",getString(R.string.peotry));
			break;
		case R.id.chart:
			SongActivity.startActivity(activity, "joke", getString(R.string.joke));
			break;
		case R.id.story:
			SongActivity.startActivity(activity, "strory",getString(R.string.story) );
			break;
		case R.id.music:
			SongActivity.startActivity(activity, "music", getString(R.string.aiui_music));
			break;
		case R.id.mode:
			CusModeActivity.startActivity(activity);
			break;
		case R.id.anHong:
			AnHongActivity.startActivity(activity);
			break;
		case R.id.sensorLayout:
			SensorPopupWindow sensorPopupWindow=new SensorPopupWindow(activity,getString(R.string.sensor));
			sensorPopupWindow.setOnOCListene(new SensorPopupWindow.OnOCListener() {
				@Override
				public void onResult(String result) {
					String cmd = ISocketCode.setForwardNameControl("请添加"+result,
							AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(cmd);
				}
			});
			sensorPopupWindow.show();
			break;
			case R.id.bluetoothLayout:
				BlueToothActivity.startActivity(activity);
				break;
			case R.id.gatewayLayout:
				HostActivity.startActivity(activity);
				break;

			default:
			break;
		}
	}

	public void clear() {
		DialogAlert alert = new DialogAlert(activity);
		alert.init(getString(R.string.tip), getString(R.string.isCleanDevice));
		alert.setSureListener(new DialogAlert.OnSureListener() {
			@Override
			public void onSure() {
				// TODO Auto-generated method stub
				String result = ISocketCode.setDelAllDevice("del", deviceID);
				MainActivity.getInstance().sendCode(result);
			}
			@Override
			public void onCancle() {
				// TODO Auto-generated method stub
			}
		});
		alert.show();
	}

	public void back() {
		finish();

	}
	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
				case -2:
					int index=Integer.parseInt(msg.obj.toString());
					warnDialog.setMessage("正在获取软件版本信息 "+(5-index));
					break;
				case -1:
					ApkAlert apkAlert = new ApkAlert(activity);
					apkAlert.show();
					break;
				case 0:
					if ((warnDialog != null) && warnDialog.isShowing()) {
						searchflag = false;
						startCount=0;
						warnDialog.dismiss();
					}
					TipAlert alert = new TipAlert(activity,
							getString(R.string.tip), msg.obj.toString());
					alert.show();
					break;
				case 1:
					if ((warnDialog != null) && warnDialog.isShowing()) {
						searchflag = false;
						startCount=0;
						warnDialog.dismiss();
					}
					break;
				case 2: {
					if ((warnDialog != null) && warnDialog.isShowing()) {
						searchflag = false;
						startCount = 0;
						warnDialog.dismiss();
					}
					String result = msg.obj.toString();
					int index2 = result.indexOf("vol");
					if (index2 != -1) {
						ToastUtils.ShowSuccess(activity,result.substring(0,index2), Toast.LENGTH_LONG,true);
						int vol =Integer.parseInt(result.substring(index2, result.length()).replace("vol:", ""));
						volSeek.setProgress(vol);
					} else {
						ToastUtils.ShowSuccess(activity,result, Toast.LENGTH_LONG,true);
					}
				}
					break;
				case 3:
					if ((warnDialog != null) && warnDialog.isShowing()) {
						searchflag = false;
						startCount=0;
						warnDialog.dismiss();
					}

					String result = msg.obj.toString();
					int index2 = result.indexOf("vol");
					if (index2 != -1) {
						ToastUtils.ShowSuccess(activity,result.substring(0,index2), Toast.LENGTH_LONG,true);
						int vol =Integer.parseInt(result.substring(index2, result.length()).replace("vol:", ""));
						volSeek.setProgress(vol);
						result=result.substring(0,index2);
					}
					ToastUtils.ShowError(activity,result, Toast.LENGTH_LONG,true);
					ApkAlert apkAlert0 = new ApkAlert(activity);
					apkAlert0.show();
					break;
				case 4:
					bluetoothLayout.setVisibility(View.VISIBLE);
//					gatewayLayout.setVisibility(View.VISIBLE);
					break;
				default:
					break;
			}
		}
	};
	@Override
	public void count(String number) {
		if(number.contains("Blue")){
			handler.sendEmptyMessage(4);
		}
		if(number.startsWith("ApkNew")){
			Message message=handler.obtainMessage();
			message.what=2;
			message.obj=number.replace("ApkNew:","").replace("Blue_","");
			handler.dispatchMessage(message);
		}else if(number.startsWith("ApkUpdate")){
			Message message=handler.obtainMessage();
			message.what=3;
			message.obj=number.replace("ApkUpdate:","").replace("Blue_","");
			handler.dispatchMessage(message);
		}
	}
}