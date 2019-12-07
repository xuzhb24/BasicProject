package com.android.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.android.basicproject.R

/**
 * Created by xuzhb on 2019/11/26
 * Desc:通知管理
 * https://blog.csdn.net/qi85481455/article/details/82895507
 * https://www.jianshu.com/p/99bc32cd8ad6
 * PendingIntent参数详解：https://blog.csdn.net/lastTNT/article/details/40299095、https://blog.csdn.net/Ray534/article/details/52758389
 * https://www.jianshu.com/p/b83fc1697232
 */
object NotificationUtil {

    private var id = 1

    fun showNotification(
        context: Context,
        title: CharSequence,
        text: CharSequence,
        smallIconId: Int = R.mipmap.ic_launcher,
        largeIconBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_logo),
        showBigText: Boolean = false,
        intent: Intent? = null
    ) {
        val manager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "1"
        val channelName = "自定义通知"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //8.0
            if (manager.getNotificationChannel(channelId) == null) {
                manager.createNotificationChannel(
                    NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                )
            }
        }
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)  //设置标题
            .setWhen(System.currentTimeMillis())  //设置时间
            .setSmallIcon(smallIconId)  //设置小图标
            .setLargeIcon(largeIconBitmap)  //设置大图标
//            .setPriority(priority)  //设置通知重要程度，要在系统通知管理中允许横幅通知
        /*
         * setPriority参数详解：
         * PRIORITY_DEFAULT：表示默认重要程度，和不设置效果一样
         * PRIORITY_MIN：表示最低的重要程度。系统只会在用户下拉状态栏的时候才会显示
         * PRIORITY_LOW：表示较低的重要性，系统会将这类通知缩小，或者改变显示的顺序，将排在更重要的通知之后
         * PRIORITY_HIGH：表示较高的重要程度，系统可能会将这类通知方法，或改变显示顺序，比较靠前
         * PRIORITY_MAX：最重要的程度， 会弹出一个单独消息框，让用户做出相应
         */
        if (!showBigText) {
            builder.setContentText(text)  //设置内容，长文本可能会显示不全
        } else {
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(text))  //显示长文本
        }
        intent?.let {
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
            val pi = PendingIntent.getActivity(context, id, it, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(pi)  //设置PendingIntent
                .setAutoCancel(true)  //设置点击后自动取消通知栏的通知图标
        }
        val notification = builder.build()
        manager.notify(id++, notification)
    }

    fun showNotificationFullUse(
        context: Context,
        title: CharSequence,
        text: CharSequence
    ) {
        val manager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "1"
        val channelName = "自定义通知"
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //8.0
//            if (manager.getNotificationChannel(channelId) != null) {
//                manager.createNotificationChannel(
//                    NotificationChannel(
//                        channelId,
//                        channelName,
//                        NotificationManager.IMPORTANCE_DEFAULT
//                    )
//                )
//            }
//        }
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)  //设置标题
            .setContentText(text)  //设置内容，长文本可能会显示不全
            .setWhen(System.currentTimeMillis())  //设置时间
            .setSmallIcon(R.mipmap.ic_launcher)  //设置小图标
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_logo))  //设置大图标
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)  //设置通知重要程度
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))  //显示长文本
            .setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher
                    )
                )
            )  //设置带有图片消息
            .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification))  //设置通知提示音
            .setVibrate(longArrayOf(0, 1000, 1000, 1000))  //设置振动
//            .setLights(Color.GREEN, 1000, 1000)  //设置前置LED灯进行闪烁，第一个为颜色值，第二个为亮的时长,第三个为暗的时长
//            .setDefaults(NotificationCompat.DEFAULT_ALL)  //使用默认效果，会根据手机当前环境播放铃声，是否振动
        val notification = builder.build()
        manager.notify(id++, notification)
    }

    //判断通知权限是否打开
    fun isNotificationEnabled(context: Context): Boolean {
        try {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //跳转通知设置界面
    fun gotoNotificationSetting(context: Context) {
        val intent = Intent()
        with(intent) {
            if (Build.VERSION.SDK_INT >= 26) {  //android 8.0引导
                setAction("android.settings.APP_NOTIFICATION_SETTINGS")
                putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
            } else if (Build.VERSION.SDK_INT >= 21) {  //android 5.0-7.0
                setAction("android.settings.APP_NOTIFICATION_SETTINGS")
                putExtra("app_package", context.packageName)
                putExtra("app_uid", context.applicationInfo.uid)
            } else {  //其他
                setAction("android.settings.APPLICATION_DETAILS_SETTINGS")
                setData(Uri.fromParts("package", context.packageName, null))
            }
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

}