package esqeee.xieqing.com.eeeeee.action;

import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xieqing.codeutils.util.SizeUtils;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.widget.CustomPath;
import esqeee.xieqing.com.eeeeee.widget.FDialog;

public class ActionCoverter {
    private static ActionCoverter coverter;
    private StringBuilder stringBuilder ;
    private int level = 0;
    private JSONBean jsonBean;
    private String TAB = "\t\t";
    public ActionCoverter(JSONBean jsonBean){
        this.jsonBean = jsonBean;
    }
    public ActionCoverter(JSONBean jsonBean,int level){
        this.jsonBean = jsonBean;
        this.level = level;
    }
    public String covert(){
        stringBuilder = new StringBuilder();
        if (jsonBean.has("variables")){
            covertVariables();
        }

        covertArray(jsonBean.getArray("actions"));
        return stringBuilder.toString();
    }

    private void covertArray(JSONArrayBean actions) {
        if (actions == null){
            return;
        }
        for (int i = 0 ; i<actions.length() ; i++){
            covertItem(actions.getJson(i));
        }
    }

    private void covertItem(JSONBean json) {
        JSONBean param = json.getJson("param");
        DoActionBean type = DoActionBean.values()[json.getInt("actionType")];
        String desc = param.has("desc") && !param.getString("desc").equals("")?param.getString("desc"):"";
        boolean status = json.has("status") ? json.getBoolean("status"):true;
        if (!desc.equals("")){
            addLine("//"+desc);
        }
        if (!status){
            stringBuilder.append("//");
        }
        switch (type){
            case OPEN_FLASH:
            case CLOSE_FLASH:
            case KEY_BACK://"返回键",R.mipmap.ic_action_back),
            case KEY_HOME://"主页键",R.drawable.ic_action_home),
            case KEY_TASK://"任务键",R.mipmap.ic_action_task),
            case SWIP_LEFT://"左滑",R.drawable.ic_action_left),
            case SWIP_RIGHT://"右滑",R.drawable.ic_action_right),
            case SWIP_TOP://上滑",R.drawable.ic_action_top),
            case SWIP_BOTTOM://"下滑",R.drawable.ic_action_bottom),
            case SYSTEM_WKKEUP://,"唤醒屏幕",R.mipmap.ic_action_wakeup),
            case KEY_LONG_POWER://"长按电源键",R.mipmap.ic_action_power),
            case SYS_OPEN_WIFI://(18,"打开WIFI",R.mipmap.ic_wifi_on),
            case SYS_CLOSE_WIFI://(19,"关闭WIFI",R.mipmap.ic_wifi_off),
            case SYS_OPEN_BLUETOOTH://(20,"打开蓝牙",R.mipmap.ic_ble_on),
            case SYS_CLOSE_BLUETOOTH://(21,"关闭蓝牙",R.mipmap.ic_ble_off),
            case SYS_WIFISETTING://(22,"WIFI设置",R.mipmap.ic_wifi_icon),
            case SYS_STROGE://(23,"存储空间",R.mipmap.ic_sd_icon),
            case SYS_APPMANGER://(24,"应用管理",R.mipmap.ic_app_icon),
            case SYS_VIBRATE://(25,"静音模式",R.mipmap.ic_volum_off),
            case SYS_NOT_VIBRATE://(26,"振动模式",R.mipmap.ic_volum_on),
            case SYS_SETTING://(27,"系统设置",R.mipmap.ic_setting_icon),
            case SYS_OPEN_NOTIC://(28,"下拉通知栏",R.drawable.ic_notifaction_on),
            case SYS_CLOSE_NOTIC://(29,"折叠通知栏",R.drawable.ic_notifaction_off),
            case STOP:
            case VBRITE://(64,"震动(短)",R.mipmap.ic_vibrate),
            case VBRITE_LONG://(65,"震动(长)",R.mipmap.ic_vibrate),
            case NOTICAFACTION://(66,"铃声提示",R.drawable.tishi),
            case SYS_CLOSE_SCREEN:
            case STOP_ALL:
            case LNK_WECHAT_SCAN://(34,"微信扫一扫",R.drawable.ic_wechat_scan),
            case LNK_ALIPAY_SCAN://(35,"支付宝扫一扫",R.drawable.ic_alipay_scan),
            case LNK_ALIPAY_GETMONEY://(36,"支付宝收钱",R.drawable.ic_alipay_pay),
            case LNK_ALIPAY_SENDMONEY://(37,"支付宝付钱",R.drawable.ic_alipay_pay),
                addLine(type.getActionName()+"()");
                break;
            case BREAK:
                addLine("退出循环");
                break;
            case LNK_TELEPHONE://(38,"一键拨号",R.drawable.ic_lnk_call),
            case LNK_LINK://(39,"打开指定URL",R.drawable.ic_lnk_link),
            case LNK_QQ://(40,"指定QQ聊天",R.drawable.ic_lnk_qq),
                addLine(type.getActionName()+"("+covertString(param.getString("text"))+")");
                break;
            case CLICK_SCREEN:
                addLine("点击("+param.getString("x")+","+param.getString("y")+","+param.getString("count")+")");
                break;
            case CLICK_SCREEN_TEXT:
            case CLICK_TEXT_RECT:
            case LONGCLICK_SCREEN_TEXT:
            case LONGCLICK_SCREEN_TEXT_RECT:
                int scanType = param.getInt("scanType",0);
                Rect rect = getRect(param);
                String line = ((type == DoActionBean.CLICK_SCREEN_TEXT || type == DoActionBean.CLICK_TEXT_RECT)?"点击":"长按") + "文字(";
                line += covertString(param.getString("text"))+",";
                line += scanType == 0?"'node'":"'ocr'";
                line += ",";
                line += ((rect == null)?"null":covertRect(rect)) +"," + param.getBoolean("root",false)+")";
                addLine(line);
                break;
            case CLICK_COLOR:
            case CLICK_RECT_COLOR:
            case LONG_RECT_COLOR:
            case LONGCLICK_COLOR:
                line = "找色"+((type == DoActionBean.CLICK_COLOR || type == DoActionBean.CLICK_RECT_COLOR)?"点击":"长按") + "(";
                line += param.getString("color")+",";
                line += getRect2(param) == null?"null":covertRect2(getRect2(param));
                line += ",相似度:"+(255 - param.getInt("accetrue",5))+",";
                line += param.getBoolean("root",false)+")";
                addLine(line);
                break;
            case CLICK_IMAGE:
            case CLICK_IMAGE_RECT:
            case LONG_CLICK_IMAGE:
            case LONG_CLICK_IMAGE_RECT:
                int accetrue = param.getInt("accetrue",80);
                rect = getRect(param);
                line = param.getBoolean("assign",false)?"屏幕找图(":(((type == DoActionBean.CLICK_IMAGE || type == DoActionBean.CLICK_IMAGE_RECT)?"点击":"长按") + "图片(");
                line += covertString(param.getString("fileName"))+",";
                line += "精确度: "+accetrue+",";
                line += param.getBoolean("assign",false)?"赋值:{横坐标："+param.getString("x")+",纵坐标:"+param.getString("y")+",宽度:"+param.getString("w")+",高度:"+param.getString("h")+"} ,":"";
                line +=((rect == null)?"null":covertRect(rect)) +"," + param.getBoolean("root",false)+")";
                addLine(line);
                break;
            case LUACH_APP:
                addLine((param.getInt("action",0) == 0 ?"打开":"关闭")+"应用("+covertString(param.getString("packName"))+")");
                break;
            case SWIP:
            case GESTRUE:
                addLine("滑动("+covertPath(param.getArray("path"))+",false)");
                break;
            case PASTE_TEXT:
                addLine("粘贴文字("+param.getString("x")+","+param.getString("y")+")");
                break;
            case RANDOM_SLEEP:
                addLine("随机延时("+param.getString("min")+","+param.getString("max")+")");
                break;
            case TOAST:
                addLine("弹出提示("+covertString(param.getString("text"))+")");
                break;
            case DESC:
                addLine("//"+param.getString("text"));
                break;
            case INPUT_TEXT:
                addLine("输入文字("+param.getString("x")+","+param.getString("y")+","+covertString(param.getString("text"))+")");
                break;
            case LONG_CIICK:
                addLine("长按("+param.getString("x")+","+param.getString("y")+","+getRealTime(param.getInt("touchTime",1))+","+param.getBoolean("root",false)+")");
                break;
            case EXCE_ACTION:
                addLine("执行("+covertString(param.getString("actionId"))+","+(param.getBoolean("runInCurrentThread",true)?"'默认'":"'多线程'")+")");
                break;
            case IF:
                convertIf(json,param);
                break;
            case WHILE:
                convertWhile(json,param);
                break;
            case FOR:
                convertFor(json,param);
                break;
            case PRO_HTTP:
                String var = param.getString("var");
                line = var + " = 网页访问(";
                line += covertString(param.getString("url"))+" ,";
                line += param.getInt("requestMethod",0) == 0?"'GET' ,":"'POST' ,";
                line += "header:{"+covertHttpHeaderOrData(param.getArray("datas"))+"} ,";
                line += "data:{"+covertHttpHeaderOrData(param.getArray("datas"))+"})";
                addLine(line);
                break;
            case PRO_ASSGIN:
                addLine(param.getString("var") +" = "+param.getString("value"));
                break;
            case SWIP_LINE:
                addLine("直线滑动("+param.getString("fX")+","+param.getString("fY")+","+param.getString("tX")+","+param.getString("tY")+","+param.getString("time")+")");
                break;
            case FILE_ACTION:
                covertFile(param.getString("arg"),param);
                break;
            case ARRAY_ACTION:
                covertArr(param.getString("arg"),param);
                break;
            case STRING_ACTION:
                covertString(param.getString("arg"),param);
                break;
            case ENCRPTY_ACTION:
                covertEn(param.getString("arg"),param);
                break;
            case DIALOG_ACTION:
                covertDialog(param.getString("arg"),param);
                break;
        }
    }
    private void covertDialog(String arg, JSONBean param) {
        switch (arg){
            case "弹出对话框":
                addLine(param.getString("var") +" = 弹出对话框(" +covertString(param.getString("title"))+","
                +covertString(param.getString("text"))+","
                        +covertString(param.getString("confirm"))+","
                        +covertString(param.getString("cannel")));
                break;
            case "弹出选择框":
                addLine(param.getString("var") +" = 弹出选择框(" +covertString(param.getString("title"))+","
                        +covertString(param.getString("text")));
                break;
            case "弹出输入框":
                addLine(param.getString("var") +" = 弹出对话框(" +covertString(param.getString("title"))+","
                        +covertString(param.getString("hint"))+","
                        +covertString(param.getString("default")));
                break;
        }
    }
    private void covertEn(String arg, JSONBean param) {
        switch (arg){
            case "MD5":
                addLine(param.getString("var") + " = MD5("+covertString(param.getString("text"))+")");
                break;
            case "取时间戳":
                addLine(param.getString("var") + " = 取时间戳("+param.getBoolean("isUseHm",false)+")");
                break;
            case "数学运算":
                addLine(param.getString("var") + " = 数学运算("+param.getString("text")+")");
                break;
            case "取随机数":
                addLine(param.getString("var") + " = 取随机数("+param.getString("min")+","+param.getString("max")+")");
                break;
            case "调试输出日志":
            case "设置剪贴板文本":
            case "执行Shell":
            case "发送文本到焦点编辑框":
                addLine(arg+"("+covertString(param.getString("text"))+")");
                break;
            case "Base64编码":
            case "Base64解码":
            case "URL编码":
            case "URL解码":
                addLine(param.getString("var") + " = "+ arg+"("+covertString(param.getString("text"))+")");
                break;
            case "获取截图":
                Rect rect  = getRect(param.getJson("rect"));
                addLine(param.getString("var") + " = 获取截图("+(rect == null ?"null":covertRect(rect))+")");
                break;
            case "获取文字":
                rect = getRect(param.getJson("rect"));
                addLine(param.getString("var") + " = 获取文字("+(rect == null ?"null":covertRect(rect))+")");
                break;
            case "获取剪贴板文本":
                addLine(param.getString("var") + " = 获取剪贴板文本()");
                break;
        }
    }
    private void covertString(String arg, JSONBean param) {
        switch (arg){
            case "到大写":
            case "到小写":
            case "取文本长度":
                addLine(param.getString("var") + " = "+arg+"("+covertString(param.getString("text"))+")");
                break;
            case "分割文本":
                addLine(param.getString("var") + " = 分割文本("+covertString(param.getString("text"))+","+covertString(param.getString("split"))+")");
                break;
            case "子文本替换":
                addLine(param.getString("var") + " = 子文本替换("+covertString(param.getString("text"))+","+covertString(param.getString("old"))+","+covertString(param.getString("replace"))+")");
                break;
            case "取文本中间":
                addLine(param.getString("var") + " = 取文本中间("+covertString(param.getString("text"))+","+covertString(param.getString("left"))+","+covertString(param.getString("right"))+")");
                break;
            case "取文本左边":
            case "取文本右边":
                addLine(param.getString("var") + " = "+arg+"("+covertString(param.getString("text"))+","+param.getString("length")+")");
                break;
            case "寻找文本":
                addLine(param.getString("var") + " = 寻找文本("+covertString(param.getString("text"))+","+covertString(param.getString("find"))+","+param.getString("start","0")+")");
                break;
        }
    }
    private void covertArr(String arg, JSONBean param) {
        switch (arg){
            case "取数组成员数":
                addLine(param.getString("var") +" = "+ arg+"("+param.getString("var_array")+")");
                break;
            case "取成员":
                addLine(param.getString("var") +" = "+ arg+"("+param.getString("var_array")+","+param.getString("index")+")");
                break;
        }
    }

