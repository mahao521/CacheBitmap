package com.bitmap.database.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2018/7/11.
 */
public class RecyclerViewWithEv extends RecyclerView {

    private static final String TAG = "RecyclerViewWithEv";
    private View emptyView;

    public RecyclerViewWithEv(Context context) {
        super(context);
    }

    public RecyclerViewWithEv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewWithEv(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     *  数据观察者AdapterDataObserver用来监听数据的变化
     */
    private final AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkEmptyView();
        }
    };

    private void checkEmptyView() {
        if(null == emptyView || null == getAdapter()){
            Log.d(TAG, "checkEmptyView: ");
            return ;
        }
        final boolean emptyViewVisiable = getAdapter().getItemCount() == 0;
        emptyView.setVisibility(emptyViewVisiable ? VISIBLE : GONE);
        setVisibility(emptyViewVisiable ? GONE : VISIBLE);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        if(oldAdapter != null){
            oldAdapter.unregisterAdapterDataObserver(mObserver);
        }
        super.setAdapter(adapter);
        if(adapter != null){
            adapter.registerAdapterDataObserver(mObserver);
        }
        checkEmptyView();
    }

    public void setEmptyView(View emptyView){
        this.emptyView = emptyView;
        checkEmptyView();
    }
}





































