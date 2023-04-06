package esqeee.xieqing.com.eeeeee.doAction.api;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.yicu.yichujifa.GlobalContext;

import java.io.File;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.helper.PathHelper;
import uidesign.MainActivity;
import uidesign.project.ViewLayoutInflater;
import uidesign.project.inflater.BaseLayoutInflater;
import uidesign.project.inflater.GboalViewHolder;
import uidesign.project.inflater.listener.UITouch;
import uidesign.project.inflater.widget.UIWebView;
import uidesign.project.model.Attr;

import static uidesign.project.inflater.BaseLayoutInflater.ATTR_NAME;

public class UI extends Base {
    public static final int ACTION_DELETE_FILE = 0;
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new UI();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        postUi(param);
        return true;
    }

    public boolean postUi(JSONBean param){
        String arg = param.getString("arg");
        JSONBean var;
        View view;
        switch (arg){
            case "取组件内容":
                var = checkVarExiets(param,"var");
                view = checkViewExiets(getStringFromParam("text"));
                if (view instanceof TextView){
                    setValue(var,((TextView)view).getText());
                }
                break;
            case "取浏览器接口参数":
                var = checkVarExiets(param,"var");
                view = checkViewExiets(getStringFromParam("text"));
                if (view instanceof UIWebView){
                    setValue(var,((UIWebView)view).getJsParam());
                }
                break;
            case "取组件选中状态":
                var = checkVarExiets(param,"var");
                view = checkViewExiets(getStringFromParam("text"));
                if (view instanceof CompoundButton){
                    setValue(var,((CompoundButton)view).isChecked());
                }
                break;

            case "取组件可见状态":
                var = checkVarExiets(param,"var");
                view = checkViewExiets(getStringFromParam("text"));
                setValue(var,view.getVisibility() == View.VISIBLE);
                break;

            case "置组件可见状态":
                ThreadUtils.runOnUiThread(()->{
                    checkViewExiets(getStringFromParam("text"))
                            .setVisibility(param.getInt("type") == 0?View.VISIBLE:View.GONE);
                });
                break;

            case "取进度条进度值":
                var = checkVarExiets(param,"var");
                view = checkViewExiets(getStringFromParam("text"));
                if (view instanceof SeekBar){
                    setValue(var,((SeekBar)view).getProgress());
                }
                break;

            case "取下拉框选中项":
                var = checkVarExiets(param,"var");
                view = checkViewExiets(getStringFromParam("text"));
                if (view instanceof Spinner){
                    setValue(var,((Spinner)view).getSelectedItemPosition());
                }
                break;

            case "置组件内容":
                ThreadUtils.runOnUiThread(()->{
                    View v = checkViewExiets(getStringFromParam("name"));
                    String content = getString(param.getString("text"));
                    if (v instanceof TextView){
                        ((TextView)v).setText(content);
                    }
                });
                break;
            case "置浏览器链接":
                ThreadUtils.runOnUiThread(()->{
                    View v = checkViewExiets(getStringFromParam("name"));
                    String content = getStringFromParam("text");
                    if (v instanceof UIWebView){
                        ((UIWebView)v).removeAllViews();
                        ((UIWebView)v).loadUrl(content);
                    }
                });
                break;
            case "置浏览器html代码":
                ThreadUtils.runOnUiThread(()->{
                    View v = checkViewExiets(getStringFromParam("name"));
                    String content = getStringFromParam("text");
                    if (v instanceof UIWebView){
                        ((UIWebView)v).removeAllViews();
                        ((UIWebView)v).loadDataWithBaseURL(null,content,"text/html","UTF-8",null);
                    }
                });
                break;
            case "取组件属性":
                var = checkVarExiets(param,"var");
                view = checkViewExiets(getStringFromParam("name"));
                String key = getStringFromParam("key");
                Attr attr = (Attr) view.getTag();
                if (attr != null){
                    if (attr.has(key)){
                        setValue(var,attr.getString(key));
                    }
                }
                break;
            case "置组件属性":
                    View viewExiets = checkViewExiets(getStringFromParam("name"));
                    String attrKey = getStringFromParam("key");
                    String attrValue = getString(param.getString("value"));
                    Attr attrs = (Attr) viewExiets.getTag();
                    if (attrs != null){
                        if (attrs.has(attrKey)){
                            attrs.put(attrKey,attrValue);
                            if (MainActivity.currentActivity!=null) {
                                ThreadUtils.runOnUiThread(()->{
                                    MainActivity.currentActivity.applyView(viewExiets, attrs);
                                });
                            }else{
                                throw new RuntimeException("没有任何界面被加载");
                            }
                        }else{
                            throw new RuntimeException("错误：组件["+getStringFromParam("name")+"] 没有该属性["+attrKey+"]");
                        }
                    }else {
                        throw new RuntimeException("错误：error view don't has attr");
                    }
                break;
            case "创建组件":
                String name = getStringFromParam("name");
                String type = getStringFromParam("type");
                if (MainActivity.currentActivity!=null) {
                    BaseLayoutInflater layoutInflater = (BaseLayoutInflater) ViewLayoutInflater.inflaterMap.get(type);
                    if (layoutInflater != null){
                        Attr attr1 = layoutInflater.getBaseAttr();
                        attr1.put(ATTR_NAME,name);
                        lock();
                        ThreadUtils.runOnUiThread(()->{
                            View instance = layoutInflater.createViewInstance(MainActivity.currentActivity,attr1);
                            MainActivity.currentActivity.addView(instance);
                            MainActivity.currentActivity.getViewHolder().put(new GboalViewHolder.UIView(new UITouch(instance,attr1,layoutInflater,false),instance,layoutInflater));
                            instance.setTag(attr1);
                            layoutInflater.bind(instance,attr1,false);
                            layoutInflater.bindView(instance,attr1);

                            unlock();
                        });
                        waitForUnlock();
                        //RuntimeLog.log(MainActivity.currentActivity.findViewByTag(name) == null);
                    }else{
                        throw new RuntimeException("创建组件失败：没有该类型组件("+type+")");
                    }
                }else{
                    throw new RuntimeException("没有任何界面被加载");
                }
                break;
            case "删除组件":
                View view1 = checkViewExiets(getStringFromParam("name"));
                ThreadUtils.runOnUiThread(()->{
                    if (MainActivity.currentActivity!=null) {
                        MainActivity.currentActivity.deleteView(view1);
                    }else{
                        throw new RuntimeException("没有任何界面被加载");
                    }
                });
                break;
            case "打开窗口":
                File file = new File(PathHelper.covert(getStringFromParam("name")));
                if (!file.exists()){
                    throw new RuntimeException("界面文件不存在："+getStringFromParam("name"));
                }
                GlobalContext.getContext().startActivity(new Intent(GlobalContext.getContext(),
                        MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("layout", FileIOUtils.readFile2String(file))
                );
                break;
            case "关闭当前窗口":
                if (MainActivity.currentActivity!=null) {
                    MainActivity.currentActivity.finish();
                    MainActivity.currentActivity=null;
                }else{
                    throw new RuntimeException("没有任何界面被加载");
                }
                break;
        }
        return true;
    }


    public View checkViewExiets(String name) {
        if (MainActivity.currentActivity == null){
            throw new RuntimeException("没有任何界面被加载");
        }

        View uiView = MainActivity.currentActivity.findViewByTag(name);
        if (uiView == null){
            throw new RuntimeException("错误，找不到组件 ["+name+"]");
        }else {
            return uiView;
        }
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
