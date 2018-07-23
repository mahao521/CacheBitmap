package com.bitmap.cachebitmap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2018/6/28.
 */
public class RevertView extends View {

    private static final String TAG = "RevertView";
    private Paint mPaint;
    private Boolean flag = false;

    public RevertView(Context context) {
        this(context,null);
    }

    public RevertView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RevertView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout: " + top + "...." + getTop() + "..." + getBottom());
    }

    public void refresh(){
        flag = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!flag){
            canvas.drawLine(200,getTop()+200,200,getTop()+400,mPaint);
            canvas.drawText("123456",300,300,mPaint);
        }else{
            Log.d(TAG, "onDraw: " + getTop());
            canvas.drawText("abc",200,200,mPaint);
            canvas.drawLine(100,getTop()+200,100,getTop()+400,mPaint);
        }
    }
}
