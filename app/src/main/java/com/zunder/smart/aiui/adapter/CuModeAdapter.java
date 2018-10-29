package com.zunder.smart.aiui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zunder.image.view.SmartImageView;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.activity.AiuiMainActivity;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Mode;
import com.zunder.smart.socket.info.ISocketCode;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

public class CuModeAdapter extends SwipeMenuAdapter<CuModeAdapter.DefaultViewHolder> {
	private static List<Mode> list;
	private static Context context;
	public static int edite = 0;
	int posion = -1;
	int imgsId = 0;

	public static String isChange = "No";

	@SuppressWarnings("static-access")
	public CuModeAdapter(Context context, List<Mode> list) {
		this.context=context;
		this.list = list;
	}

	public void changSwichSate(int posion, int imgsId) {
		this.posion = posion;
		this.imgsId = imgsId;
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

	public List<Mode> getItems() {
		return list;
	}
	@Override
	public View onCreateContentView(ViewGroup parent, int viewType) {

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cus_mode, parent, false);
	}

	@Override
	public CuModeAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new CuModeAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(CuModeAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	static class DefaultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
		TextView deviceName;
		TextView deviceEvent;
		SmartImageView img;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			img = (SmartImageView) itemView.findViewById(R.id.img);
			deviceName = (TextView) itemView
					.findViewById(R.id.deviceName);
			deviceEvent = (TextView) itemView
					.findViewById(R.id.deviceEvent);
		}
		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(int position) {
			final Mode mode=list.get(position);
			deviceName.setText(mode.getModeName());
			img.setImageUrl(Constants.HTTPS+mode.getModeImage());
			if(mode.getModeType().equals("FF")){
				deviceEvent.setText(context.getString(R.string.sence)+":"+mode.getModeCode());
			}else{
				deviceEvent.setText(context.getString(R.string.set));
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
