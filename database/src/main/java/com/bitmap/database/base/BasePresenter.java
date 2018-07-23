package com.bitmap.database.base;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/6/30.
 */
public class BasePresenter<T> {

    private WeakReference<T> mView;
    protected void attachView(T view){
        mView = new WeakReference<T>(view);
    }
    protected T getView(){
        return isViewAttach() ? mView.get() : null;
    }
    public boolean isViewAttach(){
        return null != mView && null != mView.get();
    }
    protected void detachView(){
        if(null != mView){
            mView.clear();
            mView = null;
        }
    }
}
