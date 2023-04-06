package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.xieqing.codeutils.util.ToastUtils;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.fragment.PramaerFragment;

public class EncryptHolder extends BaseHolder{

    public EncryptHolder(Context context, MyAdapter adapter) {
        super(context,-1,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(false);
        JSONBean param = jsonBean.getJson("param");
        String arg = param.getString("arg");
        setHead(arg,R.drawable.icon_56);

        setTooltips(null);
        switch (arg){
            case "主机是否可连接":
                setContentXML(R.xml.http_ping);
                break;
            case "变整数":
                setContentXML(R.xml.intval);
                break;
            case "获取最新短信":
                setContentXML(R.xml.get_sms);
                break;
            case "发送短信":
                setContentXML(R.xml.sendsms);
                break;
            case "多指点击":
                setContentXML(R.xml.touch_more_click);
                break;
            case "多指长按":
                setContentXML(R.xml.touch_more_longclick);
                break;
            case "取蓝牙状态":
            case "取WIFI状态":
                setTooltips("返回开关状态 ，真 或 假");
                setContentXML(R.xml.get_wifi_state);
                break;
            case "MD5":
                setTooltips("将一段文本转换为md5值");
                setContentXML(R.xml.string_md5);
                break;
            case "格式化时间戳":
                setContentXML(R.xml.string_format_time);
                break;
            case "播放音乐":
                setTooltips("音乐会随着脚本的停止而停止，请适当加延迟处理");
                setContentXML(R.xml.media_play);
                break;
            case "停止播放音乐":
                setContentXML(-1);
                break;
            case "取星期几":
                setTooltips("返回一个值为 1 到 7 之间的整数，表示指定时间中的星期几，注意：星期日为第一天。");
                setContentXML(R.xml.string_format_time_day);
                break;
            case "取时间戳":
                setTooltips("返回现行时间戳文本，即1970年1月1日到现在的毫秒数或秒数，类型： 13位毫秒数、10位秒数。");
                setContentXML(R.xml.string_timestamp);
                break;
            case "取随机数":
                setContentXML(R.xml.int_random);
                break;
            case "数学运算":
                setTooltips("四则数学运算，级加减乘除，例如  5 + 100 - 6 * (80/4)");
                setContentXML(R.xml.int_math);
                break;
            case "执行Shell":
            case "调试输出日志":
                if (arg.equals("调试输出日志")){
                    setTooltips("往日志处输出文本，以作脚本调试使用");
                }
                setContentXML(R.xml.root_shell);
                break;
            case "发送文本到焦点编辑框":
                setContentXML(R.xml.send_text);
                setTooltips("向一个有焦点的输入框输入文字。  ROOT: 仅可以输入数字和字母。\n输入法：可以输入任意类型，包括汉子和emoji表情。在首页 ->右上角->插件->输入法  打开激活选择才能使用！");
                break;
            case "URL编码":
            case "URL解码":
            case "Base64编码":
            case "Base64解码":
            case "Unicode转Ascll":
            case "Ascll转Unicode":
                setTooltips("将文本按指定格式进行转换，注意：一触即发的文本默认编码是\"UTF-8\"");
                setContentXML(R.xml.encode);
                break;
            case "设置剪贴板文本":
                setContentXML(R.xml.textear);
                break;
            case "获取剪贴板文本":
                setContentXML(R.xml.select_var);
                break;
            case "获取截图":
                setContentXML(R.xml.screencaptrue);
                break;

            case "获取文字":
            case "获取文字2":
                setContentXML(R.xml.screencaptrue_text);
                break;
            case "获取屏幕颜色":
                setContentXML(R.xml.screen_get_color);
                break;
            case "语音识别":
                setContentXML(R.xml.autn_re);
                break;
            case "语音播放":
                setContentXML(R.xml.textear);
                break;
            case "取通知栏信息":
                setContentXML(R.xml.notification);
                break;
            case "打开应用":
                setContentXML(R.xml.open_app);
                break;
            case "创建矩型":
                setContentXML(R.xml.var_create_rect);
                break;
            case "显示网页":
                setContentXML(R.xml.textear);
                break;
            case "保存变量":
                setTooltips("保存一个变量数据，只要不卸载或清除数据，将会一直存在");
                setContentXML(R.xml.var_save);
                break;
            case "读取变量":
                setTooltips("读取一个变量数据");
                setContentXML(R.xml.var_read);
                break;
            case "更新图片到相册":
                setTooltips("把一张图片更新到系统相册中显示");
                setContentXML(R.xml.image_sys_u);
                break;
            case "执行输入法动作":
                setContentXML(R.xml.input_action);
                break;
            case "JSON解析":
                setTooltips("参数四 类型 为JSON文本的结构类型，1为没有类名的单一结构；2为含有类名的单一结构；3为数组结构\n" +
                        "注意：对于数组结构的解析，如果有多个相同的键名，则取出的多个键值之间以换行分隔。");
                setContentXML(R.xml.json);
                break;
            case "DES加密":
                setTooltips("将普通文本进行DES加密，注意：密码必须是8位数!。");
                setContentXML(R.xml.encrypt_des);
                break;
            case "DES解密":
                setTooltips("将加密文本进行DES解密，注意：密码必须是8位数!。");
                setContentXML(R.xml.decrypt_des);
                break;
            case "RC4加密":
                setTooltips("将普通文本进行RC4加密。");
                setContentXML(R.xml.encrypt_rc4);
                break;
            case "RC4解密":
                setTooltips("将加密文本进行RC4解密。");
                setContentXML(R.xml.decrypt_rc4);
                break;
            case "Authcode加密":
                setTooltips("将普通文本进行Authcode加密。");
                setContentXML(R.xml.encrypt_authcode);
                break;
            case "Authcode解密":
                setTooltips("将普通文本进行Authcode加密。");
                setContentXML(R.xml.decrypt_authcode);
                break;
            case "临时QQ会话":
            case "打开QQ资料卡":
                setContentXML(R.xml.qq_temp_chat);
                break;
            case "打开QQ加群":
                setContentXML(R.xml.qq_join_group);
                break;
            case "取QQ昵称":
                setContentXML(R.xml.qq_get_name);
                break;
            case "取QQ头像":
                setContentXML(R.xml.qq_get_icon);
                break;
        }

    }



    @Override
    public void initView() {
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_zhushi;
    }

    @Override
    public String getName() {
        return "脚本注释";
    }

}