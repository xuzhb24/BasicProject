package com.android.frame.mvp.AATest.notListType

import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvp.BasePresenter
import com.android.frame.mvp.IBaseView
import com.android.frame.mvp.extra.http.CustomObserver
import kotlin.random.Random

/**
 * Created by xuzhb on 2021/1/5
 * Desc:
 */
class TestMvpPresenter : BasePresenter<TestMvpView>() {

    private val mCities = mutableListOf(
        "深圳", "福州", "北京", "广州", "上海", "厦门",
        "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    )

    private val mModel by lazy { TestMvpModel() }

    override fun refreshData() {
        getWeatherInfo(mCities[Random.nextInt(mCities.size)])
    }

    fun getWeatherInfo(city: String) {
        mView?.let {
            mModel.getWeatherInfo(city)
                .subscribe(object : CustomObserver<BaseResponse<WeatherBean>>(it) {
                    override fun onSuccess(response: BaseResponse<WeatherBean>) {
                        it.showWeatherInfo(response.data)
                    }

                    override fun onFailure(
                        view: IBaseView?,
                        message: String,
                        isError: Boolean,
                        t: Throwable?,
                        response: BaseResponse<WeatherBean>?
                    ) {
                        super.onFailure(view, message, isError, t, response)
                        it.showWeatherInfo(null)
                    }
                })
        }
    }

}