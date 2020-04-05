package com.android.util.threadPool.AATest;

import com.android.util.LogUtil;
import com.android.util.threadPool.StepThread;

/**
 * Created by xuzhb on 2020/4/5
 * Desc:
 */
public class CalculateTask extends StepThread<Integer> {

    private static final String TAG = "CalculateTask";

    private boolean isThrowExceptionToStop;  //是否通过抛异常来终止线程

    public CalculateTask(boolean throwException) {
        isThrowExceptionToStop = throwException;
    }

    @Override
    public void init() throws Exception {
        LogUtil.i(TAG, "init");
    }

    @Override
    public Integer execute() throws Exception {
        LogUtil.i(TAG, "execute begin");
        if (isThrowExceptionToStop) {
            Thread.sleep(3000);
            throw new InterruptedException("抛出异常终止线程");
        } else {
            Thread.sleep(10000);
            int result = 1 + 2;
            LogUtil.i(TAG, "execute end,return " + result);
            return result;
        }
    }

    @Override
    public void onError(Exception e) {
        LogUtil.e(TAG, "onError，" + e);
    }

    @Override
    public void onDestory() {
        LogUtil.e(TAG, "onDestory");
    }
}
