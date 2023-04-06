package esqeee.xieqing.com.eeeeee.doAction.api;

import com.xieqing.codeutils.util.MediaUtils;
import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.action.ActionUtils;
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

public class Setting extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Setting();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {
        int actionType = param.getInt("actionType");
        switch (DoActionBean.getBeanFromType(actionType)){
            case SYS_WIFISETTING:
                SyStemUtil.wifiSeting(Utils.getApp());
                break;
            case SYS_STROGE:
                SyStemUtil.memory(Utils.getApp());
                break;
            case SYS_APPMANGER:
                SyStemUtil.appList(Utils.getApp());
                break;
            case SYS_VIBRATE:
                SyStemUtil.noRingAndVibrate(Utils.getApp());
                break;
            case SYS_NOT_VIBRATE:
                SyStemUtil.vibrate(Utils.getApp());
                break;
            case SYS_SETTING:
                SyStemUtil.setting(Utils.getApp());
                break;
            case SYS_OPEN_NOTIC:
                SyStemUtil.expandNotification(Utils.getApp());
                break;
            case SYS_CLOSE_NOTIC:
                SyStemUtil.closeNotification(Utils.getApp());
                break;
            case STOP:
                return false;
            case STOP_ALL:
                ActionRunHelper.stopAllAction();
                return false;
            case LNK_WECHAT_SCAN:
                ActionUtils.toWeChatScanDirect(Utils.getApp());
                break;
            case LNK_ALIPAY_SCAN:
                ActionUtils.toAliPay(Utils.getApp(), 10000007);
                break;
            case LNK_ALIPAY_GETMONEY:
                ActionUtils.toAliPay(Utils.getApp(), 20000123);
                break;
            case LNK_ALIPAY_SENDMONEY:
                ActionUtils.toAliPay(Utils.getApp(), 20000056);
                break;
            case SYS_CLOSE_SCREEN:
                SyStemUtil.screenOff();
                break;
            case VBRITE:
                PhoneUtils.vibrateShort();
                break;
            case VBRITE_LONG:
                PhoneUtils.vibrate();
                break;
            case NOTICAFACTION:
                MediaUtils.playMusicFromAssets("wav/4085.wav");
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
