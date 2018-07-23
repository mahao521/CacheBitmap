package com.bitmap.skin.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2018/7/18.
 */
public class SkinResource {

    private static SkinResource instance;
    private String mSkinPackName;
    private boolean isDefaultSkin = true;
    private Resources mResources;
    private Resources mSkinResource;

    public static void init(Context context){
        if(instance == null){
            synchronized (SkinResource.class){
                if(instance == null){
                    instance = new SkinResource(context);
                }
            }
        }
    }

    public static SkinResource getInstance(){
        return instance;
    }

    private SkinResource(Context context){
        mResources = context.getResources();
    }

    public void reset(){
        mSkinResource = null;
        mSkinPackName = "";
        isDefaultSkin = true;
    }

    public void applySkin(Resources resources,String packName){
        mSkinResource = resources;
        mSkinPackName = packName;
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(packName) || resources == null;
    }

    public int getIdentifier(int resId){
        if(isDefaultSkin){
            return resId;
        }
        //在皮肤包中不一定是当前程序的Id;
        //获取对应的名称，在当前的包中。
        String resName = mResources.getResourceEntryName(resId); //名字 colorPrimary
        String resType = mResources.getResourceTypeName(resId); //类型
        int skinId = mSkinResource.getIdentifier(resName,resType,mSkinPackName);
        return skinId;
    }

    public int getColor(int resId){
       if(isDefaultSkin){
           return mResources.getColor(resId);
       }
       int skinId = getIdentifier(resId);
       if(skinId == 0){
           return mResources.getColor(resId);
       }
       return mSkinResource.getColor(skinId);
    }

    public ColorStateList getStateList(int resId){
        if(isDefaultSkin){
            return mResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if(skinId == 0){
            return mResources.getColorStateList(resId);
        }
        return mSkinResource.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId){
        if(isDefaultSkin){
            return mResources.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if(skinId == 0){
            return mResources.getDrawable(resId);
        }
        return mSkinResource.getDrawable(skinId);
    }

    public Object getBackground(int resId){
        String resourceType = mResources.getResourceTypeName(resId);
        if(resourceType.equals("color")){
            return getColor(resId);
        }else {
            return getDrawable(resId);
        }
    }

    public String getString(int resId){
        if(isDefaultSkin){
            return mResources.getString(resId);
        }
        int skinId = getIdentifier(resId);
        if(skinId == 0){
            return mResources.getString(resId);
        }else {
            return mSkinResource.getString(skinId);
        }
    }

    public Typeface getTypeFace(int resId){
        String skinPath = getString(resId);
        if(TextUtils.isEmpty(skinPath)){
            return Typeface.DEFAULT;
        }
        try {
            Typeface typeface;
            if(isDefaultSkin){
                typeface = Typeface.createFromAsset(mResources.getAssets(),skinPath);
                return typeface;
            }
            typeface = Typeface.createFromAsset(mSkinResource.getAssets(),skinPath);
            return typeface;
        }catch (RuntimeException e){
            e.printStackTrace();
        }
       return Typeface.DEFAULT;
    }

}
