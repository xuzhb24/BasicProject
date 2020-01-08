package com.android.frame.mvp.AATest.convert;

import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp;
import com.android.util.JsonUtil;
import com.android.util.LogUtil;
import com.google.gson.Gson;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class NewsFunction implements Function<ResponseBody, BaseListResponse<NewsListBeanMvp>> {

    private static final String TAG = "NewsFunction";

    @Override
    public BaseListResponse<NewsListBeanMvp> apply(ResponseBody responseBody) throws Exception {
        String body = responseBody.string();
        Gson gson = new Gson();
        OriginBean originBean = gson.fromJson(body, OriginBean.class);
        LogUtil.logLongTag(TAG, " \n===============转化前===============\n" +
                JsonUtil.formatJson(gson.toJson(originBean)));
        BaseListResponse response=new BaseListResponse();
        response.setCode(originBean.getCode());
        response.setMsg(originBean.getMessage());
        response.setData(originBean.getResult());
        LogUtil.logLongTag(TAG, " \n===============转化前===============\n" +
                JsonUtil.formatJson(gson.toJson(response)));
        return response;
    }

    private static class OriginBean implements Serializable{

        private int code;
        private String message;
        private ArrayList<NewsListBeanMvp>result;

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

        public ArrayList<NewsListBeanMvp> getResult() {
            return result;
        }

        public void setResult(ArrayList<NewsListBeanMvp> result) {
            this.result = result;
        }
    }

}
