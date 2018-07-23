package com.bitmap.database.http;

import com.bitmap.mylibrary.constant.Constant;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/5.
 */

public class BaseResponse<T> implements Serializable{

    @SerializedName("code")
    private  long status;

    @SerializedName("msg")
    private String message;

    @SerializedName("data")
    private T data;

    /**
     *  API是否请求成功
     * @return
     */
    public boolean isRequestSuccess(){
         return status == Constant.REQUEST_SUCESS;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
