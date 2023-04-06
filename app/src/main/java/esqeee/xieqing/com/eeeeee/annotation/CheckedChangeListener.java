package esqeee.xieqing.com.eeeeee.annotation;

import android.view.View;
import android.widget.CompoundButton;

import com.xieqing.codeutils.util.ToastUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CheckedChangeListener extends Listener implements CompoundButton.OnCheckedChangeListener {
    public CheckedChangeListener(Object intansce, Method callback) {
        super(intansce, callback);
    }

    public static void bind(Object intansce,String method,Object obj) throws NoSuchMethodException {
        Method callBack = obj.getClass().getDeclaredMethod(method,CompoundButton.class,boolean.class);
        callBack.setAccessible(true);
        bindListener(intansce,callBack,"setOnCheckedChangeListener",new CheckedChangeListener(obj,callBack),CompoundButton.OnCheckedChangeListener.class);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        try {
            callback.invoke(intansce,compoundButton,b);
        }   catch (IllegalAccessException e) {
            throw new RuntimeException("使用ViewListener标记的变量回调出错："+e.toString());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            //throw new RuntimeException("使用ViewListener标记的变量回调出错："+e.toString());
        }
    }
}
