package com.bitmap.cachebitmap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2018/6/21.
 */
public class MySlidePanel extends RelativeLayout {

    private static final String TAG = "MySlidePanel";
    private FrameLayout mLayoutLeft;
    private FrameLayout mLayoutCenter;
    private FrameLayout mLayoutRight;
    private int touchSlop;
    private Scroller mScroller ;
    float downX = 0,downY = 0;
    public static final int STATE_DEAULT= -1;
    public static final int STATE_HORIZONTAL = 1;
    public static final int STATE_VERTICAL = 2;
    private int state = STATE_DEAULT;

    public MySlidePanel(Context context) {
        this(context,null);
    }

    public MySlidePanel(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MySlidePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        touchSlop = ViewConfiguration.getTouchSlop();
        mLayoutLeft = new FrameLayout(context);
        mLayoutCenter = new FrameLayout(context);
        mLayoutRight = new FrameLayout(context);
        mLayoutLeft.setBackgroundColor(Color.RED);
        mLayoutCenter.setBackgroundColor(Color.GREEN);
        mLayoutRight.setBackgroundColor(Color.RED);
       // mLayoutCenter.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mLayoutLeft);
        addView(mLayoutCenter);
        addView(mLayoutRight);
        mScroller = new Scroller(context,new AccelerateInterpolator());
        mLayoutCenter.setAlpha(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLayoutCenter.measure(widthMeasureSpec,heightMeasureSpec);
        //mLayoutOver.measure(widthMeasureSpec,heightMeasureSpec);
        int sizeW = mLayoutCenter.getMeasuredWidth();
        int leftSize = (int) (sizeW*0.7f);
        Log.d(TAG,"leftSize = " + leftSize);
        int realLeftSize = MeasureSpec.makeMeasureSpec(leftSize,MeasureSpec.EXACTLY);
        mLayoutLeft.measure(realLeftSize,heightMeasureSpec);
        mLayoutRight.measure(realLeftSize,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "onLayout: " + l +"..." + t +"..." + r + "..." + b +"..." + getTop());
        mLayoutCenter.layout(0,0,r-l,b-t);
       // mLayoutOver.layout(0,0,r-l,b-t);
        Log.d(TAG,mLayoutLeft.getMeasuredWidth()+"..");
        mLayoutLeft.layout(l-mLayoutLeft.getMeasuredWidth(),t-getTop(),l,b-t);
        mLayoutRight.layout(r,t-getTop(),r+mLayoutLeft.getMeasuredWidth(),b-t);
        Log.d(TAG, "onLayout: " + mLayoutCenter.getHeight()+ "..." + getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                Log.d(TAG,"down---" + downX + "..." + downY);
               return true;
            case MotionEvent.ACTION_MOVE:
                float currentX = ev.getX();
                float currentY = ev.getY();
                Log.d(TAG,"move---" + currentX + "..." + currentY);
                float distanceX = currentX - downX;
                float distanceY = currentY - downY;
                if(state == -1){ //初始状态先判断方向
                    if(Math.abs(distanceX) > touchSlop && Math.abs(distanceX) > Math.abs(distanceY)){//可以滑动
                        state = STATE_HORIZONTAL;
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }else if(Math.abs(distanceY) > touchSlop && Math.abs(distanceY) > Math.abs(distanceX)){
                        state = STATE_VERTICAL;
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                if(state == 1){ //当前是滑动状态
                    Log.d(TAG,"正在滑动。。。。" + mLayoutLeft.getWidth());
                    int scrollX = getScrollX();
                    float realX = -distanceX + scrollX;
                    if(Math.abs(realX) > mLayoutLeft.getWidth()){
                        break;
                    }
                    scrollTo((int) realX,0);
                }
                downX = currentX;
                downY = currentY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int currScrX = getScrollX();
                Log.d(TAG,"UP----" + currScrX);
                if(Math.abs(currScrX) > mLayoutLeft.getWidth()>>1 ){ //滑动大于一半时候
                    Log.d(TAG,"UP----1" + currScrX);
                    if(currScrX > 0){ //向右滑动
                        mScroller.startScroll(currScrX,0,mLayoutLeft.getWidth()-currScrX,0,200);
                    }else{
                        mScroller.startScroll(currScrX,0,-(mLayoutLeft.getWidth()+currScrX),0,200);
                    }
                }else {
                    mScroller.startScroll(currScrX,0,-currScrX,0,200);
                }
                invalidate();
                state = STATE_DEAULT;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            int toX = mScroller.getCurrX();
            scrollTo(toX,0); //会重复调用invilidate();
            invalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        int currX = getScrollX();
        mLayoutCenter.setAlpha((float)Math.abs(currX)/(float)mLayoutLeft.getWidth());
    }
}
