package com.bitmap.database.utils.function;

import com.bitmap.database.http.ApiException;
import com.bitmap.database.http.BaseResponse;

import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2018/7/10.
 */

public class HttpFunction<T> implements Function<BaseResponse<T>,T> {

    @Override
    public T apply(BaseResponse<T> tBaseResponse) throws Exception {

        if(!tBaseResponse.isRequestSuccess()){
            throw  new ApiException(tBaseResponse.getStatus(),String.valueOf(tBaseResponse.getStatus()));
        }
        return (T)tBaseResponse.getData();
    }
}
