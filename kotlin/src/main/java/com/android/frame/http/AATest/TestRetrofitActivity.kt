package com.android.frame.http.AATest

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.http.AATest.bean.NewsListBean
import com.android.frame.http.AATest.bean.WeatherBean
import com.android.frame.http.ExceptionUtil
import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.BaseActivity
import com.android.util.JsonUtil
import com.android.util.alert
import com.android.util.initCommonLayout
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_common_layout.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by xuzhb on 2019/9/29
 * Desc:测试Retrofit
 */
class TestRetrofitActivity : BaseActivity() {
    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(
            this, "测试Retrofit",
            "获取天气信息(@Query GET)", "获取天气信息(@QueryMap GET)", "获取天气信息(@Field POST)",
            "获取天气信息(@FieldMap POST)", "获取网易新闻(@Body POST)", "访问百度网址(GET)",
            showEditText = true
        )
        val city = "北京"
        et.setText(city)
        et.setSelection(city.length)  //将光标移至文字末尾
    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            finish()
        }
        btn1.setOnClickListener {
            getWeatherByQuery(et.text.toString().trim())
        }
        btn2.setOnClickListener {
            getWeatherByQueryMap(et.text.toString().trim())
        }
        btn3.setOnClickListener {
            getWeatherByField(et.text.toString().trim())

        }
        btn4.setOnClickListener {
            getWeatherByFieldMap(et.text.toString().trim())
        }
        btn5.setOnClickListener {
            getWangYiNewsByBody("1", "10")
        }
        btn6.setOnClickListener {
            //            accessUrl(UrlConstant.BAIDU_URL)
            accessUrlRxJava(UrlConstant.BAIDU_URL)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_common_layout

    //访问网址，Retrofit
    private fun accessUrl(url: String) {
        RetrofitFactory.instance.createService(ApiService::class.java, url)
            .accessUrl()
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    showToast(ExceptionUtil.convertExceptopn(t))
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    showToast("访问成功！")
                    alert(this@TestRetrofitActivity, response.toString())
                }

            })
    }

    //访问网址，Retrofit + RxJava
    private fun accessUrlRxJava(url: String) {
        RetrofitFactory.instance.createService(ApiService::class.java, url)
            .accessUrlRxJava()
            .compose(SchedulerUtil.ioToMain())
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(response: ResponseBody) {
                    showToast("访问成功！")
                    alert(this@TestRetrofitActivity, response.string())
                }

                override fun onError(e: Throwable) {
                    showToast(ExceptionUtil.convertExceptopn(e))
                    e.printStackTrace()
                }

            })
    }

    //获取天气信息，@Query，GET请求
    private fun getWeatherByQuery(city: String) {
        RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.WEATHER_URL)
            .getWeatherByQuery(city)
            .compose(SchedulerUtil.ioToMain())
            .subscribe(WeatherObserver())
    }

    //获取天气信息，@QueryMap，GET请求
    private fun getWeatherByQueryMap(city: String) {
        val map = HashMap<String, Any>()
        map.put("city", city)
        RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.WEATHER_URL)
            .getWeatherByQueryMap(map)
            .compose(SchedulerUtil.ioToMain())
            .subscribe(WeatherObserver())
    }

    //获取天气信息，@Field，POST请求
    private fun getWeatherByField(city: String) {
        RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.WEATHER_URL)
            .getWeatherByField(city)
            .compose(SchedulerUtil.ioToMain())
            .subscribe(WeatherObserver())
    }

    //获取天气信息，@FieldMap，POST请求
    private fun getWeatherByFieldMap(city: String) {
        val map = HashMap<String, Any>()
        map.put("city", city)
        RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.WEATHER_URL)
            .getWeatherByFieldMap(map)
            .compose(SchedulerUtil.ioToMain())
            .subscribe(WeatherObserver())
    }

    private fun WeatherObserver(): Observer<BaseResponse<WeatherBean>> {
        return object : Observer<BaseResponse<WeatherBean>> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(response: BaseResponse<WeatherBean>) {
                if (response.isSuccess()) {
                    val result = JsonUtil.formatJson(Gson().toJson(response.data))
                    alert(this@TestRetrofitActivity, result)
                } else {
                    showToast(response.msg)
                }
            }

            override fun onError(e: Throwable) {
                showToast(ExceptionUtil.convertExceptopn(e))
                e.printStackTrace()
            }

        }
    }

    //获取网易新闻，@Body，POST请求
    private fun getWangYiNewsByBody(page: String, count: String) {
        val map = HashMap<String, Any>()
        map.put("page", page)
        map.put("count", count)
        val body = RequestBody.create(MediaType.parse("application/json"), Gson().toJson(map))
        RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.NEWS_URL)
            .getWangYiNewsByBody(body)
            .compose(SchedulerUtil.ioToMain())
            .subscribe(object : Observer<BaseListResponse<NewsListBean>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(response: BaseListResponse<NewsListBean>) {
                    if (response.isSuccess()) {
                        val result = JsonUtil.formatJson(Gson().toJson(response.result))
                        alert(this@TestRetrofitActivity, result)
                    } else {
                        showToast(response.msg)
                    }
                }

                override fun onError(e: Throwable) {
                    showToast(ExceptionUtil.convertExceptopn(e))
                    e.printStackTrace()
                }

            })
    }

}