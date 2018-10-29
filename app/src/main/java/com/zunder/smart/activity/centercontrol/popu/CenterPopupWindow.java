package com.zunder.smart.activity.centercontrol.popu;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import com.zunder.smart.R;
import com.zunder.smart.adapter.GridViewAdapter;
import com.zunder.smart.model.MenuItemData;

@SuppressWarnings("deprecation")
public class CenterPopupWindow {
	private Activity mContext;
	private PopupWindow mPopupWindow;
	private View mMenuView;
	private GridView mMenuGrid;
	private GridViewAdapter mGridViewAdapter;
	private OnItemListener onItemListener;
	public CenterPopupWindow(Activity mContext) {
		super();
		this.mContext = mContext;
		init();
	}
	private MenuItemData getMainMenuItemData() {
		String[] mainMenuArray = mContext.getResources().getStringArray(
				R.array.main_menu_array);
		LevelListDrawable mainMenuListDrawables = (LevelListDrawable) mContext
				.getResources().getDrawable(R.drawable.main_menu_images);
		return new MenuItemData(mainMenuListDrawables, mainMenuArray,
				mainMenuArray.length);
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.menu, null);
		mMenuGrid = (GridView) mMenuView.findViewById(R.id.menuGridChange);
		mGridViewAdapter = new GridViewAdapter(mContext, getMainMenuItemData());
		mMenuGrid.setAdapter(mGridViewAdapter);
		mPopupWindow = new PopupWindow(mMenuView, LayoutParams.WRAP_CONTENT,
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
					onItemListener.OnItem(position,"");
				}
				dismiss();
			}
		});

		mMenuGrid.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				switch (keyCode) {
				case KeyEvent.KEYCODE_MENU:
					dismiss();
					break;
				}

				return false;
			}
		});
	}

	public void setmMenuItemData(MenuItemData mMenuItemData) {
		mGridViewAdapter.refreshData(mMenuItemData);
	}

	public void display(View parent) {
		if (isShowing())
			return;
		setmMenuItemData(getMainMenuItemData());
		mPopupWindow.showAsDropDown(parent,
				Gravity.TOP, 0,0);

	}

	public void dismiss() {
		if (isShowing())
			mPopupWindow.dismiss();

	}
	private Boolean isShowing() {
		return mPopupWindow.isShowing();
	}

	public void setOnItemListener(OnItemListener onItemListener) {
		this.onItemListener = onItemListener;
	}

	public interface OnItemListener {
		public void OnItem(int pos,String ItemName);
	}

}
