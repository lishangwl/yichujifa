package esqeee.xieqing.com.eeeeee.bbs;

import androidx.annotation.Nullable;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xieqing.codeutils.util.DeviceUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.HttpUtil;
import com.xieqing.codeutils.util.HttpUtils;
import com.xieqing.codeutils.util.ThreadUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.user.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class BBS {
    public static final String host = "http://bbs.yicuba.com/public/index.php/";

    public static void getAcrtle(int tid,int page, CallBack callBack){
        get(host+"page/"+tid+"/"+page,callBack);
    }

    public static void searchAcrtle(String key,int page, CallBack callBack){
        get(host+"search/acrtle/search?keyword="+URLEncoder.encode(key)+"&page="+page,callBack);
    }
    public static void useCard(String card, CallBack callBack){
        post(host+"user/index/useCard","card="+card+"&uid="+User.getUser().getUid()+"&token="+URLEncoder.encode(User.getUser().getToken()),callBack);
    }
    public static void hackPay(String money, CallBack callBack){
        post(host+"user/index/hackPay","money="+money+"&uid="+User.getUser().getUid()+"&token="+URLEncoder.encode(User.getUser().getToken()),callBack);
    }
    public static void getUserAcrtle(int uid,int page,User user, CallBack callBack){
        get(host+"user/acrtle/page/"+uid+"/"+page+"?uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }
    public static void getUserCollectAcrtle(int uid,int page,User user, CallBack callBack){
        get(host+"user/acrtle/collect/page/"+uid+"/"+page+"?uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }

    public static void getAcrtleDetail(User user,int aid, CallBack callBack){
        //get(host+"acrtle/"+aid+(user.isLogin()?"?uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()):""),callBack);
    }

    public static void get(String url,CallBack callBack){
        ThreadUtils.executeByCpu(new NetTask<CallResult>(){
            @Nullable
            @Override
            public CallResult doInBackground() throws Throwable {
                long time = System.currentTimeMillis()/1000;
                HttpUtils.Response response = new HttpUtils.Request()
                        .get()
                        .header(header())
                        .url(url)
                        .exec();
                if (response.code != 200){
                    return new CallResult("{\"code\":-1,\"msg\":\""+response.message()+"\"}");
                }
                return new CallResult(response.body().string());
            }

            @Override
            public void onSuccess(@Nullable CallResult result) {
                callBack.callBack(result);
            }
        });
    }


    public static void attachIcon(ImageView icon, int uid) {
        Glide.with(icon.getContext()).load(host+"head/"+uid)
                .into(icon);
    }

    public static void attachIconNoCashe(ImageView icon, int uid) {
        Glide.with(icon.getContext()).load(host+"head/"+uid).skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .into(icon);
    }

    public static void commit(String content, CallBack callBack) {
        post(host+"acrtle/commit","content="+content,callBack);
    }

    public static void report(Action action,int type,long time,CallBack callBack) {
        File file = new File(action.getPath());
        if (!file.exists()){
            return;
        }
        JSONBean content = new JSONBean();
        content.put("name",action.getTitle());
        content.put("path",action.getPath());
        content.put("device",DeviceUtils.getMessage());
        content.put("deviceid", DeviceUtils.getAndroidID());
        content.put("size",file.length());
        content.put("md5", FileUtils.getFileMD5ToString(file));
        content.put("type", type);
        content.put("doTime", time);
        content.put("createtime",file.lastModified());
        post(host+"report","data="+URLEncoder.encode(Base64.encodeToString(content.toString().getBytes(),2)),callBack);
    }

    public static void zan(User user, int aid,CallBack callBack) {
        post(host+"acrtle/zan","aid="+aid+"&uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }

    public static void collect(User user, int aid,CallBack callBack) {
        post(host+"acrtle/collect","aid="+aid+"&uid="+user.getUid()+"&token="+ URLEncoder.encode(user.getToken()),callBack);
    }

    public static void reply(int aid,int atUid, User user, String nr, String image, CallBack callBack) {
        JSONBean content = new JSONBean();
        content.put("content",nr);
        content.put("image",image);
        content.put("aid",aid);
        content.put("device", DeviceUtils.getMessage());
        content.put("uid",user.getUid());
        content.put("atUid",atUid);
        content.put("token",user.getToken());
        post(host+"acrtle/reply","content="+URLEncoder.encode(content.toString()),callBack);
    }

    public static void getAcrtleReply(int aid, int page,User user, CallBack callBack) {
        get(host+"reply/page/"+aid+"/"+page+"?uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }

    public static void uploadScript(String actionId, String name, User user, CallBack callBack) {
        ThreadUtils.executeByCpu(new NetTask<CallResult>(){
            @Nullable
            @Override
            public CallResult doInBackground(){
                File file1 = new File(actionId);
                if (!file1.exists()){
                    return new CallResult("{\"code\":-1,\"msg\":\"文件不存在！\"}");
                }
                byte[] bytes = ActionHelper.actionToString(ActionHelper.from(actionId),true).getBytes();
                try {
                    Response response = HttpUtil.getmOkHttpClient().newCall(new Request.Builder()
                            .url(host+"/user/upload")
                            .post(new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("file",name,new RequestBody() {
                                        @Override
                                        public MediaType contentType() {
                                            return MediaType.parse("script/ycf") ;
                                        }

                                        @Override
                                        public long contentLength() {
                                            return bytes.length;
                                        }

                                        @Override
                                        public void writeTo(BufferedSink sink) throws IOException {
                                            sink.write(bytes);
                                        }
                                    })
                                    .addFormDataPart("uid",user.getUid()+"")
                                    .addFormDataPart("token",user.getToken())
                                    .build()
                            ).build()).execute();
                    if (response.code() != 200){
                        return new CallResult("{\"code\":-2,\"msg\":\""+response.message()+"\"}");
                    }
                    return new CallResult(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                    return new CallResult("{\"code\":-2,\"msg\":\""+e.getMessage()+"\"}");
                }
            }

            @Override
            public void onSuccess(@Nullable CallResult result) {
                if (callBack!=null){
                    callBack.callBack(result);
                }
            }
        });
    }

    public static void post(String url,String data,CallBack callBack){
        ThreadUtils.executeByCpu(new NetTask<CallResult>(){
            @Nullable
            @Override
            public CallResult doInBackground(){
                try {
                    HttpUtils.Response response = new HttpUtils.Request()
                            .post()
                            .url(url)
                            .header(header())
                            .contentString(data)
                            .exec();
                    if (response.code != 200){
                        return new CallResult("{\"code\":-2,\"msg\":\""+response.message()+"\"}");
                    }
                    return new CallResult(response.body().string());
                }catch (Exception e){
                    return new CallResult("{\"code\":-2,\"msg\":\""+e.getMessage()+"\"}");
                }
            }

            @Override
            public void onSuccess(@Nullable CallResult result) {
                if (callBack!=null){
                    callBack.callBack(result);
                }
            }
        });
    }

    public static void getUserFile(User user, int id, CallBack callBack) {
        get(host+"user/file/"+id+"?uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }

    public static void getCates(CallBack callBack) {
        get(host+"acrtle/cates",callBack);
    }

    public static Map<String,String> header(){
        Map<String,String> header = new HashMap<>();
        long time = System.currentTimeMillis()/1000;
        //System.err.println(time + Encrypt.decrypt("嶌騎侀聗依頮嗬孙梓婶佭劒瞴惫楘佒惧唔彳郉佐俍掄翌劃堢ￌ倅埠恫棘眜凪凰め总梨盬妎亅冪诸亦恳庣迹亀押缄冻坚"));
        header.put(Encrypt.decrypt("}}qt"),String.valueOf(time));
        header.put(Encrypt.decrypt("\u0086wqs"),Encrypt.md5(time + Encrypt.decrypt("嶌騎侀聗依頮嗬孙梓婶佭劒瞴惫楘佒惧唔彳郉佐俍掄翌劃堢ￌ倅埠恫棘眜凪凰め总梨盬妎亅冪诸亦恳庣迹亀押缄冻坚")));
        return header;
    }

    public static void editUserHead(User user, String url, CallBack callBack) {
        post(host+"user/edit/head","param="+URLEncoder.encode(url)+"&uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }

    public static void editUserNick(User user, String nick, CallBack callBack) {
        post(host+"user/edit/nick","param="+URLEncoder.encode(nick)+"&uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }

    public static void editUserPass(User user, String pass, CallBack callBack) {
        post(host+"user/edit/pass","param="+URLEncoder.encode(pass)+"&uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }

    public static void verify(int aid, boolean b, User user, CallBack callBack) {
        post(host+"acrtle/admin/"+(b?"verify_success":"verify_error"),"aid="+aid+"&uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }

    public static void choice(int aid, boolean b, User user, CallBack callBack) {
        post(host+"acrtle/admin/"+(b?"choice_success":"choice_error"),"aid="+aid+"&uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }

    public static void deleteAcrtle(int aid,User user, CallBack callBack) {
        post(host+"acrtle/admin/delete","aid="+aid+"&uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }

    public static void deleteReply(int aid,int pid,User user, CallBack callBack) {
        post(host+"acrtle/admin/deleteReply","aid="+aid+"&pid="+pid+"&uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }

    public static void status(int uid, boolean b, User user, CallBack callBack) {
        post(host+"user/admin/"+(b?"whiteroom":"blackroom"),"uid2="+uid+"&uid="+user.getUid()+"&token="+URLEncoder.encode(user.getToken()),callBack);
    }
}



