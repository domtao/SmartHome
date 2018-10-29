package com.zunder.smart.aiui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.activity.AiuiMainActivity;
import com.zunder.smart.aiui.activity.BlueToothActivity;
import com.zunder.smart.aiui.info.BlueDevice;
import com.zunder.smart.listview.SwipeItemLayout;
import com.zunder.smart.service.DeviceTypeIMG;
import com.zunder.smart.socket.info.ISocketCode;

import java.util.List;

public class BlueToothAdapter extends BaseAdapter {
    public static List<BlueDevice> list;
    private BlueToothActivity context;


    @SuppressWarnings("static-access")
    public BlueToothAdapter(BlueToothActivity context, List<BlueDevice> list) {
        super();
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    public List<BlueDevice> getItems() {
        return list;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewCach viewCach = null;
        final BlueDevice device = list.get(position);
        if (convertView == null) {
            View contentView = LayoutInflater.from(context).inflate(
                    R.layout.item_bluetooth_device, null);
            View editeView = LayoutInflater.from(context).inflate(
                    R.layout.activity_edite_bluetooth, null);
            viewCach = new ViewCach();
            viewCach.title = (TextView) contentView.findViewById(R.id.title);
            viewCach.img = (ImageView) contentView.findViewById(R.id.img);
            viewCach.setImg= (ImageView) contentView.findViewById(R.id.setImg);
            viewCach.deviceName = (TextView) contentView
                    .findViewById(R.id.deviceName);
            viewCach.deviceState = (TextView) contentView
                    .findViewById(R.id.deviceState);

            viewCach.editeTitle = (TextView) editeView
                    .findViewById(R.id.editeTitle);

            viewCach.delBtn = (LinearLayout) editeView
                    .findViewById(R.id.delBtn);
                convertView = new SwipeItemLayout(contentView, editeView, null,
                        null);
            convertView.setTag(viewCach);
        } else {
            viewCach = (ViewCach) convertView.getTag();
        }

        viewCach.deviceName.setText(device.getDeviceName());

        int imgID=  DeviceTypeIMG.initBlueToothImg(device.getProductsCode());
        Glide.with(context)
                .load(imgID)
                .crossFade()
                .into(viewCach.img);
        int state= device.getBoundState();
        if (needTitle(position)) {
          if(state==12) {
              viewCach.title.setText("配对设备");
          }else{
              viewCach.title.setText("可用设备");
          }
            viewCach.title.setVisibility(View.VISIBLE);
            viewCach.editeTitle.setVisibility(View.VISIBLE);
        } else {
            viewCach.title.setVisibility(View.GONE);
            viewCach.editeTitle.setVisibility(View.GONE);
        }
        if(state==12){
            int connect=device.getConnectState();
           if(connect==1){
                viewCach.deviceState.setText("正在连接");
            }else if(connect==2){
                viewCach.deviceState.setText("连接成功");
            }else{
               viewCach.deviceState.setText("");
           }
            viewCach.delBtn.setVisibility(View.VISIBLE);
        }else{
            if(state==11){
                viewCach.deviceState.setText("配对中");
            }else{
                viewCach.deviceState.setText("");
            }
            viewCach.delBtn.setVisibility(View.GONE);
        }
        viewCach.delBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               BlueDevice blueDevice= list.get(position);
               String address=device.getAddress();
                String result = ISocketCode.setBlueTooth("setRemoveBound:"+address,
                        AiuiMainActivity.deviceID);
                MainActivity.getInstance().sendCode(result);
                context.OnConnectState(address,10);
            }
        });
        viewCach.setImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                context.onItemClick(position);
            }
        });
        return convertView;
    }
    private final class ViewCach {
        TextView title;
        TextView deviceName;
        TextView deviceState;
        ImageView img;
        ImageView setImg;
        TextView editeTitle = null;
        LinearLayout delBtn = null;
    }

    private boolean needTitle(int position) {
        if (position == 0) {
            return true;
        }
        if (position < 0) {
            return false;
        }
        BlueDevice currentEntity = (BlueDevice) getItem(position);
        BlueDevice previousEntity = (BlueDevice) getItem(position - 1);
        if (null == currentEntity || null == previousEntity) {
            return false;
        }
        int currentTitle = currentEntity.getBoundState();
        int previousTitle = previousEntity.getBoundState();
        if (currentTitle==previousTitle) {
            return false;
        }
        if(currentTitle!=previousTitle){
            if(currentTitle<12&&previousTitle<12){
                return false;
            }
        }
        return true;
    }
}
