package com.bitmap.database.http;

import com.bitmap.database.BuildConfig;
import com.bitmap.database.constant.BuildConfigs;

/**
 * Created by Administrator on 2018/7/10.
 */

public class ApiException extends RuntimeException {

    private long mErrorCode;
    public ApiException(long errorCode,String errorMessage){
        super(errorMessage);
        mErrorCode = errorCode;
    }

    /**
     *  判断token是否失效
     *
     * @return 失效返回true , 否则返回flase
     */
    public boolean isTokenExpried(){
        return mErrorCode == BuildConfigs.EXCETION_TOKEN;
    }
}
