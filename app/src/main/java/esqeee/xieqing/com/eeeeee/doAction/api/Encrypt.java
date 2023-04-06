package esqeee.xieqing.com.eeeeee.doAction.api;

import android.Manifest;
import android.accessibilityservice.GestureDescription;
import android.bluetooth.BluetoothManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import android.util.Base64;
import android.view.inputmethod.EditorInfo;
import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.ConvertUtils;
import com.xieqing.codeutils.util.EncryptUtils;
import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.MathUtils;
import com.xieqing.codeutils.util.NoticUtil;
import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.QQUtils;
import com.xieqing.codeutils.util.Rc4Utils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.StringUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.xieqing.codeutils.util.TimeUtils;
import com.xieqing.codeutils.util.Utils;
import com.xieqing.codeutils.util.加密操作;
import com.yicu.yichujifa.GlobalContext;
import com.yicu.yichujifa.inputborad.InputBraod;
import com.yicu.yichujifa.ui.widget.FloatWebView;

import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.doAction.GboalNotification;
import esqeee.xieqing.com.eeeeee.doAction.core.Media;
import esqeee.xieqing.com.eeeeee.doAction.core.Text2Speech;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.ocr.SouGouOcr;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.WIFI_SERVICE;
import static com.xieqing.codeutils.util.加密操作.Authcode加密;
import static com.xieqing.codeutils.util.加密操作.Authcode解密;

public class Encrypt extends Base {
    public static Base intansce;

