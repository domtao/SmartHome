package com.zunder.smart.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.adapter.VpAdapter;
import com.zunder.smart.xpermissionutils.PermissionHelper;
import com.zunder.smart.xpermissionutils.callback.OnPermissionCallback;

import java.util.ArrayList;

public class WelcomeActivity extends Activity  implements ViewPager.OnPageChangeListener {
	private SharedPreferences spf;
	private Activity activity;
	private ViewPager vPager;
	private VpAdapter vpAdapter;
	private static  int[] imgs = {R.mipmap.wl_1,R.mipmap.wl_2, R.mipmap.wl_3,R.mipmap.wl_4,R.mipmap.wl_5};
	private ArrayList<ImageView> imageViews;
	ImageView imageDo;
	ImageView imgFinish;
	private static int index=0;
	public static void startActivity(Activity activity,int _index) {
		index=_index;
		Intent intent = new Intent(activity, WelcomeActivity.class);
		activity.startActivity(intent);
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		vPager=(ViewPager)findViewById(R.id.guide_ViewPager);
		spf = getSharedPreferences("welcome_info", Context.MODE_PRIVATE);
		activity = this;
		imageDo=(ImageView)findViewById(R.id.imageDo) ;
		imgFinish=(ImageView)findViewById(R.id.imgFinish) ;
		imageDo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index==0) {
					SharedPreferences.Editor editor = spf.edit();
					editor.putInt("wel", 1);
					editor.commit();
					Intent toMainActivity = new Intent(activity, MainActivity.class);//跳转到主界面
					startActivity(toMainActivity);
				}else{
					finish();
				}
			}
		});
		imgFinish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index==0) {
					SharedPreferences.Editor editor = spf.edit();
					editor.putInt("wel", 1);
					editor.commit();
					Intent toMainActivity = new Intent(activity, MainActivity.class);//跳转到主界面
					startActivity(toMainActivity);
				}else{
					finish();
				}
			}
		});
		initImages();

//		initDots();
	}
	private void initImages() {
		//设置每一张图片都填充窗口
		ViewPager.LayoutParams mParams = new ViewPager.LayoutParams();
		imageViews = new ArrayList <ImageView>();
		for (int i = 0; i < imgs.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);//设置布局
			iv.setImageResource(imgs[i]);//为ImageView添加图片资源
			iv.setScaleType(ImageView.ScaleType.FIT_XY);//这里也是一个图片的适配
			imageViews.add(iv);
		}
		vpAdapter=new VpAdapter(imageViews);
		vPager.setAdapter(vpAdapter);
		vPager.setOnPageChangeListener(this);
	}
		@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
        if (position == imgs.length - 1) {
            //为最后一张图片添加点击事件
			if(index==0) {
				imageDo.setVisibility(View.VISIBLE);
			}
        }else{
            imageDo.setVisibility(View.GONE);
        }
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

}
