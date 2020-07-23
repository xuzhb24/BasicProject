package com.android.frame.mvc.viewBinding.AATest;

import android.os.Bundle;
import android.text.TextUtils;

import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.SchedulerUtil;
import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.viewBinding.BaseActivity_VB;
import com.android.frame.mvc.viewBinding.extra.CustomObserver;
import com.android.frame.mvp.AATest.ApiServiceMvp;
import com.android.frame.mvp.AATest.UrlConstantMvp;
import com.android.frame.mvp.AATest.bean.WeatherBeanMvp;
import com.android.frame.mvp.AATest.convert.WeatherFunction;
import com.android.java.databinding.ActivityWeatherBinding;
import com.android.util.regex.RegexUtil;

import java.util.List;

/**
 * Created by xuzhb on 2020/7/23
 * Desc:
 */
public class WeatherActivity extends BaseActivity_VB<ActivityWeatherBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        getWeatherInfo("深圳");
    }

    @Override
    public void initListener() {
        binding.queryBtn.setOnClickListener(v -> {
            binding.resultTv.setText("");
            String area = binding.areaIl.getInputText().trim();
            if (TextUtils.isEmpty(area)) {
                showToast("请先输入要查询的地区");
                return;
            }
            getWeatherInfo(area);
        });
    }

    @Override
    public ActivityWeatherBinding getViewBinding() {
        return ActivityWeatherBinding.inflate(getLayoutInflater());
    }

    private void getWeatherInfo(String city) {
        RetrofitFactory.getInstance().createService(ApiServiceMvp.class, UrlConstantMvp.WEATHER_URL)
                .getWeather(city)
                .map(new WeatherFunction())
                .compose(SchedulerUtil.ioToMain())
                .subscribe(new CustomObserver<BaseResponse<WeatherBeanMvp>>(this, true) {
                    @Override
                    public void onSuccess(BaseResponse<WeatherBeanMvp> response) {
                        WeatherBeanMvp bean = response.getData();
                        if (bean == null) {
                            return;
                        }
                        List<WeatherBeanMvp.ForecastBean> list = bean.getForecast();
                        if (list != null && list.size() > 0) {
                            StringBuilder sb = new StringBuilder();
                            WeatherBeanMvp.ForecastBean today = list.get(0);
                            sb.append(bean.getCity()).append("天气\n今天：").append(today.getDate()).append("  ")
                                    .append(getWenduRange(today.getHigh(), today.getLow())).append("  ").append(today.getType())
                                    .append("\n当前温度：").append(bean.getWendu()).append("℃\n感冒指数：").append(bean.getGanmao())
                                    .append("\n");
                            if (list.size() > 1) {
                                sb.append("未来").append(list.size() - 1).append("天天气：\n");
                                for (int i = 1; i < list.size(); i++) {
                                    WeatherBeanMvp.ForecastBean forecast = list.get(i);
                                    sb.append(forecast.getDate()).append("  ").append(getWenduRange(forecast.getHigh(), forecast.getLow()))
                                            .append("  ").append(forecast.getType()).append("\n");
                                }
                            }
                            binding.resultTv.setText(sb.toString());
                        } else {
                            binding.resultTv.setText("未获取到天气信息");
                        }
                    }
                });
    }

    private String getWenduRange(String high, String low) {
        return RegexUtil.extractDigit(low, " ") + "℃ 至 " +
                RegexUtil.extractDigit(high, " ") + "℃";
    }

}
