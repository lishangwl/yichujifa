package com.xieqing.uidesign.project.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.ViewGroup;

import com.xieqing.uidesign.project.Utils.Dimetion;
import com.xieqing.uidesign.project.exception.LayoutInflaterException;

import java.io.File;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class Attr {
    private Map<String,String> map = new HashMap<>();

    public void put(String name,String value){
        map.put(name,value);
    }

    public String getString(String name){
        if (!map.containsKey(name)){
            throw new LayoutInflaterException("没有该属性:" + name);
        }
        return map.get(name);
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
            }else{
                throw new LayoutInflaterException("找不到图片路径："+v);
            }
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

    public boolean has(String attrName) {
        return map.containsKey(attrName);
    }



    public static int getAttrType(String attrName){
        if (attrName.equals("选中")){
            return 1;
        }
        if (attrName.equals("字体颜色") || attrName.equals("背景颜色") ){
            return 2;
        }
        return 0;
    }

    public void toXml(StringBuilder stringBuilder) {
        for (String key : keySet()){
            stringBuilder.append(" ")
                    .append(key)
                    .append(" = ")
                    .append("\"")
                    .append(getString(key))
                    .append("\"");
        }
    }
}
