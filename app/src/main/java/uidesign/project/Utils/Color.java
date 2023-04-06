package uidesign.project.Utils;

public class Color {
    public static String colorToHex(int color){
        int red = android.graphics.Color.red(color);
        int blue = android.graphics.Color.blue(color);
        int green = android.graphics.Color.green(color);
        int alpha = android.graphics.Color.alpha(color);
        return "#"+(toHex(alpha)+toHex(red)+toHex(green)+toHex(blue)).toUpperCase();
    }

    private static String toHex(int i){
        String s = Integer.toHexString(i);
        if (s.length() == 1){
            s="0"+s;
        }
        return s.toUpperCase();
    }
}
