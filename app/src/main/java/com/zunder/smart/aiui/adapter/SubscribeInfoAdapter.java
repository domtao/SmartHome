package com.zunder.smart.aiui.adapter;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.activity.AiuiMainActivity;
import com.zunder.smart.aiui.activity.SubscribeActivity;
import com.zunder.smart.aiui.info.SubscribeInfo;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.popwindow.OCPopupWindow;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.AppTools;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SubscribeInfoAdapter extends SwipeMenuAdapter<SubscribeInfoAdapter.DefaultViewHolder> {

	public static List<SubscribeInfo> list;
	private static SubscribeActivity context;
	private OnItemClickListener mOnItemClickListener;
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}
	public SubscribeInfoAdapter(SubscribeActivity context,List<SubscribeInfo> list) {
		this.context=context;
		this.list=list;
	}


	public List<SubscribeInfo> getItems() {
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemCount() {
		return list.size();
	}
	@Override
	public View onCreateContentView(ViewGroup parent, int viewType) {
		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subscribe, parent, false);
	}

	@Override
	public DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}


	static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView subscribeName;
		TextView subscribeEvent;
		TextView subscribeAction;
		TextView subscribeDate;
		ImageView subscribeShow;
		ImageView down;
		OnItemClickListener mOnItemClickListener;
		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			subscribeName = (TextView) itemView
					.findViewById(R.id.subscribeName);
			subscribeEvent = (TextView) itemView
					.findViewById(R.id.subscribeEvent);
			subscribeAction = (TextView) itemView
					.findViewById(R.id.subscribeAction);
			subscribeDate = (TextView) itemView
					.findViewById(R.id.subscribeDate);
			down = (ImageView) itemView
					.findViewById(R.id.downImage);
			subscribeShow=(ImageView)itemView.findViewById(R.id.subscribeShow);
		}
		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(final int position) {

			down.setImageResource(R.mipmap.voice_lock);
			subscribeName.setText(list.get(position).getSubscribeName());
			subscribeEvent.setText(list.get(position).getSubscribeEvent());
			subscribeAction.setText(list.get(position).getSubscribeAction());
			String date=list.get(position).getSubscribeDate();
			if(AppTools.isNumeric(date)){
				subscribeDate.setText(AppTools.getWeeks(Integer.parseInt(date)) + " " + list.get(position).getSubscribeTime());
			}else {
				subscribeDate.setText(date + " " + list.get(position).getSubscribeTime());
			}
			if(list.get(position).getUserName().equals("1")){
				subscribeShow.setVisibility(View.VISIBLE);
			}else{
				subscribeShow.setVisibility(View.GONE);
			}
			down.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					OCPopupWindow popupWindow=new OCPopupWindow(context,"启用状态");
					popupWindow.setOnOCListene(new OCPopupWindow.OnOCListener() {
						@Override
						public void onOpen() {
							SubscribeInfo voiceInfo=list.get(position);
							voiceInfo.setUserName("0");
							String result = ISocketCode.setAddSubscribe(
									voiceInfo.convertTostring(),
									AiuiMainActivity.deviceID);
							MainActivity.getInstance().sendCode(result);
							context.adapter();
						}
						@Override
						public void onClose() {
							SubscribeInfo voiceInfo=list.get(position);
							voiceInfo.setUserName("1");
							String result = ISocketCode.setAddSubscribe(
									voiceInfo.convertTostring(),
									AiuiMainActivity.deviceID);
							MainActivity.getInstance().sendCode(result);
							context.adapter();
						}
					});
					popupWindow.show();
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
