package esqeee.xieqing.com.eeeeee.doAction.api;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xieqing.codeutils.util.ImageUtils;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.library.ocr.BaiduOCR;
import esqeee.xieqing.com.eeeeee.library.ocr.OCRScanResult;
import esqeee.xieqing.com.eeeeee.library.ocr.SouGouOcr;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class ClickText extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new ClickText();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {

        int scanType = param.getInt("scanType",0);
        if (scanType!=0 && scanType!= 1){
            scanType = 0;
        }
        String text = getString(param.getString("text"));
        int type = param.getInt("actionType");
        Rect rect = getRect(param);
        if (scanType == 0){
            AccessibilityNodeInfo nodeInfo = rect == null ? AccessbilityUtils.findNodeByText(text) : AccessbilityUtils.findNodeByTextFromRect(text,rect);
            if (nodeInfo == null){
                log("执行:<"+getName()+">: 未找到（"+text+"）,ps:某些场景可能无法识别文字，建议选择ocr识别。");
            }else{
                Rect bounds = new Rect();
                nodeInfo.getBoundsInScreen(bounds);int x = bounds.left + bounds.width()/2,y=bounds.top + bounds.height()/2;
                log("执行:<"+getName()+">: 找到（"+text+"）");
                if (param.getBoolean("assign",false)){
                    setValue(queryVariable(param.getString("x")),bounds.left);
                    setValue(queryVariable(param.getString("y")),bounds.top);
                    setValue(queryVariable(param.getString("w")),bounds.width());
                    setValue(queryVariable(param.getString("h")),bounds.height());
                    return true;
                }
                if (type == 3 || type == 49){
                    if (SettingsPreference.rootMode()){
                        Shell.getShell().click(x,y);
                    }else{
                        waitForAccessbility();
                        AccessbilityUtils.clickNode(nodeInfo);
                    }
                }else{
                    if (SettingsPreference.rootMode()){
                        Shell.getShell().touchXY(x,y,1500);
                    }else{
                        waitForAccessbility();
                        AccessbilityUtils.longClickNode(nodeInfo);
                    }
                }
            }
        }else{
            OCRScanResult.ScanItem[] result = SouGouOcr.scan(ImageUtils.cutBitmap(getActionRun().getCaptruer().captrueScreen(),rect));
            if (result==null || result.length ==0){
                log("执行:<"+getName()+">: 未找到（"+text+"）");
            }else{
                for (OCRScanResult.ScanItem item : result){
                    if (item.text.contains(text)){
                        int x = item.left + item.width/2 + (rect==null?0:rect.left),y = item.top + item.height/2+ (rect==null?0:rect.top);
                        log("执行:<"+getName()+">: 找到（"+item+"）");
                        if (param.getBoolean("assign",false)){
                            setValue(queryVariable(param.getString("x")),item.left);
                            setValue(queryVariable(param.getString("y")),item.top);
                            setValue(queryVariable(param.getString("w")),item.width);
                            setValue(queryVariable(param.getString("h")),item.height);
                            return true;
                        }
                        if (type == 3 || type == 49){
                            if (SettingsPreference.rootMode()){
                                Shell.getShell().click(x,y);
                            }else{
                                AccessbilityUtils.clickXY(x,y);
                            }
                        }else{
                            if (SettingsPreference.rootMode()){
                                Shell.getShell().touchXY(x,y,1500);
                            }else{
                                AccessbilityUtils.longClickXY(x,y);
                            }
                        }
                        return true;
                    }
                }
                log("执行:<"+getName()+">: 未找到（"+text+"）");
            }
        }
        return true;
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
