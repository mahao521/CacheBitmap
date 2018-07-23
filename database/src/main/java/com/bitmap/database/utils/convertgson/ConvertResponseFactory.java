package com.bitmap.database.utils.convertgson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * Created by Administrator on 2018/7/4.
 */

public class ConvertResponseFactory<T> implements Converter<ResponseBody,T> {

    private Gson mGson;
    private TypeAdapter<T> mAdapter;
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    ConvertResponseFactory(Gson gson, TypeAdapter adapter){
        this.mGson = gson;
        this.mAdapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String response = value.toString();
        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream,charset);
        JsonReader jsonReader = mGson.newJsonReader(reader);
        try{
            return mAdapter.read(jsonReader);
        }finally {
            value.close();
        }
    }
}
