package esqeee.xieqing.com.eeeeee.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;

import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bean.StandbyAction;

public class TimerService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(this);
    }


    public String md5(String str) {
        int i = 0;
        char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            int length = digest.length;
            char[] cArr2 = new char[(length * 2)];
            int i2 = 0;
            while (i < length) {
                byte b = digest[i];
                int i3 = i2 + 1;
                cArr2[i2] = cArr[(b >>> 4) & 15];
                i2 = i3 + 1;
                cArr2[i3] = cArr[b & 15];
                i++;
            }
            return new String(cArr2);
        } catch (Exception e) {
            return null;
        }
    }
    public String POST(String url,String data) {
        try {
            URL url2=new URL(url);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url2.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            String time=System.currentTimeMillis()/1000+"";
            String sign=md5("damingshishuaige"+time+"shuaigeshidaming");
            httpURLConnection.setRequestProperty("time", time);
            httpURLConnection.setRequestProperty("sign", sign);
            httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.getOutputStream().write(data.getBytes());
            httpURLConnection.getOutputStream().flush();
            InputStream inputStream=httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode()==200) {
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(StandbyAction standbyAction){
        new Thread(new Runnable() {
            @Override
            public void run() {
                POST("http://www.baidu.com/esqeee.xieqing.com.eeeeee/standby/standby.php","data="+URLEncoder.encode(ActionHelper.actionToString(standbyAction.getAction(),true)));
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
