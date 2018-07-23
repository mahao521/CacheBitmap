package com.bitmap.database.http;

import android.text.TextUtils;

import com.bitmap.mylibrary.Handler;
import com.bitmap.mylibrary.classloader.ClassLoader;
import com.bitmap.mylibrary.constant.Constant;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/5.
 */

public class BaseModel extends BaseRetrofit {

    private static final String TAG = "BaseModel";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected Api mServertApi;
    protected Map<String,String> mMap = new HashMap<>();

    public  BaseModel(){
        mServertApi = mRetrofit.create(Api.class);
    }

    @Override
    protected Interceptor getApiHeader() {
        Interceptor apiInterHeader = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request.newBuilder()
                        .addHeader("Custom-Client-type","1")//自定义客户端类型
                        // .addHeader()
                        .build();
                return chain.proceed(request);
            }
        };
        return apiInterHeader;
    }

    @Override
    protected boolean isDebug() {
        return false;
    }

    protected HashMap<String ,String> getCommonMap(){
        HashMap<String,String> commMap = new HashMap<>(8);
        commMap.put("package", ClassLoader.PACK_NAME);
        commMap.put("versionName",ClassLoader.VERSION_CODE);
        commMap.put("channelId",ClassLoader.CHANNEL_ID);
        commMap.put("IMEL",ClassLoader.ANDROID_ID);
        commMap.put("AndroiId",ClassLoader.ANDROID_ID);
        commMap.put("SerialNo",ClassLoader.SERIAL_NO);
        commMap.put("os","android");
        commMap.put("ip", Constant.REAL_IP);
       return commMap;
    }

    public void  addParams(String key,String value){
        if(TextUtils.isEmpty(key)){
            return ;
        }
        mMap.put(key,value);
    }

    public void addParamsMap(HashMap<String,String> map){
        if(map == null){
            return ;
        }
        mMap.putAll(map);
    }
}
