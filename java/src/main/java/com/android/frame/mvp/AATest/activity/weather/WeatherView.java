package com.android.frame.mvp.AATest.activity.weather;

import com.android.frame.mvp.AATest.bean.WeatherBeanMvp;
import com.android.frame.mvp.IBaseView;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public interface WeatherView extends IBaseView {

    void showData(WeatherBeanMvp bean);

}
