package com.zunder.smart.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.json.Constants;
import com.zunder.smart.model.Products;
import com.zunder.smart.model.SmartFile;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.setting.SmartFileUtils;

import java.io.File;
import java.util.List;

public class ProductsAdapter extends BaseAdapter {

	List<Products> list;
	int mSelect = -1;
	Activity context;

	public ProductsAdapter(Activity context, List<Products> list) {
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
			convertView = inflater.inflate(R.layout.item_product, null);
			viewCach.but_text = (TextView) convertView.findViewById(R.id.text);
			viewCach.img = (SmartImageView) convertView.findViewById(R.id.img);
			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
		}
		viewCach.but_text.setText(list.get(position).getProductsName());
		viewCach.img.setImageUrl(Constants.HTTPS+list.get(position).getProductsImage());
		if (mSelect == position) {
			convertView.setBackgroundResource(R.drawable.emotion_bg);
		} else {
			convertView.setBackgroundResource(0);
		}
		return convertView;
	}

	private final class ViewCach {

		TextView but_text;
		SmartImageView img;
	}

}
