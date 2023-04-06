package esqeee.xieqing.com.eeeeee.doAction.api;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.image.ColorFinder;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class Color extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Color();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {

        int accetrue = param.getInt("accetrue",5);


        String color = param.getString("color");
        int colorValue = android.graphics.Color.parseColor(color);

        int type = param.getInt("actionType");


        org.opencv.core.Rect rect = getRect2(param);
        org.opencv.core.Point point = ColorFinder.getFinder().findColor(getActionRun().getCaptruer().capture().getMat(),
                colorValue,accetrue,rect);
        if (point == null){
            log("执行:<"+getName()+">: 未找到颜色值["+color+"]");
        }else{
            int x  = (int) (point.x);
            int y  = (int) (point.y);
            log("执行:<"+getName()+">: 找到颜色值，位置在（"+x+","+y+"）");
            if (param.getBoolean("assign",false)){
                setValue(queryVariable(param.getString("xV")),x);
                setValue(queryVariable(param.getString("yV")),y);
                return true;
            }

            if (type == 58 || type == 59){
                if (SettingsPreference.rootMode()){
                    Shell.getShell().click(x,y);
                    return true;
                }
                waitForAccessbility();
                AccessbilityUtils.clickXY(x,y);
            }else{
                if (SettingsPreference.rootMode()){
                    Shell.getShell().touchXY(x,y,2000);
                    return true;
                }
                waitForAccessbility();
                AccessbilityUtils.longClickXY(x,y);
            }
        }
        return true;
    }

    public org.opencv.core.Rect getRect2(JSONBean param) {
        if (param.has("rectVar")){
            return getRectByRect2(param);
        }
        int left = param.getInt("x",-1);
        int top = param.getInt("y",-1);
        int width = param.getInt("width",-1);
        int height = param.getInt("height",-1);
        if (left == -1 || top == -1 || width == -1 || height == -1){
            return null;
        }
        return new org.opencv.core.Rect(left,top,width,height);
    }

    @Override
    public int getType() {
        return 58;
    }

    @Override
    public String getName() {
        return "识别颜色";
    }
}
