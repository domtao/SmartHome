package com.zunder.smart.aiui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.activity.AiuiMainActivity;
import com.zunder.smart.aiui.activity.CusDeviceActivity;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listview.SwipeItemLayout;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.DeviceTypeIMG;
import com.zunder.smart.socket.info.ISocketCode;

import java.util.List;

public class CusAdapter extends BaseAdapter {
    public static List<Device> list;
    private CusDeviceActivity activity;

    @SuppressWarnings("static-access")
    public CusAdapter(CusDeviceActivity activity, List<Device> list) {
        super();
        this.activity = activity;
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

    public List<Device> getItems() {
        return list;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewCach viewCach = null;
        final Device device = list.get(position);
        if (convertView == null) {
            View contentView = LayoutInflater.from(activity).inflate(
                    R.layout.item_home_device, null);
            View editeView = LayoutInflater.from(activity).inflate(
                    R.layout.activity_edite, null);
            viewCach = new ViewCach();
            viewCach.title = (TextView) contentView.findViewById(R.id.title);
            viewCach.img = (SmartImageView) contentView.findViewById(R.id.img);
            viewCach.deviceName = (TextView) contentView
                    .findViewById(R.id.deviceName);
            viewCach.deviceEvent = (TextView) contentView
                    .findViewById(R.id.deviceEvent);
            viewCach.editeTitle = (TextView) editeView
                    .findViewById(R.id.editeTitle);
            viewCach.itemLayout = (RelativeLayout) contentView
                    .findViewById(R.id.itemLayout);
            viewCach.editeBtn = (LinearLayout) editeView
                    .findViewById(R.id.editeBtn);
            viewCach.editeTitle = (TextView) editeView
                    .findViewById(R.id.editeTitle);
            viewCach.searchBtn = (LinearLayout) editeView
                    .findViewById(R.id.searchBtn);
            viewCach.delBtn = (LinearLayout) editeView
                    .findViewById(R.id.delBtn);
            viewCach.timingBtn = (LinearLayout) editeView
                    .findViewById(R.id.timingBtn);
            convertView = new SwipeItemLayout(contentView, editeView, null,
                        null);
            convertView.setTag(viewCach);

        } else {
            viewCach = (ViewCach) convertView.getTag();
        }

        if(device.getRoomId()>0) {
            viewCach.editeBtn.setVisibility(View.VISIBLE);
            viewCach.delBtn.setVisibility(View.VISIBLE);
            viewCach.searchBtn.setVisibility(View.GONE);
            viewCach.timingBtn.setVisibility(View.GONE);
            viewCach.deviceName.setText(device.getDeviceName());
            viewCach.deviceEvent.setText("设备ID:"+device.getDeviceID());

            viewCach.img.setImageUrl(Constants.HTTPS+device.getDeviceImage());
            if (needTitle(position)) {
                viewCach.title.setText(device.getRoomName());
                viewCach.title.setVisibility(View.VISIBLE);
                viewCach.editeTitle.setVisibility(View.VISIBLE);
            } else {
                viewCach.title.setVisibility(View.GONE);
                viewCach.editeTitle.setVisibility(View.GONE);
            }
        }
        viewCach.delBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                activity.delete(position);
            }
        });
        viewCach.editeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                activity.edite(position);
            }
        });
        return convertView;
    }

    private final class ViewCach {

        TextView title;
        TextView deviceName;
        TextView deviceEvent;
        SmartImageView img;
        RelativeLayout itemLayout;
        LinearLayout editeBtn = null;
        TextView editeTitle = null;
        LinearLayout searchBtn = null;
        LinearLayout delBtn = null;
        LinearLayout timingBtn = null;
    }

    private boolean needTitle(int position) {
        if (position == 0) {
            return true;
        }


        if (position < 0) {
            return false;
        }

        Device currentEntity = (Device) getItem(position);
        Device previousEntity = (Device) getItem(position - 1);
        if (null == currentEntity || null == previousEntity) {
            return false;
        }
        String currentTitle = currentEntity.getRoomName();
        String previousTitle = previousEntity.getRoomName();
        if (null == previousTitle || null == currentTitle) {
            return false;
        }
        if (currentTitle.equals(previousTitle)) {
            return false;
        }
        return true;
    }

}
