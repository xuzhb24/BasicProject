package com.android.frame.http.AATest;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.android.frame.http.AATest.bean.NewsListBean;
import com.android.frame.http.AATest.bean.WeatherBean;
import com.android.frame.http.ExceptionUtil;
import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.SchedulerUtil;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.util.CommonLayoutUtil;
import com.android.util.ExtraUtil;
import com.android.util.JsonUtil;
import com.android.widget.InputLayout;
import com.android.widget.TitleBar;
import com.google.gson.Gson;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuzhb on 2019/10/26
 * Desc:测试Retrofit
 */
public class TestRetrofitActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.il)
    InputLayout il;
    @BindView(R.id.tv)
    TextView tv;

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "测试Retrofit", true, true,
                "获取天气信息(@Query GET)", "获取天气信息(@QueryMap GET)", "获取网易新闻(@Field POST)",
                "获取网易新闻(@FieldMap POST)", "获取网易新闻(@Body POST)", "访问百度网址(GET)");
        String city = "北京";
        il.setInputText(city);
        il.getEditText().setSelection(city.length());  //将光标移至文字末尾
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_layout;
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6})
    public void onViewClicked(View view) {
        String city = il.getInputText().trim();
        switch (view.getId()) {
            case R.id.btn1:
                getWeatherByQuery(city);
                break;
            case R.id.btn2:
                getWeatherByQueryMap(city);
                break;
            case R.id.btn3:
                getWangYiNewsByField("1", "1");
                break;
            case R.id.btn4:
                getWangYiNewsByFieldMap("1", "2");
                break;
            case R.id.btn5:
                getWangYiNewsByBody("1", "3");
                break;
            case R.id.btn6:
//                accessUrl(UrlConstant.BAIDU_URL);
                accessUrlRxJava(UrlConstant.BAIDU_URL);
                break;
        }
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

}
