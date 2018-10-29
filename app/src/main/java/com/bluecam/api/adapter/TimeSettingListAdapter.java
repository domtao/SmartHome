package com.bluecam.api.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.bluecam.api.bean.TimeRowBean;
import com.bluecam.api.utils.Contants;
import com.bluecam.api.view.BaseTextWatcher;
import com.zunder.smart.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class TimeSettingListAdapter extends RecyclerView.Adapter<TimeSettingListAdapter.TimeViewHolder>{

    private List<TimeRowBean> timeList ;
    private Context context;
    private LayoutInflater inflater;

    private String getWeekStr(int index){
        String[] week = context.getResources().getStringArray(R.array.week_zh_lable);
        return week[index];
    }

    public TimeSettingListAdapter(Context context, List<TimeRowBean> datas){
        this. context=context;
        this. timeList=datas;
        inflater= LayoutInflater. from(context);
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.setting_time_item,viewGroup, false);
        TimeViewHolder viewHolder = new TimeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position) {
        TimeRowBean rowBean = timeList.get(position);
        holder.week_tv.setText(getWeekStr(rowBean.getIndex()));
        if (rowBean.getTimeItemBean() != null) {
            holder.start_hour.setText(Contants.formatNum(rowBean.getTimeItemBean().getStarthour()));
            holder.start_min.setText(Contants.formatNum(rowBean.getTimeItemBean().getStartmin()));
            holder.end_hour.setText(Contants.formatNum(rowBean.getTimeItemBean().getEndhour()));
            holder.end_min.setText(Contants.formatNum(rowBean.getTimeItemBean().getEndmin()));
            holder.start_hour.addTextChangedListener(new TimeTextWatcher(rowBean, R.id.start_hour,holder));
            holder.start_min.addTextChangedListener(new TimeTextWatcher(rowBean, R.id.start_min,holder));
            holder.end_hour.addTextChangedListener(new TimeTextWatcher(rowBean, R.id.end_hour,holder));
            holder.end_min.addTextChangedListener(new TimeTextWatcher(rowBean, R.id.end_min,holder));
        }
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    class TimeTextWatcher extends BaseTextWatcher
    {
        private TimeRowBean rowBean;
        private int id;
        private TimeViewHolder holder;

        public TimeTextWatcher(TimeRowBean rowBean, int id, TimeViewHolder holder) {
            this.rowBean = rowBean;
            this.id = id;
            this.holder = holder;
        }

        @Override
        public void afterTextChanged(Editable s) {
            String txt = s.toString();
            if(TextUtils.isEmpty(txt)){
                return;
            }
            //LogUtil.printLog("afterTextChanged-------txt=="+txt);
            if(id == R.id.start_hour){
                if(!Contants.isNumeric(txt)){
                    Toast.makeText(context,"请输入数字0-9", Toast.LENGTH_SHORT).show();
                    holder.start_hour.setText("");
                    return;
                }
                int startHour = Integer.parseInt(txt);
                if(startHour>23){
                    Toast.makeText(context,"小时不能大于23", Toast.LENGTH_SHORT).show();
                    holder.start_hour.setText("");
                    return;
                }
                rowBean.getTimeItemBean().setStarthour(startHour);
            }
            else if(id == R.id.start_min){
                if(!Contants.isNumeric(txt)){
                    Toast.makeText(context,"请输入数字0-9", Toast.LENGTH_SHORT).show();
                    holder.start_min.setText("");
                    return;
                }
                int startMin = Integer.parseInt(txt);
                if(startMin>59){
                    Toast.makeText(context,"分钟不能大于59", Toast.LENGTH_SHORT).show();
                    holder.start_min.setText("");
                    return;
                }
                rowBean.getTimeItemBean().setStartmin(startMin);
            }
            else if(id == R.id.end_hour){
                if(!Contants.isNumeric(txt)){
                    Toast.makeText(context,"请输入数字0-9", Toast.LENGTH_SHORT).show();
                    holder.end_hour.setText("");
                    return;
                }
                int endHour = Integer.parseInt(txt);
                if(endHour>23){
                    Toast.makeText(context,"小时不能大于23", Toast.LENGTH_SHORT).show();
                    holder.end_hour.setText("");
                    return;
                }
                rowBean.getTimeItemBean().setEndhour(endHour);
            }
            else if(id == R.id.end_min){
                if(!Contants.isNumeric(txt)){
                    Toast.makeText(context,"请输入数字0-9", Toast.LENGTH_SHORT).show();
                    holder.start_min.setText("");
                    return;
                }
                int endMin = Integer.parseInt(txt);
                if(endMin>59){
                    Toast.makeText(context,"分钟不能大于59", Toast.LENGTH_SHORT).show();
                    holder.end_min.setText("");
                    return;
                }
                rowBean.getTimeItemBean().setEndmin(endMin);
            }
            /*int startHour = Integer.parseInt(holder.start_hour.getText().toString().trim());
            int endHour = Integer.parseInt(holder.end_hour.getText().toString().trim());
            int startMin = Integer.parseInt(holder.start_min.getText().toString().trim());
            int endMin = Integer.parseInt(holder.end_min.getText().toString().trim());
            if(startHour>endHour){
                ToastUtils.showToast(context,context.getResources().getString(R.string.end_less_start_toast));
                return;
            }
            else if(startHour == endHour){
                if(startMin>=endMin){
                    ToastUtils.showToast(context,context.getResources().getString(R.string.end_less_start_toast));
                    return;
                }
            }
            if(startHour == 0 && startMin == 0 && endHour==23 && endMin == 59){
                holder.week_tv.setBackgroundResource(R.drawable.record_week_select_bg);
            }
            else {
                holder.week_tv.setBackgroundResource(R.drawable.record_time_set_title_bg);
            }*/
        }
    }
    class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView week_tv;
        EditText start_hour;
        EditText start_min;
        EditText end_hour;
        EditText end_min;
        public TimeViewHolder(View view) {
            super(view);
            week_tv    = (TextView)itemView.findViewById(R.id.week_tv);
            start_hour = (EditText)itemView.findViewById(R.id.start_hour);
            start_min  = (EditText)itemView.findViewById(R.id.start_min);
            end_hour   = (EditText)itemView.findViewById(R.id.end_hour);
            end_min    = (EditText)itemView.findViewById(R.id.end_min);
        }
    }

}
