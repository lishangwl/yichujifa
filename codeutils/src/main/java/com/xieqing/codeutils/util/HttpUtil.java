package com.xieqing.codeutils.util;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public  class HttpUtil {

    //创建okHttpClient对象
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    public static OkHttpClient getmOkHttpClient() {
        return mOkHttpClient;
    }

    public static void get(String url, HttpCall c){
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(c);
    }
    public static void get(String url, HashMap<String,String> header, HttpCall c){
        Request.Builder builder=new Request.Builder();
        builder.url(url);
        Set<String> s=header.keySet();
        for (String k:s){
            builder.addHeader(k,header.get(k));
        }
        final Request request = builder.build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(c);
    }
    public static void post(String url,String data,HttpCall c){
        post(url,data,null,c);
    }
    public static void post(String url,String data, HashMap<String,String> header, HttpCall c){
        Request.Builder builder=new Request.Builder();
        builder.url(url);
        if (header!=null){
            Set<String> s=header.keySet();
            for (String k:s){
                builder.addHeader(k,header.get(k));
            }
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),data);

        final Request request = builder.post(requestBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(c);
    }
    public static Response post(String url, String data) throws IOException {
        return post(url,data,(HashMap<String,String>)null);
    }
    public static Response get(String url) throws IOException {
        return get(url,(HashMap)null);
    }
    public static Response get(String url,HashMap<String,String> header) throws IOException {
        Request.Builder builder=new Request.Builder()
                .url(url);
        if (header!=null){
            Set<String> s=header.keySet();
            for (String k:s){
                builder.addHeader(k,header.get(k));
            }
        }

        final Request request = builder.get().build();
        Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }
    public static Response post(String url,String data, HashMap<String,String> header) throws IOException {
        Request.Builder builder=new Request.Builder();
        builder.url(url);
        if (header!=null){
            Set<String> s=header.keySet();
            for (String k:s){
                builder.addHeader(k,header.get(k));
            }
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),data);

        final Request request = builder.post(requestBody).build();
        Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }

    public static void uploadFile(String url,byte[] file){

    }

    public static class HttpCall implements Callback {
        Handler handler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                onError(msg.what,msg.obj.toString());
            }
        };
        Handler handler2=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                try {
                    onSuccess(msg.obj.toString());
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        public void onSuccess(String string) throws PackageManager.NameNotFoundException {

        }

        public void onError(int code,String string){

        }

        @Override
        public void onFailure(Call call, IOException e) {
            handler.sendMessage(handler.obtainMessage(0,e.toString()));
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code()==200){
                handler2.sendMessage(handler2.obtainMessage(0,response.body().string()));
            }else{
                handler.sendMessage(handler.obtainMessage(response.code(),response.message()));
            }
        }
    }
}
