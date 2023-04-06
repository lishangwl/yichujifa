package esqeee.xieqing.com.eeeeee.doAction.api;

import android.graphics.PathMeasure;
import android.os.SystemClock;
import android.util.Log;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.widget.CustomPath;

import static esqeee.xieqing.com.eeeeee.service.AccessbilityUtils.pressGestrue;

public class SL extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new SL();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {

        log("执行:<直线滑动>");
        MathResult fx = evalMath(param.getString("fX","500"));
        if (fx.isError()){
            RuntimeLog.e("<error[正在计算起点横坐标]>计算表达式有误："+fx.getException().getMessage());
            return false;
        }
        MathResult fy = evalMath(param.getString("fY","500"));
        if (fy.isError()){
            RuntimeLog.e("<error[正在计算起点纵坐标]>计算表达式有误："+fy.getException().getMessage());
            return false;
        }

        MathResult tx = evalMath(param.getString("tX","500"));
        if (tx.isError()){
            RuntimeLog.e("<error[正在计算起点横坐标]>计算表达式有误："+tx.getException().getMessage());
            return false;
        }
        MathResult ty = evalMath(param.getString("tY","500"));
        if (ty.isError()){
            RuntimeLog.e("<error[正在计算起点纵坐标]>计算表达式有误："+ty.getException().getMessage());
            return false;
        }
        MathResult time = evalMath(param.getString("time","500"));
        if (time.isError()){
            RuntimeLog.e("<error[正在计算持续时间]>计算表达式有误："+time.getException().getMessage());
            return false;
        }


        fx.setResult(getActionRun().scaleMatriacsX(fx.getResult()));
        fy.setResult(getActionRun().scaleMatriacsY(fy.getResult()));
        tx.setResult(getActionRun().scaleMatriacsX(tx.getResult()));
        ty.setResult(getActionRun().scaleMatriacsY(ty.getResult()));
        if (!SettingsPreference.rootMode()){
            waitForAccessbility();
            AccessbilityUtils.pressSwipLocation(fx.getResult(),
                    fy.getResult(),tx.getResult(),
                    ty.getResult(),time.getResult());
        }else{
            Shell.getShell().swipXY(fx.getResult(),
                    fy.getResult(),tx.getResult(),
                    ty.getResult(),time.getResult());
        }

        return true;
    }

    @Override
    public int getType() {
        return 77;
    }

    @Override
    public String getName() {
        return "直线滑动";
    }
}
