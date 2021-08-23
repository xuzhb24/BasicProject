package com.android.frame.mvp.AATest.notListType;

import androidx.annotation.Nullable;

import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.AATest.entity.WeatherBean;
import com.android.frame.mvp.BasePresenter;
import com.android.frame.mvp.IBaseView;
import com.android.frame.mvp.extra.http.CustomObserver;

import java.util.Random;

/**
 * Created by xuzhb on 2021/1/4
 * Desc:
 */
public class TestMvpPresenter extends BasePresenter<TestMvpView> {

    private String[] mCities = new String[]{
            "深圳", "福州", "北京", "广州", "上海", "厦门",
            "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    };

    private TestMvpView mView;
    private TestMvpModel mModel;

    public TestMvpPresenter(TestMvpView view) {
        this.mView = view;
        this.mModel = new TestMvpModel();
    }

    @Override
    public void refreshData() {
        getWeatherInfo(mCities[new Random().nextInt(mCities.length)]);
    }

    public void getWeatherInfo(String city) {
        mModel.getWeatherInfo(city)
                .subscribe(new CustomObserver<BaseResponse<WeatherBean>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<WeatherBean> response) {
                        mView.showWeatherInfo(city, response.getData());
                    }

                    @Override
                    protected void onFailure(IBaseView view, String message, boolean isError, @Nullable Throwable t, @Nullable BaseResponse<WeatherBean> response) {
                        super.onFailure(view, message, isError, t, response);
                        mView.showWeatherInfo(city, null);
                    }
                });
    }

}
