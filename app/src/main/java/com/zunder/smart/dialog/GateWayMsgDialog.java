package com.zunder.smart.dialog;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.adapter.GateWayMsgAdapter;
import com.zunder.smart.model.GateWayMsg;
import com.zunder.smart.model.GateWayMsg;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
//import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import android.widget.LinearLayout.LayoutParams;

public class GateWayMsgDialog extends Dialog {
	private Activity mContext;
	List<GateWayMsg> list;
	ListView listView;
	GateWayMsgAdapter adapter;
	CameraInterFace cameraInterFace;

	public GateWayMsgDialog(Activity context, List<GateWayMsg> list) {
		super(context);
		this.mContext = context;
		this.list = list;
		this.setTitle(context.getString(R.string.gateWayFind));
		setContentView(R.layout.gateway_dialog);
		findView();
	}

	private void findView() {

		listView = (ListView) findViewById(R.id.cameraMsgList);
		adapter = new GateWayMsgAdapter(mContext, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (cameraInterFace != null) {
					cameraInterFace.setDid(list.get(position).getDeviceID(),
							list.get(position).getProductsCode());
					dismiss();
				}
			}
		});

	}

	public void setCameraInterFace(CameraInterFace cameraInterFace) {
		this.cameraInterFace = cameraInterFace;
	}

	public interface CameraInterFace {
		public void setDid(String cameraDid, int cameraType);

	}

}
