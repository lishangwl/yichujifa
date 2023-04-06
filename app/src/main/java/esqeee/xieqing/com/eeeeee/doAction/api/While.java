package esqeee.xieqing.com.eeeeee.doAction.api;

import esqeee.xieqing.com.eeeeee.action.ActionRun;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class While extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new While();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        boolean result = false;
        boolean useNot = param.getBoolean("useNot",false);


        JSONBean condition = getJsonBean().getJson("condition");
        JSONArrayBean conditions = getJsonBean().getArray("conditions");
        ActionRun.Block block = getBlock();
        ActionRun actionRun = getActionRun();
        JSONArrayBean trueDo = getJsonBean().getJson("trueDo").getArray("actions");

        do{
            if (condition != null){
                result = getActionRun().getRunDo().post(condition.getInt("actionType"),condition,actionRun,block);
            }else{
                //新版
                int conditionType = getJsonBean().getInt("AllOrOne",0); // 0 全部  1 任意一个
                www:for (int i = 0 ; i<conditions.length() ; i++){
                    JSONBean item = conditions.getJson(i);
                    //log("第"+i+"个条件");
                    result = getActionRun().getRunDo().post(item.getInt("actionType"),item,actionRun,block);
                    if (!result){
                        if (conditionType == 0){
                            //类型是&& 但有一个不符合  其他的条件就不用管了
                            break www;
                        }
                    }else{
                        if (conditionType == 1){
                            //类型是|| 有一个是符合的  其他的条件就不用管了
                            break www;
                        }
                    }
                }
            }
            if (useNot){
                result = !result;
            }
            if (result){
                ActionRun.Block b = new ActionRun.Block("while",trueDo,getActionRun(),block);
                b.exceute();
                if (b.isBreaked()){
                    result = false;
                    return true;
                }
            }
        }while (result && !getActionRun().isStop());
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
