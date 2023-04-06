package esqeee.xieqing.com.eeeeee.user;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.HttpUtil;
import com.xieqing.codeutils.util.HttpUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import static esqeee.xieqing.com.eeeeee.bbs.BBS.header;

public class User {
    private static String host = "http://bbs.yicuba.com/public/index.php";
    private static User user;
    private String name = "Null";
    private String nick = "Null";
    private String email = "Null";
    private String head = host+"/head/0";
    private String token = "";
    private long vipTime=0;
    private int developer = 0;//开发者   1是，0不是
    private int collect = 0;//被关注数
    private int point = 0;
    private int grades = 1;
    private int sex = 0;
    private int status = 1;//验证,0表示被封,1表示正常,2邮箱验证,3手机认证,5手机邮箱全部认证
    private int uid = -1;

    public long getVipTime() {
        return vipTime;
    }

    private User(){
        this.uid = SPUtils.getInstance("user").getInt("uid",-1);
        this.token = SPUtils.getInstance("user").getString("token","");
        this.name = SPUtils.getInstance("user").getString("name","");
        this.nick = SPUtils.getInstance("user").getString("nick");
        this.email = SPUtils.getInstance("user").getString("email");
        this.point = SPUtils.getInstance("user").getInt("point",0);
        this.grades = SPUtils.getInstance("user").getInt("grades",0);
        this.sex = SPUtils.getInstance("user").getInt("sex",0);
        this.status = SPUtils.getInstance("user").getInt("status",1);
        this.developer = SPUtils.getInstance("user").getInt("developer",0);
        this.vipTime = SPUtils.getInstance("user").getLong("vipTime",0);

        EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginStateChanged(User user){
        new Thread(()->{refreshInfo();}).start();
    }


    static{
        user = getUser();
    }

    public int getPoint() {
        return point;
    }

    public int getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }

    public void refreshInfo() {
        String response = HttpUtils.Post(host+"/user/info","uid="+uid+"&token="+URLEncoder.encode(token),header());
        JSONBean jsonBean = new JSONBean(response);
        if (jsonBean.getInt("code",-1)!=0){
            RuntimeLog.i(jsonBean.getString("msg"));
            //loginOut();
            uid = -1;
            token = "";
        }else{
            jsonBean = jsonBean.getJson("data");
            setUser(jsonBean);
        }
    }

    public String getName() {
        return name;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        if (nick.equals("")){
            return name;
        }
        return nick;
    }

    public static User getUser() {
        if (user == null){
            user = new User();
        }
        return user;
    }

    public int getSex() {
        return sex;
    }

    public String getCapthaUrl(){
        return host+"/index/captha?t="+System.currentTimeMillis();
    }

    public String getHeadUrl(){
        return host+"/head/"+uid;
    }
    public String getLoginUrl(){
        return host+"/user/login";
    }
    public String getRegUrl(){
        return host+"/user/reg";
    }
    public String getPassUrl(){
        return host+"/user/editPass";
    }
    public String getSendEmailCodeUrl(){
        return host+"/user/sendEmail";
    }

    public void setUser(JSONBean data) {
        name = data.getString("userName");
        token = data.getString("token");
        email = data.getString("email");
        point = data.getInt("point");
        grades = data.getInt("grades");
        sex = data.getInt("sex");
        uid = data.getInt("uid");
        nick = data.getString("nickName");
        vipTime = data.getLong("vipTime");

        SPUtils.getInstance("user").put("name",name);
        SPUtils.getInstance("user").put("point",point);
        SPUtils.getInstance("user").put("grades",grades);
        SPUtils.getInstance("user").put("sex",sex);
        SPUtils.getInstance("user").put("uid",uid);
        SPUtils.getInstance("user").put("token",token);
        SPUtils.getInstance("user").put("nick",nick);
        SPUtils.getInstance("user").put("email",email);
        SPUtils.getInstance("user").put("vipTime",vipTime);


        CookieSyncManager.createInstance(Utils.getApp()).sync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(host,"uid="+uid+";");
        cookieManager.setCookie(host,"token="+token+";");
        CookieSyncManager.getInstance().sync();

    }

    public void loginOut() {
        this.uid = -1;
        this.token = "";
        this.nick = "Null";
        this.name = "Null";
        this.email = "NUll";
        this.head = host+"/head/0";
        this.token = "";
        this.developer = 0;//开发者   1是，0不是
        this.collect = 0;//被关注数
        this.point = 0;
        this.grades = 1;
        this.sex = 0;
        this.vipTime = 0;
        this.status = 1;//验证,0表示被封,1表示正常,2邮箱验证,3手机认证,5手机邮箱全部认证
        this.uid = -1;

        SPUtils.getInstance("user").clear(true);
        EventBus.getDefault().post(this);
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return false;
    }

    public interface UploadListener{
        void failed(String s);
        void progress(int progress);
        void success(String url);
    }
    public void uploadImage(String file,UploadListener listener) {
        File file1 = new File(file);
        if (!file1.exists()){
            listener.failed("文件不存在");
            return;
        }
        HttpUtil.getmOkHttpClient().newCall(new Request.Builder()
                .url(host+"/user/upload")
                .post(new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file",System.currentTimeMillis()+"."+FileUtils.getFileExtension(file1),new RequestBody() {
                            @Override
                            public MediaType contentType() {
                                return MediaType.parse("image/png") ;
                            }

                            @Override
                            public long contentLength() {
                                return file1.length();
                            }

                            @Override public void writeTo(BufferedSink sink) throws IOException {
                                Source source;
                                try {
                                    source = Okio.source(file1);
                                    Buffer buf = new Buffer();
                                    Long remaining = contentLength();
                                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                                        sink.write(buf, readCount);
                                        listener.progress((int) ((contentLength() - readCount) * 100 / (remaining - readCount)));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .addFormDataPart("uid",uid+"")
                        .addFormDataPart("token",token)
                        .build()
                ).build())
        .enqueue(new HttpUtil.HttpCall(){
            @Override
            public void onSuccess(String string) throws PackageManager.NameNotFoundException {
                super.onSuccess(string);
                listener.success(string);
            }

            @Override
            public void onError(int code, String string) {
                super.onError(code, string);
                listener.failed(code +" - "+string);
            }
        });
    }
}
