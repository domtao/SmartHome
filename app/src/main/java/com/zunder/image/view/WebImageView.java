package com.zunder.image.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zunder.image.cache.AsyncImageLoader;
import com.zunder.smart.R;


@SuppressLint("AppCompatCustomView")
public class WebImageView extends ImageView {

	private String url;

	Context _context;

	public WebImageView(Context context) {
		super(context);
		_context = context;
	}

	public WebImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

		_context = context;
	}

	public WebImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		_context = context;
	}

	/**
	 * @return download url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置download url，开始下载
	 * 
	 * @param url
	 */
	public void load(String url) {

		this.url = url;
		AsyncImageLoader.getAsyncImageLoader(_context).loadDrawable(
				_context.getResources().getDrawable(R.mipmap.itcp_message_image), url,
				new AsyncImageLoader.ImageCallback() {
					public void imageLoaded(Drawable imageDrawable) {
						WebImageView.this.destroyDrawingCache();
						WebImageView.this.setImageDrawable(imageDrawable);
					}
				});
	}
	public void setHeight(int height) {
		android.view.ViewGroup.LayoutParams la = getLayoutParams();
		la.height = height;
		android.view.ViewGroup.LayoutParams lap = this
				.getLayoutParams();
		lap.height = height;
	}

	/**
	 * ��������Ŀ�
	 * 
	 * @param

	 *            ��
	 */
	public void setWidth(int width) {
		android.view.ViewGroup.LayoutParams la = getLayoutParams();
		la.width = width;
		android.view.ViewGroup.LayoutParams lap = this
				.getLayoutParams();
		lap.width = width;
	}
}
