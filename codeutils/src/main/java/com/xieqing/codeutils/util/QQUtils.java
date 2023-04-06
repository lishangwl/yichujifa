package com.xieqing.codeutils.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QQUtils {

    public static boolean 加入QQ群(Context context,String str) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqapi://card/show_pslcard?src_type=internal&version=1&uin=" + str + "&card_type=group&source=qrcode"));
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean 显示资料卡(Context context,String str) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqapi://card/show_pslcard?src_type=internal&version=1&uin=" + str));
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean 临时QQ会话(Context context,String str) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + str + "&version=1&src_type=web&web_src=null"));
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String 取QQ昵称(String qq) {
        Log.d("QQUtils","取QQ昵称 " + qq);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://r.qzone.qq.com/fcg-bin/cgi_get_score.fcg?mask=7&uins=" + qq).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            String name = StringUtils.getSubString(response.body().string(), ",\"", "\",");
            return name;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] 取QQ头像(String qq){
        Log.d("QQUtils","取QQ头像 " + qq);
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://q2.qlogo.cn/headimg_dl?bs=qq&dst_uin=" + qq + "&src_uin=*&fid=*&spec=100&url_enc=0&referer=bu_interface&term_type=PC").build();
            Call call = client.newCall(request);
            Response response = call.execute();
            return response.body().bytes();
        }catch(Exception e){
            e.printStackTrace();
            return new byte[0];
        }
    }


}
