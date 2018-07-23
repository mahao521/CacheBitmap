package com.bitmap.skin;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.ArrayMap;
import android.view.LayoutInflater;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/7/18.
 */

public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private ArrayMap<Activity,SkinLayoutInflaterFactory> mLayoutFactory = new ArrayMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        //更新状态栏
        SkinThemeutils.updateStatusBarColor(activity);
        //更新字体
        Typeface typeface = SkinThemeutils.getSkinTypeface(activity);
        //更新布局视图
        LayoutInflater inflater = LayoutInflater.from(activity);
        try{
            //Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
            //如设置过抛出一次
            //设置 mFactorySet 标签为false
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(inflater,false);
        }catch(Exception e){
            e.printStackTrace();
        }
        //使用factory2设置需要加载的布局
        SkinLayoutInflaterFactory skinLayoutInflaterFactory = new SkinLayoutInflaterFactory(activity,typeface);
        LayoutInflaterCompat.setFactory2(inflater,skinLayoutInflaterFactory);
        mLayoutFactory.put(activity,skinLayoutInflaterFactory);
        SkinManager.getInstance().addObserver(skinLayoutInflaterFactory);

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SkinLayoutInflaterFactory observer = mLayoutFactory.remove(activity);
        SkinManager.getInstance().deleteObserver(observer);
    }
}
