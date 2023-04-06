package esqeee.xieqing.com.eeeeee.doAction.api;

import esqeee.xieqing.com.eeeeee.action.ActionRun;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class If extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new If();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        JSONBean condition = getJsonBean().getJson("condition");
        boolean result = false;
        JSONArrayBean conditions = getJsonBean().getArray("conditions");
        ActionRun.Block parent = getBlock();
        JSONArrayBean trueDo = getJsonBean().getJson("trueDo").getArray("actions");
        JSONArrayBean falseDo = getJsonBean().getJson("falseDo").getArray("actions");

        if (condition != null){
            result = getActionRun().getRunDo().post(condition.getInt("actionType"),condition,getActionRun(),parent);
        }else{
            //新版

            int conditionType = getJsonBean().getInt("AllOrOne",0); // 0 全部  1 任意一个
            for (int i = 0 ; i<conditions.length() ; i++){
                condition = conditions.getJson(i);
                result = getActionRun().getRunDo().post(condition.getInt("actionType"),condition,getActionRun(),parent);
                if (!result){
                    if (conditionType == 0){
                        //类型是&& 但有一个不符合   其他的条件就不用管了
                        break;
                    }
                }else{
                    if (conditionType == 1){
                        //类型是|| 有一个是符合的  其他的条件就不用管了
                        break;
                    }
                }
            }
        }
        if (result){
            log("<条件判断>:执行条件成功动作");
            new ActionRun.Block("if",trueDo,getActionRun(),parent).exceute();
        }else{
            log("<条件判断>:执行条件失败动作");
            new ActionRun.Block("if",falseDo,getActionRun(),parent).exceute();
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
