package com.bitmap.skin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/7/18.
 */

public class SkinPreference {
    private static final String SKIN_SHARED = "skins";
    private static final String KEY_SKIN_PATH = "skin-path";
    private static SkinPreference instance;
    private  SharedPreferences mPreferences;

    public static void init(Context context){
        if(instance == null){
            synchronized (SkinPreference.class){
                if (instance == null){
                    instance = new SkinPreference(context.getApplicationContext());
                }
            }
        }
    }

    public static SkinPreference getInstance(){
        return instance;
    }

    private SkinPreference(Context context){
        mPreferences = context.getSharedPreferences(SKIN_SHARED,Context.MODE_PRIVATE);
    }

    public void setSkin(String skinPath){
        mPreferences.edit().putString(KEY_SKIN_PATH,skinPath).apply();
    }

    public String getSkin(){
        return mPreferences.getString(KEY_SKIN_PATH,null);
    }
}












