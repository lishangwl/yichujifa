package esqeee.xieqing.com.eeeeee.doAction.api;

import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.xieqing.codeutils.util.ActivityUtils;
import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.DeviceUtils;
import com.xieqing.codeutils.util.IntentUtils;
import com.yicu.yichujifa.GlobalContext;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.ui.OpsActivity;

public class System extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new System();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        String arg = param.getString("arg");
        JSONBean var;
        switch (arg){
            case "打开权限设置":
                GlobalContext.getContext().startActivity(new Intent(GlobalContext.getContext(), OpsActivity.class));
                break;
            case "取手机信息":
                var = checkVarExiets("var");
                setValue(var, DeviceUtils.getAllMessage());
                break;
            case "置屏幕亮度":
                int value = getMathResultOrThrow("value");
                Window localWindow = ActivityUtils.getTopActivity().getWindow();
                WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
                localLayoutParams.screenBrightness = ((float) value) / 255.0f;
                localWindow.setAttributes(localLayoutParams);
                break;
            case "置屏幕亮度模式":
                int mode = param.getInt("mode",0);
                android.provider.Settings.System.putInt(GlobalContext.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
                break;
            case "调用系统分享":
                String text = getStringFromParam("text");
                String image = getStringFromParam("image");
                if (!TextUtils.isEmpty(image)){
                    GlobalContext.getContext().startActivity(IntentUtils.getShareImageIntent(text,image,true));
                }else{
                    GlobalContext.getContext().startActivity(IntentUtils.getShareTextIntent(text,true));
                }
                break;
            case "调用系统打开文件":
                GlobalContext.getContext().startActivity(IntentUtils.getFileIntent(getStringFromParam("value")));
                break;

            case "卸载应用":
                AppUtils.uninstallApp(getStringFromParam("app"));
                break;
            case "取应用信息":
                var = checkVarExiets("var");
                setValue(var,AppUtils.getAppInfo(getStringFromParam("app")).toString());
                break;
            case "打开网络设置":
                GlobalContext.getContext().startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
                break;
        }
        return true;
    }





    @Override
    public int getType() {
        return 2;
    }

    @Override
    public String getName() {
        return "文件操作";
    }
}
