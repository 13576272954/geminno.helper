

package com.example.administrator.helper.utils;

//
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Environment;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DiskLruCacheHelper {

    DiskLruCache diskLruCache;

    public DiskLruCacheHelper(Context context){
        //1、创建disklrucache对象
        /**
         * 参数一：缓存路径：缓存根路径+image
         * 参数二：版本号
         * 参数三：1个key对应几个value
         * 参数四：硬盘缓存的大小：100m
         */
        try {
            diskLruCache= DiskLruCache.open(getDiskCacheDir(context,"image"),getAppVersion(context),1,100*1024*1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //2、向disklrucache写入数据
    public void writeToDiskLruCache(String url,Bitmap bitmap){
        //key:url做md5编码
        try {
            DiskLruCache.Editor editor=diskLruCache.edit(MD5Util.hashKeyForDisk(url));

            if(editor!=null) {
                OutputStream os = editor.newOutputStream(0);//获取key对应的第一个value()

                //bitmap数据，写入到outputstream;返回，是否写入成功
                boolean success = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

                if(success){
                    editor.commit();
                }else{
                    editor.abort();
                }
                diskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    //从disklrucache中取数据
    public Bitmap readFromDiskLrucache(String url){
        //key:做了Md5编码
        try {
            DiskLruCache.Snapshot snapshot= diskLruCache.get(MD5Util.hashKeyForDisk(url));

            if(snapshot!=null) {
                InputStream is = snapshot.getInputStream(0);//获取key对应的第一个value的输入流
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
  //获取硬盘缓存目录
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    //获取应用版本号：
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}


