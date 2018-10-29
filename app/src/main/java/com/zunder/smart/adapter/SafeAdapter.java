package com.zunder.smart.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.listener.OnItemEditeListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SafeAdapter extends SwipeMenuAdapter<SafeAdapter.DefaultViewHolder> {
	public static List<Device> list;
	public static int edite = 0;

	int posion = -1;

	static Activity activity;



	@SuppressWarnings("static-access")
	public SafeAdapter(Activity activity, List<Device> list) {
		super();
		this.list = list;
		this.activity=activity;

	}

	public List<Device> getItems() {
		return list;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private OnItemClickListener mOnItemClickListener;
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}

	private OnItemEditeListener onItemEditeListener;
	public void setOnItemEditeListener(OnItemEditeListener onItemEditeListener) {
		this.onItemEditeListener = onItemEditeListener;
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public View onCreateContentView(ViewGroup parent, int viewType) {

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_safe, parent, false);
	}
	@Override
	public void onBindViewHolder(DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
		holder.setOnItemEditeListener(onItemEditeListener);
	}
	@Override
	public SafeAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new SafeAdapter.DefaultViewHolder(realContentView);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
				TextView text;
				SmartImageView img;
				ImageView editeMode;
				OnItemClickListener mOnItemClickListener;
				OnItemEditeListener onItemEditeListener;
				public DefaultViewHolder(View itemView) {
					super(itemView);
					itemView.setOnClickListener(this);
					text = (TextView) itemView.findViewById(R.id.text);
					img = (SmartImageView) itemView.findViewById(R.id.img);
					editeMode=(ImageView)itemView.findViewById(R.id.editeMode);
					editeMode.setOnClickListener(this);
				}
				public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
					this.mOnItemClickListener = onItemClickListener;
				}
		public void setOnItemEditeListener(OnItemEditeListener onItemEditeListener) {
			this.onItemEditeListener = onItemEditeListener;
		}

		public void setData(final int position) {

					final Device device = list.get(position);
//					img.setImageUrl(Constants.HTTPS+device.getDeviceImage());
					if(device.getState()>0){
						img.setImageResource(R.mipmap.safe_bf);
					}else{
						img.setImageResource(R.mipmap.safe_cf);
					}
					text.setText(device.getRoomName()+device.getDeviceName());
				}
				@Override
				public void onClick(View v) {
					if(v.getId()==R.id.editeMode){
						if(onItemEditeListener!=null){
							onItemEditeListener.onItemEditeClick(getAdapterPosition());
						}
					}else {
						if (mOnItemClickListener != null) {
							mOnItemClickListener.onItemClick(getAdapterPosition());
						}
					}
				}
			}
		}
