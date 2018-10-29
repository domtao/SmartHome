package com.zunder.smart.remote.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zunder.smart.R;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.IFileInfo;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

public class IFileInfoAdapter extends SwipeMenuAdapter<IFileInfoAdapter.DefaultViewHolder> {
	public static List<IFileInfo> list;

	static Activity activity;


	@SuppressWarnings("static-access")
	public IFileInfoAdapter(Activity activity, List<IFileInfo> list) {
		super();
		this.list = list;
	}


	public List<IFileInfo> getItems() {
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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fileinfo, parent, false);
	}
	@Override
	public void onBindViewHolder(DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	@Override
	public IFileInfoAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new IFileInfoAdapter.DefaultViewHolder(realContentView);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

		TextView songAuthor;
		TextView songTitle;

		ImageView img;

		OnItemClickListener mOnItemClickListener;
		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			img = (ImageView) itemView.findViewById(R.id.img);
			songAuthor = (TextView) itemView
					.findViewById(R.id.songAuthor);
			songTitle = (TextView) itemView
					.findViewById(R.id.songTitle);
			img = (ImageView) itemView.findViewById(R.id.img);

		}
		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}
		public void setData(final int position) {

			songAuthor.setText(list.get(position).getExtension().replace(".",""));
			songTitle.setText(list.get(position).getFileName());
			int typeId = list.get(position).getTypeId();
			if (typeId==1) {
				img.setImageResource(R.mipmap.video_ig);
			}else 	if (typeId==2) {
				img.setImageResource(R.mipmap.image_img);
			}
			else 	if (typeId==3) {
				img.setImageResource(R.mipmap.doc_img);
			}
			else 	if (typeId==4) {
				img.setImageResource(R.mipmap.excle_img);
			}else 	if (typeId==5) {
				img.setImageResource(R.mipmap.power_img);
			}else 	if (typeId==6) {
				img.setImageResource(R.mipmap.pdf_img);
			}else 	if (typeId==7) {
				img.setImageResource(R.mipmap.other_img);
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
