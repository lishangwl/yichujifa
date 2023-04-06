package esqeee.xieqing.com.eeeeee.doAction;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionRun;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.doAction.api.App;
import esqeee.xieqing.com.eeeeee.doAction.api.Array;
import esqeee.xieqing.com.eeeeee.doAction.api.Assgin;
import esqeee.xieqing.com.eeeeee.doAction.api.Auto;
import esqeee.xieqing.com.eeeeee.doAction.api.Bule;
import esqeee.xieqing.com.eeeeee.doAction.api.Click;
import esqeee.xieqing.com.eeeeee.doAction.api.ClickText;
import esqeee.xieqing.com.eeeeee.doAction.api.Color;
import esqeee.xieqing.com.eeeeee.doAction.api.Condition;
import esqeee.xieqing.com.eeeeee.doAction.api.Encrypt;
import esqeee.xieqing.com.eeeeee.doAction.api.Fast;
import esqeee.xieqing.com.eeeeee.doAction.api.File;
import esqeee.xieqing.com.eeeeee.doAction.api.FlashClose;
import esqeee.xieqing.com.eeeeee.doAction.api.FlashOpen;
import esqeee.xieqing.com.eeeeee.doAction.api.For;
import esqeee.xieqing.com.eeeeee.doAction.api.Genster;
import esqeee.xieqing.com.eeeeee.doAction.api.Http;
import esqeee.xieqing.com.eeeeee.doAction.api.If;
import esqeee.xieqing.com.eeeeee.doAction.api.Image;
import esqeee.xieqing.com.eeeeee.doAction.api.InputText;
import esqeee.xieqing.com.eeeeee.doAction.api.Key;
import esqeee.xieqing.com.eeeeee.doAction.api.LongClick;
import esqeee.xieqing.com.eeeeee.doAction.api.Node;
import esqeee.xieqing.com.eeeeee.doAction.api.Paste;
import esqeee.xieqing.com.eeeeee.doAction.api.RandomSleep;
import esqeee.xieqing.com.eeeeee.doAction.api.SL;
import esqeee.xieqing.com.eeeeee.doAction.api.STRING;
import esqeee.xieqing.com.eeeeee.doAction.api.Setting;
import esqeee.xieqing.com.eeeeee.doAction.api.Swip;
import esqeee.xieqing.com.eeeeee.doAction.api.SwipLine;
import esqeee.xieqing.com.eeeeee.doAction.api.Toast;
import esqeee.xieqing.com.eeeeee.doAction.api.While;
import esqeee.xieqing.com.eeeeee.doAction.api.Wifi;
import esqeee.xieqing.com.eeeeee.fragment.VariableTableFragment;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.service.XQMainAccess;
import esqeee.xieqing.com.eeeeee.ui.Settings;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

public abstract class Base {

    public JSONBean checkVarExiets(JSONBean param, String var) {
        JSONBean variable = queryVariable(param.getString(var));
        if (variable == null){
            throw new RuntimeException("错误，找不到变量["+param.getString(var)+"]");
        }else {
            return variable;
        }
    }
    public JSONBean checkVarExiets(String var) {
        JSONBean param = getJsonBean().getJson("param");
        JSONBean variable = queryVariable(param.getString(var));
        if (variable == null){
            throw new RuntimeException("错误，找不到变量["+param.getString(var)+"]");
        }else {
            return variable;
        }
    }
    public int getMathResultOrThrow(String name){
        JSONBean param = getJsonBean().getJson("param");
        MathResult result = evalMath(param.getString(name));
        if (result.isError()){
            throw new RuntimeException("<error>计算表达式有误："+result.getException().getMessage());
        }
        return result.getResult();
    }
    public int getMathResultOrThrow(String name,int defult){
        JSONBean param = getJsonBean().getJson("param");
        MathResult result = evalMath(param.getString(name),defult);
        if (result.isError()){
            throw new RuntimeException("<error>计算表达式有误："+result.getException().getMessage());
        }
        return result.getResult();
    }
    public int getMathResultOrThrow(JSONBean param, String name){
        MathResult result = evalMath(param.getString(name));
        if (result.isError()){
            throw new RuntimeException("<error>计算表达式有误："+result.getException().getMessage());
        }
        return result.getResult();
    }
    public String getStringFromParam(String name) {
        return getString(getJsonBean().getJson("param").getString(name));
    }

    public void onError(Object e) {
        throw new RuntimeException(String.valueOf(e));
    }
    public Rect getRectByRect(JSONBean param){
        JSONBean var = checkVarExiets(param,"rectVar");
        JSONBean value = var.getJson("value");
        if (value == null || !(value instanceof JSONBean)){
            throw new RuntimeException("矩阵变量（"+param.getString("rectVar")+"）为空矩阵");
        }
        int left = value.getInt("left",-1);
        int top = value.getInt("top",-1);
        int width = value.getInt("width",-1);
        int height = value.getInt("height",-1);
        int right = left + width;
        int bottom = top + height;
        if (left == -1 || top == -1 || right == -1 || bottom == -1){
            return null;
        }
        return new Rect(left,top,right,bottom);
    }

