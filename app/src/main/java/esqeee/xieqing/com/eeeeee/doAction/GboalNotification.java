package esqeee.xieqing.com.eeeeee.doAction;

import android.service.notification.StatusBarNotification;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class GboalNotification {
    public static class Notification {
        public CharSequence content;
        public CharSequence sender;
        public long time;

        public Notification(StatusBarNotification statusBarNotification) {
            this.content = statusBarNotification.getNotification().tickerText;
            this.sender = statusBarNotification.getPackageName();
            this.time = statusBarNotification.getPostTime();
        }

        @Override
        public String toString() {
            return hashCode() + "[" + sender + ":" + content + "," + time + "]";
        }
    }

    private static GboalNotification gboalNotification = new GboalNotification();

    public static GboalNotification getIntansce() {
        return gboalNotification;
    }

    private ArrayList<Notification> statusBarNotifications = new ArrayList<>();

    public GboalNotification() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn.getNotification().tickerText == null) {
            return;
        }
        Notification notification = new Notification(sbn);
        //RuntimeLog.log(notification);
        statusBarNotifications.add(notification);
    }

    private boolean isInit = false;

    public boolean isInit() {
        return isInit;
    }

    public void init() {
        EventBus.getDefault().register(this);
        isInit = true;
    }

    public void recyle() {
        statusBarNotifications.clear();
        EventBus.getDefault().unregister(this);
    }

    public Notification first() {
        return statusBarNotifications.size() > 0 ? statusBarNotifications.get(0) : null;
    }

    public Notification last() {
        return statusBarNotifications.size() > 0 ? statusBarNotifications.get(statusBarNotifications.size() - 1) : null;
    }
}
