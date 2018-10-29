package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.FunctionParam;
import com.zunder.smart.MyApplication;

public class FunctionParamFactory {
	public static List<FunctionParam> list = new ArrayList<FunctionParam>();

	static {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getFunctionParam();
		}
	}

	public static List<FunctionParam> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getFunctionParam();
		}
		return list;
	}

	public static List<String> getFunctionParam(int typeId, int isShow) {
		List<String> resultList = new ArrayList<String>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getFunctionParam();
		}
		if (isShow == 1) {
			for (FunctionParam action : list) {
				if (action.getTypeId() == typeId) {
					resultList.add(action.getFunction_param());
				}
			}
		}
		return resultList;
	}

	public static void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase()
				.getFunctionParam();
	}

	public static List<String> getNormalList() {
		List<String> resultList = new ArrayList<String>();

		for (int i = 0; i <=100; i++) {

			resultList.add(i + "");

		}
		return resultList;
	}
	public static List<String> getTimeList() {
		List<String> resultList = new ArrayList<String>();

		for (int i = 0; i <=100; i++) {

			resultList.add(i + "");

		}
		return resultList;
	}
	public static List<String> getMoth() {
		List<String> resultList = new ArrayList<String>();

		for (int i = 0; i <=12; i++) {
			resultList.add(i + "");

		}
		return resultList;
	}
	public static List<String> getTimeUnit() {
		List<String> resultList = new ArrayList<String>();
		resultList.add(MyApplication.getInstance().getString(R.string.Second));
		resultList.add(MyApplication.getInstance().getString(R.string.Munits));
		resultList.add(MyApplication.getInstance().getString(R.string.Hour));

		return resultList;
	}
}
