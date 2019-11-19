package com.android.frame.push.jpush;

import android.content.Context;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;
import com.android.frame.push.PushConstant;
import com.android.util.LogUtil;

/**
 * Created by xuzhb on 2019/11/19
 * Desc:
 */
public class JPushReceiver extends JPushMessageReceiver {

    private static final String TAG = PushConstant.JPUSH_TAG;

    //连接极光服务器
    @Override
    public void onConnected(Context context, boolean b) {
        super.onConnected(context, b);
        LogUtil.w(TAG, "[onConnected] isConnected:" + b + ",token:" + JPushInterface.getRegistrationID(context));
    }

    //注册极光时的回调
    @Override
    public void onRegister(Context context, String s) {
        super.onRegister(context, s);
        LogUtil.w(TAG, "[onRegister] " + s);
    }

    //注册以及解除注册别名时回调
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
        LogUtil.w(TAG, "[onAliasOperatorResult] " + jPushMessage);
    }

    /**
     * 接收到推送下来的通知
     * 可以利用附加字段（notificationMessage.notificationExtras）来区别Notication,
     * 指定不同的动作,附加字段是个json字符串
     * 通知（Notification），指在手机的通知栏（状态栏）上会显示的一条通知信息
     */
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        LogUtil.w(TAG, "[onNotifyMessageArrived] " + notificationMessage);
    }

    /**
     * 打开了通知
     * notificationMessage.notificationExtras(附加字段)的内容处理代码
     * 比如打开新的Activity， 打开一个网页等..
     */
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        LogUtil.w(TAG, "[onNotifyMessageOpened] " + notificationMessage);
    }

    /**
     * 接收到推送下来的自定义消息
     * 自定义消息不是通知，默认不会被SDK展示到通知栏上，极光推送仅负责透传给SDK。
     * 其内容和展示形式完全由开发者自己定义。
     * 自定义消息主要用于应用的内部业务逻辑和特殊展示需求
     */
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        LogUtil.w(TAG, "[onMessage] 自定义消息：" + customMessage.message);
    }
}
