package com.zunder.smart.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.image.cache.SmartImage;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Mode;
import com.zunder.smart.service.SendCMD;

import java.util.List;

public class ModeTabAdapter extends SwipeMenuAdapter<ModeTabAdapter.DefaultViewHolder> {
	static	List<Mode> list;
	static int mSelect = -1;
	static Activity activity;

	public ModeTabAdapter(Activity activity, List<Mode> list) {

		this.list = list;
		this.activity = activity;

	}

	public List<Mode> getItems(){
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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab_mode, parent, false);
	}

	@Override
	public ModeTabAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new ModeTabAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(ModeTabAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}

	static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView modeName;
		TextView modeTip;
		TextView nickName;
		SmartImageView img;
		LinearLayout modeLayout;

		OnItemClickListener mOnItemClickListener;


		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			modeName = (TextView) itemView.findViewById(R.id.modeName);
			modeTip=(TextView)itemView.findViewById(R.id.modeTip);
			nickName=(TextView)itemView.findViewById(R.id.nickName);
			modeLayout=(LinearLayout)itemView.findViewById(R.id.modeTipLayout);
			img=(SmartImageView) itemView.findViewById(R.id.img);
		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(final int position) {

			img.setImageUrl(Constants.HTTPS+list.get(position).getModeImage());
			modeName.setText(list.get(position).getModeName());

			if(list.get(position).getModeType().equals("FF")){
				modeLayout.setVisibility(View.VISIBLE);
				modeTip.setText("情景代码 "+list.get(position).getModeCode()+" 回路 "+(list.get(position).getModeLoop()+1));
			}else{
				modeLayout.setVisibility(View.GONE);
			}
			String nickNameStr=list.get(position).getModeNickName();
			if(!nickNameStr.equals("")) {
				nickName.setVisibility(View.VISIBLE);
				nickName.setText("别名:"+nickNameStr);
			}else{
				nickName.setVisibility(View.GONE);
			}
			img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SendCMD cmdsend = SendCMD.getInstance();
					cmdsend.sendCMD(
							0,
							list.get(position).getModeName(),
							null);
				}
			});
		}

		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(getAdapterPosition());
			}
		}
	}





}
