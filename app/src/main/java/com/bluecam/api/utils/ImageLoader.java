package com.bluecam.api.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.zunder.smart.R;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ImageLoader {
    private static final int MAX_CAPACITY =20;


    private LinkedHashMap<String,Bitmap> firstCacheMap = new LinkedHashMap<String,Bitmap>(MAX_CAPACITY,0.75f,true)
    {
        @Override
        protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
            if(this.size()>MAX_CAPACITY)
            {
                //加入二级缓存
                secondCacheMap.put(eldest.getKey(),new SoftReference<Bitmap>(eldest.getValue()));
            }
            return false;
        }
    };


    //二级缓存：软引用（内存不足）

    private ConcurrentHashMap<String,SoftReference<Bitmap>> secondCacheMap = new ConcurrentHashMap<String,SoftReference<Bitmap>>();

    private ImageLoader(Context context)
    {

    }
    private ImageLoader()
    {}

    private static ImageLoader instance;
    public static ImageLoader getInstance()
    {
        if(instance == null)
        {
            instance = new ImageLoader();
        }
        return instance;
    }
    public boolean loadImage(String key, View view)
    {
        //读取缓存
        Bitmap bitmap = getFromCache(key);
        if(bitmap != null)
        {
            BitmapDrawable drawable = new BitmapDrawable(view.getContext().getResources(),bitmap);
            view.setBackgroundDrawable(drawable);
            return true;
        }
        else
        {
            view.setBackgroundResource(R.mipmap.device_list_item_bg);
            return false;
            //view.setColorFilter(Color.parseColor("#77000000"));
            //网络加载
        }
    }

    public void putImage(String key , Bitmap bitmap)
    {
        synchronized (firstCacheMap)
        {
            firstCacheMap.put(key,bitmap);
        }
    }

    private Bitmap getFromCache(String key) {
        //一级缓存
        synchronized (firstCacheMap)
        {
            Bitmap bitmap = firstCacheMap.get(key);
            if(bitmap != null){
                firstCacheMap.remove(bitmap);
                firstCacheMap.put(key,bitmap);
                return bitmap;
            }
        }
        //二级缓存
        SoftReference<Bitmap> soft_bitmap = secondCacheMap.get(key);
        if(soft_bitmap != null)
        {
            Bitmap bitmap = soft_bitmap.get();
            if(bitmap != null)
            {
                return bitmap;
            }
        }
        else
        {
            //软引用被回收了，从缓存中清除
            secondCacheMap.remove(key);
        }
        return null;
    }
}
