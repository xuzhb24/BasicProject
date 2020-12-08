package com.android.util.threadPool.AATest

import com.android.util.LogUtil
import com.android.util.threadPool.StepThread

/**
 * Created by xuzhb on 2020/12/8
 * Desc:
 */
class CalculateTask constructor(private val isThrowExceptionToStop: Boolean) : StepThread<Int>() {

    companion object {
        private const val TAG = "CalculateTask"
    }

    override fun init() {
        LogUtil.i(TAG, "init")
    }

    override fun execute(): Int {
        LogUtil.i(TAG, "execute begin")
        if (isThrowExceptionToStop) {
            Thread.sleep(3000)
            throw InterruptedException("抛出异常终止线程")
        } else {
            Thread.sleep(10000)
            val result = 1 + 2
            LogUtil.i(TAG, "execute end,return $result")
            return result
        }
    }

    override fun onError(e: Exception?) {
        LogUtil.e(TAG, "onError，$e")
    }

    override fun onDestory() {
        LogUtil.e(TAG, "onDestory")
    }
}