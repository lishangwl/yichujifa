package esqeee.xieqing.com.eeeeee.service;

import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.RequiresApi;


public class OCRUtils {
    /*
    *   点击屏幕文字
    * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean clickText(String string){
        /*if (!SettingsPreference.useOCR()){
            AccessibilityNodeInfo nodeInfo = AccessbilityUtils.findNodeByText(string);
            if (nodeInfo == null){
                RuntimeLog.log("未找到【"+string+"】，ps:某些场景可能无法识别文字，建议在设置处选择ocr识别引擎。\n");
                return false;
            }
            return AccessbilityUtils.clickNode(nodeInfo);
        }
        OCRScanResult.ScanItem[] result = BaiduOCR.getOcr().accurate(ScreenCap.getCaptruer().captrueScreen());
        if (result==null || result.length ==0){
            return false;
        }
        for (OCRScanResult.ScanItem item : result){
            if (item.text.contains(string)){
                RuntimeLog.log("文字识别OCR: 识别成功: "+string+" x:"+item.left+",y:"+item.top);
                return AccessbilityUtils.clickXY(item.left + item.width/2,item.top + item.height/2);
            }
        }
        RuntimeLog.log("文字识别OCR: 未识别到"+string);*/
        return false;
    }

    /*
     *   点击区域屏幕文字
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean clickText(String string, Rect rect){
        /*if (!SettingsPreference.useOCR()){
            AccessibilityNodeInfo nodeInfo = AccessbilityUtils.findNodeByTextFromRect(string,rect);
            if (nodeInfo == null){
                RuntimeLog.log("未找到【"+string+"】，ps:某些场景可能无法识别文字，建议在设置处选择ocr识别引擎。\n");
                return false;
            }
            return AccessbilityUtils.clickNode(nodeInfo);
        }
        OCRScanResult.ScanItem[] result = BaiduOCR.getOcr().accurate(ImageUtils.cutBitmap(ScreenCap.getCaptruer().captrueScreen(),rect));
        if (result==null || result.length ==0){
            return false;
        }
        for (OCRScanResult.ScanItem item : result){
            if (item.text.contains(string)){
                RuntimeLog.log("文字识别OCR: 识别成功: "+string+" x:"+item.left+",y:"+item.top);
                return AccessbilityUtils.clickXY(item.left + item.width/2 + rect.left,item.top + item.height/2 + rect.top);
            }
        }
        RuntimeLog.log("文字识别OCR: 未识别到"+string);*/
        return false;
    }

    /*
     *   长按屏幕文字
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean longClickText(String string){
        /*if (!SettingsPreference.useOCR()){
            AccessibilityNodeInfo nodeInfo = AccessbilityUtils.findNodeByText(string);
            if (nodeInfo == null){
                RuntimeLog.log("未找到【"+string+"】，ps:某些场景可能无法识别文字，建议在设置处选择ocr识别引擎。\n");
                return false;
            }
            return AccessbilityUtils.longClickNode(nodeInfo);
        }
        OCRScanResult.ScanItem[] result = BaiduOCR.getOcr().accurate(ScreenCap.getCaptruer().captrueScreen());
        if (result==null || result.length ==0){
            return false;
        }
        for (OCRScanResult.ScanItem item : result){
            if (item.text.contains(string)){
                RuntimeLog.log("文字识别OCR: 识别成功: "+string+" x:"+item.left+",y:"+item.top);
                return AccessbilityUtils.longClickXY(item.left + item.width/2,item.top + item.height/2);
            }
        }
        RuntimeLog.log("文字识别OCR: 未识别到"+string);*/
        return false;
    }

    /*
     *   长按区域屏幕文字
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean longClickText(String string, Rect rect){
        /*if (!SettingsPreference.useOCR()){
            AccessibilityNodeInfo nodeInfo = AccessbilityUtils.findNodeByTextFromRect(string,rect);
            if (nodeInfo == null){
                RuntimeLog.log("未找到【"+string+"】，ps:某些场景可能无法识别文字，建议在设置处选择ocr识别引擎。\n");
                return false;
            }
            return AccessbilityUtils.longClickNode(nodeInfo);
        }
        OCRScanResult.ScanItem[] result = BaiduOCR.getOcr().accurate(ImageUtils.cutBitmap(ScreenCap.getCaptruer().captrueScreen(),rect));
        if (result==null || result.length ==0){
            return false;
        }
        for (OCRScanResult.ScanItem item : result){
            if (item.text.contains(string)){
                RuntimeLog.log("文字识别OCR: 识别成功: "+string+" x:"+item.left+",y:"+item.top);
                return AccessbilityUtils.longClickXY(item.left + item.width/2 + rect.left,item.top + item.height/2 + rect.top);
            }
        }
        RuntimeLog.log("文字识别OCR: 未识别到"+string);*/
        return false;
    }
}
