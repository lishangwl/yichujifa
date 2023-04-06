package esqeee.xieqing.com.eeeeee.doAction.api;

import com.xieqing.codeutils.util.PhoneUtils;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class FlashOpen extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new FlashOpen();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {
        PhoneUtils.openFlash();
        log("执行:<"+getName()+">");
        return true;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String getName() {
        return "打开手电筒";
    }
}
