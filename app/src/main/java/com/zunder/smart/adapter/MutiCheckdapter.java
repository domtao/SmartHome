package com.zunder.smart.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.DialogAlert.OnSureListener;
import com.zunder.smart.dialog.MutilCheckAlert;
import com.zunder.smart.model.SmartFile;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.setting.SmartFileUtils;
import com.lidroid.xutils.db.annotation.Check;
import com.zunder.smart.dialog.MutilCheckAlert;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

public class MutiCheckdapter extends BaseAdapter {

	String[] list;
	Activity context;
	boolean[] selected ;
	private MutilCheckAlert.OnMultiChoiceClickListener onMultiChoiceClickListener;
	public MutiCheckdapter(Activity context, String[] list, boolean[] _selected,MutilCheckAlert.OnMultiChoiceClickListener onMultiChoiceClickListener ) {
		this.list = list;
		selected = _selected;
		this.onMultiChoiceClickListener=onMultiChoiceClickListener;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list[position];
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		  ViewCach viewCach = null;
		if (convertView == null) {
			viewCach = new ViewCach();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_mutil_check, null);
			viewCach.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
			viewCach.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();

		}
		viewCach.text.setText(list[position]);
		viewCach.cb.setChecked(selected[position]);
		viewCach.cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox checkBox=(CheckBox)v;
				if(onMultiChoiceClickListener!=null){
					onMultiChoiceClickListener.onClick(position,checkBox.isChecked());
				}
			}
		});
		return convertView;
	}

	public void setOnMultiChoiceClickListener(MutilCheckAlert.OnMultiChoiceClickListener onMultiChoiceClickListener) {
		this.onMultiChoiceClickListener = onMultiChoiceClickListener;
	}

	private final class ViewCach {
		TextView text;
		CheckBox cb;
	}

}
