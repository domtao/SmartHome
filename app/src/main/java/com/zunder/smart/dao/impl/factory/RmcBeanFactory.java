package com.zunder.smart.dao.impl.factory;

import com.zunder.cusbutton.RMCBean;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;

import java.util.ArrayList;
import java.util.List;

public class RmcBeanFactory {
	public static List<RMCBean> list = new ArrayList<RMCBean>();

	private volatile static RmcBeanFactory install;
	public static RmcBeanFactory getInstance() {
		if (null == install) {
			install = new RmcBeanFactory();
		}
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().findRMCBeans();
		}
		return install;
	}

	public  List<RMCBean> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().findRMCBeans();
		}
		return list;
	}

	public  void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase()
				.findRMCBeans();
	}

	public  RMCBean getRMCById(int Id) {
		RMCBean resultList =null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().findRMCBeans();
		}
		for (int i=0;i<list.size();i++){
			RMCBean rmcBean=list.get(i);
			if(rmcBean.getId()==Id){
				resultList=rmcBean;
			}
		}
		return resultList;
	}

	public  List<RMCBean> getRMCByRoomId(int roomId) {
		List<RMCBean> resultList = new ArrayList<RMCBean>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().findRMCBeans();
		}
		for (int i=0;i<list.size();i++){
			RMCBean rmcBean=list.get(i);
			if(rmcBean.getRoomId()==roomId){
				resultList.add(rmcBean);
			}
		}
		return resultList;
	}

	public  RMCBean junRMC(int roomId) {

		RMCBean rmcBean=null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().findRMCBeans();
		}
		for (int i=0;i<list.size();i++){
			RMCBean rmcBean1=list.get(i);
			if(rmcBean1.getRoomId()==roomId){
				rmcBean=rmcBean1;
			}
		}
		return rmcBean;
	}


}
