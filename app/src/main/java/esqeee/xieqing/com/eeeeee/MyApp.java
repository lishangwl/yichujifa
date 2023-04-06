package esqeee.xieqing.com.eeeeee;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import androidx.multidex.MultiDex;
import android.view.WindowManager;
import com.kongzue.dialog.v2.DialogSettings;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.xq.wwwwwxxxxx.xqapppay.wx.WXPay;
import com.yicu.yichujifa.GlobalContext;
import com.yicu.yichujifa.pro.App;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;

import esqeee.xieqing.com.eeeeee.manager.NotificationManager;
import esqeee.xieqing.com.eeeeee.service.NotiyService;
import esqeee.xieqing.com.eeeeee.service.SeriveManager;
import esqeee.xieqing.com.eeeeee.utils.SharedPreferencesUtils;

public class MyApp extends Application{



    public static boolean isEdit = false;

//    private static SplashActivity splashActivity;
//
//    public static SplashActivity getSplashActivity() {
//        return splashActivity;
//    }
//
//    public static void setSplashActivity(SplashActivity splashActivity) {
//        MyApp.splashActivity = splashActivity;
//    }

    private static HomeActivity splashActivity;

    public static HomeActivity getSplashActivity() {
        return splashActivity;
    }

    public static void setSplashActivity(HomeActivity splashActivity) {
        MyApp.splashActivity = splashActivity;
    }


    public static int getFloatWindowType(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;//2038
        } else {
            return WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//2003
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println();
        //App.init(this);
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_13, this, new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);
            }
        });

        UMConfigure.init(this,"5bfa797ff1f556338d000473","umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        DialogSettings.style = DialogSettings.STYLE_IOS;

        NotiyService.ensureCollectorRunning(this);

        CrashReport.initCrashReport(getApplicationContext(), "e21d127199", false);
        FlowManager.init(new FlowConfig.Builder(this).build());

        SeriveManager.getSeriveManager(this).start();
        NotificationManager.getManager(this).requirAllNotification();

        GlobalContext.init(this);

        SharedPreferencesUtils.getInstance(this,"Config");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
