package esqeee.xieqing.com.eeeeee.doAction.api;

import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.SyStemUtil;

public class Wifi extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Wifi();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {
        if (param.getInt("actionType") == 18){
            SyStemUtil.controlWifi(Utils.getApp(), true);
        }else{
            SyStemUtil.controlWifi(Utils.getApp(), false);
        }
        log("执行:<"+getName()+">");
        return true;
    }

    @Override
    public int getType() {
        return 18;
    }

    @Override
    public String getName() {
        return "WIFI";
    }
}
