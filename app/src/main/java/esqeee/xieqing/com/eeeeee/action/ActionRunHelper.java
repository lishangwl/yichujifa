package esqeee.xieqing.com.eeeeee.action;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.user.User;

public class ActionRunHelper{
    /*  All running Action map */
    private static Map<String,ActionRun> running = new HashMap<>();




    /*
     *   获取所有运行的Action
     * */
    public synchronized static Collection<ActionRun> getAllRunning(){
        return running.values();
    }



    /*
     *   Action是否正在running
     * */
    public synchronized static boolean isRunning(Action action){
        if (action == null){
            return false;
        }
        return running.containsKey(action.getPath());
    }

    /*
     *   开始执行指令
     */
    public static void startAction(Context context, String path){
        if (path == null){
            return;
        }
        startAction(context,new File(path));
    }

    /*
     *   开始执行指令
     */
    public static void startAction(Context context, File action){
        startAction(context,ActionHelper.from(action));
    }

    /*
     *   开始执行指令
     */
    public static void startAction(Context context, Action action){
        EventBus.getDefault().post(User.getUser());
        if (action == null){
            return;
        }
        if (!AccessbilityUtils.isAccessibilitySettingsOn(context)){
            AccessbilityUtils.toSetting();
            return;
        }
        if (!(PermissionUtils.isUsage()||!PermissionUtils.hasUsageOption())){
            try {
                Toast.makeText(context,"您需要开启“有权查看应用使用情况”这一权限，才能正常使用！",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (!isRunning(action)){
            ActionRun run = new ActionRun(action,Utils.getApp());
            running.put(action.getPath(),run);
            EventBus.getDefault().post(new ActionStarted(run));
        }
    }




    /*
     *   停止所有的指令
     * */
    public synchronized static void stopAllAction() {
        Set<String> keys = running.keySet();
        Object[] itesms =  keys.toArray();
        for (int i = 0;i<itesms.length;i++){
            stopAction(running.get(itesms[i]));
        }
    }




    /*
     *   停止指令
     */
    public synchronized static void stopAction(ActionRun actionRun) {
        if (actionRun == null){return;}
        String key = actionRun.getAction().getPath();
        if (running.containsKey(key)){
            EventBus.getDefault().post(new ActionFinished(actionRun));
            ActionRun run = running.get(key);
            running.remove(key);
            run.stopDo();
        }
    }

    /*
     *   停止指令
     */
    public synchronized static void stopAction(Action action) {
        if (action == null){return;}
        if (running.containsKey(action.getPath())){
            ActionRun run = running.get(action.getPath());
            EventBus.getDefault().post(new ActionFinished(run));
            running.remove(action.getPath());
            run.stopDo();
        }else{
            RuntimeLog.i("自动化并未运行："+action.getPath());
        }
    }

    /*
     *   是否有action正在运行
     * */

    public static boolean hasActionRunning(){
        return running.size()>0;
    }
}
