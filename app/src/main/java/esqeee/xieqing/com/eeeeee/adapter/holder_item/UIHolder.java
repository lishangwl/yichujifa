package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class UIHolder extends BaseHolder{

    public UIHolder(Context context, MyAdapter adapter) {
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
            case "取组件内容":
                setContentXML(R.xml.ui_edit_content);
                break;
            case "取组件选中状态":
            case "取组件可见状态":
                setContentXML(R.xml.ui_ischecked);
                break;
            case "置组件可见状态":
                setContentXML(R.xml.ui_set_checked);
                break;
            case "取进度条进度值":
            case "取下拉框选中项":
                setContentXML(R.xml.ui_progress_current);
                break;
            case "置组件内容":
                setContentXML(R.xml.ui_set_text);
                break;
            case "取组件属性":
                setContentXML(R.xml.ui_get_attr);
                break;
            case "置组件属性":
                setContentXML(R.xml.ui_set_attr);
                setTooltips("每个组件都有其特定的属性值，如名称、大小、宽度、可视、标题等。请在界面处创建组件进行查看");
                break;
            case "取浏览器接口参数":
                setContentXML(R.xml.ui_edit_content);
                break;
            case "置浏览器链接":
            case "置浏览器html代码":
                setContentXML(R.xml.ui_set_content);
                break;
            case "创建组件":
                setContentXML(R.xml.ui_create);
                break;
            case "删除组件":
                setContentXML(R.xml.ui_delete);
                break;
            case "打开窗口":
                setContentXML(R.xml.ui_luach);
                break;

            case "关闭当前窗口":
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