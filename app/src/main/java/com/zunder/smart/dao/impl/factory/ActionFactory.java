package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.DeviceAction;
import com.zunder.smart.MyApplication;

public class ActionFactory {
	public static List<DeviceAction> list = new ArrayList<DeviceAction>();

	static {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceAction();
		}
	}

	public static List<DeviceAction> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceAction();
		}
		return list;
	}

	public static List<String> getAction(int typeId, int isShow) {
		List<String> resultList = new ArrayList<String>();

		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceAction();
		}
		if (isShow == 1) {
			for (DeviceAction action : list) {
				if (action.getTypeId() == typeId) {
					resultList.add(action.getActionName());
				}
			}
		}
		return resultList;
	}

	public static void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase()
				.getDeviceAction();
	}

	public static List<String> getSequential() {
		List<String> list = new ArrayList<String>();
		list.add(MyApplication.getInstance().getString(R.string.open_1));
		list.add(MyApplication.getInstance().getString(R.string.close_1));
		return list;
	}
}
