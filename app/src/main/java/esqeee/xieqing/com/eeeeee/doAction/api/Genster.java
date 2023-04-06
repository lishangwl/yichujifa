package esqeee.xieqing.com.eeeeee.doAction.api;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

import static esqeee.xieqing.com.eeeeee.service.AccessbilityUtils.pressGestrue;

public class Genster extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Genster();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {
        log("执行:<"+getName()+">");
        if (!param.has("path") || !pressGestrue(param.getArray("path"),getActionRun().getScaleMatrics())){
            RuntimeLog.e("执行手势读取失败！可能使用了旧版滑动，请删除该动作重新录制！");
            return false;
        }
        return true;
    }

    @Override
    public int getType() {
        return 18;
    }

    @Override
    public String getName() {
        return "执行手势";
    }
}
