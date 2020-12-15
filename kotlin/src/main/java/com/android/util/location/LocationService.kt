package com.android.util.location

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import com.android.util.LogUtil

/**
 * Created by xuzhb on 2020/12/14
 * Desc:
 */
class LocationService : Service() {

    companion object {
        private const val TAG = "LocationService"
    }

    private var isSuccess = false
    private var lastLatitude = ""
    private var lastLongitude = ""
    private var latitude = ""
    private var longitude = ""
    private var country = ""
    private var locality = ""
    private var street = ""

    private val onLocationUpdateListener = object : LocationUtil.OnLocationUpdateListener {

        override fun getLastKnownLocation(location: Location?) {
            lastLatitude = location?.latitude.toString()
            lastLongitude = location?.longitude.toString()
            LogUtil.e(TAG, "getLastKnownLocation：$lastLatitude $lastLongitude $latitude $longitude")
            if (mListener != null) {
                mListener?.getLocation(lastLatitude, lastLongitude, latitude, longitude, country, locality, street)
            }
        }

        override fun onLocationChanged(location: Location?) {
            latitude = location?.latitude.toString()
            longitude = location?.longitude.toString()
            LogUtil.e(TAG, "onLocationChanged：$lastLatitude $lastLongitude $latitude $longitude")
            country = LocationUtil.getCountryName(applicationContext, latitude.toDouble(), longitude.toDouble()) ?: ""
            locality = LocationUtil.getLocality(applicationContext, latitude.toDouble(), longitude.toDouble()) ?: ""
            street = LocationUtil.getStreet(applicationContext, latitude.toDouble(), longitude.toDouble()) ?: ""
            if (mListener != null) {
                mListener!!.getLocation(lastLatitude, lastLongitude, latitude, longitude, country, locality, street)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            LogUtil.e(TAG, "$provider status：$status")
        }

        override fun onProviderEnabled(provider: String?) {
            LogUtil.e(TAG, "$provider is enabled")
        }

        override fun onProviderDisabled(provider: String?) {
            LogUtil.e(TAG, "$provider is disabled")
        }

    }

    override fun onCreate() {
        super.onCreate()
        isSuccess = LocationUtil.register(applicationContext, 0, 0, onLocationUpdateListener)
        LogUtil.e(TAG, "位置服务初始化" + if (isSuccess) "成功" else "失败")
    }

    override fun onBind(intent: Intent?): IBinder? = LocationBinder()

    inner class LocationBinder : Binder() {
        fun getService() = this@LocationService
    }

    override fun onDestroy() {
        LocationUtil.unregister()
        mListener = null
        super.onDestroy()
    }

    private var mListener: OnLocationListener? = null

    fun setOnLocationListener(listener: OnLocationListener) {
        mListener = listener
        mListener?.initState(isSuccess)
    }

    //位置监听
    interface OnLocationListener {
        //是否初始化成功
        fun initState(isSuccess: Boolean)

        //获取经纬度
        fun getLocation(
            lastLatitude: String, lastLongitude: String,
            latitude: String, longitude: String,
            country: String, locality: String, street: String
        )
    }

}