package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.DeviceFunction;
import com.zunder.smart.MyApplication;

public class FunctionFactory {
	public static List<DeviceFunction> list = new ArrayList<DeviceFunction>();

	static {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceFunction();
		}
	}

	public static List<DeviceFunction> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceFunction();
		}
		return list;
	}

	public static List<String> getFunction(int typeId, int isShow) {
		List<String> resultList = new ArrayList<String>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceFunction();
		}
		if (isShow == 1) {
			for (DeviceFunction action : list) {
				if (action.getTypeId() == typeId) {
					resultList.add(action.getFunctionName());
				}
			}
		}
		return resultList;
	}

	public static void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase()
				.getDeviceFunction();
	}

	public static List<String> getSequential() {
		List<String> list = new ArrayList<String>();
		list.add(MyApplication.getInstance().getString(R.string.sequential1));
		list.add(MyApplication.getInstance().getString(R.string.sequential2));
		list.add(MyApplication.getInstance().getString(R.string.sequential3));
		list.add(MyApplication.getInstance().getString(R.string.sequential4));
		list.add(MyApplication.getInstance().getString(R.string.sequential5));
		list.add(MyApplication.getInstance().getString(R.string.sequential6));
		list.add(MyApplication.getInstance().getString(R.string.sequential7));
		list.add(MyApplication.getInstance().getString(R.string.sequential8));
		return list;
	}
}
