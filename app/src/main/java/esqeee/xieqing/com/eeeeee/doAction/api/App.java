package esqeee.xieqing.com.eeeeee.doAction.api;

import android.app.ActivityManager;
import android.content.Context;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.SyStemUtil;
import esqeee.xieqing.com.eeeeee.library.root.Shell;

public class App extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new App();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {

        String pack = param.getString("packName");
        int action = param.getInt("action",0);
        if (action == 0){
            log("<启动应用>:"+pack);
            AppUtils.launchApp(pack);
            log("【如未能启动该应用，请到权限列表处-后台弹出页面，打开该权限即可！】");
        }else if (action == 1){
            Shell.getShell().exce("am force-stop "+pack);
        }else{
            ActivityManager activityManager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(pack);
        }

        return true;
    }

    @Override
    public int getType() {
        return 12;
    }

    @Override
    public String getName() {
        return "启动应用";
    }
}
