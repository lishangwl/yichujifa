package esqeee.xieqing.com.eeeeee.doAction.api;

import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.SyStemUtil;

public class Bule extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Bule();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {
        if (param.getInt("actionType") == 20){
            SyStemUtil.controlBlueTooth(Utils.getApp(), true);
        }else{
            SyStemUtil.controlBlueTooth(Utils.getApp(), false);
        }
        log("执行:<"+getName()+">");
        return true;
    }

    @Override
    public int getType() {
        return 20;
    }

    @Override
    public String getName() {
        return "蓝牙";
    }
}
