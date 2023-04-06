package esqeee.xieqing.com.eeeeee.annotation;

import android.view.View;

import com.xieqing.codeutils.util.ToastUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClickListener extends Listener implements View.OnClickListener{

    public ClickListener(Object intansce, Method callback) {
        super(intansce, callback);
    }

    @Override
    public void onClick(View view) {
        try {
            callback.invoke(intansce,view);
        }   catch (IllegalAccessException e) {
            throw new RuntimeException("使用ViewListener标记的变量回调出错："+e.toString());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            //throw new RuntimeException("使用ViewListener标记的变量回调出错："+e.toString());
        }
    }

    public static void bind(Object intansce,String method,Object obj) throws NoSuchMethodException {
        Method callBack = obj.getClass().getDeclaredMethod(method,View.class);
        callBack.setAccessible(true);
        bindListener(intansce,callBack,"setOnClickListener",new ClickListener(obj,callBack),View.OnClickListener.class);
    }
}