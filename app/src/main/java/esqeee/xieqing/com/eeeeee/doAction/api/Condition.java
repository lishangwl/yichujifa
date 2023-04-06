package esqeee.xieqing.com.eeeeee.doAction.api;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.xieqing.codeutils.util.Utils;

import java.io.File;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.helper.RectFloatHelper;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.MatchingUtil;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.ScreenPointBean;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.library.image.ColorFinder;
import esqeee.xieqing.com.eeeeee.library.ocr.BaiduOCR;
import esqeee.xieqing.com.eeeeee.library.ocr.OCRScanResult;
import esqeee.xieqing.com.eeeeee.library.ocr.SouGouOcr;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class Condition extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Condition();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        int type = param.getInt("actionType");

        switch (type){
            case 54:
            case 63:
                return recyleColor(param);
            case 44:
            case 45:
                return recyleText(param);

            case 47:
            case 48:
                return recyleImage(param);
            case 72:
                return recyleVar(param);
        }
        return false;
    }

    private boolean recyleVar(JSONBean param) {
        String value = getString(param.getString("value"));
        String var = param.getString("var");
        int assignType = param.getInt("assgin",0);
        JSONBean jsonBean = queryVariable(var);

        if (jsonBean == null){
            RuntimeLog.e("<条件：变量比较>:找不到变量 ["+var+"]");
            getActionRun().stopDo();
            return false;
        }
        String varValue = getString(jsonBean.getString("value"));

        try {
            switch (assignType){
                case 0:// ==
                    return varValue.equals(value);
                case 1:// !=
                    return !varValue.equals(value);
                case 2:// >
                    MathResult mathResult = evalMath(value);
                    if (mathResult.getException()!=null){
                        throw mathResult.getException();
                    }
                    return Double.parseDouble(varValue)>mathResult.getResult();
                case 3:// <
                    mathResult = evalMath(value);
                    if (mathResult.getException()!=null){
                        throw mathResult.getException();
                    }
                    return Double.parseDouble(varValue)<mathResult.getResult();
                case 4:// >=
                    mathResult = evalMath(value);
                    if (mathResult.getException()!=null){
                        throw mathResult.getException();
                    }
                    return Double.parseDouble(varValue)>=mathResult.getResult();
                case 5:// <=
                    mathResult = evalMath(value);
                    if (mathResult.getException()!=null){
                        throw mathResult.getException();
                    }
                    return Double.parseDouble(varValue)<=mathResult.getResult();
                case 6:// contains
                    return varValue.contains(value);
            }
        }catch (Exception e){
            RuntimeLog.e("无法转换成整数型:"+e.toString());
        }
        return false;
        /*int type = jsonBean.getInt("type");
        if (type == 0){
            //string
            return value.equals(varValue);
        }else if (type == 1){
            //int
            int varIntValue = 0;
            if (!varValue.equals("Null")){
                try {
                    varIntValue = Integer.parseInt(varValue);
                }catch (NumberFormatException e){
                    log("<条件：变量比较>：无法比较，整数型变量错误："+e.getMessage());
                    getActionRun().stopDo();
                    return false;
                }
            }
            MathResult result = evalMath(value);
            if (result.isError()){
                log("<条件：变量比较>：无法比较，计算错误："+result.getException().getMessage());
                getActionRun().stopDo();
                return false;
            }else{
                return (int)result.getResult() == varIntValue;
            }
        }else if (type == 3){
            //bool
            String[] variables = findVariables(value);
            JSONBean varBean = null;
            for (String v:variables){
                if (var != null){
                    log("<条件：变量比较>：布尔型的变量只能与 真 或者 假 或者 布尔型的单一变量 进行比较");
                    getActionRun().stopDo();
                    return false;
                }
                varBean = queryVariable(v);
            }
            if (varBean != null && varBean.getInt("type")!=VariableType.BOOL.ordinal()){
                log("<条件：变量比较>：布尔型的变量只能与 真 或者 假 或者 布尔型的单一变量 进行比较");
                getActionRun().stopDo();
                return false;
            }
            value = getString(value);
            if (!value.trim().equals("真") && !value.trim().equals("假")&& !value.trim().equals("Null")){
                log("<条件：变量比较>：布尔型的变量只能与 真 或者 假 或者 布尔型的单一变量 进行比较");
                getActionRun().stopDo();
                return false;
            }else{
                return varValue.equals(value.trim());
            }
        }else{
            log("<条件：变量比较>：错误，不支持比较的变量 ["+var+"]  类型["+VariableType.values()[type].getTypeName()+"]");
            getActionRun().stopDo();
            return false;
        }*/
    }

    private boolean recyleImage(JSONBean param) {
        int accetrue = param.getInt("accetrue",80);
        File image = new File(param.getString("fileName"));
        if (!image.exists()){
            RuntimeLog.e("执行:<"+getName()+">: 图片不存在或已被删除！");
            return false;
        }
        Bitmap bitmap = ImageUtils.getBitmap(image);

        int type = param.getInt("actionType");
        Rect rect = getRect(param);


        ScreenPointBean screenPointBean = MatchingUtil.match(getActionRun().getCaptruer().capture().getMat(),bitmap,rect,accetrue);
        if (screenPointBean == null){
            log("执行:<是否有该图片>: 未找到");
            return false;
        }else{
            if (SettingsPreference.isDoPicActionShowRect()){
                RectFloatHelper.getHelper(Utils.getApp()).showRectView(screenPointBean.getImgX()+ (rect == null?0:rect.left),screenPointBean.getImgY()+ (rect == null?0:rect.top),screenPointBean.getImgXlegth(),screenPointBean.getImgYlength());
                ThreadUtils.sleep(500);
                RectFloatHelper.getHelper(Utils.getApp()).removeRectView();
            }
            log("执行:<是否有该图片>: 找到图，位置在（"+screenPointBean.getImgX()+","+screenPointBean.getImgY()+"）");
            return true;
        }
    }

    private boolean recyleColor(JSONBean param) {
        int accetrue = param.getInt("accetrue",5);
        int color = Color.argb(255,param.getInt("red"),param.getInt("green"),param.getInt("blue"));
        Rect rect = getRect(param);

        org.opencv.core.Point point = ColorFinder.getFinder().findColor(getActionRun().getCaptruer().capture().getMat(),
                color,accetrue,rect == null?null:new org.opencv.core.Rect(rect.left,rect.top,rect.right - rect.left,rect.bottom - rect.top));
        if (point == null){
            log("执行:<是否含有颜色值>: 未找到颜色值["+color+"]");
            return false;
        }else{
            log("执行:<是否含有颜色值>: 找到颜色值，位置在（"+point.x+","+point.y+"）");
            return true;
        }
    }

    public org.opencv.core.Rect getRect2(JSONBean param) {
        int left = param.getInt("x",-1);
        int top = param.getInt("y",-1);
        int right = param.getInt("width",-1);
        int bottom = param.getInt("height",-1);
        if (left == -1 || top == -1 || right == -1 || bottom == -1){
            return null;
        }
        return new org.opencv.core.Rect(left,top,right,bottom);
    }

    private boolean recyleText(JSONBean param) {
        int scanType = param.getInt("scanType",0);
        if (scanType!=0 && scanType!= 1){
            scanType = 0;
        }
        String text = getString(param.getString("text"));
        Rect rect = getRect(param);

        if (scanType == 0){
            AccessibilityNodeInfo nodeInfo = rect == null ? AccessbilityUtils.findNodeByText(text) : AccessbilityUtils.findNodeByTextFromRect(text,rect);
            if (nodeInfo == null){
                log("执行:<是否含有该文字>: 未找到（"+text+"）,ps:某些场景可能无法识别文字，建议选择ocr识别。");
                return false;
            }else{
                log("执行:<是否含有该文字>: 找到（"+text+"）");
                return true;
            }
        }else{
            OCRScanResult.ScanItem[] result = SouGouOcr.scan(ImageUtils.cutBitmap(getActionRun().getCaptruer().captrueScreen(),rect));
            if (result==null || result.length ==0){
                log("执行:<"+getName()+">: 未找到（"+text+"）");
            }else{
                for (OCRScanResult.ScanItem item : result){
                    if (item.text.contains(text)){
                        log("执行:<是否含有该文字>: 找到（"+text+"）");
                        return true;
                    }
                }
                log("执行:<是否含有该文字>: 未找到（"+text+"）");
                return false;
            }
        }
        return false;
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public String getName() {
        return "识别文字";
    }
}
