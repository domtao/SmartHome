package com.zunder.smart.adapter;

import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.DeviceTimer;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.smart.MyApplication;
import com.zunder.smart.model.DeviceTimer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ModeTimerAdapter extends SwipeMenuAdapter<ModeTimerAdapter.DefaultViewHolder> {
	static List<DeviceTimer> list;

	public ModeTimerAdapter(List<DeviceTimer> _list) {
		this.list=_list;
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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timer, parent, false);
	}

	@Override
	public ModeTimerAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new ModeTimerAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(ModeTimerAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView textName;
		TextView textWeek;
		ImageView img;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			textName = (TextView) itemView.findViewById(R.id.timerName);
			textWeek = (TextView) itemView.findViewById(R.id.timerWeek);
			img = (ImageView) itemView.findViewById(R.id.timerEdite);
		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(int position) {
			textName.setText(list.get(position).getCycle());

			textWeek.setText(list.get(position).getTimer());
			if (list.get(position).getCycle()
					.contains(MyApplication.getInstance().getString(R.string.actionOne))) {
				img.setImageResource(R.mipmap.one_img);
			} else {
				img.setImageResource(R.mipmap.cycle_img);
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
