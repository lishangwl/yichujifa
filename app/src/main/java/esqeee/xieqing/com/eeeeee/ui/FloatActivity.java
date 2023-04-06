package esqeee.xieqing.com.eeeeee.ui;

import android.app.AppOpsManager;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Method;

import butterknife.ButterKnife;
import esqeee.xieqing.com.eeeeee.MyApp;


public class FloatActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    @Override
    public <T extends View> T findViewById(@IdRes int id) {
        return view.findViewById(id);
    }


    View view = null;
    @Override
    public void attachView() {
        if (getContentLayout() !=0 ){
            view = getLayoutInflater().inflate(getContentLayout(),null);
        }else{
            view =getContentView();
        }

        if (ensureAppOpsPersmission()){
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            layoutParams.type = MyApp.getFloatWindowType();
            layoutParams.x = 0;
            layoutParams.y = 0;
            layoutParams.width = 800;
            layoutParams.height = 1000;
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
            getWindowManager().addView(view,layoutParams);
            ButterKnife.bind(this,view);
        }else{
            setContentView(view);
            ButterKnife.bind(this);
        }
    }


    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @return true 允许 false禁止
     */
    public boolean ensureAppOpsPersmission() {
        if (Build.VERSION.SDK_INT >= 23){
            return Settings.canDrawOverlays(this);
        }
        try {
            Object object = getSystemService(APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
