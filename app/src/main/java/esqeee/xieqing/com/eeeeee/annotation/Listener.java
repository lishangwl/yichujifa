package esqeee.xieqing.com.eeeeee.annotation;

import android.view.View;

import com.xieqing.codeutils.util.ToastUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Listener {
    public Method callback;
    public Object intansce;
    public Listener(Object intansce,Method callback){
        this.callback = callback;
        this.intansce = intansce;

    }


    public static void bindListener(Object intansce,Method callback,String method,Object listener,Class<?>... param){
        try {
            Method listenerCall = intansce.getClass().getMethod(method,param);
            listenerCall.invoke(intansce,listener);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("使用ViewListener标记的变量设置listener出错："+e.toString());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("使用ViewListener标记的变量设置listener出错："+e.toString());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("使用ViewListener标记的变量设置listener出错："+e.toString());
        }

    }

}
