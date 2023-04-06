package esqeee.xieqing.com.eeeeee.helper;

import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;

import com.xieqing.codeutils.util.BarUtils;
import com.xieqing.codeutils.util.LogUtils;
import com.xieqing.codeutils.util.ThreadUtils;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

import static android.content.Context.WINDOW_SERVICE;

public class RectFloatHelper {
    private static RectFloatHelper helper;
    private Context context;
    private WindowManager mMWindowManager;
    private WindowManager.LayoutParams mWmParams;
    private View mView;
    public static RectFloatHelper getHelper(Context context) {
        if (helper == null){
            helper = new RectFloatHelper(context);
        }
        return helper;
    }
    private RectFloatHelper(Context context){
        this.context = context;
    }

    public void showRectView(Rect rect) {
        showRectView(rect.left,rect.top,rect.width(),rect.height());
    }
    public void showRectView(int x0, int y0, int xlegth, int ylength) {
        RuntimeLog.log("showRect("+x0+","+y0+","+xlegth+","+ylength+")");
        try {
            if (this.mView == null) {
                this.mView = new View(context);
                this.mView.setBackgroundResource(R.drawable.red_border);
            }
            if (this.mMWindowManager == null) {
                this.mMWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            }
            if (this.mWmParams == null) {
                this.mWmParams = new WindowManager.LayoutParams();
                this.mWmParams.type= MyApp.getFloatWindowType();
                this.mWmParams.format = -3;
                this.mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;
                this.mWmParams.gravity = 51;
            }
            this.mWmParams.x = x0 ;
            this.mWmParams.y = (y0) - BarUtils.getStatusBarHeight();
            this.mWmParams.width = xlegth;
            this.mWmParams.height = ylength;
            //removeRectView();
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mMWindowManager.addView(mView, mWmParams);
                    }catch (Exception e){
                        RuntimeLog.log("显示矩形失败："+e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeRectView() {
        try {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try{
                        mMWindowManager.removeView(mView);
                    }catch (Exception e){
                        RuntimeLog.log("关闭矩形失败："+e);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
