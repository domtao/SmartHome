package com.zunder.smart.activity.passive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.adapter.PassiveAdapter;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.EditTxtAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.listener.PassiveListener;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.Passive;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.view.ListViewDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PassiveFragment extends Fragment implements OnClickListener,
		PassiveListener {
	private TextView backTxt;
	private List<Passive> listPassive = new ArrayList<Passive>();
	private SwipeMenuRecyclerView listView;
	public  Activity activity;
	private int random = 0;
	PassiveAdapter adapter;
	public static int iSRandom = 0;
	RelativeLayout mainLayout;
	TextView msgTxt;
	TextView addDevice;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_passive, container,
				false);

		activity = getActivity();
		mainLayout = (RelativeLayout) root.findViewById(R.id.mainLayout);
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		msgTxt=(TextView)root.findViewById(R.id.msgTxt);
		backTxt.setOnClickListener(this);
		addDevice=(TextView)root.findViewById(R.id.addDevice);
		addDevice.setOnClickListener(this);
		listView = (SwipeMenuRecyclerView) root.findViewById(R.id.deviceList);
		listView.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		// 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
		// 设置菜单创建器。
		listView.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。
		listView.setSwipeMenuItemClickListener(menuItemClickListener);

		adapter = new PassiveAdapter(activity,listPassive);
		listView.setAdapter(adapter);
		adapter.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(adapter);


        return root;
	}

	public void init(){
		TcpSender.setPassiveListener(this);
		startCount = 0;
		searchflag = true;
		dialog();
		startTime();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			TabMyActivity.getInstance().hideFragMent(10);
			onHideCode();
			break;
		case R.id.editeTxt:
			break;
		case R.id.arceLayout:
			break;
		case R.id.typeLayout:
			break;
		case R.id.productLayout:
			break;
			case R.id.addDevice:
				DialogAlert alert = new DialogAlert(activity);

				alert.init(getString(R.string.tip), getString(R.string.device_key));
				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub

						SendCMD.getInstance().sendCMD(241,"6:00:00", DeviceFactory.getInstance().getGateWayDevice());
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





	private int startCount = 0;
	private boolean searchflag = false;

	private void startTime() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(1000);
						startCount++;
						if (startCount >=2) {
							searchflag = false;
							if ((warnDialog != null) && warnDialog.isShowing()) {
								warnDialog.dismiss();
							}
						}else{
							handler.sendEmptyMessage(-1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	WarnDialog warnDialog = null;
	public void dialog(){
		if (warnDialog == null) {
			warnDialog = new WarnDialog(activity,getString(R.string.searchdevice));
			warnDialog.setMessage(getString(R.string.getdevice)+" 4");
			warnDialog.setSureListener(new WarnDialog.OnSureListener() {
				@Override
				public void onCancle() {
					searchflag = false;
					warnDialog.dismiss();
				}
			});
		}
	warnDialog.show();
}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){

		}
	}
	public void onHideCode(){
		listPassive.clear();
		SendCMD.getInstance().sendCMD(241,"04:00:00", DeviceFactory.getInstance().getGateWayDevice());
		TcpSender.setPassiveListener(null);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case -1:
				warnDialog.setMessage(getString(R.string.getdevice)+" "+(5-startCount));
				break;
			case 0:
				if (listPassive.size() > 0) {
					mainLayout.setBackgroundColor(Color.WHITE);
					msgTxt.setVisibility(View.VISIBLE);
				} else {
					mainLayout.setBackgroundResource(R.mipmap.passiveimage);
					msgTxt.setVisibility(View.GONE);
				}
				sort(listPassive);
				adapter.notifyDataSetChanged();
				if(searchflag&&(listPassive.size()>=2)){
					startCount=5;
				}
				break;
			case 1:
				adapter.changSwichSate(msg.arg1);
				break;
			case 2:
				//hideProgressDialog();
				//PassiveListActivity.startActivity(activity, msg.obj.toString());
				break;
			default:
				break;
			}
		}
	};



	public String toHex(int number) {
		if (number <= 15) {
			return ("0" + Integer.toHexString(number).toUpperCase());
		} else {
			return Integer.toHexString(number).toUpperCase();
		}
	}

	@Override
	public void setPassive(Passive passive) {
		// TODO Auto-generated method stub

		if (passive.getSubCommand().equals("00")) {
			int isexit=0;
			for (int i=0;i<listPassive.size();i++) {
				Passive passive2=listPassive.get(i);
				if (passive2.getCmdString().equals(passive.getCmdString())) {
				//	listPassive.add(i,passive);
				//	listPassive.remove(i+1);
					passive2.setOnStart(passive.getOnStart());
					isexit=1;
					break;
				}
			}
			if(isexit==0){
				listPassive.add(passive);
			}
			handler.sendEmptyMessage(0);
		} else if (passive.getSubCommand().equals("01")) {
			int _index = -1;
			for (int i = 0; i < listPassive.size(); i++) {
				Passive passive3 = listPassive.get(i);
				if (passive3.getCmdString().equals(passive.getCmdString())) {
					_index = i;
					break;
				}
			}
			Message message = handler.obtainMessage();
			message.what = 1;
			message.arg1 = _index;
			handler.sendMessage(message);
		} else {
//			if (Integer.valueOf(passive.getOnStart(), 16) == iSRandom
//					&& listPassive.get(index).getMemIndex()
//					.equals(passive.getMemIndex())) {
//				Message message = handler.obtainMessage();
//				message.what = 2;
//				message.obj = passive.getMemIndex();
//				handler.sendMessage(message);
//			}
		}
	}

	int timerValue = 0;

	public void AskTimeSch(String name, final String memIndex, String onStart) {
		timerValue = 0;

		int value = Integer.parseInt(onStart, 16);
		final String items[] = (String[]) activity.getResources()
				.getStringArray(R.array.passives);
		final boolean[] selected = new boolean[items.length];

		value ^= 0x00ff;

		for (int i = 0; i < selected.length; i++) {
			if ((value & (1 << i)) > 0) {
				// 1
				selected[i] = true;
			} else {
				// 0
				selected[i] = false;
			}

		}
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setIcon(R.mipmap.img_air_time_meihong);
		builder.setTitle(name);
		builder.setMultiChoiceItems(items, selected,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						// TODO Auto-generated method stub
						selected[which] = isChecked;
					}
				});
		builder.setPositiveButton(activity.getString(R.string.sure),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						for (int i = 0; i < selected.length; i++) {
							if (selected[i]) {
								timerValue += (1 << i);
							}
						}
						timerValue ^= 0x00ff;
						try {
						SendCMD.getInstance()
								.sendCMD(
										241,
										"2:" + toHex(timerValue) + ":"
												+ memIndex, DeviceFactory.getInstance().getGateWayDevice());
							Thread.sleep(50);
							String cmd = "*C000051000100000000000000";
							String result = ISocketCode.setForward(cmd, DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
							MainActivity.getInstance().sendCode(result);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				});
		builder.setNegativeButton(activity.getString(R.string.cancle),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
		builder.create().show();

	}
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);

			SwipeMenuItem learnItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.blue)
					.setText(getString(R.string.learn))
					.setTextColor(Color.WHITE)
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(learnItem); // 添加一个按钮到右侧菜单。

			SwipeMenuItem senceItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.orange)
					.setText(getString(R.string.sence))
					.setTextColor(Color.WHITE)
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(senceItem); // 添加一个按钮到右侧菜单。


			SwipeMenuItem addItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.pink)
					.setText(getString(R.string.edit))
					.setTextColor(Color.WHITE)
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。

			SwipeMenuItem delItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setTextColor(Color.WHITE)
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(delItem); // 添加一个按钮到右侧菜单。
		}
	};
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(int position) {
			if(listPassive!=null&&listPassive.size()>0) {
				if(position>=0&&position<listPassive.size()) {
					iSRandom = (new Random().nextInt(255) % 255) + 1;
					String menIndex = listPassive.get(position).getMemIndex();
					SendCMD.getInstance().sendCMD(241,
							"1:" + toHex(iSRandom) + ":" + menIndex, DeviceFactory.getInstance().getGateWayDevice());

				}
			}
		}
	};
	/**
	 * 菜单点击监听。
	 */
	private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
		@Override
		public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
			closeable.smoothCloseMenu();// 关闭被点击的菜单。
			if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
				switch (menuPosition) {
					case 0:
						DialogAlert learnAlert = new DialogAlert(activity);

						learnAlert.init(getString(R.string.tip), getString(R.string.is_del_learn));
						learnAlert.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								SendCMD.getInstance().sendCMD(241,"6:00:"+listPassive.get(adapterPosition).getMemIndex(), DeviceFactory.getInstance().getGateWayDevice());
							}

							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						learnAlert.show();
						break;
					case 1:
						final Passive passive=	listPassive.get(adapterPosition);
						final int memIndex=Integer.valueOf(
								passive.getCmdString().substring(0,2), 16);
						int code=0;
						if(memIndex<25){
							code=150+memIndex;
						}else{
							code=(memIndex<28)?175:176;
						}
						final int modeCode=code;
						final Mode mode= ModeFactory.getInstance().getName(modeCode);

						if (mode == null) {
							TabMyActivity.getInstance().showFragMent(11);
							TabMyActivity.getInstance().modeAddFragment.setDate("Add", "FF", modeCode);
							/*
							final EditTxtAlert alert = new EditTxtAlert(activity);
							alert.setTitle(android.R.drawable.ic_dialog_info,"是否关联" +passive.getName()+"模式",0);
							alert.setHint(getString(R.string.input_name1));

							alert.setOnSureListener(new EditTxtAlert.OnSureListener() {
								@Override
								public void onSure(String fileName) {
									// TODO Auto-generated method stub
									if (fileName.equals("")) {
										Toast.makeText(activity, activity.getString(R.string.name_null) + "!",
												Toast.LENGTH_SHORT).show();
										return;
									}
									passive.setName(fileName);
									adapter.notifyDataSetChanged();
									Mode mode = new Mode();
									mode.setModeName(fileName);
									mode.setModeImage("ImagesFile/Picture/2018071913574232.jpg");
									mode.setModeType("FF");
									mode.setModeCode(modeCode);
									mode.setSeqencing(ModeFactory.getInstance().getAll().size());
									mode.setStartTime("00:00");
									mode.setEndTime("00:00");
									mode.setModeLoop(0);
									if (ModeFactory.getInstance().judgeName(fileName) == 0) {
										int result = MyApplication.getInstance().getWidgetDataBase().insertMode(mode);
										if (result > 1) {
											ModeFactory.getInstance().clearList();
											Toast.makeText(activity, getString(R.string.addSuccess), Toast.LENGTH_SHORT).show();
											alert.dismiss();
											final Mode _mode = ModeFactory.getInstance().getName(modeCode);
											if (_mode != null) {
												DialogAlert alert = new DialogAlert(activity);
												alert.init(activity.getString(R.string.tip),
														getString(R.string.is_to_sence)+"[" + _mode.getModeName() + "]");

												alert.setSureListener(new DialogAlert.OnSureListener() {

													@Override
													public void onSure() {
														// TODO Auto-generated
														// method stub
//														ModeListActivity.startActivity(activity, _mode.getModeName(),
//																_mode.getId());

													}
													@Override
													public void onCancle() {
														// TODO Auto-generated
														// method stub
													}
												});
												alert.show();
											}
										}
									} else {
										Toast.makeText(activity, getString(R.string.sence) + fileName + getString(R.string.exist), Toast.LENGTH_SHORT).show();
									}
								}
								@Override
								public void onCancle() {
									// TODO Auto-generated method stub
								}
							});
							alert.show();
							*/
						}
