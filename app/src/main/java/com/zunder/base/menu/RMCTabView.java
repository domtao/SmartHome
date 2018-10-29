package com.zunder.base.menu;

import android.content.Context;
import android.view.ViewGroup;

import com.zunder.base.DrawableData;
import com.zunder.base.RMCBaseView;
import com.zunder.base.RMCMenuView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;

public class RMCTabView extends RMCMenuView {

	public RMCTabView(Context context) {
		super(context);
	}

	private CharSequence text;
	private float textSize;
	private DrawableData drawableData;
	private Object tag;
	private int Id;
	private int BtnType;
	private ViewGroup layout;

	public int getBtnType() {
		return BtnType;
	}

	public void setBtnType(int btnType) {
		BtnType = btnType;
	}

	public ViewGroup getLayout() {
		return layout;
	}

	public void setLayout(ViewGroup layout) {
		this.layout = layout;
	}

	
	public CharSequence getText() {
		return text;
	}

	public void setText(CharSequence text) {
		this.text = text;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}


	public DrawableData getDrawableData() {
		return drawableData;
	}

	public void setDrawableData(String resources) {
		if(getBtnType()==1){
			drawableData=new DrawableData(MyApplication.getInstance().getDrawabled(R.drawable.tabhostpress1),"");
		}else if(getBtnType()==2) {
			drawableData = new DrawableData(MyApplication.getInstance().getDrawabled(R.drawable.tabhostpress2), "");
		}else if(getBtnType()==3) {
			drawableData = new DrawableData(MyApplication.getInstance().getDrawabled(R.drawable.tabhostpress3), "");
		}else{
			drawableData = new DrawableData(MyApplication.getInstance().getDrawabled(R.drawable.tabhostpress4), "");
		}
	}

	@Override
	public Object getTag() {
		return tag;
	}
	@Override
	public void setTag(Object o) {
		this.tag = o;
	}

	
	@Override
	public void hideSubViews() {
		// TODO Auto-generated method stub
		super.hideSubViews();
	}

	@Override
	public void displaySubViews() {
		// TODO Auto-generated method stub
		super.displaySubViews();
	}
	
	@Override
	public Boolean isSub(RMCBaseView v) {
		// TODO Auto-generated method stub
		Boolean flag=false;
		if(getTag().equals(v.getTabViewTag())){
			flag=true;
		}
		return flag;
	}

	@Override
	protected int getTitleResId() {
		// TODO Auto-generated method stub
		return R.string.tab_view_title;
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getId() {
		return Id;
	}
	@Override
	public void setId(int id) {
		Id = id;
	}
}
