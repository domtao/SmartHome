package com.bluecam.api.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.bluecam.api.utils.Contants;
import com.bluecam.api.utils.ImageLoader;
import com.bluecam.bluecamlib.BCamera;
import com.zunder.smart.R;

import java.util.List;

public class CameraListAdapter extends RecyclerView.Adapter<CameraListAdapter.DeviceViewHolder>{

    private List<BCamera> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;

    public CameraListAdapter(Context context, List<BCamera> datas, OnItemClickListener onItemClickListener){
        this. mContext=context;
        this. mDatas=datas;
        this.onItemClickListener = onItemClickListener;
        inflater= LayoutInflater. from(mContext);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.camera_list_item,viewGroup, false);
        DeviceViewHolder viewHolder = new DeviceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder deviceViewHolder, int position) {
        BCamera camera = mDatas.get(position);
        deviceViewHolder.device_name_txt.setText(camera.getCameraBean().getDevName());
        deviceViewHolder.status_txt.setText(Contants.getOnlineStatusString(camera.getStatus(),mContext));

        imageLoader.loadImage(camera.getCameraBean().getDevID(),deviceViewHolder.snapshot_view);
        deviceViewHolder.snapshot_view.setOnClickListener(new OnClickListener(camera));
        deviceViewHolder.record_tv.setOnClickListener(new OnClickListener(camera));
        deviceViewHolder.setting_tv.setOnClickListener(new OnClickListener(camera));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class OnClickListener implements View.OnClickListener
    {
        private BCamera camera;

        public OnClickListener(BCamera camera) {
            this.camera = camera;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(onItemClickListener == null)
                return;
            if(id == R.id.snapshot_view){
                onItemClickListener.onItemClick(camera);
            }
            else if(id == R.id.record_tv){
                onItemClickListener.onRecordClick(camera);
            }
            else if(id == R.id.setting_tv){
                onItemClickListener.onSettingClick(camera);
            }

        }
    }
    class DeviceViewHolder extends RecyclerView.ViewHolder {
        FrameLayout snapshot_view;
        TextView status_txt;
        TextView device_name_txt;
        TextView record_tv;
        TextView setting_tv;

        public DeviceViewHolder(View view) {
            super(view);
            snapshot_view       = (FrameLayout)view.findViewById(R.id.snapshot_view);
            status_txt          = (TextView)view.findViewById(R.id.status_item);
            device_name_txt     = (TextView)view.findViewById(R.id.device_name_txt);
            record_tv           = (TextView)view.findViewById(R.id.record_tv);
            setting_tv          = (TextView)view.findViewById(R.id.setting_tv);

        }
    }


    public  interface OnItemClickListener
    {
        void onItemClick(BCamera camera);
        void onRecordClick(BCamera camera);
        void onSettingClick(BCamera camera);
    }
}
