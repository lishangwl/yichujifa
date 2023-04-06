package esqeee.xieqing.com.eeeeee.doAction.api;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;

public class Node extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Node();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        waitForAccessbility();
        String arg = param.getString("arg");
        JSONBean var;
        switch (arg){
            case "取当前窗口类名":
                var = queryVariable(param.getString("var"));
                assignVar(arg,var,param.getString("var"));
                setValue(var,AccessbilityUtils.getService().getCurrentWindowClassName());
                break;
            case "查询所有控件":
                var = queryVariable(param.getString("var"));
                assignVar(arg,var,param.getString("var"));
                int type = param.getInt("type",0);
                String text = getString(param.getString("text"));
                if (type == 0 || type == 1){
                    AccessibilityNodeInfo root = AccessbilityUtils.getService().getRootInActiveWindow();
                    if (root != null){
                        setValue(var,type == 0 ?root.findAccessibilityNodeInfosByViewId(text):root.findAccessibilityNodeInfosByText(text));
                    }else {
                        setValue(var,"空");
                    }
                }else if (type == 2){
                    setValue(var,AccessbilityUtils.findNodesByClass(text));
                }else if (type == 3){
                    setValue(var,AccessbilityUtils.findNodesByDescription(text));
                }else{
                    RuntimeLog.e("<查询所有控件>:没有定义查询方式："+type);
                    return false;
                }
                break;
            case "取查询控件总数":
                var = queryVariable(param.getString("var"));
                assignVar(arg,var,param.getString("var"));
                JSONBean var2 = queryVariable(param.getString("var2"));
                assignVar(arg,var,param.getString("var2"));

                List<AccessibilityNodeInfo> nodeInfos = getNodesValue(var2);
                setValue(var,nodeInfos == null ?0:nodeInfos.size());
                break;
            case "取子控件数":
                var = queryVariable(param.getString("var"));
                assignVar(arg,var,param.getString("var"));
                var2 = queryVariable(param.getString("var2"));
                assignVar(arg,var,param.getString("var2"));

                AccessibilityNodeInfo node = getNodeValue(var2);
                setValue(var,node == null ?0:node.getChildCount());
                break;
            case "取控件矩阵":
                var = queryVariable(param.getString("var"));
                node = getNodeValue(var);
                if (node!=null){
                    Rect rect = new Rect();
                    node.getBoundsInScreen(rect);
                    setValue(queryVariable(param.getString("left")),rect.left);
                    setValue(queryVariable(param.getString("top")),rect.top);
                    setValue(queryVariable(param.getString("width")),rect.width());
                    setValue(queryVariable(param.getString("height")),rect.height());
                }
                break;
            case "取控件内容":
            case "取控件类名":
                var = queryVariable(param.getString("var"));
                assignVar(arg,var,param.getString("var"));
                var2 = queryVariable(param.getString("var2"));
                assignVar(arg,var,param.getString("var2"));

                node = getNodeValue(var2);
                text = "";
                switch (arg){
                    case "取控件内容":
                        text = node == null ?"":node.getText() == null?"":node.getText().toString();
                        break;
                    case "取控件类名":
                        text = node == null ?"":node.getClassName() == null?"":node.getClassName().toString();
                        break;
                }
                setValue(var,text);
                break;
            case "取控件":
                var = queryVariable(param.getString("var"));
                assignVar(arg,var,param.getString("var"));
                var2 = queryVariable(param.getString("var2"));
                assignVar(arg,var,param.getString("var2"));
                MathResult index = evalMath(param.getString("index","0"));
                if (index.getException()!=null){
                    RuntimeLog.e("<取控件>:计算索引错误:"+index.getException());
                    return false;
                }

                nodeInfos = getNodesValue(var2);
                if (nodeInfos == null ){
                    setValue(var,"空");
                }else{
                    if (index.getResult()>= nodeInfos.size()){
                        RuntimeLog.e("<取控件>:超出数组下标，索引:"+index.getResult()+",总大小："+nodeInfos.size());
                        return false;
                    }else{
                        setValue(var,nodeInfos.get(index.getResult()));
                    }
                }
                break;
            case "取子控件":
                var = queryVariable(param.getString("var"));
                assignVar(arg,var,param.getString("var"));
                var2 = queryVariable(param.getString("var2"));
                assignVar(arg,var,param.getString("var2"));
                index = evalMath(param.getString("index","0"));
                if (index.getException()!=null){
                    RuntimeLog.e("<取子控件>:计算索引错误:"+index.getException());
                    return false;
                }

                node = getNodeValue(var2);
                if (node == null ){
                    setValue(var,"空");
                }else{
                    if (index.getResult()>= node.getChildCount()){
                        RuntimeLog.e("<取子控件>:超出数组下标，索引:"+index.getResult()+",总大小："+node.getChildCount());
                        return false;
                    }else{
                        setValue(var,node.getChild(index.getResult()));
                    }
                }
                break;
            case "取父控件":
                var = queryVariable(param.getString("var"));
                assignVar(arg,var,param.getString("var"));
                var2 = queryVariable(param.getString("var2"));
                assignVar(arg,var,param.getString("var2"));
                node = getNodeValue(var2);
                if (node == null ){
                    setValue(var,"空");
                }else{
                    setValue(var,node.getParent());
                }
                break;
            case "设置控件内容":
                var = queryVariable(param.getString("var"));
                assignVar(arg,var,param.getString("var"));
                text = param.getString("text");
                text = getString(text);

                AccessbilityUtils.setNodeText(getNodeValue(var),text);
                break;
            case "反向滑动控件":
            case "正向滑动控件":
            case "点击控件":
            case "长按控件":
            case "获取焦点":
                var = queryVariable(param.getString("var"));
                assignVar(arg,var,param.getString("var"));
                boolean result = false;
                AccessibilityNodeInfo nodeInfo = getNodeValue(var);
                if (nodeInfo == null){
                    RuntimeLog.i("<"+arg+">:控件为空");
                }else{
                    switch (arg){
                        case "反向滑动控件":
                            result = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                            break;
                        case "正向滑动控件":
                            result = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                            break;
                        case "点击控件":
                            result = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            break;
                        case "长按控件":
                            result = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                            break;
                        case "获取焦点":
                            result = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                            break;
                    }
                }
                log("<"+arg+">:"+(result?"成功":"失败"));
                break;
        }
        return true;
    }

    public List<AccessibilityNodeInfo> getNodesValue(JSONBean var){
        if (var == null){
            return null;
        }
        Object nodeInfos = var.get("value");
        if (nodeInfos!=null){
            if (nodeInfos instanceof ArrayList){
                return (List<AccessibilityNodeInfo>) nodeInfos;
            }
        }
        return null;
    }
    public AccessibilityNodeInfo getNodeValue(JSONBean var){
        if (var == null){
            return null;
        }
        Object nodeInfos = var.get("value");
        if (nodeInfos!=null){
            if (nodeInfos instanceof AccessibilityNodeInfo){
                return (AccessibilityNodeInfo) nodeInfos;
            }
        }
        return null;
    }
    public void setValue(JSONBean var,List<AccessibilityNodeInfo> nodeInfos){
        if (var == null){
            return;
        }
        var.put("value",nodeInfos);
    }

    public void setValue(JSONBean var,AccessibilityNodeInfo nodeInfo){
        if (var == null){
            return;
        }
        var.put("value",nodeInfo);
    }

    public void recyle(JSONBean var){
        Object nodeInfos = var.get("value");
        if (nodeInfos!=null){
            if (nodeInfos instanceof ArrayList){
                AccessbilityUtils.recyleNodes((List<AccessibilityNodeInfo>) nodeInfos);
            }
            if (nodeInfos instanceof AccessibilityNodeInfo){
                ((AccessibilityNodeInfo) nodeInfos).recycle();
            }
        }
    }

    private void assignVar(String arg,JSONBean var, String var1) {
        if (var == null){
            RuntimeLog.e("<"+arg+">:错误，找不到变量["+var1+"]");
            getActionRun().stopDo();
        }
    }

    private boolean writeByte(String file,byte[] bytes){
        try {
            java.io.File file1 = new java.io.File(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
