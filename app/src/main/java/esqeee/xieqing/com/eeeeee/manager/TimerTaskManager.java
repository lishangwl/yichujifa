package esqeee.xieqing.com.eeeeee.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yicu.yichujifa.GlobalContext;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import esqeee.xieqing.com.eeeeee.bean.DeleteAction;
import esqeee.xieqing.com.eeeeee.sql.model.TimeTask;
import esqeee.xieqing.com.eeeeee.sql.model.TimeTask_Table;


public class TimerTaskManager {
    private static TimerTaskManager manager;
    private AlarmManager alarmManager = null;
    private BroadcastReceiver registerReceiver;
    private Context context;

    private TimerTaskManager(Context context){
        this.context = context;
        EventBus.getDefault().register(this);
        alarmManager = ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE));


    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onDelete(DeleteAction action){
        deleteTask(action.getAction().getAbsolutePath());
    }


    public static TimerTaskManager getManager() {
        if (manager == null) {
            manager = new TimerTaskManager(GlobalContext.getContext());
            manager.reset();
        }
        return manager;
    }

    private void reset() {
        List<TimeTask> timeTasks = queryList();
        for (TimeTask task : timeTasks){
            scheduleAlarm(task);
        }
    }


    public static TimerTaskManager getManager(Context context) {
        return new TimerTaskManager(context);
    }

    private PendingIntent createTaskCheckPendingIntent(Context context,String actionPath,int id,int hour,int min) {
        Intent intent = new Intent("xq.action.CheckTask_"+id)
                .setComponent(new ComponentName(context.getPackageName(),"esqeee.xieqing.com.eeeeee.receiver.TimerTaskReceiver"))
                .putExtra("actionId",actionPath)
                .putExtra("hour",hour)
                .putExtra("taskId",id)
                .putExtra("min",min);
        return PendingIntent.getBroadcast(context, id,intent , PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public void scheduleAlarm(TimeTask timeTask) {
        scheduleAlarm(timeTask.hour,timeTask.min,timeTask.path,timeTask.index);
    }
    public void scheduleAlarm(int hour,int min,String actionPath,int id) {
        PendingIntent intent = createTaskCheckPendingIntent(this.context,actionPath,id,hour,min);
        if (Build.VERSION.SDK_INT >= 21) {
            this.alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(getNextDayRealTime(hour,min),intent)
                    , intent);
        } else {
            this.alarmManager.setExact(AlarmManager.RTC_WAKEUP
                    , getNextDayRealTime(hour,min)
                    , intent);
        }
    }

    public long getNextDayRealTime(int hour,int min){
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(Calendar.HOUR_OF_DAY,hour);
        gregorianCalendar.set(Calendar.MINUTE,min);
        gregorianCalendar.set(Calendar.SECOND,0);
        gregorianCalendar.set(Calendar.MILLISECOND,0);
        long nextTime = gregorianCalendar.getTimeInMillis();
        if (nextTime < System.currentTimeMillis()) {
            //如果时间已经过  选第二天吧
            gregorianCalendar.add(Calendar.DATE, 1);
            nextTime = gregorianCalendar.getTimeInMillis();
        }
        return nextTime;
    }


    public void deleteTask(String actionPath){
        List<TimeTask> timerTasks = queryList(actionPath);
        for (TimeTask task : timerTasks){
            if (task!=null){
                task.delete();
                alarmManager.cancel(createTaskCheckPendingIntent(context,task.path,task.index,task.hour,task.min));
            }
        }
    }


    public void deleteTask(TimeTask task){
        if (task!=null){
            task.delete();
            alarmManager.cancel(createTaskCheckPendingIntent(context,task.path,task.index,task.hour,task.min));
        }
    }

    public List<TimeTask> queryList(){
        List<TimeTask> timerTasks = SQLite
                .select().from(TimeTask.class).queryList();
        return timerTasks;
    }
    public List<TimeTask> queryList(String path){
        List<TimeTask> timerTasks = SQLite
                .select().from(TimeTask.class).where(TimeTask_Table.path.eq(path)).queryList();
        return timerTasks;
    }

    public TimeTask queryTask(String actionFile,int hour,int min){
        return SQLite
                .select().from(TimeTask.class)
                .where(TimeTask_Table.path.eq(actionFile),TimeTask_Table.hour.eq(hour),TimeTask_Table.min.eq(min)).querySingle();
    }
}
