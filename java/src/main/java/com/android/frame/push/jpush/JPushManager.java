package com.android.frame.push.jpush;

import android.content.Context;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import com.android.frame.push.PushConstant;
import com.android.java.BuildConfig;
import com.android.util.LogUtil;
import com.android.util.encrypt.MessageDigestUtil;

/**
 * Created by xuzhb on 2019/11/19
 * Desc:极光管理器
 */
public class JPushManager {

    private static final String TAG = PushConstant.JPUSH_TAG;

    private JPushManager() {

    }

    private static class SingleTonHolder {
        private static final JPushManager holder = new JPushManager();
    }

    public static JPushManager getInstance() {
        return SingleTonHolder.holder;
    }

    //在Application的onCreate()初始化SDK
    public void init(Context context) {
        try {
            JPushInterface.setDebugMode(BuildConfig.DEBUG);
            JPushInterface.init(context);
            LogUtil.i(TAG, "极光初始化完成");
        } catch (Exception e) {
            LogUtil.e(TAG, "极光初始化异常，异常信息：" + e.getMessage());
            e.printStackTrace();
        }
    }

    //获取极光token，利用别名推送，需要在初始化后再调用，否则可能获取不到token
    public void registerToken(Context context) {
        try {
            LogUtil.i(TAG, "获取极光token开始");
            String token = JPushInterface.getRegistrationID(context);
            if (!TextUtils.isEmpty(token)) {
                LogUtil.w(TAG, "获取极光token成功，token为：" + token);
                String alias = MessageDigestUtil.md5(token);
                JPushInterface.setAlias(context, 0, alias);  //利用别名精准推送
                LogUtil.w(TAG, "推送别名：" + alias);
            } else {
                LogUtil.e(TAG, "获取极光token失败，token为空！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
