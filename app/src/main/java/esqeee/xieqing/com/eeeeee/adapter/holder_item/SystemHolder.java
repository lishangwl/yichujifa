package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class SystemHolder extends BaseHolder{

    public SystemHolder(Context context, MyAdapter adapter) {
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
            case "取手机信息":
                setTooltips("取手机信息返回一段json文本");
                setContentXML(R.xml.sys_getinfo);
                break;
            case "置屏幕亮度":
                setContentXML(R.xml.sys_set_light);
                break;
            case "置屏幕亮度模式":
                setContentXML(R.xml.sys_set_light_mode);
                break;
            case "调用系统分享":
                setTooltips("一键分享，可以选择系统中已安装的相关应用（例如短信、QQ空间等）进行分享\n" +
                        "参数二 待分享的图片 为要分享的图片的完整路径，必须是SD卡上的图片，可以用相机组件的浏览相册命令来选择相册中的图片，如果不需要分享图片可以为空文本。");
                setContentXML(R.xml.sys_share);
                break;
            case "调用系统打开文件":
                setContentXML(R.xml.sys_open_file);
                break;
            case "卸载应用":
                setContentXML(R.xml.app_uninstall);
                break;
            case "取应用信息":
                setContentXML(R.xml.app_info);
                break;
            case "打开网络设置":
                setContentXML(-1);
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