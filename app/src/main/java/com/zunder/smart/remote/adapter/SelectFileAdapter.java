package com.zunder.smart.remote.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zunder.smart.R;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.IFileInfo;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.HashMap;
import java.util.List;

public class SelectFileAdapter extends SwipeMenuAdapter<SelectFileAdapter.DefaultViewHolder> {
	public static List<IFileInfo> list;

	public static int edite = 0;
	int muCheck = -1;
	int posion = -1;
	int pos = -1;
	int imgsId = 0;
	int imges = 0;
	private static  HashMap<Integer, Boolean> isSelected;
	static Activity activity;

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	@SuppressWarnings("static-access")
	public SelectFileAdapter(Activity activity, List<IFileInfo> list) {
		super();
		this.list = list;
		this.activity=activity;
		isSelected = new HashMap<Integer, Boolean>();
		initDate();
	}
	public void changSwichSate(int posion, int imgsId) {
		this.posion = posion;
		this.imgsId = imgsId;
		notifyDataSetChanged();
	}

	public void muCheck() {

		if(isSelected!=null){
			for (int i = 0; i < isSelected.size(); i++) {
				isSelected.put(i, true);
			}
		}
		notifyDataSetChanged();
	}
	public List<IFileInfo> getItems() {
		return list;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private void initDate() {
		for (int i = 0; i < list.size(); i++) {
			isSelected.put(i, false);
		}
	}

	public void setList(List<IFileInfo> _list) {
		this.list = _list;
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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_file, parent, false);
	}
	@Override
	public void onBindViewHolder(DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	@Override
	public SelectFileAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new SelectFileAdapter.DefaultViewHolder(realContentView);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

				TextView fileName;
				TextView fileType;

				ImageView img;
				CheckBox cb;
				OnItemClickListener mOnItemClickListener;
				public DefaultViewHolder(View itemView) {
					super(itemView);
					itemView.setOnClickListener(this);
					img = (ImageView) itemView.findViewById(R.id.img);
					fileName = (TextView) itemView
							.findViewById(R.id.fileName);
					fileType = (TextView) itemView
							.findViewById(R.id.fileType);
					cb = (CheckBox) itemView.findViewById(R.id.item_cb);
				}
				public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
					this.mOnItemClickListener = onItemClickListener;
				}
				public void setData(final int position) {
					fileName.setText(list.get(position).getFileName());
					fileType.setText(list.get(position).getExtension().replace(".",""));
					int typeId = list.get(position).getTypeId();
					if (typeId==1) {
						img.setImageResource(R.mipmap.video_ig);
					}else 	if (typeId==2) {
						img.setImageResource(R.mipmap.image_img);
					}
					else 	if (typeId==3) {
						img.setImageResource(R.mipmap.doc_img);
					}
					else 	if (typeId==4) {
						img.setImageResource(R.mipmap.excle_img);
					}else 	if (typeId==5) {
						img.setImageResource(R.mipmap.power_img);
					}else 	if (typeId==6) {
						img.setImageResource(R.mipmap.pdf_img);
					}else 	if (typeId==7) {
						img.setImageResource(R.mipmap.other_img);
					}
					if(isSelected.get(position)){
						cb.setChecked(true);
					}else{
						cb.setChecked(false);
					}
					cb.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (((CheckBox) v).isChecked()) {
								isSelected.put(position, true);
							} else {
								isSelected.put(position, false);
							}
							notifyDataSetChanged();
						}
						// }
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
