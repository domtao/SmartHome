package com.zunder.smart.remote.fragment;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.model.IFileInfo;
import com.zunder.smart.remote.activity.IFileInfoActivity;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.adapter.FileTypeAdapter;
import com.zunder.smart.remote.model.FileType;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ProgressDialogUtils;
import com.zunder.smart.remote.webservice.FileTypeServiceUtils;
import com.zunder.smart.remote.webservice.IFileInfoServiceUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.smart.model.IFileInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.activity.IFileInfoActivity;
import com.zunder.smart.remote.adapter.FileTypeAdapter;
import com.zunder.smart.remote.model.FileType;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;

public class FileTypeFragment extends Fragment {
	private SwipeRefreshLayout freshlayout;
	private Activity activity;
	private GridView fileTypeGrid;
	FileTypeAdapter adapter;
	private List<FileType> list=new ArrayList<FileType>();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		View root = inflater.inflate(R.layout.activity_filetype, container, false);
		freshlayout = (SwipeRefreshLayout) root.findViewById(R.id.freshlayout);
		freshlayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW);
		freshlayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#BBFFFF"));
		freshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
			@Override
			public void onRefresh() {
				new TypeTask().execute();
			}
		});
		fileTypeGrid = (GridView) root.findViewById(R.id.fileTypeGrid);
		fileTypeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				IFileInfoActivity.startActivity(activity,list.get(position).getTypeName(), RemoteMainActivity.deviceID,list.get(position).getTypeId());
			}
		});

		return root;
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(freshlayout!=null) {
			freshlayout.setRefreshing(true);
		}
		new TypeTask().execute();
	}


	class TypeTask extends AsyncTask<String, Void, List<FileType>> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<FileType> doInBackground(String... params) {
			List<FileType> result = null;
			try {
				String resultParam = FileTypeServiceUtils.getFileTypes(1,10);
				result = (List<FileType>) JSONHelper.parseCollection(
						resultParam, List.class, FileType.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		@Override
		protected void onPostExecute(List<FileType> result) {
			if (freshlayout.isRefreshing()) {
				freshlayout.setRefreshing(false);
			}
			if (result != null && result.size() > 0) {
				list=result;
				adapter = new FileTypeAdapter(activity, list);
				fileTypeGrid.setAdapter(adapter);
			}
		}
	}
	class DataTask extends AsyncTask<String, Void, List<IFileInfo>> {

		private int typeId;
		public DataTask(int typeId){
			this.typeId=typeId;
		}

		@Override
		protected void onPreExecute() {
			ProgressDialogUtils.showProgressDialog(activity,"正在获取数据");
		}
		@Override
		protected List<IFileInfo> doInBackground(String... params) {
			List<IFileInfo> result = null;
			try {
				String resultParam = IFileInfoServiceUtils.getIFileInfos(RemoteMainActivity.deviceID,typeId,1,10);
				result = (List<IFileInfo>) JSONHelper.parseCollection(
						resultParam, List.class, IFileInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<IFileInfo> result) {
			ProgressDialogUtils.dismissProgressDialog();
			if (result != null && result.size() > 0) {
			}else{
				ToastUtils.ShowError(activity,"没有文件", Toast.LENGTH_SHORT,true);
			}
		}
	}
}
