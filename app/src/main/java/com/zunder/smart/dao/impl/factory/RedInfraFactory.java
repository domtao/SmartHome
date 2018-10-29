package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;
import com.zunder.smart.MyApplication;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.MyApplication;

public class RedInfraFactory {
	public static List<RedInfra> list = new ArrayList<RedInfra>();

	private volatile static RedInfraFactory install;
	public static  RedInfraFactory getInstance() {
		if (null == install) {
			install = new RedInfraFactory();

		}
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getInfrareds();
		}
		return install;
	}

	public  List<RedInfra> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getInfrareds();
		}
		return list;
	}
	public  List<RedInfra> getLimit(int limit) {
		List<RedInfra> resultList = new ArrayList<RedInfra>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getInfrareds();
		}
		if (limit < list.size()) {
			resultList = list.subList(0, limit);
		} else {
			resultList.addAll(list);
		}
		return resultList;
	}
	public  List<RedInfra> getInfraById(int id) {

		List<RedInfra> resultList = new ArrayList<RedInfra>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getInfrareds();
		}
		for (RedInfra infrared : list) {
			if (infrared.getFatherId() == id) {
				resultList.add(infrared);
			}
		}
		return resultList;
	}
	public  List<String> getInfraNames(int fatherId) {
		List<String> resultList = new ArrayList<String>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getInfrareds();
		}
		for (RedInfra infrared : list) {
			if (infrared.getFatherId() == fatherId) {
				resultList.add(infrared.getInfraredName());
			}
		}
		return resultList;
	}
	public  RedInfra getInfraByIndex(int id) {
		RedInfra resultList = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getInfrareds();
		}
		for (RedInfra infrared : list) {
			if (infrared.getInfraredIndex() == id) {
				resultList = infrared;
				break;
			}
		}
		return resultList;
	}
	public  int getInfraKey(String deviceID) {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getInfrareds();
		}
		int result = -1;
		for (int i = 1; i < 64; i++) {
			if (infraKey(deviceID, i)) {
				result = i;
				break;
			}
		}
		return result;
	}
	public  boolean infraKey(String deviceID, int num) {
		boolean flag = true;
		for (RedInfra infrared : list) {
			if (infrared.getDeviceId().equals(deviceID)
					&& infrared.getInfraredKey() == num) {
				flag = false;
			}
		}
		return flag;
	}
	public  RedInfra getInfra(int id) {

		RedInfra resultList = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getInfrareds();
		}
		for (RedInfra infrared : list) {
			if (infrared.getId() == id) {
				resultList = infrared;
				break;
			}
		}

		return resultList;
	}

	public  RedInfra getInfraKey(int id) {

		RedInfra resultList = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getInfrareds();
		}
		for (RedInfra infrared : list) {
			if (infrared.getInfraredKey() == id) {
				resultList = infrared;
				break;
			}
		}

		return resultList;
	}
	public  int isExite(int infrad_key, String deviceID) {
		int result = 0;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getInfrareds();
		}
		for (RedInfra infrared : list) {
			if (infrared.getInfraredKey() == infrad_key
					&& infrared.getDeviceId().equals(deviceID)) {
				result = 1;
				break;
			}
		}
		return result;
	}
	public  void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase().getInfrareds();
	}
	public  int judgeName(String deviceName,int FatherId) {
		int result = 0;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getInfrareds();
		}
		for (RedInfra infrared : list) {
			String name = infrared.getInfraredName();
			if(name!=null) {
				if(infrared.getFatherId() == FatherId) {
					if (name.indexOf(deviceName) != -1
							|| deviceName.indexOf(name) != -1) {
						result = 1;
						break;
					}
				}
			}
		}
		return result;
	}
	public   int updateName(int id, String deviceName, String oldName,int infrared_index) {
		int result = 0;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getInfrareds();
		}
		if (deviceName.equals(oldName)) {
			result = 0;
			return result;
		}
		int num = 0;
		for (RedInfra infrared : list) {
			String name = infrared.getInfraredName();
			if(name!=null) {
				if(infrared.getInfraredIndex() == infrared_index) {
					if (name.indexOf(deviceName) != -1
							|| deviceName.indexOf(name) != -1) {
						if (infrared.getId() == id) {
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
		}
		return result;
	}
}
