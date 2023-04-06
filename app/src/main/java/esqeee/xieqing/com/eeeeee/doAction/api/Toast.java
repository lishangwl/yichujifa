package esqeee.xieqing.com.eeeeee.doAction.api;

import android.os.Build;

import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class Toast extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Toast();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {

        String toast = getString(param.getString("text"));
        ToastUtils.showShort(toast);
        log("执行:<"+getName()+">:"+toast);
        return true;
    }

    @Override
    public int getType() {
        return 17;
    }

    @Override
    public String getName() {
        return "弹出提示";
    }
}
