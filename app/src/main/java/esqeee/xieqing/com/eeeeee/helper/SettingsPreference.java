package esqeee.xieqing.com.eeeeee.helper;

import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.SizeUtils;
import com.xieqing.codeutils.util.Utils;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class SettingsPreference {

    private static SPUtils getIntansce(){
        return SPUtils.getInstance(Utils.getApp().getPackageName()+"_preferences");
    }
    public static boolean useRootOpenAccess(){
        return getIntansce().getBoolean("useRootOpenAccess",false);
    }
    public static boolean useRootCaptrue(){
        return getIntansce().getBoolean("useRootCaptrue",false);
    }
    public static boolean appLuanchGetRoot(){
        return getIntansce().getBoolean("appLuanchGetRoot",false);
    }
    public static boolean rootMode(){
        return getIntansce().getBoolean("rootMode",false);
    }
    public static boolean isSensorOpen(){
        return getIntansce().getBoolean("isSensorOpen",false);
    }

    public static void setSensorOpen(boolean open){
        getIntansce().put("isSensorOpen",open);
    }

    public static boolean isDoPicActionShowRect(){
        return getIntansce().getBoolean("isDoPicActionShowRect");
    }

    public static boolean isShowFloatWindow(){
        return getIntansce().getBoolean("isShowFloatWindow",false);
    }

    public static boolean canMoveFloatMenu(){
        return getIntansce().getBoolean("canMoveFloatMenu",true);
    }

    public static void setCanMoveFloatMenu(boolean b){
        getIntansce().put("canMoveFloatMenu",b);
    }

    public static boolean canTileFloatMenu(){
        return getIntansce().getBoolean("canTileFloatMenu",true);
    }
    public static boolean useSignleFloat(){
        return getIntansce().getBoolean("useSignleFloat",false);
    }
    public static boolean useOCR(){
        return getIntansce().getBoolean("useOCR",false);
    }
    public static void setUseSignleFloat(boolean b){
        getIntansce().put("useSignleFloat",b);
    }
    public static void setCanTileFloatMenu(boolean b){
        getIntansce().put("canTileFloatMenu",b);
    }

    public static void setShowFloatWindow(boolean open){
        getIntansce().put("isShowFloatWindow",open);
    }

    public static List<JSONBean> getFloatMenuItems(){
        String items = getIntansce().getString("floatMenuItems","{\"items\":[]}");
        JSONArrayBean jsonArrayBean = new JSONBean(items).getArray("items");
        List<JSONBean> jsonBeans = new ArrayList<>();
        for (int i = 0 ; i<jsonArrayBean.length();i++){
            jsonBeans.add(jsonArrayBean.getJson(i));
        }
        return jsonBeans;
    }

    public static void saveFloatMenuItems(List<JSONBean> jsonBeans){
        JSONBean jsonBean = new JSONBean();
        JSONArrayBean jsonArrayBean = new JSONArrayBean(jsonBeans);
        jsonBean.put("items",jsonArrayBean);
        getIntansce().put("floatMenuItems",jsonBean.toString());
    }

    public static boolean isPiexlKeepLive(){
        return getIntansce().getBoolean("isPiexlKeepLive",false);
    }

    public static void setPiexlKeepLive(boolean open){
        getIntansce().put("isPiexlKeepLive",open);
    }

    public static int getFloatSize(){
        return SizeUtils.dp2px(getIntansce().getInt("floatSize",30));
    }
}
