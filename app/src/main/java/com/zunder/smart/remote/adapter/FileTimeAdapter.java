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
import com.zunder.smart.remote.model.FileType;
import com.zunder.smart.remote.model.FileUser;
import com.zunder.smart.tools.AppTools;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.smart.remote.model.FileTime;
import com.zunder.smart.tools.AppTools;

import java.util.List;

public class FileTimeAdapter extends SwipeMenuAdapter<FileTimeAdapter.DefaultViewHolder> {
	public static List<FileTime> list;

	 Activity activity;


	@SuppressWarnings("static-access")
	public FileTimeAdapter(Activity activity, List<FileTime> list) {
		super();
		this.activity=activity;
		this.list = list;
	}


	public List<FileTime> getItems() {
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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fileuser, parent, false);
	}
	@Override
	public void onBindViewHolder(DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	@Override
	public FileTimeAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new FileTimeAdapter.DefaultViewHolder(realContentView);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

				TextView userName;
				TextView userType;
				ImageView img;
				OnItemClickListener mOnItemClickListener;
				public DefaultViewHolder(View itemView) {
					super(itemView);
					itemView.setOnClickListener(this);
					img = (ImageView) itemView.findViewById(R.id.img);
					userName = (TextView) itemView
							.findViewById(R.id.userName);
					userType = (TextView) itemView
							.findViewById(R.id.userType);

				}
				public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
					this.mOnItemClickListener = onItemClickListener;
				}
				public void setData(final int position) {
					userName.setText("名称:"+list.get(position).getTimeName());

					int cycle=list.get(position).getCycle();
					if(cycle==0) {
						Glide.with(activity)
								.load(R.mipmap.one_img)
								.placeholder(R.mipmap.unselected_digit)
								.into(img);
					userType.setText("时间:"+list.get(position).getStartTime()+"——"+list.get(position).getEndTime()+"	仅一次:"+list.get(position).getAssignDate());
					}else if(cycle==128){
						Glide.with(activity)
								.load(R.mipmap.zd_img)
								.placeholder(R.mipmap.unselected_digit)
								.into(img);
						userType.setText("时间:"+list.get(position).getStartTime()+"——"+list.get(position).getEndTime()+"	指定日期:"+list.get(position).getAssignDate());
					}else{
						Glide.with(activity)
								.load(R.mipmap.cycle_img)
								.placeholder(R.mipmap.unselected_digit)
								.into(img);
						userType.setText("时间:"+list.get(position).getStartTime()+"——"+list.get(position).getEndTime()+"\t周期:"+ AppTools.getWeeks(cycle));
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
