package com.zunder.smart.dao.impl.factory;

import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;

import java.util.ArrayList;
import java.util.List;

public class CloundModeFactory {
	public static List<Mode> list = new ArrayList<Mode>();

	public static int size() {
		return list.size();
	}

	public static void add(Mode mode) {
		boolean flag = true;
		for (Mode _mode : list) {
			if (_mode.getModeName().equals(mode.getModeName())) {
				flag = false;
				break;
			}
		}
		if (flag == true) {
			list.add(mode);
		}
	}
	public static int updateName(int id,String deviceName, String oldName) {
		int result = 0;

		if (list.size() > 0) {
			if (deviceName.equals(oldName)) {
				result = 0;
				return result;
			}
			int num = 0;
			for (Mode device : list) {
				String name = device.getModeName();
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
	public static void update(String oldName, String newName) {
		if (list.size() > 0) {
			for (Mode device : list) {
				if (device.getModeName().equals(oldName)) {
					device.setModeName(newName);
					break;
				}
			}

		}
	}
}
