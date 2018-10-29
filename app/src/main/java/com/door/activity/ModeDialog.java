package com.door.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.zunder.smart.R;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;

import java.util.List;

@SuppressLint("NewApi")
public class ModeDialog {
	private Activity activity;
	ModeDialogAdapter adapter;
	OnItemListen itemListen;

	public void setItemListen(OnItemListen itemListen) {
		this.itemListen = itemListen;
	}

	public ModeDialog(Activity activity, List<Mode> list) {
		super();
		this.activity = activity;
		adapter = new ModeDialogAdapter(activity,list);
	}

	public void show() {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity,
				R.style.MyDialog);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (itemListen != null) {
					itemListen.OnItemClick(((Mode)adapter.getItem(which)).getModeName());
				}

			}
		}).show();
	}

	public interface OnItemListen {
		public void OnItemClick(String params);
	}
}
