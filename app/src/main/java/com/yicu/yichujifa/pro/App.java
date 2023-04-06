package com.yicu.yichujifa.pro;

import android.content.Context;

public class App {
    static {
        System.loadLibrary("app");
    }
    public native static void init(Context context);
}
