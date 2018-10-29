package com.zunder.smart.dao.impl.factory;

import com.zunder.base.RMCBaseView;
import com.zunder.base.RMCMenuView;
import com.zunder.base.RMSBaseView;
import com.zunder.base.RMSMenuView;
import com.zunder.base.menu.RMCTabView;
import com.zunder.base.menu.RMSTabView;
import com.zunder.cusbutton.RMSCustomButton;
import com.zunder.smart.MyApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RmsTabFactory {
	private volatile static RmsTabFactory install;
	public static RmsTabFactory getInstance() {
		if (null == install) {
			install = new RmsTabFactory();
		}
		return install;
	}

	private List<RMSTabView> tabs;
	private List<RMSBaseView> subViews = new ArrayList<RMSBaseView>();
	private List<RMSMenuView> menus = new ArrayList<RMSMenuView>();
	public List <RMSBaseView> getSubViews() {
		return subViews;
	}
	public void setSubViews(List <RMSBaseView> subViews) {
		this.subViews = subViews;
	}

	public void initViews() {
		tabs =MyApplication.getInstance().getWidgetDataBase().findTabSViews();
		menus.addAll(tabs);
		subViews.addAll(MyApplication.getInstance().getWidgetDataBase().findSubSViews());
		for (RMSMenuView menu : menus) {
			menu.setSubViews(subViews);
		}
	}

	public List<RMSTabView> getTabs() {
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
		for (RMSBaseView base : subViews) {
			if(base instanceof RMSCustomButton) {
				RMSCustomButton rmsCustomButton=(RMSCustomButton)base;
				if (max < (Integer) rmsCustomButton.getId()) {
					max = (Integer) rmsCustomButton.getId();
				}
			}
		}
		return max;
	}


}
