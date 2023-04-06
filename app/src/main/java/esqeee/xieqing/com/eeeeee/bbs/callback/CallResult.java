package esqeee.xieqing.com.eeeeee.bbs.callback;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class CallResult extends JSONBean {
    public CallResult(String s){
        super(s);
    }
    public int getCode(){
        return getInt("code",-1);
    }

    public String getMessage(){
        return getString("msg");
    }

    public JSONBean getData(){
        return getJson("data");
    }
}
