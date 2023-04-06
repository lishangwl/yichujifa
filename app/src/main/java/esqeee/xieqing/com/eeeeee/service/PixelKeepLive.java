package esqeee.xieqing.com.eeeeee.service;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class PixelKeepLive {
    private static PixelKeepLive intansce;
    private FloatWindow floatWindow;
    public PixelKeepLive(Context context) {
        floatWindow = new FloatWindow.FloatWindowBuilder().id("keepLive")
                .move(false)
                .param(new FloatWindow.FloatWindowLayoutParamBuilder().type(MyApp.getFloatWindowType())
                .width(1).height(1).flags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE).format(-3).x(0).y(0).build())
                .with(new View(context))
                .build();
    }

    public void start(){
        //RuntimeLog.log("启动1像素保活 ----");
        floatWindow.add();
    }

    public void destory(){
        //RuntimeLog.log("关闭1像素保活 ----");
        floatWindow.close();
    }


    public static PixelKeepLive getIntansce(Context context) {
        if (intansce == null){
            intansce = new PixelKeepLive(context);
        }
        return intansce;
    }
}
