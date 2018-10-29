package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;
import com.zunder.smart.MyApplication;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.MyApplication;

public class ModeListFactory {

	private volatile static ModeListFactory install;
	public static ModeListFactory getInstance() {
		if (null == install) {
			install = new ModeListFactory();
		}
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getModeList();
		}
		return install;
	}
	private static  List<ModeList> list = new ArrayList<ModeList>();


	public  List<ModeList> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getModeList();
		}
		return list;
	}

	public  List<ModeList> getModeDevice(int modeId) {
		List<ModeList> resultList = new ArrayList<ModeList>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getModeList();
		}
		for (ModeList modeList : list) {
			if (modeList.getModeId() == modeId) {
				resultList.add(modeList);
			}

		}
		return resultList;
	}
	public  ModeList getModeList(int Id) {
		ModeList modeList=null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getModeList();
		}
		for (ModeList _modeList : list) {
			if (_modeList.getId() == Id) {
				modeList=_modeList;
				break;
			}

		}
		return modeList;
	}

	public  void delete(int id) {

		for (ModeList modeList : list) {
			if (modeList.getId() == id) {
				list.remove(modeList);
				break;
			}

		}
	}

	public  void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase().getModeList();
	}

}
