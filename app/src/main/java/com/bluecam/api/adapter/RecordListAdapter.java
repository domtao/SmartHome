package com.bluecam.api.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bluecam.api.bean.RecordBean;
import com.zunder.smart.R;

import java.util.List;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>{

    private List<RecordBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public RecordListAdapter(Context context, List<RecordBean> datas, OnItemClickListener onItemClickListener){
        this. mContext=context;
        this. mDatas=datas;
        this.onItemClickListener = onItemClickListener;
        inflater= LayoutInflater. from(mContext);
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.record_list_item,viewGroup, false);
        RecordViewHolder viewHolder = new RecordViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecordViewHolder viewHolder, int position) {
        RecordBean recordBean = mDatas.get(position);
        viewHolder.txt_lable.setText(recordBean.getStrDate());
        viewHolder.txt_lable.setOnClickListener(new OnClickListener(recordBean));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class OnClickListener implements View.OnClickListener
    {
        private RecordBean recordBean;

        public OnClickListener(RecordBean recordBean) {
            this.recordBean = recordBean;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(recordBean);
        }
    }
    class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView txt_lable;

        public RecordViewHolder(View view) {
            super(view);
            txt_lable          = (TextView)view.findViewById(R.id.txt_lable);
        }
    }


    public  interface OnItemClickListener
    {
        void onItemClick(RecordBean recordBean);
    }
}
