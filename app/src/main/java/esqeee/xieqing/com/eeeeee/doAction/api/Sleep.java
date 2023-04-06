package esqeee.xieqing.com.eeeeee.doAction.api;

import com.xieqing.codeutils.util.MathUtils;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class Sleep extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Sleep();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        //log(getJsonBean());
        MathResult time = evalMath(getJsonBean().getString("witeTime"));

        if (time.isError()){
            RuntimeLog.e("<计算错误>:"+time.getException().getMessage());
            return false;
        }
        try {
            Thread.sleep(time.getResult());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public int getType() {
        return 51;
    }

    @Override
    public String getName() {
        return "随机延迟";
    }
}