    private void covertFile(String arg,JSONBean param) {
        switch (arg){
            case "删除文件":
                addLine("删除文件("+covertString(param.getString("path"))+")");
                break;
            case "复制文件":
                addLine("复制文件("+covertString(param.getString("path"))+","+covertString(param.getString("topath"))+")");
                break;
            case "重命名文件":
                addLine("重命名文件("+covertString(param.getString("path"))+","+covertString(param.getString("topath"))+")");
                break;
            case "删除目录":
                addLine("删除目录("+covertString(param.getString("path"))+")");
                break;
            case "写出字节集文件":
                addLine("写出字节集文件("+covertString(param.getString("path"))+","+param.getString("var")+")");
                break;
            case "写出文本文件":
                addLine("写出文本文件("+covertString(param.getString("path"))+","+param.getString("var")+","+param.getBoolean("append",false)+")");
                break;
            case "读入字节集文件":
            case "读入文本文件":
                addLine(param.getString("var") +" = "+arg +"("+covertString(param.getString("path"))+")");
                break;
        }
    }

    private String covertHttpHeaderOrData(JSONArrayBean jsonArrayBean){
        String line = "";
        for (int i = 0 ; i < jsonArrayBean.length() ; i++){
            JSONBean item = jsonArrayBean.getJson(i);
            if (i!=0){
                line += ",";
            }
            line += covertString(item.getString("key"))+" : "+covertString(item.getString("value"));
        }
        return line;
    }

