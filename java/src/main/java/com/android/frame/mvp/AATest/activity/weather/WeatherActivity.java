package com.android.frame.mvp.AATest.activity.weather;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

import com.android.frame.mvp.AATest.bean.WeatherBeanMvp;
import com.android.frame.mvp.BaseCompatActivity;
import com.android.java.R;
import com.android.util.regex.RegexUtil;
import com.android.widget.InputLayout;
import com.android.widget.TitleBar;

import java.util.List;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class WeatherActivity extends BaseCompatActivity<WeatherView, WeatherPresenter> implements WeatherView {


    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.area_il)
    InputLayout areaIl;
    @BindView(R.id.result_tv)
    TextView resultTv;

    @Override
    public void handleView(Bundle savedInstanceState) {
        mPresenter.getWeatherInfo("深圳");
    }

    @Override
    public void initListener() {
        titleBar.setOnLeftClickListener(v -> {
            finish();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    public WeatherPresenter getPresenter() {
        return new WeatherPresenter(this);
    }

    @Override
    public void showData(WeatherBeanMvp bean) {
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
            resultTv.setText(sb.toString());
        } else {
            resultTv.setText("未获取到天气信息");
        }
    }

    private String getWenduRange(String high, String low) {
        return RegexUtil.extractDigit(low, " ") + "℃ 至 " +
                RegexUtil.extractDigit(high, " ") + "℃";
    }

    @OnClick(R.id.query_btn)
    public void onViewClicked() {
        resultTv.setText("");
        String area = areaIl.getInputText().trim();
        if (TextUtils.isEmpty(area)) {
            showToast("请先输入要查询的地区");
            return;
        }
        mPresenter.getWeatherInfo(area);
    }
}
