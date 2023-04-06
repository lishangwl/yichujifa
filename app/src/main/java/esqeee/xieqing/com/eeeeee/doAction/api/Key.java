package esqeee.xieqing.com.eeeeee.doAction.api;

import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.SyStemUtil;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME;
import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_POWER_DIALOG;
import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_RECENTS;
import static esqeee.xieqing.com.eeeeee.service.AccessbilityUtils.performGlobalAction;

public class Key extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Key();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {
        int actionType = param.getInt("actionType");
        switch (DoActionBean.getBeanFromType(actionType)){
            case KEY_BACK:
                waitForAccessbility();
                performGlobalAction(GLOBAL_ACTION_BACK);
                break;
            case KEY_HOME:
                waitForAccessbility();
                performGlobalAction(GLOBAL_ACTION_HOME);
                break;
            case KEY_TASK:
                waitForAccessbility();
                performGlobalAction(GLOBAL_ACTION_RECENTS);
                break;
            case KEY_LONG_POWER:
                waitForAccessbility();
                performGlobalAction(GLOBAL_ACTION_POWER_DIALOG);
                break;
            case SYSTEM_WKKEUP:
                PhoneUtils.wakeUp();
                break;
        }
        log("执行:<"+DoActionBean.getBeanFromType(actionType).getActionName()+">");
        return true;
    }

    @Override
    public int getType() {
        return 18;
    }

    @Override
    public String getName() {
        return "KEY";
    }
}
