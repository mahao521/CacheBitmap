package com.bitmap.database.utils.convertgson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/7/4.
 */

public class ConvertRequestFactory<T> implements Converter<T, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private  Gson mGson;
    private  TypeAdapter<T> mAdapter;

    ConvertRequestFactory(Gson gson,TypeAdapter<T> adapter){
        this.mGson = gson;
        this.mAdapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = mGson.newJsonWriter(writer);
        mAdapter.write(jsonWriter,value);
        jsonWriter.close();
        return RequestBody.create(MEDIA_TYPE,buffer.readByteString());
    }
}
