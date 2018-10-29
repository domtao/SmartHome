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
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.adapter.FileTimeListAdapter;
import com.zunder.smart.remote.dialog.TimelistAlert;
import com.zunder.smart.remote.model.FileTime;
import com.zunder.smart.remote.model.FileTimeList;
import com.zunder.smart.remote.webservice.FileTimeListServiceUtils;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 
 */
public class TimeListActivity extends Activity implements OnClickListener {
	private SwipeRefreshLayout freshlayout;
	SwipeMenuRecyclerView listView;
	TimeListActivity activity;
	private List<FileTimeList> list = new ArrayList<FileTimeList>();
	FileTimeListAdapter adapter;
	private static String title = "时间段";
	TextView titleView;
	TextView  tipTxt,editeTxt;
	private static int timeId=0;
	TextView back;
	static FileTime fileTime;
	public static void startActivity(Activity activity, FileTime _fileTime) {
		fileTime=_fileTime;
		Intent intent = new Intent(activity, TimeListActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.timelist_activity);
		activity = this;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// startTime();
		freshlayout.setRefreshing(true);
		new DataAsyncTask().execute();
	}

	private void initView() {
		if(fileTime!=null){
			title=fileTime.getStartTime()+"--"+fileTime.getEndTime();
			timeId=fileTime.getId();
		}
		tipTxt = (TextView) findViewById(R.id.tipTxt);
		titleView = (TextView) findViewById(R.id.titleTxt);
		titleView.setText(title);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		editeTxt.setOnClickListener(this);
		titleView.setText(title);

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
		if(GateWayFactory.getInstance().isAdmin(RemoteMainActivity.deviceID)) {
			editeTxt.setVisibility(View.VISIBLE);
			listView.setSwipeMenuCreator(swipeMenuCreator);
			listView.setSwipeMenuItemClickListener(menuItemClickListener);
		}

		back=(TextView)findViewById(R.id.backTxt);

		back.setOnClickListener(this);


	}
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height60);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
			SwipeMenuItem wechatItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText( getString(R.string.delete))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(wechatItem);// 添加一个按钮到右侧菜单。

			SwipeMenuItem closeItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.orange)
					.setText( getString(R.string.edit))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。
		}
	};
	private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
		@Override
		public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
			closeable.smoothCloseMenu();// 关闭被点击的菜单。
			if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
				switch (menuPosition){
					case 0:
						DialogAlert alert = new DialogAlert(activity);

						alert.init(activity.getString(R.string.tip),
								"是否删除"+list.get(adapterPosition).getControlTime());
						alert.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								final int id = list.get(adapterPosition).getId();
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										try {

											String result= FileTimeListServiceUtils.delFileTimeList(list.get(adapterPosition).getId()).replace("[","").replace("]","");
											ResultInfo resultInfo = (ResultInfo) JSONHelper.parseObject(result,
													ResultInfo.class);
											if(resultInfo.getResultCode()==0) {
												ToastUtils.ShowError(activity, resultInfo.getMsg(), Toast.LENGTH_SHORT, true);
											}else{
												ToastUtils.ShowSuccess(activity,resultInfo.getMsg(),Toast.LENGTH_SHORT,true);
												new DataAsyncTask().execute();
											}
										}catch (Exception e){
										}
									}
								});

							}
							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						alert.show();
						break;
					case 1:
						TimelistAlert timelistAlert=new TimelistAlert(activity,list.get(adapterPosition));
						timelistAlert.setInit(timeId,fileTime.getCycle(),fileTime.getStartTime(),fileTime.getEndTime(),fileTime.getAssignDate());
						timelistAlert.setOnSureListener(new TimelistAlert.OnSureListener() {
							@Override
							public void onCancle() {
							}
							@Override
							public void onSure() {
								String result = ISocketCode.setForwardRemoteControl("timming|定时播放",
										RemoteMainActivity.deviceID, 0);
								MainActivity.getInstance().sendCode(result);
								freshlayout.setRefreshing(true);
								new DataAsyncTask().execute();
							}
						});
						timelistAlert.show();
						break;

				}
			}
		}
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.editeTxt:
			TimelistAlert timelistAlert=new TimelistAlert(activity,null);
			timelistAlert.setInit(timeId,fileTime.getCycle(),fileTime.getStartTime(),fileTime.getEndTime(),fileTime.getAssignDate());
			timelistAlert.setOnSureListener(new TimelistAlert.OnSureListener() {
				@Override
				public void onCancle() {
				}
				@Override
				public void onSure() {
					String result = ISocketCode.setForwardRemoteControl("timming|定时播放",
							RemoteMainActivity.deviceID, 0);
					MainActivity.getInstance().sendCode(result);
					freshlayout.setRefreshing(true);
					new DataAsyncTask().execute();
				}
			});
			timelistAlert.show();
			break;
			case R.id.backTxt:
				finish();
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
		ReceiverBroadcast.setRemoteControlListener(null);
	}
	public class DataAsyncTask extends AsyncTask<String, Void, List<FileTimeList>> {

		@Override
		protected void onPreExecute() {
			list.clear();
		}
		@Override
		protected List<FileTimeList> doInBackground(String... params) {
			List<FileTimeList> result = null;
			try {
				String resultParam = FileTimeListServiceUtils.getFileTimeLists(timeId);
				result = (List<FileTimeList>) JSONHelper.parseCollection(
						resultParam, List.class, FileTimeList.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return result;
		}
		@Override
		protected void onPostExecute(List<FileTimeList> result) {
			if (freshlayout.isRefreshing()) {
				freshlayout.setRefreshing(false);
			}
			if (result != null && result.size() > 0) {

				list=result;
			} else {
					ToastUtils.ShowError(activity,getString(R.string.noupdata),Toast.LENGTH_SHORT,true);
			}
			adapter = new FileTimeListAdapter(activity, list);
			adapter.setOnItemClickListener(mOnItemClickListener);
			listView.setAdapter(adapter);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		freshlayout.setRefreshing(true);
		new DataAsyncTask().execute();
	}

	public void back() {
		finish();
	}
	private OnItemClickListener mOnItemClickListener=new OnItemClickListener() {
		@Override
		public void onItemClick(int position) {
//			iFileInfo=list.get(position);
//			play(list.get(position).getFileName(),
//					list.get(position).getFileUrl(), position,list.get(position).getTypeId());
		}
	};
}