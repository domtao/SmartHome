package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.model.ActionParam;
import com.zunder.smart.MyApplication;

public class ActionParamFactory {
	public static List<ActionParam> list = new ArrayList<ActionParam>();

	static {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getActionParam();
		}
	}

	public static List<ActionParam> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getActionParam();
		}
		return list;
	}

	public static List<String> getActionParam(int typeId, int isShow) {
		List<String> resultList = new ArrayList<String>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getActionParam();
		}
		if (isShow == 1) {
			for (ActionParam actionParam : list) {
				if (actionParam.getTypeId() == typeId) {
					resultList.add(actionParam.getActionparam());
				}
			}
		}
		return resultList;
	}

	public static void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase().getActionParam();
	}
}
