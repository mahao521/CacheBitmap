package com.bitmap.database.base;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/6/30.
 */

public interface BaseImpl {

    boolean addDisposable(Disposable dispoable);
    Context getContext();
}
