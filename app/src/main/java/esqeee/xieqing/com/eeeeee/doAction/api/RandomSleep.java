package esqeee.xieqing.com.eeeeee.doAction.api;

import com.xieqing.codeutils.util.MathUtils;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class RandomSleep extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new RandomSleep();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        MathResult min = evalMath(param.getString("min"));
        MathResult max = evalMath(param.getString("max"));

        if (min.isError()){
            RuntimeLog.e("<计算错误>:"+min.getException().getMessage());
            return false;
        }
        if (max.isError()){
            RuntimeLog.e("<计算错误>:"+max.getException().getMessage());
            return false;
        }
        if (max.getResult() > min.getResult()){
            try {
                int r = MathUtils.random((int)min.getResult(),(int)max.getResult());
                log("执行:<"+getName()+">:"+r+"ms");
                Thread.sleep(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try {
                log("执行:<"+getName()+">:"+(int)min.getResult()+"ms");
                Thread.sleep((int)min.getResult());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