//						else{
//							DialogAlert alert = new DialogAlert(activity);
//							alert.init(activity.getString(R.string.tip), getString(R.string.is_to_sence)+"[" + mode.getModeName() + "]");
//
//							alert.setSureListener(new DialogAlert.OnSureListener() {
//
//								@Override
//								public void onSure() {
//									// TODO Auto-generated method stub
//									// 	ModeListActivity.startActivity(activity, mode.getModeName(), mode.getId());
//								}
//								@Override
//								public void onCancle() {
//									// TODO Auto-generated method stub
//								}
//							});
//							alert.show();
//						}

							break;
					case 2:
					AskTimeSch(listPassive.get(adapterPosition).getName(),
							listPassive.get(adapterPosition).getMemIndex(), listPassive.get(adapterPosition)
									.getOnStart());
						break;
					case 3:
						DialogAlert alert = new DialogAlert(activity);

						alert.init(getString(R.string.tip), getString(R.string.is_del_device));
						alert.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								SendCMD.getInstance().sendCMD(241,"5:00:"+listPassive.get(adapterPosition).getMemIndex(), DeviceFactory.getInstance().getGateWayDevice());
								listPassive.remove(adapterPosition);
								adapter.notifyDataSetChanged();
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
	public void sort(List<Passive> list) {
		int i, j, small;
		Passive temp;
		int n = list.size();
		for (i = 0; i < n - 1; i++) {
			small = i;
			for (j = i + 1; j < n; j++) {
				if (list.get(j).getId() < list.get(small).getId())
					small = j;
				if (small != i) {
					temp = list.get(i);
					list.set(i, list.get(small));
					list.set(small, temp);
				}
			}
		}
	}

}
