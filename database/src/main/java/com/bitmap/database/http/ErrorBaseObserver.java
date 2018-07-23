package com.bitmap.database.http;

import android.accounts.NetworkErrorException;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.bitmap.database.base.BaseImpl;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.json.JSONException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2018/7/10.
 */
public abstract class ErrorBaseObserver<T> extends BaseObserver<T> {

    private static final String TAG = "ErrorBaseObserver";
    private boolean isNeedProgress;
    private String titleMsg;

    public ErrorBaseObserver(){
        this(null,null);
    }

    public ErrorBaseObserver(BaseImpl baseImpl) {
        this(baseImpl,null);
    }

    public ErrorBaseObserver(BaseImpl baseImpl,String titleMsg){
        super(baseImpl);
        this.titleMsg = titleMsg;
        if(TextUtils.isEmpty(titleMsg)){
            this.isNeedProgress = false;
        }else{
            this.isNeedProgress = true;
        }
    }

    @Override
    protected boolean isNeedProgressDialog() {
        return isNeedProgress;
    }

    @Override
    public String getTitleMsg() {
        return titleMsg;
    }

    @Override
    protected void onBaseError(Throwable throwable) {
        Log.d(TAG, "onBaseError: " + throwable.getMessage());
        StringBuffer sb = new StringBuffer();
        if(throwable instanceof NetworkErrorException || throwable instanceof UnknownHostException
                || throwable instanceof ConnectException){
            sb.append("网络异常");
        }else if(throwable instanceof SocketTimeoutException || throwable instanceof InterruptedIOException
                || throwable instanceof TimeoutException){
            sb.append("请求超时");
        }else if(throwable instanceof JsonSyntaxException){
            //Toast.makeText() 请求不合法
            return;
        }else if(throwable instanceof JsonParseException || throwable instanceof JSONException
                || throwable instanceof ParseException){
            sb.append("解析错误");
        }else if(throwable instanceof ApiException){
            if(((ApiException) throwable).isTokenExpried()){
                sb.append("Token出错");
            }
        }else {
            //showMessage();
            return;
        }
        //showMessage(sb.toString);
        Log.d(TAG, "onBaseError: " + sb.toString());
    }
}
