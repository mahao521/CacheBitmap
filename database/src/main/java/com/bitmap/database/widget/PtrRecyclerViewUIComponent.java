package com.bitmap.database.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.bitmap.database.R;
import com.bitmap.database.widget.loadmore.OnPullToRefreshListener;
import com.bitmap.database.widget.loadmore.OnScrollToBottomLoadMoreListener;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;

/**
 * Created by Administrator on 2018/7/11.
 */

public class PtrRecyclerViewUIComponent  extends PtrClassicFrameLayout {

    private boolean canRefresh = true;
    private RecyclerViewWithEv mRecyclerView;
    private OnScrollToBottomLoadMoreListener mOnScrollToBottomLoadMoreListener;
    private OnPullToRefreshListener mOnPullToRefreshListener;
    public PtrRecyclerViewUIComponent(Context context) {
        super(context);
    }

    public PtrRecyclerViewUIComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrRecyclerViewUIComponent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollToBottomLoadMoreListener(OnScrollToBottomLoadMoreListener moreListener){
        this.mOnScrollToBottomLoadMoreListener = moreListener;
    }

    public void setOnPullToRefreshListener(OnPullToRefreshListener refreshListener){
        this.mOnPullToRefreshListener = refreshListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init(){
        mRecyclerView = (RecyclerViewWithEv) findViewById(R.id.ptr_recy_view);
        mRecyclerView.getItemAnimator().setChangeDuration(0);
        setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
               mOnPullToRefreshListener.onPullToRefresh();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if(!canRefresh){
                    return false;
                }
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,mRecyclerView,header);
            }
        });
        setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                mOnScrollToBottomLoadMoreListener.onScrollToBottomMore();
            }
        });
    }

    public <T extends RecyclerView.LayoutManager> void setLayoutManager(T layoutManager){
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public <T extends RecyclerView.Adapter> void setAdapter(T adapter){
        mRecyclerView.setAdapter(adapter);
    }

    public void setCanRefresh(boolean canRefresh){
        this.canRefresh = canRefresh;
    }

    public void setEmptyView(View view){
        mRecyclerView.setEmptyView(view);
    }

    public void setRecyclerViewDivider(RecyclerViewDivider divider){

    }
}
