package esqeee.xieqing.com.eeeeee.manager;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import esqeee.xieqing.com.eeeeee.bean.DeleteAction;
import esqeee.xieqing.com.eeeeee.sql.model.App;
import esqeee.xieqing.com.eeeeee.sql.model.App_Table;
import esqeee.xieqing.com.eeeeee.sql.model.System;
import esqeee.xieqing.com.eeeeee.sql.model.System_Table;

public class SystemActionManager {
    private static SystemActionManager manager;

    private SystemActionManager(){
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onDelete(DeleteAction action){
        deleteTask(action.getAction().getAbsolutePath());
    }

    public static SystemActionManager getManager() {
        if (manager == null){
            manager = new SystemActionManager();
        }
        return manager;
    }

    public int addOrUpdateTask(String actionIntent,String actionName,String actionPath){
        System task = queryTask(actionPath);
        if (task == null){
            task = new System();
            task.actionIntent = actionIntent;
            task.path = actionPath;
            task.actionName = actionName;
            task.insert();
        }else{
            task.actionIntent = actionIntent;
            task.path = actionPath;
            task.actionName = actionName;
            task.update();
        }
        return task.index;
    }

    public void deleteTask(String actionPath){
        System task = queryTask(actionPath);
        if (task!=null){
            task.delete();
        }
    }

    public List<System> queryList(){
        return SQLite
                .select().from(System.class).queryList();
    }

    public List<System> queryList(String path){
        return SQLite
                .select().from(System.class).where(System_Table.path.eq(path)).queryList();
    }

    public System queryTask(String actionFile){
        return SQLite
                .select().from(System.class)
                .where(System_Table.path.eq(actionFile)).querySingle();
    }
}
