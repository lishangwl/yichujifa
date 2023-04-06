package esqeee.xieqing.com.eeeeee.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.library.SyStemUtil;


public class DoActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("xieqing.doAction")){
            SyStemUtil.closeNotification(context);
            Action action = ActionHelper.from(intent.getStringExtra("actionID"));
            if (action!=null){
                ActionRunHelper.startAction(context,action);
            }
        }
    }
}
