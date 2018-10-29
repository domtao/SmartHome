/**
 * 
 */
package com.zunder.smart.remote.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.model.IFileInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.adapter.FileTypeDialogAdapter;
import com.zunder.smart.remote.adapter.SelectFileAdapter;
import com.zunder.smart.remote.model.FileType;
import com.zunder.smart.remote.webservice.FileListServiceUtils;
import com.zunder.smart.remote.webservice.FileTypeServiceUtils;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ProgressDialogUtils;
import com.zunder.smart.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 * 
 */
public class SelectFileActivity extends Activity implements OnClickListener {
	private SwipeRefreshLayout freshlayout;
	SwipeMenuRecyclerView listView;
	SelectFileActivity activity;
	private List<IFileInfo> list = new ArrayList<IFileInfo>();
	SelectFileAdapter adapter;
	TextView backTxt;
	Spinner fileTypeSpinner;
	static int userId=0;
	int typeId=0;

	private Button checkAll,submit;
	public static void startActivity(Activity activity,int _userId) {
		userId=_userId;
		Intent intent = new Intent(activity, SelectFileActivity.class);
		activity.startActivityForResult(intent,100);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.select_file_activity);
		activity = this;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		initView();
		new FileTypeTask().execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initView() {
		backTxt=(TextView)findViewById(R.id.backTxt) ;
		backTxt.setOnClickListener(this);
		freshlayout = (SwipeRefreshLayout) findViewById(R.id.freshlayout);

		freshlayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW);
		freshlayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#BBFFFF"));
		freshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
			@Override
			public void onRefresh() {
				new DataAsyncTask().execute();
			}
		});
		listView = (SwipeMenuRecyclerView) findViewById(R.id.songList);
		listView.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		fileTypeSpinner=(Spinner) findViewById(R.id.fileType);
		fileTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {
				typeId=((FileType)parent.getItemAtPosition(pos)).getId();
				new DataAsyncTask().execute();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});

				checkAll=(Button)findViewById(R.id.checkAll);
		submit=(Button)findViewById(R.id.submit);
		checkAll.setOnClickListener(this);
		submit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				back();
			case R.id.checkAll:
				if(adapter!=null){
					adapter.muCheck();
				}
				break;
			case R.id.submit:

				if(list!=null&&list.size()>0) {
					new SendDataAsyncTask().execute();

				}
				break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			back();
		}
		return super.onKeyDown(keyCode, event);
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public class FileTypeTask extends AsyncTask<String, Void, List<FileType>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<FileType> doInBackground(String... params) {
			List<FileType>  list = null;
			try {
				String json = FileTypeServiceUtils.getFileTypes(1,10);
				list = (List<FileType>) JSONHelper.parseCollection(json,
						List.class, FileType.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return list;
		}
		@Override
		protected void onPostExecute(List<FileType> result) {

			if (result != null&&result.size()>0) {
				if(result.size()>0) {
					FileTypeDialogAdapter userTypeAdapter = new FileTypeDialogAdapter(activity, result);
					fileTypeSpinner.setAdapter(userTypeAdapter);
				}
			}
		}
	}
	public class DataAsyncTask extends AsyncTask<String, Void, List<IFileInfo>> {

		@Override
		protected void onPreExecute() {
			list.clear();
		}
		@Override
		protected List<IFileInfo> doInBackground(String... params) {
			List<IFileInfo> result = null;
			try {
				String resultParam = FileListServiceUtils.getFindFileByType(typeId,userId, RemoteMainActivity.deviceID);
				result = (List<IFileInfo>) JSONHelper.parseCollection(
						resultParam, List.class, IFileInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return result;
		}
		@Override
		protected void onPostExecute(List<IFileInfo> result) {
			if (freshlayout.isRefreshing()) {
				freshlayout.setRefreshing(false);
			}
			if (result != null && result.size() > 0) {
				list=result;
			} else {
			ToastUtils.ShowError(activity,"该类型的数据已添加到用户列表",Toast.LENGTH_SHORT,true);
			}
			adapter = new SelectFileAdapter(activity, list);
			listView.setAdapter(adapter);
		}
	}
	public void back() {
		finish();
	}
	public class SendDataAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			ProgressDialogUtils.showProgressDialog(activity,"开始上传数据");
		}
		@Override
		protected String doInBackground(String... params) {
			String  result = null;
			try {
				HashMap<Integer, Boolean> isSelected = SelectFileAdapter.getIsSelected();
				for (int i = 0; i < list.size(); i++) {
					if (isSelected.get(i) == true) {
						String resultParam = FileListServiceUtils.insertFileList(userId,list.get(i).getId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			ProgressDialogUtils.dismissProgressDialog();
			ToastUtils.ShowSuccess(activity,"添加成功!",Toast.LENGTH_SHORT,true);
			new DataAsyncTask().execute();
		}
	}

}