package com.android.util.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;

import com.android.util.PermissionUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by xuzhb on 2020/5/31
 * Desc:位置工具类
 */
public class LocationUtil {

    //判断GPS是否可用
    public static boolean isGPSEnable(Context context) {
        return getLocationManager(context).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //判断定位是否可用
    public static boolean isLocationEnable(Context context) {
        LocationManager manager = getLocationManager(context);
        return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //打开GPS设置界面
    public static void openGPSSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //获取位置，需要权限<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>和<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    public static Location getLocation(Context context) {
        LocationManager manager = getLocationManager(context);
        List<String> providers = manager.getProviders(true);  //获取所有可用的位置提供器
        String provider;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {  //GPS
            provider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {  //网络
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            return null;
        }
        return manager.getLastKnownLocation(provider);
    }

    //根据经纬度获取地理位置
    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList.size() > 0) {
                return addressList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据经纬度获取所在国家
    public static String getCountryName(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "" : address.getCountryName();
    }

    //根据经纬度获取所在地
    public static String getLocality(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "" : address.getLocality();
    }

    //根据经纬度获取所在街道
    public static String getStreet(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "" : address.getAddressLine(0);
    }

    //获取LocationManager
    public static LocationManager getLocationManager(Context context) {
        return (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    private static OnLocationUpdateListener mListener;
    private static LocationManager mLocationManager;

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
    public static boolean register(Context context, long minTime, long minDistance, OnLocationUpdateListener listener) {
        if (listener == null || !isLocationEnable(context) || !PermissionUtil.isAllPermissionGranted(context,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
            return false;
        }
        mLocationManager = getLocationManager(context);
        mListener = listener;
        String provider = mLocationManager.getBestProvider(getCriteria(), true);
        Location location = mLocationManager.getLastKnownLocation(provider);
        if (location != null) {
            mListener.getLastKnownLocation(location);
        }
        //provider：定位模式，一般有gps和network两种
        mLocationManager.requestLocationUpdates(provider, minTime, minDistance, mListener);
        return true;
    }

    //设置定位参数
    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    //注销
    public static void unregister() {
        if (mLocationManager != null) {
            if (mListener != null) {
                mLocationManager.removeUpdates(mListener);
                mListener = null;
            }
            mLocationManager = null;
        }
    }

    interface OnLocationUpdateListener extends LocationListener {

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
        void getLastKnownLocation(Location location);

    }

}
