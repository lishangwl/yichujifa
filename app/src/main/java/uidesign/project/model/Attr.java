package uidesign.project.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uidesign.project.Utils.Dimetion;
import uidesign.project.exception.LayoutInflaterException;
import uidesign.project.inflater.BaseLayoutInflater;
import uidesign.project.inflater.WebViewInflater;

import static uidesign.project.inflater.BaseLayoutInflater.ATTR_BACKGROUND_IMAGE;
import static uidesign.project.inflater.WebViewInflater.ATTR_WEB_DISPATCH_BACK;
import static uidesign.project.inflater.WebViewInflater.ATTR_WEB_JSENABLE;

public class Attr {
    private Map<String,String> map = new HashMap<>();

    public void put(String name,String value){
        map.put(name,value);
    }

    public String getString(String name){
        if (!map.containsKey(name)){
            throw new LayoutInflaterException("没有该属性:" + name);
        }

        return map.get(name).replace("&lt;","<")
                .replace("&gt;",">")
                .replace("&#x000A;","\n")
                .replace("&quot;","\"")
                .replace( "&apos;","\'")
                .replace("&amp;","&");
    }

    public String getStringAndRemove(String name){
        if (!map.containsKey(name)){
            throw new LayoutInflaterException("没有该属性:" + name);
        }
        return map.remove(name);
    }


    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public String toString() {
        return map.toString();
    }

    public void copy(String attrs) {
        Map<String,String> map = new HashMap<>();
        String[] lines = attrs.split("\n");
        for (String line : lines){
            line = line.trim();
            String[] keyAndValue = line.split("=");
            if (keyAndValue.length != 2){
                if (keyAndValue.length == 1 && line.endsWith("=")){
                    map.put(keyAndValue[0].trim(),"");
                }else{
                    throw new RuntimeException("unknown parse lines : "+line);
                }
            }else{
                map.put(keyAndValue[0].trim(),keyAndValue[1]);
            }
        }

        this.map.clear();
        this.map.putAll(map);
    }

    public boolean getBoolean(String name) {
        String v = getString(name);
        if ("假".equals(v) || "false".equals(v)){
            return false;
        }
        if ("真".equals(v) || "true".equals(v)){
            return true;
        }

        throw new LayoutInflaterException("无法识别的逻辑型值："+v+"，只能为真和假");
    }

    public Bitmap getBitmap(String key) {
        String v = getString(key);
        if (!TextUtils.isEmpty(v)){
            File file = new File(v);
            if (file.exists()){
                return BitmapFactory.decodeFile(v);
            }
            return null;
        }else{
            return null;
        }
    }

    public int getColor(String string) {
        return Color.parseColor(getString(string));
    }

    public int getInt(String string) {
        return Integer.parseInt(getString(string));
    }

    public int getValue(String string) {
        string = getString(string);
        if (string.equals("-1")){
            return ViewGroup.LayoutParams.MATCH_PARENT;
        }else if (string.equals("-2")){
            return ViewGroup.LayoutParams.WRAP_CONTENT;
        }else{
            return Dimetion.parseToPixel(string);
        }
    }

    public int getGravity(String string) {
       int g = getInt(string);
       switch (g){
           case 0:
               return Gravity.LEFT | Gravity.TOP;
           case 1:
               return Gravity.CENTER | Gravity.TOP;
           case 2:
               return Gravity.RIGHT | Gravity.TOP;
           case 3:
               return Gravity.LEFT | Gravity.CENTER;
           case 4:
               return Gravity.CENTER | Gravity.CENTER;
           case 5:
               return Gravity.RIGHT | Gravity.CENTER;
           case 6:
               return Gravity.LEFT | Gravity.BOTTOM;
           case 7:
               return Gravity.CENTER | Gravity.BOTTOM;
           case 8:
               return Gravity.RIGHT | Gravity.BOTTOM;
       }
       throw new LayoutInflaterException("未知的对齐方式：" + g);
    }

    public int getInputType(String string) {
        int g = getInt(string);
        switch (g){
            case 0:
                return InputType.TYPE_CLASS_TEXT;
            case 1:
                return InputType.TYPE_CLASS_NUMBER;
            case 2:
                return -2;
        }
        throw new LayoutInflaterException("未知的输入方式：" + g);
    }

    public boolean has(String attrName) {
        return map.containsKey(attrName);
    }



    public static int getAttrType(String attrName){
        if (attrName.equals("选中")
                || attrName.equals(ATTR_WEB_JSENABLE)
                || attrName.equals(ATTR_WEB_DISPATCH_BACK)
                || attrName.equals(BaseLayoutInflater.ATTR_VISIBILITY)
                || attrName.equals(BaseLayoutInflater.ATTR_HTML)){
            return 1;
        }
        if (attrName.equals("字体颜色")
                || attrName.equals(BaseLayoutInflater.ATTR_BACKGROUND_COLOR)
                || attrName.equals(BaseLayoutInflater.ATTR_STATUS_BAR_COLOR)
                || attrName.equals(BaseLayoutInflater.ATTR_NAV_BAR_COLOR)){
            return 2;
        }
        if (attrName.equals(BaseLayoutInflater.ATTR_CLICK)
                || attrName.equals(BaseLayoutInflater.ATTR_ON_CREATE)
                || attrName.equals(WebViewInflater.ATTR_WEB_JS_EVENT)
                || attrName.equals(WebViewInflater.ATTR_WEB_ONLOAD_COMPLETE)
                || attrName.equals(WebViewInflater.ATTR_WEB_ONLOAD_FIAL)
                || attrName.equals(BaseLayoutInflater.ATTR_ON_DES) ){
            return 3;
        }
        if (attrName.equals(BaseLayoutInflater.ATTR_GRAVITY) ){
            return 4;
        }
        if (attrName.equals(BaseLayoutInflater.ATTR_IMAGE) ||attrName.equals(ATTR_BACKGROUND_IMAGE)){
            return 5;
        }
        if (attrName.equals("输入方式") ){
            return 6;
        }
        return 0;
    }

    public void toXml(StringBuilder stringBuilder) {
        for (String key : keySet()){
            stringBuilder.append(" ")
                    .append(key)
                    .append(" = ")
                    .append("\"")
                    .append(getString(key).replace("<","&lt;")
                        .replace(">","&gt;")
                        .replace("\n","&#x000A;")
                        .replace("\"","&quot;")
                        .replace("\'", "&apos;")
                        .replace("&", "&amp;"))
                    .append("\"");
        }
    }
}
