package com.android.util.threadPool.AATest

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.viewBinding.BaseActivity_VB
import com.android.util.DateUtil
import com.android.util.LogUtil
import com.android.util.initCommonLayout
import com.android.util.threadPool.ThreadPoolManager
import java.lang.ref.WeakReference
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

/**
 * Created by xuzhb on 2020/12/8
 * Desc:
 */
class TestThreadPoolUtilActivity : BaseActivity_VB<ActivityCommonLayoutBinding>() {

    companion object {
        private const val TAG = "ThreadPoolLog"
    }

    private var mWeakReference: WeakReference<TestThreadPoolUtilActivity>? = null

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(
            this, "线程池工具", false, true,
            "通过execute执行Runnable任务", "通过submit执行Runnable任务返回结果",
            "通过submit执行Callable任务抛出异常", "通过submit执行自定义Callable任务返回结果",
            "通过submit执行自定义Callable任务抛出异常", "等待5秒后执行任务", "等待2秒后开始每5秒执行任务",
            "等待提交的任务执行后关闭线程池", "立即关闭线程池"
        )
        mWeakReference = WeakReference(this)
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            ThreadPoolManager.instance.getFixedThreadPool().execute(Runnable {
                setResult("正在计算1+1，模拟耗时10秒")
                sleep(10)
                val result = 1 + 1
                appendResult("1 + 1 = $result")
            })
        }
        binding.btn2.setOnClickListener {
            ThreadPoolManager.instance.getFixedThreadPool().execute(Runnable {
                setResult("计算开始，模拟耗时5秒")
                val result = "计算完成"
                val future = ThreadPoolManager.instance.getFixedThreadPool().submit(Runnable {
                    sleep(5)
                }, result)
                try {
                    appendResult(future?.get() ?: "计算失败")
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            })
        }
        binding.btn3.setOnClickListener {
            ThreadPoolManager.instance.getFixedThreadPool().execute(Runnable {
                setResult("计算开始，将在第2秒抛出异常")
                val future = ThreadPoolManager.instance.getFixedThreadPool().submit(Callable<String> {
                    sleep(2)
                    throw InterruptedException("线程被终止")
                })
                try {
                    appendResult("计算结果：" + future?.get())
                } catch (e: ExecutionException) {
                    appendResult(e.message!!)
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    appendResult(e.message!!)
                    e.printStackTrace()
                }
            })
        }
        binding.btn4.setOnClickListener {
            ThreadPoolManager.instance.getFixedThreadPool().execute(Runnable {
                val future = ThreadPoolManager.instance.getFixedThreadPool().submit(CalculateTask(false))
                setResult("正在计算1+2，模拟耗时10秒")
                try {
                    if (future?.get() != null) {
                        appendResult("1 + 2 = " + future.get())
                    } else {
                        appendResult("获取计算结果失败")
                    }
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            })
        }
        binding.btn5.setOnClickListener {
            ThreadPoolManager.instance.getFixedThreadPool().execute(Runnable {
                val future = ThreadPoolManager.instance.getFixedThreadPool().submit(CalculateTask(true))
                setResult("正在计算1+2，将在第3秒抛出异常")
                try {
                    if (future?.get() != null) {
                        appendResult("1 + 2 = " + future.get())
                    } else {
                        appendResult("获取计算结果失败")
                    }
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            })
        }
        binding.btn6.setOnClickListener {
            setResult("准备执行")
            ThreadPoolManager.instance.getScheduledThreadPool().schedule(Runnable {
                appendResult("开始执行")
                sleep(2)
                appendResult("执行完成")
            }, 5, TimeUnit.SECONDS)
        }
        binding.btn7.setOnClickListener {
            setResult("准备执行")
            ThreadPoolManager.instance.getScheduledThreadPool().scheduleWithFixedDelay(Runnable {
                appendResult("开始执行")
            }, 2, 5, TimeUnit.SECONDS)
        }
        binding.btn8.setOnClickListener {
            appendResult("关闭线程池")
            ThreadPoolManager.instance.getFixedThreadPool().shutDown()
            ThreadPoolManager.instance.getScheduledThreadPool().shutDown()
        }
        binding.btn9.setOnClickListener {
            appendResult("立即关闭线程池")
            ThreadPoolManager.instance.getFixedThreadPool().shutdownNow()
            ThreadPoolManager.instance.getScheduledThreadPool().shutdownNow()
        }
    }

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

    private fun setResult(result: String) {
        LogUtil.i(TAG, "setResult:$result")
        val activity = mWeakReference?.get()
        if (activity != null) {
            val text = "${DateUtil.getCurrentDateTime(DateUtil.H_M_S_S)}  $result(${Thread.currentThread().id})\n"
            activity.runOnUiThread {
                (activity.findViewById<View>(R.id.tv) as TextView).text = text
            }
        } else {
            LogUtil.i(TAG, "activity is null")
        }
    }

    private fun appendResult(result: String) {
        LogUtil.i(TAG, "appendResult:$result")
        val activity = mWeakReference?.get()
        if (activity != null) {
            val text = "${DateUtil.getCurrentDateTime(DateUtil.H_M_S_S)}  $result(${Thread.currentThread().id})\n"
            activity.runOnUiThread {
                (activity.findViewById<View>(R.id.tv) as TextView).append(text)
            }
        } else {
            LogUtil.i(TAG, "activity is null")
        }
    }

    private fun sleep(second: Long) {
        try {
            Thread.sleep(second * 1000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        mWeakReference?.clear()
        ThreadPoolManager.instance.getFixedThreadPool().shutdownNow()
        ThreadPoolManager.instance.getScheduledThreadPool().shutdownNow()
        super.onDestroy()
    }

}