package esqeee.xieqing.com.eeeeee.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.yicu.yichujifa.GlobalContext;

public class ToastUtils {

    private static Handler sHandler;

    public static void shortShow(String msg) {
        toast(msg, Toast.LENGTH_SHORT);
    }

    public static void longShow(String msg) {
        toast(msg, Toast.LENGTH_LONG);
    }

    public static void toast(String msg) {
        toast(msg, Toast.LENGTH_SHORT);
    }

    public static void toast(String msg, int duration) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            show(msg, duration);
            return;
        }
        if (sHandler == null) {
            sHandler = new Handler(Looper.getMainLooper());
        }
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(msg, duration);
            }
        });
    }

    private static void show(String msg, int duration) {
        Toast.makeText(GlobalContext.getContext(), msg, duration).show();
    }

}

