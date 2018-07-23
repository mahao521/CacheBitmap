package com.bitmap.skin;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bitmap.skin.utils.SkinResource;

import java.lang.reflect.Constructor;

/**
 * Created by Administrator on 2018/7/18.
 */

public class SkinThemeutils {

    private static final String TAG = "SkinThemeutils";
    private static int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {
            android.support.v7.appcompat.R.attr.colorPrimaryDark,android.support.v7.appcompat.R.attr.colorPrimary
    };
    private static int[] STATUSBAR_COLOR_ATTRS = {android.R.attr.statusBarColor,android.R.attr.navigationBarColor};
    private static int[] TYPEFACE_ATTRS = {R.attr.skinTypeface};

    public static int[] getResId(Context context,int[] attrs){
        int[] resIds = new int[attrs.length];
        TypedArray a = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < attrs.length; i++) {
            resIds[i] = a.getResourceId(i,0);
        }
        a.recycle();
        return resIds;
    }

    public static Typeface getSkinTypeface(Activity activity){
        int skinTypefaceId = getResId(activity,TYPEFACE_ATTRS)[0];
        return SkinResource.getInstance().getTypeFace(skinTypefaceId);
    }

    public static void updateStatusBarColor(Activity activity){
        //只有5.0以上才可以修改
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
           return ;
        }
        //或得startusBarColor 与 navavigationBarColor(状态栏颜色）
        //当与colorPrimaryDark 不同时，以statusBarColor为准
        int[] statusBarColorResId = getResId(activity,STATUSBAR_COLOR_ATTRS);
        //如果直接在style中写固定的颜色值而不是（@color/xxx）获得0
        if(statusBarColorResId[0] != 0){
            activity.getWindow().setStatusBarColor(SkinResource.getInstance().getColor(statusBarColorResId[0]));
        }else {
            //获取colorPrimaryDark
            int colorPrimaryDarkResId = getResId(activity,APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0];
            if(colorPrimaryDarkResId != 0){
                activity.getWindow().setStatusBarColor(SkinResource.getInstance().getColor(colorPrimaryDarkResId));
            }
        }

        //获取colorPrimary
        int colorPrimary = getResId(activity,APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[1];
        Log.d(TAG, "updateStatusBarColor: " + colorPrimary);
        if(colorPrimary != 0){
            ((AppCompatActivity)activity).getSupportActionBar().setBackgroundDrawable(SkinResource.getInstance().getDrawable(colorPrimary));
        }

        if(statusBarColorResId[1] != 0){
            activity.getWindow().setNavigationBarColor(SkinResource.getInstance().getColor(statusBarColorResId[1]));
        }
    }
}











