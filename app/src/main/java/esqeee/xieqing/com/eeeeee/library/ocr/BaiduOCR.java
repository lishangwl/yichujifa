package esqeee.xieqing.com.eeeeee.library.ocr;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.xieqing.codeutils.util.ConvertUtils;
import com.xieqing.codeutils.util.EncryptUtils;
import com.xieqing.codeutils.util.HttpUtil;
import com.xieqing.codeutils.util.HttpUtils;
import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.LogUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class BaiduOCR {
    private static BaiduOCR ocr;

    public static BaiduOCR getOcr() {
        if (ocr == null) {
            ocr = new BaiduOCR();
        }
        return ocr;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setRequestProperty("Connection", "keep-alive");
            //
            //conn.setRequestProperty("Cookie", "BAIDUID=2910178331363B1C1F0DCE95F764B6FC:FG=1;  BDAIUID=5d426b21467a12295453452; BDAUIDD=5d426b21467e86095146234;");
            conn.setRequestProperty("Cookie", "BAIDUID=" + EncryptUtils.encryptMD5ToString(System.currentTimeMillis() + "") + ":FG=1;" + cookies);
            Log.d("accurate", cookies);
            conn.setRequestProperty("Origin", "https://cloud.baidu.com");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            conn.setRequestProperty("Content-Length", param.getBytes().length + "");
            conn.setRequestProperty("Referer", "https://ai.baidu.com/tech/ocr/general");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            // 发送请求参数
            conn.getOutputStream().write(param.getBytes());
            // flush输出流的缓冲
            conn.getOutputStream().flush();
            if (conn.getResponseCode() == 200) {
                return readIO(conn.getInputStream());
            }

        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e.toString());
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static byte[] readFile(File file) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static String readIO(InputStream inputStream) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    public int getScale(int value) {
        float scale = (float) 100 / (float) SPUtils.getInstance(Utils.getApp().getPackageName() + "_preferences").getInt("ocring", 80);
        float p = (float) value * scale;
        return (int) p;
    }

    public OCRScanResult.ScanItem[] accurate(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled() || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
            RuntimeLog.i("文字识别OCR: 图像为空！");
            return null;
        }

        if (cookies.equals("")) {
            refreshUUid();
        }

        //bitmap = ImageUtils.resizeBitmap(bitmap,(float)SPUtils.getInstance(Utils.getApp().getPackageName()+"_preferences").getInt("ocring", 80) / (float)100);
        String string = "image={base64}&image_url=&type=general_location&detect_direction=false";
        String baseString = URLEncoder.encode("data:image/png;base64," + Base64.encodeToString(ConvertUtils.bitmap2Bytes(bitmap, Bitmap.CompressFormat.PNG), Base64.NO_WRAP));
        string = string.replace("{base64}", baseString);
        String finalString = string;
        try {
            String s = sendPost("http://cloud.baidu.com/aidemo", finalString);
            //RuntimeLog.log(s);
            JSONObject jsonObject = new JSONObject(s);
            jsonObject = jsonObject.getJSONObject("data");
            int num = jsonObject.getInt("words_result_num");
            OCRScanResult.ScanItem[] items = new OCRScanResult.ScanItem[num];
            for (int i = 0; i < num; i++) {
                JSONObject item = jsonObject.getJSONArray("words_result").getJSONObject(i);
                JSONObject local = item.getJSONObject("location");
                OCRScanResult.ScanItem scanItem = new OCRScanResult.ScanItem();
                scanItem.height = getScale(local.getInt("height"));
                scanItem.top = getScale(local.getInt("top"));
                scanItem.left = getScale(local.getInt("left"));
                scanItem.width = getScale(local.getInt("width"));
                scanItem.text = item.getString("words");
                items[i] = scanItem;
            }
            return items;
        } catch (JSONException e) {
            e.printStackTrace();
            refreshUUid();
        }
        return null;
    }

    public String accurateText(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled() || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
            RuntimeLog.i("文字识别OCR: 图像为空！");
            return null;
        }

        if (cookies.equals("")) {
            cookies = SPUtils.getInstance().getString("BAIDUaiCK");
            if (cookies.equals("")) {
                refreshUUid();
            }
        }

        //bitmap = ImageUtils.resizeBitmap(bitmap,(float)SPUtils.getInstance(Utils.getApp().getPackageName()+"_preferences").getInt("ocring", 80) / (float)100);
        String string = "image={base64}&image_url=&type=commontext&detect_direction=false";
        String baseString = URLEncoder.encode("data:image/png;base64," + Base64.encodeToString(ConvertUtils.bitmap2Bytes(bitmap, Bitmap.CompressFormat.PNG), Base64.NO_WRAP));
        string = string.replace("{base64}", baseString);
        String finalString = string;
        try {
            String s = sendPost("https://cloud.baidu.com/aidemo", finalString);
            RuntimeLog.log(s);
            JSONObject jsonObject = new JSONObject(s);
            jsonObject = jsonObject.getJSONObject("data");
            int num = jsonObject.getInt("words_result_num");
            JSONArray resultJson = jsonObject.getJSONArray("words_result");
            String result = "";
            for (int i = 0; i < num; i++) {
                JSONObject item = resultJson.getJSONObject(i);
                result += "\n" + item.getString("words");
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            refreshUUid();
        }
        return "NULL";
    }

    static String cookies = "";

    private void refreshUUid() {
        List<String> header = new ArrayList<>();
        String s = Get("http://ai.baidu.com/tech/ocr/general", header);
        cookies = header.toString().replace(",", ";");
        cookies = cookies.substring(1, cookies.length() - 1);
        SPUtils.getInstance().put("BAIDUaiCK", cookies);
        Log.d("accurate", cookies + "s");
    }

    public static synchronized String Get(String url, List<String> cookies) {
        try {
            URL url2 = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(10000);
            InputStream inputStream = httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode() == 200) {
                if (cookies != null) {
                    Map<String, List<String>> responeHeader = httpURLConnection.getHeaderFields();
                    Log.d("accurate", responeHeader.toString());
                    List<String> set = responeHeader.get("Set-Cookie");
                    if (set != null) {
                        cookies.addAll(set);
                    }
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                return new String(byteArrayOutputStream.toByteArray());
            } else {
                return "";
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}
