package com.zunder.smart.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.List;

import com.bumptech.glide.Glide;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.R;
import com.zunder.smart.json.Constants;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.popwindow.listener.OnItemClickListener;
import com.zunder.smart.service.DeviceTypeIMG;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.tools.AppTools;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

public class ModeListAdapter extends SwipeMenuAdapter<ModeListAdapter.DefaultViewHolder> {
	private static List<ModeList> list;
	private Activity context;

	int posion = -1;
	int pos = -1;
	int imgsId = -1;
	int imges = 0;

	@SuppressWarnings("static-access")
	public ModeListAdapter(Activity context, List<ModeList> list) {
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

	public  List<ModeList>	getitems(){
		return list;
	}
	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public View onCreateContentView(ViewGroup parent, int viewType) {

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mode_list_1, parent, false);
	}
	@Override
	public void onBindViewHolder(ModeListAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	@Override
	public ModeListAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new ModeListAdapter.DefaultViewHolder(realContentView);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView deviceName;
		TextView deviceEvent;
		SmartImageView img;
		ImageView checkBox;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			img = (SmartImageView) itemView.findViewById(R.id.img);
			deviceName = (TextView) itemView
					.findViewById(R.id.deviceName);
			deviceEvent = (TextView) itemView
					.findViewById(R.id.deviceEvent);
			checkBox = (ImageView) itemView
					.findViewById(R.id.checkBox_on);

		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(final int position) {
			deviceName.setText(list.get(position).getRoomName()+list.get(position).getDeviceName());
			String time = list.get(position).getModeTime();
			String delay = list.get(position).getModeDelayed();
			String period = list.get(position).getModePeriod();
			String result=list.get(position).getModeAction()+list.get(position).getModeFunction();

			if (!AppTools.getNumbers(time).equals("0")) {
				result+=time;
			}

			if (!AppTools.getNumbers(delay).equals("0")) {
				result+= "#" + delay;
			}
			if(!period.equals("")&&!period.equals("00:00~00:00")&&period!="00:00~00:00"){
				result+="("+period+")\n";
			}
			String beginMonth=list.get(position).getBeginMonth();
			String endMonth=list.get(position).getEndMonth();
			if(Integer.parseInt(beginMonth)!=0||Integer.parseInt(endMonth)!=0)
			{
				result+=" "+beginMonth+context.getString(R.string.mouth)+"~"+endMonth+context.getString(R.string.mouth);
			}
			deviceEvent.setText(result);
			img.setImageUrl(Constants.HTTPS+list.get(position).getDeviceImage());
			checkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String delay = list.get(position).getModeDelayed();
					String deviceName = list.get(position).getDeviceName()
							+ list.get(position).getModeFunction()
							+ list.get(position).getModeEvent() + "#" + delay;
					if (delay.equals("") || delay == null || delay.equals("0")) {
						deviceName = list.get(position).getDeviceName()
								+ list.get(position).getModeFunction()
								+ list.get(position).getModeEvent();
					}
					SendCMD cmdsend = SendCMD.getInstance();
					cmdsend.sendCMD(0, deviceName, null);
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
