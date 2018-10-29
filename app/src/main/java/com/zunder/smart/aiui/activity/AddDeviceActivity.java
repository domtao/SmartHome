package com.zunder.smart.aiui.activity;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dao.impl.factory.CloundDeviceFactory;
import com.zunder.smart.dao.impl.factory.CloundModeFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.JSONHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AddDeviceActivity extends Activity implements OnClickListener {
	public static void startActivity(Activity activity, String _modelOpration,
			Device _device) {
		modelOpration = _modelOpration;
		device=_device;
		Intent intent = new Intent(activity, AddDeviceActivity.class);
		activity.startActivityForResult(intent, 100);
	}

	private TextView backTxt;
	private TextView editeTxt;
	private static RelativeLayout roomLayout,typeLayout,productLayout;
	private static TextView roomTxt,typeTxt,productTxt,ioTxt,timeTxt;
	static RelativeLayout  ioLayout;
	private static String modelOpration = "Add";
	int Id = 0;
	private String OldName="";
	static  Device device;
	private Activity activity;
	private EditText deviceId, deviceName, nickName;

	private int sendId = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cus_device_add);
		activity = this;
		deviceName = (EditText) findViewById(R.id.deviceName);
		deviceId = (EditText) findViewById(R.id.deviceID);
		nickName = (EditText) findViewById(R.id.nickName);
		backTxt = (TextView) findViewById(R.id.backTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		timeTxt=(TextView)findViewById(R.id.timeTxt);
		roomTxt=(TextView)findViewById(R.id.roomTxt);
		typeTxt=(TextView)findViewById(R.id.typeTxt);
		roomLayout = (RelativeLayout) findViewById(R.id.roomLayout);
		typeLayout = (RelativeLayout) findViewById(R.id.typeLayout);
		ioTxt=(TextView)findViewById(R.id.ioTxt) ;
		productTxt = (TextView) findViewById(R.id.productTxt);
		productLayout = (RelativeLayout) findViewById(R.id.productLayout);
		ioLayout = (RelativeLayout) findViewById(R.id.ioLayout);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);

		setData();
	}

	public void setData() {
		if (device != null) {
			Id = device.getId();
			OldName=device.getDeviceName();
			deviceName.setText(device.getDeviceName());
			deviceId.setText(device.getDeviceID());
			deviceName.setText(device.getDeviceName());
			deviceId.setText(device.getDeviceID());
			timeTxt.setText(device.getStartTime()+"~"+device.getEndTime());
			nickName.setText(device.getDeviceNickName());
			nickName.setText(device.getDeviceNickName());
			roomTxt.setText(device.getRoomName());
			typeTxt.setText(device.getDeviceTypeName());
			productTxt.setText(device.getProductsName());
			if (device.getDeviceTypeClick()>0&&device.getDeviceTypeKey()!=-1) {
				productLayout.setVisibility(View.VISIBLE);
			} else {
				productLayout.setVisibility(View.GONE);
			}
			if (device.getProductsKey() != -1 && device.getProductsIO() > 0) {
				ioLayout.setVisibility(View.VISIBLE);
				ioTxt.setText("回路"+(Integer.parseInt(device.getDeviceIO())+1));
			} else {
				ioLayout.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", "Add");
				resultIntent.putExtras(bundle);
				this.setResult(100, resultIntent);
				finish();
				break;
			case R.id.editeTxt:
				if (TextUtils.isEmpty(deviceName.getText())) {
					return;
				}
				if (modelOpration.equals("Add")) {
					Device _device =new Device();
					_device=device;
					if (CloundDeviceFactory.judgeName(deviceName.getText()
							.toString()) == 0 && CloundModeFactory.updateName(_device.getId(),deviceName.getText()
							.toString(), OldName) == 0) {
						CloundDeviceFactory.list.add(_device);
						String result = ISocketCode
								.setAddDevice(JSONHelper.toJSON(_device),
										AiuiMainActivity.deviceID);
						MainActivity.getInstance().sendCode(result);
						ToastUtils.ShowSuccess(activity, getString(R.string.addSuccess),
								Toast.LENGTH_SHORT, true);
					} else {
						_device.setDeviceName(deviceName.getText().toString());
						ToastUtils.ShowError(activity,
								deviceName.getText().toString()
										+ getString(R.string.exist),
								Toast.LENGTH_SHORT, true);
						}
				}else if (modelOpration.equals("Edite")) {

				if (CloundDeviceFactory.updateName(device.getId(),deviceName.getText()
						.toString(), OldName) == 0&& CloundModeFactory.updateName(device.getId(),deviceName.getText()
						.toString(), OldName)==0) {
					device.setDeviceName(deviceName.getText().toString());
					device.setOldName(OldName);
					CloundDeviceFactory.update(OldName, device);
					String result = ISocketCode.setUpdateDevice(
							JSONHelper.toJSON(device),
							AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(result);
					ToastUtils.ShowSuccess(activity, getString(R.string.updateSuccess),
							Toast.LENGTH_SHORT,true);
				} else {
					ToastUtils.ShowError(activity, getString(R.string.namebeing), Toast.LENGTH_SHORT,true);
				}
			}
			break;
		default:
			break;
		}
	}

}
