package com.zunder.smart.aiui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.OnItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.tools.AppTools;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.TextView;

public class CusModeListAdapter extends SwipeMenuAdapter<CusModeListAdapter.DefaultViewHolder> {
	private static List<ModeList> list;
	private Activity context;
	public static int edite = 0;
	int posion = -1;
	int pos = -1;
	int imgsId = 0;
	int imges = 0;
	public static Map<String, Integer> map;

	public static String isChange = "No";

	@SuppressWarnings("static-access")
	public CusModeListAdapter(List<ModeList> list) {
		map = new HashMap<String, Integer>();
		this.list = list;
	}

	public void changSwichSate(int posion, int imgsId) {
		this.posion = posion;
		this.imgsId = imgsId;
		notifyDataSetChanged();
	}


	public List<ModeList> getItems() {
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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cus_mode, parent, false);
	}

	@Override
	public CusModeListAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new CusModeListAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(CusModeListAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView deviceName;
		TextView deviceEvent;
		SmartImageView img;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			img=(SmartImageView) itemView
					.findViewById(R.id.img);
			deviceName = (TextView) itemView
					.findViewById(R.id.deviceName);
			deviceEvent= (TextView) itemView
					.findViewById(R.id.deviceEvent);
		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(int position) {
			deviceName.setText(list.get(position).getDeviceName());
			String time = list.get(position).getModeTime();
			String delay = list.get(position).getModeDelayed();
			String period = list.get(position).getModePeriod();
			String result=list.get(position).getModeAction()+list.get(position).getModeFunction();
			img.setImageUrl(Constants.HTTPS+list.get(position).getDeviceImage());
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
				result+=" "+beginMonth+ MyApplication.getInstance().getString(R.string.mouth)+"~"+endMonth+ MyApplication.getInstance().getString(R.string.mouth);
			}
			deviceEvent.setText(result);
		}

		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(getAdapterPosition());
			}
		}
	}
}
