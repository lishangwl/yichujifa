package esqeee.xieqing.com.eeeeee.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.SmsManager;

import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.library.tilefloat.MenuFloat;
import esqeee.xieqing.com.eeeeee.library.tilefloat.TileFloat;
import esqeee.xieqing.com.eeeeee.manager.TimerTaskManager;
import esqeee.xieqing.com.eeeeee.receiver.SystemReceiver;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;
import static android.content.Intent.ACTION_USER_PRESENT;

public class SeriveManager {
    private static SeriveManager seriveManager;
    private Context context;
    private SystemReceiver systemReceiver;
    public SeriveManager(Context context) {
        this.context = context;
        systemReceiver = new SystemReceiver();
    }
    
    public void start(){
        startFloat();
        startTimer();

        //注册广播
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            IntentFilter intentFilter = new IntentFilter();

            intentFilter.addAction(Intent.ACTION_SHUTDOWN);
            intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
            intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            intentFilter.addAction(ACTION_SCREEN_ON);
            intentFilter.addAction(ACTION_SCREEN_OFF);
            intentFilter.addAction(ACTION_USER_PRESENT);
            intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
            intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
            intentFilter.addAction("android.intent.action.PHONE_STATE");
            context.registerReceiver(systemReceiver,intentFilter);
        }
    }

    private void startTimer() {
        context.startService(new Intent(context,TimerService.class));
    }

    private void startFloat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context,FloattingService.class));
        } else {
            context.startService(new Intent(context,FloattingService.class));
        }
        //context.startService(new Intent(context,FloattingService.class));
    }

    public static SeriveManager getSeriveManager(Context context) {
        if (seriveManager == null){
            seriveManager = new SeriveManager(context);
        }
        return seriveManager;
    }

    public void reslese() {
        stopFloat();
        stopTimer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && AccessbilityUtils.getService()!= null) {
            AccessbilityUtils.getService().disableSelf();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.unregisterReceiver(systemReceiver);
        }
    }

    private void stopTimer() {
        context.stopService(new Intent(context,TimerService.class));
    }

    private void stopFloat() {
        MenuFloat.getMenuFloat(context).dismiss();
        TileFloat.getTileFloat(context).dismiss();
        PixelKeepLive.getIntansce(context).destory();
        context.stopService(new Intent(context,FloattingService.class));
    }
}
