package esqeee.xieqing.com.eeeeee.doAction.api;

import android.os.Build;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.helper.PathHelper;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class Auto extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Auto();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {

        String actionId = PathHelper.covert(param.getString("actionId"));
        Action action =  ActionHelper.from(actionId);
        if (action != null) {

            int type = param.getInt("type",-1);
            boolean runInCurrentThread = param.getBoolean("runInCurrentThread",true);
            if (type == -1){
                type = runInCurrentThread?0:1;
            }

            if (type == 0){
                getActionRun().doAction(action);
                log("运行自动化<"+actionId+">");
            }else  if (type == 1){
                ActionRunHelper.startAction(Utils.getApp(),action);
            }else if (type == 2){
                ActionRunHelper.stopAction(action);
            }else {
                throw new RuntimeException("[auto]unknow run type "+type);
            }
        }else{
            RuntimeLog.e("自动化<"+actionId+">不存在！，如果是使用其他人分享的脚本，请尝试重新选取自动化！");
            return false;
        }
        return true;
    }

    @Override
    public int getType() {
        return 17;
    }

    @Override
    public String getName() {
        return "执行自动化";
    }
}
