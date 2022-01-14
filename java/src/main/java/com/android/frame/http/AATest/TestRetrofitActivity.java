package com.android.frame.http.AATest;

import android.os.Bundle;

import com.android.frame.http.AATest.bean.NewsListBean;
import com.android.frame.http.AATest.bean.WeatherBean;
import com.android.frame.http.ExceptionUtil;
import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.SchedulerUtil;
import com.android.frame.http.function.RetryWithDelay;
import com.android.frame.http.interceptor.MaxRetryInterceptor;
import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;
import com.android.util.ExtraUtil;
import com.android.util.JsonUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xuzhb on 2019/10/26
 * Desc:测试Retrofit
 */
public class TestRetrofitActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "测试Retrofit", true, false,
                "获取天气信息(@Query GET)", "获取天气信息(@QueryMap GET)", "获取网易新闻(@Field POST)",
                "获取网易新闻(@FieldMap POST)", "获取网易新闻(@Body POST)", "访问百度网址(GET)",
                "缓存GET请求", "清除缓存文件", "最多重试3次", "最多重试3次，每次间隔5秒请求");
        String city = "北京";
        binding.il.setInputText(city);
        binding.il.getEditText().setSelection(city.length());  //将光标移至文字末尾
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            String city = binding.il.getInputText().trim();
            getWeatherByQuery(city);
        });
        binding.btn2.setOnClickListener(v -> {
            String city = binding.il.getInputText().trim();
            getWeatherByQueryMap(city);
        });
        binding.btn3.setOnClickListener(v -> {
            getWangYiNewsByField("1", "1");
        });
        binding.btn4.setOnClickListener(v -> {
            getWangYiNewsByFieldMap("1", "2");
        });
        binding.btn5.setOnClickListener(v -> {
            getWangYiNewsByBody("1", "3");
        });
        binding.btn6.setOnClickListener(v -> {
//            accessUrl(UrlConstant.BAIDU_URL);
            accessUrlRxJava(UrlConstant.BAIDU_URL);
        });
        binding.btn7.setOnClickListener(v -> {
            String city = binding.il.getInputText().trim();
            testCache(city);
        });
        binding.btn8.setOnClickListener(v -> {
            if (RetrofitFactory.getInstance().clearCache()) {
                showToast("缓存已清除");
            } else {
                showToast("缓存清除失败");
            }
        });
        binding.btn9.setOnClickListener(v -> {
            String city = binding.il.getInputText().trim();
            testMaxRetry(city);
        });
        binding.btn10.setOnClickListener(v -> {
            String city = binding.il.getInputText().trim();
            testRetryWithDelay(city);
        });
    }

    //获取天气信息，@Query，GET请求
    private void getWeatherByQuery(String city) {
        RetrofitFactory.getInstance().createService(ApiService.class, UrlConstant.WEATHER_URL)
                .getWeatherByQuery(city)
                .compose(SchedulerUtil.ioToMain())
                .subscribe(WeatherObserver());
    }

    //获取天气信息，@QueryMap，GET请求
    private void getWeatherByQueryMap(String city) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("city", city);
        RetrofitFactory.getInstance().createService(ApiService.class, UrlConstant.WEATHER_URL)
                .getWeatherByQueryMap(map)
                .compose(SchedulerUtil.ioToMain())
                .subscribe(WeatherObserver());
    }

    private Observer<WeatherBean> WeatherObserver() {
        return new Observer<WeatherBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(WeatherBean bean) {
                if (bean.isSuccess()) {
                    String result = JsonUtil.formatJson(new Gson().toJson(bean));
                    ExtraUtil.alert(TestRetrofitActivity.this, result);
                } else {
                    showToast(bean.getDesc());
                }
            }

            @Override
            public void onError(Throwable e) {
                showToast(ExceptionUtil.convertExceptopn(e));
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    //获取网易新闻，@Field，POST请求
    private void getWangYiNewsByField(String page, String count) {
        RetrofitFactory.getInstance().createService(ApiService.class, UrlConstant.NEWS_URL)
                .getWangYiNewsByField(page, count)
                .compose(SchedulerUtil.ioToMain())
                .subscribe(NewsObserver());
    }

    //获取网易新闻，@FieldMap，POST请求
    private void getWangYiNewsByFieldMap(String page, String count) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("count", count);
        RetrofitFactory.getInstance().createService(ApiService.class, UrlConstant.NEWS_URL)
                .getWangYiNewsByFieldMap(map)
                .compose(SchedulerUtil.ioToMain())
                .subscribe(NewsObserver());
    }

    //获取网易新闻，@Body，POST请求
    private void getWangYiNewsByBody(String page, String count) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("count", count);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        String data = sb.toString().substring(0, sb.toString().length() - 1);
//        String data = "page=" + page + "&count=" + count;
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), data);
        RetrofitFactory.getInstance().createService(ApiService.class, UrlConstant.NEWS_URL)
                .getWangYiNewsByBody(body)
                .compose(SchedulerUtil.ioToMain())
                .subscribe(NewsObserver());
    }

    private Observer<NewsListBean> NewsObserver() {
        return new Observer<NewsListBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(NewsListBean bean) {
                if (bean.isSuccess()) {
                    String result = JsonUtil.formatJson(new Gson().toJson(bean));
                    ExtraUtil.alert(TestRetrofitActivity.this, result);
                } else {
                    showToast(bean.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                showToast(ExceptionUtil.convertExceptopn(e));
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    //访问网址，Retrofit
    private void accessUrl(String url) {
        RetrofitFactory.getInstance().createService(ApiService.class, url)
                .accessUrl()
                .enqueue(new Callback<ResponseBody>() {  //异步请求
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        showToast("访问成功！");
                        ExtraUtil.alert(TestRetrofitActivity.this, response.toString());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToast(ExceptionUtil.convertExceptopn(t));
                        t.printStackTrace();
                    }
                });
    }

    //访问网址，Retrofit + RxJava
    private void accessUrlRxJava(String url) {
        RetrofitFactory.getInstance().createService(ApiService.class, url)
                .accessUrlRxJava()
                .compose(SchedulerUtil.ioToMain())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        showToast("访问成功！");
                        try {
                            ExtraUtil.alert(TestRetrofitActivity.this, responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("访问失败，" + ExceptionUtil.convertExceptopn(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //测试缓存机制
    private void testCache(String city) {
        RetrofitFactory.getInstance().createService(ApiService.class, UrlConstant.WEATHER_URL, true)
                .getWeatherByQuery(city)
                .compose(SchedulerUtil.ioToMain())
                .subscribe(WeatherObserver());
    }

    //最多重试3次
    private void testMaxRetry(String city) {
        RetrofitFactory.getInstance().createService(ApiService.class, UrlConstant.WEATHER_URL,
                GsonConverterFactory.create(), new MaxRetryInterceptor(3), 30, false)
                .getWeatherByQuery(city)
                .compose(SchedulerUtil.ioToMain())
                .subscribe(WeatherObserver());
    }

    //最多重试3次，每次间隔5秒
    private void testRetryWithDelay(String city) {
        RetrofitFactory.getInstance().createService(ApiService.class, UrlConstant.WEATHER_URL)
                .getWeatherByQuery(city)
                .retryWhen(new RetryWithDelay(3, 5000))
                .compose(SchedulerUtil.ioToMain())
                .subscribe(WeatherObserver());
    }

}
