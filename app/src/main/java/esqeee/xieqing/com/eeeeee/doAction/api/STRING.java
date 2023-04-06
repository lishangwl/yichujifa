package esqeee.xieqing.com.eeeeee.doAction.api;

import android.util.Log;

import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.StringUtils;
import com.zhihu.matisse.compress.FileUtil;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class STRING extends Base {
    public static final int ACTION_DELETE_FILE = 0;
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new STRING();
        }
        return intansce;
    }

    public static String 子文本替换2(String str, int start, int end, String replace) {
        if ("".equals(str) || start < 0 || start > str.length() || end < start || end > str.length()) {
            return "";
        }
        return str.substring(0, start) + replace + str.substring(end + 1);
    }

    private String trasplateRegx(String split) {
        if (split.contains("\\")){
            split = split.replace("\\","\\\\");
        }else if (split.contains("$")){
            split = split.replace("$","\\$");
        }else if (split.contains("(")){
            split = split.replace("(","\\(");
        }else if (split.contains(")")){
            split = split.replace(")","\\)");
        }else if (split.contains(".")){
            split = split.replace(".","\\.");
        }else if (split.contains("|")){
            split = split.replace("|","\\|");
        }else if (split.contains("*")){
            split = split.replace("*","\\*");
        }else if (split.contains("+")){
            split = split.replace("+","\\+");
        }else if (split.contains("[")){
            split = split.replace("[","\\[");
        }else if (split.contains("?")){
            split = split.replace("?","\\?");
        }
        return split;
    }

    @Override
    public boolean post(JSONBean param) {
        java.lang.String arg = param.getString("arg");
        switch (arg){
            case "到大写":
            case "到小写":
            case "取文本长度":
                JSONBean var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<文本操作："+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                java.lang.String text = getString(param.getString("text"));
                var.put("value", arg.equals("取文本长度")?String.valueOf(text.length()):arg.equals("到大写")?text.toUpperCase():text.toLowerCase());
                break;
            case "分割文本":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<文本操作："+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                text = getString(param.getString("text"));
                String split = getString(param.getString("split"));
                split=trasplateRegx(split);
                var.put("value",new JSONArrayBean(text.split(split)));
                break;
            case "子文本替换":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<文本操作：子文本替换>:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                text = getString(param.getString("text"));
                java.lang.String old = getString(param.getString("old"));
                java.lang.String replace = getString(param.getString("replace"));
                //log("t="+text+",l="+left+",r="+right);
                var.put("value", text.replace(old,replace));
                break;
            case "子文本替换2":
                var = checkVarExiets("var");
                if (var == null){
                    RuntimeLog.e("<文本操作："+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }

                text = getStringFromParam("text");

                MathResult replaceStart = evalMath(param.getString("replace_start"));
                if (replaceStart.isError()){
                    RuntimeLog.e("<error[正在计算长度]>计算表达式有误："+replaceStart.getException().getMessage());
                    return false;
                }
                if (replaceStart.getResult()<0){
                    RuntimeLog.e("<"+arg+">error：长度不能小于0");
                    return false;
                }

                MathResult replaceStop = evalMath(param.getString("replace_start"));
                if (replaceStop.isError()){
                    RuntimeLog.e("<error[正在计算长度]>计算表达式有误："+replaceStop.getException().getMessage());
                    return false;
                }
                if (replaceStop.getResult()<0){
                    RuntimeLog.e("<"+arg+">error：长度不能小于0");
                    return false;
                }
                replace = getString(param.getString("replace"));
                replace = 子文本替换2(text,replaceStart.getResult(),replaceStop.getResult(),replace);
                Log.d("子文本替换2","结果 -> " + replace);
                setValue(var, replace);
                break;
            case "取文本中间":
                return getStringCenter(param);
            case "批量取文本中间":
                var = checkVarExiets(param,"var");
                text = getString(param.getString("text"));
                java.lang.String left = getString(param.getString("left"));
                java.lang.String right = getString(param.getString("right"));
                var.put("value", new JSONArrayBean(StringUtils.getSubStringArray(text,left,right)));
                break;
            case "取文本左边":
            case "取文本右边":
                var = queryVariable(param.getString("var"));
                if (var == null){
                    RuntimeLog.e("<文本操作："+arg+">:错误，找不到变量["+param.getString("var")+"]");
                    return false;
                }
                text = getString(param.getString("text"));
                MathResult length = evalMath(param.getString("length"));
                if (length.isError()){
                    RuntimeLog.e("<error[正在计算长度]>计算表达式有误："+length.getException().getMessage());
                    return false;
                }
                if (length.getResult()<0){
                    RuntimeLog.e("<"+arg+">error：长度不能小于0");
                    return false;
                }

                setValue(var,arg.equals("取文本左边")?text.substring(0,(int)length.getResult()):text.substring(text.length() - (int)length.getResult(),text.length()));
                break;
            case "倒找文本":
            case "寻找文本":
                var = checkVarExiets("var");
                int start = getMathResultOrThrow("start",0);
                text = getStringFromParam("text");
                String find = getStringFromParam("find");
                setValue(var,arg.equals("寻找文本")?text.indexOf(find,start):text.lastIndexOf(find,start));
                break;
            case "删首尾空":
                var = checkVarExiets(param,"var");
                setValue(var,var.getString("value").trim());
                break;
            case "文本到整数":
                var = checkVarExiets(param,"var");
                setValue(var,Long.parseLong(getString(param.getString("text"))));
                break;
            case "文本比较":
                var = checkVarExiets(param,"var");
                text = getStringFromParam("text");
                String text2 = getStringFromParam("text2");
                setValue(var,text.compareTo(text2));
                break;
            case "翻转文本":
                var = checkVarExiets(param,"var");
                text = getStringFromParam("text");
                setValue(var, new StringBuffer(text).reverse().toString());
                break;
        }
        return true;
    }

    private boolean getStringCenter(JSONBean param) {
        JSONBean var = queryVariable(param.getString("var"));
        if (var == null){
            RuntimeLog.e("<文本操作：取文本中间>:错误，找不到变量["+param.getString("var")+"]");
            return false;
        }
        java.lang.String text = getString(param.getString("text"));
        java.lang.String left = getString(param.getString("left"));
        java.lang.String right = getString(param.getString("right"));
        //log("t="+text+",l="+left+",r="+right);
        var.put("value", StringUtils.getSubString(text,left,right));
        return true;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public java.lang.String getName() {
        return "文件操作";
    }
}
