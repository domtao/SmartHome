/**
 * 
 */
package com.zunder.smart.gateway;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import org.json.JSONException;
import org.json.JSONObject;

import com.zunder.smart.R;
import com.zunder.smart.adapter.GateWayAdapter;
import com.zunder.smart.gateway.bean.AlarmModel;
import com.zunder.smart.listener.DevicePramsListener;
import com.zunder.smart.service.BridgeService;
import com.zunder.smart.adapter.GateWayAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��������
 * 
 * @author wang.jingui
 * 
 */
public class SettingAlarmActivity extends BaseActivity implements
		OnClickListener, DevicePramsListener, OnCheckedChangeListener,
		OnTouchListener {
	private CheckBox alerm_cbx_move_layout;
	private CheckBox alerm_cbx_io_layout;
	private CheckBox alerm_cbx_pir_layout;
	private CheckBox alerm_cbx_audio_layout;
	private CheckBox alerm_cbx_io_move;
	private CheckBox alerm_cbx_mail;
	private CheckBox alerm_cbx_isrecord;
	private CheckBox alerm_cbx_upload_picture;
	private CheckBox alerm_cbx_smart_record_layout;

	private RelativeLayout alerm_moveview;
	private RelativeLayout alerm_ioview;
	private RelativeLayout alerm_audio_level;
	private RelativeLayout alerm_io_move_view;
	private RelativeLayout alerm_uploadpicture_view;
	private LinearLayout alerm_eventview;
	private ScrollView scrollView;
	private LinearLayout smart_record_option_view;

	private TextView alerm_tv_sensitivity;
	private TextView alerm_tv_triggerlevel;
	private TextView alerm_audio_triggerlevel;
	private TextView alerm_tv_preset;
	private TextView alerm_tv_ioout_level_value;
	private TextView alarm_io_layout;

	private ImageView alerm_img_sensitive_drop;
	private ImageView alerm_img_leveldrop;
	private ImageView alerm_audio_leveldrop;
	private ImageView alerm_img_preset_drop;
	private ImageView alerm_img_ioout_level_drop;
	private ImageView alerm_io;
	private EditText alerm_edit_picture_interval;

	private PopupWindow sensitivePopWindow = null;
	private PopupWindow ioOutLevelPopWindow = null;
	private PopupWindow presteMovePopWindow = null;
	private PopupWindow audioPopWindow = null;
	private PopupWindow triggerLevelPopWindow = null;
	private long userid;
	private boolean isFun = false;
	private AlarmModel alarmModel = new AlarmModel();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gateway_alarm_screen);

		initView();
		showProgressDialog("正在获取数据");
		userid = GateWayAdapter.userid;
		BridgeService.setDevicePramsListener(this);
		DeviceSDK.getDeviceParam(userid, 0x2018);
	}

	private void initView() {
		backItem = (TextView) findViewById(R.id.back_item);
		backItem.setText("返回");
		titleItem = (TextView) findViewById(R.id.title_item);
		titleItem.setText("报警设置");
		functionItem = (TextView) findViewById(R.id.fun_item);
		functionItem.setOnClickListener(this);
		backItem.setOnClickListener(this);
		alerm_cbx_move_layout = (CheckBox) findViewById(R.id.alerm_cbx_move_layout);
		alerm_cbx_io_layout = (CheckBox) findViewById(R.id.alerm_cbx_io_layout);
		alerm_cbx_pir_layout = (CheckBox) findViewById(R.id.alerm_cbx_pir_layout);
		alerm_cbx_audio_layout = (CheckBox) findViewById(R.id.alerm_cbx_audio_layout);
		alerm_cbx_io_move = (CheckBox) findViewById(R.id.alerm_cbx_io_move);
		alerm_cbx_mail = (CheckBox) findViewById(R.id.alerm_cbx_mail);
		alerm_cbx_isrecord = (CheckBox) findViewById(R.id.alerm_cbx_isrecord);
		alerm_cbx_upload_picture = (CheckBox) findViewById(R.id.alerm_cbx_upload_picture);

		alerm_cbx_smart_record_layout = (CheckBox) findViewById(R.id.alerm_cbx_smart_record_layout);
		alerm_moveview = (RelativeLayout) findViewById(R.id.alerm_moveview);
		alerm_ioview = (RelativeLayout) findViewById(R.id.alerm_ioview);
		alerm_audio_level = (RelativeLayout) findViewById(R.id.alerm_audio_level);
		alerm_io_move_view = (RelativeLayout) findViewById(R.id.alerm_io_move_view);
		alerm_uploadpicture_view = (RelativeLayout) findViewById(R.id.alerm_uploadpicture_view);
		alerm_eventview = (LinearLayout) findViewById(R.id.alerm_eventview);
		smart_record_option_view = (LinearLayout) findViewById(R.id.smart_record_option_view);

		alarm_io_layout = (TextView) findViewById(R.id.alarm_io_layout);
		alerm_io = (ImageView) findViewById(R.id.alerm_io);
	
		scrollView = (ScrollView) findViewById(R.id.scrollView1);

		alerm_tv_sensitivity = (TextView) findViewById(R.id.alerm_tv_sensitivity);
		alerm_tv_triggerlevel = (TextView) findViewById(R.id.alerm_tv_triggerlevel);
		alerm_audio_triggerlevel = (TextView) findViewById(R.id.alerm_audio_triggerlevel);
		alerm_tv_preset = (TextView) findViewById(R.id.alerm_tv_preset);
		alerm_tv_ioout_level_value = (TextView) findViewById(R.id.alerm_tv_ioout_level_value);

		alerm_img_sensitive_drop = (ImageView) findViewById(R.id.alerm_img_sensitive_drop);
		alerm_img_leveldrop = (ImageView) findViewById(R.id.alerm_img_leveldrop);
		alerm_audio_leveldrop = (ImageView) findViewById(R.id.alerm_audio_leveldrop);
		alerm_img_preset_drop = (ImageView) findViewById(R.id.alerm_img_preset_drop);
		alerm_img_ioout_level_drop = (ImageView) findViewById(R.id.alerm_img_ioout_level_drop);

		alerm_edit_picture_interval = (EditText) findViewById(R.id.alerm_edit_picture_interval);

		alerm_cbx_move_layout.setOnCheckedChangeListener(this);
		alerm_cbx_io_layout.setOnCheckedChangeListener(this);
		alerm_cbx_pir_layout.setOnCheckedChangeListener(this);
		alerm_cbx_audio_layout.setOnCheckedChangeListener(this);
		alerm_cbx_io_move.setOnCheckedChangeListener(this);
		alerm_cbx_mail.setOnCheckedChangeListener(this);
		alerm_cbx_isrecord.setOnCheckedChangeListener(this);
		alerm_cbx_upload_picture.setOnCheckedChangeListener(this);
		alerm_cbx_smart_record_layout.setOnCheckedChangeListener(this);

		alerm_img_sensitive_drop.setOnClickListener(this);
		alerm_img_leveldrop.setOnClickListener(this);
		alerm_audio_leveldrop.setOnClickListener(this);
		alerm_img_preset_drop.setOnClickListener(this);
		alerm_img_ioout_level_drop.setOnClickListener(this);

		scrollView.setOnTouchListener(this);
	}

	private void initInputPopupWindow() {
		LinearLayout triggerLayout = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.alermtriggerpopwindow, null);
		TextView tvHight = (TextView) triggerLayout
				.findViewById(R.id.trigger_hight);
		TextView tvLow = (TextView) triggerLayout
				.findViewById(R.id.trigger_low);
		tvLow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				alerm_tv_triggerlevel.setText(getResources().getString(
						R.string.alerm_ioin_levellow));
				alarmModel.setIoin_level(0);
			}
		});
		tvHight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				alerm_tv_triggerlevel.setText(getResources().getString(
						R.string.alerm_ioin_levelhight));
				alarmModel.setIoin_level(1);
			}
		});
		triggerLevelPopWindow = new PopupWindow(triggerLayout, 200,
				WindowManager.LayoutParams.WRAP_CONTENT);
		triggerLevelPopWindow.showAsDropDown(alerm_img_leveldrop, -140, 0);
	}

	private void initPresetPopupWindow() {
		LinearLayout preLayout = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.alermpresetmovepopwindow, null);
		TextView presetNo = (TextView) preLayout.findViewById(R.id.preset_no);
		TextView preset1 = (TextView) preLayout.findViewById(R.id.preset_1);
		TextView preset2 = (TextView) preLayout.findViewById(R.id.preset_2);
		TextView preset3 = (TextView) preLayout.findViewById(R.id.preset_3);
		TextView preset4 = (TextView) preLayout.findViewById(R.id.preset_4);
		TextView preset5 = (TextView) preLayout.findViewById(R.id.preset_5);
		TextView preset6 = (TextView) preLayout.findViewById(R.id.preset_6);
		TextView preset7 = (TextView) preLayout.findViewById(R.id.preset_7);
		TextView preset8 = (TextView) preLayout.findViewById(R.id.preset_8);
		TextView preset9 = (TextView) preLayout.findViewById(R.id.preset_9);
		TextView preset10 = (TextView) preLayout.findViewById(R.id.preset_10);
		TextView preset11 = (TextView) preLayout.findViewById(R.id.preset_11);
		TextView preset12 = (TextView) preLayout.findViewById(R.id.preset_12);
		TextView preset13 = (TextView) preLayout.findViewById(R.id.preset_13);
		TextView preset14 = (TextView) preLayout.findViewById(R.id.preset_14);
		TextView preset15 = (TextView) preLayout.findViewById(R.id.preset_15);
		TextView preset16 = (TextView) preLayout.findViewById(R.id.preset_16);
		presetNo.setOnClickListener(this);
		preset1.setOnClickListener(this);
		preset2.setOnClickListener(this);
		preset3.setOnClickListener(this);
		preset4.setOnClickListener(this);
		preset5.setOnClickListener(this);
		preset6.setOnClickListener(this);
		preset7.setOnClickListener(this);
		preset8.setOnClickListener(this);
		preset9.setOnClickListener(this);
		preset10.setOnClickListener(this);
		preset11.setOnClickListener(this);
		preset12.setOnClickListener(this);
		preset13.setOnClickListener(this);
		preset14.setOnClickListener(this);
		preset15.setOnClickListener(this);
		preset16.setOnClickListener(this);
		presteMovePopWindow = new PopupWindow(preLayout, 160,
				WindowManager.LayoutParams.WRAP_CONTENT);
		presteMovePopWindow.showAsDropDown(alerm_img_preset_drop, -130, 0);
	}

	private void initIOlinkMovePopupWindow() {
		LinearLayout outLayout = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.alermiooutpopwindow, null);
		TextView outHight = (TextView) outLayout.findViewById(R.id.ioout_hight);
		TextView outLow = (TextView) outLayout.findViewById(R.id.ioout_low);
		outHight.setOnClickListener(this);
		outLow.setOnClickListener(this);
		ioOutLevelPopWindow = new PopupWindow(outLayout, 200,
				WindowManager.LayoutParams.WRAP_CONTENT);
		ioOutLevelPopWindow.showAsDropDown(alerm_img_ioout_level_drop, -140, 0);
	}

	private void initMovePopupWindow() {
		LinearLayout layout1 = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.alermsensitivepopwindow, null);
		TextView sensitive10 = (TextView) layout1
				.findViewById(R.id.sensitive_10);
		TextView sensitive9 = (TextView) layout1.findViewById(R.id.sensitive_9);
		TextView sensitive8 = (TextView) layout1.findViewById(R.id.sensitive_8);
		TextView sensitive7 = (TextView) layout1.findViewById(R.id.sensitive_7);
		TextView sensitive6 = (TextView) layout1.findViewById(R.id.sensitive_6);
		TextView sensitive5 = (TextView) layout1.findViewById(R.id.sensitive_5);
		TextView sensitive4 = (TextView) layout1.findViewById(R.id.sensitive_4);
		TextView sensitive3 = (TextView) layout1.findViewById(R.id.sensitive_3);
		TextView sensitive2 = (TextView) layout1.findViewById(R.id.sensitive_2);
		TextView sensitive1 = (TextView) layout1.findViewById(R.id.sensitive_1);
		sensitive10.setOnClickListener(this);
		sensitive9.setOnClickListener(this);
		sensitive8.setOnClickListener(this);
		sensitive7.setOnClickListener(this);
		sensitive6.setOnClickListener(this);
		sensitive5.setOnClickListener(this);
		sensitive4.setOnClickListener(this);
		sensitive3.setOnClickListener(this);
		sensitive2.setOnClickListener(this);
		sensitive1.setOnClickListener(this);
		sensitivePopWindow = new PopupWindow(layout1, 160,
				WindowManager.LayoutParams.WRAP_CONTENT);
		sensitivePopWindow.showAsDropDown(alerm_img_sensitive_drop, -130, 10);
	}

	private void initAudioPopupWindow() {
		LinearLayout audiotriggerLayout = (LinearLayout) LayoutInflater.from(
				this).inflate(R.layout.alermaudiopopwindow, null);
		TextView senHight = (TextView) audiotriggerLayout
				.findViewById(R.id.trigger_audio_levelhigh);
		TextView senMiddle = (TextView) audiotriggerLayout
				.findViewById(R.id.trigger_audio_levelmiddle);
		TextView senLow = (TextView) audiotriggerLayout
				.findViewById(R.id.trigger_audio_levellow);
		TextView senForbid = (TextView) audiotriggerLayout
				.findViewById(R.id.trigger_audio_levelforbid);
		senHight.setOnClickListener(this);
		senLow.setOnClickListener(this);
		senMiddle.setOnClickListener(this);
		senForbid.setOnClickListener(this);
		audioPopWindow = new PopupWindow(audiotriggerLayout, 200,
				WindowManager.LayoutParams.WRAP_CONTENT);
		audioPopWindow.showAsDropDown(alerm_audio_leveldrop, -140, 0);
	}

	private void dismissPopupWindow() {
		if (presteMovePopWindow != null && presteMovePopWindow.isShowing())
			presteMovePopWindow.dismiss();

		if (sensitivePopWindow != null && sensitivePopWindow.isShowing())
			sensitivePopWindow.dismiss();

		if (ioOutLevelPopWindow != null && ioOutLevelPopWindow.isShowing())
			ioOutLevelPopWindow.dismiss();

		if (audioPopWindow != null && audioPopWindow.isShowing())
			audioPopWindow.dismiss();

		if (triggerLevelPopWindow != null && triggerLevelPopWindow.isShowing())
			triggerLevelPopWindow.dismiss();
	}

	@Override
	public void onClick(View arg0) {
		dismissPopupWindow();
		switch (arg0.getId()) {
		// �ƶ����������
		case R.id.alerm_img_sensitive_drop:
			initMovePopupWindow();
			break;
		// ������ƽ
		case R.id.alerm_img_leveldrop:
			initInputPopupWindow();
			break;
		// ������
		case R.id.alerm_audio_leveldrop:
			initAudioPopupWindow();
			break;
		// ����ʱԤλ������
		case R.id.alerm_img_preset_drop:
			initPresetPopupWindow();
			break;
		// IO����
		case R.id.alerm_img_ioout_level_drop:
			initIOlinkMovePopupWindow();
			break;
		// ������ƽѡ��
		case R.id.ioout_hight:
			alerm_tv_ioout_level_value.setText(getResources().getString(
					R.string.alerm_ioin_levelhight));
			alarmModel.setIoout_level(1);
			break;
		case R.id.ioout_low:
			alerm_tv_ioout_level_value.setText(getResources().getString(
					R.string.alerm_ioin_levellow));
			alarmModel.setIoout_level(0);
			break;
		// ������ѡ��
		case R.id.trigger_audio_levelhigh:
			alarmModel.setAlarm_audio(1);
			alerm_audio_triggerlevel.setText(getResources().getString(
					R.string.alerm_audio_levelhigh));
			break;

		case R.id.trigger_audio_levelmiddle:
			alerm_audio_triggerlevel.setText(getResources().getString(
					R.string.alerm_audio_levelmiddle));
			alarmModel.setAlarm_audio(2);
			break;

		case R.id.trigger_audio_levellow:
			alerm_audio_triggerlevel.setText(getResources().getString(
					R.string.alerm_audio_levellow));
			alarmModel.setAlarm_audio(3);
			break;

		case R.id.trigger_audio_levelforbid:
			alerm_audio_triggerlevel.setText(getResources().getString(
					R.string.alerm_audio_levelforbid));
			alarmModel.setAlarm_audio(0);
			break;
		// ����
		case R.id.sensitive_10:
			sensitivePopWindow.dismiss();
			alarmModel.setMotion_sensitivity(10);
			alerm_tv_sensitivity.setText(String.valueOf(10));
			break;

		case R.id.sensitive_9:
			sensitivePopWindow.dismiss();
			alarmModel.setMotion_sensitivity(9);
			alerm_tv_sensitivity.setText(String.valueOf(9));
			break;

		case R.id.sensitive_8:
			sensitivePopWindow.dismiss();
			alarmModel.setMotion_sensitivity(8);
			alerm_tv_sensitivity.setText(String.valueOf(8));
			break;

		case R.id.sensitive_7:
			sensitivePopWindow.dismiss();
			alarmModel.setMotion_sensitivity(7);
			alerm_tv_sensitivity.setText(String.valueOf(7));
			break;

		case R.id.sensitive_6:
			sensitivePopWindow.dismiss();
			alarmModel.setMotion_sensitivity(6);
			alerm_tv_sensitivity.setText(String.valueOf(6));
			break;
		case R.id.sensitive_5:
			sensitivePopWindow.dismiss();
			alarmModel.setMotion_sensitivity(5);
			alerm_tv_sensitivity.setText(String.valueOf(5));
			break;
		case R.id.sensitive_4:
			sensitivePopWindow.dismiss();
			alarmModel.setMotion_sensitivity(4);
			alerm_tv_sensitivity.setText(String.valueOf(4));
			break;
		case R.id.sensitive_3:
			sensitivePopWindow.dismiss();
			alarmModel.setMotion_sensitivity(3);
			alerm_tv_sensitivity.setText(String.valueOf(3));
			break;
		case R.id.sensitive_2:
			sensitivePopWindow.dismiss();
			alarmModel.setMotion_sensitivity(2);
			alerm_tv_sensitivity.setText(String.valueOf(2));
			break;
		case R.id.sensitive_1:
			sensitivePopWindow.dismiss();
			alarmModel.setMotion_sensitivity(1);
			alerm_tv_sensitivity.setText(String.valueOf(1));
			break;
		case R.id.preset_no:
			alarmModel.setAlermpresetsit(0);
			alerm_tv_preset.setText(getResources().getString(
					R.string.alerm_preset_no));
			break;
		case R.id.preset_1:
			alarmModel.setAlermpresetsit(1);
			alerm_tv_preset.setText("1");
			break;
		case R.id.preset_2:
			alarmModel.setAlermpresetsit(2);
			alerm_tv_preset.setText("2");
			break;
		case R.id.preset_3:
			alarmModel.setAlermpresetsit(3);
			alerm_tv_preset.setText("3");
			break;
		case R.id.preset_4:
			alarmModel.setAlermpresetsit(4);
			alerm_tv_preset.setText("4");
			break;
		case R.id.preset_5:
			alarmModel.setAlermpresetsit(5);
			alerm_tv_preset.setText("5");
			break;
		case R.id.preset_6:
			alarmModel.setAlermpresetsit(6);
			alerm_tv_preset.setText("6");
			break;
		case R.id.preset_7:
			alarmModel.setAlermpresetsit(7);
			alerm_tv_preset.setText("7");
			break;
		case R.id.preset_8:
			alarmModel.setAlermpresetsit(8);
			alerm_tv_preset.setText("8");
			break;
		case R.id.preset_9:
			alarmModel.setAlermpresetsit(9);
			alerm_tv_preset.setText("9");
			break;
		case R.id.preset_10:
			alarmModel.setAlermpresetsit(10);
			alerm_tv_preset.setText("10");
			break;
		case R.id.preset_11:
			alarmModel.setAlermpresetsit(11);
			alerm_tv_preset.setText("11");
			break;
		case R.id.preset_12:
			alarmModel.setAlermpresetsit(12);
			alerm_tv_preset.setText("12");
			break;
		case R.id.preset_13:
			alarmModel.setAlermpresetsit(13);
			alerm_tv_preset.setText("13");
			break;
		case R.id.preset_14:
			alarmModel.setAlermpresetsit(14);
			alerm_tv_preset.setText("14");
			break;
		case R.id.preset_15:
			alarmModel.setAlermpresetsit(15);
			alerm_tv_preset.setText("15");
			break;
		case R.id.preset_16:
			alarmModel.setAlermpresetsit(16);
			alerm_tv_preset.setText("16");
			break;
		case R.id.fun_item:
			function();
		case R.id.back_item:
			this.finish();
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		dismissPopupWindow();
		switch (arg0.getId()) {
		// �ƶ���Ⲽ��
		case R.id.alerm_cbx_move_layout:
			if (isChecked) {
				alarmModel.setMotion_armed(1);
				alerm_moveview.setVisibility(View.VISIBLE);
			} else {
				alarmModel.setMotion_armed(0);
				alerm_moveview.setVisibility(View.GONE);
			}
			break;
		// PIR��������
		case R.id.alerm_cbx_pir_layout:
			if (isChecked)
				alarmModel.setPirenable(1);
			else
				alarmModel.setPirenable(0);
			break;
		// �������벼��
		case R.id.alerm_cbx_io_layout:
			if (isChecked) {
				// if (device.getDeviceModel().getProductsCode() ==
				// ContantsModel.DeviceType.SMART_CAMERA) {
				// alerm_ioview.setVisibility(View.GONE);
				// } else {
				// alerm_ioview.setVisibility(View.VISIBLE);
				// }
				alarmModel.setInput_armed(1);
			} else {
				alarmModel.setInput_armed(0);

			}
			break;
		// ������Ⲽ��
		case R.id.alerm_cbx_audio_layout:
			if (isChecked) {
				alarmModel.setAudioArmedCheck(1);
				alerm_audio_level.setVisibility(View.VISIBLE);
			} else {
				alarmModel.setAudioArmedCheck(0);
				alerm_audio_level.setVisibility(View.GONE);
			}
			break;
		// IO����
		case R.id.alerm_cbx_io_move:
			if (isChecked) {
				alarmModel.setIolinkage(1);
				alerm_io_move_view.setVisibility(View.VISIBLE);
			} else {
				alarmModel.setIolinkage(0);
				alerm_io_move_view.setVisibility(View.GONE);
			}
			break;
		// �����ʼ�֪ͨ
		case R.id.alerm_cbx_mail:
			if (isChecked)
				alarmModel.setMail(1);
			else
				alarmModel.setMail(0);
			break;
		// ����¼��
		case R.id.alerm_cbx_isrecord:
			if (isChecked)
				alarmModel.setRecord(1);
			else
				alarmModel.setRecord(0);
			break;
		// �������ϴ�ͼƬ
		case R.id.alerm_cbx_upload_picture:
			if (isChecked) {
				alarmModel.setSnapshot(1);
				alerm_uploadpicture_view.setVisibility(View.VISIBLE);
			} else {
				alarmModel.setSnapshot(0);
				alerm_uploadpicture_view.setVisibility(View.GONE);
			}
			break;
		case R.id.alerm_cbx_smart_record_layout:

			break;
		}
		// ��ʾ���������¼�
		if (alarmModel.getMotion_armed() == 1
				|| alarmModel.getInput_armed() == 1
				|| alarmModel.getAudioArmedCheck() == 1
				|| alarmModel.getPirenable() == 1) {
			alerm_eventview.setVisibility(View.VISIBLE);
		} else {
			alerm_eventview.setVisibility(View.GONE);
		}

	}

	@Override
	protected void function() {
		if (!isFun)
			return;
		if (!alerm_cbx_upload_picture.isChecked()) {
			alarmModel.setUpload_interval(0);
		} else {
			String num = alerm_edit_picture_interval.getText().toString()
					.trim();
			if (!TextUtils.isEmpty(num)) {
				int result = Integer.parseInt(num);
				if (result <= 10) {
					alarmModel.setUpload_interval(result);
				} else {
					showToast(getResources().getString(
							R.string.alerm_uploadinterval_toolong));
					return;
				}
			} else {
				alarmModel.setUpload_interval(0);
			}
		}

		alarmModel.setMotion_sensitivity(Integer.parseInt(alerm_tv_sensitivity
				.getText().toString().trim()));
		try {
			JSONObject obj = new JSONObject();
			obj.put("motion_armed", alarmModel.getMotion_armed());
			obj.put("motion_sensitivity", alarmModel.getMotion_sensitivity());
			obj.put("pirenable", alarmModel.getPirenable());
			obj.put("input_armed", alarmModel.getInput_armed());
			obj.put("ioin_level", alarmModel.getIoin_level());
			obj.put("iolinkage", alarmModel.getIolinkage());
			obj.put("iolinkage_level", alarmModel.getIoout_level());
			obj.put("alarmpresetsit", alarmModel.getAlermpresetsit());
			obj.put("mail", alarmModel.getMail());
			obj.put("snapshot", alarmModel.getSnapshot());
			obj.put("record", alarmModel.getRecord());
			obj.put("upload_interval", alarmModel.getUpload_interval());

			if (alerm_cbx_audio_layout.isChecked())
				obj.put("alarm_audio", alarmModel.getAlarm_audio());
			else
				obj.put("alarm_audio", 0);

			obj.put("alarm_temp", alarmModel.getAlarm_temp());
			obj.put("schedule_enable", alarmModel.getSchedule_enable());
			obj.put("schedule_sun_0", -1);
			obj.put("schedule_sun_1", -1);
			obj.put("schedule_sun_2", -1);
			obj.put("schedule_mon_0", -1);
			obj.put("schedule_mon_1", -1);
			obj.put("schedule_mon_2", -1);
			obj.put("schedule_tue_0", -1);
			obj.put("schedule_tue_1", -1);
			obj.put("schedule_tue_2", -1);
			obj.put("schedule_wed_0", -1);
			obj.put("schedule_wed_1", -1);
			obj.put("schedule_wed_2", -1);
			obj.put("schedule_thu_0", -1);
			obj.put("schedule_thu_1", -1);
			obj.put("schedule_thu_2", -1);
			obj.put("schedule_fri_0", -1);
			obj.put("schedule_fri_1", -1);
			obj.put("schedule_fri_2", -1);
			obj.put("schedule_sat_0", -1);
			obj.put("schedule_sat_1", -1);
			obj.put("schedule_sat_2", -1);
			int Result = DeviceSDK.setDeviceParam(userid, 0x2017,
					obj.toString());
			if (Result > 0) {
				Toast.makeText(SettingAlarmActivity.this, "设置成功.",
						Toast.LENGTH_SHORT).show();
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			showToast(getResources().getString(R.string.alerm_set_failed));
			e.printStackTrace();
		}

	}

	private Handler freshHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				hideProgressDialog();
				// �ƶ���Ⲽ��
				if (alarmModel.getMotion_armed() == 0) {
					alerm_moveview.setVisibility(View.GONE);
					alerm_cbx_move_layout.setChecked(false);
				} else {
					alerm_cbx_move_layout.setChecked(true);
					alerm_moveview.setVisibility(View.VISIBLE);
				}
				alerm_tv_sensitivity.setText(String.valueOf(alarmModel
						.getMotion_sensitivity()));

//				 if (device.getDeviceModel().getProductsCode() ==
//				 ContantsModel.DeviceType.SMART_CAMERA) {
//				 // �������벼��
//				 if (alarmModel.getInput_armed() == 0) {
//				 alerm_cbx_io_layout.setChecked(false);
//				 } else {
//				 alerm_cbx_io_layout.setChecked(true);
//				 }
//				 alerm_ioview.setVisibility(View.GONE);
//				 } else {
				 // �������벼��
				 if (alarmModel.getInput_armed() == 0) {
				 alerm_ioview.setVisibility(View.GONE);
//				 alerm_cbx_io_layout.setChecked(false);
				 } else {
				 alerm_cbx_io_layout.setChecked(true);
//				 alerm_ioview.setVisibility(View.VISIBLE);
				 }

//				 }

				if (alarmModel.getIoin_level() == 0) {
					alerm_tv_triggerlevel.setText(getResources().getString(
							R.string.alerm_ioin_levellow));
				} else {
					alerm_tv_triggerlevel.setText(getResources().getString(
							R.string.alerm_ioin_levelhight));
				}

				// PIR��������
				if (alarmModel.getPirenable() == 1) {
					alerm_cbx_pir_layout.setChecked(true);
				} else {
					alerm_cbx_pir_layout.setChecked(false);
				}
				// ������Ⲽ��
				if (alarmModel.getAlarm_audio() == 0) {
					alerm_audio_level.setVisibility(View.GONE);
					alerm_audio_triggerlevel.setText(getResources().getString(
							R.string.alerm_audio_levelforbid));
					alerm_cbx_audio_layout.setChecked(false);
				} else {
					alerm_cbx_audio_layout.setChecked(true);
					alerm_audio_level.setVisibility(View.VISIBLE);
					switch (alarmModel.getAlarm_audio()) {
					case 1:
						alerm_audio_triggerlevel.setText(getResources()
								.getString(R.string.alerm_audio_levelhigh));
						break;
					case 2:
						alerm_audio_triggerlevel.setText(getResources()
								.getString(R.string.alerm_audio_levelmiddle));
						break;
					case 3:
						alerm_audio_triggerlevel.setText(getResources()
								.getString(R.string.alerm_audio_levellow));
						break;
					default:
						break;
					}
				}
				// IO����
				if (alarmModel.getIolinkage() == 0) {
					alerm_io_move_view.setVisibility(View.GONE);
					alerm_cbx_io_move.setChecked(false);
				} else {
					alerm_cbx_io_move.setChecked(true);
					alerm_io_move_view.setVisibility(View.VISIBLE);
				}
				if (0 == alarmModel.getIoout_level()) {
					alerm_tv_ioout_level_value.setText(getResources()
							.getString(R.string.alerm_ioin_levellow));
				} else {
					alerm_tv_ioout_level_value.setText(getResources()
							.getString(R.string.alerm_ioin_levelhight));
				}
				// Ԥλ��
				if (alarmModel.getAlermpresetsit() == 0)
					alerm_tv_preset.setText(getResources().getString(
							R.string.alerm_preset_no));
				else
					alerm_tv_preset.setText(String.valueOf(alarmModel
							.getAlermpresetsit()));
				// �����ʼ�֪ͨ
				if (alarmModel.getMail() == 0)
					alerm_cbx_mail.setChecked(false);
				else
					alerm_cbx_mail.setChecked(true);
				// ¼�񱨾�
				if (alarmModel.getRecord() == 1)
					alerm_cbx_isrecord.setChecked(true);
				else
					alerm_cbx_isrecord.setChecked(false);
				// �������ϴ�ͼƬ
				if (alarmModel.getUpload_interval() == 0) {
					alerm_cbx_upload_picture.setChecked(false);
					alerm_uploadpicture_view.setVisibility(View.GONE);
				} else {
					alerm_uploadpicture_view.setVisibility(View.VISIBLE);
					alerm_cbx_upload_picture.setChecked(true);
				}
				if (alarmModel.getUpload_interval() > 0) {
					alerm_edit_picture_interval.setText(String
							.valueOf(alarmModel.getUpload_interval()));
				}
				// ���������¼�
				if (alarmModel.getMotion_armed() == 1
						|| alarmModel.getInput_armed() == 1
						|| alarmModel.getAudioArmedCheck() == 1
						|| alarmModel.getPirenable() == 1) {
					alerm_eventview.setVisibility(View.VISIBLE);
				} else {
					alerm_eventview.setVisibility(View.GONE);
				}
			} else {
				if (msg.arg1 == 0) {
					showToast(getResources().getString(
							R.string.alerm_set_failed));
				} else {
					// showToast(getResources().getString(
					// R.string.setting_aler_sucess));
					finish();
				}
			}
		}
	};

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		dismissPopupWindow();
		return false;
	}

	@Override
	public void getDevicePrams(long UserID, long nType, String param) {
		// TODO Auto-generated method stub
		if (userid == UserID && nType == 0X2018) {
			try {
				isFun = true;
				JSONObject obj = new JSONObject(param);
				alarmModel.setMotion_armed(obj.getInt("motion_armed"));
				alarmModel.setInput_armed(obj.getInt("input_armed"));
				alarmModel.setIoin_level(obj.getInt("ioin_level"));
				alarmModel.setIolinkage(obj.getInt("iolinkage"));
				alarmModel.setIoout_level(obj.getInt("iolinkage_level"));
				alarmModel.setAlermpresetsit(obj.getInt("alarmpresetsit"));
				alarmModel.setMail(obj.getInt("mail"));
				alarmModel.setSnapshot(obj.getInt("snapshot"));
				alarmModel.setRecord(obj.getInt("record"));
				alarmModel.setUpload_interval(obj.getInt("upload_interval"));
				alarmModel.setAlarm_audio(obj.getInt("alarm_audio"));
				alarmModel.setAlarm_temp(obj.getInt("alarm_temp"));
				alarmModel.setSchedule_enable(obj.getInt("schedule_enable"));
				alarmModel.setPirenable(obj.getInt("pirenable"));
				if (obj.getInt("motion_sensitivity") == 0)
					alarmModel.setMotion_sensitivity(5);
				else
					alarmModel.setMotion_sensitivity(obj
							.getInt("motion_sensitivity"));
				freshHandler.sendEmptyMessage(0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
