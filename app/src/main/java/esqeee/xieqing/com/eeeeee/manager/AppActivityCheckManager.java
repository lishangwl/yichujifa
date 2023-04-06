package esqeee.xieqing.com.eeeeee.manager;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import esqeee.xieqing.com.eeeeee.bean.DeleteAction;
import esqeee.xieqing.com.eeeeee.sql.model.App;
import esqeee.xieqing.com.eeeeee.sql.model.App_Table;

public class AppActivityCheckManager {
    private static AppActivityCheckManager manager;

    private AppActivityCheckManager(){
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onDelete(DeleteAction action){
        deleteTask(action.getAction().getAbsolutePath());
    }

    public static AppActivityCheckManager getManager() {
        if (manager == null){
            manager = new AppActivityCheckManager();
        }
        return manager;
    }

    public int addOrUpdateTask(String pack,String activity,String actionPath){
        App task = queryTask(actionPath);
        if (task == null){
            task = new App();
            task.packageName = pack;
            task.path = actionPath;
            task.activityName = activity;
            task.insert();
        }else{
            task.packageName = pack;
            task.path = actionPath;
            task.activityName = activity;
            task.update();
        }
        return task.index;
    }

    public void deleteTask(String actionPath){
        App task = queryTask(actionPath);
        if (task!=null){
            task.delete();
        }
    }

    public List<App> queryList(){
        return SQLite
                .select().from(App.class).queryList();
    }

    public List<App> queryList(String path){
        return SQLite
                .select().from(App.class).where(App_Table.path.eq(path)).queryList();
    }

    public App queryTask(String actionFile){
        return SQLite
                .select().from(App.class)
                .where(App_Table.path.eq(actionFile)).querySingle();
    }
}
