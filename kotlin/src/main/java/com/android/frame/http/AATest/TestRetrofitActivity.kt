package com.android.frame.http.AATest

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.mvc.BaseActivity
import com.android.util.initCommonLayout
import kotlinx.android.synthetic.main.activity_common_layout.*

/**
 * Created by xuzhb on 2019/9/29
 * Desc:测试Retrofit
 */
class TestRetrofitActivity : BaseActivity() {
    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "测试Retrofit", "查询")
        //获取首页Banner数据
        btn1.setOnClickListener {
            RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.BASE_URL)
                .getRankList()
                .compose(SchedulerUtil.ioToMain())
                .subscribe({

                }, {
                    it.printStackTrace()
                })
        }
    }

    override fun initListener() {
    }

    override fun getLayoutId(): Int = R.layout.activity_common_layout
}