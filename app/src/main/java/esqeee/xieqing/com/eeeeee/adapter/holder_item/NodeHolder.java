package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class NodeHolder extends BaseHolder{

    public NodeHolder(Context context, MyAdapter adapter) {
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
            case "取当前窗口类名":
                setTooltips("获取当前窗口的ClassName");
                setContentXML(R.xml.node_getwindow);
                break;
            case "查询所有控件":
                setContentXML(R.xml.node_query);
                break;
            case "取查询控件总数":
                setContentXML(R.xml.node_num);
                break;
            case "取控件":
                setContentXML(R.xml.node_get);
                break;
            case "取控件内容":
            case "取控件类名":
                setContentXML(R.xml.node_get_class);
                break;
            case "取控件矩阵":
                setTooltips("获取控件在屏幕上所占的矩形区域");
                setContentXML(R.xml.node_get_rect);
                break;
            case "取子控件数":
                setContentXML(R.xml.node_num2);
                break;
            case "取子控件":
                setContentXML(R.xml.node_get2);
                break;
            case "取父控件":
                setContentXML(R.xml.node_get3);
                break;
            case "设置控件内容":
                setContentXML(R.xml.node_set);
                break;
            case "反向滑动控件":
            case "正向滑动控件":
                setTooltips("如果是左右滑动的控件(如垂直滚动框，滑动页面框)，则向右滑动\n如果是上下滑动的控件(如垂直滚动框，滑动页面框)，则向下滑动");
                setContentXML(R.xml.node_action);
                break;
            case "长按控件":
            case "点击控件":
            case "获取焦点":
                setContentXML(R.xml.node_action);
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