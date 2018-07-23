package com.bitmap.cachebitmap.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telecom.ConnectionService;

/**
 * Created by Administrator on 2018/6/30.
 */

public class NetWorkUtils {

    public static boolean isNetWorkEnable(Context context){
        ConnectivityManager  connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null){
            return false;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info != null && info.isConnected()){
            //判断当前网络是否已经连接
            if(info.getState() == NetworkInfo.State.CONNECTED){
                return true;
            }
        }
        return false;
    }
}
