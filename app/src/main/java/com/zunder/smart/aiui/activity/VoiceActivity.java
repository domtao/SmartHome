/**
 * 
 */
package com.zunder.smart.aiui.activity;

import java.util.ArrayList;
import java.util.List;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.adapter.VoiceAdapter;
import com.zunder.smart.aiui.adapter.VoiceInfoAdapter;
import com.zunder.smart.aiui.info.SubscribeInfo;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.listener.GetDeviceListener;
import com.zunder.smart.listview.SwipyRefreshLayout;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.model.VoiceInfo;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.view.ListViewDecoration;
import com.zunder.smart.webservice.forward.VoiceServiceUtils;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Administrator
 * 
 */
public class VoiceActivity extends Activity implements
		OnClickListener,GetDeviceListener,SwipyRefreshLayout.OnRefreshListener {

	private TextView edite;
	SwipeMenuRecyclerView listView;
	VoiceActivity activity;
	TextView titleTxt,backTxt;
	private List<VoiceInfo> list = new ArrayList<VoiceInfo>();
	VoiceInfoAdapter adapter;
	VoiceAdapter voiceAdapter;
	private SharedPreferences spf;
	private RightMenu rightButtonMenuAlert;
	private int  EditeMode=0;
	WarnDialog warnDialog = null;
	SwipyRefreshLayout  refreshLayout;
	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, VoiceActivity.class);
		activity.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		spf = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		ReceiverBroadcast.setGetDeviceListener(this);
		setContentView(R.layout.activity_voice);
		activity = this;
		initView();
		titleTxt.setText("云知声问答");
	}
	public void showDialog(){

		if (warnDialog == null) {
			warnDialog = new WarnDialog(activity, getString(R.string.search));
			LayoutInflater warnDialog_inflater = getLayoutInflater();
			warnDialog.setMessage(getString(R.string.get_answer_info)+" 10");

			warnDialog.setSureListener(new WarnDialog.OnSureListener() {
				@Override
				public void onCancle() {
					searchflag = false;
					warnDialog.dismiss();
				}
			});
		}
		warnDialog.show();
		startTime();
	}
	private boolean searchflag = false;
	private int startCount = 0;

	private void startTime() {
		startCount=0;
		searchflag=true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(1000);
						startCount++;
						if (startCount >= 10) {
							searchflag = false;
							if ((warnDialog != null) && warnDialog.isShowing()) {
								warnDialog.dismiss();
								handler.sendEmptyMessage(-1);
							}
						}else{
							Message message = handler.obtainMessage();
							message.what = -2;
							message.obj = startCount;
							handler.sendMessage(message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// startTime();
		onRefresh();
	}

	private void initView() {
		backTxt=(TextView)findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		titleTxt=(TextView)findViewById(R.id.titleTxt);
		edite = (TextView) findViewById(R.id.edite);
		edite.setOnClickListener(this);
		listView = (SwipeMenuRecyclerView) findViewById(R.id.ListView);

		listView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。

		listView.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。
		listView.setSwipeMenuItemClickListener(menuItemClickListener);

		refreshLayout= (SwipyRefreshLayout) findViewById(R.id.refreshLayout);
		refreshLayout.setOnRefreshListener(this);

		if (AiuiMainActivity.gateWay.getUserName().equals("admin")) {
			initRightButtonMenuAlert();
		} else {
			edite.setVisibility(View.GONE);
		}

	}

	private void initRightButtonMenuAlert() {

		rightButtonMenuAlert = new RightMenu(activity, R.array.voicelists,
				R.drawable.devicelist_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {
			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub

				switch (position) {
					case 0:
						titleTxt.setText(getString(R.string.ddanswer));
						EditeMode=0;
						onRefresh();
						break;
					case 1:
						titleTxt.setText(getString(R.string.cloundanswer));
						EditeMode=1;
						onRefresh();
						break;
					case 2:
						VoiceAddActivity.startActivity(activity,null,0);
						break;
					case 3:
						clear();
					break;
				}
				rightButtonMenuAlert.dismiss();
			}
		});

	}

	public void clear() {
		DialogAlert alert = new DialogAlert(activity);
		alert.init(getString(R.string.tip), getString(R.string.isCleanVoice));
		alert.setSureListener(new DialogAlert.OnSureListener() {
			@Override
			public void onSure() {
				// TODO Auto-generated method stub

				String result = ISocketCode.setDelAllVoice("del",
						AiuiMainActivity.deviceID);
				MainActivity.getInstance().sendCode(result);
			}

			@Override
			public void onCancle() {
				// TODO Auto-generated method stub
			}
		});
		alert.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.edite:
			rightButtonMenuAlert.show(edite);
			break;
			case R.id.backTxt:
				finish();
				break;
		default:
			break;
		}

	}

	public void onRefresh() {
		// TODO Auto-generated method stub
		list.clear();

		showDialog();
		if(EditeMode==0) {
			adapter = new VoiceInfoAdapter(activity, list);
			listView.setAdapter(adapter);
			String result = ISocketCode.setGetVoice("getVoice",
					AiuiMainActivity.deviceID);
			MainActivity.getInstance().sendCode(result);
		}else {
			voiceAdapter = new VoiceAdapter(activity, list);
			listView.setAdapter(voiceAdapter);
			new VoiceAsyncTask(1).execute();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ReceiverBroadcast.setGetDeviceListener(null);
	}

	public void hideProgressDialog() {
		if ((warnDialog != null) && warnDialog.isShowing()) {
			refreshLayout.setRefreshing(false);
			searchflag = false;
			startCount=0;
			warnDialog.dismiss();
		}
	}

	@Override
	public void getDevice(Device device) {

	}

	@Override
	public void getMode(Mode mode) {

	}

	@Override
	public void getMessage(String info) {
		Message message = handler.obtainMessage();
		message.what = 0;
		message.obj = info;
		handler.sendMessage(message);
	}

	@Override
	public void getVoice(VoiceInfo voiceInfo) {
		Message message = handler.obtainMessage();
		message.what = 1;
		message.obj = voiceInfo;
		handler.sendMessage(message);
	}

	@Override
	public void onRefresh(int index) {
		list.clear();
		showDialog();
		if(EditeMode==0) {
			adapter = new VoiceInfoAdapter(activity, list);
			listView.setAdapter(adapter);
			String result = ISocketCode.setGetVoice("getVoice",
					AiuiMainActivity.deviceID);
			MainActivity.getInstance().sendCode(result);
		}else {
			voiceAdapter = new VoiceAdapter(activity, list);
			listView.setAdapter(voiceAdapter);
			new VoiceAsyncTask(1).execute();
		}
	}
	@Override
	public void onLoad(int index) {
		if(EditeMode!=0) {
			new VoiceAsyncTask(index).execute();
		}
	}

	public class VoiceAsyncTask extends
			AsyncTask<String, Void, List<VoiceInfo>> {
		private int pageNum;

		VoiceAsyncTask(int _pageNum) {
			this.pageNum = _pageNum;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<VoiceInfo> doInBackground(String... params) {
			List<VoiceInfo> listUser = null;
			try {
				String json = VoiceServiceUtils.getVoice(
						spf.getString("userName", ""), pageNum, 10);
				listUser = (List<VoiceInfo>) JSONHelper.parseCollection(json,
						List.class, VoiceInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return listUser;
		}
		@Override
		protected void onPostExecute(List<VoiceInfo> result) {
			hideProgressDialog();
			refreshLayout.setRefreshing(false);
			if (result != null && result.size() > 0) {
				list.addAll(result);
				voiceAdapter.notifyDataSetChanged();
			} else {
				if (list.size() == 0) {
					TipAlert verifyAlert = new TipAlert(activity,
							getResources().getString(R.string.tip),getString(R.string.noupdata));
					verifyAlert.show();
				}
			}
		}
	}
	private Handler handler=new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what){
				case -2:
			  refreshLayout.setRefreshing(false);
					int index=Integer.parseInt(msg.obj.toString());
					warnDialog.setMessage(getString(R.string.get_answer_info)+" "+(10-index));

					break;
				case -1:
					refreshLayout.setRefreshing(false);
					TipAlert errAlert = new TipAlert(activity,
							getString(R.string.tip),getString(R.string.pass));
					errAlert.show();
					break;
				case 0:
					if ((warnDialog != null) && warnDialog.isShowing()) {
						searchflag = false;
						startCount=0;
						warnDialog.dismiss();
					}
					TipAlert alert = new TipAlert(activity,
							getString(R.string.tip), msg.obj.toString());
					alert.show();
					break;
				case 1:
					hideProgressDialog();
					list.add((VoiceInfo) msg.obj);
					adapter.notifyDataSetChanged();
					break;
			}
		}
	};
	public void getModeList(String  param){}

	@Override
	public void getSubscribe(SubscribeInfo subscribeInfo) {

	}
	private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
		@Override
		public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
			closeable.smoothCloseMenu();// 关闭被点击的菜单。
			if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
				final VoiceInfo device=adapter.getItems().get(adapterPosition);
				switch (menuPosition){
					case 0:
						if(EditeMode==0){
							VoiceAddActivity.startActivity(activity,device,0);
						}
						else{
							VoiceAddActivity.startActivity(activity,device,1);
						}
						break;
					case 1:
						DialogAlert alert = new DialogAlert(activity);
						alert.init(activity.getString(R.string.is_Del),device.getVoiceName());
						alert.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								if(EditeMode==0) {
									String result = ISocketCode.setDelVoice(device.getId() + "", AiuiMainActivity.deviceID);
									MainActivity.getInstance().sendCode(result);
									list.remove(adapterPosition);
									adapter.notifyDataSetChanged();
								}else{
									new VoiceDelAsyncTask().execute(device.getId() + "");
									list.remove(adapterPosition);
									voiceAdapter.notifyDataSetChanged();
								}
							}
							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						alert.show();
						break;

					case 2:

						break;
					case 3:

						break;
					default:
						break;
				}

			}
		}
	};

	int index=0;
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_hei);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(wSize, ViewGroup.LayoutParams.MATCH_PARENT);
			SwipeMenuItem deleteItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.green)
					.setText(getString(R.string.edit))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧菜单。



			SwipeMenuItem wechatItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setWidth(wSize)
					.setHeight(hSize);

			swipeRightMenu.addMenuItem(wechatItem);
			//添加一个按钮到右侧菜单。
		}
	};

	public class VoiceDelAsyncTask extends AsyncTask<String, Void, ResultInfo> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo listUser = null;
			try {
				String json = VoiceServiceUtils.detVoice(Integer
						.parseInt(params[0]));
				listUser = (ResultInfo) JSONHelper.parseObject(json,
						ResultInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return listUser;
		}
		@Override
		protected void onPostExecute(ResultInfo result) {
			if (result != null) {

				if (result.getResultCode() == 1) {
					TipAlert verifyAlert = new TipAlert(activity, activity
							.getResources().getString(R.string.tip), activity.getString(R.string.deleteSuccess));
					verifyAlert.show();
				} else {
					TipAlert verifyAlert = new TipAlert(activity, activity
							.getResources().getString(R.string.tip), activity.getString(R.string.deleteFail));
					verifyAlert.show();
				}
			}

		}
	}
	public void adapter(){
		adapter.notifyDataSetChanged();
	}
}