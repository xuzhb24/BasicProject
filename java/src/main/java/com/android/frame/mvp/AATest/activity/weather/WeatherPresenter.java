package com.android.frame.mvp.AATest.activity.weather;

import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvp.AATest.bean.WeatherBeanMvp;
import com.android.frame.mvp.BasePresenter;
import com.android.frame.mvp.extra.CustomObserver;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class WeatherPresenter extends BasePresenter<WeatherView> {

    private WeatherView mView;
    private WeatherModel mModel;

    public WeatherPresenter(WeatherView view) {
        this.mView = view;
        mModel = new WeatherModel();
    }

    public void getWeatherInfo(String city) {
        mModel.getWeather(city)
                .subscribe(new CustomObserver<BaseResponse<WeatherBeanMvp>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<WeatherBeanMvp> response) {
                        mView.showData(response.getData());
                    }
                });
    }

}
