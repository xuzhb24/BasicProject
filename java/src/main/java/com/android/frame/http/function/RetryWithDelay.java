package com.android.frame.http.function;

import com.android.util.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by xuzhb on 2022/1/12
 * Desc:请求重试
 */
public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {

    private static final String TAG = "RetryWithDelay";

    private int maxRetries = 3;           //最大出错重试次数
    private int retryDelayMillis = 5000;  //重试间隔时间
    private int retryCount = 0;           //当前出错重试次数

    public RetryWithDelay() {
    }

    public RetryWithDelay(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable
                .flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
                    if (++retryCount <= maxRetries) {  //设置请求次数
                        LogUtil.i(TAG, "重试次数：" + retryCount + "，重试间隔：" + retryDelayMillis);
                        return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);  //设置请求间隔
                    }
                    return Observable.error(throwable);
                });
    }
}
