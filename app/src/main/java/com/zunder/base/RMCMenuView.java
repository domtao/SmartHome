package com.zunder.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;

public abstract class RMCMenuView extends RMCBaseView {

	protected List<RMCBaseView> mSubViews=new ArrayList<RMCBaseView>();

	public RMCMenuView(Context context) {
		super(context);
	}

	
	public List<RMCBaseView> getSubViews(){
		return mSubViews;
	}
	
	public void setSubViews(Collection<? extends RMCBaseView> views){
		for(RMCBaseView view:views){
			if(isSub(view)){
				mSubViews.add(view);
			}
		}
	}

	private Boolean isSelected = false;

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public void hideSubViews() {
		for (RMCBaseView sub : mSubViews) {
			sub.hide();
		}
		
	}
	
	public void displaySubViews() {
		for (RMCBaseView sub : mSubViews) {
			sub.display();
		}
	}


	public abstract Boolean isSub(RMCBaseView v);
}
