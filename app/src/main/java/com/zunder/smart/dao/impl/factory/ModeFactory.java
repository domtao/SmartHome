package com.zunder.smart.dao.impl.factory;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.zunder.smart.MyApplication;
import com.zunder.smart.json.Constants;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.MyApplication;
import com.zunder.smart.tools.AppTools;

public class ModeFactory {
	private volatile static ModeFactory install;
	public static  ModeFactory getInstance() {
		if (null == install) {
			install = new ModeFactory();
			
		}
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		return install;
	}

	private static   List<Mode> list = new ArrayList<Mode>();

	public  List<Mode> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		return list;
	}
	public  List<String> getModeName() {
		List<String> result=new ArrayList<String>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		for (int i=0;i<list.size();i++){
			result.add(list.get(i).getModeName());
		}
		return result;
	}
	public  Mode getName(int modeCode) {
		Mode result=null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		for (int i=0;i<list.size();i++){
			if(list.get(i).getModeCode()==modeCode){
				result=list.get(i);
				break;
			}
		}
		return result;
	}

	public  Mode getModeById(int id) {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		Mode result = null;
		for (Mode mode : list) {
			if (mode.getId() == id) {
				result = mode;
				break;
			}
		}
		return result;
	}
	
	public  Mode getModeByAnHong(int code,int io) {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		Mode result = null;
		for (Mode mode : list) {
			if (mode.getModeCode()== code&&mode.getModeLoop()==io) {
				result = mode;
				break;
			}
		}
		return result;
	}

	public  Mode getByIDMode(int id) {
		Mode result = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		for (Mode mode : list) {
			if (mode.getId() == id) {
				result = mode;
				break;
			}
		}
		return result;
	}

	public  int  useModeId(int modeCode) {
		int result = 0;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		for (Mode mode : list) {
			if (mode.getModeCode() == modeCode&&mode.getModeType().equals("FF")) {
				result = 1;
				break;
			}
		}
		return result;
	}
	public  int  useIO(int modeCode) {
		int result = 0;
		for (int i=0;i<8;i++){
			if(isExitode(modeCode,i)==1){
				continue;
			}else{
				result=1;
			}
		}
		return result;
	}

	public  int  useModeId(int modeCode,int modeLoop) {
		int result = 0;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		for (Mode mode : list) {
			if (mode.getModeCode() == modeCode&&mode.getModeType().equals("FF")&&mode.getModeLoop()==modeLoop) {
				result = 1;
				break;
			}
		}
		return result;
	}
	public  Mode getModeNyName(String name) {
		Mode mode = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		for (Mode _mode : list) {
			if (_mode.getModeName().equals(name)) {
				mode = _mode;
				break;
			}
		}
		return mode;
	}
	public  void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase().getMode();
	}

	public  int judgeName(String modeName) {
		int result = 0;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		for (Mode mode : list) {
			String name = mode.getModeName();
			if (name.indexOf(modeName) != -1 || modeName.indexOf(name) != -1) {
				result = 1;
				break;
			}
		}
		return result;
	}
	public  int updateName(int id, String deviceName, String oldName) {
		int result = 0;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
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
		return result;
	}

	public  List<Mode> getModesByType(String modeType,int isShow) {

		List<Mode> resultList = new ArrayList<Mode>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}

		for (int i=0;i<list.size();i++){
			Mode mode=list.get(i);
			if(mode.getModeType().equals(modeType)&&mode.getModeCode()<200){
				if(isShow==-1){
					resultList.add(mode);
				}else{
					if(mode.getIsShow()==isShow) {
						resultList.add(mode);
					}
				}

			}
		}
		return resultList;
	}
	public  List<Mode> getModesByType(String modeType,String name) {

		List<Mode> resultList = new ArrayList<Mode>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		for (int i=0;i<list.size();i++){
			if(list.get(i).getModeType().equals(modeType)&&list.get(i).getModeCode()<200){
				if(list.get(i).getModeName().contains(name)) {
					resultList.add(list.get(i));
				}
			}
		}
		return resultList;
	}

	public int isExitode(int modeCode,int modeLoop){
		int result=0;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		for (int i=0;i<list.size();i++){
			Mode mode=list.get(i);
			if(mode.getModeCode()==modeCode&&mode.getModeLoop()==modeLoop&&mode.getModeType().equals("FF")){
				result=1;
				break;
			}
		}
		return result;
	}
	public Mode getMode(int modeCode,int modeLoop){
		Mode mode=null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getMode();
		}
		for (int i=0;i<list.size();i++){

			if(list.get(i).getModeCode()==modeCode&&list.get(i).getModeLoop()==modeLoop){
				mode=list.get(i);
				break;
			}
		}
		return mode;
	}

	public int AddOpenModeWarm(int modeLoop){

			Mode mode=new Mode();
			mode.setModeName(251+"_"+modeLoop+"开启报警情景");
			mode.setModeImage(Constants.MODEIMAGEPATH);
			mode.setModeType("FF");
			mode.setSeqencing(0);
			mode.setStartTime("00:00");
			mode.setEndTime("00:00");
			mode.setModeLoop(modeLoop);
			mode.setModeCode(251);
			mode.setModeNickName("");
			return  MyApplication.getInstance().getWidgetDataBase().insertMode(mode);
	}
	public int  AddCloseModeWarm(int modeLoop) {
		Mode mode=new Mode();
		mode.setModeName(250+"_"+modeLoop+"关闭报警情景");
		mode.setModeImage(Constants.MODEIMAGEPATH);
		mode.setModeType("FF");
		mode.setSeqencing(0);
		mode.setStartTime("00:00");
		mode.setEndTime("00:00");
		mode.setModeLoop(modeLoop);
		mode.setModeCode(250);
		mode.setModeNickName("");
		return  MyApplication.getInstance().getWidgetDataBase().insertMode(mode);
	}
	public int AddOpenMode(int modeCode) {
		Mode mode=new Mode();
		mode.setModeName((modeCode+200)+"_0"+"开启联动情景");
		mode.setModeImage(Constants.MODEIMAGEPATH);
		mode.setModeType("FF");
		mode.setSeqencing(0);
		mode.setStartTime("00:00");
		mode.setEndTime("00:00");
		mode.setModeLoop(0);
		mode.setModeCode(modeCode+200);
		mode.setModeNickName("");
		return  MyApplication.getInstance().getWidgetDataBase().insertMode(mode);
	}
	public int AddCloseNode(int modeCode) {
		Mode mode = new Mode();
		mode.setModeName((modeCode+200)+"_7"+"关闭联动情景");
		mode.setModeImage(Constants.MODEIMAGEPATH);
		mode.setModeType("FF");
		mode.setSeqencing(0);
		mode.setStartTime("00:00");
		mode.setEndTime("00:00");
		mode.setModeLoop(7);
		mode.setModeCode(modeCode + 200);
		mode.setModeNickName("");
		return  MyApplication.getInstance().getWidgetDataBase().insertMode(mode);
	}

}
