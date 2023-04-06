package com.yicu.yichujifa.apk.bean;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.xieqing.codeutils.util.FileUtils;
import com.yicu.yichujifa.GlobalContext;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class ResFile extends FileBean{
    @Override
    public int size() {
        return items.size();
    }

    @Override
    public String name(int position) {
        return items.get(position).name;
    }

    @Override
    public Drawable icon(int position) {
        String hz = FileUtils.getFileExtension(items.get(position).path);
        switch (hz){
            case "ycf":
                return GlobalContext.getContext().getResources().getDrawable(R.drawable.icon_56);
            case "zip":
                return GlobalContext.getContext().getResources().getDrawable(R.drawable.format_compress);
            case "jpeg":
            case "png":
            case "dddbbb":
                return new BitmapDrawable(BitmapFactory.decodeFile(items.get(position).path));
            default:
                return GlobalContext.getContext().getResources().getDrawable(R.drawable.format_other);
        }
    }

    @Override
    public String path(int position) {
        return items.get(position).path;
    }

    @Override
    public boolean exits(String path) {
        for (ResItem item : items){
            if (item.path.equals(path)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void delete(int position) {
        items.remove(position);
    }

    @Override
    public void add(Object item) {
        if (item instanceof ResItem){
            if (exits(((ResItem) item).path)){
                return;
            }
            items.add((ResItem) item);
        }
    }



    List<ResItem> items = new ArrayList<>();

    public ResFile(JSONArrayBean jsonArrayBean){
        if (jsonArrayBean == null){
            return;
        }
        for (int i = 0;i<jsonArrayBean.length();i++){
            items.add(new ResItem(jsonArrayBean.getJson(i)));
        }
    }

    public JSONArrayBean toJson(){
        JSONArrayBean arrayBean = new JSONArrayBean();
        for (ResItem item : items){
            arrayBean.put(item.toJson());
        }
        return arrayBean;
    }

    public ResItem getItem(int pos){
        return items.get(pos);
    }



    public static class ResItem{
        String name;
        String path;
        public String getName(){
            return name;
        }

        public String getPath(){
            return path;
        }


        public ResItem(JSONBean jsonBean){
            this.name = jsonBean.getString("name");
            this.path = jsonBean.getString("path");
        }


        public ResItem(String name ,String path){
            this.name = name;
            this.path = path;
        }

        JSONBean toJson(){
            JSONBean jsonBean = new JSONBean();
            jsonBean.put("name",name);
            jsonBean.put("path",path);
            return jsonBean;
        }
    }
}
