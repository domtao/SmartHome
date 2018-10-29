package com.zunder.smart.image;

import java.util.List;

import com.bumptech.glide.Glide;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class ScrollImage extends RelativeLayout {

	private ImageScrollView imageScrollView = null;
	private PageControlView pageControlView = null;

	public ScrollImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.scroll_image, ScrollImage.this);
		imageScrollView = (ImageScrollView) this
				.findViewById(R.id.myImageScrollView);
		pageControlView = (PageControlView) this
				.findViewById(R.id.myPageControlView);
	}

	public void setBitmapList(List<Integer> list) {
		int num = list.size();
		imageScrollView.removeAllViews();
		for (int i = 0; i < num; i++) {
			ImageView imageView = new ImageView(getContext());
			imageView.setScaleType(ScaleType.FIT_XY);
//			imageView.setImageBitmap(list.get(i));
			Glide.with(MainActivity.getInstance())
					.load("")
					.placeholder(list.get(i))
					.crossFade()
					.into(imageView);
			imageScrollView.addView(imageView);
		}
		pageControlView.setCount(imageScrollView.getChildCount());
		pageControlView.generatePageControl(0);
		imageScrollView.setScrollToScreenCallback(pageControlView);
	}


	public void setHeight(int height) {
		android.view.ViewGroup.LayoutParams la = getLayoutParams();
		la.height = height;
		android.view.ViewGroup.LayoutParams lap = imageScrollView
				.getLayoutParams();
		lap.height = height;
	}


	public void setWidth(int width) {
		android.view.ViewGroup.LayoutParams la = getLayoutParams();
		la.width = width;
		android.view.ViewGroup.LayoutParams lap = imageScrollView
				.getLayoutParams();
		lap.width = width;
	}


	public void start(int time) {
		imageScrollView.start(time);
	}


	public void stop() {
		imageScrollView.stop();
	}

	public int getPosition() {
		return imageScrollView.getCurrentScreenIndex();
	}


	public void setClickListener(OnClickListener clickListener) {
		imageScrollView.setClickListener(clickListener);
	}
}
