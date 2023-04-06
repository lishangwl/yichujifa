package esqeee.xieqing.com.eeeeee.bean;

public class Log {
    public enum LogLevel{
        VERBOSE,
        DEBUG,
        WARN,
        ERROR
    }

    public LogLevel level = LogLevel.VERBOSE;
    public CharSequence content;
    public String time = "";
}
