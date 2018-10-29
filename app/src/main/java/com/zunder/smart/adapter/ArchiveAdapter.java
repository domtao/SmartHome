package com.zunder.smart.adapter;

import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.DialogAlert.OnSureListener;
import com.zunder.smart.model.Archive;
import com.zunder.smart.MyApplication;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.model.Archive;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ArchiveAdapter extends BaseAdapter {

	List<Archive> list;
	int mSelect = -1;
	Activity context;

	public ArchiveAdapter(Activity context, List<Archive> list) {
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
			convertView = inflater.inflate(R.layout.item_file, null);
			viewCach.but_text = (TextView) convertView.findViewById(R.id.text);
			viewCach.img = (ImageView) convertView.findViewById(R.id.img);
			viewCach.edite = (ImageView) convertView
					.findViewById(R.id.editeMode);
			viewCach.del = (ImageView) convertView.findViewById(R.id.delMode);

			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();

		}

		if (mSelect == position) {
			// gridview_item_select emotion_bg
			convertView.setBackgroundResource(R.drawable.emotion_bg);
		} else {
			convertView.setBackgroundResource(0); //
		}
		viewCach.but_text.setText(list.get(position).getProjectName());
		Bitmap bitmap = list.get(position).getProjectImage();
		if (bitmap == null) {
			viewCach.img.setImageResource(R.mipmap.icon_cj_mr);
		} else {
			viewCach.img.setImageBitmap(bitmap);
		}
		if (mSelect != -1 && position != list.size() - 1) {
			viewCach.del.setVisibility(View.VISIBLE);
			viewCach.edite.setVisibility(View.VISIBLE);
		} else {
			viewCach.del.setVisibility(View.GONE);
			viewCach.edite.setVisibility(View.GONE);
		}

		viewCach.del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogAlert alert = new DialogAlert(context);
				alert.init(list.get(position).getProjectName(),
						context.getString(R.string.isDelMode));
				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						int result = MyApplication.getInstance()
								.getWidgetDataBase()
								.deleteMode(list.get(position).getId());
						if (result > 0) {
							Toast.makeText(context,
									context.getString(R.string.deleteSuccess),
									Toast.LENGTH_SHORT).show();
							list.remove(position);
							// HomeFragment.isEdite = 1;
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
		viewCach.edite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		return convertView;
	}

	private final class ViewCach {

		TextView but_text;
		ImageView img;
		ImageView edite;
		ImageView del;
	}

}
