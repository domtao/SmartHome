package com.bluecam.api.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zunder.smart.R;

/**
 * Created by Administrator on 2017/5/25.
 */

public class SettingListAdapter extends RecyclerView.Adapter<SettingListAdapter.SettingViewHolder>{

    private String[] mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private SettingClickListener clickListener;



    public SettingListAdapter(Context context, String[] datas, SettingClickListener clickListener){
        this. mContext=context;
        this. mDatas=datas;
        this.clickListener = clickListener;
        inflater= LayoutInflater. from(mContext);
    }

    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.setting_list_item,viewGroup, false);
        SettingViewHolder viewHolder = new SettingViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SettingViewHolder viewHolder, int position) {
        String title = mDatas[position];
        viewHolder.name_txt.setText(title);
        viewHolder.setting_item.setOnClickListener(new OnItemListener(position));
    }


    private class OnItemListener implements View.OnClickListener
    {
        private int position;
        public OnItemListener(int position)
        {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null)
            {
                clickListener.onClick(position);
            }
        }
    }
    @Override
    public int getItemCount() {
        return mDatas.length;
    }

    class SettingViewHolder extends RecyclerView.ViewHolder {
        TextView name_txt;
        View setting_item;

        public SettingViewHolder(View view) {
            super(view);
            name_txt  = (TextView)view.findViewById(R.id.name_txt);
            setting_item = view.findViewById(R.id.setting_item);
        }
    }
    public interface SettingClickListener
    {
        void onClick(int position);
    }
}
