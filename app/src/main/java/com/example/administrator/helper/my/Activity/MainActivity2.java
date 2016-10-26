package com.example.administrator.helper.my.Activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helper.Fragment.HomePageFragment;
import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.utils.UrlUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity2 extends AppCompatActivity {
    Bitmap bitmap ;
//    final int SELECT_FROM_ALBUM = 0;    // 从相册中选
//    final int SELECT_FROM_CAMERA = 1;  //从相机中选
//    final int PHOTO_REQUEST_CUT = 2;   //裁剪图片

//    String circleImageUrl = "true";//若用户修改了头像，则上传该信息，服务端会将图片地址写好，这里只需要传个信息，令其不为空即可
//    File circleImageFile;  //保存用户从相机或相册选取的文件
//    Uri uri; // 图片的Uri地址

    //    public static final int TAKE_PHOTO = 1;
//
//    public static final int CROP_PHOTO = 2;


    //相机拍摄照片和视频的标准目录
    private File file;
    private Uri imageUri;


    String items[]={"相册选择","拍照"};
    User user;

    public static final int SELECT_PIC=11;
    public static final int TAKE_PHOTO=12;
    public static final int CROP=13;






    private LinearLayout ll;


    int selectFruitIndex = 0;

    Toolbar toolbar;
    ImageView  imageView_touxiang;
    TextView tv_nickname;
    TextView tv_sginName;
    TextView tv_profession;
    TextView tv_phoneNumber;
    TextView jifen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

         jifen= (TextView) findViewById(R.id.jifen);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        Intent intent = getIntent();
        if (intent.getStringExtra("nickName") != null) {
            tv_nickname.setText(intent.getStringExtra("nickName"));
        }
        tv_sginName = (TextView) findViewById(R.id.tv_signName);
        Intent intent1 = getIntent();
        if (intent1.getStringExtra("signName") != null) {
            tv_sginName.setText(intent1.getStringExtra("signName"));
        }

        tv_phoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
        Intent intent3 = getIntent();
        if (intent3.getStringExtra("phoneNumber") != null) {
            tv_profession.setText(intent3.getStringExtra("phoneNumber"));
        }
        imageView_touxiang= (ImageView) findViewById(R.id.imageView_touxiang);



        if(Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED) ) {
            //目录，文件名Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            file = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
            imageUri = Uri.fromFile(file);
        }







            String url = UrlUtils.MYURL + "UserSelectServlet";//访问网络的url
        RequestParams requestParams = new RequestParams(url);
       // MyApplication myApplication= (MyApplication) getApplication();
        requestParams.addQueryStringParameter("userId", ((MyApplication)getApplication()).getUser().getId()+"");

        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("Main2Activity", "onSuccess: 222");
                Gson gson = new Gson();
                User user = gson.fromJson(result, User.class);
                if (user != null) {
                    tv_sginName.setText(user.getSign());
                    tv_nickname.setText(user.getName());
                    tv_phoneNumber.setText(user.getPhoneNumber());
                    Log.i("MainActivity2", "onSuccess: 积分++"+user.getPoints());

                    Log.i("MainActivity2", "onSuccess: 图片"+user.getImage());
                    x.image().bind(imageView_touxiang,UrlUtils.MYURL+"image/"+user.getImage());
                    jifen.setText(user.getPoints());

                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

        toolbar = (Toolbar) findViewById(R.id.toobar);
        toolbar.setNavigationIcon(R.mipmap.a2);
        //设置toolbar的导航图标点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        findViewById(R.id.item1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, XiugainicActivity.class);
                startActivity(intent);

            }
        });
        findViewById(R.id.item2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, Xiugaiqianm.class);
                startActivity(intent);

            }
        });


        findViewById(R.id.item4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, XiugaiHpone.class);
                startActivity(intent);
            }
        });


        View view = LayoutInflater.from(this).inflate(R.layout.item, null);


    }

    //拍照的
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }


    public void method(View view) {
        //修改头像
        new android.support.v7.app.AlertDialog.Builder(this).setTitle("选择").setItems(items,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                switch(which){
                    case 0:

                        //相册选择
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                        startActivityForResult(intent, SELECT_PIC);

                        break;


                    case 1:

                        //拍照:
//                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(intent2,TAKE_PHOTO);

                        break;
                }
            }
        }).show();
    }
    public void crop(Uri uri){
        //  intent.setType("image/*");
        //裁剪
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //请求吗， 2 结果码 3 jintent对象
        //
        switch (requestCode) {
            case SELECT_PIC:
                //相册选择
                if (data != null) {
                    crop(data.getData());

                }

                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    crop(Uri.fromFile(file));
                } else if (resultCode == RESULT_CANCELED) {

                }
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
    //显示图片，上传服务器
    public void showImage(Bitmap bitmap){
        imageView_touxiang.setImageBitmap(bitmap);//iv显示图片
        saveImage(bitmap);//保存文件
        uploadImage();//上传服务器


    }
    //将bitmap保存在文件中
    public void saveImage(Bitmap bitmap){
        FileOutputStream fos=null;
        try {
            fos=new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,fos);
    }
    //上传图片
    public void uploadImage(){

        RequestParams requestParams=new RequestParams(UrlUtils.MYURL+"UploadImageServlet");
        requestParams.setMultipart(true);
        //获取application的userId
        MyApplication myApplication= (MyApplication) getApplication();
        //传参数：userId
        requestParams.addQueryStringParameter("userId",myApplication.getUser().getId()+"");
        Log.i("MainActivity2", "uploadImage: "+myApplication.getUser().getId());
        requestParams.addBodyParameter("file",file);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("MainActivity2", "onSuccess: "+result);
                Gson gson=new Gson();
                User user=gson.fromJson(result,User.class);

                if (user != null) {
//                    tvRealName.setText(user.getUserName());
//                    tvRealSex.setText(user.getUserSex());
                    Log.i("MainActivity2", "onSuccess: ");




                }




            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
}



        //dialog.setMessage("上传照片");


//       dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//           @Override
//           public void onClick(DialogInterface dialog, int which) {
//
//           }
//       });


//
//        final String[] array = new String[]{"拍照", "从本地选择"};
//        //这里不能设置setMessage会和下面的列表冲突因为都是正文
//        new AlertDialog.Builder(this).setTitle("上传照片").setIcon(R.mipmap.ic).
//                setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which) {
//                        selectFruitIndex = which;
//                        if(which==0) {
//                            File outputImage = new File(Environment.getExternalStorageDirectory(),
//                                    "output_image.jpg");
//                            Log.i("mmmmmmm", "onClick:000011 ");
//                            try {
//                                if (outputImage.exists()) {
//                                    outputImage.delete();
//                                }
//                                outputImage.createNewFile();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            Log.i("mmmmmmm", "onClick:0000111122 ");
//                            imageUri = Uri.fromFile(outputImage);
//                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                            startActivityForResult(intent, TAKE_PHOTO);
//                            Log.i("mmmmmmm", "onClick:0001112233");
////                     if (resultCode == RESULT_OK) {
////                         Intent intent2 = new Intent("com.android.camera.action.CROP");
////                         intent.setDataAndType(imageUri, "image/*");
////                         intent.putExtra("scale", true);
////                         intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
////                         startActivityForResult(intent2, CROP_PHOTO);
////                     }
//                        }else {
//
//                            // 创建File对象，用于存贮选择的照片
//                            File outputImage1 = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
//                            try {
//                                if (outputImage1.exists()) {
//                                    outputImage1.delete();
//                                }
//                                Log.i("mmmmmmm", "onClick:3333333 ");
//                                outputImage1.createNewFile();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                Log.i("mmmmmmm", "onClick:10000 ");
//                            }
//                            imageUri = Uri.fromFile(outputImage1);
//                            Intent intent1 = new Intent("android.intent.action.GET_ CONTENT");
//                            intent1.setType("image/*");
//                            intent1.putExtra("crop", true);
//                            Log.i("mmmmmmm", "onClick:22222 ");
//                            intent1.putExtra("scale", true);
//                            intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                            Log.i("mmmmmmm", "onClick:5555 ");
//                            startActivityForResult(intent1, CROP_PHOTO);
//
//                            Log.i("mmmmmmm", "onClick:44444 ");
//                        }
//
//
//                        //这是个展示选中
////                Toast.makeText(DialogActivity.this,arrayFruit[which],Toast.LENGTH_SHORT).show();
//                    }
//                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
////               Toast.makeText(DialogActivity.this,arrayFruit[selectFruitIndex],Toast.LENGTH_SHORT);
////               Log.i("checkbox",arrayFruit[selectFruitIndex]);
//            }
//        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//
//            }
//        }).show();

//        final CharSequence[] items = {"从本地选择", "拍照"};//CharSequence是接口，String实现
//        new AlertDialog.Builder(this).setTitle("上传照片").setIcon(R.mipmap.ic).
//                setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == SELECT_FROM_ALBUM) {//点击从相册选区
//                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                            intent.addCategory(Intent.CATEGORY_OPENABLE);
//                            intent.setType("image/*");  //设置图片类型
//                            startActivityForResult(Intent.createChooser(intent, "选择图片"), SELECT_FROM_ALBUM);
//                        } else {//点击拍照
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(intent, SELECT_FROM_CAMERA);
//
//                        }
//                    }
//                }).create().show();
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && requestCode == SELECT_FROM_ALBUM) {//从相册选择
//            //Uri uri = data.getData();
//            uri = data.getData();
//            ContentResolver cr = this.getContentResolver();
//            //Log.i("CreateTaoquanActivity", "onActivityResult: "+uri);
//            try {
//                if (bitmap != null) {
//                    bitmap.recycle();//如果不释放的话，不断取图片，将会内存不够
//                }
//
//                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                //暂时写入SD卡
//                writeImageToSdCard();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            //设置从相册选取的图片显示在ImageView上面
//            imageView_touxiang.setImageBitmap(bitmap);
//
//        } else if (resultCode == RESULT_OK && requestCode == SELECT_FROM_CAMERA) {//拍照选择
//            if (bitmap != null) {
//                bitmap.recycle();
//            }
//            //设置拍照后的图片显示在ImageView上面
//            bitmap = data.getParcelableExtra("data");
//            //crop(Uri.fromFile(circleImageFile));//将拍照后的图片裁剪并保存
//
//
//            imageView_touxiang.setImageBitmap(bitmap);
//
//        } else{
//            Toast.makeText(MainActivity2.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//    //将从相册或者相机中选取的照片暂时写入SD卡,相应成功后会删掉
//    public void writeImageToSdCard(){
//        //开子子线程，将图片暂时写入SD卡
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//
//                //将图片写到SD卡，判断是否有SD卡
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    //1、获取sd卡目录
//                    File sdFile = Environment.getExternalStorageDirectory();
//                    //2、获取想要存储的文件夹的路径
//                    File imageFile = new File(sdFile+"oyuanershou/image");
//                    if(!imageFile.exists()){//如果文件夹不存在，则创建该目录
//                        imageFile.mkdirs();
//                    }
//                    //3、获取文件完整目录
//                    circleImageFile = new File(imageFile, "Image.png");
//                    FileOutputStream fos = null;
//                    try {
//                       fos = new FileOutputStream(circleImageFile); //获得文件输出流
//                        bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);//将bitmap写入输出流
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//                        if(fos!=null){
//                            try {
//                                fos.flush();
//                                fos.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }else{
//                    Toast.makeText(MainActivity2.this, "未找到SD卡", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }.start();
//    }
//
//    //裁剪图片,以及进行一些设置
//    public void crop(Uri uri){
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri,"image/*");
//        //设置裁剪
//        intent.putExtra("crop",true);
//        //aspectX , aspectY :宽高的比例
//        intent.putExtra("aspectX",1);
//        intent.putExtra("aspectY",1);
//        //outputX , outputY : 裁剪图片宽高
//        intent.putExtra("outputX",250);
//        intent.putExtra("outputY",250);
//        //设置格式
//        intent.putExtra("outputFormat","JPEG");
//        intent.putExtra("noFaceDetection",true);
//        intent.putExtra("btn_return-data",true);
//
//        startActivityForResult(intent,PHOTO_REQUEST_CUT);
//    }

















