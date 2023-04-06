package esqeee.xieqing.com.eeeeee.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xieqing.codeutils.util.Utils;

import java.util.List;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.SyStemUtil;
import esqeee.xieqing.com.eeeeee.manager.SystemActionManager;
import esqeee.xieqing.com.eeeeee.sql.model.System;


public class SystemReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //RuntimeLog.log("SystemReceiver",intent.toString());

        String action = intent.getAction();
        List<System> systems = SystemActionManager.getManager().queryList();
        for (System system:systems){
            if (system.actionIntent.equals(action)){
                ActionRunHelper.startAction(Utils.getApp(),system.path);
            }
        }
        systems.clear();
        systems = null;
    }
}
