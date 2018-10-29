package com.zunder.smart.remote.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zunder.smart.R;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.remote.model.FileTime;
import com.zunder.smart.remote.model.FileTimeList;
import com.zunder.smart.tools.AppTools;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.smart.remote.model.FileTimeList;

import java.util.List;

public class FileTimeListAdapter extends SwipeMenuAdapter<FileTimeListAdapter.DefaultViewHolder> {
	public static List<FileTimeList> list;

	 Activity activity;


	@SuppressWarnings("static-access")
	public FileTimeListAdapter(Activity activity, List<FileTimeList> list) {
		super();
		this.activity=activity;
		this.list = list;
	}


	public List<FileTimeList> getItems() {
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
	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public View onCreateContentView(ViewGroup parent, int viewType) {

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_list, parent, false);
	}
	@Override
	public void onBindViewHolder(DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	@Override
	public FileTimeListAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new FileTimeListAdapter.DefaultViewHolder(realContentView);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

				TextView contolName;
				TextView contolType;
				TextView contolTime;
				OnItemClickListener mOnItemClickListener;
				public DefaultViewHolder(View itemView) {
					super(itemView);
					itemView.setOnClickListener(this);
					contolTime = (TextView) itemView.findViewById(R.id.contolTime);
					contolName = (TextView) itemView
							.findViewById(R.id.contolName);
					contolType = (TextView) itemView
							.findViewById(R.id.controlType);
				}
				public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
					this.mOnItemClickListener = onItemClickListener;
				}
				public void setData(final int position) {
					FileTimeList timeList=list.get(position);
					String controlTime=timeList.getControlTime();
					contolTime.setText(controlTime.substring(0,controlTime.lastIndexOf(":")));
					contolName.setText(timeList.getControlName()+" "+timeList.getExtension());
					if(timeList.getControlIndex()==5){
						contolType.setText(timeList.getControlDevice());
					}else {
						contolType.setText(timeList.getFileName());
					}
				}
				@Override
				public void onClick(View v) {
					if (mOnItemClickListener != null) {
						mOnItemClickListener.onItemClick(getAdapterPosition());
					}
				}
			}
		}
