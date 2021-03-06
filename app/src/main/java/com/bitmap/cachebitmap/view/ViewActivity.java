package com.bitmap.cachebitmap.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bitmap.cachebitmap.R;

import java.util.Timer;
import java.util.TimerTask;

public class ViewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ViewActivity";
    private Timer mTimer;
    private Button mBtnClick;
    private SeekBar mSeekBar;
    private WindowManager mWindowManager;
    private View mView;
    private Boolean isShow = false;
    private RulerView mRulerView;
    private ImageView mImgeBit;
    private RevertView mRevertView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mSeekBar = findViewById(R.id.view_seek_bar);
        mBtnClick = findViewById(R.id.btn_click);
        mBtnClick.setOnClickListener(this);
        
        //获取屏幕密度
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int densityDpi = displayMetrics.densityDpi;
        float density = displayMetrics.density;
        Log.d(TAG, "onCreate: " + density + "....." + densityDpi);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(mSeekBar.getProgress() != mSeekBar.getMax()){
                    mSeekBar.incrementProgressBy(3);
                }else{
                    mTimer.cancel();
                    mTimer = null;
                }
            }
        },0,1000);

        mRulerView = findViewById(R.id.view_ruler);
        mImgeBit = findViewById(R.id.img_test);
        mRevertView = findViewById(R.id.view_revert);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
        if(mWindowManager!= null){
            mWindowManager.removeView(mView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_click:
                mSeekBar.incrementProgressBy(5);
                Bitmap bitmap = mRulerView.getBitmap();
                mImgeBit.setImageBitmap(bitmap);
                if (!isShow){
                    makeView();
                }else {
                    mWindowManager.removeView(mView);
                }
                isShow = !isShow;
                break;
        }
    }

    public  void  makeView(){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(1,1);
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mView = LayoutInflater.from(this).inflate(R.layout.window_layout,null);
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(mView,layoutParams);
        Button button = mView.findViewById(R.id.btn_window);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewActivity.this,"弹出对话框",Toast.LENGTH_SHORT).show();
                getViewWH();
            }
        });
    }

    /**
     *  获取宽高
     */
    public void getViewWH(){
        SlidingPanel slidingPanel = findViewById(R.id.slide_panel);
        MySlidePanel mySlidePanel = findViewById(R.id.slide_my_panel);
        mySlidePanel.measure(0,0);
        Log.d(TAG, "onCreate : " + mySlidePanel.getMeasuredHeight() + "  measure:" + mySlidePanel.getMeasuredWidth());
        Log.d(TAG, "onCreate : " + mySlidePanel.getHeight() + "  measure:" + mySlidePanel.getWidth());
    }

}



















