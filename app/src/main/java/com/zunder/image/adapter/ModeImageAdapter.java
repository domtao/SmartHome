package com.zunder.image.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.image.model.ImageNet;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.R;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.OnItemClickListener;

import java.util.List;

public class ModeImageAdapter extends SwipeMenuAdapter<ModeImageAdapter.DefaultViewHolder> {
	static List<ImageNet> list;
	static int mSelect = -1;
	static Activity activity;

	public ModeImageAdapter(Activity activity, List<ImageNet> list) {

		this.list = list;
		this.activity = activity;

	}
	public List<ImageNet> getItems(){
		return list;
	}

	public void changeSelected(int edite) {
		mSelect = edite;
		notifyDataSetChanged();
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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
	}

	@Override
	public ModeImageAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new ModeImageAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(ModeImageAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}

	static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView ImageName;
		SmartImageView imgSrc;
		OnItemClickListener mOnItemClickListener;


		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			ImageName = (TextView) itemView.findViewById(R.id.ImageName);
			imgSrc = (SmartImageView) itemView.findViewById(R.id.imgSrc);

		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(int position) {
			ImageName.setText(list.get(position).getImageName());
			imgSrc.setImageUrl(Constants.HTTPS+list.get(position).getImageUrl());


		}

		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(getAdapterPosition());
			}
		}
	}





}
