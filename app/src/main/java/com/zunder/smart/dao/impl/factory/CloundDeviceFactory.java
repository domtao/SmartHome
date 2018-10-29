package com.zunder.smart.dao.impl.factory;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.Device;

public class CloundDeviceFactory {
	public static List<Device> list = new ArrayList<Device>();

	public static int size() {
		return list.size();
	}

	public static Device getDevicesByName(String deviceName) {
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

	public static void delete(int id) {
		if (list.size() > 0) {
			for (Device device : list) {
				if (device.getId() == id) {
					list.remove(device);
					break;
				}
			}
		}
	}

	public static void update(String oldName, Device Device) {
		if (list.size() > 0) {
			for (Device device : list) {
				if (device.getId()==Device.getId()) {
					device.setRoomName(device.getRoomName());
					break;
				}
			}
		}
	}

	public static void add(Device device) {
		boolean flag = true;
		for (Device _device : list) {
			if (_device.getDeviceName().equals(device.getDeviceName())) {
				flag = false;
				break;
			}
		}

		int temp=0;
		for (int i=0;i<list.size();i++) {
			Device _device=list.get(i);
			if(_device.getRoomName().equals(device.getRoomName())){
				if(_device.getDeviceTypeKey()==device.getDeviceTypeKey()) {
					list.add(i, device);
					flag = false;
					break;
				}else{
					temp=i;
				}
			}
		}
		if (flag == true) {
			if(temp==0) {
				list.add(device);
			}else{
				if(temp<list.size()-1) {
					list.add(temp+1, device);
					}else{
					list.add(temp, device);
				}
			}
		}
	}

	public static void sort(List<Device> list) {
		int i, j, small;
		Device temp;
		int n = list.size();
		for (i = 0; i < n - 1; i++) {
			small = i;
			for (j = i + 1; j < n; j++) {
				if (list.get(j).getDeviceTypeKey()< list.get(small)
						.getDeviceTypeKey())
					small = j;
				if (small != i) {
					temp = list.get(i);
					list.set(i, list.get(small));
					list.set(small, temp);
				}
			}
		}
		sortArce(list);
	}
	public static void sortArce(List<Device> list) {
		int i, j, small;
		Device temp;
		int n = list.size();
		for (i = 0; i < n - 1; i++) {
			small = i;
			for (j = i + 1; j < n; j++) {
				if (list.get(j).getRoomId() < list.get(small)
						.getRoomId())
					small = j;
				if (small != i) {
					temp = list.get(i);
					list.set(i, list.get(small));
					list.set(small, temp);
				}
			}
		}
	}
	public static List<Device> getList() {
		List<Device> resultList = new ArrayList<Device>();
		for (Device _device : list) {
			if (_device.getDeviceTypeKey()==6) {
				resultList.add(_device);
			}
		}
		return resultList;
	}

	public static List<Device> getDeviceList() {
		List<Device> resultList = new ArrayList<Device>();
		for (Device _device : list) {
			if (_device.getDeviceTypeKey()!=20) {
				resultList.add(_device);
			}
		}
		return resultList;
	}
	public static List<String> getDeviceNames() {
		List<String> resultList = new ArrayList<String>();
		for (Device _device : list) {
			resultList.add(_device.getDeviceName());
		}
		return resultList;
	}

	public static Device getDevice(String deviceName) {
		Device device=null;
		for (Device _device : list) {
			if(_device.getDeviceName().equals(deviceName)) {
				device=_device;
			}
		}
		return device;
	}
	public static Device getDeviceById(int deviceId) {
		Device device=null;
		for (Device _device : list) {
			if(_device.getId()==deviceId) {
				device=_device;
			}
		}
		return device;
	}
	public static int judgeName(String deviceName) {
		int result = 0;
		if (list.size() > 0) {
			for (Device device : list) {
				String name = device.getDeviceName();
				if (name.indexOf(deviceName) != -1
						|| deviceName.indexOf(name) != -1) {
					result = 1;
					break;
				}
			}
		}
		return result;
	}

	public static int updateName(int id,String deviceName, String oldName) {
		int result = 0;

		if (list.size() > 0) {
			if (deviceName.equals(oldName)) {
				result = 0;
				return result;
			}

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
		}
		return result;
	}
}
