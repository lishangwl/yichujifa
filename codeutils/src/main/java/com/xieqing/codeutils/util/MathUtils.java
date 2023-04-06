package com.xieqing.codeutils.util;

import java.text.DecimalFormat;
import java.util.*;
public class MathUtils {

    /*
    * 取随机数
    * */
    public static int random(int min,int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    /*
     * 格式化浮点数，保留小数点几位
     * */
    public static float decimal(float number) {
        DecimalFormat df  = new DecimalFormat("#.00");
        return Float.parseFloat(df.format(number));
    }
}
