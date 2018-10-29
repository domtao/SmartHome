package com.zunder.smart.dao.impl.factory;

import com.zunder.smart.MyApplication;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ModeList;

import java.util.ArrayList;
import java.util.List;

public class CloundModeListFactory {
	private volatile static CloundModeListFactory install;
	public static CloundModeListFactory getInstance() {
		if (null == install) {
			install = new CloundModeListFactory();
		}
		return install;
	}
	public  List<ModeList> list = new ArrayList<ModeList>();

	public  int size() {
		return list.size();
	}
	public  List<ModeList> getAll(){
		return list;
	}
	public  void add(ModeList modeList) {
		boolean flag = true;
		for (ModeList _modeList: list) {
			if (_modeList.getId()==modeList.getId()) {
				flag = false;
				break;
			}
		}
		if (flag == true) {
			list.add(modeList);
		}
	}
	public ModeList getModeList(int Id){
		ModeList modeList=null;
		for (ModeList _modeList: list) {
			if (_modeList.getId()==Id) {
				modeList=_modeList;
				break;
			}
		}
		return modeList;
	}
	public void update(ModeList modeList){
		for (ModeList _modeList: list) {
			if (_modeList.getId()==modeList.getId()) {
				_modeList.setModeAction(modeList.getModeAction());
				_modeList.setModeFunction(modeList.getModeFunction());
				_modeList.setModeTime(modeList.getModeTime());
				_modeList.setModeDelayed(modeList.getModeDelayed());
				_modeList.setModePeriod(modeList.getModePeriod());
				_modeList.setBeginMonth(modeList.getBeginMonth());
				_modeList.setEndMonth(modeList.getEndMonth());
				break;
			}
		}
	}

}
