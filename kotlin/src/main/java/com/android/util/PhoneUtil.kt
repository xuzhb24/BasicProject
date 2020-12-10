package com.android.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import kotlin.collections.set

/**
 * Created by xuzhb on 2020/12/10
 * Desc:手机相关工具类
 */
object PhoneUtil {

    //判断设备是否是手机
    fun isPhone(context: Context): Boolean {
        val tm = getTelephonyManager(context)
        return tm != null && tm.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    /**
     * 判断手机号是否合法
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     * 联通：130、131、132、145、155、156、175、176、185、186
     * 电信：133、153、173、177、180、181、189
     * 全球星：1349
     * 虚拟运营商：170
     */
    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$".toRegex()
        return !TextUtils.isEmpty(phoneNumber) && phoneNumber.matches(regex)
    }

    //获取IMEI码，需要添加权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    fun getIMEI(context: Context): String? {
        val tm = getTelephonyManager(context)
        return tm?.deviceId
    }

    //获取IMSI码，需要添加权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    fun getIMSI(context: Context): String? {
        val tm = getTelephonyManager(context)
        return tm?.subscriberId
    }

    /**
     * 获取移动终端类型
     * TelephonyManager.PHONE_TYPE_NONE  //0：手机制式未知
     * TelephonyManager.PHONE_TYPE_GSM   //1：手机制式为GSM，移动和联通
     * TelephonyManager.PHONE_TYPE_CDMA  //2：手机制式为CDMA，电信
     * TelephonyManager.PHONE_TYPE_SIP   //3
     */
    fun getPhoneType(context: Context): Int {
        val tm = getTelephonyManager(context)
        return tm?.phoneType ?: -1
    }

    //判断sim卡是否准备好
    fun isSimReady(context: Context): Boolean {
        val tm = getTelephonyManager(context)
        return tm != null && tm.simState == TelephonyManager.SIM_STATE_READY
    }

    //获取Sim卡运营商名称：如中国移动、中国联通、中国电信
    fun getSimOperatorName(context: Context): String? {
        val tm = getTelephonyManager(context)
        return tm?.simOperatorName
    }

    //获取Sim卡运营商名称：如中国移动、中国联通、中国电信
    fun getSimOperatorCH(context: Context): String? {
        val tm = getTelephonyManager(context)
        val operator = tm?.simOperator ?: return null
        return when (operator) {
            "46000", "46002", "46007" -> "中国移动"
            "46001" -> "中国联通"
            "46003" -> "中国电信"
            else -> operator
        }
    }

    //获取手机信息，需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    fun getPhoneInfo(context: Context): Map<String, Any?>? {
        val tm = getTelephonyManager(context) ?: return null
        return HashMap<String, Any?>().apply {
            put("DeviceId", tm.getDeviceId())
            put("DeviceSoftwareVersion", tm.getDeviceSoftwareVersion())
            put("Line1Number", tm.getLine1Number())
            put("NetworkCountryIso", tm.getNetworkCountryIso())
            put("NetworkOperator", tm.getNetworkOperator())
            put("NetworkOperatorName", tm.getNetworkOperatorName())
            put("NetworkType", tm.getNetworkType())
            put("PhoneType", tm.getPhoneType())
            put("SimCountryIso", tm.getSimCountryIso())
            put("SimOperator", tm.getSimOperator())
            put("SimOperatorName", tm.getSimOperatorName())
            put("SimSerialNumber", tm.getSimSerialNumber())
            put("SimState", tm.getSimState())
            put("SubscriberId", tm.getSubscriberId())
            put("VoiceMailNumber", tm.getVoiceMailNumber())
        }
    }

    //跳转至拨号界面
    fun dial(context: Context, phoneNumber: String) {
        context.startActivity(IntentUtil.getDialIntent(phoneNumber))
    }

    //拨打电话，需要权限<uses-permission android:name="android.permission.CALL_PHONE" />
    fun call(context: Context, phoneNumber: String) {
        context.startActivity(IntentUtil.getCallIntent(phoneNumber))
    }

    //跳转至发送短信界面
    fun sendSms(context: Context, phoneNumber: String, content: String) {
        context.startActivity(IntentUtil.getSendSmsIntent(phoneNumber, content))
    }

    //发送短信，需要权限<uses-permission android:name="android.permission.SEND_SMS" />
    fun sendSmsSilent(context: Context, phoneNumber: String, content: String) {
        if (TextUtils.isEmpty(content)) {
            return
        }
        val sentIntent = PendingIntent.getBroadcast(context.applicationContext, 0, Intent(), 0)
        val smsManager = SmsManager.getDefault()
        if (content.length >= 70) {
            val ms: List<String> = smsManager.divideMessage(content) //SmsManager.divideMessage(content)发送内容大于70时自动拆分
            for (str in ms) {
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null)
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null)
        }
    }

    //获取前几条手机联系人信息
    fun getContactInfo(context: Context, count: Int): MutableList<Map<String, String?>>? {
        if (count <= 0) {
            return null
        }
        val list: MutableList<Map<String, String?>> = mutableListOf()
        val resolver = context.applicationContext.contentResolver
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val number = ContactsContract.CommonDataKinds.Phone.NUMBER
        val name = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        val cursor = resolver.query(uri, arrayOf(number, name), null, null, null)
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val map: MutableMap<String, String?> = HashMap()
                    map["phone"] = cursor.getString(cursor.getColumnIndex(number)) //电话号码
                    map["name"] = cursor.getString(cursor.getColumnIndex(name)) //姓名
                    LogUtil.e("ContactInfo", map.toString())
                    list.add(map)
                    if (list.size == count) {
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list
    }

    /**
     * 获取手机联系人信息，需要权限<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"></uses-permission>
     * 和<uses-permission android:name="android.permission.READ_CONTACTS"></uses-permission>
     */
    fun getAllContactInfo(context: Context): MutableList<Map<String, String?>>? {
        val list: MutableList<Map<String, String?>> = mutableListOf()
        val resolver = context.applicationContext.contentResolver
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val number = ContactsContract.CommonDataKinds.Phone.NUMBER
        val name = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        val cursor = resolver.query(uri, arrayOf(number, name), null, null, null)
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val map: MutableMap<String, String?> = HashMap()
                    map["phone"] = cursor.getString(cursor.getColumnIndex(number)) //电话号码
                    map["name"] = cursor.getString(cursor.getColumnIndex(name)) //姓名
                    LogUtil.e("ContactInfo", map.toString())
                    list.add(map)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list
    }

    //获取前几条手机短信内容
    fun getSMSInfo(context: Context, count: Int): MutableList<Map<String, String?>>? {
        val list: MutableList<Map<String, String?>> = mutableListOf()
        val resolver = context.applicationContext.contentResolver
        val uri = Uri.parse("content://sms")
        val cursor = resolver.query(uri, arrayOf("_id", "address", "person", "body", "date", "type", "read"), null, null, null)
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val map: MutableMap<String, String?> = HashMap()
                    map["_id"] = cursor.getString(cursor.getColumnIndex("_id"))
                    map["address"] = cursor.getString(cursor.getColumnIndex("address"))
                    map["person"] = cursor.getString(cursor.getColumnIndex("person"))
                    map["body"] = cursor.getString(cursor.getColumnIndex("body"))
                    map["date"] = cursor.getString(cursor.getColumnIndex("date"))
                    map["type"] = cursor.getString(cursor.getColumnIndex("type"))
                    map["read"] = cursor.getString(cursor.getColumnIndex("read"))
                    LogUtil.e("SMSInfo", map.toString())
                    list.add(map)
                    if (list.size == count) {
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list
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
    fun getAllSMSInfo(context: Context): MutableList<Map<String, String?>>? {
        val list: MutableList<Map<String, String?>> = mutableListOf()
        val resolver = context.applicationContext.contentResolver
        val uri = Uri.parse("content://sms")
        val cursor = resolver.query(uri, arrayOf("_id", "address", "person", "body", "date", "type", "read"), null, null, null)
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val map: MutableMap<String, String?> = HashMap()
                    map["_id"] = cursor.getString(cursor.getColumnIndex("_id"))
                    map["address"] = cursor.getString(cursor.getColumnIndex("address"))
                    map["person"] = cursor.getString(cursor.getColumnIndex("person"))
                    map["body"] = cursor.getString(cursor.getColumnIndex("body"))
                    map["date"] = cursor.getString(cursor.getColumnIndex("date"))
                    map["type"] = cursor.getString(cursor.getColumnIndex("type"))
                    map["read"] = cursor.getString(cursor.getColumnIndex("read"))
                    LogUtil.e("SMSInfo", map.toString())
                    list.add(map)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list
    }

    //获取TelephonyManager
    private fun getTelephonyManager(context: Context) = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

}