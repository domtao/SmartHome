/**
 * 
 */
package com.zunder.smart.gateway;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import org.json.JSONException;
import org.json.JSONObject;

import com.zunder.smart.R;
import com.zunder.smart.adapter.GateWayAdapter;
import com.zunder.smart.gateway.bean.SdCardModel;
import com.zunder.smart.listener.DevicePramsListener;
import com.zunder.smart.service.BridgeService;
import com.zunder.smart.adapter.GateWayAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * SD����������ҳ��
 * 
 * @author wang.jingui
 * 
 */
public class SettingSdcardActivity extends BaseActivity implements
		OnClickListener, DevicePramsListener {
	private TextView totalItem;
	private TextView statusItem;
	private CheckBox coverageBox;
	private CheckBox audioBox;
	private CheckBox timeBox;
	private Button formatBtn;
	long userid;
	private SdCardModel sdCardModel = new SdCardModel();

	private int time7, time15, time23;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gateway_sdcard);
		BridgeService.setDevicePramsListener(this);
		initViews();
		showProgressDialog("获取SD卡参数");
	}

	private void initViews() {
		backItem = (TextView) findViewById(R.id.back_item);
		backItem.setText("返回");
		titleItem = (TextView) findViewById(R.id.title_item);
		titleItem.setText("SD卡设置");
		functionItem = (TextView) findViewById(R.id.fun_item);
		functionItem.setOnClickListener(this);
		backItem.setOnClickListener(this);
		totalItem = (TextView) findViewById(R.id.tv_sd_total);
		statusItem = (TextView) findViewById(R.id.tv_state);
		coverageBox = (CheckBox) findViewById(R.id.cbx_coverage);
		audioBox = (CheckBox) findViewById(R.id.cbx_record_audio);
		timeBox = (CheckBox) findViewById(R.id.cbx_record_time);
		formatBtn = (Button) findViewById(R.id.set_sd_format);

		coverageBox.setOnClickListener(this);
		audioBox.setOnClickListener(this);
		timeBox.setOnClickListener(this);
		formatBtn.setOnClickListener(this);
		userid = GateWayAdapter.userid;
		DeviceSDK.getDeviceParam(userid, 0x2021);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.set_sd_format) {
			if (sdCardModel.getSdcard_totalsize() == 0) {
				showToast("没有插入SD卡");
				return;
			}
			int Result = DeviceSDK.setDeviceParam(this.userid, 0x2024, "");
			if (Result > 0) {
				Toast.makeText(SettingSdcardActivity.this, "SD卡格式化成功.",
						Toast.LENGTH_SHORT).show();
				try {
					Thread.sleep(100);
					DeviceSDK.getDeviceParam(userid, 0x2021);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else if (id == R.id.cbx_coverage) {
			if (coverageBox.isChecked())
				sdCardModel.setRecord_cover(1);
			else
				sdCardModel.setRecord_cover(0);
		} else if (id == R.id.cbx_record_audio) {
			if (audioBox.isChecked())
				sdCardModel.setRecord_audio(1);
			else
				sdCardModel.setRecord_audio(0);
		} else if (id == R.id.cbx_record_time) {
			if (timeBox.isChecked())
				sdCardModel.setTime_schedule_enable(1);
			else
				sdCardModel.setTime_schedule_enable(0);
		} else if (id == R.id.fun_item) {
			function();
		} else if (id == R.id.back_item) {
			this.finish();
		}

	}

	@Override
	protected void function() {
		if (sdCardModel.getTime_schedule_enable() == 1) {
			time7 = -1;
			time15 = -1;
			time23 = -1;
		} else {
			time7 = 0;
			time15 = 0;
			time23 = 0;
		}
		JSONObject obj = new JSONObject();
		try {
			obj.put("record_cover", sdCardModel.getRecord_cover());
			obj.put("record_audio", sdCardModel.getRecord_audio());
			obj.put("time_schedule_enable",
					sdCardModel.getTime_schedule_enable());

			obj.put("schedule_sun_0", time7);
			obj.put("schedule_sun_1", time15);
			obj.put("schedule_sun_2", time23);

			obj.put("schedule_mon_0", time7);
			obj.put("schedule_mon_1", time15);
			obj.put("schedule_mon_2", time23);

			obj.put("schedule_tue_0", time7);
			obj.put("schedule_tue_1", time15);
			obj.put("schedule_tue_2", time23);

			obj.put("schedule_wed_0", time7);
			obj.put("schedule_wed_1", time15);
			obj.put("schedule_wed_2", time23);

			obj.put("schedule_thu_0", time7);
			obj.put("schedule_thu_1", time15);
			obj.put("schedule_thu_2", time23);

			obj.put("schedule_fri_0", time7);
			obj.put("schedule_fri_1", time15);
			obj.put("schedule_fri_2", time23);

			obj.put("schedule_sat_0", time7);
			obj.put("schedule_sat_1", time15);
			obj.put("schedule_sat_2", time23);

			int Result = DeviceSDK.setDeviceParam(userid, 0x2022,
					obj.toString());
			if (Result > 0) {
				Toast.makeText(SettingSdcardActivity.this, "SD卡参数设置成功.",
						Toast.LENGTH_SHORT).show();
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			finish();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private Handler freshHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				hideProgressDialog();
				totalItem.setText(sdCardModel.getSdcard_totalsize() + "M");

				if (sdCardModel.getSdcard_status() == 1) {
					// 0 mei 1 yi 2re 3sd�ļ�ϵͳ����4 sd����ʽ����
					statusItem.setText("SD存在");
				} else if (sdCardModel.getSdcard_status() == 2) {
					statusItem.setText("读取中……");
				} else if (sdCardModel.getSdcard_status() == 3) {
					statusItem.setText("文件系统错误");
				} else if (sdCardModel.getSdcard_status() == 4) {
					statusItem.setText("正在格式化SD卡");
				} else {
					statusItem.setText("请插入SD卡");
				}
				if (sdCardModel.getRecord_cover() == 1) {
					coverageBox.setChecked(true);
				} else {
					coverageBox.setChecked(false);
				}

				if (sdCardModel.getRecord_audio() == 1) {
					audioBox.setChecked(true);
				} else {
					audioBox.setChecked(false);
				}

				if (sdCardModel.getTime_schedule_enable() == 1) {
					timeBox.setChecked(true);
				} else {
					timeBox.setChecked(false);
				}
			} else if (msg.what == 1) {
				hideProgressDialog();
				if (msg.arg1 == 1) {
					showToast("SD卡格式化成功");
					finish();
				} else {
					showToast("SD卡格式化失败");
				}
			} else if (msg.what == 2) {
				if (msg.arg1 == 1) {
					showToast("SD卡设置成功");
					finish();
				} else {
					showToast("SD卡设置失败");
				}
			}
		}
	};

	@Override
	public void getDevicePrams(long UserID, long nType, String param) {
		// TODO Auto-generated method stub
		try {
			if (userid == UserID && nType == 0X2021) {
				JSONObject obj = new JSONObject(param);
				sdCardModel.setRecord_cover(obj.getInt("record_cover"));
				sdCardModel.setRecord_audio(obj.getInt("record_audio"));
				sdCardModel.setTime_schedule_enable(obj
						.getInt("time_schedule_enable"));
				sdCardModel.setSdcard_status(obj.getInt("sdcard_status"));
				sdCardModel.setSdcard_totalsize(obj.getInt("sdcard_totalsize"));
				freshHandler.sendEmptyMessage(0);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
