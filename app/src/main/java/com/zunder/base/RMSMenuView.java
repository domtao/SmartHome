package com.zunder.base;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class RMSMenuView extends RMSBaseView {

	protected List<RMSBaseView> mSubViews=new ArrayList<RMSBaseView>();

	public RMSMenuView(Context context) {
		super(context);
	}

	
	public List<RMSBaseView> getSubViews(){
		return mSubViews;
	}
	
	public void setSubViews(Collection<? extends RMSBaseView> views){
		for(RMSBaseView view:views){
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
		for (RMSBaseView sub : mSubViews) {
			sub.hide();
		}
		
	}
	
	public void displaySubViews() {
		for (RMSBaseView sub : mSubViews) {
			sub.display();
		}
	}

	@Override
	public View copy() {
		return null;
	}

	public abstract Boolean isSub(RMSBaseView v);
}
