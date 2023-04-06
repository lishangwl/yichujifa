package com.xieqing.codeutils.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

public class SyStemUtil {
    private static Intent mIntent;

    public static void controlWifi() {
        WifiManager wifiManager = (WifiManager) Utils.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        } else {
            wifiManager.setWifiEnabled(true);
        }
    }

    public static void controlWifi(boolean value) {
        ((WifiManager) Utils.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(value);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void controlBlueTooth() {
        BluetoothAdapter adapter = ((BluetoothManager) Utils.getApp().getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        if (adapter == null) {
            return;
        }
        if (adapter.isEnabled()) {
            adapter.disable();
        } else {
            adapter.enable();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void controlBlueTooth( boolean value) {
        BluetoothAdapter adapter = ((BluetoothManager) Utils.getApp().getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        if (adapter == null) {
            return;
        }
        if (value) {
            adapter.enable();
        } else {
            adapter.disable();
        }
    }

    public static void controlFlow() {
        boolean z = true;
        boolean mobile = isMobile();
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) Utils.getApp().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            Method method = mConnectivityManager.getClass().getMethod("setMobileDataEnabled", new Class[]{Boolean.TYPE});
            Object[] objArr = new Object[1];
            if (mobile) {
                z = false;
            }
            objArr[0] = Boolean.valueOf(z);
            method.invoke(mConnectivityManager, objArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isMobile() {
        NetworkInfo networkInfo = ((ConnectivityManager) Utils.getApp().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.getType() != 0) {
            return false;
        }
        return true;
    }

    public static void wifiSeting() {
        mIntent = new Intent("android.settings.WIFI_SETTINGS");
        Utils.getApp().getApplicationContext().startActivity(mIntent);
    }

    public static void closeNotification() {
        try {
            Method collapse;
            Object statusBarManager = Utils.getApp().getApplicationContext().getSystemService("statusbar");
            if (VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse", new Class[0]);
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels", new Class[0]);
            }
            collapse.invoke(statusBarManager, new Object[0]);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public static void expandNotification() {
        Object service = Utils.getApp().getApplicationContext().getSystemService("statusbar");
        if (service != null) {
            try {
                Method expand;
                Class<?> clazz = Class.forName("android.app.StatusBarManager");
                if (VERSION.SDK_INT <= 16) {
                    expand = clazz.getMethod("expand", new Class[0]);
                } else {
                    expand = clazz.getMethod("expandSettingsPanel", new Class[0]);
                }
                expand.setAccessible(true);
                expand.invoke(service, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void memory() {
        mIntent = new Intent("android.settings.MEMORY_CARD_SETTINGS");
        Utils.getApp().getApplicationContext().startActivity(mIntent);
    }

    public static void appList() {
        mIntent = new Intent("android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS");
        Utils.getApp().getApplicationContext().startActivity(mIntent);
    }

    public static void setting() {
        mIntent = new Intent("android.settings.SETTINGS");
        Utils.getApp().getApplicationContext().startActivity(mIntent);
    }

    public static void fly() {
        mIntent = new Intent("android.settings.AIRPLANE_MODE_SETTINGS");
        Utils.getApp().getApplicationContext().startActivity(mIntent);
    }

    public static void developer() {
        mIntent = new Intent("android.settings.APPLICATION_DEVELOPMENT_SETTINGS");
        Utils.getApp().getApplicationContext().startActivity(mIntent);
    }

    public static void ringAndVibrate() {
        AudioManager audio = (AudioManager) Utils.getApp().getApplicationContext().getSystemService("audio");
        audio.setRingerMode(2);
        audio.setVibrateSetting(0, 1);
        audio.setVibrateSetting(1, 1);
    }

    public static void vibrate() {
        AudioManager audio = (AudioManager) Utils.getApp().getApplicationContext().getSystemService("audio");
        audio.setRingerMode(1);
        audio.setVibrateSetting(0, 1);
        audio.setVibrateSetting(1, 1);
    }

    public static void noRingAndVibrate() {
        AudioManager audio = (AudioManager) Utils.getApp().getApplicationContext().getSystemService("audio");
        audio.setRingerMode(0);
        audio.setVibrateSetting(0, 0);
        audio.setVibrateSetting(1, 0);
    }
}