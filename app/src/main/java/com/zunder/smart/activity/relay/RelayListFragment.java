package com.zunder.smart.activity.relay;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.zunder.smart.R;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.adapter.RelayAdapter;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.gridview.DragGridView;
import com.zunder.smart.listener.RelayListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RelayListFragment extends Fragment implements OnClickListener,
		RelayListener,View.OnTouchListener{
	GridView deviceGrid;
	DragGridView relayGrid;
	RelayAdapter reLayAdapter;
	RelayAdapter deviceAdapter;
	List<Device> listRelay = new ArrayList<Device>();
	List<Device> listDevice = new ArrayList<Device>();
	private Activity activity;
	private TextView backTxt;
	private TextView titleTxt;
	private static int ID = 0;
	Device device = null;
	private int iSRandom = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_relay_list, container,
				false);
		activity = getActivity();
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		titleTxt = (TextView) root.findViewById(R.id.titleTxt);
		backTxt.setOnClickListener(this);

		relayGrid = (DragGridView) root.findViewById(R.id.relayGrid);
		relayGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				TipAlert alert = new TipAlert(activity,
						getString(R.string.tip), getString(R.string.is_j)+"["
								+ listRelay.get(position).getDeviceName()
								+ "]"+getString(R.string.fromrelay)+"[" + device.getDeviceName() + "]"+getString(R.string.moving));
				alert.setSureListener(new TipAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						Device _device=listRelay.get(position);
						if(DeviceFactory.getInstance().getGateWayDevice()!=null) {
							_device.setDeviceBackCode(DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
							_device.setCmdDecodeType(DeviceFactory.getInstance().getGateWayDevice().getCmdDecodeType());
							device.setDeviceBackCode(DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
							device.setCmdDecodeType(DeviceFactory.getInstance().getGateWayDevice().getCmdDecodeType());
						}else{
							_device.setDeviceBackCode("");
							_device.setCmdDecodeType(1);
							device.setDeviceBackCode("");
							device.setCmdDecodeType(1);
						}
						SendCMD.getInstance().sendCmd(246, "00_" + iSRandom,_device);
						SendCMD.getInstance().sendCMD(246, "02_" + iSRandom,device);
						listRelay.remove(position);
						reLayAdapter.notifyDataSetChanged();
					}
				});
				alert.show();
			}
		});

		deviceGrid = (GridView) root.findViewById(R.id.deviceGrid);
		deviceGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				TipAlert alert = new TipAlert(activity,
						getString(R.string.tip), getString(R.string.is_j)+"["
								+ listDevice.get(position).getDeviceName()
								+ "]"+getString(R.string.add_relay)+"[" + device.getDeviceName() + "]");
				alert.setSureListener(new TipAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						Device _device=listDevice.get(position);
						if(DeviceFactory.getInstance().getGateWayDevice()!=null) {
							_device.setDeviceBackCode(DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
							_device.setCmdDecodeType(DeviceFactory.getInstance().getGateWayDevice().getCmdDecodeType());
							device.setDeviceBackCode(DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
							device.setCmdDecodeType(DeviceFactory.getInstance().getGateWayDevice().getCmdDecodeType());
						}else{
							_device.setDeviceBackCode("");
							_device.setCmdDecodeType(1);
							device.setDeviceBackCode("");
							device.setCmdDecodeType(1);
						}
						SendCMD.getInstance().sendCmd(246, "01_" + iSRandom,_device);
						SendCMD.getInstance().sendCMD(246, "02_" + iSRandom,device);
					}

				});
				alert.show();
			}

		});
		root.setOnTouchListener(this);
		return root;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				relayGrid.setAdapter(reLayAdapter);
				break;
			}
		}
	};

	public void initAdpapter(int Id) {
		TcpSender.setRelayListListener(this);
		listRelay.clear();
		this.ID=Id;
		device = DeviceFactory.getInstance().getDevicesById(ID);
		if (device != null) {
			titleTxt.setText(device.getDeviceName());
		}
		listDevice = DeviceFactory.getInstance().getDeviceByAction(0, 0, -1,1);
		deviceAdapter = new RelayAdapter(activity, listDevice);
		deviceGrid.setAdapter(deviceAdapter);

		reLayAdapter = new RelayAdapter(activity, listRelay);
		relayGrid.setAdapter(reLayAdapter);
		iSRandom = (new Random().nextInt(255) % 255) + 1;
		if(DeviceFactory.getInstance().getGateWayDevice()!=null) {
			device.setDeviceBackCode(DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
			device.setCmdDecodeType(DeviceFactory.getInstance().getGateWayDevice().getCmdDecodeType());
		}else{
			device.setDeviceBackCode("");
			device.setCmdDecodeType(1);
		}
		SendCMD.getInstance().sendCmd(246, "02_" + iSRandom, device);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {
			TcpSender.setRelayListListener(null);
		}
	}
	public void onHideCode(){
		if (device != null) {
			if(DeviceFactory.getInstance().getGateWayDevice()!=null) {
				device.setDeviceBackCode(DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
				device.setCmdDecodeType(DeviceFactory.getInstance().getGateWayDevice().getCmdDecodeType());
			}else{
				device.setDeviceBackCode("");
				device.setCmdDecodeType(1);
			}
			SendCMD.getInstance().sendCmd(246, "03_" + iSRandom, device);
			}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			TabMyActivity.getInstance().hideFragMent(8);
			onHideCode();
			break;
		default:
			break;
		}

	}

	public void findAllRelay(String string) {
		// *C1E 1C 06 01 4B 10 95 20 04 0004
		//*C001C(Type)(SType)(ID)(MID1)(MID2)(RND)(SID)(SMID1)(SMID2)
		//       6      8     10   12    14    16   18   20    22
		//if (string.substring(16, 18).equals(AppTools.toHex(iSRandom))) {
		if (string.substring(16, 18).equals("FF")) {
			//joe 提示系統忙碌中
		}else{
			//int key = Integer.valueOf(string.substring(20, 24), 16);
			String deviceCode = string.substring(6, 8);
			String deviceID = string.substring(10,16);
			if(device.getDeviceID().equals(deviceID)&&device.getProductsCode().equals(deviceCode)) {
				deviceCode = string.substring(8, 10);
				deviceID = string.substring(18, 24);
				String deviceIO = string.substring(17, 18);
				Device device = DeviceFactory.getInstance().getDeviceByID(deviceID, deviceCode,deviceIO);
				if (device != null) {
					boolean addObj = true;
					if (listRelay.size() > 0) {
						for (Device _list : listRelay) {
							if (_list.getProductsCode()
									.equals(device.getProductsCode())&&_list.getDeviceID()
									.equals(device.getDeviceID()) && _list.getDeviceIO().equals(device.getDeviceIO())) {
								addObj = false;
								break;
							}
						}
					}
					if (addObj) {
						listRelay.add(device);
					}
				}else{
					device=DeviceFactory.getInstance().getAddDevice();
					device.setDeviceID(deviceID);
					device.setProductsCode(deviceCode);
					device.setDeviceName("未知设备"+deviceID+deviceCode);
					listRelay.add(device);
				}
			}
			handler.sendEmptyMessage(1);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}