package com.android.frame.mvp.AATest.convert;

import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvp.AATest.bean.WeatherBeanMvp;
import com.android.util.JsonUtil;
import com.android.util.LogUtil;
import com.google.gson.Gson;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

import java.io.Serializable;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class WeatherFunction implements Function<ResponseBody, BaseResponse<WeatherBeanMvp>> {

    private static final String TAG = "WeatherFunction";

    @Override
    public BaseResponse<WeatherBeanMvp> apply(ResponseBody responseBody) throws Exception {
        String body = responseBody.string();
        Gson gson = new Gson();
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
        private WeatherBeanMvp data;

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

        public WeatherBeanMvp getData() {
            return data;
        }

        public void setData(WeatherBeanMvp data) {
            this.data = data;
        }

    }

}
