package com.android.util.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.*
import android.provider.Settings
import com.android.util.permission.PermissionUtil
import java.io.IOException
import java.util.*

/**
 * Created by xuzhb on 2020/12/14
 * Desc:位置工具类
 */
object LocationUtil {

    //判断GPS是否可用
    fun isGPSEnable(context: Context) = getLocationManager(context).isProviderEnabled(LocationManager.GPS_PROVIDER)

    //判断定位是否可用
    fun isLocationEnable(context: Context): Boolean {
        val manager = getLocationManager(context)
        return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    //打开GPS设置界面
    fun openGPSSettings(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    //获取位置，需要权限<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>和<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    @SuppressLint("MissingPermission")
    fun getLocation(context: Context): Location? {
        val manager = getLocationManager(context)
        val providers = manager.getProviders(true) //获取所有可用的位置提供器
        val provider = when {
            providers.contains(LocationManager.GPS_PROVIDER) -> {  //GPS
                LocationManager.GPS_PROVIDER
            }
            providers.contains(LocationManager.NETWORK_PROVIDER) -> {  //网络
                LocationManager.NETWORK_PROVIDER
            }
            else -> {
                return null
            }
        }
        return manager.getLastKnownLocation(provider)
    }

    //根据经纬度获取地理位置
    fun getAddress(context: Context, latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(context.applicationContext, Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList.size > 0) {
                return addressList[0]
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    //根据经纬度获取所在国家
    fun getCountryName(context: Context, latitude: Double, longitude: Double): String? {
        val address = getAddress(context, latitude, longitude)
        return address?.countryName
    }

    //根据经纬度获取所在地
    fun getLocality(context: Context, latitude: Double, longitude: Double): String? {
        val address = getAddress(context, latitude, longitude)
        return address?.locality
    }

    //根据经纬度获取所在街道
    fun getStreet(context: Context, latitude: Double, longitude: Double): String? {
        val address = getAddress(context, latitude, longitude)
        return address?.getAddressLine(0)
    }

    //获取LocationManager
    private fun getLocationManager(context: Context): LocationManager {
        return context.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private var mListener: OnLocationUpdateListener? = null
    private var mLocationManager: LocationManager? = null

    /**
     * 注册位置监听事件，记得取消注册
     * 需权限<uses-permission android:name="android.permission.INTERNET"/>
     * 和<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
     * 和<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
     *
     * @param minTime     位置信息更新周期（单位：毫秒）
     * @param minDistance 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息（单位：米），如果不为0，以minDistance为准，否则通过minTime定时更新
     * @param listener    位置刷新的回调接口
     * @return true：注册成功  false：注册失败
     */
    @SuppressLint("MissingPermission")
    fun register(context: Context, minTime: Long, minDistance: Long, listener: OnLocationUpdateListener?): Boolean {
        if (listener == null || !isLocationEnable(context) ||
            !PermissionUtil.isPermissionGranted(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            return false
        }
        mLocationManager = getLocationManager(context)
        mListener = listener
        val provider = mLocationManager?.getBestProvider(getCriteria(), true)
        val location = mLocationManager?.getLastKnownLocation(provider)
        if (location != null) {
            mListener?.getLastKnownLocation(location)
        }
        //provider：定位模式，一般有gps和network两种
        mLocationManager?.requestLocationUpdates(provider, minTime, minDistance.toFloat(), mListener)
        return true
    }

    //设置定位参数
    private fun getCriteria(): Criteria = Criteria().apply {
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        accuracy = Criteria.ACCURACY_FINE
        //设置是否要求速度
        isSpeedRequired = false
        //设置是否允许运营商收费
        isCostAllowed = false
        //设置是否需要方位信息
        isBearingRequired = false
        //设置是否需要海拔信息
        isAltitudeRequired = false
        //设置对电源的需求
        powerRequirement = Criteria.POWER_LOW
    }

    //注销
    fun unregister() {
        if (mLocationManager != null) {
            if (mListener != null) {
                mLocationManager?.removeUpdates(mListener)
                mListener = null
            }
            mLocationManager = null
        }
    }

    interface OnLocationUpdateListener : LocationListener {

        /*
         * LocationListener内部事件监听：
         * 1、onLocationChanged：当坐标改变时触发，如果Provider传进相同的坐标，它就不会被触发
         * 2、onStatusChanged：provider的在可用(status为LocationProvider.AVAILABLE)、
         *    暂时不可用(status为LocationProvider.TEMPORARILY_UNAVAILABLE)、
         *    无服务(status为LocationProvider.OUT_OF_SERVICE)三个状态直接切换时触发
         * 3、onProviderEnabled：provider可用时触发，如GPS被打开
         * 4、onProviderDisabled：provider不可用时触发，如GPS被关闭
         */

        //获取最后一次保留的坐标
        fun getLastKnownLocation(location: Location?)

    }

}