package com.zunder.smart.spinner;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.Products;
import com.zunder.smart.spinner.ArceSpinerAdapter.IOnItemSelectListener;
import com.zunder.smart.model.Products;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

public class ProductSpinerPopWindow extends PopupWindow implements
		OnItemClickListener {

	private Context mContext;
	private ListView mListView;
	private ProductSpinerAdapter mAdapter;
	private ArceSpinerAdapter.IOnItemSelectListener mItemSelectListener;

	public ProductSpinerPopWindow(Context context) {
		super(context);

		mContext = context;
		init();
	}

	public void setItemListener(ArceSpinerAdapter.IOnItemSelectListener listener) {
		mItemSelectListener = listener;
	}

	public void setAdatper(ProductSpinerAdapter adapter) {
		mAdapter = adapter;
		mListView.setAdapter(mAdapter);
	}

	private void init() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.spiner_window_layout, null);
		setContentView(view);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);

		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);

		mListView = (ListView) view.findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
	}

	public <T> void refreshData(List<Products> list, int selIndex) {
		if (list != null && selIndex != -1) {
			if (mAdapter != null) {
				mAdapter.refreshData(list, selIndex);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		dismiss();
		if (mItemSelectListener != null) {
			mItemSelectListener.onItemClick(pos);
		}
	}

}