    private void convertFor(JSONBean json, JSONBean param) {
        String line = getTAB()+"计次循环首 " + param.getString("condition")+"\n";
        JSONBean action = json.getJson("trueDo");
        if (action.has("actions")){
            line += new ActionCoverter(action,level+1).covert();
        }
        line+="\n"+getTAB()+"计次循环尾\n";
        stringBuilder.append(line);
    }

    private void convertWhile(JSONBean json, JSONBean param) {
        JSONArrayBean conditions = json.getArray("conditions");
        String line = getTAB()+"判断循环首 " + covertCondition(conditions,json.getInt("AllOrOne",0) == 0)+"\n";
        JSONBean action = json.getJson("trueDo");
        if (action.has("actions")){
            line += new ActionCoverter(action,level+1).covert();
        }
        line+="\n"+getTAB()+"判断循环尾\n";
        stringBuilder.append(line);
    }

    private void convertIf(JSONBean json,JSONBean param) {
        JSONArrayBean conditions = json.getArray("conditions");
        String line = getTAB()+"如果 " + covertCondition(conditions,json.getInt("AllOrOne",0) == 0)+" 则 \n";
        JSONBean action = json.getJson("trueDo");
        if (action.has("actions")){
            line += new ActionCoverter(action,level+1).covert();
        }
        action = json.getJson("falseDo");
        if (action.has("actions")){
            line += getTAB()+"否则\n";
            line += new ActionCoverter(action,level+1).covert();
            line += "\n";
        }

        line+="\n"+getTAB()+"结束 如果";
        stringBuilder.append(line);
    }

