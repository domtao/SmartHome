package com.zunder.smart.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.R;

import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Mode;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.smart.model.Mode;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ModeAdapter extends SwipeMenuAdapter<ModeAdapter.DefaultViewHolder> {
	static	List<Mode> list;
	static int mSelect = -1;
	static Activity activity;

	public ModeAdapter(Activity activity,List<Mode> list) {

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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mode, parent, false);
	}

	@Override
	public ModeAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new ModeAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(ModeAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}

	static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView but_text;
		SmartImageView img;
		ImageView edite;
		OnItemClickListener mOnItemClickListener;


		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			but_text = (TextView) itemView.findViewById(R.id.text);
			img = (SmartImageView) itemView.findViewById(R.id.img);

			edite = (ImageView) itemView
					.findViewById(R.id.editeMode);
		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(int position) {
			but_text.setText(list.get(position).getModeName());
			img.setImageUrl(Constants.HTTPS+list.get(position).getModeImage());
			if (mSelect != -1 && position != list.size() - 1) {
				edite.setVisibility(View.VISIBLE);
				if (mSelect == 0) {
					edite.setImageResource(R.mipmap.edit_scene);
				} else if (mSelect == 1) {
					edite.setImageResource(R.mipmap.chang_scene);
				} else if (mSelect == 2) {
					edite.setImageResource(R.mipmap.delete_scen);
					if (position <= 7) {
						edite.setVisibility(View.GONE);
					}
				} else if (mSelect == 3) {
					if (list.get(position).getModeType().equals("FF")) {
						edite.setVisibility(View.VISIBLE);
						edite.setImageResource(R.mipmap.appoiont_scen);
					} else {
						edite.setVisibility(View.GONE);
					}
				}
			} else {
				edite.setVisibility(View.GONE);
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
