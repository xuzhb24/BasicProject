package com.android.frame.http.converter;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Converter;

import java.io.IOException;

/**
 * Created by xuzhb on 2019/12/10
 * Desc:处理响应报文，将Json转化成实体
 */
final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    //在这里根据业务逻辑处理响应报文
    //参考https://blog.csdn.net/u010386612/article/details/67637695
    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();  //注意value.string()不可重复使用
        try {
            JSONObject object = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //继续处理body数据反序列化
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } finally {
            value.close();
        }
    }
}
