package esqeee.xieqing.com.eeeeee.doAction.api;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class LongClick extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new LongClick();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {
        MathResult x = evalMath(param.getString("x"));
        if (x.isError()){
            RuntimeLog.e("<error[正在计算横坐标]>计算表达式有误："+x.getException().getMessage());
            return false;
        }
        MathResult y = evalMath(param.getString("y"));
        if (y.isError()){
            RuntimeLog.e("<error[正在计算纵坐标]>计算表达式有误："+x.getException().getMessage());
            return false;
        }

        if (SettingsPreference.rootMode()){
            Shell.getShell().touchXY((int)x.getResult(), (int)y.getResult(),getRealTime(param.getInt("touchTime",1)));
        }else{
            waitForAccessbility();
            AccessbilityUtils.touchXY((int)x.getResult(), (int)y.getResult(),getRealTime(param.getInt("touchTime",1)));
        }

        log("执行:<"+getName()+">:  坐标->("+(int)x.getResult()+","+(int)y.getResult()+")");
        return true;
    }

    private int getRealTime(int type){
        if (type == 0){
            return 1000;
        }
        if (type == 2){
            return 4000;
        }
        if (type == 3){
            return getMathResultOrThrow("onTouchTime");
        }
        return 2000;
    }

    @Override
    public int getType() {
        return 16;
    }

    @Override
    public String getName() {
        return "长按屏幕";
    }
}