    private String covertCondition(JSONArrayBean conditions,boolean all) {
        String line = "";
        if(conditions == null){
            return "null";
        }
        for (int i =0;i<conditions.length();i++){
            JSONBean c =conditions.getJson(i);
            if (c!=null){
                if (i != 0) {
                    line += all ? " 且 " : " 或 ";
                }
                line += convertConditionItem(c);
            }
        }
        return line;
    }

    private String convertConditionItem(JSONBean jsonBean) {
        String line = "";
        JSONBean param = jsonBean.getJson("param");
        switch (jsonBean.getInt("actionType")){
            case 54:
            case 63:
                int accetrue = param.getInt("accetrue",5);
                int color = Color.argb(255,param.getInt("red"),param.getInt("green"),param.getInt("blue"));
                line += "屏幕中是否包含颜色(#"+Integer.toHexString(color);
                line += ",相似度:"+(255 - accetrue);
                line += "," + (getRect2(param)==null?"null":covertRect2(getRect2(param)));
                line += ")";
                break;
            case 44:
            case 45:
                int scanType = param.getInt("scanType",0);
                Rect rect = getRect(param);
                line += "屏幕中是否包含文字(";
                line += covertString(param.getString("text"))+" ,";
                line += scanType == 0 ?"'node' ," : "'ocr' ,";
                line += rect == null ?"null" : covertRect(rect);
                line +=")";
                break;
            case 47:
            case 48:
                accetrue = param.getInt("accetrue",80);
                rect = getRect(param);

                line += "屏幕中是否包含图片("+param.getString("fileName");
                line += ",精确度:"+accetrue;
                line += " ," + (rect == null ?"null" : covertRect(rect));
                line += ")";
                break;

            case 72:
                String value = param.getString("value");
                String var = param.getString("var");
                int assignType = param.getInt("assgin",0);

                line += var + " " + covertAssignType(assignType)+" "+value;
                break;
        }
        return line;
    }

