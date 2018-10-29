package com.zunder.smart.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zunder.smart.R;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Device;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */

public class TestDeviceAdapter extends SwipeMenuAdapter<TestDeviceAdapter.DefaultViewHolder> {

    private static List<Device> list;

    public TestDeviceAdapter(List<Device> list) {
        this.list = list;
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

        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
    }

    @Override
    public DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new TestDeviceAdapter.DefaultViewHolder(realContentView);
    }
    @Override
    public void onBindViewHolder(TestDeviceAdapter.DefaultViewHolder holder, int position) {
        holder.setData(position);
        holder.setOnItemClickListener(mOnItemClickListener);
    }
    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setData(int position) {

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
