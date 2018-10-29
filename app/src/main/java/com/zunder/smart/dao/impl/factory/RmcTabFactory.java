package com.zunder.smart.dao.impl.factory;

import com.zunder.base.RMCBaseView;
import com.zunder.base.RMCMenuView;
import com.zunder.base.RMSBaseView;
import com.zunder.base.menu.RMCTabView;
import com.zunder.cusbutton.RMCCustomButton;
import com.zunder.cusbutton.RMSCustomButton;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.DeviceAction;

import java.util.ArrayList;
import java.util.List;

public class RmcTabFactory {
	private volatile static RmcTabFactory install;
	public static RmcTabFactory getInstance() {
		if (null == install) {
			install = new RmcTabFactory();
		}
		return install;
	}

	private List<RMCTabView> tabs;
	private List<RMCBaseView> subViews = new ArrayList<RMCBaseView>();
	private List<RMCMenuView> menus = new ArrayList<RMCMenuView>();
	public List <RMCBaseView> getSubViews() {
		return subViews;
	}
	public void setSubViews(List <RMCBaseView> subViews) {
		this.subViews = subViews;
	}

	public void initViews() {
		tabs =MyApplication.getInstance().getWidgetDataBase().findTabViews();
		menus.addAll(tabs);
		subViews.addAll(MyApplication.getInstance().getWidgetDataBase().findSubViews());
		for (RMCMenuView menu : menus) {
			menu.setSubViews(subViews);
		}
	}

	public List<RMCTabView> getTabs() {
		if(tabs==null){
			subViews.clear();
			menus.clear();
			initViews();
		}
		return tabs;
	}
	public void clear(){
		tabs=null;
		subViews.clear();
		menus.clear();
	}

	public int getMax() {
		int max = 0;
		for (RMCBaseView base : subViews) {
			if(base instanceof RMCCustomButton) {
				RMCCustomButton rmsCustomButton=(RMCCustomButton)base;
				if (max < (Integer) rmsCustomButton.getId()) {
					max = (Integer) rmsCustomButton.getId();
				}
			}
		}
		return max;
	}
	public int isCustom(){
		int result=0;
		if(tabs!=null&&tabs.size()>0){
			for (int i=0;i<tabs.size();i++){
				if(tabs.get(i).getBtnType()==0){
					result=1;
					break;
				}
			}
		}
		return result;
	}
}
