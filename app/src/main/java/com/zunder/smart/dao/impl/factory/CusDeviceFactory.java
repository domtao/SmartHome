package com.zunder.smart.dao.impl.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import android.text.TextUtils;

import com.zunder.smart.aiui.info.CusDevice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.json.JSONFileUtils;

import com.zunder.smart.tools.JSONHelper;

public class CusDeviceFactory {
	public static List<CusDevice> list = new ArrayList<CusDevice>();
	private static File jsonFile = new File(Constants.REMOTE_PATH
			+ File.separator + "cusdevice_json");

	static {
		if (list.size() == 0) {
			list = getCusDevice();
		}
	}

	public static List<CusDevice> getCusDevice() {
		List<CusDevice> resultList = new ArrayList<CusDevice>();
		try {
			String json = JSONFileUtils.getJsonStringFromFile(jsonFile)
					.replace("\r\n", "");
			if (!TextUtils.isEmpty(json)) {

				resultList = (List<CusDevice>) JSONHelper.parseCollection(json,
						List.class, CusDevice.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resultList == null) {
			resultList = new ArrayList<CusDevice>();
		}
		return resultList;

	}

	public static int size() {
		if (list.size() == 0) {
			list = getCusDevice();
		}
		return list.size();
	}

	public static CusDevice getDevicesById(int id) {
		CusDevice resultList = null;
		if (list.size() == 0) {
			list = getCusDevice();
		}

		for (CusDevice device : list) {
			if (device.getId() == id) {
				resultList = device;
				break;
			}
		}
		return resultList;
	}

	public static void delete(int id) {
		if (list.size() > 0) {
			for (CusDevice device : list) {
				if (device.getId() == id) {
					list.remove(device);
					saveCusDevice();
					break;
				}
			}
		}
	}

	public static void update(int id, CusDevice cusDevice) {
		if (list.size() > 0) {
			for (CusDevice device : list) {
				if (device.getId() == id) {
					list.remove(device);

					break;
				}
			}
			list.add(cusDevice);

		}
	}

	public static void add(CusDevice device) {
		if (list == null) {
			list = new ArrayList<CusDevice>();
		}
		list.add(device);
	}

	public static int judgeName(String deviceName) {
		int result = 0;
		if (list.size() == 0) {
			list = getCusDevice();
		}
		for (CusDevice device : list) {
			String name = device.getDeviceName();
			if (name.indexOf(deviceName) != -1
					|| deviceName.indexOf(name) != -1) {
				result = 1;
				break;
			}
		}
		return result;
	}

	public static int updateName(int id, String deviceName, String oldName) {
		int result = 0;

		if (list.size() == 0) {
			list = getCusDevice();
		}
		if (deviceName.equals(oldName)) {
			result = 0;
			return result;
		}

		int num = 0;
		for (CusDevice device : list) {
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

	public static void saveCusDevice() {
		try {
			JSONFileUtils.saveJSONToFile(jsonFile, JSONHelper.toJSON(list));
		} catch (Exception e) {
		}
	}
}
