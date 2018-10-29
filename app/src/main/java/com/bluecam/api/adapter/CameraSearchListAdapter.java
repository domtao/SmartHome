package com.bluecam.api.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bluecam.api.bean.SearchBean;
import com.bluecam.bluecamlib.BCamera;
import com.zunder.smart.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class CameraSearchListAdapter extends RecyclerView.Adapter<CameraSearchListAdapter.SearchViewHolder>{

    private List<SearchBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private AddClickListener addClickListener;



    public CameraSearchListAdapter(Context context, List<SearchBean> datas, AddClickListener addClickListener){
        this. mContext=context;
        this. mDatas=datas;
        this.addClickListener = addClickListener;
        inflater= LayoutInflater. from(mContext);
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.search_camera_list_item,viewGroup, false);
        SearchViewHolder viewHolder = new SearchViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder searchViewHolder, int position) {
        SearchBean searchBean = mDatas.get(position);
        BCamera camera = searchBean.getCamera();
        searchViewHolder.name_txt.setText(camera.getCameraBean().getDevName());
        searchViewHolder.did_txt.setText(camera.getCameraBean().getDevID());
        searchViewHolder.add_iv.setOnClickListener(new AddListener(searchBean));

    }


    private class AddListener implements View.OnClickListener
    {
        private SearchBean searchBean;
        public AddListener(SearchBean searchBean)
        {
            this.searchBean = searchBean;
        }

        @Override
        public void onClick(View v) {
            if(addClickListener != null)
            {
                addClickListener.onAddClick(searchBean.getCamera());
            }
        }
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView name_txt;
        TextView did_txt;
        ImageView add_iv;
        public SearchViewHolder(View view) {
            super(view);
            name_txt  = (TextView)view.findViewById(R.id.name_txt);
            did_txt   = (TextView)view.findViewById(R.id.did_txt);
            add_iv    = (ImageView)view.findViewById(R.id.add_iv);
        }
    }
    public interface AddClickListener
    {
        void onAddClick(BCamera camera);
    }
}
