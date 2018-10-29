package com.zunder.smart.dialog;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.adapter.InfraNameAdapter;
import com.zunder.smart.adapter.ItemDialogAdapter;
import com.zunder.smart.model.InfraName;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.InfraServiceUtils;

import a.a.a.a.g.l;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
//import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
//import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class AlertDialogAdapter extends Dialog {
	private Activity mContext;
	List<String> list;
	ListView listView;
	ItemDialogAdapter adapter;
	OnItemClick itemClick;
	TextView infraTxt;

	public AlertDialogAdapter(Activity context, String name, List<String> list) {
		super(context, R.style.MyDialog);
		this.mContext = context;
		this.list = list;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_dialog_adapter);
		findView(name);
	}

	private void findView(String name) {
		listView = (ListView) findViewById(R.id.infraList);

		infraTxt = (TextView) findViewById(R.id.infraTxt);
		infraTxt.setText(name);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (itemClick != null) {
					itemClick.setOnItemClick(list.get(position), position);
					dismiss();
				}
			}
		});
		adapter = new ItemDialogAdapter(mContext, list);
		listView.setAdapter(adapter);
	}

	public void setOnItemClick(OnItemClick _itemClick) {
		this.itemClick = _itemClick;
	}

	public interface OnItemClick {
		public void setOnItemClick(String itemName, int position);
	}

}
