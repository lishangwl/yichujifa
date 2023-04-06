package esqeee.xieqing.com.eeeeee.doAction.api;

import com.xieqing.codeutils.util.ScreenUtils;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME;
import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_POWER_DIALOG;
import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_RECENTS;
import static esqeee.xieqing.com.eeeeee.service.AccessbilityUtils.performGlobalAction;
import static esqeee.xieqing.com.eeeeee.service.AccessbilityUtils.pressSwipLocation;

public class Swip extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Swip();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {

        waitForAccessbility();
        int mWidth = ScreenUtils.getScreenWidth();
        int mHeight = ScreenUtils.getScreenHeight();


        int actionType = param.getInt("actionType");
        switch (DoActionBean.getBeanFromType(actionType)){
            case SWIP_LEFT:
                pressSwipLocation((mWidth / 4) * 3, mHeight / 2, mWidth / 4,  mHeight / 2);
                break;
            case SWIP_RIGHT:
                pressSwipLocation(mWidth / 4, mHeight / 2, (mWidth / 4) * 3,  mHeight / 2);
                break;
            case SWIP_TOP:
                pressSwipLocation(mWidth / 2, (mHeight / 4) * 3, mWidth / 2,  mHeight / 4);
                break;
            case SWIP_BOTTOM:
                pressSwipLocation(mWidth / 2, mHeight / 4, mWidth / 2,  (mHeight / 4) * 3);
                break;
        }
        log("执行:<"+DoActionBean.getBeanFromType(actionType).getActionName()+">");
        return true;
    }

    void pressSwipLocation(int x,int x1,int y,int y1){
        if (SettingsPreference.rootMode()){
            Shell.getShell().swipXY(x,x1,y,y1,500);
        }else{
            AccessbilityUtils.pressSwipLocation(x,x1,y,y1,500);
        }
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
