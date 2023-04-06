package com.xieqing.uidesign.project.Utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

public class Dimetion {
    public static int parseToPixel(String dimension) {
        if (dimension.endsWith("dp")) {
            float pct = Float.parseFloat(dimension.substring(0, dimension.length() - 2));
            return dp2px(pct);
        }else if (dimension.endsWith("px")) {
            float pct = Float.parseFloat(dimension.substring(0, dimension.length() - 2));
            return (int) pct;
        }else{
            return (int) Float.parseFloat(dimension);
        }
    }


    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int screenHeight(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String toPx(float value) {
        return ((int)value)+"px";
    }
}
