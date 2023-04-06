package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class DialogHolder extends BaseHolder{

    public DialogHolder(Context context, MyAdapter adapter) {
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
            case "弹出对话框":
                setTooltips("参数都为选填，点击了确定后返回 0 ，点击取消或者按了返回键后  返回 1");
                setContentXML(R.xml.dialog_confirm);
                break;
            case "弹出输入框":
                setTooltips("参数都为选填，点击了确定后返回输入的内容，点击取消或者按了返回键后  返回 空内容");
                setContentXML(R.xml.dialog_input);
                break;
            case "弹出选择框":
                setTooltips("参数都为选填，点击了选项后返回选项的位置，从0开始，点击取消或者按了返回键后  返回 -1");
                setContentXML(R.xml.dialog_choose);
                break;
            case "弹出输入法选择框":
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