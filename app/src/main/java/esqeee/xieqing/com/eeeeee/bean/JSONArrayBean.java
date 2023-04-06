package esqeee.xieqing.com.eeeeee.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JSONArrayBean {
    private static Field values;
    static {
        try {
            values = JSONArray.class.getDeclaredField("values");
            values.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private JSONArray jsonObject;
    private List<Object> valuesObject;
    public JSONArray getObject() {
        return jsonObject;
    }


    public JSONArrayBean(){
        this.jsonObject = new JSONArray();
        initValue();
    }
    public JSONArrayBean(JSONArray jsonObject){
        this.jsonObject = jsonObject;
        initValue();
    }
    public JSONArrayBean(String json){
        try {
            this.jsonObject = new JSONArray(json);
            initValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JSONArrayBean(List<JSONBean> jsonBeans){
        this.jsonObject = new JSONArray();
        for (JSONBean jsonBean:jsonBeans){
            jsonObject.put(jsonBean.toJson());
        }
        initValue();
    }

    public JSONArrayBean(String[] strings){
        this.jsonObject = new JSONArray();
        for (String jsonBean:strings){
            jsonObject.put(jsonBean);
        }
        initValue();
    }
    private void initValue(){
        try {
            this.valuesObject = (List<Object>) values.get(this.jsonObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    public JSONArrayBean put(JSONBean jsonBean){
        this.jsonObject.put(jsonBean.toJson());
        return this;
    }

    public JSONArrayBean put(String string){
        this.jsonObject.put(string);
        return this;
    }

    public Object get(int index){
        try {
            return this.jsonObject.get(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String toString() {
        return jsonObject.toString();
    }

    public JSONArrayBean getJsonArray(int index){
        if (jsonObject!=null){
            if (jsonObject.length()>index){
                try {
                    return new JSONArrayBean(jsonObject.getJSONArray(index));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public JSONBean getJson(int index){
        if (jsonObject!=null){
            if (jsonObject.length()>index){
                try {
                    return new JSONBean(jsonObject.getJSONObject(index));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public List<JSONBean> toList(){
        List<JSONBean> list = new ArrayList<>();
        for (int i = 0 ; i < length() ; i++){
            list.add(getJson(i));
        }
        return list;
    }
    public int length(){
        return jsonObject!= null? jsonObject.length():0;
    }

    public JSONArrayBean put(JSONArrayBean jsonArrayBean) {
        jsonObject.put(jsonArrayBean.getObject());
        return this;
    }

    public JSONArrayBean put(int i, JSONBean jsonBean) {
        try {
            jsonObject.put(i,jsonBean.toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
    public JSONArrayBean add(JSONBean jsonBean){
        jsonObject.put(jsonBean.toJson());
        return this;
    }
    public JSONArrayBean add(JSONArrayBean jsonBean){
        for (int i=0;i<jsonBean.length();i++){
            jsonObject.put(jsonBean.getJson(i).toJson());
        }
        return this;
    }
    public JSONArrayBean add(int index,JSONBean jsonBean){
        valuesObject.add(index,jsonBean.toJson());
        return this;
    }

    public JSONArrayBean add(int index,Object object){//[5,2,6,8]
        valuesObject.add(index,object);
        return this;
    }

    public JSONArrayBean add(int index,JSONArrayBean object){//[5,2,6,8]
        valuesObject.add(index,object.jsonObject);
        return this;
    }

    public Object remove(int from) {
        return jsonObject.remove(from);
    }

    public boolean remove(Object from) {
        return valuesObject.remove(from);
    }


    public void clear() {
        this.jsonObject = new JSONArray();
    }
}
