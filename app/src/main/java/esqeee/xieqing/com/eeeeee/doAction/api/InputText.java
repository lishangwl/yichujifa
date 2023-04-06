package esqeee.xieqing.com.eeeeee.doAction.api;

import android.view.accessibility.AccessibilityNodeInfo;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class InputText extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new InputText();
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


        String text = getString(param.getString("text"));

        waitForAccessbility();
        AccessibilityNodeInfo node = AccessbilityUtils.findEditNodeByXY((int)x.getResult(),(int)y.getResult());
        AccessbilityUtils.setNodeText(node,text);
        log("执行:<"+getName()+">:  坐标->("+(int)x.getResult()+","+(int)y.getResult()+")输入("+text+")");

        return true;
    }

    @Override
    public int getType() {
        return 15;
    }

    @Override
    public String getName() {
        return "输入内容";
    }
}
