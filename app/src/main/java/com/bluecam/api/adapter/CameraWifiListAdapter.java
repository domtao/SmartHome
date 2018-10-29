package com.bluecam.api.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zunder.smart.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CameraWifiListAdapter extends RecyclerView.Adapter<CameraWifiListAdapter.SettingViewHolder>{

    private List<JSONObject> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private OnWifiItemClickListener onItemClickListener;

    public CameraWifiListAdapter(Context context, List<JSONObject> datas, OnWifiItemClickListener onItemClickListener){
        this. mContext=context;
        this. mDatas=datas;
        this.onItemClickListener = onItemClickListener;
        inflater= LayoutInflater. from(mContext);
    }

    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.setting_wifi_list_item,viewGroup, false);
        SettingViewHolder viewHolder = new SettingViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SettingViewHolder viewHolder, int position) {
        JSONObject obj = mDatas.get(position);
        try
        {
            String ssid = obj.getString("ssid");
            String dbm0 = obj.getString("dbm0");
            viewHolder.ssid_txt.setText(ssid);
            viewHolder.sign_txt.setText("WIFI信号:"+dbm0+"%");
            viewHolder.wifi_item_fy.setOnClickListener(new OnItemListener(obj));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class SettingViewHolder extends RecyclerView.ViewHolder {
        FrameLayout wifi_item_fy;
        TextView ssid_txt;
        TextView sign_txt;



        public SettingViewHolder(View view) {
            super(view);
            wifi_item_fy  = (FrameLayout)view.findViewById(R.id.wifi_item_fy);
            ssid_txt      = (TextView)view.findViewById(R.id.ssid_txt);
            sign_txt      = (TextView)view.findViewById(R.id.sign_txt);
        }
    }

    private class OnItemListener implements View.OnClickListener
    {
        JSONObject obj;

        public OnItemListener(JSONObject obj) {

            this.obj = obj;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(obj);
        }
    }

    public  interface OnWifiItemClickListener
    {
        void onItemClick(JSONObject obj);

    }
}
