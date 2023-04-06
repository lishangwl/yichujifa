package esqeee.xieqing.com.eeeeee.library;

import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import esqeee.xieqing.com.eeeeee.bean.Log;

public class RuntimeLog {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
    private static final int MAX_SIZE = 5242880;//5M
    private static int BUFF_SIZE = 0;
    private static List<Log> logs = new ArrayList<>();


    public static void i(String s) {
        log(Log.LogLevel.WARN, s);
    }

    public static void i(Throwable e) {
        String error = e.getMessage() + "\n";
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        int i = 0;
        for (StackTraceElement element : stackTraceElements) {
            if (i > 3) {
                break;
            }
            error += element.toString() + "\n";
            i++;
        }
        log(Log.LogLevel.WARN, error);
    }

    public static void e(Throwable e) {
        String error = e.getMessage() + "\n";
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        int i = 0;
        for (StackTraceElement element : stackTraceElements) {
            if (i > 3) {
                break;
            }
            error += element.toString() + "\n";
            i++;
        }
        log(Log.LogLevel.ERROR, error);
    }

    public static void e(Object message) {
        log(Log.LogLevel.ERROR, message.toString());
    }

    public static void d(Object message) {
        log(Log.LogLevel.DEBUG, message.toString());
    }

    public static void log(Object e) {
        if (e == null) {
            return;
        }
        log(Log.LogLevel.VERBOSE, e.toString());
    }

    public static void log(String tag, Object message) {
        log(Log.LogLevel.VERBOSE, tag + ":\t" + message);
    }

    public static void log(Throwable e) {
        e(e);
    }


    public synchronized static void log(Log.LogLevel level, CharSequence content) {
        try {
            if (content == null) {
                content = "";
            }
            if (BUFF_SIZE >= MAX_SIZE) {
                logs.clear();
                //System.gc();
                BUFF_SIZE = 0;
            }
            String time = TimeUtils.millis2String(System.currentTimeMillis(), simpleDateFormat);
            BUFF_SIZE += time.length() + content.length();

            Log log = new Log();
            log.level = level;
            log.time = time;
            log.content = content;
            logs.add(log);
        } catch (OutOfMemoryError e) {
            logs.clear();
            //System.gc();
            BUFF_SIZE = 0;
        }
    }


    public static List<Log> getLogs() {
        return logs;
    }

    public static void clear() {
        logs.clear();
    }
}
