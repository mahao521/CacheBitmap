package com.bitmap.skin;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.inputmethodservice.AbstractInputMethodService;
import android.text.TextUtils;
import android.util.Log;

import com.bitmap.skin.utils.SkinPreference;
import com.bitmap.skin.utils.SkinResource;

import java.lang.reflect.Method;
import java.util.Observable;

/**
 * Created by Administrator on 2018/7/18.
 */
public class SkinManager  extends Observable{

    private static final String TAG = "SkinManager";
    private static SkinManager instance;
    private Context mContext;

    public static void init(Application application){
        if(instance == null){
            synchronized (SkinManager.class){
                if(instance == null){
                    instance = new SkinManager(application);
                }
            }
        }
    }

    public static SkinManager getInstance(){
        return instance;
    }

    private SkinManager(Application application){
        this.mContext  = application;
        //记录当前使用的皮肤
        SkinPreference.init(application);
        //资源管理类，获取从APP中加载资源
        SkinResource.init(application);
        //注册Activity生命周期
        SkinActivityLifecycle lifecycle = new SkinActivityLifecycle();
        application.registerActivityLifecycleCallbacks(lifecycle);
        //加载皮肤
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    public void loadSkin(String skinPath){
        Log.d(TAG, "loadSkin: " + skinPath);
        if(TextUtils.isEmpty(skinPath)){
            //使用默认皮肤
            SkinPreference.getInstance().setSkin("");
            //清空资源管理器，皮肤资源属性
            SkinResource.getInstance().reset();
        }else {
            try {
                //反射创建AssertManager 与 Resource
                AssetManager assetManager = AssetManager.class.newInstance();
                //资源目录设置，目录或者压缩包
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",String.class);
                addAssetPath.invoke(assetManager,skinPath);
                Resources appResouce = mContext.getResources();
                //依据当前显示和配置（横竖屏，语言等）创建reource
                Resources skinResource = new Resources(assetManager,appResouce.getDisplayMetrics(),appResouce.getConfiguration());
                //记录
                SkinPreference.getInstance().setSkin(skinPath);
                //获取外部的APK的包名
                PackageManager manager = mContext.getPackageManager();
                PackageInfo info = manager.getPackageArchiveInfo(skinPath,PackageManager.GET_ACTIVITIES);
                String packageName = info.packageName;
                Log.d(TAG, "loadSkin: "+ packageName);
                SkinResource.getInstance().applySkin(skinResource,packageName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //通知采集的View,更新皮肤
        //被观察者改变，通知所有观察者
        setChanged();
        notifyObservers(null);
    }
}
