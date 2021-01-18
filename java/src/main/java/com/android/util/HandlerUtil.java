package com.android.util;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by xuzhb on 2021/1/13
 * Desc:Handler工具类
 */
public final class HandlerUtil {

    private HandlerUtil() {
    }

    public static class HandlerHolder extends Handler {
        WeakReference<Callback> mListenerWeakReference;

        /**
         * 使用必读：推荐在Activity或者Activity内部持有类中实现该接口，不要使用匿名类，可能会被GC
         *
         * @param listener 收到消息回调接口
         */
        public HandlerHolder(Callback listener) {
            mListenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mListenerWeakReference != null && mListenerWeakReference.get() != null) {
                mListenerWeakReference.get().handleMessage(msg);
            }
        }
    }
}