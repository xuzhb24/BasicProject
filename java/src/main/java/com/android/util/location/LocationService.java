package com.android.util.location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.util.LogUtil;

/**
 * Created by xuzhb on 2020/5/31
 * Desc:
 */
public class LocationService extends Service {

    private static final String TAG = "LocationService";

    private boolean isSuccess;
    private String lastLatitude;
    private String lastLongitude;
    private String latitude;
    private String longitude;
    private String country;
    private String locality;
    private String street;

    private LocationUtil.OnLocationUpdateListener mOnLocationUpdateListener = new LocationUtil.OnLocationUpdateListener() {
        @Override
        public void getLastKnownLocation(Location location) {
            lastLatitude = String.valueOf(location.getLatitude());
            lastLongitude = String.valueOf(location.getLongitude());
            LogUtil.e(TAG, "getLastKnownLocation：" + lastLatitude + " " + lastLongitude + " " + latitude + " " + longitude);
            if (mListener != null) {
                mListener.getLocation(lastLatitude, lastLongitude, latitude, longitude, country, locality, street);
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            LogUtil.e(TAG, "onLocationChanged：" + lastLatitude + " " + lastLongitude + " " + latitude + " " + longitude);
            country = LocationUtil.getCountryName(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            locality = LocationUtil.getLocality(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            street = LocationUtil.getStreet(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            if (mListener != null) {
                mListener.getLocation(lastLatitude, lastLongitude, latitude, longitude, country, locality, street);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogUtil.e(TAG, provider + " status：" + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogUtil.e(TAG, provider + " is enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogUtil.e(TAG, provider + " is disabled");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        isSuccess = LocationUtil.register(getApplicationContext(), 0, 0, mOnLocationUpdateListener);
        LogUtil.e(TAG, "位置服务初始化" + (isSuccess ? "成功" : "失败"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocationBinder();
    }

    public class LocationBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public void onDestroy() {
        LocationUtil.unregister();
        mListener = null;
        super.onDestroy();
    }

    private OnLocationListener mListener;

    public void setOnLocationListener(OnLocationListener listener) {
        mListener = listener;
        mListener.initState(isSuccess);
    }

    //位置监听
    public interface OnLocationListener {

        //是否初始化成功
        void initState(boolean isSuccess);

        //获取经纬度
        void getLocation(String lastLatitude, String lastLongitude, String latitude, String longitude,
                         String country, String locality, String street);
    }

}
