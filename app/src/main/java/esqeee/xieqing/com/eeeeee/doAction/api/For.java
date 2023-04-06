package esqeee.xieqing.com.eeeeee.doAction.api;

import android.util.Log;

import esqeee.xieqing.com.eeeeee.action.ActionRun;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class For extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new For();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        ActionRun.Block parent = getBlock();
        ActionRun actionRun = getActionRun();


        int condition = getJsonBean().getInt("condition",-1);//旧版
        if (condition == -1){
            MathResult count = evalMath(param.getString("condition"));
            if (count.isError()){
                RuntimeLog.e("<error[正在计算循环次数]>计算表达式有误："+count.getException().getMessage());
                return false;
            }else{
                condition = (int) count.getResult();
            }
        }
        JSONArrayBean trueDo = getJsonBean().getJson("trueDo").getArray("actions");
        for (int i = 0 ; (i <condition || condition == 0)&& !getActionRun().isStop(); i++){
            ActionRun.Block block = new ActionRun.Block("for",trueDo,actionRun,parent);
            block.exceute();
            if (block.isBreaked()){
                return true;
            }
        }
        return true;
    }

    @Override
    public int getType() {
        return 53;
    }

    @Override
    public String getName() {
        return "计次循环";
    }
}
