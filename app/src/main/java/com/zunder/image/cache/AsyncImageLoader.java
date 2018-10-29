package com.zunder.image.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.AppTools;

public class AsyncImageLoader {

	private  HashMap<String, SoftReference<Drawable>> imageCache;
	private  BlockingQueue queue;
	private  ThreadPoolExecutor executor;
	private static AsyncImageLoader loader;
	Context context;

	
	private AsyncImageLoader(){};
	
	public static AsyncImageLoader getAsyncImageLoader(Context c)
	{
		if(loader==null){
			
			synchronized (AsyncImageLoader.class) {
				
				loader = new AsyncImageLoader();
				loader.imageCache = new HashMap<String, SoftReference<Drawable>>();
				loader.context = c;
				// 线程池：最大100条，每次执行：10条，空闲线程结束的超时时间：60秒
				loader.queue = new LinkedBlockingQueue();
				loader.executor = new ThreadPoolExecutor(40, 100, 20, TimeUnit.SECONDS, loader.queue);
			}
		}
		
		return loader;
		
	}

	public void loadDrawable(final Drawable errorImg, final String imageUrl,
			final ImageCallback imageCallback) {
		Drawable drawable = null;
		String[] array = imageUrl.split("/");
		final String key = array[array.length - 2];
		if (imageCache.containsKey(key)) {
			SoftReference<Drawable> softReference = imageCache.get(key);
			drawable = softReference.get();
			if (drawable != null) {
				imageCallback.imageLoaded(drawable);
				return;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj);
			}
		};

		// 用线程池来做下载图片的任务
		executor.execute(new Runnable() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(errorImg, imageUrl);
				if (drawable == null) {
					return;
				}
				imageCache.put(key, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		});

	}

	// 网络图片先下载到本地cache目录保存，以imagUrl的图片文件名保存。如果有同名文件在cache目录就从本地加载
	public Drawable loadImageFromUrl(Drawable errorImg, String imageUrl) {

		if (executor.isShutdown()) {
			return null;
		}
		Drawable drawable = null;
		String[] array = imageUrl.split("/");
		String fileName = array[array.length - 2];
		if (checkSDCard()) {
			final File targetFile = new File(Constants.CACHE_DIR, fileName);
			File dirs = new File(Constants.CACHE_DIR);
			/*if (!dirs.exists()) {
				dirs.mkdirs();
			}*/
			if (!targetFile.exists()) {
				try {
					targetFile.createNewFile();

					final InputStream is = AppTools.getNetInputStream(imageUrl);
					FileOutputStream fos = new FileOutputStream(targetFile);
					int len = -1;
					while ((len = is.read()) != -1) {
						fos.write(len);
					}
					fos.close();
					is.close();

					drawable = Drawable.createFromStream(new FileInputStream(
							targetFile), "");
				} catch (IOException e) {
					drawable = errorImg;
				}
			} else {
				try {
					drawable = Drawable.createFromStream(new FileInputStream(
							targetFile), "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {

			InputStream is;
			try {
				is = AppTools.getNetInputStream(imageUrl);
				drawable = Drawable.createFromStream(is, "src");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return drawable;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable);
	}

	public static boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	public void stopThreadPool() {
		executor.shutdown();
		queue.clear();
	}
}
