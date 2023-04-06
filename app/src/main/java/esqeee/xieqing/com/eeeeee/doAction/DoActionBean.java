package esqeee.xieqing.com.eeeeee.doAction;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.xieqing.codeutils.util.IntentUtils;
import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionUtils;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.SyStemUtil;

public enum DoActionBean {
    OPEN_FLASH(0,"开手电筒", R.mipmap.ic_flash_on),
    CLOSE_FLASH(1,"关手电筒", R.mipmap.ic_flash_off),
    CLICK_SCREEN(2,"点击此处",R.mipmap.ic_doaction),
    CLICK_SCREEN_TEXT(3,"点击屏幕文字",R.drawable.click_word),
    KEY_BACK(4,"返回键",R.mipmap.ic_action_back),
    KEY_HOME(5,"主页键",R.drawable.ic_action_home),
    KEY_TASK(6,"任务键",R.mipmap.ic_action_task),
    SWIP_LEFT(7,"左滑",R.drawable.ic_action_left),
    SWIP_RIGHT(8,"右滑",R.drawable.ic_action_right),
    SWIP_TOP(9,"上滑",R.drawable.ic_action_top),
    SWIP_BOTTOM(10,"下滑",R.drawable.ic_action_bottom),
    SYSTEM_WKKEUP(11,"唤醒屏幕",R.mipmap.ic_action_wakeup),
    LUACH_APP(12,"打开指定应用"),
    SWIP(13,"模拟滑动起点 - 终点",R.mipmap.ic_action_swip),
    KEY_LONG_POWER(14,"长按电源键",R.mipmap.ic_action_power),
    INPUT_TEXT(15,"在此输入文字",R.drawable.input),
    LONG_CIICK(16,"长按此处",R.drawable.longclick),
    EXCE_ACTION(17,"执行某个自动化",R.mipmap.ic_action_exce),
    SYS_OPEN_WIFI(18,"打开WIFI",R.mipmap.ic_wifi_on),
    SYS_CLOSE_WIFI(19,"关闭WIFI",R.mipmap.ic_wifi_off),
    SYS_OPEN_BLUETOOTH(20,"打开蓝牙",R.mipmap.ic_ble_on),
    SYS_CLOSE_BLUETOOTH(21,"关闭蓝牙",R.mipmap.ic_ble_off),
    SYS_WIFISETTING(22,"WIFI设置",R.mipmap.ic_wifi_icon),
    SYS_STROGE(23,"存储空间",R.mipmap.ic_sd_icon),
    SYS_APPMANGER(24,"应用管理",R.mipmap.ic_app_icon),
    SYS_VIBRATE(25,"静音模式",R.mipmap.ic_volum_off),
    SYS_NOT_VIBRATE(26,"振动模式",R.mipmap.ic_volum_on),
    SYS_SETTING(27,"系统设置",R.mipmap.ic_setting_icon),
    SYS_OPEN_NOTIC(28,"下拉通知栏",R.drawable.ic_notifaction_on),
    SYS_CLOSE_NOTIC(29,"折叠通知栏",R.drawable.ic_notifaction_off),
    CLICK_IMAGE(30,"识图点击",R.drawable.shitu_old_press),
    STOP(31,"停止自动化",R.drawable.shop),
    CODER_CLICK(32,"单击控件"),
    CODER_LONG_CLICK(33,"长按控件"),
    LNK_WECHAT_SCAN(34,"微信扫一扫",R.drawable.ic_wechat_scan),
    LNK_ALIPAY_SCAN(35,"支付宝扫一扫",R.drawable.ic_alipay_scan),
    LNK_ALIPAY_GETMONEY(36,"支付宝收钱",R.drawable.ic_alipay_pay),
    LNK_ALIPAY_SENDMONEY(37,"支付宝付钱",R.drawable.ic_alipay_pay),
    LNK_TELEPHONE(38,"一键拨号",R.drawable.ic_lnk_call),
    LNK_LINK(39,"打开指定URL",R.drawable.ic_lnk_link),
    LNK_QQ(40,"指定QQ聊天",R.drawable.ic_lnk_qq),
    LONGCLICK_SCREEN_TEXT(41,"长按屏幕文字",R.drawable.click_word),
    GESTRUE(42,"执行手势",R.drawable.gestrue),
    IF(43,"条件判断",R.drawable.ic_action_if),
    CONDITION_SCREEN_TEXT(44,"是否包含文字（全屏）"),
    CONDITION_RECT_TEXT(45,"是否包含文字（区域）"),
    PASTE_TEXT(46,"在此粘贴输入",R.drawable.zhantie),
    CONDITION_SCREEN_IMAGE(47,"是否包含图片(全屏)"),
    CONDITION_RECT_IMAGE(48,"是否包含图片(区域)"),
    CLICK_TEXT_RECT(49,"点击屏幕文字(区域)",R.drawable.click_word),
    CLICK_IMAGE_RECT(50,"识图点击(区域)",R.drawable.shitu_old_press),
    RANDOM_SLEEP(51,"随机延时",R.drawable.suiji),
    WHILE(52,"条件循环",R.drawable.ic_action_while),
    FOR(53,"计次循环",R.drawable.ic_action_for),
    CONDITION_COLOR(54,"是否包含颜色(全屏)"),
    TOAST(55,"弹出提示",R.drawable.tishi),
    DESC(56,"注释",R.drawable.zhushi),
    SYS_CLOSE_SCREEN(57,"息屏",R.mipmap.ic_sys_lockscreen),
    CLICK_COLOR(58,"找色点击",R.drawable.zhaose),
    CLICK_RECT_COLOR(59,"找色点击(区域)",R.drawable.zhaose),
    LONG_CLICK_IMAGE(60,"识图长按",R.drawable.shitu_old_press),
    LONG_CLICK_IMAGE_RECT(61,"识图长按(区域)",R.drawable.shitu_old_press),
    LONGCLICK_SCREEN_TEXT_RECT(62,"长按屏幕文字(区域)",R.drawable.click_word),
    CONDITION_COLOR_RECT(63,"是否包含颜色(区域)"),
    VBRITE(64,"震动(短)",R.mipmap.ic_vibrate),
    VBRITE_LONG(65,"震动(长)",R.mipmap.ic_vibrate),
    NOTICAFACTION(66,"铃声提示",R.drawable.tishi),
    LONGCLICK_COLOR(67,"找色长按",R.drawable.zhaose),
    LONG_RECT_COLOR(68,"找色长按(区域)",R.drawable.zhaose),
    STOP_ALL(69,"停止全部自动化",R.drawable.shop),
    PRO_HTTP(70,"访问网页"),
    PRO_ASSGIN(71,"赋值变量"),
    CONDITION_VAR(72,"变量比较"),
    STRING_ACTION(73,"文本操作"),
    FILE_ACTION(74,"文件操作"),
    ENCRPTY_ACTION(75,"加密操作"),
    BREAK(76,"跳出循环",R.drawable.shop),
    SWIP_LINE(77,"直线滑动",R.mipmap.ic_action_swip),
    ARRAY_ACTION(78,"数组操作"),
    NODE_ACTION(79,"控件操作"),
    DIALOG_ACTION(80,"对话框操作"),
    UI_ACTION(81,"界面操作"),
    RETURN(82,"退出",R.drawable.shop),
    SYSTEM_ACTION(83,"系统操作");

