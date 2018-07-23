package com.bitmap.database.http;

import android.text.TextUtils;

/**
 * Created by Administrator on 2018/7/4.
 */

public class HttpServletAddress {

    private String onlineAddress;
    private String  offlineAddress;

    private  static final class SingletonHolder{
         private static  final HttpServletAddress instance = new HttpServletAddress();
    }

    public static HttpServletAddress  getInstance(){
        return SingletonHolder.instance;
    }

    public String getOnlineAddress() {
        return onlineAddress;
    }

    public void setOnlineAddress(String onlineAddress) {
        this.onlineAddress = onlineAddress;
    }

    public String getOfflineAddress() {
        return offlineAddress;
    }

    public void setOfflineAddress(String offlineAddress) {
        this.offlineAddress = offlineAddress;
    }

    public String getServletAddress(){
        return TextUtils.isEmpty(onlineAddress) ? offlineAddress : onlineAddress;
    }
}
