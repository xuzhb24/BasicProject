package com.android.frame.mvvm.AATest.convert;

import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class NewsConverterFactory extends Converter.Factory {

    public static NewsConverterFactory create() {
        return new NewsConverterFactory(new Gson());
    }

    private final Gson gson;

    private NewsConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomRequestBodyConverter<>(gson, adapter);
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new NewsResponseBodyConverter(gson, (TypeAdapter<BaseListResponse<NewsListBean>>) adapter);
    }
}
