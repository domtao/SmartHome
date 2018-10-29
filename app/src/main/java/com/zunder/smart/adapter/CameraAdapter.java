package com.zunder.smart.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.GateWayTypeFactory;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.GateWay;

import java.util.List;
import java.util.Map;
import java.util.Set;

;

public class CameraAdapter extends SwipeMenuAdapter<CameraAdapter.DefaultViewHolder> {

	public static List<GateWay> list;
	private static Activity context;
	public static Map<String, String> map;
	public static long userid = 1;
	private static int pos = -1;
	private OnItemClickListener mOnItemClickListener;
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}
	public CameraAdapter(Activity context, List<GateWay> list,
                         Map<String, String> map) {
		 this.context=context;
		CameraAdapter.list = list;
		CameraAdapter.map = map;
	}

	public void getPosstion(int pos) {
		if (CameraAdapter.pos != pos) {
			CameraAdapter.pos = pos;
		}
		notifyDataSetChanged();
	}


	public List<GateWay> getItems() {
		// TODO Auto-generated method stub
		return list;
	}

	public void setList(List<GateWay> _list) {
		// TODO Auto-generated method stub
		this.list = _list;
		notifyDataSetChanged();
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
		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gateway, parent, false);
	}

	@Override
	public CameraAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new CameraAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(CameraAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}

	private boolean checkOnline(int pos) {
		boolean isCheck = false;
		Set<String> set = map.keySet();
		for (String key : set) {
			if (key.equals(list.get(pos).getGatewayID())) {
				String s_userid = map.get(key);
				userid = Long.parseLong(s_userid);
				isCheck = true;
				break;
			}
		}
		if (isCheck) {
			String name = list.get(pos).getState();
			return ((name.equals(context.getString(R.string.line))) ? true : false);
		}
		return false;
	}

	public static GateWay gateWay(int Id) {
		GateWay gateWay = null;
		if (list != null) {
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId() == Id) {
						gateWay = list.get(i);
						break;
					}
				}
			}
		}
		return gateWay;
	}

	public static GateWay gateWay(String name) {
		GateWay gateWay = null;
		if (list != null) {
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getGatewayName().equals(name)) {
						gateWay = list.get(i);
						break;
					}
				}
			}
		}
		return gateWay;
	}

	static class DefaultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
		TextView add_str;
		TextView add_line;
		ImageView edit;
		SmartImageView pic_add;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			add_str = (TextView) itemView
					.findViewById(R.id.add_str);
			add_line = (TextView) itemView
					.findViewById(R.id.add_line);
			edit = (ImageView) itemView.findViewById(R.id.edit_btn);
			pic_add = (SmartImageView) itemView
					.findViewById(R.id.index_add);
		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(int position) {
		if (list.get(position).getIsCurrent() == 1) {
				edit.setImageResource(R.mipmap.wvr_lock_icon);
				if (list.get(position).getTypeId() == 4
						&& list.get(position).getState()
						.equals(MyApplication.getInstance().getString(R.string.gateWayLine))) {
				}
			} else if (list.get(position).getIsCurrent() == 2) {
				edit.setImageResource(R.mipmap.status_online);
			} else {
				edit.setImageResource(0);
			}
			add_str.setText(list.get(position).getGatewayName());
			add_line.setText(list.get(position).getState());
			if(list.get(position).getTypeId()>3) {
				Drawable drawable = list.get(position).getPicData();
				if (drawable != null) {
					pic_add.setImageDrawable(drawable);
				}
			}else{
				pic_add.setImageUrl(Constants.HTTPS+GateWayTypeFactory.getGatewayImage(list.get(position).getTypeId()));
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
