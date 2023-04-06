package esqeee.xieqing.com.eeeeee.manager;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import esqeee.xieqing.com.eeeeee.bean.DeleteAction;
import esqeee.xieqing.com.eeeeee.sql.model.NotificationTextCheck;
import esqeee.xieqing.com.eeeeee.sql.model.NotificationTextCheck_Table;
import esqeee.xieqing.com.eeeeee.sql.model.TextCheck;
import esqeee.xieqing.com.eeeeee.sql.model.TextCheck_Table;
import esqeee.xieqing.com.eeeeee.sql.model.TimeTask_Table;

public class CheckTextManager {
    private static CheckTextManager manager;

    private CheckTextManager(){
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onDelete(DeleteAction action){
        TextCheck task = queryTask(action.getAction().getAbsolutePath());
        if (task != null){
            task.delete();
        }
    }

    public static CheckTextManager getManager() {
        if (manager == null){
            manager = new CheckTextManager();
        }
        return manager;
    }

    public int addOrUpdateTask(String keys,String actionPath){
        TextCheck task = queryTask(actionPath);
        if (task == null){
            task = new TextCheck();
            task.keys = keys;
            task.path = actionPath;
            task.insert();
        }else{
            task.keys = keys;
            task.path = actionPath;
            task.update();
        }
        return task.index;
    }
    public int addOrUpdateTask(int notiydo,String keys,String actionPath){
        NotificationTextCheck task = queryNotifactionTask(actionPath);
        if (task == null){
            task = new NotificationTextCheck();
            task.keys = keys;
            task.notiydo = notiydo;
            task.path = actionPath;
            task.insert();
        }else{
            task.keys = keys;
            task.notiydo = notiydo;
            task.path = actionPath;
            task.update();
        }
        return task.index;
    }
    public void deleteTask(String actionPath){
        TextCheck task = queryTask(actionPath);
        if (task!=null){
            task.delete();
        }

        NotificationTextCheck notificationTextCheck = queryNotifactionTask(actionPath);
        if (notificationTextCheck!=null){
            notificationTextCheck.delete();
        }
    }



    public List<TextCheck> queryList(){
        return SQLite
                .select().from(TextCheck.class).queryList();
    }
    public List<TextCheck> queryList(String path){
        return SQLite
                .select().from(TextCheck.class).where(TextCheck_Table.path.eq(path)).queryList();
    }
    public List<NotificationTextCheck> queryNotificationList(){
        return SQLite
                .select().from(NotificationTextCheck.class).queryList();
    }

    public List<NotificationTextCheck> queryNotificationList(String path){

        return SQLite
                .select().from(NotificationTextCheck.class).where(NotificationTextCheck_Table.path.eq(path)).queryList();
    }

    public TextCheck queryTask(String actionFile){
        return SQLite
                .select().from(TextCheck.class)
                .where(TextCheck_Table.path.eq(actionFile)).querySingle();
    }

    public NotificationTextCheck queryNotifactionTask(String actionFile){
        return SQLite
                .select().from(NotificationTextCheck.class)
                .where(NotificationTextCheck_Table.path.eq(actionFile)).querySingle();
    }
}
