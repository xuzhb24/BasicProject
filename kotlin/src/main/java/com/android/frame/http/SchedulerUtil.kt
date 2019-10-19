package com.android.frame.http

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by xuzhb on 2019/8/8
 * Desc:简化subscribeOn和observeOn调用
 */
object SchedulerUtil {

    fun <T> ioToMain(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

}