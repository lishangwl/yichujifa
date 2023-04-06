package esqeee.xieqing.com.eeeeee.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

public class ActivityUtils {

    /**
     * 获取Activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

}
