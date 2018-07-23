package com.bitmap.database.utils.convertgson;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2018/7/4.
 */
public class ConvertGsonFactory extends Converter.Factory {

    private Gson mGson;
    private ConvertGsonFactory(Gson gson){
        if(null == gson){
            throw  new NullPointerException("gson is  null");
        }
        this.mGson = gson;
    }

    public static ConvertGsonFactory create(){
        return create(new Gson());
    }

    public static ConvertGsonFactory create(Gson gson){
        return new ConvertGsonFactory(gson);
    }

    public @Nullable Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                     Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new ConvertResponseFactory<>(mGson,adapter);
    }

    public @Nullable Converter<?, RequestBody> requestBodyConverter(Type type,
                                                                    Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new ConvertRequestFactory<>(mGson,adapter);
    }
}
