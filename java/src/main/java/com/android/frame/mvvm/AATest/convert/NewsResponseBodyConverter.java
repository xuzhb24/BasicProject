package com.android.frame.mvvm.AATest.convert;

import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.android.util.JsonUtil;
import com.android.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class NewsResponseBodyConverter implements Converter<ResponseBody, BaseListResponse<NewsListBean>> {

    private static final String TAG = "NewsResponseBodyConvert";

    private final Gson gson;
    private final TypeAdapter<BaseListResponse<NewsListBean>> adapter;

    public NewsResponseBodyConverter(Gson gson, TypeAdapter<BaseListResponse<NewsListBean>> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public BaseListResponse<NewsListBean> convert(ResponseBody value) throws IOException {
        String body = value.string();
        value.close();
        OriginBean originBean = gson.fromJson(body, OriginBean.class);
        LogUtil.logLongTag(TAG, " \n===============转化前===============\n" +
                JsonUtil.formatJson(gson.toJson(originBean)));
        BaseListResponse response = new BaseListResponse();
        response.setCode(originBean.getCode());
        response.setMsg(originBean.getMessage());
        response.setData(originBean.getResult());
        LogUtil.logLongTag(TAG, " \n===============转化前===============\n" +
                JsonUtil.formatJson(gson.toJson(response)));
        return response;
    }

    private static class OriginBean implements Serializable {

        private int code;
        private String message;
        private ArrayList<NewsListBean> result;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ArrayList<NewsListBean> getResult() {
            return result;
        }

        public void setResult(ArrayList<NewsListBean> result) {
            this.result = result;
        }
    }

}
