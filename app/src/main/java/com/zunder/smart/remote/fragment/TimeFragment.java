package com.zunder.smart.remote.fragment;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.activity.FileListActivity;
import com.zunder.smart.remote.activity.TimeListActivity;
import com.zunder.smart.remote.adapter.FileTimeAdapter;
import com.zunder.smart.remote.adapter.FileUserAdapter;
import com.zunder.smart.remote.dialog.FileUserAlert;
import com.zunder.smart.remote.dialog.TimeAlert;
import com.zunder.smart.remote.model.FileTime;
import com.zunder.smart.remote.model.FileUser;
import com.zunder.smart.remote.webservice.FileTimeServiceUtils;
import com.zunder.smart.remote.webservice.FileUserServiceUtils;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.activity.TimeListActivity;
import com.zunder.smart.remote.adapter.FileTimeAdapter;
import com.zunder.smart.remote.dialog.TimeAlert;
import com.zunder.smart.remote.model.FileTime;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.view.ListViewDecoration;

import java.util.ArrayList;
import java.util.List;

public class TimeFragment extends Fragment {
	private Activity activity;
	private SwipeRefreshLayout freshlayout;
	private SwipeMenuRecyclerView listView;
	FileTimeAdapter adapter;
	List<FileTime> list=new ArrayList<FileTime>();
	TextView edit;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		activity = getActivity();
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		View root = inflater.inflate(R.layout.fragment_time, container, false);
		freshlayout = (SwipeRefreshLayout) root.findViewById(R.id.freshlayout);
		edit=(TextView)root.findViewById(R.id.editeTxt);
		freshlayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW);
		freshlayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#BBFFFF"));
		freshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
			@Override
			public void onRefresh() {
				new FileTimeTask().execute();
			}
		});
		//手动调用,通知系统去测量
//        mSwipeRefreshLayout.measure(0,0);
		listView = (SwipeMenuRecyclerView) root.findViewById(R.id.userList);
		listView.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		if(GateWayFactory.getInstance().isAdmin(RemoteMainActivity.deviceID)) {
			listView.setSwipeMenuCreator(swipeMenuCreator);
			listView.setSwipeMenuItemClickListener(menuItemClickListener);
			edit.setVisibility(View.VISIBLE);
		}

		edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final TimeAlert fileUserAlert=new TimeAlert(activity,list,null);
				fileUserAlert.setOnSureListener(new TimeAlert.OnSureListener() {
					@Override
					public void onCancle() {
					}

					@Override
					public void onSure() {
						new FileTimeTask().execute();
						fileUserAlert.dismiss();
					}
				});
				fileUserAlert.show();

			}
		});
		return root;
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

                        alert.init( activity.getString(R.string.tip),
                               "是否删除时间段["+list.get(adapterPosition).getStartTime()+"]--["+list.get(adapterPosition).getEndTime()+"]的数据");
                        alert.setSureListener(new DialogAlert.OnSureListener() {

                            @Override
                            public void onSure() {
                                // TODO Auto-generated method stub
                                final int id = list.get(adapterPosition).getId();
                               activity.runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       try {

                                     String result=  FileTimeServiceUtils.delFileTime(id).replace("[","").replace("]","");
                                       ResultInfo resultInfo = (ResultInfo) JSONHelper.parseObject(result,
                                               ResultInfo.class);
                                           if(resultInfo.getResultCode()==0) {
                                               ToastUtils.ShowError(activity, resultInfo.getMsg(), Toast.LENGTH_SHORT, true);
                                           }else{
                                               ToastUtils.ShowSuccess(activity,resultInfo.getMsg(),Toast.LENGTH_SHORT,true);
                                               new FileTimeTask().execute();
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
						final TimeAlert fileUserAlert=new TimeAlert(activity,list,list.get(adapterPosition));
						fileUserAlert.setOnSureListener(new TimeAlert.OnSureListener() {
							@Override
							public void onCancle() {
							}
							@Override
							public void onSure() {
								new FileTimeTask().execute();
								fileUserAlert.dismiss();
							}
						});
						fileUserAlert.show();
						break;

				}
			}
		}
	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(freshlayout!=null) {
			freshlayout.setRefreshing(true);
		}
		new FileTimeTask().execute();
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
		TimeListActivity.startActivity(activity,list.get(position));
		}
	};
	class FileTimeTask extends AsyncTask<String, Void, List<FileTime>> {
		@Override
		protected void onPreExecute() {
			list.clear();
		}

		@Override
		protected List<FileTime> doInBackground(String... params) {
			List<FileTime> result = null;
			try {
				String resultParam = FileTimeServiceUtils.getFileTimes(RemoteMainActivity.deviceID);
				result = (List<FileTime>) JSONHelper.parseCollection(
						resultParam, List.class, FileTime.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		@Override
		protected void onPostExecute(List<FileTime> result) {
//			ProgressDialogUtils.dismissProgressDialog();
			if (freshlayout.isRefreshing()) {
				freshlayout.setRefreshing(false);
			}
			if (result != null && result.size() > 0) {
				list = result;
			}
				adapter = new FileTimeAdapter(activity, list);
				adapter.setOnItemClickListener(onItemClickListener);
				listView.setAdapter(adapter);
		}
	}
}