    public static Base getIntansce() {
        if (intansce == null) {
            intansce = new Encrypt();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param){
        String arg = param.getString("arg");
        String text = "";
        JSONBean var;
        switch (arg) {
            case "主机是否可连接":
                var = checkVarExiets(param,"var");
                boolean rechAble = false;
                try {
                    rechAble= InetAddress.getByName(getStringFromParam("url")).isReachable(3000);
                }catch (Exception e){}
                setValue(var,rechAble);
                break;
            case "取星期几":
                var = checkVarExiets(param,"var");
                Calendar date = new GregorianCalendar();
                date.setTimeInMillis(Long.parseLong(getStringFromParam("time")));
                setValue(var,date.get(Calendar.DAY_OF_WEEK));
                break;
            case "JSON解析":
                var = checkVarExiets(param,"var");
                int type = param.getInt("type");
                String json = getStringFromParam("json");
                String key = getStringFromParam("key");
                String item = getStringFromParam("class");
                String value = "";
                switch (type) {
                    case 1:
                        value = new JSONBean(json).getString(key);
                        break;
                    case 2:
                        value = new JSONBean(json).getJson(item).getString(key);
                        break;
                    case 3:
                        JSONArrayBean numberList = new JSONBean(json).getArray(item);
                        for (int i = 0; i < numberList.length(); i++) {
                            if (i == 0) {
                                value = numberList.getJson(i).getString(key);
                            } else {
                                value = value + "\n" + numberList.getJson(i).getString(key);
                            }
                        }
                        break;
                }
                setValue(var,value);
                break;
            case "获取最新短信":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                setValue(var,PhoneUtils.getSmsMessage());
                break;
            case "发送短信":
                String phone = getString(param.getString("phone"));
                String content = getString(param.getString("text"));
                if (ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    RuntimeLog.e("<发送短信>:没有发送短信权限");
                    return false;
                }
                PhoneUtils.sendSmsSilent(phone, content);
                break;
            case "取蓝牙状态":
            case "取WIFI状态":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                if (arg.equals("取WIFI状态")){
                    WifiManager wifiManager = (WifiManager) Utils.getApp().getApplicationContext().getSystemService(WIFI_SERVICE);
                    setValue(var,wifiManager.isWifiEnabled()?"真":"假");
                }else{
                    BluetoothManager bluetoothManager = (BluetoothManager)Utils.getApp().getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
                    setValue(var,bluetoothManager.getAdapter().isEnabled()?"真":"假");
                }
                break;
            case "MD5":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                text = getString(param.getString("text"));
                var.put("value", EncryptUtils.encryptMD5ToString(text));
                break;
            case "取时间戳":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                var.put("value", String.valueOf(param.getBoolean("isUseHm",false)?java.lang.System.currentTimeMillis():java.lang.System.currentTimeMillis()/1000));
                break;
            case "格式化时间戳":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                String longTime = getString(param.getString("time"));
                String format = getString(param.getString("text"));
                long timeValue = 0;
                try {
                    timeValue = Long.parseLong(longTime);
                }catch (Exception e){
                    RuntimeLog.e("<"+arg+">时间戳格式错误："+e.getMessage());
                    timeValue  = java.lang.System.currentTimeMillis();
                }
                var.put("value", TimeUtils.millis2String(timeValue,new SimpleDateFormat(format,Locale.getDefault())));
                break;
            case "数学运算":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                MathResult evalMath = evalMath(param.getString("text"));
                if (evalMath.isError()){
                    RuntimeLog.e("<数学运算>计算表达式有误："+evalMath.getException().getMessage());
                    return false;
                }
                var.put("value",String.valueOf(evalMath.getResult()));
                break;
            case "取随机数":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                MathResult min = evalMath(param.getString("min"));
                if (min.isError()){
                    RuntimeLog.e("<取随机数[最小值]>计算表达式有误："+min.getException().getMessage());
                    return false;
                }
                MathResult max = evalMath(param.getString("max"));
                if (min.isError()){
                    RuntimeLog.e("<取随机数[最大值]>计算表达式有误："+min.getException().getMessage());
                    return false;
                }
                var.put("value",MathUtils.random((int)min.getResult(),(int)max.getResult()));
                break;

            case "执行Shell":
                text = getString(param.getString("text"));
                log("<执行Shell>："+text);
                Shell.getShell().exce(text);
                break;
            case "发送文本到焦点编辑框":
                int sendType = param.getInt("type",0);
                text = getString(param.getString("text"));
                log("<发送文本到焦点编辑框>："+text);
                if (sendType == 0){
                    Shell.getShell().exce("input text '"+text+"'");
                }else{
                    Utils.getApp().sendBroadcast(new Intent(InputBraod.SEND_TEXT).putExtra(InputBraod.EXTRA_CONTENT,text));
                }
                break;
            case "变整数":
                var = checkVarExiets(param,"var");
                setValue(var,Math.round(Double.parseDouble(var.getString("value"))));
                break;
            case "URL编码":
            case "URL解码":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                text = getString(param.getString("text"));
                setValue(var,arg.equals("URL编码")?URLEncoder.encode(text):URLDecoder.decode(text));
                break;
            case "获取截图":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                Rect rect = getRect(param.getJson("rect"));
                Bitmap bitmap = getActionRun().getCaptruer().captrueScreen();
                bitmap = ImageUtils.cutBitmap(bitmap,rect);
                byte[] bytes = ConvertUtils.bitmap2Bytes(bitmap,Bitmap.CompressFormat.PNG);
                ImageUtils.recycle(bitmap);
                var.put("value",bytes);
                break;
            case "Ascll转Unicode":
                var = checkVarExiets(param,"var");
                setValue(var,StringUtils.AscllToUnicode(getString(param.getString("text"))));
                break;
            case "Unicode转Ascll":
                var = checkVarExiets(param,"var");
                setValue(var,StringUtils.UnicodeToAscll(getString(param.getString("text"))));
                break;
            case "获取屏幕颜色":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                MathResult x = evalMath(param.getString("x"));
                if (x.isError()){
                    RuntimeLog.e("<error[正在计算横坐标]>计算表达式有误："+x.getException().getMessage());
                    return false;
                }
                MathResult y = evalMath(param.getString("y"));
                if (y.isError()){
                    RuntimeLog.e("<error[正在计算纵坐标]>计算表达式有误："+x.getException().getMessage());
                    return false;
                }
                bitmap = getActionRun().getCaptruer().captrueScreen();
                if (x.getResult() < 0 || x.getResult()>bitmap.getWidth()){RuntimeLog.e("横坐标必须大于0，小于屏幕宽度");return false;}
                if (y.getResult() < 0 || y.getResult()>bitmap.getHeight()){RuntimeLog.e("纵坐标必须大于0，小于屏幕高度");return false;}

                int pixel = bitmap.getPixel(x.getResult(),y.getResult());
                int red = android.graphics.Color.red(pixel);
                int blue = android.graphics.Color.blue(pixel);
                int green = android.graphics.Color.green(pixel);
                int alpha = Color.alpha(pixel);

                var.put("value","#"+(toHex(alpha)+toHex(red)+toHex(green)+toHex(blue)).toUpperCase());
                break;
            case "获取文字":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                rect = getRect(param.getJson("rect"));
                setValue(var, SouGouOcr.scanText(ImageUtils.cutBitmap(getActionRun().getCaptruer().captrueScreen(),rect)));
                break;
            case "获取文字2":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                rect = getRect(param.getJson("rect"));
                setValue(var, AccessbilityUtils.findNodesByRectToString(rect));
                break;
            case "多指点击":
                if (Build.VERSION.SDK_INT>=24){
                    JSONArrayBean touchs = param.getArray("touchs");
                    if (touchs == null){
                        return true;
                    }
                    GestureDescription.StrokeDescription[] strokeDescriptions = new GestureDescription.StrokeDescription[touchs.length()];
                    for (int i = 0;i<touchs.length();i++){
                        JSONBean jsonBean = touchs.getJson(i);
                        if (jsonBean == null){
                            RuntimeLog.e("[error]:多指点击，位置为空！");
                            return false;
                        }
                        x = evalMath(jsonBean.getString("x"));
                        if (x.isError()){
                            RuntimeLog.e("<error[正在计算横坐标]>计算表达式有误："+x.getException().getMessage());
                            return false;
                        }
                        y = evalMath(jsonBean.getString("y"));
                        if (y.isError()){
                            RuntimeLog.e("<error[正在计算纵坐标]>计算表达式有误："+y.getException().getMessage());
                            return false;
                        }

                        int xValue = x.getResult();
                        int yValue = y.getResult();
                        if (xValue < 0) {
                            yValue = 0;
                        }
                        if (xValue > ScreenUtils.getScreenWidth()) {
                            xValue = ScreenUtils.getScreenWidth();
                        }
                        if (yValue > ScreenUtils.getScreenHeight()) {
                            yValue = ScreenUtils.getScreenHeight();
                        }
                        if (yValue < 0) {
                            yValue = 0;
                        }

                        Path path = new Path();
                        path.moveTo(xValue, yValue);
                        strokeDescriptions[i] = new GestureDescription.StrokeDescription(path, 0, 50);
                    }
                    AccessbilityUtils.touch(strokeDescriptions);
                }
                break;
            case "多指长按":
                if (Build.VERSION.SDK_INT>=24){
                    JSONArrayBean touchs = param.getArray("touchs");
                    if (touchs == null){
                        return true;
                    }
                    GestureDescription.StrokeDescription[] strokeDescriptions = new GestureDescription.StrokeDescription[touchs.length()];
                    for (int i = 0;i<touchs.length();i++){
                        JSONBean jsonBean = touchs.getJson(i);
                        if (jsonBean == null){
                            RuntimeLog.e("[error]:多指长按，位置为空！");
                            return false;
                        }
                        x = evalMath(jsonBean.getString("x"));
                        if (x.isError()){
                            RuntimeLog.e("<error[正在计算横坐标]>计算表达式有误："+x.getException().getMessage());
                            return false;
                        }
                        y = evalMath(jsonBean.getString("y"));
                        if (y.isError()){
                            RuntimeLog.e("<error[正在计算纵坐标]>计算表达式有误："+y.getException().getMessage());
                            return false;
                        }
                        MathResult time = evalMath(jsonBean.getString("time"));
                        if (time.isError()){
                            RuntimeLog.e("<error[正在计算按住时间]>计算表达式有误："+time.getException().getMessage());
                            return false;
                        }

                        int xValue = x.getResult();
                        int yValue = y.getResult();
                        int tValue = time.getResult();
                        if (xValue < 0) {
                            yValue = 0;
                        }
                        if (xValue > ScreenUtils.getScreenWidth()) {
                            xValue = ScreenUtils.getScreenWidth();
                        }
                        if (yValue > ScreenUtils.getScreenHeight()) {
                            yValue = ScreenUtils.getScreenHeight();
                        }
                        if (yValue < 0) {
                            yValue = 0;
                        }

                        Path path = new Path();
                        path.moveTo(xValue, yValue);
                        strokeDescriptions[i] = new GestureDescription.StrokeDescription(path, 0, tValue);
                    }
                    AccessbilityUtils.touch(strokeDescriptions);
                }
                break;
            case "Base64编码":
            case "Base64解码":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }

                text = param.getString("text");
                if (text.startsWith("{") && text.endsWith("}") && text.length() >= 3 && arg.equals("Base64编码")){
                    text = text.substring(1,text.length() - 1);
                    //log(text);
                    JSONBean v = queryVariable(text);
                    if (v == null){
                        RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                        return false;
                    }


                    Object object = v.get("value");
                    if (object instanceof byte[]){
                        byte[] base = (byte[]) object;
                        setValue(var,Base64.encodeToString(base,0));
                    }else{
                        String base = String.valueOf(object);
                        setValue(var,Base64.encodeToString(base.getBytes(),0));
                    }
                    return true;
                }
                text = getString(text);
                setValue(var,arg.equals("Base64编码")?Base64.encodeToString(text.getBytes(),0):new String(Base64.decode(text,0)));
                break;
            case "设置剪贴板文本":
                ThreadUtils.runOnUiThread(()->{
                    ((ClipboardManager) Utils.getApp().getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("一触即发--剪贴板", getString(param.getString("text"))));
                });
                break;
            case "调试输出日志":
                text = getString(param.getString("text"));
                RuntimeLog.log(text);
                break;
            case "获取剪贴板文本":
                JSONBean v = queryVariable(param.getString("var"));
                if (v == null){
                    RuntimeLog.e("<"+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    getActionRun().stopDo();
                }
                lock();
                ThreadUtils.runOnUiThread(()->{
                    setValue(v,((ClipboardManager) Utils.getApp().getSystemService(CLIPBOARD_SERVICE)).getText());
                    unlock();
                });
                waitForUnlock();
                break;
            case "语音播放":
                text = getString(param.getString("text"));
                String finalText = text;
                Text2Speech speech =getActionRun().getRunDo().getSpeech();
                while (!speech.isInited()){
                    ThreadUtils.sleep(50);
                }
                speech.speek(finalText);
                while (speech.isSpeeking()){
                    ThreadUtils.sleep(50);
                }
                break;
            case "语音识别":
                /*var = checkVarExiets(param,"var");
                lock();
                ThreadUtils.runOnUiThread(()->{
                    if (!XunFei.isInstallLocalService()){
                        new FDialog(GboalContext.getContext())
                                .setTitle("缺少资源文件")
                                .setMessage("是否下载语音识别合成所需要的资源文件，这可能会消耗您20M左右的流量")
                                .setCannel("取消",view -> {
                                    setValue(var,"");
                                    unlock();
                                }).setDismissListener((C)->{
                                    setValue(var,"");
                                    unlock();
                                }).setCanfirm("确定",view -> {
                                    XunFei.inStallLocalService(()->{
                                        setValue(var,"");
                                        unlock();
                                    });
                                }).show();
                    }else{
                        XunFei.startRecognizer(new RecognizerDialogListener() {
                            @Override
                            public void onResult(RecognizerResult recognizerResult, boolean b) {
                                setValue(var, JsonParser.parseIatResult(recognizerResult.getResultString()));
                                unlock();
                            }

                            @Override
                            public void onError(SpeechError speechError) {
                                setValue(var,speechError.toString());
                                unlock();
                            }
                        });
                    }
                });
                waitForUnlock();*/
                break;
            case "取通知栏信息":
                JSONBean sender = queryVariable(param.getString("sender"));
                JSONBean mes = queryVariable(param.getString("mes"));
                GboalNotification.Notification notification = GboalNotification.getIntansce().last();
                if (notification == null &&
                        !NoticUtil.isNotificationEnabled2(getActionRun().getContext())){
                    RuntimeLog.e("没有获取通知栏信息权限!");
                    return false;
                }
                if (notification == null){
                    RuntimeLog.log("没有通知栏信息");
                    return true;
                }
                setValue(sender, notification.sender);
                setValue(mes,notification.content);
                break;
            case "打开应用":
                text = getString(param.getString("text"));
                AppUtils.launchApp(text);
                break;
            case "创建矩型":
                var = checkVarExiets(param,"var");
                var.put("value",new JSONBean()
                        .put("left",getMathResultOrThrow(param,"left"))
                        .put("top",getMathResultOrThrow(param,"top"))
                        .put("width",getMathResultOrThrow(param,"width"))
                        .put("height",getMathResultOrThrow(param,"height")));
                break;
            case "显示网页":
                ThreadUtils.runOnUiThread(()->{
                    FloatWebView.getInstance().show(getString(param.getString("text")));
                });
                break;
            case "保存变量":
                SPUtils.getInstance("auto_var_table").put(getString(param.getString("name")),getString(param.getString("value")));
                break;
            case "读取变量":
                var = checkVarExiets(param,"var");
                setValue(var,SPUtils.getInstance("auto_var_table").getString(getString(param.getString("name"))));
                break;
            case "播放音乐":
                Media media = getActionRun().getRunDo().getMedia();
                media.setVolume(param.getInt("volume",80));
                media.setLooping(param.getBoolean("for",false));
                media.play(getString(param.getString("text")));
                break;
            case "停止播放音乐":
                media = getActionRun().getRunDo().getMedia();
                media.stop();
                break;

            case "更新图片到相册":
                String path = getString(param.getString("text"));
                getActionRun().getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + path)));
                break;
            case "执行输入法动作":
                sendType = param.getInt("type",0);
                int action = EditorInfo.IME_ACTION_DONE;
                if (sendType == 0){
                    action = EditorInfo.IME_ACTION_DONE;
                }else if (sendType == 1){
                    action = EditorInfo.IME_ACTION_GO;
                }else if (sendType == 2){
                    action = EditorInfo.IME_ACTION_SEARCH;
                }else if (sendType == 3){
                    action = EditorInfo.IME_ACTION_SEND;
                }else if (sendType == 4){
                    action = EditorInfo.IME_ACTION_NEXT;
                }else if (sendType == 5){
                    action = EditorInfo.IME_ACTION_PREVIOUS;
                }
                Utils.getApp().sendBroadcast(new Intent(InputBraod.SEND_ACTION).putExtra(InputBraod.EXTRA_ACTION,action));
                break;
            case "DES加密":
                var = checkVarExiets(param,"var");
                text = getStringFromParam("text");
                String pass = getStringFromParam("pass");
                setValue(var,加密操作.DES加密(text,pass));
                break;
            case "DES解密":
                var = checkVarExiets(param,"var");
                text = getStringFromParam("text");
                pass = getStringFromParam("pass");
                setValue(var, 加密操作.DES解密(text,pass));
                break;
            case "RC4加密":
                var = checkVarExiets(param,"var");
                text = getStringFromParam("text");
                pass = getStringFromParam("pass");
                setValue(var, Rc4Utils.encrypt(text,pass));
                break;
            case "RC4解密":
                var = checkVarExiets(param,"var");
                text = getStringFromParam("text");
                pass = getStringFromParam("pass");
                setValue(var, Rc4Utils.decrypt(text,pass));
                break;
            case "Authcode加密":
                var = checkVarExiets(param,"var");
                text = getStringFromParam("text");
                pass = getStringFromParam("pass");
                setValue(var, Authcode加密(text,pass));
                break;
            case "Authcode解密":
                var = checkVarExiets(param,"var");
                text = getStringFromParam("text");
                pass = getStringFromParam("pass");
                setValue(var, Authcode解密(text,pass));
                break;
            case "临时QQ会话":
                text = getStringFromParam("text");
                QQUtils.临时QQ会话(GlobalContext.getContext(),text);
                break;
            case "打开QQ加群":
                text = getStringFromParam("text");
                QQUtils.加入QQ群(GlobalContext.getContext(),text);
                break;
            case "打开QQ资料卡":
                text = getStringFromParam("text");
                QQUtils.显示资料卡(GlobalContext.getContext(),text);
                break;
            case "取QQ昵称":
                var = checkVarExiets(param,"var");
                text = getStringFromParam("text");
                setValue(var, QQUtils.取QQ昵称(text));
                break;
            case "取QQ头像":
                var = checkVarExiets(param,"var");
                text = getStringFromParam("text");
                setValue(var, QQUtils.取QQ头像(text));
                break;
        }
        return true;
    }
    public String toHex(int i){
        String s = Integer.toHexString(i);
        if (s.length() == 1){
            s="0"+s;
        }
        return s.toUpperCase();
    }
    private boolean getStringCenter(JSONBean param) {
        JSONBean var = queryVariable(param.getString("var"));
        if (var == null){
            log("<文本操作：取文本中间>:错误，找不到变量["+param.getString("var")+"]");
            return false;
        }
        String text = getString(param.getString("text"));
        String left = getString(param.getString("left"));
        String right = getString(param.getString("right"));
        var.put("value", StringUtils.getSubString(text,left,right));
        return true;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public String getName() {
        return "文件操作";
    }
}