    private String covertAssignType(int assignType){
        switch (assignType){
            case 0:// ==
                return "=";
            case 1:// !=
                return "!=";
            case 2:// >
                return ">";
            case 3:// <
                return "<";
            case 4:// >=
                return ">=";
            case 5:// <=
                return "<=";
            case 6:// contains
                return "包含";
        }
        return "unkownAssgin";
    }


    private int getRealTime(int type){
        if (type == 0){
            return 1000;
        }
        if (type == 2){
            return 4000;
        }
        return 2000;
    }
    private String covertPath(JSONArrayBean customPaths) {
        String path = "[";
        for (int i = 0;i<customPaths.length();i++){
            CustomPath customPath =CustomPath.from(customPaths.getJson(i));
            if (customPath == null){
                return "[]";
            }
            if (i!=0){
                path += " , ";
            }
            path+= "{手势: &手势"+(i+1)+",time: "+customPath.getDuration()+"}";
        }
        path+="]";
        return path;
    }

    private String covertRect(Rect rect) {
        return "[left:"+rect.left+",top:"+rect.top+",right:"+rect.right+",bottom:"+rect.bottom+"]";
    }
    private String covertRect2(org.opencv.core.Rect rect) {
        return "[left:"+rect.x+",top:"+rect.y+",right:"+(rect.x+rect.width)+",bottom:"+(rect.y+rect.height)+"]";
    }

    public Rect getRect(JSONBean param){
        int left = param.getInt("left",-1);
        int top = param.getInt("top",-1);
        int right = param.getInt("right",-1);
        int bottom = param.getInt("bottom",-1);
        if (left == -1 || top == -1 || right == -1 || bottom == -1){
            return null;
        }
        return new Rect(left,top,right,bottom);
    }
    public org.opencv.core.Rect getRect2(JSONBean param) {
        int left = param.getInt("x",-1);
        int top = param.getInt("y",-1);
        int right = param.getInt("width",-1);
        int bottom = param.getInt("height",-1);
        if (left == -1 || top == -1 || right == -1 || bottom == -1){
            return null;
        }
        return new org.opencv.core.Rect(left,top,right,bottom);
    }

    private void covertVariables() {
        JSONArrayBean jsonArrayBean = jsonBean.getArray("variables");
        for (int i = 0 ; i<jsonArrayBean.length() ; i++){
            JSONBean var = jsonArrayBean.getJson(i);
            VariableType type = VariableType.from(var.getInt("type"));
            String name = var.getString("name");
            String value = var.getString("value","空");
            String line = "变量 "+name+" = ";
            if (value.equals("空")){
                line += "空";
            }else{
                switch (type){
                    case INT:
                        try {
                            line += value;
                        }catch (NumberFormatException e){
                            line += "到整数("+covertString(value)+")";
                        }
                        break;
                    case BOOL:
                        if (value.equals("真") || value.equals("true")){
                            line += "真";
                        }else if (value.equals("假") || value.equals("false")){
                            line += "假";
                        }else{
                            line += "假";
                        }
                        break;
                    case BYTES:
                        line += "到字节集("+covertString(value)+")";
                        break;
                    case STRING:
                        line += covertString(value);
                        break;
                    case STRING_ARRAY:
                        line += "创建文本数组("+covertString(value)+")";
                        break;
                }
            }
            addLine(line);
        }
    }

    private String getTAB(){
        String s = "";
        for (int i = 0 ;i <level;i++){
            s+=TAB;
        }
        return s;
    }

    private void addLine(String line) {
        stringBuilder.append(getTAB());
        stringBuilder.append(line).append(";\n");
    }

    private String covertString(String s){
        return "\""+s.replace("\"","\\\"")+"\"";
    }
}
