package com.android.frame.http.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.http.AATest.bean.NewsListBean
import com.android.frame.http.AATest.bean.WeatherBean
import com.android.frame.http.ExceptionUtil
import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
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
class TestRetrofitActivity : BaseActivity<ActivityCommonLayoutBinding>() {
    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(
            this, "测试Retrofit",
            "获取天气信息(@Query GET)", "获取天气信息(@QueryMap GET)", "获取网易新闻(@Field POST)",
            "获取网易新闻(@FieldMap POST)", "获取网易新闻(@Body POST)", "访问百度网址(GET)",
            showInputLayout = true
        )
        val city = "北京"
        il.inputText = city
        il.getEditText().setSelection(city.length)  //将光标移至文字末尾
    }

    override fun initListener() {
        btn1.setOnClickListener {
            getWeatherByQuery(il.inputText.trim())
        }
        btn2.setOnClickListener {
            getWeatherByQueryMap(il.inputText.trim())
        }
        btn3.setOnClickListener {
            getWangYiNewsByField("1", "1")
        }
        btn4.setOnClickListener {
            getWangYiNewsByFieldMap("1", "2")
        }
        btn5.setOnClickListener {
            getWangYiNewsByBody("1", "3")
        }
        btn6.setOnClickListener {
            //            accessUrl(UrlConstant.BAIDU_URL)
            accessUrlRxJava(UrlConstant.BAIDU_URL)
        }
    }

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

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

    private fun WeatherObserver(): Observer<WeatherBean> {
        return object : Observer<WeatherBean> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(bean: WeatherBean) {
                if (bean.isSuccess()) {
                    val result = JsonUtil.formatJson(Gson().toJson(bean))
                    alert(this@TestRetrofitActivity, result)
                } else {
                    showToast(bean.desc)
                }
            }

            override fun onError(e: Throwable) {
                showToast(ExceptionUtil.convertExceptopn(e))
                e.printStackTrace()
            }

        }
    }

    //获取网易新闻，@Field，POST请求
    private fun getWangYiNewsByField(page: String, count: String) {
        RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.NEWS_URL)
            .getWangYiNewsByField(page, count)
            .compose(SchedulerUtil.ioToMain())
            .subscribe(NewsObserver())
    }

    //获取网易新闻，@FieldMap，POST请求
    private fun getWangYiNewsByFieldMap(page: String, count: String) {
        val map = HashMap<String, Any>()
        map.put("page", page)
        map.put("count", count)
        RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.NEWS_URL)
            .getWangYiNewsByFieldMap(map)
            .compose(SchedulerUtil.ioToMain())
            .subscribe(NewsObserver())
    }

    //获取网易新闻，@Body，POST请求
    private fun getWangYiNewsByBody(page: String, count: String) {
        val map = HashMap<String, Any>()
        map.put("page", page)
        map.put("count", count)
        val sb = StringBuilder()
        map.forEach {
            sb.append("${it.key}=${it.value}&")
        }
        val data = sb.toString().substring(0, sb.toString().length - 1)
//        val data = "page=$page&count=$count"
        val body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), data)
        RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.NEWS_URL)
            .getWangYiNewsByBody(body)
            .compose(SchedulerUtil.ioToMain())
            .subscribe(NewsObserver())
    }

    private fun NewsObserver(): Observer<NewsListBean> {
        return object : Observer<NewsListBean> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(bean: NewsListBean) {
                if (bean.isSuccess()) {
                    val result = JsonUtil.formatJson(Gson().toJson(bean))
                    alert(this@TestRetrofitActivity, result)
                } else {
                    showToast(bean.message)
                }
            }

            override fun onError(e: Throwable) {
                showToast(ExceptionUtil.convertExceptopn(e))
                e.printStackTrace()
            }

        }
    }

}