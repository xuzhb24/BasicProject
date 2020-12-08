package com.android.util.threadPool

import java.util.concurrent.Callable

/**
 * Created by xuzhb on 2020/12/8
 * Desc:封装Callable接口
 */
abstract class StepThread<V> : Callable<V> {

    //初始化
    @Throws(Exception::class)
    abstract fun init()

    //执行任务并返回最终结果
    @Throws(Exception::class)
    abstract fun execute(): V

    //发生异常时回调
    abstract fun onError(e: Exception?)

    //可以做一些最终的资源释放
    abstract fun onDestory()

    @Throws(Exception::class)
    override fun call(): V? {
        try {
            init()
            return execute()
        } catch (e: Exception) {
            onError(e)
        } finally {
            onDestory()
        }
        return null
    }
}
