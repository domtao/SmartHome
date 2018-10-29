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

import com.bumptech.glide.Glide;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.GateWay;
import com.p2p.core.P2PHandler;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.smart.model.GateWay;

import java.util.List;
import java.util.Map;
import java.util.Set;

;

public class HostAdapter extends SwipeMenuAdapter<HostAdapter.DefaultViewHolder> {

	public static List<GateWay> list;
	private static Activity context;
	private OnItemClickListener mOnItemClickListener;
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}
	public HostAdapter(Activity context, List<GateWay> list,
                       Map<String, String> map) {
		 this.context=context;
		HostAdapter.list = list;

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
	public HostAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new HostAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(HostAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}

	static class DefaultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
		TextView add_str;
		TextView add_line;
		ImageView edit;
		ImageView open;
		ImageView pic_add;
		OnItemClickListener mOnItemClickListener;
		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			add_str = (TextView) itemView
					.findViewById(R.id.add_str);
			add_line = (TextView) itemView
					.findViewById(R.id.add_line);
			edit = (ImageView) itemView.findViewById(R.id.edit_btn);
			open = (ImageView) itemView.findViewById(R.id.open);
			pic_add = (ImageView) itemView
					.findViewById(R.id.index_add);
		}
		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}
		public void setData(int position) {
			add_str.setText(list.get(position).getGatewayName());
			add_line.setText(list.get(position).getGatewayID());
			Drawable drawable = list.get(position).getPicData();
			if (drawable != null) {
				pic_add.setImageDrawable(drawable);
			}
			if (list.get(position).getTypeId() == 5) {
				Glide.with(context)
						.load("")
						.placeholder(R.mipmap.soft_ico)
						.crossFade()
						.into(pic_add);
				add_line.setText(context.getString(R.string.clickapp));
			}else if(list.get(position).getTypeId()==4){
//				String userName = list.get(position).getGatewayID();
//				String passWord=list.get(position).getUserPassWord();
//				String	pwd = P2PHandler.getInstance().EntryPassword(passWord);
//				//经过转换后的设备密码
//				P2PHandler.getInstance().checkPassword(userName,pwd);
			}
			else if (list.get(position).getTypeId() == 2) {
				Glide.with(context)
						.load("")
						.placeholder(R.mipmap.dd)
						.crossFade()
						.into(pic_add);
			}
			else if (list.get(position).getTypeId() == 1) {
				Glide.with(context)
						.load("")
						.placeholder(R.mipmap.rak)
						.crossFade()
						.into(pic_add);
			}
			if(list.get(position).getSeqencing()==0){
				open.setVisibility(View.VISIBLE);
			}else{
				open.setVisibility(View.GONE);
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
