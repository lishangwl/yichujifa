package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.view.View;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;

public class SettingHolder extends BaseHolder{

    public SettingHolder(Context context, MyAdapter adapter) {
        super(context,-1,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        findViewById(R.id.holder_expland).setVisibility(View.GONE);
        int actionType = jsonBean.getInt("actionType");
        switch (DoActionBean.getBeanFromType(actionType)){
            case SYS_WIFISETTING:
                setHead("打开WIFI设置",R.drawable.ic_wifi);
                break;
            case SYS_STROGE:
                setHead("打开存储空间",R.drawable.ic_chucun);
                break;
            case SYS_APPMANGER:
                setHead("打开应用管理",R.drawable.ic_yingyongguanli);
                break;
            case SYS_VIBRATE:
                setHead("静音模式",R.drawable.ic_jinyin);
                break;
            case SYS_NOT_VIBRATE:
                setHead("震动模式",R.drawable.ic_zhendongmoshi);
                break;
            case SYS_SETTING:
                setHead("打开系统设置",R.drawable.ic_setting);
                break;
            case SYS_OPEN_NOTIC:
                setHead("展开通知栏",R.drawable.ic_xiala_tongzhi);
                break;
            case SYS_CLOSE_NOTIC:
                setHead("折叠通知栏",R.drawable.ic_shangla_tongzhi);
                break;
            case STOP:
                requestTime(false);
                setHead("停止脚本",R.drawable.ic_holder_stop);
                break;
            case BREAK:
                requestTime(false);
                setHead("跳出循环",R.drawable.ic_holder_stop);
                break;
            case RETURN:
                requestTime(false);
                setHead("退出",R.drawable.ic_holder_stop);
                break;
            case STOP_ALL:
                requestTime(false);
                setHead("停止所有脚本",R.drawable.ic_holder_stop);
                break;
            case LNK_WECHAT_SCAN:
                setHead("打开微信扫一扫",R.drawable.ic_weixin_saoyisao);
                break;
            case LNK_ALIPAY_SCAN:
                setHead("打开支付宝扫一扫",R.drawable.ic_zhifubao_saoyisao);
                break;
            case LNK_ALIPAY_GETMONEY:
                setHead("打开支付宝收钱",R.drawable.ic_zhifubao);
                break;
            case LNK_ALIPAY_SENDMONEY:
                setHead("打开支付宝付钱",R.drawable.ic_zhifubao);
                break;
            case SYS_CLOSE_SCREEN:
                setHead("息灭屏幕",R.drawable.ic_xiping);
                break;
            case VBRITE:
                setHead("震动(短)",R.drawable.ic_zhendong);
                break;
            case VBRITE_LONG:
                setHead("震动(长)",R.drawable.ic_zhendong);
                break;
            case NOTICAFACTION:
                setHead("铃声提示",R.drawable.ic_xiangling);
                break;
        }
    }



    @Override
    public void initView() {
    }

    @Override
    public int getIcon() {
        return R.drawable.holder_0_1;
    }

    @Override
    public String getName() {
        return "KEY";
    }
}
