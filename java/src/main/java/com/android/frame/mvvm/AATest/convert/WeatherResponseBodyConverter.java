package com.android.frame.mvvm.AATest.convert;

import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.AATest.entity.WeatherBean;
import com.android.util.JsonUtil;
import com.android.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class WeatherResponseBodyConverter implements Converter<ResponseBody, BaseResponse<WeatherBean>> {

    private static final String TAG = "WeatherResponseBodyConv";

    private final Gson gson;
    private final TypeAdapter<BaseResponse<WeatherBean>> adapter;

    public WeatherResponseBodyConverter(Gson gson, TypeAdapter<BaseResponse<WeatherBean>> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public BaseResponse<WeatherBean> convert(ResponseBody value) throws IOException {
        String body = value.string();
        value.close();
        OriginBean originBean = gson.fromJson(body, OriginBean.class);
        LogUtil.logLongTag(TAG, " \n===============转化前===============\n" +
                JsonUtil.formatJson(gson.toJson(originBean)));
        BaseResponse response = new BaseResponse();
        response.setCode(originBean.getStatus() == 1000 ? 200 : originBean.getStatus());
        response.setMsg(originBean.getDesc());
        response.setData(originBean.getData());
        LogUtil.logLongTag(TAG, " \n===============转化前===============\n" +
                JsonUtil.formatJson(gson.toJson(response)));
        return response;
    }

    private static class OriginBean implements Serializable {

        private int status;
        private String desc;
        private WeatherBean data;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public WeatherBean getData() {
            return data;
        }

        public void setData(WeatherBean data) {
            this.data = data;
        }
    }

}
