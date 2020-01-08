package com.android.frame.mvp.extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.android.util.NetworkUtil;

/**
 * Created by xuzhb on 2020/1/5
 * Desc:监听网络变化
 */
public class NetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                mListener.onNetChange(NetworkUtil.isConnected(context));
            }
        }
    }

    public interface OnNetChangeListener {
        void onNetChange(boolean isConnected);
    }

    private OnNetChangeListener mListener;

    public void setOnNetChangeListener(OnNetChangeListener listener) {
        this.mListener = listener;
    }

}
