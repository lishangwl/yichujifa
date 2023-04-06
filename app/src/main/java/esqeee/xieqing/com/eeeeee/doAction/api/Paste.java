package esqeee.xieqing.com.eeeeee.doAction.api;

import android.view.accessibility.AccessibilityNodeInfo;

import com.xieqing.codeutils.util.AppUtils;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class Paste extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Paste();
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


        waitForAccessbility();
        AppUtils.getClipText(new AppUtils.ThreadGetClipTextListener() {
            @Override
            public void getClipText(String clip) {
                AccessibilityNodeInfo node2 = AccessbilityUtils.findEditNodeByXY((int)x.getResult(), (int)y.getResult());
                AccessbilityUtils.setNodeText(node2,clip);
            }
        });

        log("执行:<"+getName()+">:  坐标->("+(int)x.getResult()+","+(int)y.getResult()+")");

        return true;
    }

    @Override
    public int getType() {
        return 46;
    }

    @Override
    public String getName() {
        return "粘贴输入";
    }
}
