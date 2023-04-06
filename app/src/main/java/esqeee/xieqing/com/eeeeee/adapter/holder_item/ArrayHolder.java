package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class ArrayHolder extends BaseHolder{

    public ArrayHolder(Context context, MyAdapter adapter) {
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
            case "取数组成员数":
                setTooltips("返回数组的成员数。");
                setContentXML(R.xml.array_len);
                break;
            case "取成员":
                setContentXML(R.xml.array_get);
                break;
            case "加入成员":
                setTooltips("向数组指定位置中加入一条值, 如果不填索引则默认为在最后面加入");
                setContentXML(R.xml.array_push);
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