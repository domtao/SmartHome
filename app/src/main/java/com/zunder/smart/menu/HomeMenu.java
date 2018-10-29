package com.zunder.smart.menu;

import android.app.Activity;
import android.graphics.drawable.LevelListDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zunder.smart.R;
import com.zunder.smart.menu.adapter.RightMenuAdapter;
import com.zunder.smart.menu.model.MenuItem;
import com.zunder.smart.model.ProCase;

import java.util.ArrayList;
import java.util.List;

//actionbar��������
public class HomeMenu {

	private List<ProCase> list;
	private Activity context;
	private PopupWindow popupWindow;

	public OnRightMenuClickListener listener;

	public void setListener(OnRightMenuClickListener listener) {
		this.listener = listener;
	}

	public HomeMenu(Activity context, List<ProCase> list) {
		super();
		this.context = context;
		this.list = list;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		List<MenuItem> items = new ArrayList<MenuItem>();


		for (int i = 0; i < list.size(); i++) {
			MenuItem item = new MenuItem();
			item.setName(list.get(i).getProCaseName());
//			menuDrawables.setLevel(i);
//			item.setDrawable(menuDrawables.getCurrent());
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
