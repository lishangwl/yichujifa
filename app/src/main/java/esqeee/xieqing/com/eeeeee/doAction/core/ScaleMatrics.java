package esqeee.xieqing.com.eeeeee.doAction.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class ScaleMatrics {
    private static float deviceScreenHeight;
    private static float deviceScreenWidth;
    private static boolean initialized = false;
    private static int deviceScreenDensity;
    private static Display display;

    public static void initIfNeeded(Context activity) {
        if (initialized)
            return;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(metrics);
        deviceScreenHeight = metrics.heightPixels;
        deviceScreenWidth = metrics.widthPixels;
        deviceScreenDensity = metrics.densityDpi;
        display = windowManager.getDefaultDisplay();
        initialized = true;
    }

    public static float getDeviceScreenHeight() {
        return deviceScreenHeight;
    }

    public static float getDeviceScreenWidth() {
        return deviceScreenWidth;
    }

    public static float getDeviceScreenDensity() {
        return deviceScreenDensity;
    }

    public static float getOrientationAwareScreenWidth(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return getDeviceScreenHeight();
        } else {
            return getDeviceScreenWidth();
        }
    }

    public static float getOrientationAwareScreenHeight(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return getDeviceScreenWidth();
        } else {
            return getDeviceScreenHeight();
        }
    }

    private float mDesignWidth;
    private float mDesignHeight;

    public ScaleMatrics(int designWidth, int designHeight) {
        mDesignWidth = designWidth;
        mDesignHeight = designHeight;
    }


    public int scaleX(int x,int oriation) {
        float currentWidth = getOrientationAwareScreenWidth(oriation);
        return (int)(currentWidth / mDesignWidth * x);
    }

    public int scaleY(int y,int oriation) {
        float currentHeight = getOrientationAwareScreenHeight(oriation);
        return (int)(currentHeight / mDesignHeight * y);
    }


    public float scaleX(float x) {
        float currentWidth = getDeviceScreenWidth();
        return (currentWidth / mDesignWidth * x);
    }

    public float scaleY(float y) {
        float currentHeight = getDeviceScreenHeight();
        return (currentHeight / mDesignHeight * y);
    }
}
