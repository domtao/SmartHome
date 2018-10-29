package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Mode;
import com.zunder.smart.remote.model.FileType;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.tools.SystemInfo;
import com.zunder.smart.MyApplication;

public class GateWayFactory {
	private volatile static GateWayFactory install;
	public static List<GateWay> list = new ArrayList<GateWay>();

	public static GateWayFactory getInstance() {
		if (null == install) {
			install = new GateWayFactory();
		}
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
		}
		return install;
	}

	public  List<GateWay> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
		}
		isMainGateWay();
		return list;
	}

	public  void isMainGateWay(){
		int result=0;
		if(list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				GateWay gateWay = list.get(i);
				if (gateWay.getIsCurrent() == 1&&gateWay.getTypeId()<=3) {
					result += 1;
					break;
				}
			}
			if (result == 0) {
				for (int i = 0; i < list.size(); i++) {
					GateWay gateWay = list.get(i);
					if(gateWay.getTypeId()==2||(gateWay.getTypeId()>3&&gateWay.getTypeId()<6)) {
						gateWay.setIsCurrent(1);
						MyApplication.getInstance()
								.getWidgetDataBase()
								.updateIsCurrent(gateWay.getId(), 1);
					break;
					}
				}
			}
		}
	}

	public  void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
	}

	public  GateWay getGateWay(String deviceID) {
		GateWay gateWay = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getGatewayID().equals(deviceID)) {
				gateWay = list.get(i);
				break;
			}
		}
		return gateWay;
	}

	public GateWay getGateWayByName(String gateWayName) {
		GateWay gateWay=null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
		}

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getGatewayName().equals(gateWayName)) {
				gateWay = list.get(i);
				break;
			}
		}
		return gateWay;
	}
	public  GateWay getGateWayById(String deviceName) {
		GateWay gateWay = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getGatewayName().equals(deviceName)) {
				gateWay = list.get(i);
				break;
			}
		}
		return gateWay;
	}
	public  String getGateWayByID(String deviceID) {
		String  result = "主网关";
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getGatewayID().equals(deviceID)) {
				result = list.get(i).getGatewayName();
				break;
			}
		}
		return result;
	}
	public  GateWay getGateWayByDeviceId(String deviceId) {
		GateWay gateWay = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getGatewayID().equals(deviceId)) {
				gateWay = list.get(i);
				break;
			}
		}
		return gateWay;
	}

	public  boolean idOpen() {
		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getTypeId() == 4) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	public  List<String> getAllName() {
		List<String> result = new ArrayList<String>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
		}
		for (int i = 0; i < list.size(); i++) {
			GateWay gateWay=list.get(i);
			if (gateWay.getGatewayID().equals(SystemInfo.getMasterID(MyApplication.getInstance()))) {
				continue;
			}
			result.add(gateWay.getGatewayName());
		}
		return result;
	}

	public  List<FileType> getAllByName() {
		List<FileType> result = new ArrayList<FileType>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
		}

		for (int i = 0; i < list.size(); i++) {
			GateWay gateWay=list.get(i);
			FileType fileType=new FileType();
			fileType.setId(gateWay.getId());
			fileType.setTypeName(gateWay.getGatewayName());
			fileType.setTypeText(gateWay.getGatewayID());
			result.add(fileType);
		}
		return result;
	}
	public  List<String> getRemoteCotrol() {
		List<String> result = new ArrayList<String>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
		}
		for (int i = 0; i < list.size(); i++) {
			GateWay gateWay=list.get(i);
			if (gateWay.getTypeId()==3) {
				result.add(gateWay.getGatewayName());
			}

		}
		return result;
	}

	public boolean isAdmin(String gateWayID){
		boolean isFlag=false;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWay();
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getGatewayID().equals(gateWayID)) {
				if(list.get(i).getUserName().equals("admin")){
					isFlag=true;
				}
				break;
			}
		}
		return isFlag;
	}

	public List<String> getGateWay() {
		List<String> resultLlist = new ArrayList<String>();
		List<GateWay> list = GateWayService.list;
		for (int i = 0; i < list.size(); i++) {
			GateWay gateWay = list.get(i);
			if (gateWay.getTypeId()<3) {
				resultLlist.add(gateWay.getGatewayName());
			}
		}
		return resultLlist;
	}
	public int getMainGateWay() {
		int result=0;
		List<GateWay> list = GateWayService.list;
		for (int i = 0; i < list.size(); i++) {
			GateWay gateWay = list.get(i);
			if (gateWay.getTypeId()<3) {
				result++;
			}
		}
		return result;
	}
}
