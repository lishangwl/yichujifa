package esqeee.xieqing.com.eeeeee.doAction.api;

import com.xieqing.codeutils.util.PhoneUtils;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class FlashClose extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new FlashClose();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {
        PhoneUtils.closeFlash();
        log("执行:<"+getName()+">");
        return true;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public String getName() {
        return "关闭手电筒";
    }
}
