package com.bluecam.api.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zunder.smart.R;


/**
 * Created by Administrator on 2017/8/7.
 */

public class SettingCommonAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    public SettingCommonAdapter(@NonNull Context context, @NonNull String[] objects) {
        super(context, 0, objects);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HolderView holder = null;
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.setting_common_item, null);
            holder = new HolderView(convertView);
            convertView.setTag(holder);
        }
        else
            holder = (HolderView) convertView.getTag();
        String lable = getItem(position);
        holder.txt_lable.setText(lable);
        return convertView;
    }


    public class HolderView{
        TextView txt_lable;

        public HolderView(View view) {
            this.txt_lable = (TextView)view.findViewById(R.id.txt_lable);
        }
    }
}
