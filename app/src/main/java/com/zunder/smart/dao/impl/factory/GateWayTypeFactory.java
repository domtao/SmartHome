package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.json.Constants;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.GatewayType;
import com.zunder.smart.MyApplication;
import com.zunder.smart.service.GateWayService;

public class GateWayTypeFactory {
	public static List<GatewayType> list = new ArrayList<GatewayType>();
	static {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getGateWayType();
		}
	}

	public static List<GatewayType> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWayType();
		}
		return list;
	}

	public static List<GatewayType> getLimit(int limit) {

		List<GatewayType> resultList = new ArrayList<GatewayType>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWayType();
		}

		if (limit < list.size()) {
			resultList = list.subList(0, limit);
		} else {
			resultList.addAll(list);
		}
		return resultList;
	}

	public static void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase().getGateWayType();
	}
	 static List<String> result=new ArrayList <String>();
	public static List<String> getGatewayNames() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWayType();
		}
		if(result.size()==0) {
			for (int i = 0; i < list.size(); i++) {
				result.add(list.get(i).getGatewayTypeName());
			}
		}
		return result;
	}
    public static String getGatewayName(int typeId) {
        if (list.size() == 0) {
            list = MyApplication.getInstance().getWidgetDataBase().getGateWayType();
        }
        String result="小网关";
        for (int i=0;i<list.size();i++){
            if(list.get(i).getTypeId()==typeId){
            	result=list.get(i).getGatewayTypeName();
            	break;
			}
        }
        return result;
    }


	public static String getGatewayImage(int typeId) {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getGateWayType();
		}
		String result= Constants.GATEWAYMIMAGEPATH;
		for (int i=0;i<list.size();i++){
			if(list.get(i).getTypeId()==typeId){
				result=list.get(i).getGatewayTypeImage();
				break;
			}
		}
		return result;
	}
}
