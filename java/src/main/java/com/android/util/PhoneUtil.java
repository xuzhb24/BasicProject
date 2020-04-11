package com.android.util;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuzhb on 2020/4/9
 * Desc:手机相关工具类
 */
public class PhoneUtil {

    //判断设备是否是手机
    public static boolean isPhone(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 判断手机号是否合法
     * 移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、170、173、177、180、181、189、（1349卫通）
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        //[1]表示第1位为数字1，[34578]表示第二位可以为3、4、5、7、8中的一个，\\d{9}表示后面是可以是0～9的数字，有9位
        return !TextUtils.isEmpty(phoneNumber) && phoneNumber.matches("[1][34578]\\d{9}");
    }

    //获取IMEI码，需要添加权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    public static String getIMEI(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        return tm != null ? tm.getDeviceId() : null;
    }

    //获取IMSI码，需要添加权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    public static String getIMSI(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        return tm != null ? tm.getSubscriberId() : null;
    }

    /**
     * 获取移动终端类型
     * TelephonyManager.PHONE_TYPE_NONE  //0：手机制式未知
     * TelephonyManager.PHONE_TYPE_GSM   //1：手机制式为GSM，移动和联通
     * TelephonyManager.PHONE_TYPE_CDMA  //2：手机制式为CDMA，电信
     * TelephonyManager.PHONE_TYPE_SIP   //3
     */
    public static int getPhoneType(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        return tm != null ? tm.getPhoneType() : -1;
    }

    //判断sim卡是否准备好
    public static boolean isSimReady(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    //获取Sim卡运营商名称：如中国移动、中国联通、中国电信
    public static String getSimOperatorName(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        return tm != null ? tm.getSimOperatorName() : null;
    }

    //获取Sim卡运营商名称：如中国移动、中国联通、中国电信
    public static String getSimOperatorCH(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        String operator = tm != null ? tm.getSimOperator() : null;
        if (operator == null) return null;
        switch (operator) {
            case "46000":
            case "46002":
            case "46007":
                return "中国移动";
            case "46001":
                return "中国联通";
            case "46003":
                return "中国电信";
            default:
                return operator;
        }
    }

    //获取手机信息，需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    public static Map<String, Object> getPhoneInfo(Context context) {
        Map<String, Object> map = new HashMap<>();
        TelephonyManager tm = getTelephonyManager(context);
        map.put("DeviceId", tm.getDeviceId());
        map.put("DeviceSoftwareVersion", tm.getDeviceSoftwareVersion());
        map.put("Line1Number", tm.getLine1Number());
        map.put("NetworkCountryIso", tm.getNetworkCountryIso());
        map.put("NetworkOperator", tm.getNetworkOperator());
        map.put("NetworkOperatorName", tm.getNetworkOperatorName());
        map.put("NetworkType", tm.getNetworkType());
        map.put("PhoneType", tm.getPhoneType());
        map.put("SimCountryIso", tm.getSimCountryIso());
        map.put("SimOperator", tm.getSimOperator());
        map.put("SimOperatorName", tm.getSimOperatorName());
        map.put("SimSerialNumber", tm.getSimSerialNumber());
        map.put("SimState", tm.getSimState());
        map.put("SubscriberId", tm.getSubscriberId());
        map.put("VoiceMailNumber", tm.getVoiceMailNumber());
        return map;
    }

    //跳转至拨号界面
    public static void dial(Context context, String phoneNumber) {
        context.startActivity(IntentUtil.getDialIntent(phoneNumber));
    }

    //拨打电话，需要权限<uses-permission android:name="android.permission.CALL_PHONE" />
    public static void call(Context context, String phoneNumber) {
        context.startActivity(IntentUtil.getCallIntent(phoneNumber));
    }

    //跳转至发送短信界面
    public static void sendSms(Context context, String phoneNumber, String content) {
        context.startActivity(IntentUtil.getSendSmsIntent(phoneNumber, content));
    }

    //发送短信，需要权限<uses-permission android:name="android.permission.SEND_SMS" />
    public static void sendSmsSilent(Context context, String phoneNumber, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        PendingIntent sentIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, new Intent(), 0);
        SmsManager smsManager = SmsManager.getDefault();
        if (content.length() >= 70) {
            List<String> ms = smsManager.divideMessage(content);  //SmsManager.divideMessage(content)发送内容大于70时自动拆分
            for (String str : ms) {
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }

    //获取前几条手机联系人信息
    public static List<Map<String, String>> getContactInfo(Context context, int count) {
        if (count <= 0) {
            return null;
        }
        List<Map<String, String>> list = new ArrayList<>();
        ContentResolver resolver = context.getApplicationContext().getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String number = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String name = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        Cursor cursor = resolver.query(uri, new String[]{number, name}, null, null, null);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("phone", cursor.getString(cursor.getColumnIndex(number)));  //电话号码
                    map.put("name", cursor.getString(cursor.getColumnIndex(name)));  //姓名
                    LogUtil.e("ContactInfo", map.toString());
                    list.add(map);
                    if (list.size() == count) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取手机联系人信息，需要权限<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     * 和<uses-permission android:name="android.permission.READ_CONTACTS" />
     */
    public static List<Map<String, String>> getAllContactInfo(Context context) {
        List<Map<String, String>> list = new ArrayList<>();
        ContentResolver resolver = context.getApplicationContext().getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String number = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String name = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        Cursor cursor = resolver.query(uri, new String[]{number, name}, null, null, null);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("phone", cursor.getString(cursor.getColumnIndex(number)));  //电话号码
                    map.put("name", cursor.getString(cursor.getColumnIndex(name)));  //姓名
                    LogUtil.e("ContactInfo", map.toString());
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    //获取前几条手机短信内容
    public static List<Map<String, String>> getSMSInfo(Context context, int count) {
        List<Map<String, String>> list = new ArrayList<>();
        ContentResolver resolver = context.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://sms");
        Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "person", "body", "date", "type", "read"},
                null, null, null);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
                    map.put("address", cursor.getString(cursor.getColumnIndex("address")));
                    map.put("person", cursor.getString(cursor.getColumnIndex("person")));
                    map.put("body", cursor.getString(cursor.getColumnIndex("body")));
                    map.put("date", cursor.getString(cursor.getColumnIndex("date")));
                    map.put("type", cursor.getString(cursor.getColumnIndex("type")));
                    map.put("read", cursor.getString(cursor.getColumnIndex("read")));
                    LogUtil.e("SMSInfo", map.toString());
                    list.add(map);
                    if (list.size() == count) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取手机短信内容，需要权限<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     * 和<uses-permission android:name="android.permission.READ_SMS" />
     * URI主要有：
     * content://sms/       所有短信
     * content://sms/inbox  收件箱
     * content://sms/sent   已发送
     * content://sms/draft  草稿
     * content://sms/outbox  发件箱
     * content://sms/failed  发送失败
     * content://sms/queued  待发送列表
     * sms主要结构：
     * _id             一个自增字段，从1开始
     * thread_id       对话的序号，如100，同一发信人的id相同
     * address         发件人手机号，如+8613811810000
     * person          发件人，返回一个数字就是联系人列表里的序号，陌生人为null
     * date            发件日期，long型，如1256539465022
     * protocol        协议，0 SMS_RPOTO、1 MMS_PROTO
     * read            是否阅读，0 未读、1 已读
     * status          状态，-1 接收、0 complete、64 pending、128 failed
     * type            类型，1是接收到的，2是已发出
     * body            短消息内容
     * service_center  短信服务中心号码编号，如+8613800755500
     */
    public static List<Map<String, String>> getAllSMSInfo(Context context) {
        List<Map<String, String>> list = new ArrayList<>();
        ContentResolver resolver = context.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://sms");
        Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "person", "body", "date", "type", "read"},
                null, null, null);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
                    map.put("address", cursor.getString(cursor.getColumnIndex("address")));
                    map.put("person", cursor.getString(cursor.getColumnIndex("person")));
                    map.put("body", cursor.getString(cursor.getColumnIndex("body")));
                    map.put("date", cursor.getString(cursor.getColumnIndex("date")));
                    map.put("type", cursor.getString(cursor.getColumnIndex("type")));
                    map.put("read", cursor.getString(cursor.getColumnIndex("read")));
                    LogUtil.e("SMSInfo", map.toString());
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    //获取TelephonyManager
    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

}
