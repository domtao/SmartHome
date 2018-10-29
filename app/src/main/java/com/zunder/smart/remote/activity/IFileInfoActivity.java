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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.MusicPlayMedia;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.listener.RemoteControlListener;
import com.zunder.smart.model.IFileInfo;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.remote.adapter.IFileInfoAdapter;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.view.ListViewDecoration;
import com.zunder.smart.remote.webservice.IFileInfoServiceUtils;
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
public class IFileInfoActivity extends Activity implements OnClickListener,RemoteControlListener {
	private SwipeRefreshLayout freshlayout;
	SwipeMenuRecyclerView listView;
	IFileInfoActivity activity;
	private List<IFileInfo> list = new ArrayList<IFileInfo>();
	IFileInfoAdapter adapter;
	private static int  rtypeId ;
	private static String title = "音乐";
	private static String masterID="";
	TextView titleView;
	TextView  tipTxt;
	int index = 0;
	private LinearLayout musicPre, musicNext,allScreen,back,forward,musicStop,musicPlay,backTxt,musicThrink,musicMagnify,bootLayput;

	public static void startActivity(Activity activity,String _title, String _masterID,int _typedId) {
		masterID=_masterID;
		rtypeId = _typedId;
		title = _title;
		Intent intent = new Intent(activity, IFileInfoActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.ifile_activity);
		activity = this;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		ReceiverBroadcast.setRemoteControlListener(this);
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// startTime();
		freshlayout.setRefreshing(true);
		new DataAsyncTask(1).execute();
	}

