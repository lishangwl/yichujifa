package com.yicu.yichujifa.apk.bean;

import com.yicu.yichujifa.apk.scan.XmlScanner;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class Xml {
    List<XmlItem> xmlItems = new ArrayList<>();
    public Xml(){

    }
    public Xml(JSONArrayBean jsonBean){
        if (jsonBean == null){
            return;
        }
        for (int i = 0;i<jsonBean.length();i++){
            xmlItems.add(new XmlItem(jsonBean.getJson(i)));
        }
    }

    public XmlItem getItem(int pos){
        return xmlItems.get(pos);
    }

    public int getItemCount(){
        return xmlItems.size();
    }

    public JSONArrayBean toJson() {
        JSONArrayBean arrayBean = new JSONArrayBean();
        for (XmlItem item : xmlItems){
            arrayBean.put(item.toJson());
        }
        return arrayBean;
    }

    public boolean addItem(XmlItem item) {
        if (exits(item)){
            return false;
        }
        return xmlItems.add(item);
    }

    public void remove(XmlItem item){
        xmlItems.remove(item);
    }

    private boolean exits(XmlItem i) {
        for (XmlItem item : xmlItems){
            if (i.path.equals(item.path)){
                return true;
            }
        }
        return false;
    }


    public static class XmlItem{
        String name;
        String path;
        Action action;
        ResFile resFile;
        public String getName(){
            return name;
        }

        public Action getAction() {
            return action;
        }

        public String getPath(){
            return path;
        }
        XmlItem(JSONBean jsonBean){
            this.name = jsonBean.getString("name");
            this.path = jsonBean.getString("path");
            this.action = new Action(jsonBean.getArray("action"));
            resFile = new ResFile(jsonBean.getArray("res"));
        }
        public XmlItem(String name ,String path){
            this.name = name;
            this.path = path;
            this.action = new Action(null);
            resFile = new ResFile(null);

            new XmlScanner(this,path).start();
        }

        public ResFile getResFile() {
            return resFile;
        }

        JSONBean toJson(){
            JSONBean jsonBean = new JSONBean();
            jsonBean.put("name",name);
            jsonBean.put("path",path);
            jsonBean.put("action",action.toJson());
            jsonBean.put("res",resFile.toJson());
            return jsonBean;
        }
    }
}
