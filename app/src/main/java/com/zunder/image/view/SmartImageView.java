package com.zunder.image.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.zunder.image.cache.ContactImage;
import com.zunder.image.cache.SmartImage;
import com.zunder.image.cache.SmartImageTask;
import com.zunder.image.cache.WebImage;
import com.zunder.smart.R;

@SuppressLint("AppCompatCustomView")
public class SmartImageView extends ImageView {
	private static final int LOADING_THREADS = 4;
	/**
	 * TYPE_CIRCLE / TYPE_ROUND
	 */
	private int type=1;
	private static final int TYPE_CIRCLE = 0;
	private static final int TYPE_ROUND = 1;

	/**
	 * ͼƬ
	 */
	private Bitmap mSrc;

	/**
	 * Բ�ǵĴ�С
	 */
	private int mRadius;

	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(LOADING_THREADS);

	private SmartImageTask currentTask;
	public Bitmap getSrc() {
		return mSrc;
	}

	public void setSrc(Bitmap mSrc) {
		this.mSrc = mSrc;
	}

	public SmartImageView(Context context) {
		this(context, null);
	}

	public SmartImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SmartImageView, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.SmartImageView_src:
				mSrc = BitmapFactory.decodeResource(getResources(),
						a.getResourceId(attr, 0));
				break;
			case R.styleable.SmartImageView_type:
				type = a.getInt(attr, 1);
				break;
			case R.styleable.SmartImageView_borderRadius:
				mRadius = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f,
								getResources().getDisplayMetrics()));// Ĭ��Ϊ10DP
				break;
			}
		}
		a.recycle();
	}

	/**
	 * ���ԭͼ�ͱ䳤����Բ��ͼƬ
	 * 
	 * @param source
	 * @param min
	 * @return
	 */
	private Bitmap createCircleImage(Bitmap source, int min) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = null;
		// ʵ��Bitmap
		try {
			target = Bitmap.createBitmap(min, min, Config.ARGB_4444);
		} catch (OutOfMemoryError e) {
			target = Bitmap.createBitmap(100, 100, Config.ARGB_4444);
		}

		/**
		 * ����һ��ͬ���С�Ļ���
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * ���Ȼ���Բ��
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * ʹ��SRC_IN���ο������˵��
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * ����ͼƬ
		 */
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	/**
	 * ���ԭͼ���Բ��
	 * 
	 * @param source
	 * @return
	 */
	//换成圆角图片
	private Bitmap createRoundConerImage(Bitmap source) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);

		Bitmap target = null;
		try {
			target = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
					Config.ARGB_4444);
		} catch (OutOfMemoryError e) {
			target = Bitmap.createBitmap(100, 100, Config.ARGB_4444);
		}
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
		canvas.drawRoundRect(rect, mRadius, mRadius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	// Helpers to set image by URL
	public void setImageUrl(String url) {
		setImage(new WebImage(url));
	}

	public void setImageUrl(String url,
			SmartImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), completeListener);
	}

	public void setImageUrl(String url, final Integer fallbackResource) {
		setImage(new WebImage(url), fallbackResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			SmartImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), fallbackResource, completeListener);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(new WebImage(url), fallbackResource, loadingResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource,
			SmartImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), fallbackResource, loadingResource,
				completeListener);
	}

	// Helpers to set image by contact address book id
	public void setImageContact(long contactId) {
		setImage(new ContactImage(contactId));
	}

	public void setImageContact(long contactId, final Integer fallbackResource) {
		setImage(new ContactImage(contactId), fallbackResource);
	}

	public void setImageContact(long contactId, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(new ContactImage(contactId), fallbackResource,
				fallbackResource);
	}

	// Set image using SmartImage object
	public void setImage(final SmartImage image) {
		setImage(image, null, null, null);
	}

	public void setImage(final SmartImage image,
			final SmartImageTask.OnCompleteListener completeListener) {
		setImage(image, null, null, completeListener);
	}

	public void setImage(final SmartImage image, final Integer fallbackResource) {
		setImage(image, fallbackResource, fallbackResource, null);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource,
			SmartImageTask.OnCompleteListener completeListener) {
		setImage(image, fallbackResource, fallbackResource, completeListener);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource, final Integer loadingResource) {
		setImage(image, fallbackResource, loadingResource, null);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource, final Integer loadingResource,
			final SmartImageTask.OnCompleteListener completeListener) {
		// Set a loading resource
		if (loadingResource != null) {
			setImageResource(loadingResource);
		}

		// Cancel any existing tasks for this image view
		if (currentTask != null) {
			currentTask.cancel();
			currentTask = null;
		}

		// Set up the new task
		currentTask = new SmartImageTask(getContext(), image);
		currentTask
				.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
					@Override
					public void onComplete(Bitmap bitmap) {
						if (bitmap != null) {
							switch (type) {
							// �����TYPE_CIRCLE����Բ��
							case TYPE_CIRCLE:
								int min = Math.min(bitmap.getWidth(),
										bitmap.getHeight());
								mSrc = Bitmap.createScaledBitmap(bitmap,
										bitmap.getWidth(), bitmap.getHeight(),
										false);
								mSrc = createCircleImage(bitmap, min);
								break;
							case TYPE_ROUND:
								mSrc = createRoundConerImage(bitmap);
								break;
							}
							setImageBitmap(mSrc);
						} else {
							// Set fallback resource
							if (fallbackResource != null) {
								setImageResource(fallbackResource);
							}
						}
						if (completeListener != null) {
							completeListener.onComplete();
						}
					}
				});

		// Run the task in a threadpool
		threadPool.execute(currentTask);
	}

	public static void cancelAllTasks() {
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}
}