    public org.opencv.core.Rect getRectByRect2(JSONBean param){
        JSONBean var = checkVarExiets(param,"rectVar");
        JSONBean value = var.getJson("value");
        if (value == null || !(value instanceof JSONBean)){
            throw new RuntimeException("矩阵变量（"+param.getString("rectVar")+"）为空矩阵");
        }
        int left = value.getInt("left",-1);
        int top = value.getInt("top",-1);
        int width = value.getInt("width",-1);
        int height = value.getInt("height",-1);
        if (left == -1 || top == -1 || width == -1 || height == -1){
            return null;
        }
        return new org.opencv.core.Rect(left,top,width,height);
    }

    public Rect getRect(JSONBean param){
        if (param.has("rectVar")){
            return getRectByRect(param);
        }
        int left = param.getInt("left",-1);
        int top = param.getInt("top",-1);
        int right = param.getInt("right",-1);
        int bottom = param.getInt("bottom",-1);
        if (left == -1 || top == -1 || right == -1 || bottom == -1){
            return null;
        }
        return new Rect(left,top,right,bottom);
    }


    private JSONBean jsonBean;
    private ActionRun.Block block;

    public ActionRun.Block getBlock() {
        return block;
    }

    public Base setBlock(ActionRun.Block block) {
        this.block = block;
        return  this;
    }

    public Base setJsonBean(JSONBean jsonBean) {
        this.jsonBean = jsonBean;
        return  this;
    }

    public JSONBean getJsonBean() {
        return jsonBean;
    }

    private ActionRun actionRun;
    public Base setRun(ActionRun self) {
        this.actionRun = self;
        return this;
    }

    public ActionRun getActionRun() {
        return actionRun;
    }


    public abstract boolean post(JSONBean param);
    public abstract int getType();
    public abstract String getName();


    private boolean isLocked = false;

    public boolean isLocked() {
        return isLocked;
    }
    public void lock(){
        isLocked = true;
    }

    public void unlock(){
        isLocked = false;
    }
    public void waitForUnlock(){
        while (isLocked){
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void setValue(JSONBean var, Object value){
        if (var == null || value == null){
            return;
        }
        var.put("value",String.valueOf(value));
    }
    public void setValue(JSONBean var, byte[] value){
        if (var == null || value == null){
            return;
        }
        var.put("value",value);
    }
    public void setValue(JSONBean var, boolean value){
        if (var == null){
            return;
        }
        var.put("value",value?"真":"假");
    }
    public void setValue(JSONBean var, JSONArrayBean value){
        if (var == null || value == null){
            return;
        }
        var.put("value",value);
    }

    public String[] findVariables(String find){
        return StringUtils.getSubStringArray(find,"{","}");
    }


    /*
    *   查询变量
    * */
    public JSONBean queryVariable(String name) {
        if (block.getVariables() == null){
            return null;
        }
        for (int i = 0; i<block.getVariables().length();i++){
            JSONBean v = block.getVariables().getJson(i);
            if (v.getString("name").equals(name)){
                return v;
            }
        }
        return null;
    }

    public String getString(String eval){
        String[] variables = findVariables(eval);
        for (String name : variables){
            JSONBean variable = VariableTableFragment.queryFinal(name);
            if (variable == null){
                variable = queryVariable(name);
                if (variable == null){
                    continue;
                }
            }
            eval = eval.replace("{"+name+"}",variable.getString("value"));
        }
        return eval;
    }


    public void waitForAccessbility(){
        if (AccessbilityUtils.getService() == null){
            AccessbilityUtils.toSetting();
        }
        while (AccessbilityUtils.getService() == null){
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public MathResult evalMath(String eval,int defult){
        if (TextUtils.isEmpty(eval)){
            MathResult result = new MathResult();
            result.setResult(defult);
            return result;
        }
        return evalMath(eval);
    }
    /*
     *   计算整数
     * */
    public MathResult evalMath(String eval){
        Scope scope = new Scope();
        String[] variables = findVariables(eval);
        MathResult result = new MathResult();
        for (String name : variables){
            JSONBean variable = VariableTableFragment.queryFinal(name);
            int type = 0;
            if (variable == null){
                variable = queryVariable(name);
                if (variable == null){
                    continue;
                }
            }
            type = variable.getInt("type");
            if (type!=VariableType.INT.ordinal() && type!=VariableType.STRING.ordinal()){
                continue;
            }

            //Variable var = scope.create(name);
            int var = 0;
            if (variable.getString("value").equals("Null")||variable.getString("value").equals("")){
                var = 0;
            }else{
                try {
                    var = Integer.valueOf(variable.getString("value"));
                }catch (NumberFormatException e){
                    var = 0;
                    return result;
                }
            }
            eval = eval.replace("{"+name+"}",var+"");
        }
        try {
            //RuntimeLog.log("计算："+eval);
            Expression expr = Parser.parse(eval, scope);
            result.setResult(expr.evaluate());
        } catch (ParseException e) {
            //RuntimeLog.log("计算错误："+e.getErrors());
            result.setException(e);
        }


        return result;
    }

    

    public void log(Object o){
        if (getActionRun()!=null){
            getActionRun().log(o);
        }
    }
}
