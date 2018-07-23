package com.bitmap.database.http;

import com.bitmap.database.utils.convertgson.ConvertGsonFactory;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2018/7/4.
 */

public abstract class BaseRetrofit {

    protected Retrofit mRetrofit;
    private static final int DEFAULT_TIME= 20;
    private final int RETRY_TIMES = 0;

    public BaseRetrofit(){
      //创建okHttpClient
        if(null == mRetrofit){
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
            builder.connectTimeout(DEFAULT_TIME,TimeUnit.SECONDS);
            builder.addInterceptor(getApiHeader());
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(HttpServletAddress.getInstance().getServletAddress())
                    .client(okHttpClient)
                    .addConverterFactory(ConvertGsonFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    protected abstract Interceptor getApiHeader();

    protected abstract boolean isDebug();

    protected <T> void toSubscribe(Observable<T> observable, Observer<T> observer){

          observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .timeout(DEFAULT_TIME, TimeUnit.SECONDS)
                  .retry(RETRY_TIMES)
                  .subscribe(observer);
    }


    protected  static <T> T getPresent(Class<T> cls){

        T instance = null;
        try {
            instance = cls.newInstance();
            if(instance == null){
                return null;
            }
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}














