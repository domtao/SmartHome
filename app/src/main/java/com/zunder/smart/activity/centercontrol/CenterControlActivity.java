package com.zunder.smart.activity.centercontrol;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.zunder.base.CustomViewPager;
import com.zunder.base.RMCBaseView;
import com.zunder.base.RMCMenuView;
import com.zunder.base.ToastUtils;
import com.zunder.base.menu.RMCTabView;
import com.zunder.cusbutton.RMCCustomButton;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.centercontrol.dialog.SettingDialog;
import com.zunder.smart.activity.centercontrol.edite.TouchPanelActivity;
import com.zunder.smart.dao.impl.factory.RmcTabFactory;
import com.zunder.smart.dao.impl.factory.RmsTabFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.activity.centercontrol.popu.CenterPopupWindow;


@SuppressWarnings("deprecation")
public class CenterControlActivity extends TabActivity implements
		OnTabChangeListener {
	private TabHost mTabHost;
	private TabWidget mTabWidget;
	private HorizontalScrollView hs;
	List<RMCTabView> tabs = null;
	ImageView centerSet;
//	RootCenter rootCenter;

    private static CenterControlActivity instance;
    public static CenterControlActivity getInstance() {
        return instance;
    }
	private List<View> mListViews;
	private CustomViewPager mViewPager;
	CenterPopupWindow menuPopupWindow;
	private Handler handler = new Handler();
	private Runnable startDelayRunable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				loadingView();
				initTabView();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	};

	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, CenterControlActivity.class);
		activity.startActivity(intent);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance=this;
//		rootCenter= RootCenterUtils.getRootCenter();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_centercontrol);

