package esqeee.xieqing.com.eeeeee.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.TimerTask;

import esqeee.xieqing.com.eeeeee.SplashActivity;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.manager.TimerTaskManager;
import esqeee.xieqing.com.eeeeee.sql.model.TimeTask;

public class TimerTaskReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int hour = intent.getIntExtra("hour",0);
        int min = intent.getIntExtra("min",0);
        int taskId = intent.getIntExtra("taskId",0);
        String path = intent.getStringExtra("actionId");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RuntimeLog.log("TimerTask","taskId:"+taskId+",当前时间:"+dateFormat.format(new Date())+",定时："+hour+":"+min);

        TimeTask timerTask = TimerTaskManager.getManager(context).queryTask(path,hour,min);
        if (timerTask != null){
            if (path == null){
                RuntimeLog.log("TimerTask","执行错误：路径为空，taskId："+taskId);
                return;
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, taskId,intent , PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT >= 21) {
                ((AlarmManager) Objects.requireNonNull(context.getSystemService(Context.ALARM_SERVICE))).setAlarmClock(new AlarmManager.AlarmClockInfo( getNextDayRealTime(hour,min),pendingIntent)
                        , pendingIntent);
            } else {
                ((AlarmManager) Objects.requireNonNull(context.getSystemService(Context.ALARM_SERVICE))).setExact(AlarmManager.RTC_WAKEUP
                        , getNextDayRealTime(hour,min)
                        , pendingIntent);
            }
            ActionRunHelper.startAction(context,path);
        }
    }

    public long getNextDayRealTime(int hour,int min){
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(Calendar.HOUR_OF_DAY,hour);
        gregorianCalendar.set(Calendar.MINUTE,min);
        gregorianCalendar.set(Calendar.SECOND,0);
        gregorianCalendar.set(Calendar.MILLISECOND,0);
        gregorianCalendar.add(Calendar.DATE, 1);
        long nextTime = gregorianCalendar.getTimeInMillis();
        return nextTime;
    }
}
