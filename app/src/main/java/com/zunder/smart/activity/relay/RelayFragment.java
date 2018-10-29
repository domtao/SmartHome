package com.zunder.smart.activity.relay;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.device.DeviceFragment;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.adapter.RelayAdapter;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.gridview.DragGridView;
import com.zunder.smart.listener.RelayListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;

import java.util.ArrayList;
import java.util.List;

public class RelayFragment extends Fragment implements OnClickListener,
		RelayListener,View.OnTouchListener{

	DragGridView gridView;
	RelayAdapter adapter;
	List<Device> listDevice;
	private Activity activity;
	private TextView backTxt;
	private TextView editeTxt;
	public static int isEdite = 0;
	public boolean editeFla = false;
	private RightMenu rightButtonMenuAlert;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_relay, container,
				false);
		activity = getActivity();
	
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		gridView = (DragGridView) root.findViewById(R.id.relayGrid);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (position == listDevice.size() - 1) {
					TabMyActivity.getInstance().showFragMent(9);
					TabMyActivity.getInstance().relayDeviceFragment.initAdapter();

				} else {
				if(AppTools.isCharNum(listDevice.get(position).getDeviceName())){
					ToastUtils.ShowError(activity,getString(R.string.no_into_relay), Toast.LENGTH_SHORT,true);
				}else{
					TabMyActivity.getInstance().showFragMent(8);
					TabMyActivity.getInstance().relayListFragment.initAdpapter(
							listDevice.get(position).getId());
				}}
			}
		});
		initRightButtonMenuAlert();
		root.setOnTouchListener(this);
		return root;
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			TcpSender.setRelayListener(null);
		}
	}

	private void initRightButtonMenuAlert() {

		rightButtonMenuAlert = new RightMenu(activity, R.array.relay_menus,
				R.drawable.right_relay_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					if (!editeFla) {
						adapter.changeSelected(0, 0);
						editeFla = true;
					} else {
						adapter.changeSelected(-1, 0);
						editeFla = false;
					}
					break;
				case 1:
					DialogAlert alert = new DialogAlert(activity);
					alert.init(getString(R.string.tip),
							getString(R.string.isDelArce));
					alert.setSureListener(new DialogAlert.OnSureListener() {

						@Override
						public void onSure() {
							// TODO Auto-generated method stub
							SendCMD.getInstance().sendCMD(247, "FF", DeviceFactory.getInstance().getGateWayDevice());
							listDevice.clear();
							Device device = new Device();
							device.setDeviceName(getString(R.string.add));
							listDevice.add(device);
							handler.sendEmptyMessage(1);
						}
						@Override
						public void onCancle() {
							// TODO Auto-generated method stub
						}
					});
					alert.show();
					break;
				case 2:
					SendCMD.getInstance().sendCMD(247, "3", DeviceFactory.getInstance().getGateWayDevice());
					break;

				}
				rightButtonMenuAlert.dismiss();
			}
		});

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				editeFla = false;
				adapter = new RelayAdapter(activity, listDevice);
				gridView.setAdapter(adapter);
				adapter.changeSelected(-1, 0);
				break;
			}
		}
	};

	public void initAdpapter(Device deviceParam) {
		TcpSender.setRelayListener(this);
		if (listDevice == null) {
			listDevice = new ArrayList<Device>();
		}
		listDevice.clear();
		Device device = new Device();
		device.setDeviceName(getString(R.string.add));
		listDevice.add(device);
		adapter = new RelayAdapter(activity, listDevice);
		gridView.setAdapter(adapter);
		SendCMD.getInstance().sendCMD(247, "2", deviceParam);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			TabMyActivity.getInstance().hideFragMent(7);
			break;
		case R.id.editeTxt:
			rightButtonMenuAlert.show(editeTxt);
			break;
		default:
			break;
		}
	}

	@Override
	public void findAllRelay(String cmd) {
		// TODO Auto-generated method stub
		Log.e("relay",cmd);
		//*Z00(key1)(key2)(type)(ID)(MID1)(MID2)
		//     4     6     8     10  12    14
		String string = cmd;
		Integer id = Integer.parseInt(string.substring(4, 8), 16);
		String deviceid = string.substring(10, 16);
		String deviceCode = string.substring(8, 10);
		Device device = DeviceFactory.getInstance().getDevicesById(id,deviceCode);
		Device deviceID = DeviceFactory.getInstance().getDeviceID(deviceid,deviceCode);
		if(device!=null){
			Boolean addObj = true;
			if (listDevice.size() > 1) {
				for (Device _device : listDevice) {
					if (_device.getId() == device.getId()) {
						addObj = false;
						break;
					}
				}
			}
			if (addObj) {
				listDevice.add(0, device);
			}
			handler.sendEmptyMessage(1);
		} else if (deviceID != null) {
			Boolean addObj = true;
				if (listDevice.size() > 1) {
					for (Device _device : listDevice) {
						if (_device.getId() == deviceID.getId()) {
							addObj = false;
							break;
						}
					}
				}
				if (addObj) {
					listDevice.add(0, deviceID);
				}
				handler.sendEmptyMessage(1);
		} else {
			device=new Device();
			device.setId(id);
			device.setDeviceName(deviceid);
			device.setDeviceID(deviceid);
			device.setProductsCode(deviceCode);
			device.setDeviceIO("0");
			Boolean addObj = true;
			if (listDevice.size() > 1) {
				for (Device _device : listDevice) {
					if (_device.getId() == device.getId()) {
						addObj = false;
						break;
					}
				}
			}
			if (addObj) {
				listDevice.add(0, device);
			}
			handler.sendEmptyMessage(1);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
