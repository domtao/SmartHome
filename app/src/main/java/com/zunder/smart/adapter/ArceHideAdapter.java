package com.zunder.smart.adapter;

import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dialog.DialogAlert;

import com.zunder.smart.model.Room;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ArceHideAdapter extends BaseAdapter {

	List<Room> list;
	int mSelect = -1;
	Activity context;

	public ArceHideAdapter(Activity context, List<Room> list) {
		this.list = list;
		this.context = context;

	}

	public void changeSelected(int edite) {
		mSelect = edite;
		notifyDataSetChanged();
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
			convertView = inflater.inflate(R.layout.item_arce, null);
			viewCach.but_text = (TextView) convertView.findViewById(R.id.text);

			viewCach.del = (ImageView) convertView.findViewById(R.id.delArce);

			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();

		}

		viewCach.del.setVisibility(View.GONE);

		viewCach.but_text.setText(list.get(position).getRoomName());
		viewCach.del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogAlert alert = new DialogAlert(context);
				alert.init(list.get(position).getRoomName(),
						context.getString(R.string.isDelArce));
				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						int result = MyApplication.getInstance()
								.getWidgetDataBase()
								.deleteRoom(list.get(position).getId());
						if (result > 0) {
							Toast.makeText(context,
									context.getString(R.string.deleteSuccess),
									Toast.LENGTH_SHORT).show();
							list.remove(position);
							notifyDataSetChanged();
						}
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert.show();

			}
		});
		return convertView;
	}

	private final class ViewCach {

		TextView but_text;
		ImageView del;
	}

}
