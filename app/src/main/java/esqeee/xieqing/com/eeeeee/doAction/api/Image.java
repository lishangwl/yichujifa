package esqeee.xieqing.com.eeeeee.doAction.api;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.xieqing.codeutils.util.Utils;

import java.io.File;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.helper.RectFloatHelper;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.MatchingUtil;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.ScreenPointBean;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class Image extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Image();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {


        boolean assign = param.getBoolean("assign",false);

        int accetrue = param.getInt("accetrue",80);
        File image = new File(getStringFromParam("fileName"));
        if (!image.exists()){
            RuntimeLog.e("执行:<"+getName()+">: 图片不存在或已被删除！");
            return false;
        }
        Bitmap bitmap = ImageUtils.getBitmap(image);
        int type = param.getInt("actionType");
        Rect rect = getRect(param);

        ScreenPointBean screenPointBean = MatchingUtil.match(getActionRun().getCaptruer().capture().getMat(),bitmap,rect,accetrue);

        if (screenPointBean == null){
            log("执行:<"+getName()+">: 未找到");
        }else{
            int x = screenPointBean.getImgX() + screenPointBean.getImgXlegth()/2 + (rect == null?0:rect.left);
            int y = screenPointBean.getImgY() + screenPointBean.getImgYlength()/2+ (rect == null?0:rect.top);
            log("执行:<"+getName()+">: 找到图，位置在（"+x+","+y+"）");
            if (SettingsPreference.isDoPicActionShowRect()){
                RectFloatHelper.getHelper(Utils.getApp()).showRectView(screenPointBean.getImgX()+ (rect == null?0:rect.left),screenPointBean.getImgY()+ (rect == null?0:rect.top),screenPointBean.getImgXlegth(),screenPointBean.getImgYlength());
                ThreadUtils.sleep(500);
                RectFloatHelper.getHelper(Utils.getApp()).removeRectView();
                ThreadUtils.sleep(200);
            }
            if (assign){
                setValue(queryVariable(param.getString("x")),x);
                setValue(queryVariable(param.getString("y")),y);
                setValue(queryVariable(param.getString("w")),screenPointBean.getImgXlegth());
                setValue(queryVariable(param.getString("h")),screenPointBean.getImgYlength());
                return true;
            }
            if (type == 30 || type == 50){
                if (SettingsPreference.rootMode()){
                    Shell.getShell().click(x,y);
                }else{
                    waitForAccessbility();
                    AccessbilityUtils.clickXY(x,y);
                }
            }else{
                if (SettingsPreference.rootMode()){
                    Shell.getShell().touchXY(x,y,1500);
                }else{
                    waitForAccessbility();
                    AccessbilityUtils.touchXY(x,y,1500);
                }
            }
        }
        return true;
    }

    @Override
    public int getType() {
        return 30;
    }

    @Override
    public String getName() {
        return "识别图片";
    }
}
