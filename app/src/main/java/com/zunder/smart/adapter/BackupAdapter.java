package com.zunder.smart.adapter;
import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Archive;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.smart.model.Archive;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BackupAdapter extends SwipeMenuAdapter<BackupAdapter.DefaultViewHolder> {
	List<Archive> list;
	public BackupAdapter(List<Archive> list) {
		this.list = list;
	}




	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_backup_list_item, parent, false);
	}

	@Override
	public BackupAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new BackupAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(BackupAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView name_text;
		TextView pwd_text;
		TextView timeTxt;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			name_text = (TextView) itemView
					.findViewById(R.id.backUpName);
			timeTxt = (TextView) itemView
					.findViewById(R.id.timeTxt);
			pwd_text = (TextView) itemView
					.findViewById(R.id.backUpPwd);
		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(int position) {
			name_text.setText(list.get(position).getProjectName());
			pwd_text.setText(list.get(position).getProjectPwd());
			timeTxt.setText(list.get(position).getProjectTime()
					.substring(5));
		}

		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(getAdapterPosition());
			}
		}
	}

}
