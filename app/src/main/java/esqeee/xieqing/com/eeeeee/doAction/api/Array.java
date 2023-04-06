package esqeee.xieqing.com.eeeeee.doAction.api;

import android.text.TextUtils;

import com.xieqing.codeutils.util.StringUtils;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class Array extends Base {
    public static final int ACTION_DELETE_FILE = 0;
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Array();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        String arg = param.getString("arg");
        switch (arg){
            case "加入成员":
                JSONBean array = checkVarExiets(param,"var_array");
                String text = getString(param.getString("value"));
                JSONArrayBean jsonArrayBean1 = array.getArray("value");
                if (jsonArrayBean1 != null){
                    if (TextUtils.isEmpty(param.getString("index").trim())){
                        jsonArrayBean1.put(text);
                    }else{
                        try {
                            jsonArrayBean1.add(evalMathOrThrow(param,"index"),text);
                        }catch (Exception e){
                            throw new RuntimeException("超出数组下标:"+e.getMessage());
                        }
                    }
                }
                break;
            case "取数组成员数":
            case "取成员":
                JSONBean var = checkVarExiets(param,"var");
                JSONBean var2 = checkVarExiets(param,"var_array");
                JSONArrayBean jsonArrayBean = var2.getArray("value");
                if (jsonArrayBean == null){
                    if (!arg.equals("取成员")){
                        setValue(var,"0");
                    }else {
                        setValue(var,"空");
                    }
                }else{
                    if (!arg.equals("取成员")){
                        setValue(var,String.valueOf(jsonArrayBean.length()));
                    }else {
                        MathResult index = evalMath(param.getString("index"));
                        if (index.isError()){
                            throw new RuntimeException("<error[正在计算数组下标]>计算表达式有误："+index.getException().getMessage());
                        }
                        if (index.getResult() < 0){
                            throw new RuntimeException("<取成员>error:数组下标不能为小于0，当前数组下标："+(int)index.getResult());
                        }

                        if (index.getResult() >= jsonArrayBean.length()){
                            throw new RuntimeException("<取成员>error:数组下标超出数组长度，当前数组下标："+(int)index.getResult()+",数组长度："+jsonArrayBean.length());
                        }
                        setValue(var,String.valueOf(jsonArrayBean.get((int)index.getResult())));
                    }
                }
                break;
        }
        return true;
    }

    private int evalMathOrThrow(JSONBean param, String index) {
        MathResult result = evalMath(param.getString(index));
        if (result.isError()){
            throw new RuntimeException("计算表达式有误："+result.getException().getMessage());
        }
        return result.getResult();
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
