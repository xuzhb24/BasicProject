package com.android.frame.http.function

import com.android.util.LogUtil
import io.reactivex.Observable
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit

/**
 * Created by xuzhb on 2022/1/12
 * Desc:请求重试
 */
class RetryWithDelay(
    private val maxRetries: Int = 3,           //最大出错重试次数
    private var retryDelayMillis: Long = 5000  //重试间隔时间
) : Function<Observable<out Throwable?>, Observable<*>> {

    companion object {
        private const val TAG = "RetryWithDelay"
    }

    private var retryCount = 0  //当前出错重试次数

    @Throws(Exception::class)
    override fun apply(observable: Observable<out Throwable?>): Observable<*> {
        return observable
            .flatMap { throwable: Throwable? ->
                if (++retryCount <= maxRetries) {  //设置请求次数
                    LogUtil.i(TAG, "重试次数：$retryCount，重试间隔：$retryDelayMillis")
                    return@flatMap Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS) //设置请求间隔
                }
                return@flatMap Observable.error<Any?>(throwable)
            }
    }

}