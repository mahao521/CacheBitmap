package com.bitmap.database.base;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/7/3.
 */
public abstract class BaseLoadFragment  extends Fragment {

    private static final String TAG = "BaseFragment";

    public BaseActivity context;

    protected Unbinder mUnbinder;

    protected abstract int layoutRes();

    protected void onViewReallyCreated(View view) {
    }

    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == rootView) {
            rootView = inflater.inflate(layoutRes(), null);
            onViewReallyCreated(rootView);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (this.mUnbinder != null) {
            this.mUnbinder.unbind();
        }

        super.onDestroyView();
    }
}
