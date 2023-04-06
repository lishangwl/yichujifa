package esqeee.xieqing.com.eeeeee.doAction.api;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import com.xieqing.codeutils.util.IntentUtils;
import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.action.ActionUtils;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;

public class Fast extends Base {
    public static Base intansce;

    public static Base getIntansce() {
        if (intansce == null) {
            intansce = new Fast();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        int actionType = param.getInt("actionType");
        String text = getString(param.getString("text"));
        switch (DoActionBean.getBeanFromType(actionType)) {
            case LNK_TELEPHONE:
                long phone = Long.parseLong(text);
                if (ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    log("没有拨打电话的权限");
                    return false;
                }
                PhoneUtils.call(text);
                break;
            case LNK_LINK:
                try {
                    Utils.getApp().startActivity(IntentUtils.getUrlIntent(text));
                }catch (ActivityNotFoundException ex){
                    log(ex);
                    return false;
                }
                break;
            case LNK_QQ:
                ActionUtils.toMobileQQChat(Utils.getApp(),text);
                break;
        }
        log("执行:<"+DoActionBean.getBeanFromType(actionType).getActionName()+">");
        return true;
    }

    @Override
    public int getType() {
        return 18;
    }

    @Override
    public String getName() {
        return "KEY";
    }
}
