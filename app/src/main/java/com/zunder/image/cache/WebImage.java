package com.zunder.image.cache;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class WebImage implements SmartImage {
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 10000;

	private static WebImageCache webImageCache;

	private String url;

	public WebImage(String url) {
		this.url = url;
	}

	public Bitmap getBitmap(Context context) {
		// Don't leak context
		if (webImageCache == null) {
			webImageCache = new WebImageCache(context);
		}

		// Try getting bitmap from cache first
		Bitmap bitmap = null;
		if (url != null) {
			bitmap = webImageCache.get(url);
			if (bitmap == null) {
				bitmap = getBitmapFromUrl(url);
				if (bitmap != null) {
					webImageCache.put(url, bitmap);
				}
			}
		}
		return bitmap;
	}

	private Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			bitmap = BitmapFactory
					.decodeStream((InputStream) conn.getContent());
			// if (bitmap.getHeight() > 2000 || bitmap.getWidth() > 2000) {
			// final BitmapFactory.Options options = new
			// BitmapFactory.Options();
			// options.inSampleSize = 8;
			// bitmap = BitmapFactory.decodeStream(
			// (InputStream) conn.getContent(), null, options);
			// } else if (bitmap.getHeight() > 1000 || bitmap.getWidth() > 1000)
			// {
			// final BitmapFactory.Options options = new
			// BitmapFactory.Options();
			// options.inSampleSize = 4;
			// bitmap = BitmapFactory.decodeStream(
			// (InputStream) conn.getContent(), null, options);
			// } else if (bitmap.getHeight() > 500 || bitmap.getWidth() > 500) {
			// final BitmapFactory.Options options = new
			// BitmapFactory.Options();
			// options.inSampleSize = 2;
			// bitmap = BitmapFactory.decodeStream(
			// (InputStream) conn.getContent(), null, options);
			// }
			// bitmap = decodeBitmapFromFile((InputStream) conn.getContent(),
			// 300,
			// 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	@SuppressWarnings("unused")
	private Bitmap decodeBitmapFromFile(InputStream inputStream, int reqWidth,
			int reqHeight) {
		Bitmap bm = null;

		// First decode with inJustDecodeBounds=true to check dimensions
		final Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputStream, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeStream(inputStream, null, options);

		return bm;
	}

	private int calculateInSampleSize(Options options, int reqWidth,
			int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}

		return inSampleSize;
	}

	public static void removeFromCache(String url) {
		if (webImageCache != null) {
			webImageCache.remove(url);
		}
	}
}