    private int actionType;
    private String actionName;
    private Drawable drawable;
    private String param;
    DoActionBean(int actionType,String actionName){
        this.actionType=actionType;
        this.actionName = actionName;
    }
    DoActionBean(int actionType,String actionName,int drawable){
        this.actionType=actionType;
        this.actionName = actionName;
        try {
            this.drawable = Utils.getApp().getResources().getDrawable(drawable);
            //无语，这里竟然有人oom
        }catch (Exception e){
            RuntimeLog.log(e);
        }
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public int getActionType() {
        return actionType;
    }

    public String getActionName() {
        return actionName;
    }

    public static DoActionBean getBeanFromType(int type){
        return values()[type];
    }




    public synchronized static void postAction(int type, Context context, @Nullable String param){
        switch (type){
            case 0:
                PhoneUtils.openFlash();
                RuntimeLog.log("打开闪光灯");
                break;
            case 1:
                PhoneUtils.closeFlash();
                RuntimeLog.log("关闭闪光灯");
                break;
            case 18:
                SyStemUtil.controlWifi(context, true);
                RuntimeLog.log("打开wifi");
                break;
            case 19:
                SyStemUtil.controlWifi(context, false);
                RuntimeLog.log("关闭wifi");
                break;
            case 20:
                SyStemUtil.controlBlueTooth(context, true);
                RuntimeLog.log("打开蓝牙");
                break;
            case 21:
                SyStemUtil.controlBlueTooth(context, false);
                RuntimeLog.log("关闭蓝牙");
                break;
            case 22:
                SyStemUtil.wifiSeting(context);
                RuntimeLog.log("进入Wifi设置");
                break;
            case 23:
                SyStemUtil.memory(context);
                RuntimeLog.log("进入内存管理");
                break;
            case 57:
                SyStemUtil.screenOff();
                //PhoneUtils.screenOff();
                RuntimeLog.log("息屏");
                break;
            case 24:
                SyStemUtil.appList(context);
                RuntimeLog.log("进入应用管理");
                break;
            case 25:
                SyStemUtil.noRingAndVibrate(context);
                RuntimeLog.log("静音模式");
                break;
            case 26:
                SyStemUtil.vibrate(context);
                RuntimeLog.log("振动模式");
                break;
            case 27:
                SyStemUtil.setting(context);
                RuntimeLog.log("进入系统设置");
                break;
            case 28:
                SyStemUtil.expandNotification(context);
                RuntimeLog.log("下拉通知栏");
                break;
            case 29:
                SyStemUtil.closeNotification(context);
                RuntimeLog.log("收起通知栏");
                break;
            case 34:
                ActionUtils.toWeChatScanDirect(context);
                break;
            case 35:
                ActionUtils.toAliPay(context, 10000007);
                break;
            case 36:
                ActionUtils.toAliPay(context, 20000123);
                break;
            case 37:
                ActionUtils.toAliPay(context, 20000056);
                break;
            case 38:
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    PhoneUtils.call(param);
                    RuntimeLog.log("开始拨打电话-》"+param);
                }
                break;
            case 39:
                try {
                    context.startActivity(IntentUtils.getUrlIntent(param));
                }catch (ActivityNotFoundException ex){
                    RuntimeLog.log(ex);
                }
                RuntimeLog.log("打开链接-》"+param);
                break;
            case 40:
                ActionUtils.toMobileQQChat(context,param);
                break;
            case 64:
                RuntimeLog.log("震动");
                PhoneUtils.vibrateShort();
                break;
            case 65:
                RuntimeLog.log("震动");
                PhoneUtils.vibrate();
                break;
            case 66:
                RuntimeLog.log("铃声提示");
                //MediaUtils.playMusicFromAssets("wav/4085.wav");
                break;
        }
    }


}
