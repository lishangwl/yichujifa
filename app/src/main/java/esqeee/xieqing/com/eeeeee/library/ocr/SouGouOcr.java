package esqeee.xieqing.com.eeeeee.library.ocr;

import android.graphics.Bitmap;
import android.util.Base64;

import com.xieqing.codeutils.util.ConvertUtils;
import com.xieqing.codeutils.util.HttpUtils;

import java.net.URLEncoder;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class SouGouOcr {

    public static OCRScanResult.ScanItem[] scan(Bitmap bitmap){
        String string = "imgBase={base64}&lang=auto&company=";
        String baseString = URLEncoder.encode("data:image/png;base64,"+ Base64.encodeToString(ConvertUtils.bitmap2Bytes(bitmap,Bitmap.CompressFormat.PNG),Base64.NO_WRAP));
        string=string.replace("{base64}", baseString);
        //String result = HttpUtils.Post("https://aidemo.youdao.com/ocrapi1",string);

        HttpUtils.Response response = new HttpUtils.Request().url("https://aidemo.youdao.com/ocrapi1")
                .post()
                .header("Origin","https://ai.youdao.com")
                .header("Referer","https://ai.youdao.com/product-ocr.s")
                .contentString(string)
                .exec();
        //RuntimeLog.log(response.body().string());
        JSONBean jsonBean = new JSONBean(response.body().string());
        JSONArrayBean lines = jsonBean.getArray("lines");
        if (lines == null){
            return null;
        }
        OCRScanResult.ScanItem[] items = new OCRScanResult.ScanItem[lines.length()];
        for (int i = 0 ;i<lines.length();i++){
            OCRScanResult.ScanItem item = new OCRScanResult.ScanItem();
            JSONBean j=lines.getJson(i);
            String[] pixel = j.getString("boundingBox").split(",");
            if (pixel.length != 8){
                continue;
            }
            item.text = j.getString("words");
            item.left = Integer.parseInt(pixel[0]);
            item.top = Integer.parseInt(pixel[1]);
            item.width = Integer.parseInt(pixel[3]) - item.left;
            item.height = Integer.parseInt(pixel[5]) - item.top;
            //RuntimeLog.log(item);
            items[i] = item;
        }
        return items;
    }

    public static String scanText(Bitmap bitmap){
        if (1==1){
           return BaiduOCR.getOcr().accurateText(bitmap);
        }
        OCRScanResult.ScanItem[] scanItems = scan(bitmap);
        if (scanItems == null || scanItems.length == 0){
            return "";
        }
        String text = "";
        for (OCRScanResult.ScanItem item : scanItems){
            text += item.text;
        }
        return text;
    }
}
