package com.android.util.threadPool;

import java.util.concurrent.Callable;

/**
 * Created by xuzhb on 2020/4/5
 * Desc:封装Callable接口
 */
public abstract class StepThread<V> implements Callable<V> {

    //初始化
    public abstract void init() throws Exception;

    //执行任务并返回最终结果
    public abstract V execute() throws Exception;

    //发生异常时回调
    public abstract void onError(Exception e);

    //可以做一些最终的资源释放
    public abstract void onDestory();

    @Override
    public V call() throws Exception {
        try {
            init();
            return execute();
        } catch (Exception e) {
            onError(e);
        } finally {
            onDestory();
        }
        return null;
    }

}
