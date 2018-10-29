package com.zunder.smart.adapter;

import java.io.File;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.model.SmartFile;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.setting.SmartFileUtils;

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

public class FileAdapter extends BaseAdapter {

	List<SmartFile> list;
	int mSelect = -1;
	Activity context;

	public FileAdapter(Activity context, List<SmartFile> list) {
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
			viewCach.del = (ImageView) convertView.findViewById(R.id.delMode);

			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();

		}

		viewCach.but_text.setText(list.get(position).getFileName());
		Bitmap bitmap = list.get(position).getBitmap();
		if (bitmap == null) {
			viewCach.img.setImageResource(R.mipmap.file_sdr);
		} else {
			viewCach.img.setImageBitmap(bitmap);
		}
		if (mSelect != -1) {
			if (list.get(position).getFileName().equals("Root")) {
				viewCach.del.setVisibility(View.GONE);
			} else if (ProjectUtils.getRootPath().getRootName()
					.equals(list.get(position).getFileName())) {
				viewCach.del.setVisibility(View.VISIBLE);
				viewCach.del.setImageResource(R.mipmap.normal_imag);
			} else {
				viewCach.del.setVisibility(View.VISIBLE);
				viewCach.del.setImageResource(R.mipmap.delete_scen);
			}
		} else {

			viewCach.del.setVisibility(View.GONE);
			if (ProjectUtils.getRootPath().getRootName()
					.equals(list.get(position).getFileName())) {
				viewCach.del.setVisibility(View.VISIBLE);
				viewCach.del.setImageResource(R.mipmap.normal_imag);
			} else {
				viewCach.del.setImageResource(R.mipmap.delete_scen);
			}
		}
		viewCach.del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final String fileName = list.get(position).getFileName();

				DialogAlert alert = new DialogAlert(context);
				alert.init(fileName, context.getString(R.string.isDelFile));
				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						SmartFileUtils.RecursionDeleteFile(new File(
								MyApplication.getInstance().getRootPath()
										+ File.separator + fileName));

						SmartFileUtils.delFile(fileName);
						// list.remove(position);
						notifyDataSetChanged();
						Toast.makeText(context,
								context.getString(R.string.deleteSuccess),
								Toast.LENGTH_SHORT).show();

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
		ImageView img;
		ImageView del;
	}

}
