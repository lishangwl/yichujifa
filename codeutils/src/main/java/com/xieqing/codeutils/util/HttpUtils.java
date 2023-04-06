package com.xieqing.codeutils.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpUtils {

    private static Map<String,String> cookiesMap = new HashMap<>();
    public static  Bitmap getBitmap(String url){
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            if (conn.getResponseCode() == 200){
                getCookie(conn);
                return BitmapFactory.decodeStream(conn.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }


        return null;
    }

    public static synchronized String Post(String url, String param) {
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
            buildCookie(conn);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            // 发送请求参数
            conn.getOutputStream().write(param.getBytes());
            // flush输出流的缓冲
            conn.getOutputStream().flush();
            if (conn.getResponseCode() == 200) {
                getCookie(conn);
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                byte[] buffer=new byte[1024];
                int len=0;
                while ((len=conn.getInputStream().read(buffer))>0) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                return new String(byteArrayOutputStream.toByteArray());
            }
            return "";
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e.toString());
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static synchronized String Post(String url, String param,Map<String,String> header) {
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
            Set<String> keys = header.keySet();
            for (String key:keys){
                conn.setRequestProperty(key,header.get(key));
            }
            buildCookie(conn);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            // 发送请求参数
            conn.getOutputStream().write(param.getBytes());
            // flush输出流的缓冲
            conn.getOutputStream().flush();
            if (conn.getResponseCode() == 200) {
                getCookie(conn);
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                byte[] buffer=new byte[1024];
                int len=0;
                while ((len=conn.getInputStream().read(buffer))>0) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                return new String(byteArrayOutputStream.toByteArray());
            }
            return "";
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e.toString());
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static synchronized void getCookie(HttpURLConnection conn) {
        List<String> setCookie = conn.getHeaderFields().get("Set-Cookie");
        if (setCookie!=null){
            for (String cookie : setCookie){
                String[] cookies = cookie.split(";");
                for (String one : cookies){
                    String[] keys = one.split("=");
                    if (keys.length == 2 && !keys[0].trim().equals("path")){
                        cookiesMap.put(keys[0].trim(),keys[1].trim());
                    }
                }
            }
        }
    }

    private static synchronized void buildCookie(HttpURLConnection conn) {
        String cookie = new String();
        Set<String> keys = cookiesMap.keySet();
        for (String key : keys){
            if (!cookie.equals("")){
                cookie += ";";
            }
            cookie += key +"="+cookiesMap.get(key);
        }
        conn.setRequestProperty("Cookie",cookie);
    }

    public static synchronized String Get(String url) {
        return Get(url,null);
    }

    public static class Response{
        public int code = 200;
        public String message = "";
        private Body body;
        private Header header;


        private Response(HttpURLConnection httpURLConnection) throws IOException {
            code = httpURLConnection.getResponseCode();
            message = httpURLConnection.getResponseMessage();
            body = new Body(httpURLConnection);
            header = new Header(httpURLConnection);
        }

        private Response(Exception e){
            code = -1;
            message = e.getMessage();
            body = new Body(e);
            header = new Header(e);
        }

        public Header header(){
            return header;
        }
        public Body body(){
            return body;
        }

        public int code(){
            return code;
        }

        public String message(){
            return message;
        }

        public static class Header{
            public Map<String,List<String>> header;
            public List<String> cookies;
            private Header(HttpURLConnection httpURLConnection) throws IOException {
                header = httpURLConnection.getHeaderFields();
                cookies = header.get("Set-Cookie");
            }

            private Header(Exception e){
                header = new HashMap<>();
                cookies = new ArrayList<>();
            }

            public Map<String,List<String>> header(){
                return header;
            }

            public List<String> header(String key){
                return header.get(key);
            }

            public List<String> cookies(){
                return cookies;
            }
        }

        public static class Body{
            private byte[] bytes;
            private String string;
            private Body(HttpURLConnection httpURLConnection) throws IOException {
                bytes = readIO(httpURLConnection.getInputStream());
                string = new String(bytes);
            }

            private Body(Exception e){
                bytes = e.getMessage().getBytes();
                string = e.getMessage();
            }

            public byte[] bytes(){
                return bytes;
            }

            public String string(){
                return string;
            }

            public String string(String charest) throws UnsupportedEncodingException {
                return new String(bytes,charest);
            }

            private byte[] readIO(InputStream inputStream) throws IOException {
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                byte[] buffer=new byte[1024];
                int len=0;
                while ((len=inputStream.read(buffer))>0) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    public static class Request{
        public static final String WWW_ENCODE = "application/x-www-form-urlencoded";
        public static final String WWW_ENCODE_utf_8 = "application/x-www-form-urlencoded; charset=UTF-8";
        private String url;
        private String method = "GET";
        private byte[] contents = new byte[0];
        private Map<String,String> header = new HashMap<>();
        public Request url(String url){
            this.url = url;
            return this;
        }

        public Request get(){
            this.method = "GET";
            return this;
        }

        public Request post(){
            this.method = "POST";
            header.put("Content-Type",WWW_ENCODE);
            return this;
        }

        public Request contentByte(byte[] bytes){
            this.contents = bytes;
            return this;
        }

        public Request contentString(String content){
            this.contents = content.getBytes();
            return this;
        }

        public Request header(String key,String value){
            this.header.put(key,value);
            return this;
        }

        public Request header(Map<String,String> header){
            this.header = header;
            return this;
        }

        public Response exec(){
            try {
                URL url2=new URL(url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url2.openConnection();
                httpURLConnection.setRequestMethod(method);

                Set<String> keys = header.keySet();
                for (String key:keys){
                    httpURLConnection.setRequestProperty(key,header.get(key));
                }
                //httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36");
                httpURLConnection.setDoInput(true);
                if (method.equals("POST")){
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.getOutputStream().write(contents);
                    httpURLConnection.getOutputStream().flush();
                    httpURLConnection.getOutputStream().close();
                }
                httpURLConnection.setConnectTimeout(10000);
                InputStream inputStream=httpURLConnection.getInputStream();
                return new Response(httpURLConnection);
            } catch (Exception e) {
                return new Response(e);
            }
        }
    }

    public static synchronized Response GetToResponse(String url) {
        try {
            URL url2=new URL(url);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url2.openConnection();
            httpURLConnection.setRequestMethod("GET");
            //httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(10000);
            InputStream inputStream=httpURLConnection.getInputStream();
            return new Response(httpURLConnection);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized String Get(String url, List<String> cookies) {
        try {
            URL url2=new URL(url);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url2.openConnection();
            httpURLConnection.setRequestMethod("GET");
            //httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(10000);
            InputStream inputStream=httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode()==200) {
                if (cookies != null) {
                    Map<String,List<String>> responeHeader = httpURLConnection.getHeaderFields();
                    Log.d("accurate",responeHeader.toString());
                    List<String> set = responeHeader.get("Set-Cookie");
                    if (set!=null){
                        cookies.addAll(set);
                    }
                }
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                byte[] buffer=new byte[1024];
                int len=0;
                while ((len=inputStream.read(buffer))>0) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                return new String(byteArrayOutputStream.toByteArray());
            }else {
                return "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
