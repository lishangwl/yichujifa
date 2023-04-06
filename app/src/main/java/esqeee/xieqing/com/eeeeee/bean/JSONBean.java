package esqeee.xieqing.com.eeeeee.bean;

import com.xieqing.codeutils.util.ThreadUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class JSONBean {
    private JSONObject jsonObject;
    public JSONBean(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }
    public JSONBean(Map map){
        this.jsonObject = new JSONObject(map);
    }
    public JSONBean(){
        this.jsonObject = new JSONObject();
    }
    public JSONBean(String jsonObject){
        try {
            if (jsonObject == null){
                jsonObject = "";
            }
            this.jsonObject = new JSONObject(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONBean(JSONBean action) {
        try {

            this.jsonObject = new JSONObject(action.jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONBean optInt(String name,int value){
        return new JSONBean(new JSONObject()).put(name,value);
    }
    public static JSONBean optBoolean(String name,boolean value){
        return new JSONBean(new JSONObject()).put(name,value);
    }
    public static JSONBean optString(String name,String value){
        return new JSONBean(new JSONObject()).put(name,value);
    }

    public JSONObject toJson(){
        return jsonObject;
    }

    public JSONBean put(String name,int value){
        try {
            jsonObject.put(name,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    public JSONBean put(String name,boolean value){
        try {
            jsonObject.put(name,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }

    public JSONBean put(String name, Object value){
        try {
            jsonObject.put(name,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    public JSONBean put(String name, float value){
        try {
            jsonObject.put(name,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    public JSONBean put(String name, JSONArrayBean value){
        try {
            jsonObject.put(name,value.getObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    public JSONBean put(String name,String value){
        try {
            jsonObject.put(name,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    public JSONBean put(String name,JSONBean value){
        try {
            jsonObject.put(name,value.jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getString(String name){
        return getString(name,"");
    }
    public String getString(String name,String defaultString){
        if (jsonObject!=null){
            if (jsonObject.has(name)){
                try {
                    return jsonObject.getString(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return defaultString;
    }

    public JSONArrayBean getArray(String name){
        if (jsonObject!=null){
            if (jsonObject.has(name)){
                try {
                    return new JSONArrayBean(jsonObject.getJSONArray(name));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean getBoolean(String name) {
        return getBoolean(name,false);
    }

    public boolean getBoolean(String name, boolean b) {
        if (jsonObject!=null){
            if (jsonObject.has(name)){
                try {
                    return jsonObject.getBoolean(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return b;
    }

    public int getInt(String name) {
        return getInt(name,0);
    }

    public int getInt(String name, int i) {
        if (jsonObject!=null){
            if (jsonObject.has(name)){
                try {
                    return jsonObject.getInt(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return i;
    }

    public JSONBean getJson(String name) {
        if (jsonObject!=null){
            if (jsonObject.has(name)){
                try {
                    return new JSONBean(jsonObject.getJSONObject(name));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean has(String actions) {
        return jsonObject.has(actions);
    }

    public void remove(String condition) {
        if (jsonObject.has(condition)){
            jsonObject.remove(condition);
        }
    }

    public double getDouble(String name) {
        if (jsonObject!=null){
            if (jsonObject.has(name)){
                try {
                    return jsonObject.getDouble(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public Object get(String name) {
        if (jsonObject!=null){
            if (jsonObject.has(name)){
                try {
                    return jsonObject.get(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public long getLong(String name) {
        return getLong(name,0);
    }

    public long getLong(String name,long defult) {
        if (jsonObject!=null){
            if (jsonObject.has(name)){
                try {
                    return jsonObject.getLong(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return defult;
    }
}
