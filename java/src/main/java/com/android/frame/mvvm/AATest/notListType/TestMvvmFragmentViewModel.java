package com.android.frame.mvvm.AATest.notListType;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.AATest.entity.WeatherBean;
import com.android.frame.mvvm.AATest.server.ApiHelper;
import com.android.frame.mvvm.BaseViewModel;
import com.android.frame.mvvm.extra.LiveDataEntity.ErrorResponse;
import com.android.java.databinding.FragmentTestMvcBinding;
import com.android.util.JsonUtil;
import com.android.util.ToastUtil;
import com.google.gson.Gson;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class TestMvvmFragmentViewModel extends BaseViewModel<FragmentTestMvcBinding> {

    private MutableLiveData<BaseResponse<WeatherBean>> mSuccessData = new MutableLiveData<>();
    private MutableLiveData<ErrorResponse<BaseResponse<WeatherBean>>> mErrorData = new MutableLiveData<>();

    private MutableLiveData<BaseResponse<WeatherBean>> mSuccessData2 = new MutableLiveData<>();
    private MutableLiveData<ErrorResponse<BaseResponse<WeatherBean>>> mErrorData2 = new MutableLiveData<>();

    //主接口或者页面最后一个接口请求，由它来控制加载完成的逻辑
    public void showWeatherInfo(String city) {
        launch(ApiHelper.getWeatherByQuery(city), mSuccessData, mErrorData, false, true, "处理中", true, true);
    }

    //次要的接口，假定这个接口只保存数据
    public void saveWeatherInfo(String city) {
        //不显示加载状态，不显示加载弹窗，不处理加载完成的逻辑（needLoadFinish设为false，因为这个接口不参与UI界面的显示，让主接口处理加载状态
        launch(ApiHelper.getWeatherByQuery(city), mSuccessData2, mErrorData2, false, false, false);
    }

    @Override
    public void observe(Fragment fragment, LifecycleOwner owner) {
        String tip = "下拉刷新获取更多城市天气\n\n";
        mSuccessData.observe(owner, it -> {
            binding.tv.setText(tip + JsonUtil.formatJson(new Gson().toJson(it)));
        });
        mErrorData.observe(owner, it -> {
            ToastUtil.showToast(it.getMessage());
            binding.tv.setText(tip);
        });
        mSuccessData2.observe(owner, it -> {
            System.out.println("TestMvvmFragment 请求成功，可以保存数据了，" + tip + JsonUtil.formatJson(new Gson().toJson(it)));
        });
        mErrorData2.observe(owner, it -> {
            ToastUtil.showToast(it.getMessage());
            System.out.println("TestMvvmFragment 请求失败，" + it.getMessage());
        });
    }
}
