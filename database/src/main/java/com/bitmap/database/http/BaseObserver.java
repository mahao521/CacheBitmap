package com.bitmap.database.http;

import android.util.Log;

import com.bitmap.database.base.BaseImpl;
import com.bitmap.database.base.ProgressDialogHandler;

import java.security.BasicPermission;
import java.util.Observable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/7/10.
 */

public abstract class BaseObserver<T> implements Observer<T>{

    private static final String TAG = "BaseObserver";
    protected  abstract  void onBaseError(Throwable throwable);
    protected  abstract void onBaseNext(T data);
    protected  abstract boolean isNeedProgressDialog();
    protected abstract String getTitleMsg();
    private ProgressDialogHandler mDialogHandler;
    private BaseImpl mBaseImpl;

    public BaseObserver(BaseImpl baseImpl){
        mBaseImpl = baseImpl;
        if(mBaseImpl != null){
            if(null == mDialogHandler){
                mDialogHandler = new ProgressDialogHandler(baseImpl.getContext(),true);
            }
        }
    }

    public void showProgressDialog(){
        if(mDialogHandler != null){
            mDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG,getTitleMsg()).sendToTarget();
        }
    }

    public void disMissProgressDialog(){
        if(mDialogHandler != null){
            mDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG,getTitleMsg()).sendToTarget();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        //显示进度条
        if(isNeedProgressDialog()){
            showProgressDialog();
        }
        if(null != mBaseImpl){
            if(null != d){
                mBaseImpl.addDisposable(d);
            }
        }
    }

    @Override
    public void onNext(T t) {
        Log.d(TAG, "base onNext: ");
        if(t != null){
            onBaseNext(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        //关闭进度条
        if(isNeedProgressDialog()){
            disMissProgressDialog();
        }
        onBaseError(e);
    }

    @Override
    public void onComplete() {
        //关闭进度
        if(isNeedProgressDialog()){
            disMissProgressDialog();
        }
    }
}
















