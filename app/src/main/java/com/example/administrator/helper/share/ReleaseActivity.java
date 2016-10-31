package com.example.administrator.helper.share;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.helper.MyApplication;

import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Share;
import com.example.administrator.helper.utils.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.internal.Streams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ReleaseActivity extends AppCompatActivity {
    Share share =new Share();
    TextView tvqx;
    TextView tvfb;
    EditText edtnr;
    ImageView imtuku;
    ImageView imtupian;

    String items[] = {"相册选择", "拍照"};
    public static final int SELECT_PIC = 11;
    public static final int TAKE_PHOTO = 12;
    public static final int CROP = 13;
    private File file;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_release);
        super.onCreate(savedInstanceState);

        initDate();

        //判断sd卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),getPhotoFileName());
            imageUri = Uri.fromFile(file);
            Log.i("ReleaseActivity", "onCreate: imageUri"+"---"+imageUri);
        }


        //点击事件
        //取消
        tvqx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
        //发表
        tvfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String share1=edtnr.getText().toString();//内容
                Log.i("ReleaseActivity", "onClick: share1"+"--"+share1);
                share.setShare(share1);
                MyApplication myApplication= (MyApplication) getApplication();
                int userID=myApplication.getUser().getId();
                Log.i("ReleaseActivity", "onClick: userID"+"--"+userID);
                share.setUserID(userID);//用户id
                Timestamp sentTime = new Timestamp(System.currentTimeMillis());//创建时间
                Log.i("ReleaseActivity", "onClick: sentTime"+"--"+sentTime);
                share.setSendTim(sentTime);
                String url = UrlUtils.MYURL+"InsertShareServlet";
                RequestParams requestParams = new RequestParams(url);
                Gson gson = new Gson();
                String sharejson = gson.toJson(share);
                requestParams.addBodyParameter("share", sharejson);
                Log.i("ReleaseActivity", "onClick:  share"+sharejson);
               requestParams.setMultipart(true);
               requestParams.addBodyParameter("file", file);
                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i("ReleaseActivity", "onSuccess: "+result);
                        finish();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.i("ReleaseActivity", "onError: "+ex);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Log.i("ReleaseActivity", "onCancelled: "+cex);
                    }

                    @Override
                    public void onFinished() {
                        Log.i("ReleaseActivity", "onFinished: --------");
                    }
                });
            }
        });



        //选择图片
        imtuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ReleaseActivity.this).setTitle("选择").setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        switch (which) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent, SELECT_PIC);
                                break;


                            case 1:
                                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                startActivityForResult(intent2, TAKE_PHOTO);
                                break;
                        }
                    }

                }).show();
            }

        });

    }
//找控件
    public void initDate(){
        tvqx= (TextView) findViewById(R.id.tv_quxiao);
        tvfb= (TextView) findViewById(R.id.tv_fabiao);
        edtnr= (EditText) findViewById(R.id.edt_nr);
        imtuku= (ImageView) findViewById(R.id.im_tuku);
        imtupian= (ImageView) findViewById(R.id.im_tupian);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SELECT_PIC:
                if (data != null) {
                    crop(data.getData());
                }
                break;
            case TAKE_PHOTO:
                crop(Uri.fromFile(file));
                break;
            case CROP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {

                        Bitmap bitmap = extras.getParcelable("data");
                        showImage(bitmap);

                    }
                    super.onActivityResult(requestCode, resultCode, data);
                }
        }
    }
    //拍照
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }
//编辑图片
    public void crop(Uri uri) {
        //  intent.setType("image/*");
        //裁剪
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX",100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP);
    }
    public void showImage(Bitmap bitmap) {
        imtupian.setImageBitmap(bitmap);//iv显示图片
        saveImage(bitmap);//保存图片
    }
    public void saveImage(Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("ReleaseActivity", "saveImage: "+fos);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
    }
}
