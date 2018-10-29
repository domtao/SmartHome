package com.zunder.smart.aiui.adapter;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.activity.AiuiMainActivity;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.OnItemClickListener;
;
import com.zunder.smart.model.VoiceInfo;
import com.zunder.smart.socket.info.ISocketCode;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class VoiceAdapter extends SwipeMenuAdapter<VoiceAdapter.DefaultViewHolder> {

	public static List<VoiceInfo> list;
	private static Activity context;
	private OnItemClickListener mOnItemClickListener;
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}
	public VoiceAdapter(Activity context,List<VoiceInfo> list) {
		this.context=context;
		this.list=list;
	}


	public List<VoiceInfo> getItems() {
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
		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice, parent, false);
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

	static class DefaultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
		TextView voiceName;
		TextView voiceAnswer;
		TextView voiceAction;
		ImageView down;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			voiceName = (TextView) itemView
					.findViewById(R.id.voiceName);
			voiceAnswer = (TextView) itemView
					.findViewById(R.id.voiceAnswer);
			voiceAction = (TextView) itemView
					.findViewById(R.id.voiceAction);
			down = (ImageView) itemView
					.findViewById(R.id.downImage);
		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(final int position) {
			voiceName.setText(list.get(position).getVoiceName());
			voiceAnswer.setText(list.get(position).getVoiceAnswer());
			String action=list.get(position).getVoiceAction();
			if(!action.equals("")) {
				voiceAction.setText("动作:"+action);
			}
			down.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DialogAlert alert = new DialogAlert(context);
					alert.init(context.getString(R.string.is_down), list.get(position).getVoiceName());
					alert.setSureListener(new DialogAlert.OnSureListener() {

						@Override
						public void onSure() {
							// TODO Auto-generated method stub
							VoiceInfo device = list.get(position);
							device.setId(0);
							device.setUserName("1");
							String result = ISocketCode.setAddVoice(
									device.convertTostring(),
									AiuiMainActivity.deviceID);
							MainActivity.getInstance().sendCode(result);
						}
						@Override
						public void onCancle() {
							// TODO Auto-generated method stub
						}
					});
					alert.show();
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
