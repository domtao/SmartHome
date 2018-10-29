package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.model.Room;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.ItemName;
import com.zunder.smart.model.Mode;

public class DeviceFactory {
	private volatile static DeviceFactory install;
	public static DeviceFactory getInstance() {
		if (null == install) {
			install = new DeviceFactory();
		}
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		return install;
	}

	private static  List<Device> list = new ArrayList<Device>();
	public  List<Device> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		return list;
	}

	public  Device getDevice() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		if (list.size() > 0) {
			return list.get(0);
		} else {
			Device device = new Device();
			device.setId(0);
			device.setDeviceNickName("");
			device.setSeqencing(0);
			device.setDeviceOnLine(0);
			device.setDeviceTimer("");
			device.setDeviceOrdered("");
			device.setStartTime("");
			device.setEndTime("00:00");
			device.setSceneId(0);
			device.setDeviceTypeKey(0);
			device.setDeviceTypeClick(0);
			device.setDeviceName("");
			device.setDeviceTypeName("");
			device.setProductsName("");
			device.setProductsCode("00");
			device.setDeviceID("000000");
			device.setDeviceImage(null);
			device.setDeviceIO("0");
			device.setStartTime("00:00");
			device.setCreationTime("");
			device.setEndTime("00:00");
			device.setGradingId(0);
			device.setRoomId(0);
			device.setDeviceTypeKey(0);
			device.setProductsKey(0);
			device.setProductsIO(0);
			device.setActionShow(0);
			device.setFunctionShow(0);
			device.setActionPramShow(0);
			device.setFunctionParamShow(0);
			return device;
		}
	}

	public  String getCount() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}

		return (list.size()-1)  + "/" + (list.size()-1);

	}

	public  Device getDevicesByName(String deviceName) {
		Device resultList = null;
		if (list.size() > 0) {
			for (Device device : list) {
				if (device.getDeviceName().equals(deviceName)) {
					resultList = device;
					break;
				}
			}
		}
		return resultList;
	}
	public  int  getDevicesContainsName(String deviceName) {
		int result=0;
		if (list.size() > 0) {
			for (Device device : list) {
				if (deviceName.contains(device.getDeviceName())) {
					result=device.getId();
					break;
				}
			}
		}
		return result;
	}

	public  List<Device> getDevices(int roomId,int isShow) {
		List<Device> resultList = new ArrayList<Device>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		for (Device device : list) {
			if (device.getRoomId() == roomId) {
				if(isShow!=-1) {
					if(device.getIsShow()==isShow) {
						resultList.add(device);
					}
				}else{
					resultList.add(device);
				}
			}
		}
		return resultList;
	}
	public  List<String> getDeviceNames(int roomId) {
		List<String> resultList = new ArrayList<String>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		for (Device device : list) {
			if (device.getRoomId() == roomId) {
				resultList.add(device.getDeviceName());
			}
		}
		return resultList;
	}
	public  List<String> getCur() {
		List<String> resultList = new ArrayList<String>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		for (Device device : list) {
			if (device.getDeviceTypeKey()==2) {
				resultList.add(device.getRoomName()+device.getDeviceName());
			}
		}
		return resultList;
	}

	public  Device getDevicesById(int id) {
		Device resultList = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}

		for (Device device : list) {
			if (device.getId() == id) {
				resultList = device;
				break;
			}
		}
		return resultList;
	}


	public  Device getDevicesByName(String roomName,String deviceName) {
		Device resultList = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}

		for (Device device : list) {
			if (device.getRoomName().equals(roomName)&&device.getDeviceName().equals(deviceName)) {
				resultList = device;
				break;
			}
		}
		return resultList;
	}
	public  Device getDevicesById(int id,String deviceCode) {
		Device resultList = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}

		for (Device device : list) {
			if (device.getId() == id&&device.getProductsCode().equals(deviceCode)) {
				resultList = device;
				break;
			}
		}
		return resultList;
	}
	public  void delete(int id) {
		if (list.size() > 0) {
			for (Device device : list) {
				if (device.getId() == id) {
					list.remove(device);
					break;
				}
			}
		}
	}

	public  void add(Device device) {
		if (list == null) {
			list = new ArrayList<Device>();
		}
		list.add(device);
	}
	public  String judgeName(String deviceName,int roomId) {
		String result = "0";

		List<Device> romList=getDevices(roomId,-1);
		for (Device device : romList) {
			String name = device.getDeviceName();
			if (name.indexOf(deviceName) != -1
					|| deviceName.indexOf(name) != -1) {
				String	arceName = device.getRoomName();
				result = "[" + arceName + "]" + MyApplication.getInstance().getString(R.string.exist1) + "[" + name + "]";
				break;

			}
		}
		return result;
	}
	public  String judgeName(String deviceName) {
		String result = "0";
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		for (Device device : list) {
			String name = device.getDeviceName();
			if (name.indexOf(deviceName) != -1
					|| deviceName.indexOf(name) != -1) {
				Room room= RoomFactory.getInstance().getArceById(device.getRoomId());
				if(room!=null) {
					String	arceName = room.getRoomName();
					result = "[" + arceName + "]" + MyApplication.getInstance().getString(R.string.exist1) + "[" + name + "]";
					break;
				}
			}
		}
		return result;
	}

	public  String getDeviceName(int Id) {
		String result = "";
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		if (Id != 0) {
			for (Device device : list) {
				if (device.getId() == Id) {
					result = device.getDeviceName();

					break;
				}
			}
		}
		return result;
	}

	public  int updateName(int id, String deviceName, String oldName,int roomId) {
		int result = 0;

		List<Device> list=getDevices(roomId,-1);
//		if (deviceName.equals(oldName)) {
//			result = 0;
//			return result;
//		}

		int num = 0;
		for (Device device : list) {
			String name = device.getDeviceName();
			if (name.indexOf(deviceName) != -1
					|| deviceName.indexOf(name) != -1) {
				if (device.getId() == id) {
					result = 0;
				} else {
					num++;
				}
				if (num > 0) {
					result = 1;
					break;
				}
			}
		}
		return result;
	}

	public  List<Device> getDeviceByAction(int roomId, int deviceTypeKey,
										   int pastControl,int isShow) {
		List<Device> resultList = new ArrayList<Device>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		if (roomId > 0 && deviceTypeKey > 0) {
			for (Device device : list) {
				if (device.getRoomId() == roomId
						&& device.getDeviceTypeKey() == deviceTypeKey&&device.getIsShow()==isShow) {
					resultList.add(device);
				}
			}

		} else if (roomId > 0 && deviceTypeKey <= 0) {
			for (Device device : list) {
				if (device.getRoomId() == roomId&&device.getIsShow()==isShow) {
					resultList.add(device);
				}
			}
		} else if (roomId <= 0 && deviceTypeKey > 0) {
			for (Device device : list) {
				if (device.getDeviceTypeKey() == deviceTypeKey&&device.getIsShow()==isShow) {
					resultList.add(device);
				}
			}
		} else if (roomId <= 0 && deviceTypeKey <= 0) {
			for (Device device : list) {
				if(pastControl==1){
					if ((pastControl==0&&device.getRoomId()==0)) {
						continue;
					}
					resultList.add(device);
				}
				else{
					if(device.getIsShow()==isShow){
						resultList.add(device);
					}
				}
			}
		}


		return resultList;
	}
	public  List<Device> getSafeDevice(int safeType) {
		List<Device> resultList = new ArrayList<Device>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		if(safeType==0){
			for (Device device : list) {
				if (device.getDeviceTypeKey() == 18&&Integer.parseInt(device.getDeviceIO())<28&&device.getIsShow()==0) {
					resultList.add(device);
				}
			}
		}else{
			for (Device device : list) {
				if (device.getDeviceTypeKey() == 18&&Integer.parseInt(device.getDeviceIO())>=28&&device.getIsShow()==0) {
					resultList.add(device);
				}
			}
		}

		return resultList;
	}
	public  List<Device> getDeviceByTypeKey(int deviceTypeKey) {
		List<Device> resultList = new ArrayList<Device>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}

		for (Device device : list) {
			if (device.getDeviceTypeKey()==deviceTypeKey&&device.getIsShow()==1) {
				resultList.add(device);
			}
		}
		return resultList;
	}
	public  List<Device> getDeviceById(String deviceID, int product_id) {
		List<Device> resultList = new ArrayList<Device>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		for (Device device : list) {
			if (device.getDeviceID().equals(deviceID)
					&& device.getProductsKey() == product_id) {
				resultList.add(device);
			}
		}
		return resultList;
	}
	public  List<Device> getByDeviceId(String deviceID, int deviceTypeKey) {
		List<Device> resultList = new ArrayList<Device>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}

		for (Device device : list) {
			if (device.getDeviceID().equals(deviceID)
					&& device.getDeviceTypeKey() == deviceTypeKey) {
				resultList.add(device);
			}
		}

		return resultList;
	}

	public  Device getDeviceBykey(int product_key) {
		Device resultList = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}

		for (Device device : list) {
			if (device.getProductsKey() == product_key) {
				resultList = device;
				break;
			}
		}

		return resultList;
	}

	public  Device getModeDevice() {
		Device device = null;
		for (Device device2 : list) {
			if (device2.getRoomId() == 0) {
				device = device2;
				break;
			}
		}
		return device;
	}

	public  Device getDeviceID(String deviceID) {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		Device device = null;
		for (Device device2 : list) {
			if (device2.getDeviceID().equals(deviceID)&&device2.getProductsCode().equals("07")) {
				device = device2;
				break;
			}
		}
		return device;
	}
	public  Device getDeviceID(String deviceID,String deviceCode) {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		Device device = null;
		for (Device device2 : list) {
			if (device2.getDeviceID().equals(deviceID)&&deviceCode.equals(device2.getProductsCode())) {
				device = device2;
				break;
			}
		}
		return device;
	}
	public  Device getDeviceByID(String deviceID,String deviceCode,String deviceIO) {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		Device device = null;
		for (Device device2 : list) {
			if (device2.getDeviceID().equals(deviceID)&&deviceCode.equals(device2.getProductsCode())&&device2.getDeviceIO().equals(deviceIO)) {
				device=device2;
				break;
			}
		}
		return device;
	}
	public  List<String> getDeviceName() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		List<String> resultList = new ArrayList<String>();
		for (Device device2 : list) {
			if(!device2.getDeviceName().equals("快捷控制")) {
				resultList.add(device2.getRoomName()+device2.getDeviceName());
			}
		}
		return resultList;
	}

	public  List<String> getRmcDeviceName(int roomId) {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		List<String> resultList = new ArrayList<String>();
		for (Device device2 : list) {
			if(device2.getRoomId()==roomId) {
				resultList.add(device2.getDeviceName());
			}
		}
		return resultList;
	}

	public  List<String> getCusDeviceName() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		List<String> resultList = new ArrayList<String>();
		for (Device device2 : list) {
			if(!device2.getDeviceName().equals("快捷控制")) {
				resultList.add(device2.getDeviceName());
			}
		}
		return resultList;
	}

	public  List<ItemName> getDeviceByName() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		List<ItemName> resultList = new ArrayList<ItemName>();
		List<Mode>	modes=ModeFactory.getInstance().getAll();
		for (int i = 0; i < modes.size(); i++) {
			Mode mode=modes.get(i);
			ItemName itemName=new ItemName();
			itemName.setId(-1);
			itemName.setItemName(mode.getModeName());
			resultList.add(itemName);
		}

		for (Device device2 : list) {
			if(!device2.getDeviceName().equals("快捷控制")) {
				ItemName itemName=new ItemName();
				itemName.setId(device2.getId());
				itemName.setItemName(device2.getDeviceName());
				resultList.add(itemName);
			}
		}
		return resultList;
	}
	public  List<String> getDeviceAnHiongName() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getDevices();
		}
		List<String> resultList = new ArrayList<String>();
		for (Device device2 : list) {
			if(device2.getDeviceTypeKey()<18) {
				resultList.add(device2.getDeviceName());
			}
		}
		return resultList;
	}
	public  void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase().getDevices();
	}

	public  void addFirstDevice() {
		Device device = new Device();
		device.setId(1);
		device.setDeviceNickName("000000");
		device.setSeqencing(0);
		device.setDeviceOnLine(0);
		device.setDeviceTimer("0");
		device.setDeviceOrdered("0");
		device.setStartTime("00:00");
		device.setEndTime("00:00");
		device.setSceneId(0);
		device.setDeviceName("灯光全开");
		device.setDeviceID("000000");
		device.setDeviceImage(null);
		device.setDeviceIO("0");
		device.setCreationTime("000000");
		device.setGradingId(0);
		device.setRoomId(0);
		device.setProductsCode("00");
		device.setDeviceTypeKey(20);
		device.setProductsKey(93);
		MyApplication.getInstance().getWidgetDataBase().insertDevice(device);
	}
	Device addDevice = null;

	public  Device getAddDevice() {
			addDevice = new Device();
			addDevice.setId(0);
			addDevice.setProductsCode("00");
			addDevice.setDeviceName("快捷控制");
			addDevice.setDeviceTypeName("快捷控制");
			Resources res = MainActivity.getInstance().getResources();
			addDevice.setDeviceNickName("");
			addDevice.setDeviceOnLine(0);
			addDevice.setDeviceTimer("");
			addDevice.setDeviceOrdered("");
			addDevice.setStartTime("");
			addDevice.setEndTime("00:00");
			addDevice.setSceneId(0);
			addDevice.setDeviceTypeKey(20);
			addDevice.setDeviceTypeClick(0);
			addDevice.setProductsName("快捷控制");
			addDevice.setDeviceID("000000");
			addDevice.setDeviceIO("0");
			addDevice.setStartTime("00:00");
			addDevice.setCreationTime("");
			addDevice.setEndTime("00:00");
			addDevice.setProductsCode("00");
			addDevice.setGradingId(0);
			addDevice.setRoomId(0);
			addDevice.setDeviceTypeKey(20);
			addDevice.setProductsKey(93);
			addDevice.setProductsIO(0);
			addDevice.setActionShow(1);
			addDevice.setFunctionShow(1);
			addDevice.setActionPramShow(0);
			addDevice.setFunctionParamShow(0);
		return addDevice;
	}

	private Device gateWayDevice;

	public Device getGateWayDevice() {
		if(gateWayDevice==null){
			this.gateWayDevice = new Device();
			this.gateWayDevice.setDeviceBackCode("");
			this.gateWayDevice.setDeviceID("");
			this.gateWayDevice.setProductsCode("00");
			this.gateWayDevice.setDeviceIO("0");
			this.gateWayDevice.setProductsCode("00");
			this.gateWayDevice.setDeviceName("");
			this.gateWayDevice.setDeviceTypeName("");
			this.gateWayDevice.setDeviceNickName("");
			this.gateWayDevice.setDeviceOnLine(0);
			this.gateWayDevice.setDeviceTimer("");
			this.gateWayDevice.setDeviceOrdered("");
			this.gateWayDevice.setSceneId(0);
			this.gateWayDevice.setDeviceTypeKey(20);
			this.gateWayDevice.setDeviceTypeClick(0);
			this.gateWayDevice.setStartTime("00:00");
			this.gateWayDevice.setCreationTime("");
			this.gateWayDevice.setEndTime("00:00");
			this.gateWayDevice.setGradingId(0);
			this.gateWayDevice.setRoomId(0);
			this.gateWayDevice.setDeviceTypeKey(0);
			this.gateWayDevice.setProductsKey(0);
			this.gateWayDevice.setProductsIO(0);
			this.gateWayDevice.setActionShow(1);
			this.gateWayDevice.setFunctionShow(1);
			this.gateWayDevice.setActionPramShow(0);
			this.gateWayDevice.setFunctionParamShow(0);
		}
		return gateWayDevice;
	}

	public void setGateWayDevice(Device gateWayDevice) {
		this.gateWayDevice = gateWayDevice;
		this.gateWayDevice.setDeviceID("000000");
		this.gateWayDevice.setProductsCode("00");
		this.gateWayDevice.setDeviceIO("0");
		this.gateWayDevice.setProductsCode("00");
		this.gateWayDevice.setDeviceName("");
		this.gateWayDevice.setDeviceTypeName("");
		this.gateWayDevice.setDeviceNickName("");
		this.gateWayDevice.setDeviceOnLine(0);
		this.gateWayDevice.setDeviceTimer("");
		this.gateWayDevice.setDeviceOrdered("");
		this.gateWayDevice.setSceneId(0);
		this.gateWayDevice.setDeviceTypeKey(20);
		this.gateWayDevice.setDeviceTypeClick(0);
		this.gateWayDevice.setStartTime("00:00");
		this.gateWayDevice.setCreationTime("");
		this.gateWayDevice.setEndTime("00:00");
		this.gateWayDevice.setGradingId(0);
		this.gateWayDevice.setRoomId(0);
		this.gateWayDevice.setDeviceTypeKey(0);
		this.gateWayDevice.setProductsKey(0);
		this.gateWayDevice.setProductsIO(0);
		this.gateWayDevice.setActionShow(1);
		this.gateWayDevice.setFunctionShow(1);
		this.gateWayDevice.setActionPramShow(0);
		this.gateWayDevice.setFunctionParamShow(0);
	}

	public Device getNewDevice() {
		return newDevice;
	}

	public void setNewDevice(Device newDevice) {
		this.newDevice = newDevice;
	}

	private Device newDevice;
}
