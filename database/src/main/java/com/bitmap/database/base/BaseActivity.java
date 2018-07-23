package com.bitmap.database.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bitmap.database.R;

import butterknife.ButterKnife;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity  implements BaseImpl{

    private  T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null == mPresenter){
            mPresenter = createPresenter();
        }
        setContentView(layoutRes());
        ButterKnife.bind(this);
        initView();
    }

    public abstract int layoutRes();

    public T createPresenter(){
        return null;
    }
    protected  abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detachView();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
