package com.zunder.smart.menu;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.menu.adapter.RightMenuAdapter;
import com.zunder.smart.menu.model.MenuItem;
import com.zunder.smart.menu.adapter.RightMenuAdapter;
import com.zunder.smart.menu.model.MenuItem;

import android.app.Activity;
import android.graphics.drawable.LevelListDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

//actionbar��������
public class RightMenu {

	private int menuArrayId;
	private int menuDrawablesId;
	private Activity context;
	private PopupWindow popupWindow;

	public OnRightMenuClickListener listener;

	public void setListener(OnRightMenuClickListener listener) {
		this.listener = listener;
	}

	public RightMenu(Activity context, int menuArrayId,
			int menuDrawablesId) {
		super();
		this.context = context;
		this.menuArrayId = menuArrayId;
		this.menuDrawablesId = menuDrawablesId;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		List<MenuItem> items = new ArrayList<MenuItem>();
		String[] menuNames = context.getResources().getStringArray(menuArrayId);
		LevelListDrawable menuDrawables = (LevelListDrawable) context
				.getResources().getDrawable(menuDrawablesId);

		for (int i = 0; i < menuNames.length; i++) {
			MenuItem item = new MenuItem();
			item.setName(menuNames[i]);
			menuDrawables.setLevel(i);
			item.setDrawable(menuDrawables.getCurrent());
			items.add(item);
		}
		RightMenuAdapter adapter = new RightMenuAdapter(context, items);

		View layout = context.getLayoutInflater().inflate(
				R.layout.right_menu_list, null);
		ListView listview = (ListView) layout.findViewById(R.id.listview);

		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onItemClick(position);
				}
			}
		});
		popupWindow = new PopupWindow(layout,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
				R.mipmap.home_add_background));
//		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true); // 设置popupwindow可点击
		popupWindow.setOutsideTouchable(true); // 设置popupwindow外部可点击
		
	}
	public void show(View parent) {
		popupWindow.showAsDropDown(parent, -100, 10);
	}

	public void dismiss() {
		popupWindow.dismiss();
	}

}
