package esqeee.xieqing.com.eeeeee.doAction.api;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class Assgin extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Assgin();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        String varName = param.getString("var");
        JSONBean jsonBean = queryVariable(varName);

        if (jsonBean == null){
            throw new RuntimeException("<赋值变量>:找不到变量 ["+varName+"]");
        }

        int type = jsonBean.getInt("type");
        String value = param.getString("value");
        if (type == 0){
            //string
            jsonBean.put("value",getString(value));
        }else if (type == 1){
            //int
            MathResult result = evalMath(value);
            if (result.isError()){
                throw new RuntimeException("<赋值变量>：无法赋值，计算错误："+result.getException().getMessage());
            }else{
                jsonBean.put("value",String.valueOf(result.getResult()));
            }
        }else if (type == 3){
            //bool
            String[] variables = findVariables(value);
            JSONBean var = null;
            for (String v:variables){
                if (var != null){
                    throw new RuntimeException("<赋值变量>：布尔型的变量只能赋值 真 或者 假 或者 布尔型的单一变量");
                }
                var = queryVariable(v);
            }
            if (var != null && var.getInt("type")!=VariableType.BOOL.ordinal()){
                throw new RuntimeException("<赋值变量>：布尔型的变量只能赋值 真 或者 假 或者 布尔型的单一变量");
            }
            value = getString(value);
            if (!value.trim().equals("真") && !value.trim().equals("假")&& !value.trim().equals("Null")){
                throw new RuntimeException("<赋值变量>：布尔型的变量只能赋值 真 或者 假 或者 布尔型的单一变量");
            }else{
                jsonBean.put("value",value.trim());
            }
        }else{
            throw new RuntimeException("<赋值变量>：错误，不支持赋值的变量 ["+varName+"]  类型["+VariableType.values()[type].getTypeName()+"]");
        }

        return true;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public String getName() {
        return "赋值变量";
    }
}
