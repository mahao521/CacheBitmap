package com.bitmap.database.http;

import com.bitmap.database.bean.ChannelListData;
import com.bitmap.database.bean.MainInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/7/5.
 */

public interface Api {

    @FormUrlEncoded
    @POST("MainServlet")
    Observable<BaseResponse<List<MainInfo>>> getArtile(@FieldMap Map<String,String> params);

    @POST("v1/search/channelOf360")
    Observable<BaseResponse<ChannelListData>> getChannel(@Body RequestBody body);
}
