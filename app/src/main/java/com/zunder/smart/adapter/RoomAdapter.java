package com.zunder.smart.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.zunder.image.view.SmartImageView;
import com.zunder.smart.R;
import com.zunder.smart.json.Constants;
import com.zunder.smart.model.Room;
import com.zunder.smart.popwindow.listener.OnItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

public class RoomAdapter extends SwipeMenuAdapter<RoomAdapter.DefaultViewHolder> {
	private static List<Room> list;
	private Activity context;

	int posion = -1;
	int pos = -1;
	int imgsId = -1;
	int imges = 0;

	@SuppressWarnings("static-access")
	public RoomAdapter(Activity context, List<Room> list) {
		this.context=context;
		this.list = list;
	}
	private OnItemClickListener mOnItemClickListener;
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}

	public void changSwichSate(int posion, int imgsId) {
		this.posion = posion;
		this.imgsId = imgsId;
		//	notifyDataSetChanged();//
	}



	@Override
	public long getItemId(int position) {
		return position;
	}

	public  List<Room>	getitems(){
		return list;
	}
	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public View onCreateContentView(ViewGroup parent, int viewType) {

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
	}
	@Override
	public void onBindViewHolder(RoomAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	@Override
	public RoomAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new RoomAdapter.DefaultViewHolder(realContentView);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView roomName;
		TextView roomTxt;
		TextView typeTxt;
		SmartImageView img;

		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			img = (SmartImageView) itemView.findViewById(R.id.img);
			typeTxt= (TextView) itemView
					.findViewById(R.id.typeTxt);
			roomName = (TextView) itemView
					.findViewById(R.id.roomName);
			roomTxt = (TextView) itemView
					.findViewById(R.id.roomTxt);

		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(final int position) {
			img.setImageUrl(Constants.HTTPS+list.get(position).getRoomImage());
			roomName.setText(list.get(position).getRoomName());
			typeTxt.setText(getisShow(list.get(position).getIsShow()));
		}

		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(getAdapterPosition());
			}
		}
	}
	public String getisShow(int isShow){
		if(isShow==0){
			return "隐藏";
		}else if(isShow==1){
			return "家居";
		}else {
			return "中控";
		}
	}

}
