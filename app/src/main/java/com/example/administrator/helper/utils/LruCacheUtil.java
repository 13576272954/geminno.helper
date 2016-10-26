package com.example.administrator.helper.utils;



import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 创建对象
 * 写入数据
 * 读取数据
 *
 */

public class LruCacheUtil {
    //key:图片的url； value:Bitmap
    LruCache<String,Bitmap> lruCache;

    public LruCacheUtil(){
      int maxSize=(int)Runtime.getRuntime().maxMemory()/8;//应用程序所占最大内容
        //参数：内存缓存的最大空间
        lruCache=new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //默认返回：内存缓存中元素个数
                //重写：返回的是某个元素的大小
                return value.getByteCount();
            }
        };

    }

    //写入数据:key-value
    public void writeToLruCache(String key,Bitmap bitmap){
        lruCache.put(key,bitmap);
    }


    //读取数据
    public Bitmap readFromLruCache(String key){
      return  lruCache.get(key);
    }


}
