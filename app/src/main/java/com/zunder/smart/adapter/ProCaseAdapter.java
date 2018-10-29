package com.zunder.smart.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.R;
import com.zunder.smart.json.Constants;
import com.zunder.smart.model.ProCase;
import com.zunder.smart.model.Room;
import com.zunder.smart.popwindow.listener.OnItemClickListener;

import java.util.List;

public class ProCaseAdapter extends SwipeMenuAdapter<ProCaseAdapter.DefaultViewHolder> {
	private static List<ProCase> list;
	private Activity context;

	int posion = -1;
	int pos = -1;
	int imgsId = -1;
	int imges = 0;

	@SuppressWarnings("static-access")
	public ProCaseAdapter(Activity context, List<ProCase> list) {
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

	public  List<ProCase>	getitems(){
		return list;
	}
	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public View onCreateContentView(ViewGroup parent, int viewType) {

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_procase, parent, false);
	}
	@Override
	public void onBindViewHolder(ProCaseAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	@Override
	public ProCaseAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new ProCaseAdapter.DefaultViewHolder(realContentView);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView proCaseName;
		TextView proCaseTxt;
		SmartImageView img;

		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			img = (SmartImageView) itemView.findViewById(R.id.img);
			proCaseName = (TextView) itemView
					.findViewById(R.id.proCaseName);
			proCaseTxt = (TextView) itemView
					.findViewById(R.id.proCaseTxt);

		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(final int position) {
			proCaseName.setText(list.get(position).getProCaseName());
			if(list.get(position).getProCaseIndex()==1){
				proCaseTxt.setText("当前");
			}else{
				proCaseTxt.setText("");
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
