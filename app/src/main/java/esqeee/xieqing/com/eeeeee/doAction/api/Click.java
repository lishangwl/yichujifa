package esqeee.xieqing.com.eeeeee.doAction.api;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class Click extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Click();
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
        MathResult count = evalMath(param.getString("count"));
        if (count.isError()){
            RuntimeLog.e("<error[正在计算点击次数]>计算表达式有误："+x.getException().getMessage());
            return false;
        }

        x.setResult(getActionRun().scaleMatriacsX(x.getResult()));
        y.setResult(getActionRun().scaleMatriacsY(y.getResult()));
        for (int i = 0; (i < count.getResult() || count.getResult() == 0) && !getActionRun().isStop(); i++) {
            if (SettingsPreference.rootMode()){
                Shell.getShell().click(x.getResult(),y.getResult());
            }else{
                waitForAccessbility();
                AccessbilityUtils.clickXY(x.getResult(), y.getResult());
            }
            log("执行:<"+getName()+">:  坐标->("+x.getResult()+","+y.getResult()+")");
        }
        return true;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public String getName() {
        return "点击屏幕";
    }
}
