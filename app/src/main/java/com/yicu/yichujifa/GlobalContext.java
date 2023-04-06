package com.yicu.yichujifa;

import android.app.Application;
import android.content.Context;

public class GlobalContext {
    private static Application application;
    public static void init(Application application2){
        application = application2;
    }

    public static Application getApplication() {
        return application;
    }

    public static Context getContext(){
        return application.getApplicationContext();
    }
}
