package com.bitmap.skin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2018/7/18.
 */

public class SkinLayoutInflaterFactory  implements LayoutInflater.Factory2,Observer{

    private static final String TAG = "SkinLayoutInflaterFacto";
    private static  String[] mClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    //记录对应的View的构造函数
    private static final Map<String,Constructor<? extends View>> mConstructorMap = new HashMap<>();

    private static final Class<?>[] mConsructorSingature = new Class[]{
            Context.class,AttributeSet.class
    };

    private SkinAttribute mAttribute;
    private Activity mActivity;
    public  SkinLayoutInflaterFactory(Activity activity, Typeface typeface){
        this.mActivity = activity;
        mAttribute = new SkinAttribute(typeface);
    }

    /**
     *   创建对应的布局
     * @param parent  当前TAG父布局
     * @param name    在布局中的TAG 如 TextView, android.xx.x.toolbar
     * @param context 上下文
     * @param attrs 对应的TAG属性
     * @return  null ，则系统默认创建
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //换肤的过程就是替换view的属性
        View view = createViewFromTag(name,context,attrs);
        if(view == null){
            Log.d(TAG, "createView: " + name + " " + context.getClass().getName());
            view = createView(name,context,attrs);
        }
        if(null != view){
            Log.d(TAG, "loadAttr: " + name + " " + context.getClass().getName());
            mAttribute.Load(view,attrs);
        }
        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return null;
    }

    private View createViewFromTag(String  name, Context context, AttributeSet attrs) {
        //如果包含.则不是SDK中的View,可能是自定义View包括support包中的view
        if(-1 != name.indexOf(".")){
            return  null;
        }
        for (int i = 0; i < mClassPrefixList.length; i++) {
            return createView(mClassPrefixList[i]+name,context,attrs);
        }
        return null;
    }
    private View createView(String name, Context context, AttributeSet attrs) {
        Log.d(TAG, "createView: " + name);
        Constructor<? extends View> constructor = findConstructor(context,name);
        try {
            if(constructor != null){
                return constructor.newInstance(context,attrs);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = mConstructorMap.get(name);
        if(constructor == null){
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                Log.d(TAG, "findConstructor:  clazz " + clazz);
                constructor = clazz.getConstructor(mConsructorSingature);
                mConstructorMap.put(name,constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return constructor;
    }

    @Override
    public void update(Observable o, Object arg) {
        SkinThemeutils.updateStatusBarColor(mActivity);
        Typeface typeface = SkinThemeutils.getSkinTypeface(mActivity);
        mAttribute.setTypeface(typeface);
        mAttribute.applySkin();
    }
}
