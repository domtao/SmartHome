package com.zunder.base;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

@SuppressWarnings("deprecation")
public abstract class RMSBaseView extends LinearLayout implements Moveable,
		Copyable {
	private int Id;
	private String BtnName;
	private String BtnImage;
	private int RoomId;
	private String CreationTime;
	private int BtnSeqencing;
	private int CodeType=-1;
	private String BtnAction;
	private int BtnX;
	private int BtnY;
	private int BtnWidth;
	private int BtnHeight;
	private int BtnType=-1;
	private int BtnSize=16;
	private String BtnColor="000000";
	protected Context _context;
	private int secondTierButtonTag = -1;
	private int tabViewTag = -1;
	private int tierButtonTag = -1;


	public RMSBaseView(Context context) {
		super(context);
		_context = context;

	}

	@Override
	public int getId() {
		return Id;
	}

	@Override
	public void setId(int id) {
		Id = id;
	}

	public String getBtnName() {
		return BtnName;
	}

	public void setBtnName(String btnName) {
		BtnName = btnName;
	}

	public String getBtnImage() {
		return BtnImage;
	}

	public void setBtnImage(String btnImage) {
		BtnImage = btnImage;
	}

	public int getRoomId() {
		return RoomId;
	}

	public void setRoomId(int roomId) {
		RoomId = roomId;
	}

	public String getCreationTime() {
		return CreationTime;
	}

	public void setCreationTime(String creationTime) {
		CreationTime = creationTime;
	}

	public int getBtnSeqencing() {
		return BtnSeqencing;
	}

	public void setBtnSeqencing(int btnSeqencing) {
		BtnSeqencing = btnSeqencing;
	}

	public int getCodeType() {
		return CodeType;
	}

	public void setCodeType(int codeType) {
		CodeType = codeType;
	}

	public String getBtnAction() {
		if(BtnAction==null||BtnAction=="null"||BtnAction.equals("null")){
			BtnAction="";
		}
		return BtnAction;
	}
	public void setBtnAction(String btnAction) {
		BtnAction = btnAction;
	}

	public int getBtnX() {
		return BtnX;
	}

	public void setBtnX(int btnX) {
		BtnX = btnX;
	}

	public int getBtnY() {
		return BtnY;
	}

	public void setBtnY(int btnY) {
		BtnY = btnY;
	}

	public int getBtnWidth() {
		return BtnWidth;
	}

	public void setBtnWidth(int btnWidth) {
		BtnWidth = btnWidth;
	}

	public int getBtnHeight() {
		return BtnHeight;
	}

	public void setBtnHeight(int btnHeight) {
		BtnHeight = btnHeight;
	}

	public int getBtnType() {
		return BtnType;
	}

	public void setBtnType(int btnType) {
		BtnType = btnType;
	}

	public int getBtnSize() {
		return BtnSize;
	}

	public void setBtnSize(int btnSize) {
		BtnSize = btnSize;
	}

	public String getBtnColor() {
		return BtnColor;
	}

	public void setBtnColor(String btnColor) {
		BtnColor = btnColor;
	}

	public int getSecondTierButtonTag() {
		return secondTierButtonTag;
	}

	public void setSecondTierButtonTag(int secondTierButtonTag) {
		this.secondTierButtonTag = secondTierButtonTag;
	}

	public int getTabViewTag() {
		return tabViewTag;
	}

	public void setTabViewTag(int tabViewTag) {
		this.tabViewTag = tabViewTag;
	}

	public int getTierButtonTag() {
		return tierButtonTag;
	}

	public void setTierButtonTag(int tierButtonTag) {
		this.tierButtonTag = tierButtonTag;
	}




	public void setParams(int w, int h, int x, int y) {
		this.setLayoutParams(new AbsoluteLayout.LayoutParams(w, h, x, y));
	}

	@Override
	public void moveTo(int x, int y) {
		setParams(getWidth(), getHeight() ,  x , y);
		setBtnX(x);
		setBtnY(y);
	}

	public void hide() {
		this.setVisibility(View.INVISIBLE);
	}

	public void display() {
		this.setVisibility(View.VISIBLE);
	}

	@Override
	public String toString() {
		return getResources().getString(getTitleResId()) + "\r\nID: "
				+ getTag() + "\r\n宽:" + getWidth() + "高" + getHeight();
	}

	abstract protected void init();

	abstract protected int getTitleResId();

	public void release() {}

	private int color;
	public void setTextColor(int color) {this.color=color;}
	public int getTextColor(){return color;}

	private Typeface tf;
	public void setTypeface(Typeface tf) {this.tf=tf;}
	public Typeface getTypeface(){return tf;}
}
