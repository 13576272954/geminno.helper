

package com.example.administrator.helper.utils;

//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.ImageView;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageLoader {
    Context context;
    LruCacheUtil lruCacheUtil;
    DiskLruCacheHelper diskLruCacheUtil;

    public ImageLoader(Context context) {
        this.context = context;
        lruCacheUtil = new LruCacheUtil();
        diskLruCacheUtil = new DiskLruCacheHelper(context);
    }

    public void showImageByUrl(String url, ImageView iv) {

        //从内存缓存取
        if (getBitmapFromLruCache(url, iv)) {

            Log.i("MyImageLoader", "MyImageLoader: showImageByUrl:内存缓存中取到");
            return;
        } else if (getImageFromDiskLrucache(url, iv)) {
            //从硬盘缓存取
            Log.i("MyImageLoader", "MyImageLoader: showImageByUrl:硬盘缓存中取到");
            return;
        } else {
            //从网络取
            getImageFromInternet(url, iv);
            Log.i("MyImageLoader", "MyImageLoader: showImageByUrl:网络获取");
        }

        //没有取到，1)AsyncTask获取图片，显示在Iv 2)加到lrucache
    }
    /*
       从缓存中取数据，判断是否取到
        */
    public boolean getBitmapFromLruCache(String url, ImageView iv) {
        //先从lrucache中取数据，取到==》显示在iv
        Bitmap bm = lruCacheUtil.readFromLruCache(url);
        if (bm != null) {
            //取到图片，直接显示
            iv.setImageBitmap(bm);
            Log.i("MyImageLoader", "MyImageLoader: getBitmapFromLruCache:内存缓存取：取到");
            return true;
        }
        Log.i("MyImageLoader", "MyImageLoader: getBitmapFromLruCache:内存缓存取：没有取到");
        return false;
    }
//从硬盘缓存读取数据，判断是否读取到数据

    // @return 如果读取到数据，1）直接显示 2）加入到内存缓存；
    //   没有读取到，返回false
    public boolean getImageFromDiskLrucache(String url, ImageView iv) {
        //读取disklrucache数据
        Bitmap bm = diskLruCacheUtil.readFromDiskLrucache(url);
        if (bm != null) {
            //1、显示
            iv.setImageBitmap(bm);
            //2、加到内存缓存
            lruCacheUtil.writeToLruCache(url, bm);
            Log.i("MyImageLoader", "MyImageLoader: getImageFromDiskLrucache:硬盘中取到，加到内存缓存中");
            return true;
        } else {
            Log.i("MyImageLoader", "MyImageLoader: getImageFromDiskLrucache:硬盘中没有取到");
            return false;
        }
    }


    //从网络获取图片,获取到，加到内存缓存
    public void getImageFromInternet(String url, ImageView iv) {
        MyAsyncTask myAsyncTask = new MyAsyncTask(iv);
        myAsyncTask.execute(url);//参数传给doInBackground
    }


    //定义异步任务
    class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        ImageView iv;
        String urlStr;

        public MyAsyncTask(ImageView iv) {
            this.iv = iv;
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            //第一个参数：url
            try {

                urlStr = params[0];
                URL url = new URL(urlStr);
                //访问网络，获取bitmap对象
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                //响应成功
                if (httpURLConnection.getResponseCode() == 200) {
                    InputStream is = httpURLConnection.getInputStream();//获取网络返回数据
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    return bitmap;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //1、显示iv
            iv.setImageBitmap(bitmap);
            //2、加到内存缓存
            Log.i("MyAsyncTask", "MyAsyncTask: onPostExecute:网络获取，加到内存缓存");
            Log.i("MyAsyncTask", "onPostExecute:  ooooo" +urlStr+"      "+bitmap);
            lruCacheUtil.writeToLruCache(urlStr, bitmap);//key:url

            //3、加到硬盘缓存
            Log.i("MyAsyncTask", "MyAsyncTask: onPostExecute:网络获取，加到硬盘缓存");
            diskLruCacheUtil.writeToDiskLruCache(urlStr, bitmap);
        }
    }
}
