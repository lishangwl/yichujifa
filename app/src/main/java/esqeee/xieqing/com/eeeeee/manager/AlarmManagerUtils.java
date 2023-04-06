package esqeee.xieqing.com.eeeeee.manager;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import java.util.Calendar;

import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

/**
 * 定时提醒工具类
 *
 * @author Jack Zhang
 * create at 2019-05-28 15:51
 */
public class AlarmManagerUtils
{

  /**
   * 设置定时提醒，供AlarmService使用
   *
   * @author Jack Zhang
   * create at 2019-05-28 15:53
   */
  public static void setAlarmTime(Context context, long timeInMillis, Intent intent)
  {
    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//    PendingIntent sender = PendingIntent.getBroadcast(context, intent.getIntExtra("id", 0), intent, PendingIntent.FLAG_CANCEL_CURRENT);
    PendingIntent sender = PendingIntent.getService(context, 2019, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    timeInMillis = System.currentTimeMillis() + timeInMillis;
    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
      am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
      RuntimeLog.log("os >= 6.0");
    }
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    {
      AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(timeInMillis, sender);
      am.setAlarmClock(alarmClockInfo, sender);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
      am.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, sender);}*/

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(timeInMillis, sender);
        am.setAlarmClock(alarmClockInfo, sender);
    }else{
        am.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
    }
  }

  public static void cancelAlarm(Context context, String action, int id)
  {
    Intent intent = new Intent(action);
    PendingIntent pi = PendingIntent.getService(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    am.cancel(pi);
  }

  /**
   * triggerAtMillis 计算方法
   *
   * @param weekflag 传入的是周几
   * @param dateTime 传入的是时间戳（设置当天的年月日+从选择框拿来的时分秒）
   * @return 返回起始闹钟时间的时间戳
   */
  private static long calMethod(int weekflag, long dateTime)
  {
    long time = 0;
    //weekflag == 0表示是按天为周期性的时间间隔或者是一次行的，weekfalg非0时表示每周几的闹钟并以周为时间间隔
    if (weekflag != 0)
    {
      Calendar c = Calendar.getInstance();
      int week = c.get(Calendar.DAY_OF_WEEK);
      if (1 == week)
      {
        week = 7;
      } else if (2 == week)
      {
        week = 1;
      } else if (3 == week)
      {
        week = 2;
      } else if (4 == week)
      {
        week = 3;
      } else if (5 == week)
      {
        week = 4;
      } else if (6 == week)
      {
        week = 5;
      } else if (7 == week)
      {
        week = 6;
      }

      if (weekflag == week)
      {
        if (dateTime > System.currentTimeMillis())
        {
          time = dateTime;
        } else
        {
          time = dateTime + 7 * 24 * 3600 * 1000;
        }
      } else if (weekflag > week)
      {
        time = dateTime + (weekflag - week) * 24 * 3600 * 1000;
      } else
      {
        time = dateTime + (weekflag - week + 7) * 24 * 3600 * 1000;
      }
    } else
    {
      if (dateTime > System.currentTimeMillis())
      {
        time = dateTime;
      } else
      {
        time = dateTime + 24 * 3600 * 1000;
      }
    }
    return time;
  }


}