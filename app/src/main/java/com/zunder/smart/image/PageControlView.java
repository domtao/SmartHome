package com.zunder.smart.image;



import com.zunder.smart.R;
import com.zunder.smart.image.ImageScrollView.ScrollToScreenCallback;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PageControlView extends LinearLayout implements
        ImageScrollView.ScrollToScreenCallback {
	/** Context���� **/
	private Context context;
	/** ԲȦ������ **/
	private int count;

	public PageControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	/** �ص����� **/
	@Override
	public void callback(int currentIndex) {
		generatePageControl(currentIndex);
	}

	/** ����ѡ��ԲȦ **/
	public void generatePageControl(int currentIndex) {
		this.removeAllViews();

		for (int i = 0; i < this.count; i++) {
			ImageView iv = new ImageView(context);
			if (currentIndex == i) {
				iv.setImageResource(R.mipmap.page_indicator_focused);
			} else {
				iv.setImageResource(R.mipmap.page_indicator);
			}
			iv.setLayoutParams(new LayoutParams(1, 0));
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 8;
			layoutParams.rightMargin = 8;
			iv.setLayoutParams(layoutParams);
			this.addView(iv);
		}
	}

	/** ����ԲȦ���� **/
	public void setCount(int count) {
		this.count = count;
	}
}