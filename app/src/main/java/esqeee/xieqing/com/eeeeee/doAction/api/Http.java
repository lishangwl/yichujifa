package esqeee.xieqing.com.eeeeee.doAction.api;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class Http extends Base {
    public static Http intansce;
    public static Http getIntansce(){
        if (intansce==null){
            intansce = new Http();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        String url = getString(param.getString("url"));
        int requestMethod = param.getInt("requestMethod",0);
        JSONArrayBean headers = param.getArray("header");
        JSONArrayBean datas = param.getArray("datas");
        String var = param.getString("var");


        if (!url.startsWith("http://") && !url.startsWith("https://")){
            url = "http://"+url;
        }

        log("<访问网页>:正在以"+(requestMethod == 0 ? "GET":"POST")+"形式访问："+url);



        HashMap<String,String> head = new HashMap<>();
        if (headers!=null){
            for (int i = 0 ; i < headers.length() ; i++){
                JSONBean item = headers.getJson(i);
                head.put(getString(item.getString("key")),getString(item.getString("value")));
            }
        }
        String result = "";
        if (requestMethod == 0){
            result = sendGet(url,head);
        }else{

            int dataType = param.getInt("dataType",0);

            HashMap<String,String> data = new HashMap<>();
            if (datas!=null){
                for (int i = 0 ; i < datas.length() ; i++){
                    JSONBean item = datas.getJson(i);
                    data.put(getString(item.getString("key")),getString(item.getString("value")));
                }
            }
            if (dataType == 0){
                result = sendPost(url,head,data,"application/x-www-form-urlencoded");
            }else if (dataType == 1){
                result = sendPost(url,head,data,"text/json");
            }else{
                result = sendPost(url,head,getString(param.getString("post_text")),"application/x-www-form-urlencoded");
            }
        }

        JSONBean variable = queryVariable(var);
        if (variable!=null){
            if (variable.getInt("type") == VariableType.STRING.ordinal()){
                variable.put("value",result);
            }
        }
        setValue(queryVariable(param.getString("respone_header")),response);
        return true;
    }
    public String sendGet(String url, HashMap<String,String> header) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Connection", "keep-alive");

            bindHeader(conn,header);

            // 发送POST请求必须设置如下两行
            conn.setDoInput(true);
            conn.setUseCaches(false);
            if (conn.getResponseCode() == 200) {
                response(conn);
                return readIO(conn.getInputStream(),"UTF-8");
            }else{
                response(conn);
                return readIO(conn.getErrorStream(),"UTF-8");
            }
        } catch (Exception e) {
            return "发送 POST 请求出现异常！"+e.toString();
        }
    }

    private void response(HttpURLConnection conn) {
        response = "";
        Map<String, List<String>> headers = conn.getHeaderFields();
        for (String key : headers.keySet()){
            List<String> values = headers.get(key);
            for (String value : values){
                response += key +":\t"+value+"\r\n";
            }
        }
    }

    private String response = "";
    public String sendPost(String url, HashMap<String,String> header,String datas,String contentType) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Connection", "keep-alive");

            bindHeader(conn,header);

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            // 发送请求参数
            conn.getOutputStream().write(datas.getBytes());
            // flush输出流的缓冲
            conn.getOutputStream().flush();
            if (conn.getResponseCode() == 200) {
                response(conn);
                return readIO(conn.getInputStream(),"UTF-8");
            }else{
                response(conn);
                return readIO(conn.getErrorStream(),"UTF-8");
                //return conn.getResponseCode()+" - "+conn.getResponseMessage();
            }

        } catch (Exception e) {
            return "发送 POST 请求出现异常！"+e.toString();
        }
    }

    public String sendPost(String url, HashMap<String,String> header,HashMap<String,String> datas,String contentType) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Connection", "keep-alive");

            bindHeader(conn,header);

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            String param = new String();

            if (contentType.equals("text/json")){
                param = buildJson(datas);
            }else{
                param = buildUrlencode(datas);
            }

            // 发送请求参数
            conn.getOutputStream().write(param.getBytes());
            // flush输出流的缓冲
            conn.getOutputStream().flush();
            if (conn.getResponseCode() == 200) {
                response(conn);
                return readIO(conn.getInputStream(),"UTF-8");
            }else{
                response(conn);
                return readIO(conn.getErrorStream(),"UTF-8");
                //return conn.getResponseCode()+" - "+conn.getResponseMessage();
            }

        } catch (Exception e) {
            return "发送 POST 请求出现异常！"+e.toString();
        }
    }

    private String buildJson(HashMap<String,String> datas) {
        JSONObject jsonObject = new JSONObject();
        Iterator<Map.Entry<String, String>> iter = datas.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String,String> entry = iter.next();
            String inputName = entry.getKey();
            String inputValue = entry.getValue();
            try {
                try {
                    jsonObject.put(inputName,Long.parseLong(inputValue));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    jsonObject.put(inputName,inputValue);
                }
            }catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    private String buildUrlencode(HashMap<String,String> datas) {
        String param = new String();
        Set<String> keys = datas.keySet();
        for (String key : keys){
            if (!param.equals("")){
                param += "&";
            }
            param += key +"="+URLEncoder.encode(datas.get(key));
        }
        return param;
    }

    private void bindHeader(HttpURLConnection conn, HashMap<String,String> header) {
        if (header == null){
            return;
        }
        Set<String> keys = header.keySet();
        for (String key : keys){
            if (!TextUtils.isEmpty(key)){
                conn.setRequestProperty(key,header.get(key));
            }
        }
    }


    private String readIO(InputStream inputStream,String charest) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int len=-1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer))!=-1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return new String(byteArrayOutputStream.toByteArray(),charest);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public String getName() {
        return "网页访问";
    }
}
