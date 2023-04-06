package esqeee.xieqing.com.eeeeee.helper;

import android.os.Environment;

public class PathHelper {
    public static String covert(String path){
        int indexOf = path.indexOf("YiChuJiFaProject");
        if (indexOf!=-1){
            String end = path.substring(indexOf);
            return Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+end;
        }
        return path;
    }
}
