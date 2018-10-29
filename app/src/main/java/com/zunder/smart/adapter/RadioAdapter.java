package com.zunder.smart.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zunder.smart.R;
import com.zunder.smart.dialog.RadioCheckAlert;
import com.zunder.smart.model.ItemName;
import com.zunder.smart.dialog.RadioCheckAlert;
import com.zunder.smart.model.ItemName;

import java.util.List;

public class RadioAdapter extends BaseAdapter {

	List<ItemName> list;
	Activity context;
	private String  seletItem;
	private int selecIndex;
	private RadioCheckAlert.OnRadioClickListener onRadioClickListener;
	public RadioAdapter(Activity context, List<ItemName> list, String _seletItem, int _selecIndex, RadioCheckAlert.OnRadioClickListener onRadioClickListener ) {
		this.context = context;
		this.list = list;
		this.seletItem=_seletItem;
		this.selecIndex=_selecIndex;
		this.onRadioClickListener=onRadioClickListener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
			convertView = inflater.inflate(R.layout.item_radio_check, null);
			viewCach.cb = (RadioButton) convertView.findViewById(R.id.item_cb);
			viewCach.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();

		}
		viewCach.text.setText(list.get(position).getItemName());
		if(seletItem!=null){
			if(list.get(position).getItemName().equals(seletItem)){
				viewCach.cb.setChecked(true);
			}else{
				viewCach.cb.setChecked(false);
			}
		}
		if(selecIndex!=-1){
			if(selecIndex==position){
				viewCach.cb.setChecked(true);
			}else{
				viewCach.cb.setChecked(false);
			}
		}

		viewCach.cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RadioButton radioButton=(RadioButton)v;
				if(onRadioClickListener!=null){
					onRadioClickListener.onClick(list.get(position),position,radioButton.isChecked());
					selecIndex=position;
					notifyDataSetChanged();
				}
			}
		});
		return convertView;
	}
	private final class ViewCach {
		TextView text;
		RadioButton cb;
	}

}
