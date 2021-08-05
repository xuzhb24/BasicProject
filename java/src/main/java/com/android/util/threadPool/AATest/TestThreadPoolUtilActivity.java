package com.android.util.threadPool.AATest;

import android.os.Bundle;
import android.widget.TextView;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;
import com.android.util.DateUtil;
import com.android.util.LogUtil;
import com.android.util.threadPool.ThreadPoolManager;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuzhb on 2020/4/5
 * Desc:
 */
public class TestThreadPoolUtilActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    private static final String TAG = "ThreadPoolLog";

    private WeakReference<TestThreadPoolUtilActivity> mWeakReference;

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "线程池工具", false, true,
                "通过execute执行Runnable任务", "通过submit执行Runnable任务返回结果",
                "通过submit执行Callable任务抛出异常", "通过submit执行自定义Callable任务返回结果",
                "通过submit执行自定义Callable任务抛出异常", "等待5秒后执行任务", "等待2秒后开始每5秒执行任务",
                "等待提交的任务执行后关闭线程池", "立即关闭线程池");
        mWeakReference = new WeakReference<>(this);
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            ThreadPoolManager.getInstance().getFixedThreadPool().execute(() -> {
                setResult("正在计算1+1，模拟耗时10秒");
                sleep(10);
                int result = 1 + 1;
                appendResult("1 + 1 = " + result);
            });
        });
        binding.btn2.setOnClickListener(v -> {
            ThreadPoolManager.getInstance().getFixedThreadPool().execute(() -> {
                setResult("计算开始，模拟耗时5秒");
                String result = "计算完成";
                Future<String> future = ThreadPoolManager.getInstance().getFixedThreadPool().submit(() -> {
                    sleep(5);
                }, result);
                try {
                    appendResult(future.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        binding.btn3.setOnClickListener(v -> {
            ThreadPoolManager.getInstance().getFixedThreadPool().execute(() -> {
                setResult("计算开始，将在第2秒抛出异常");
                Future<String> future = ThreadPoolManager.getInstance().getFixedThreadPool().submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        sleep(2);
                        throw new InterruptedException("线程被终止");
                    }
                });
                try {
                    appendResult("计算结果：" + future.get());
                } catch (ExecutionException e) {
                    appendResult(e.getMessage());
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    appendResult(e.getMessage());
                    e.printStackTrace();
                }
            });
        });
        binding.btn4.setOnClickListener(v -> {
            ThreadPoolManager.getInstance().getFixedThreadPool().execute(() -> {
                Future<Integer> future = ThreadPoolManager.getInstance().getFixedThreadPool().submit(new CalculateTask(false));
                setResult("正在计算1+2，模拟耗时10秒");
                try {
                    if (future.get() != null) {
                        appendResult("1 + 2 = " + future.get());
                    } else {
                        appendResult("获取计算结果失败");
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        binding.btn5.setOnClickListener(v -> {
            ThreadPoolManager.getInstance().getFixedThreadPool().execute(() -> {
                Future<Integer> future = ThreadPoolManager.getInstance().getFixedThreadPool().submit(new CalculateTask(true));
                setResult("正在计算1+2，将在第3秒抛出异常");
                try {
                    if (future.get() != null) {
                        appendResult("1 + 2 = " + future.get());
                    } else {
                        appendResult("获取计算结果失败");
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        binding.btn6.setOnClickListener(v -> {
            setResult("准备执行");
            ThreadPoolManager.getInstance().getScheduledThreadPool().schedule(() -> {
                appendResult("开始执行");
                sleep(2);
                appendResult("执行完成");
            }, 5, TimeUnit.SECONDS);
        });
        binding.btn7.setOnClickListener(v -> {
            setResult("准备执行");
            ThreadPoolManager.getInstance().getScheduledThreadPool().scheduleWithFixedDelay(() -> {
                appendResult("开始执行");
            }, 2, 5, TimeUnit.SECONDS);
        });
        binding.btn8.setOnClickListener(v -> {
            appendResult("关闭线程池");
            ThreadPoolManager.getInstance().getFixedThreadPool().shutDown();
            ThreadPoolManager.getInstance().getScheduledThreadPool().shutDown();
        });
        binding.btn9.setOnClickListener(v -> {
            appendResult("立即关闭线程池");
            ThreadPoolManager.getInstance().getFixedThreadPool().shutdownNow();
            ThreadPoolManager.getInstance().getScheduledThreadPool().shutdownNow();
        });
    }

    private void setResult(String result) {
        LogUtil.i(TAG, "setResult:" + result);
        TestThreadPoolUtilActivity activity = mWeakReference.get();
        if (activity != null) {
            String text = DateUtil.getCurrentDateTime(DateUtil.H_M_S_S) + "  " + result
                    + "(" + Thread.currentThread().getId() + ")\n";
            activity.runOnUiThread(() -> ((TextView) activity.findViewById(R.id.tv)).setText(text));
        } else {
            LogUtil.i(TAG, "activity is null");
        }
    }

    private void appendResult(String result) {
        LogUtil.i(TAG, "appendResult:" + result);
        TestThreadPoolUtilActivity activity = mWeakReference.get();
        if (activity != null) {
            String text = DateUtil.getCurrentDateTime(DateUtil.H_M_S_S) + "  " + result
                    + "(" + Thread.currentThread().getId() + ")\n";
            activity.runOnUiThread(() -> ((TextView) activity.findViewById(R.id.tv)).append(text));
        } else {
            LogUtil.i(TAG, "activity is null");
        }
    }

    private void sleep(long second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        mWeakReference.clear();
        ThreadPoolManager.getInstance().getFixedThreadPool().shutdownNow();
        ThreadPoolManager.getInstance().getScheduledThreadPool().shutdownNow();
        super.onDestroy();
    }
}
