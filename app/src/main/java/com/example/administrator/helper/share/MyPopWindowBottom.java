package com.example.administrator.helper.share;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.administrator.helper.R;
import com.example.administrator.helper.View.NoTouchLinearLayout;

/**
 * Created by Administrator on 2016/11/5.
 */
public class MyPopWindowBottom extends PopupWindow implements OnClickListener {

    private LayoutInflater layoutInflater;
    private MyPopWindowListener myPopWindowListener;
    private View popView;
    private Button butSend;
    private EditText mCommentEdittext;
    private NoTouchLinearLayout mLytEdittextVG;

    public MyPopWindowBottom(Context context , MyPopWindowListener myPopWindowListener){
        this.myPopWindowListener = myPopWindowListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init();
    }

    private void init(){
        popView = layoutInflater.inflate(R.layout.pup_comment,null);
        butSend = (Button) popView.findViewById(R.id.but_comment_send);
        mCommentEdittext = (EditText) popView.findViewById(R.id.edit_comment);
        mLytEdittextVG = (NoTouchLinearLayout) popView.findViewById(R.id.edit_vg_lyt);
        butSend.setOnClickListener(this);

        //添加PopupWindow布局
        this.setContentView(popView);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.update();
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.BottomPopWindowAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
//        //点击外部消失
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());

        popView.setOnTouchListener(new View.OnTouchListener() {//设置背景区域外为点击消失popwindow
            public boolean onTouch(View v, MotionEvent event) {

                int height = popView.findViewById(R.id.edit_vg_lyt).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        Log.i("MyPopWindowBottom", "onTouch:  0o0o0o0");
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_comment_send:
                Log.i("MyPopWindowBottom", "onClick:  11111111");
                myPopWindowListener.firstItem(mCommentEdittext);
            break;
        }
    }

    /**
     * 显示或隐藏输入法
     */
    public void onFocusChange(boolean hasFocus, final Context context) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        mCommentEdittext.getContext().getSystemService(context.INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    mCommentEdittext.requestFocus();//获取焦点
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    Log.i("123456789", "run:  隐藏");
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(mCommentEdittext.getWindowToken(),0);

                }
            }
        }, 100);
    }

}
