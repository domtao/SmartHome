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
import com.zunder.smart.remote.model.FileUser;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

public class FileUserAdapter extends SwipeMenuAdapter<FileUserAdapter.DefaultViewHolder> {
	public static List<FileUser> list;

	 Activity activity;


	@SuppressWarnings("static-access")
	public FileUserAdapter(Activity activity, List<FileUser> list) {
		super();
		this.activity=activity;
		this.list = list;
	}


	public List<FileUser> getItems() {
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
	public FileUserAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new FileUserAdapter.DefaultViewHolder(realContentView);
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
					userName.setText(list.get(position).getUserName());
					userType.setText(list.get(position).getTypeName());
					Glide.with(activity)
							.load(list.get(position).getTypeUrl())
							.placeholder(R.mipmap.unselected_digit)
							.into(img);
				}
				@Override
				public void onClick(View v) {
					if (mOnItemClickListener != null) {
						mOnItemClickListener.onItemClick(getAdapterPosition());
					}
				}
			}
		}
