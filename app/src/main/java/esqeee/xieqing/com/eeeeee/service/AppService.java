package esqeee.xieqing.com.eeeeee.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class AppService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Timer timer;
    private boolean enable = true;
    private ActivityManager activityManager;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (enable){

            }
            timer.schedule(timerTask,1000);
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        timer = new Timer();
        timer.schedule(timerTask,1000);
    }
}
