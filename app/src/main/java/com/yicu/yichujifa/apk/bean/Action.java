package com.yicu.yichujifa.apk.bean;

import android.graphics.drawable.Drawable;

import com.yicu.yichujifa.GlobalContext;
import com.yicu.yichujifa.apk.scan.ActionScanner;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class Action extends FileBean{
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
        return GlobalContext.getContext().getResources().getDrawable(R.drawable.icon_56);
    }

    @Override
    public String path(int position) {
        return items.get(position).path;
    }

    @Override
    public boolean exits(String path) {
        for (ActionItem item : items){
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
        if (item instanceof ActionItem){
            if (exits(((ActionItem) item).path)){
                return;
            }
            items.add((ActionItem) item);
        }
    }



    List<ActionItem> items = new ArrayList<>();

    public Action(JSONArrayBean jsonArrayBean){
        if (jsonArrayBean == null){
            return;
        }
        for (int i = 0;i<jsonArrayBean.length();i++){
            items.add(new ActionItem(jsonArrayBean.getJson(i)));
        }
    }

    public JSONArrayBean toJson(){
        JSONArrayBean arrayBean = new JSONArrayBean();
        for (ActionItem item : items){
            arrayBean.put(item.toJson());
        }
        return arrayBean;
    }

    public ActionItem getItem(int adapterPosition) {
        return items.get(adapterPosition);
    }


    public static class ActionItem{
        String name;
        String path;
        ResFile resFile;
        public String getName(){
            return name;
        }

        public String getPath(){
            return path;
        }


        public ActionItem(JSONBean jsonBean){
            this.name = jsonBean.getString("name");
            this.path = jsonBean.getString("path");
            resFile = new ResFile(jsonBean.getArray("res"));
        }


        public ActionItem(String name ,String path){
            this.name = name;
            this.path = path;
            resFile = new ResFile(null);
            new ActionScanner(this,path).run();
        }

        public ResFile getResFile() {
            return resFile;
        }

        JSONBean toJson(){
            JSONBean jsonBean = new JSONBean();
            jsonBean.put("name",name);
            jsonBean.put("path",path);
            jsonBean.put("res",resFile.toJson());
            return jsonBean;
        }
    }
}