//        loadingView();
//        initTabView();
		handler.postDelayed(startDelayRunable, 200);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		for (RMCBaseView v :RmcTabFactory.getInstance().getSubViews()) {
			v.release();
		}
		removeViewInViewGroup();
		super.onDestroy();
	}

	private int childTabMinWidth;
	private int min;

	private void initTabView() {
		int count = mTabWidget.getChildCount();
		int screenWidth = getScreenWidth();

		min = 4;
//		int height = rootCenter.getTabHeight();
		if (min == 0) {
			min = 1;
		}
		if (count > min) {
			childTabMinWidth = screenWidth / min;

			hs = (HorizontalScrollView) findViewById(R.id.main_sv);
//			hs.getLayoutParams().height=height;
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
				RMCTabView tabView=	tabs.get(i);
				mTabWidget.getChildTabViewAt(i).getLayoutParams().height = mTabHost.getHeight();
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
//				tv.setTypeface(application().getTypeface());

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
//				}
			} catch (Exception e) {
				System.out.println(this.getClass().toString() + ":" + e);
			}
		}
	}

	private void loadingView() {

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
		mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
		centerSet=(ImageView)findViewById(R.id.centerSet);
		centerSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				menuPopupWindow.display(centerSet);
			}
		});
		menuPopupWindow = new CenterPopupWindow(instance);
		menuPopupWindow.setOnItemListener(new CenterPopupWindow.OnItemListener() {
            @Override
            public void OnItem(int pos, String ItemName) {
                switch (pos){
                    case 0:
//                        SettingDialog settingDialog=new SettingDialog(instance);
//                        settingDialog.show();
                        break;
                    case 1:
						if(RmcTabFactory.getInstance().isCustom()>0) {
							RmsTabFactory.getInstance().clear();
							TouchPanelActivity.startActivity(instance);
							finish();
						}else{
							ToastUtils.ShowError(instance,"当前没有自定义界面,请到[房间管理]添加[中控]", Toast.LENGTH_SHORT,true);
						}
                        break;
                    case 3:
                        DialogAlert alert = new DialogAlert(instance);
                        alert.init(instance.getString(R.string.tip), "是否退出中控系统");
                        alert.setSureListener(new DialogAlert.OnSureListener() {
                            @Override
                            public void onSure() {
                                // TODO Auto-generated method stub
                               finish();
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
        });
//		int padding = rootCenter.getPadding();
//		((ViewGroup) findViewById(R.id.mainLayout)).setPadding(padding,
//				padding, padding, padding);

		tabs =RmcTabFactory.getInstance().getTabs();
		mListViews = new ArrayList<View>();
		for (RMCTabView tab : tabs) {
			View view = getView(tab);
			mListViews.add(view);
			mTabHost.addTab(mTabHost
					.newTabSpec(tab.getTag() + "")
					.setIndicator(tab.getText(),
                            tab.getDrawableData().getDrawable())
					.setContent(new TabContentFactory() {

						@Override
						public View createTabContent(String tag) {
							// TODO Auto-generated method stub
							View v = new View(instance);
							return v;
						}
					}));
		}

		mTabHost.setOnTabChangedListener(this);
		boolean isScroll = Boolean.parseBoolean("1");
		mViewPager.setIsCallScroll(isScroll);
		mViewPager.setAdapter(new MyPageAdapter(mListViews));
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mTabHost.setCurrentTab(position);
				if (mTabWidget.getChildCount() > min && min != 0) {
					int d = mTabHost.getCurrentTab() / min;
					hs.smoothScrollTo(childTabMinWidth * min * d, 0);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

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

	public RMCTabView getCurrentTabView() {
		try {
			return RmcTabFactory.getInstance().getTabs().get(mTabHost.getCurrentTab());
		} catch (Exception e) {
			return null;
		}
	}

	public CustomViewPager getViewPage() {
		return mViewPager;
	}


	public void refresh() {
		mTabHost.setCurrentTab(0);
		mTabHost.clearAllTabs();
		removeViewInViewGroup();
//		rootCenter= RootCenterUtils.getRootCenter();
//        setRequestedOrientation(rootCenter.getScreenOrientation() == 0? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//                : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		setContentView(rootCenter.getTabPosition() ==0 ? R.layout.activity_centercontrol
//				: R.layout.main_tab_bottom);

		loadingView();
		initTabView();
	}

	private void removeViewInViewGroup() {
		for (RMCTabView tab : RmcTabFactory.getInstance().getTabs()) {
			if (tab.getLayout() != null)
				tab.getLayout().removeAllViews();
		}
	}


	private View getView(RMCTabView tab) {
		AbsoluteLayout contentLayout = new AbsoluteLayout(
                instance);
		tab.setLayout(contentLayout);
		List<RMCMenuView> menus = new ArrayList<RMCMenuView>();
		menus.add(tab);

		for (RMCMenuView menu : menus) {

			List<RMCBaseView> subs = menu.getSubViews();
			for (RMCBaseView sub : subs) {
				contentLayout.addView(sub, sub.getLayoutParams());
				if (sub.getTabViewTag() == -1)
					sub.hide();

//				sub.setTextColor(application().getAppTextColor());
//				sub.setTypeface(application().getTypeface());
			}
			if (!(menu instanceof RMCTabView)) {
//				menu.setTextColor(application().getAppTextColor());
//				menu.setTypeface(application().getTypeface());
				contentLayout.addView(menu, menu.getLayoutParams());
			}
		}

		return contentLayout;
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		int id = mTabHost.getCurrentTab();
		RMCTabView tabView=tabs.get(id);
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
		mViewPager.setCurrentItem(id);
	}

	private class MyPageAdapter extends PagerAdapter {

		private List<View> list;

		private MyPageAdapter(List<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object arg2) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			ViewPager pViewPager = ((ViewPager) view);
			pViewPager.addView(list.get(position));
			return list.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class MyTabContentFactory implements TabContentFactory {
		private RMCTabView tab;

		public MyTabContentFactory(RMCTabView tab) {
			super();
			this.tab = tab;
		}

		@Override
		public View createTabContent(String tag) {
			// TODO Auto-generated method stub
			AbsoluteLayout contentLayout = new AbsoluteLayout(
					instance);
			tab.setLayout(contentLayout);
			List<RMCMenuView> menus = new ArrayList<RMCMenuView>();
			menus.add(tab);

			for (RMCMenuView menu : menus) {
				List<RMCBaseView> subs = menu.getSubViews();
				for (RMCBaseView sub : subs) {
//					sub.setOnLongClickListener(new MoveableLongClickListener());
//					sub.setOnTouchListener(new MoveableListener());
					contentLayout.addView(sub, sub.getLayoutParams());
					if (sub.getTabViewTag() == -1)
						sub.hide();
//					sub.setTextColor(application().getAppTextColor());
//					sub.setTypeface(application().getTypeface());
				}
				if (!(menu instanceof RMCTabView)) {
//					menu.setTextColor(application().getAppTextColor());
//					menu.setTypeface(application().getTypeface());
					contentLayout.addView(menu, menu.getLayoutParams());
				}
			}

			return contentLayout;
		}
	}

	public void updateSubCustomButton(int Id,String name,String imagePath){
		List<RMCBaseView> list=	RmcTabFactory.getInstance().getTabs().get(mTabHost.getCurrentTab()).getSubViews();
		for (int i=0;i<list.size();i++){
			RMCCustomButton customButton=(RMCCustomButton)list.get(i);
			if(customButton.getId()==Id){
				customButton.setText(name);
				customButton.setBackgroundFromProperties(imagePath);
				break;
			}
		}
	}

}