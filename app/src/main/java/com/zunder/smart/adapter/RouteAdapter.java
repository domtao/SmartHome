package com.zunder.smart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zunder.smart.R;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Route;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

public class RouteAdapter extends SwipeMenuAdapter<RouteAdapter.DefaultViewHolder> {
	private static List<Route> list;
	public static int edite = 0;
	int _posion = -1;
	int imges = 0;
	Context context;
	public RouteAdapter(Context context, List<Route> list) {
		this.context=context;
		this.list = list;
	}

	public void changSwichSate(int posion) {
		this._posion = posion;
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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
	}
	@Override
	public RouteAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new RouteAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(RouteAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);

		holder.setOnItemClickListener(mOnItemClickListener);
	}
class DefaultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
		TextView routeName;
		TextView routeID;
		ImageView img;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			img = (ImageView) itemView.findViewById(R.id.img);
			routeName = (TextView) itemView
					.findViewById(R.id.routeName);
			routeID = (TextView) itemView
					.findViewById(R.id.routeID);
		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(int position) {

			routeName.setText(list.get(position).getRouteName());
			routeID.setText(list.get(position).getRouteID());
			if(list.get(position).getRouteType()==4){
				Glide.with(context)
						.load(R.mipmap.dd)
						.placeholder(R.mipmap.load_img)
						.crossFade()
						.into(img);
			}else{
				Glide.with(context)
						.load(R.mipmap.gateway)
						.placeholder(R.mipmap.load_img)
						.crossFade()
						.into(img);
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
