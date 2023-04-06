package esqeee.xieqing.com.eeeeee.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.doAction.GboalNotification;
import esqeee.xieqing.com.eeeeee.manager.CheckTextManager;
import esqeee.xieqing.com.eeeeee.sql.model.NotificationTextCheck;

@SuppressLint("OverrideAbstract")
public class NotiyService extends NotificationListenerService{
    public static String LAST_MESSAGE = "";
    public static String LAST_MESSAGE_CRETOR = "";
    @Override
    public void onCreate() {
        super.onCreate();
        if (!GboalNotification.getIntansce().isInit()){
            GboalNotification.getIntansce().init();
        }
    }


    //确认NotificationMonitor是否开启
    public static void ensureCollectorRunning(Context context) {

        ComponentName collectorComponent = new ComponentName(context, NotiyService.class);
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean collectorRunning = false;
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices == null ) {
            return;
        }
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (service.service.equals(collectorComponent)) {
                if (service.pid == android.os.Process.myPid() ) {
                    collectorRunning = true;
                }
            }
        }
        Log.e("xieqing520", "collectorRunning" + "-----" + collectorRunning);
        if (collectorRunning) {
            return;
        }
        toggleNotificationListenerService(context);
    }
    //重新开启NotificationMonitor
    private static void toggleNotificationListenerService(Context context) {
        ComponentName thisComponent = new ComponentName(context,  NotiyService.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //sbn.getNotification().deleteIntent.send();
        EventBus.getDefault().post(sbn);
        try {
            //有些通知不能解析出TEXT内容，这里做个信息能判断
            if (sbn.getNotification().tickerText != null) {
                LAST_MESSAGE_CRETOR = sbn.getNotification().contentIntent.getCreatorPackage();
                LAST_MESSAGE = sbn.getNotification().tickerText.toString();
                String nMessage = sbn.getNotification().tickerText.toString();
                Log.e("xieqing520", "Get Message" + "-----" + nMessage);
                List<NotificationTextCheck> checkList = CheckTextManager.getManager().queryNotificationList();
                for (NotificationTextCheck check : checkList){
                    if (checkIsCanDo(check,nMessage)){
                        switch (check.notiydo){
                            case 0:
                                sbn.getNotification().deleteIntent.send();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    cancelNotification(sbn.getKey());
                                }
                                break;
                            case 1:
                                sbn.getNotification().contentIntent.send();
                                break;
                            case 2:
                                break;
                        }
                        ActionRunHelper.startAction(getApplicationContext(),check.path);
                    }
                }
                checkList.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkIsCanDo(NotificationTextCheck check,String message) {
        String[] keys = check.keys.split(",.split.,");
        for (String key:keys){
            if (message.contains(key)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }


}
