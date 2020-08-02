package com.android.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.java.R;

/**
 * Created by xuzhb on 2019/11/24
 * Desc:通知管理
 * https://blog.csdn.net/qi85481455/article/details/82895507
 * https://www.jianshu.com/p/99bc32cd8ad6
 * PendingIntent参数详解：https://blog.csdn.net/lastTNT/article/details/40299095、https://blog.csdn.net/Ray534/article/details/52758389
 */
public class NotificationUtil {

    public static int id = 1;

    public static void showNotification(Context context, CharSequence title, CharSequence text) {
        showNotificationWithIntent(context, null, title, text);
    }

    public static void showNotificationWithIntent(Context context, Intent intent, CharSequence title, CharSequence text) {
        NotificationManager manager = (NotificationManager) context.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "1";
        CharSequence channelName = "自定义通知";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager.getNotificationChannel(channelId) == null) {
                notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT));
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)  //设置标题
                .setContentText(text)  //设置内容
                .setWhen(System.currentTimeMillis())  //设置时间
                .setSmallIcon(R.mipmap.ic_launcher)  //设置小图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_logo));  //设置大图标
        if (intent != null) {
            /*
             * getActivity(Context context, int requestCode, Intent intent, @Flags int flags)
             * flag参数详解：参考https://blog.csdn.net/lastTNT/article/details/40299095和https://blog.csdn.net/Ray534/article/details/52758389
             * FLAG_CANCEL_CURRENT：如果该PendingIntent已经存在，则在生成新的之前取消当前的，也就是说只有最后的PendingIntent有效，之前的都无效
             * FLAG_UPDATE_CURRENT：如果该PendingIntent已经存在，则用新传入的Intent更新当前的数据
             * FLAG_NO_CREATE：如果该PendingIntent不存在，直接返回null而不是创建一个PendingIntent
             * FLAG_ONE_SHOT：该PendingIntent只能用一次，在send()方法执行后，自动取消
             *
             * FLAG_CANCEL_CURRENT和FLAG_UPDATE_CURRENT的区别：共同点是都可以更新Intent的extras，不同点在于
             * FLAG_CANCEL_CURRENT会先把之前的extras清除，此外FLAG_UPDATE_CURRENT能够新建一个Intent，
             * 而FLAG_CANCEL_CURRENT则不能，只能使用第一次的Intent
             *
             * 当第二个参数requestCode为常数时，
             * 如果使用FLAG_CANCEL_CURRENT，创建多条通知时只有最后一条通知点击后可以跳转，
             * 传递的extra也是最后一条通知的extra，之前创建的通知点击后都无法跳转；
             * 如果使用FLAG_UPDATE_CURRENT，创建多条通知时每条通知点击后都可以跳转，但是传递的extra都是最后一条通知的extra
             * 当第二个参数requestCode每次创建的通知都不同，
             * FLAG_CANCEL_CURRENT和FLAG_UPDATE_CURRENT点击每条通知都可以跳转，而且extra都不同
             */
            PendingIntent pi = PendingIntent.getActivity(context, id,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi)  //设置PendingIntent
                    .setAutoCancel(true);  //设置点击后自动取消通知栏的通知图标
        }
        Notification notification = builder.build();
        manager.notify(id++, notification);
    }

    //判断通知权限是否打开
    public static boolean isNotificationEnabled(Context context) {
        try {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //跳转通知设置界面
    public static void gotoNotificationSetting(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {  //android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {  //android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else {  //其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
