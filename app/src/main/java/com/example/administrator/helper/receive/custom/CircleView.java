package com.example.administrator.helper.receive.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Task;
import com.example.administrator.helper.receive.utils.DisplayUtils;
import com.example.administrator.helper.utils.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Mr_immortalZ on 2016/5/3.
 * email : mr_immortalz@qq.com
 */
public class CircleView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private float radius = DisplayUtils.dp2px(getContext(),9);//半径
    private float disX;//位置X
    private float disY;//位置Y
    private float angle;//旋转的角度
    private float proportion;//根据远近距离的不同计算得到的应该占的半径比例
    Bitmap photo;
    String url_s;
    Task task;
    Bitmap bitmap = null;
    InputStream is;
    ImageLoader myImageLoader;
    public float getProportion() {
        return proportion;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getDisX() {
        return disX;
    }

    public void setDisX(float disX) {
        this.disX = disX;
    }

    public float getDisY() {
        return disY;
    }

    public void setDisY(float disY) {
        this.disY = disY;
    }


    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.bg_color_pink));
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = DisplayUtils.dp2px(getContext(),18);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(radius, radius, radius, mPaint);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, null, new Rect(0, 0, 2 * (int) radius, 2 * (int) radius), mPaint);
        }
    }

    public void setPaintColor(int resId) {
        mPaint.setColor(resId);
        invalidate();
    }

     public void setPortraitIcon(int resId) {

        mBitmap = BitmapFactory.decodeResource(getResources(), resId);
        invalidate();
    }


    public void setPortraitIcon(String url) {
        returnBitMap(url);

//  /      mBitmap = BitmapFactory.decodeResource(getResources(), resId);
        invalidate();
    }

    public final void returnBitMap(final String url) {

        new Thread(){
            @Override
            public void run() {
                super.run();
                URL myFileUrl = null;


                try {
                    myFileUrl = new URL(url);
                    HttpURLConnection conn;

                    conn = (HttpURLConnection) myFileUrl.openConnection();

                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    mBitmap  = BitmapFactory.decodeStream(is);//获取网络的图片源
                    postInvalidate();//刷新界面


                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }  catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }.start();

    }



//    public static String getRealFilePath( final Context context, final Uri uri ) {
//        if ( null == uri ) return null;
//        final String scheme = uri.getScheme();
//        String data = null;
//        if ( scheme == null )
//            data = uri.getPath();
//        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
//            data = uri.getPath();
//        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
//            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
//            if ( null != cursor ) {
//                if ( cursor.moveToFirst() ) {
//                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
//                    if ( index > -1 ) {
//                        data = cursor.getString( index );
//                    }
//                }
//                cursor.close();
//            }
//        }
//        return data;
//    }
//    String type = UrlUtils.MYURL(intent.getType());
//
//    if (uri.getScheme().equals("file") && (type.contains("image/"))) {
//        String path = uri.getEncodedPath();
//
//        if (path != null) {
//            path = Uri.decode(path);
//
//            ContentResolver cr = this.getContentResolver();
//            StringBuffer buff = new StringBuffer();
//            buff.append("(")
//                    .append(MediaStore.Images.ImageColumns.DATA)
//                    .append("=")
//                    .append("'" + path + "'")
//                    .append(")");
//            Cursor cur = cr.query(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    new String[] { MediaStore.Images.ImageColumns._ID },
//                    buff.toString(), null, null);
//            int index = 0;
//            for (cur.moveToFirst(); !cur.isAfterLast(); cur
//                    .moveToNext()) {
//                index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
//                // set _id value
//                index = cur.getInt(index);
//            }
//            if (index == 0) {
//                //do nothing
//            } else {
//                Uri uri_temp = Uri
//                        .parse("content://media/external/images/media/"
//                                + index);
//
//                if (uri_temp != null) {
//                    uri = uri_temp;
//                }
//            }
//        }
//    }

//    public  Bitmap getbitmap(String imageUri) {
//        Log.v("111", "getbitmap:" + imageUri);
//        // 显示网络上的图片
//
//        try {
//            URL myFileUrl = new URL(imageUri);
//            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
//            conn.setDoInput(true);
//            conn.connect();
//             is = conn.getInputStream();
//            bitmap = BitmapFactory.decodeStream(is);
////            mBitmap = BitmapFactory.decodeResource(getResources(), resId);
//            is.close();
//
//            Log.v("111", "image download finished." + imageUri);
//        } catch (OutOfMemoryError e) {
//            e.printStackTrace();
//            bitmap = null;
//        } catch (IOException e) {
//            e.printStackTrace();
////            Log.v("111", "getbitmap bmp fail---");
//            bitmap = null;
//        }
//        return bitmap;
//    }


    public void clearPortaitIcon(){
        mBitmap = null;
        invalidate();
    }
}
