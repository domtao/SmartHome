package com.zunder.smart.activity.centercontrol.popu;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.zunder.base.RMSBaseView;
import com.zunder.smart.R;
import com.zunder.smart.adapter.GridViewAdapter;
import com.zunder.smart.model.MenuItemData;

@SuppressWarnings("deprecation")
public class ButtonManagePopupWindow {
	private Context mContext;
	private PopupWindow mPopupWindow;
	private View mMenuView;
	private GridView mMenuGrid;
	private GridViewAdapter mGridViewAdapter;
	// private MenuItemData mMenuItemData;
	OnItemListener onItemListener;
	private View mView;

	public ButtonManagePopupWindow(Context mContext) {
		super();
		this.mContext = mContext;
		init();
	}

	public void setmView(View v) {
		this.mView = v;
	}

	private void init() {

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.button_manage_menu, null);
		mMenuGrid = (GridView) mMenuView
				.findViewById(R.id.button_manage_menu_gv);
		mGridViewAdapter = new GridViewAdapter(mContext, getMenuItemData());
		mMenuGrid.setAdapter(mGridViewAdapter);
		mPopupWindow = new PopupWindow(mMenuView, 400,
				LayoutParams.WRAP_CONTENT, true);
		ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(
				R.color.transparent));
		mPopupWindow.setBackgroundDrawable(dw);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Toast);

		mMenuGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				if(onItemListener!=null){
					onItemListener.OnItem(position,mView);
				}
			}
		});
	}

	private MenuItemData getMenuItemData() {
		String[] mMenuArray = mContext.getResources().getStringArray(
				R.array.button_manage_array);
		LevelListDrawable mMenuListDrawables = (LevelListDrawable) mContext
				.getResources().getDrawable(
						R.drawable.button_manage_menu_images);
		return new MenuItemData(mMenuListDrawables, mMenuArray,
				mMenuArray.length);
	}

	public void display(View parent) {
		mGridViewAdapter.refreshData(getMenuItemData());
		mPopupWindow.showAsDropDown(parent,
				Gravity.LEFT | Gravity.TOP,
				-parent.getWidth(),	-parent.getHeight()-mPopupWindow.getHeight());

	}

	public void dismiss() {
		mPopupWindow.dismiss();
	}
	public Boolean isShowing() {
		return mPopupWindow.isShowing();
	}

	public void setOnItemListener(OnItemListener onItemListener) {
		this.onItemListener = onItemListener;
	}

	public interface OnItemListener {
		public void OnItem(int pos,View rmsBaseView);
	}
}