	private void initView() {
		tipTxt = (TextView) findViewById(R.id.tipTxt);

		titleView = (TextView) findViewById(R.id.titleTxt);
		musicPre = (LinearLayout) findViewById(R.id.musicPre);
		musicPlay= (LinearLayout) findViewById(R.id.musicPlay);
		musicStop = (LinearLayout) findViewById(R.id.musicStop);
		musicNext = (LinearLayout) findViewById(R.id.musicNext);
		freshlayout = (SwipeRefreshLayout) findViewById(R.id.freshlayout);
		bootLayput= (LinearLayout) findViewById(R.id.bootLayput);
		freshlayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW);
		freshlayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#BBFFFF"));
		freshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
			@Override
			public void onRefresh() {
				new DataAsyncTask(1).execute();
			}
		});
		listView = (SwipeMenuRecyclerView) findViewById(R.id.songList);
		listView.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		if(GateWayFactory.getInstance().isAdmin(RemoteMainActivity.deviceID)) {
			listView.setSwipeMenuCreator(swipeMenuCreator);
			listView.setSwipeMenuItemClickListener(menuItemClickListener);
		}
		musicPre.setOnClickListener(this);
		musicNext.setOnClickListener(this);

		musicPlay.setOnClickListener(this);
		musicStop.setOnClickListener(this);
		allScreen=(LinearLayout)findViewById(R.id.allscreen);
		back=(LinearLayout)findViewById(R.id.back);
		forward=(LinearLayout)findViewById(R.id.forward);
		backTxt=(LinearLayout)findViewById(R.id.musicBack);
		musicThrink=(LinearLayout)findViewById(R.id.musicThrink);
		musicMagnify=(LinearLayout)findViewById(R.id.musicMagnify);
		allScreen.setOnClickListener(this);
		back.setOnClickListener(this);
		forward.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		musicThrink.setOnClickListener(this);
		musicMagnify.setOnClickListener(this);
	}
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

				int hSize = getResources().getDimensionPixelSize(R.dimen.item_height60);
				int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
				SwipeMenuItem wechatItem = new SwipeMenuItem(activity)
						.setBackgroundDrawable(R.color.red)
						.setText(getString(R.string.delete))
						.setWidth(wSize)
						.setHeight(hSize);
				swipeRightMenu.addMenuItem(wechatItem);// 添加一个按钮到右侧菜单。

				SwipeMenuItem closeItem = new SwipeMenuItem(activity)
						.setBackgroundDrawable(R.color.orange)
						.setText(getString(R.string.edit))
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
								"是否删除"+list.get(adapterPosition).getFileName());
						alert.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								final int id = list.get(adapterPosition).getId();
								final String masterId=list.get(adapterPosition).getMasterID();
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										try {

											String result= IFileInfoServiceUtils.deleteIFileInfo(id,masterId).replace("[","").replace("]","");
											ResultInfo resultInfo = (ResultInfo) JSONHelper.parseObject(result,
													ResultInfo.class);
											if(resultInfo.getResultCode()==0) {
												ToastUtils.ShowError(activity, resultInfo.getMsg(), Toast.LENGTH_SHORT, true);
											}else{
												ToastUtils.ShowSuccess(activity,resultInfo.getMsg(),Toast.LENGTH_SHORT,true);
												new DataAsyncTask(1).execute();
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

				}
			}
		}
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.musicBack:
			back();
			break;
		case R.id.musicPre:
			if (index <= 0) {
				ToastUtils.ShowSuccess(activity, getString(R.string.isOne), Toast.LENGTH_SHORT,true);
			} else {
				index--;
				play(list.get(index).getFileName(), list.get(index).getFileUrl(), index,list.get(index).getTypeId());
			}
			break;
			case R.id.musicPlay:
			{
				String result = ISocketCode.setForwardRemoteControl("pause|暂停播放",
						masterID,rtypeId);
				MainActivity.getInstance().sendCode(result);
			//	ToastUtils.ShowSuccess(activity, "暂停播放", Toast.LENGTH_SHORT, true);

			}
			break;
		case R.id.musicStop:
		{
			String result = ISocketCode.setForwardRemoteControl("stop|"+getString(R.string.stopPlay),
						masterID,0);
				MainActivity.getInstance().sendCode(result);
				ToastUtils.ShowSuccess(activity, getString(R.string.stopPlay), Toast.LENGTH_SHORT, true);
			}
			break;
		case R.id.musicNext:
			if ( list.size()<=index) {
				ToastUtils.ShowSuccess(activity, getString(R.string.isLast), Toast.LENGTH_SHORT,true);
			} else {
				play(list.get(index).getFileName(), list.get(index).getFileUrl(), index,list.get(index).getTypeId());
				index++;
			}
			break;
			case R.id.allscreen:{
				String result = ISocketCode.setForwardRemoteControl("allScreen|全屏播放",
						RemoteMainActivity.deviceID,rtypeId);
				MainActivity.getInstance().sendCode(result);
			//	ToastUtils.ShowSuccess(activity, "全屏播放", Toast.LENGTH_SHORT, true);
			}
			break;
			case R.id.back:{
				String result = ISocketCode.setForwardRemoteControl("down|后退",
						RemoteMainActivity.deviceID,rtypeId);
				MainActivity.getInstance().sendCode(result);
				//ToastUtils.ShowSuccess(activity, "后退", Toast.LENGTH_SHORT, true);
			}
			break;
			case R.id.forward:{
				String result = ISocketCode.setForwardRemoteControl("up|快进",
						RemoteMainActivity.deviceID,rtypeId);
				MainActivity.getInstance().sendCode(result);
				//ToastUtils.ShowSuccess(activity, "快进", Toast.LENGTH_SHORT, true);
			}
			case R.id.musicThrink:{
				String result = ISocketCode.setForwardRemoteControl("thrink|缩小",
						RemoteMainActivity.deviceID,rtypeId);
				MainActivity.getInstance().sendCode(result);
			//	ToastUtils.ShowSuccess(activity, "缩小", Toast.LENGTH_SHORT, true);
			}
			break;
			case R.id.musicMagnify:{
				String result = ISocketCode.setForwardRemoteControl("magnify|放大",
						RemoteMainActivity.deviceID,rtypeId);
				MainActivity.getInstance().sendCode(result);
			//	ToastUtils.ShowSuccess(activity, "放大", Toast.LENGTH_SHORT, true);
			}
			break;
		default:
			break;
		}
	}
	public void play(String titleName, String url, int _index,int _playType) {
		try {
			index=_index;
			tipTxt.setText(getString(R.string.playing) + titleName);
			if(_playType==0) {
				MusicPlayMedia.getInstansMedia().play(url);
			}else {
				String result = ISocketCode.setForwardRemoteControl("url|" +url,
						masterID,_playType);
					MainActivity.getInstance().sendCode(result);
			}
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public void setMsg(final int type, final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(type==0) {
					ToastUtils.ShowError(activity, msg, Toast.LENGTH_SHORT, true);
				}else{
					ToastUtils.ShowSuccess(activity, msg, Toast.LENGTH_SHORT, true);
				}
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ReceiverBroadcast.setRemoteControlListener(null);
	}
	public class DataAsyncTask extends AsyncTask<String, Void, List<IFileInfo>> {
		private int pageNum;

		DataAsyncTask(int _pageNum) {
			this.pageNum = _pageNum;
		}
		@Override
		protected void onPreExecute() {
		}
		@Override
		protected List<IFileInfo> doInBackground(String... params) {
			List<IFileInfo> result = null;
			try {
				String resultParam = IFileInfoServiceUtils.getIFileInfos(masterID,rtypeId,pageNum,10);
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
				bootLayput.setVisibility(View.VISIBLE);
				list=result;
				adapter = new IFileInfoAdapter(activity, list);
					adapter.setOnItemClickListener(mOnItemClickListener);
				listView.setAdapter(adapter);
			} else {
				if (list.size() == 0) {
					bootLayput.setVisibility(View.GONE);
					ToastUtils.ShowError(activity,getString(R.string.noupdata),Toast.LENGTH_SHORT,true);
				}
			}

		}
	}

	public void back() {
		finish();
	}
	private OnItemClickListener mOnItemClickListener=new OnItemClickListener() {
		@Override
		public void onItemClick(int position) {
			play(list.get(position).getFileName(),
					list.get(position).getFileUrl(), position,list.get(position).getTypeId());
		}
	};
}