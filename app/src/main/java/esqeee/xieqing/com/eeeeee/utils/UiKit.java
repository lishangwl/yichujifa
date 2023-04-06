package esqeee.xieqing.com.eeeeee.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;

//import org.jdeferred.android.AndroidDeferredManager;

public class UiKit {

    //private static final AndroidDeferredManager gDM = new AndroidDeferredManager();
    private static final Handler gUiHandler = new Handler(Looper.getMainLooper());

    //public static AndroidDeferredManager defer() {
    //    return gDM;
    //}

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static void post(Runnable r) {
        gUiHandler.post(r);
    }

    public static void postDelayed(long delay, Runnable r) {
        gUiHandler.postDelayed(r, delay);
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Handler getUiHandler() {
        return gUiHandler;
    }

}
