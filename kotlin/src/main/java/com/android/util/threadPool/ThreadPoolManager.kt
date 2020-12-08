package com.android.util.threadPool

/**
 * Created by xuzhb on 2020/12/8
 * Desc:线程池管理器，获取线程池单例
 */
class ThreadPoolManager private constructor() {

    companion object {
        val instance: ThreadPoolManager = SingleTonHolder.holder
    }

    private object SingleTonHolder {
        val holder = ThreadPoolManager()
    }

    private var mFixedThreadPool: ThreadPoolUtil? = null
    private var mCachedThreadPool: ThreadPoolUtil? = null
    private var mSingleThreadPool: ThreadPoolUtil? = null
    private var mScheduledThreadPool: ThreadPoolUtil? = null

    fun getFixedThreadPool(): ThreadPoolUtil {
        if (mFixedThreadPool == null || mFixedThreadPool!!.isShutDown()) {
            mFixedThreadPool = ThreadPoolUtil(ThreadPoolUtil.FixedThread, 10)
        }
        return mFixedThreadPool!!
    }

    fun getCachedThreadPool(): ThreadPoolUtil {
        if (mCachedThreadPool == null || mCachedThreadPool!!.isShutDown()) {
            mCachedThreadPool = ThreadPoolUtil(ThreadPoolUtil.CachedThread, 0)
        }
        return mCachedThreadPool!!
    }

    fun getSingleThreadPool(): ThreadPoolUtil {
        if (mSingleThreadPool == null || mSingleThreadPool!!.isShutDown()) {
            mSingleThreadPool = ThreadPoolUtil(ThreadPoolUtil.SingleThread, 1)
        }
        return mSingleThreadPool!!
    }

    fun getScheduledThreadPool(): ThreadPoolUtil {
        if (mScheduledThreadPool == null || mScheduledThreadPool!!.isShutDown()) {
            mScheduledThreadPool = ThreadPoolUtil(ThreadPoolUtil.ScheduledThread, 10)
        }
        return mScheduledThreadPool!!
    }

}
