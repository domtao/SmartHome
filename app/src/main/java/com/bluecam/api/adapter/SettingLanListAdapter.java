package com.bluecam.api.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bluecam.api.bean.LanBean;
import com.zunder.smart.R;

import java.util.List;

public class SettingLanListAdapter extends ArrayAdapter<LanBean>
{
    private LayoutInflater inflater;
    private Context mContext;
    public SettingLanListAdapter(Context context, List<LanBean> cameras)
    {
        super(context, 0, cameras);
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LanHolder holder = null;
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.setting_language_list_item, null);
            holder = new LanHolder(convertView);
            convertView.setTag(holder);
        }
        else
            holder = (LanHolder) convertView.getTag();
        LanBean bean = getItem(position);
        if(bean.isSelected()){
            holder.lan_select_iv.setImageResource(R.drawable.lan_selected);
        }
        else {
            holder.lan_select_iv.setImageResource(R.drawable.lan_unselect);
        }
        holder.name_tv.setText(bean.getLan());
        return convertView;
    }

    private class LanHolder
    {
        public TextView name_tv;
        public ImageView lan_select_iv;
        public View item_view;

        public LanHolder(View view)
        {
            name_tv   = (TextView)view.findViewById(R.id.name_tv);
            item_view =  view.findViewById(R.id.item_view);
            lan_select_iv = (ImageView)view.findViewById(R.id.lan_select_iv);
        }
    }

}
