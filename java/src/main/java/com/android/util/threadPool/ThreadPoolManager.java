package com.android.util.threadPool;

/**
 * Created by xuzhb on 2020/4/4
 * Desc:线程池管理器，获取线程池单例
 */
public class ThreadPoolManager {

    private ThreadPoolUtil mFixedThreadPool;
    private ThreadPoolUtil mCachedThreadPool;
    private ThreadPoolUtil mSingleThreadPool;
    private ThreadPoolUtil mScheduledThreadPool;

    private ThreadPoolManager() {
    }

    public static ThreadPoolManager getInstance() {
        return SingleTonHolder.holder;
    }

    private static class SingleTonHolder {
        private static final ThreadPoolManager holder = new ThreadPoolManager();
    }

    public ThreadPoolUtil getFixedThreadPool() {
        if (mFixedThreadPool == null || mFixedThreadPool.isShutDown()) {
            mFixedThreadPool = new ThreadPoolUtil(ThreadPoolUtil.FixedThread, 10);
        }
        return mFixedThreadPool;
    }

    public ThreadPoolUtil getCachedThreadPool() {
        if (mCachedThreadPool == null || mCachedThreadPool.isShutDown()) {
            mCachedThreadPool = new ThreadPoolUtil(ThreadPoolUtil.CachedThread, 0);
        }
        return mCachedThreadPool;
    }

    public ThreadPoolUtil getSingleThreadPool() {
        if (mSingleThreadPool == null || mSingleThreadPool.isShutDown()) {
            mSingleThreadPool = new ThreadPoolUtil(ThreadPoolUtil.SingleThread, 1);
        }
        return mSingleThreadPool;
    }

    public ThreadPoolUtil getScheduledThreadPool() {
        if (mScheduledThreadPool == null || mScheduledThreadPool.isShutDown()) {
            mScheduledThreadPool = new ThreadPoolUtil(ThreadPoolUtil.ScheduledThread, 10);
        }
        return mScheduledThreadPool;
    }

}
