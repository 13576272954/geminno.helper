package com.example.administrator.helper.send.TimePicker;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.helper.R;
import com.github.florent37.viewanimator.ViewAnimator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/2.
 */
public class GetTimePicker{
    private TextView showView;
    private View cankao;
    private Context context;
    private Activity activity;

    private WheelMain wheelMainDate;
    private String beginTime;

    private java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public GetTimePicker(TextView showView,View cankao , Context context,Activity activity) {
        this.showView = showView;
        this.cankao = cankao;
        this.context = context;
        this.activity = activity;
    }

    public void showBottoPopupWindow() {
        WindowManager manager = (WindowManager)activity.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(context).inflate(R.layout.show_popup_window,null);
        final PopupWindow mPopupWindow = new PopupWindow(menuView, (int)(width*0.8),
                ActionBar.LayoutParams.WRAP_CONTENT);
        ScreenInfo screenInfoDate = new ScreenInfo(activity);
        wheelMainDate = new WheelMain(menuView, true);
        wheelMainDate.screenheight = screenInfoDate.getHeight();
        String time = DateUtils.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMainDate.initDateTimePicker(year, month, day, hours,minute);
        final String currentTime = wheelMainDate.getTime().toString();
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(cankao, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        ViewAnimator.animate(mPopupWindow.getContentView())
                .slideBottom()
                .duration(1000)
                .interpolator(new AccelerateInterpolator())
                .start();
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
        tv_pop_title.setText("选择起始时间");
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                beginTime = wheelMainDate.getTime().toString();
                try {
                    Date begin = dateFormat.parse(currentTime);
                    Date end = dateFormat.parse(beginTime);
                    showView.setText(DateUtils.formateStringH(beginTime,DateUtils.yyyyMMddHHmm)+":00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }
}
