package com.bitmap.cachebitmap.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.Measure;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Scroller;
import android.widget.SeekBar;

import com.bitmap.cachebitmap.R;

import static java.lang.Math.abs;

/**
 * Created by Administrator on 2018/6/14.
 */

/**
 *   disptachEvent 比 ontouchEvent多了个ontouch事件
 *
 *   view 没有ontouchInterceEvent（）拦截方法
 *
 */

public class MySeekBar extends android.support.v7.widget.AppCompatSeekBar implements SeekBar.OnSeekBarChangeListener,View.OnTouchListener, GestureDetector.OnGestureListener {

    private static final String TAG = "MySeekBar";
    private Context mContext;
    private Bitmap mBitmapSrc;
    private Rect mRectSrc;
    private int mSrcWidth;
    private int mSrcHight;
    private Paint mPaint;
    private int mPaddingRight,mPaddingLeft;
    private int mTouchProgressOffset;
    private int mMin = 0;
    private boolean isDraging = false;
    private float mTouchDownX = 0;
    private float mTouchDownY = 0;
    private float mScaledTouchSlop = 0;
    private GestureDetector mDetector;
    private Scroller mScroller;

    public MySeekBar(Context context) {
        this(context,null);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        BitmapDrawable drawableSrc = (BitmapDrawable) context.getResources().getDrawable(R.drawable.tvplayer_progress_pro_music);
        mBitmapSrc = drawableSrc.getBitmap();
        mSrcWidth = mBitmapSrc.getWidth();
        mSrcHight = mBitmapSrc.getHeight();
        Log.d(TAG, "init: " + mSrcWidth + "..." + mSrcHight);
        this.setBackgroundResource(R.drawable.tvplayer_progress_bg_music);
        setMax(100);
        setProgress(20);
        mRectSrc = new Rect();
        this.setOnSeekBarChangeListener(this);
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
        setPressed(true);
        setEnabled(true);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int progress = getProgress();
        float ration = (float)progress / (float)getMax();
        int rectWidth = (int) (ration* getWidth());
        mRectSrc.left = 0;
        mRectSrc.top = 0;
        mRectSrc.bottom = mSrcHight;
        mRectSrc.right  = rectWidth;
        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        canvas.drawBitmap(mBitmapSrc,mRectSrc,mRectSrc,mPaint);

        setOnTouchListener(this);
        mDetector = new GestureDetector(this);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure:    "  + sizeW);
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int widthMea = MeasureSpec.makeMeasureSpec(point.x,MeasureSpec.EXACTLY);
        int heightMea = MeasureSpec.makeMeasureSpec(sizeH,MeasureSpec.EXACTLY);
        Log.d(TAG, "onMeasure: " + widthMea + "...." + heightMea);
        setMeasuredDimension(widthMea,heightMea);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG, "onProgressChanged:        1  " + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStartTrackingTouch:     2  " + seekBar.getProgress());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStopTrackingTouch:      3  " + seekBar.getProgress());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        Log.d(TAG, "dispatchTouchEvent: ");
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = event.getX();
                mTouchDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getX();
                if (abs(currentX - mTouchDownX) > mScaledTouchSlop) {
                    startDrag(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                setPressed(false);
                onStopTrackingTouch();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (isDraging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                break;
        }
        return true;
    }


    private void onStopTrackingTouch() {
        isDraging = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
        return false;
    }

    private void startDrag(MotionEvent event) {
        setPressed(true);
        attemptClaimDrag();
        float x = event.getX();
        float offsetX = x - mTouchDownX;
        float realW = Math.abs(offsetX)/ getWidth() * getMax();
        Log.d(TAG, "startDrag: " + realW);
        if(offsetX > 0){
            this.incrementProgressBy((int) realW);
        }else if(offsetX < 0){
            this.incrementProgressBy(-(int) realW);
        }
        mTouchDownX = x;
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    /**
     *   是在OntouchEvent中调用；
     * @param v
     * @param event
     * @return
     */
   @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch:  " );
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    private float mCurrentX = 0;
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, "onScroll: " + distanceX+"......"+distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            int scrollX = getScrollX();
            Log.d(TAG, "computeScroll: " + scrollX);
        }
    }

}
