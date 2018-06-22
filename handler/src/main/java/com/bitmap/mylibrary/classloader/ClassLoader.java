package com.bitmap.mylibrary.classloader;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 */

public class ClassLoader extends Application {

    private static final String TAG = "ClassLoader";
    private String app_name;
    private String app_version;
    private final static float UI_WIDTH = 1080;
    private final static float UI_HEIGHT = 1920;
    private final static int DENSITY_INCH = 5;
    private final static float BIG_SCREEN_FACTOR = 0.2f;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        getMetaData();
        //屏幕适配
        setMetrics();
    }

    /**
     *  适配不同屏幕
     */
    public void setMetrics(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int heidht = displayMetrics.heightPixels;
        float density = displayMetrics.density;
        int densityDpi = displayMetrics.densityDpi;  //根号下(宽像素的平方 + 高像素的平方) /  屏幕尺寸(斜边）
        Log.d(TAG, "setMetrics: " + width + "..."+ heidht + "..." + density + "..." + densityDpi);
        int uiDensityDpi = (int) Math.sqrt(UI_WIDTH*UI_WIDTH + UI_HEIGHT*UI_HEIGHT)/DENSITY_INCH;
        float uiDensity = uiDensityDpi/160;
        float ratio = Math.min(UI_WIDTH,UI_HEIGHT) / Math.min(width,heidht);
        float realDensity = uiDensity * ratio;
        if(realDensity > uiDensity){
            realDensity = uiDensity + (realDensity - uiDensity)*BIG_SCREEN_FACTOR;
        }
        displayMetrics.density = realDensity;
        Log.d(TAG, "setMetrics: " + ratio+ "..." + realDensity);
    }

    /**
     *  获取mainfest里面的app_version
     */
    private void getMetaData() {
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = applicationInfo.metaData;
            if(null != metaData){
                if(metaData.containsKey("app_name")){
                    app_name = metaData.getString("app_name");
                }
                if(metaData.containsKey("app_version")){
                    app_version = metaData.getString("app_version");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  加载dex文件集合
     * @param listFile
     * @param optimizedDirectory
     */
    public void loadDex(List<File> listFile,File optimizedDirectory) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {

       //1 获取系统的classLoader数组

        //1.1获取系统中的classloader的pathList类
        Field pathListField = findField(getClassLoader(),"pathList");
        Object pathList = pathListField.get(getClassLoader());

        //获取pathList类中的dexElements
        Field dexElementsField = findField(pathList,"dexElements");
        Object[] dexElements = (Object[]) dexElementsField.get(pathList);

        //创建新的element 数组，
        Method makeDexElements = findMethod(pathList,"makeDexElements",ArrayList.class,Field.class,ArrayList.class);
        ArrayList<IOException> suppressedExceptions = new ArrayList<>();
        Object[] addElements = (Object[]) makeDexElements.invoke(pathList,listFile,optimizedDirectory,suppressedExceptions);

        //合并两个数组
        Object[] newElements = (Object[]) Array.newInstance(dexElements.getClass().getComponentType(),dexElements.length+addElements.length);
        System.arraycopy(dexElements,0,newElements,0,dexElements.length);
        System.arraycopy(addElements,0,addElements.length,dexElements.length,addElements.length);

        //替换classLoader中的element数组；
        dexElementsField.set(pathList,newElements);
    }


    /**
     *   反射获得指定类的属性
     * @param instace
     * @param name
     * @return
     * @throws NoSuchFieldException
     */
    public static Field findField(Object instace,String name) throws NoSuchFieldException {
        Class clazz = instace.getClass();
        while (clazz != null){
            try {
                Field field = clazz.getDeclaredField(name);
                if(!field.isAccessible()){
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                //e.printStackTrace();
                //如果找不到，就在父类里面找
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("field" + name + "not found in" + instace.getClass());
    }

    public static Method findMethod(Object object,String name,Class... parameterTypes) throws NoSuchFieldException {
        Class clazz = object.getClass();
        while (clazz != null){
            try {
                Method method = clazz.getDeclaredMethod(name,parameterTypes);
                if(!method.isAccessible()){
                    method.setAccessible(true);
                }
                return method;
            } catch (NoSuchMethodException e) {
             //   e.printStackTrace();
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("method " + name + "with parameters " + Arrays.asList(parameterTypes).toString());
    }

}
