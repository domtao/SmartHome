package com.zunder.smart.activity.centercontrol.edite;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.zunder.base.Copyable;
import com.zunder.base.RMSBaseView;
import com.zunder.base.RMSMenuView;
import com.zunder.base.ToastUtils;
import com.zunder.base.menu.RMSTabView;
import com.zunder.cusbutton.RMSCustomButton;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.centercontrol.CenterControlActivity;
import com.zunder.smart.activity.centercontrol.dialog.AddRMSCusbuttonDialog;
import com.zunder.smart.activity.centercontrol.edite.util.MoveableListener;
import com.zunder.smart.activity.centercontrol.edite.util.MoveableLongClickListener;
import com.zunder.smart.activity.centercontrol.popu.ButtonManagePopupWindow;
import com.zunder.smart.activity.centercontrol.popu.TouchPopupWindow;
import com.zunder.smart.dao.impl.factory.RmcTabFactory;
import com.zunder.smart.dao.impl.factory.RmsTabFactory;
import com.zunder.smart.dialog.ButtonAlert;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.TipAlert;


@SuppressWarnings("deprecation")
public class TouchPanelActivity extends TabActivity implements TabHost.OnTabChangeListener {
	private TabHost mTabHost;
	private HorizontalScrollView hs;
	List<RMSTabView> tabs = null;
	private boolean isAdd=false;
	ImageView centerSet;
	private TouchPopupWindow touchPopupWindow;
	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, TouchPanelActivity.class);
		activity.startActivity(intent);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance=this;
		loadingView();
		initTabView();
		mTabHost.getTabWidget().setStripEnabled(false);
		touchPopupWindow = new TouchPopupWindow(this);
		touchPopupWindow.setOnItemListener(new TouchPopupWindow.OnItemListener() {
			@Override
			public void OnItem(int pos, String ItemName) {
				switch (pos){
					case 0: {
						//Joe 增加
						final AddRMSCusbuttonDialog addRMSCusbuttonDialog = new AddRMSCusbuttonDialog(instance,null);
						addRMSCusbuttonDialog.setOnSureListener(new AddRMSCusbuttonDialog.OnSureListener() {
							@Override
							public void onCancle() {
							}

							@Override
							public void onSure(RMSCustomButton rmcBean) {
								getCurrentTabView().getLayout().addView(rmcBean, rmcBean.getLayoutParams());
								RmsTabFactory.getInstance().getSubViews().add(rmcBean);
								rmcBean.setOnLongClickListener(new MoveableLongClickListener());
								rmcBean.setOnTouchListener(new MoveableListener());
								isAdd=true;
								addRMSCusbuttonDialog.dismiss();
							}

							@Override
							public void onEditeSure(RMSBaseView rmcBean) {

							}
						});
						addRMSCusbuttonDialog.show();
					}
						break;
					case 1:
						paste();
						break;
					case 2: {
						if(isAdd) {
							DialogAlert alert = new DialogAlert(instance);
							alert.init(instance.getString(R.string.tip), "是否保存数据");
							alert.setSureListener(new DialogAlert.OnSureListener() {
								@Override
								public void onSure() {
									// TODO Auto-generated method stub
									MyApplication.getInstance().getWidgetDataBase().deleteAllRMS();
									List <RMSBaseView> list=RmsTabFactory.getInstance().getSubViews();
									for (int i=0;i<list.size();i++) {
										RMSCustomButton rmsCustomButton=(RMSCustomButton)list.get(i);
										rmsCustomButton.setId(i+1);
										MyApplication.getInstance().getWidgetDataBase().updateRMSviews(rmsCustomButton);
									}
									ToastUtils.ShowSuccess(instance,"保存成功!",Toast.LENGTH_SHORT,true);
//									RmcTabFactory.getInstance().clear();
//									CenterControlActivity.startActivity(instance);
//									finish();
									isAdd=false;
								}

								@Override
								public void onCancle() {
									// TODO Auto-generated method stub

								}
							});
							alert.show();
						}
					}
						break;

					case 3: {
						if(!isAdd) {
							DialogAlert alert = new DialogAlert(instance);
							alert.init(instance.getString(R.string.tip), "是否退出中控编辑");
							alert.setSureListener(new DialogAlert.OnSureListener() {
								@Override
								public void onSure() {
									// TODO Auto-generated method stub
									RmcTabFactory.getInstance().clear();
									CenterControlActivity.startActivity(instance);
									finish();
								}
								@Override
								public void onCancle() {
									// TODO Auto-generated method stub
								}
							});
							alert.show();
						}else{
							DialogAlert alert = new DialogAlert(instance);
							alert.init(instance.getString(R.string.tip), "数据有更改是否保存");
							alert.setSureListener(new DialogAlert.OnSureListener() {
								@Override
								public void onSure() {
									// TODO Auto-generated method stub
									MyApplication.getInstance().getWidgetDataBase().deleteAllRMS();
									List <RMSBaseView> list=RmsTabFactory.getInstance().getSubViews();
									for (int i=0;i<list.size();i++) {
										RMSCustomButton rmsCustomButton=(RMSCustomButton)list.get(i);
										rmsCustomButton.setId(i+1);
										MyApplication.getInstance().getWidgetDataBase().updateRMSviews(rmsCustomButton);
									}
									ToastUtils.ShowSuccess(instance,"保存成功!",Toast.LENGTH_SHORT,true);
									RmcTabFactory.getInstance().clear();
									CenterControlActivity.startActivity(instance);
									finish();
								}

								@Override
								public void onCancle() {
									// TODO Auto-generated method stub
								}
							});
							alert.show();
						}
					}
						break;

				}
			}
		});
	}
	private static TouchPanelActivity instance;
	public static TouchPanelActivity getInstance() {
		return instance;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void dimissMenuPopupWindow() {
		touchPopupWindow.dismiss();
	}

	@Override
	protected void onDestroy() {

		removeViewInViewGroup();
		super.onDestroy();
	}

	// ��ʼ���˵���
	private int childTabMinWidth;

	private void initTabView() {
		TabWidget tabWidget = mTabHost.getTabWidget();
		int count = tabWidget.getChildCount();
		int screenWidth = getScreenWidth();

		int min = 4;
		int height =60;
		if (min == 0) {
			min = 1;
		}
		if (count > min) {
			childTabMinWidth = screenWidth / min;
			hs = (HorizontalScrollView) findViewById(R.id.main_sv);
			hs.setOnTouchListener(new View.OnTouchListener() {
				int x;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					try {
						int action = event.getAction();
						HorizontalScrollView mSV = (HorizontalScrollView) v;
						switch (action) {
							case MotionEvent.ACTION_DOWN:
								break;
							case MotionEvent.ACTION_MOVE:
								break;
							case MotionEvent.ACTION_UP:
								x = mSV.getScrollX();
								if (x % childTabMinWidth > childTabMinWidth / 2) {
									x = (x / childTabMinWidth + 1)
											* childTabMinWidth;
								} else {
									x = (x / childTabMinWidth) * childTabMinWidth;
								}
								mSV.smoothScrollTo(x, 0);
								return true;
						}
					} catch (Exception e) {
						System.out
								.println(this.getClass().toString() + ":" + e);
					}
					return false;
				}
			});
		}

		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

			try {
				RMSTabView tabView=	tabs.get(i);
//				mTabWidget.getChildTabViewAt(i).getLayoutParams().height = mTabHost.getHeight();
				View view = mTabHost.getTabWidget().getChildAt(i);
				if(tabView.getBtnType()==1) {
					view.setBackgroundDrawable(MyApplication.getInstance().getDrawabled(
							R.drawable.tabhostpress1));
				}else 	if(tabView.getBtnType()==2) {
					view.setBackgroundDrawable(MyApplication.getInstance().getDrawabled(
							R.drawable.tabhostpress2));
				}else 	if(tabView.getBtnType()==3) {
					view.setBackgroundDrawable(MyApplication.getInstance().getDrawabled(
							R.drawable.tabhostpress3));
				}else {
					view.setBackgroundDrawable(MyApplication.getInstance().getDrawabled(
							R.drawable.tabhostpress4));
				}

				TextView tv = (TextView) view.findViewById(android.R.id.title);
				tv.setGravity(Gravity.CENTER);
				tv.setTextSize(tabs.get(i).getTextSize());
				tv.setTextColor(instance.getResources().getColor(R.color.whites));
				view.getLayoutParams().width = screenWidth / min;

				ImageView iv = (ImageView) view
						.findViewById(android.R.id.icon);
				iv.setVisibility(View.GONE);
				RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
				paramsLeft.addRule(RelativeLayout.ALIGN_RIGHT);
				paramsLeft.addRule(RelativeLayout.CENTER_VERTICAL,
						RelativeLayout.TRUE);
				iv.setLayoutParams(paramsLeft);

				RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
				paramsRight.addRule(RelativeLayout.CENTER_IN_PARENT);
				paramsRight.addRule(RelativeLayout.CENTER_VERTICAL,
						RelativeLayout.TRUE);
				tv.setLayoutParams(paramsRight);

			} catch (Exception e) {
				System.out.println(this.getClass().toString() + ":" + e);
			}
		}

	}

	private void loadingView() {
		setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
		centerSet=(ImageView)findViewById(R.id.centerSet);
		centerSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				touchPopupWindow.display(centerSet);
			}
		});
		mTabHost = getTabHost();
		mTabHost.setOnTabChangedListener(this);
		tabs = RmsTabFactory.getInstance().getTabs();
		for (final RMSTabView tab : tabs) {
			mTabHost.addTab(mTabHost
					.newTabSpec(tab.getTag() + "")
					.setIndicator(tab.getText(),
							tab.getDrawableData().getDrawable())
					.setContent(new MyTabContentFactory(tab)));
		}
	}

	private DisplayMetrics dm;
	private DisplayMetrics getDisplayMetrics() {
		if (dm == null) {
			dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
		}
		return dm;
	}

	public int getScreenWidth() {
		return getDisplayMetrics().widthPixels;
	}

	public int getScreenHeight() {
		return getDisplayMetrics().heightPixels;
	}

	public int getTabHostHeight() {
		return mTabHost.getTabWidget().getHeight();
	}

	public RMSTabView getCurrentTabView() {
		try {
			return RmsTabFactory.getInstance().getTabs().get(mTabHost.getCurrentTab());
		} catch (Exception e) {
			return null;
		}
	}


	public void refresh() {
		mTabHost.setCurrentTab(0);
		mTabHost.clearAllTabs();
		removeViewInViewGroup();
		dimissMenuPopupWindow();
		loadingView();
		initTabView();
	}

	private void removeViewInViewGroup() {
		for (RMSTabView tab : RmsTabFactory.getInstance().getTabs()) {
			if (tab.getLayout() != null)
				tab.getLayout().removeAllViews();
		}
	}

	@Override
	public void onTabChanged(String tabId) {
		int id = mTabHost.getCurrentTab();
		RMSTabView tabView=tabs.get(id);
		if(tabView!=null){
			if(tabView.getBtnType()==1){
				mTabHost.setBackgroundResource(R.mipmap.zun_custom_bg1);
			}else if(tabView.getBtnType()==2){
				mTabHost.setBackgroundResource(R.mipmap.zun_custom_bg2);
			}else if(tabView.getBtnType()==3){
				mTabHost.setBackgroundResource(R.mipmap.zun_custom_bg3);
			}else{
				mTabHost.setBackgroundResource(R.mipmap.zun_custom_bg4);

			}
		}
	}

	private class MyTabContentFactory implements TabContentFactory {
		private RMSTabView tab;

		public MyTabContentFactory(RMSTabView tab) {
			super();
			this.tab = tab;
		}

		@Override
		public View createTabContent(String tag) {
			// TODO Auto-generated method stub
			AbsoluteLayout contentLayout = new AbsoluteLayout(
					TouchPanelActivity.this);
			tab.setLayout(contentLayout);
			List<RMSMenuView> menus = new ArrayList<RMSMenuView>();
			menus.add(tab);

			for (RMSMenuView menu : menus) {

				List<RMSBaseView> subs = menu.getSubViews();
				for (RMSBaseView sub : subs) {
					sub.setOnLongClickListener(new MoveableLongClickListener());
					sub.setOnTouchListener(new MoveableListener());
					contentLayout.addView(sub, sub.getLayoutParams());
					if (sub.getTabViewTag() == -1)
						sub.hide();
				}
				if (!(menu instanceof RMSTabView)) {
					contentLayout.addView(menu, menu.getLayoutParams());
				}
			}
			return contentLayout;
		}

	}
	public void hideTouchPopupWindow(){
		if(bPopupWindow!=null&&bPopupWindow.isShowing()){
			bPopupWindow.dismiss();
			isAdd=true;
		}
	}
	private ButtonManagePopupWindow bPopupWindow = null;

	public ButtonManagePopupWindow getButtonManagePopupWindow() {
		if (bPopupWindow == null) {
			bPopupWindow = new ButtonManagePopupWindow(instance);
			bPopupWindow.setOnItemListener(new ButtonManagePopupWindow.OnItemListener() {
				@Override
				public void OnItem(int pos,final View rmsBaseView) {
					switch (pos) {
						case 0:{
							final AddRMSCusbuttonDialog addRMSCusbuttonDialog = new AddRMSCusbuttonDialog(instance,(RMSBaseView) rmsBaseView);
							addRMSCusbuttonDialog.setOnSureListener(new AddRMSCusbuttonDialog.OnSureListener() {
								@Override
								public void onCancle() {
								}
								@Override
								public void onSure(RMSCustomButton rmcBean) {
								}
								@Override
								public void onEditeSure(RMSBaseView rmcBean) {

									if(rmcBean instanceof RMSCustomButton){
										((RMSCustomButton)rmcBean).initParams();
										isAdd=true;
									}
									addRMSCusbuttonDialog.dismiss();
								}
							});
							addRMSCusbuttonDialog.show();
						}
							break;
						case 1: {
							DialogAlert alert = new DialogAlert(instance);
							alert.init(instance.getString(R.string.tip), "是否删除数据");
							alert.setSureListener(new DialogAlert.OnSureListener() {
								@Override
								public void onSure() {
									// TODO Auto-generated method stub
									isAdd = true;
									getCurrentTabView().getLayout().removeView(rmsBaseView);
									RmsTabFactory.getInstance().getSubViews().remove(rmsBaseView);
								}

								@Override
								public void onCancle() {
									// TODO Auto-generated method stub

								}
							});
							alert.show();

						}
							break;
						case 2:
							setCopyView(rmsBaseView);
							ToastUtils.ShowSuccess(instance,"已复制到剪贴板", Toast.LENGTH_SHORT,true);
							break;
						case 3:
							{
							TipAlert alert = new TipAlert(instance,instance.getString(R.string.property), ((RMSCustomButton)rmsBaseView).toString());
							alert.show();
							}
							break;
					}
					bPopupWindow.dismiss();
				}
			});
		}
		return bPopupWindow;
	}
	private View cView = null;

	public void setCopyView(View v) {
		cView = v;
	}

	public View getCopyView() {
		return cView;
	}

	public Boolean paste() {
		try {
			View v = getCopyView();
			if (v == null)
				return false;

			View viewToPaste = ((Copyable)v).copy();
			RMSTabView rmsTabView= TouchPanelActivity.getInstance().getCurrentTabView();
			RMSCustomButton rmsCustomButton=(RMSCustomButton) viewToPaste;
			rmsCustomButton.setTabViewTag(rmsTabView.getId());
			rmsCustomButton.setBtnType(rmsTabView.getBtnType());
			rmsCustomButton.setId(RmsTabFactory.getInstance().getMax()+1);
			rmsCustomButton.setOnLongClickListener(new MoveableLongClickListener());
			rmsCustomButton.setOnTouchListener(new MoveableListener());
			getCurrentTabView().getLayout().addView(rmsCustomButton, rmsCustomButton.getLayoutParams());
			RmsTabFactory.getInstance().getSubViews().add(rmsCustomButton);
			isAdd=true;

		} catch (Exception e) {
			return false;
		}
		return true;
	}

}