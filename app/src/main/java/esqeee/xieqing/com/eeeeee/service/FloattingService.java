package esqeee.xieqing.com.eeeeee.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.xieqing.codeutils.util.ReflectUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import esqeee.xieqing.com.eeeeee.SplashActivity;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionFinished;
import esqeee.xieqing.com.eeeeee.action.ActionStarted;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.bean.FloatWindowAction;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class FloattingService extends Service {
    private Map<String, FloatWindow> windowMap = new HashMap<>();
    private static FloattingService service = new FloattingService();

    public static FloattingService getService() {
        return service;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        service = this;
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private WindowManager windowManager;

    @Override
    public void onCreate() {
        super.onCreate();
        service = this;
        RuntimeLog.log("FloattingService is started!");

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //设置点击跳转
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            String id = "yicu_float_id";
            String name = "yicu_float_name";
            String title = "一触即发运行中(前台服务保活)";
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
                mChannel.setSound(null, null);
                notificationManager.createNotificationChannel(mChannel);
                notification = new Notification.Builder(this)
                        .setChannelId(id)
                        .setContentTitle(title)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false)// 设置这个标志当用户单击面板就可以让通知将自动取消
                        .setOngoing(true)// true，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        //.setSmallIcon(getApplicationInfo().icon)
                        .build();
            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(title)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                        .setAutoCancel(false)// 设置这个标志当用户单击面板就可以让通知将自动取消
                        .setOngoing(true);// true，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                //.setSmallIcon(R.drawable.icon);
                notification = notificationBuilder.build();
            }
            startForeground(427, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void message(ActionStarted actionStarted) {
        Action action = actionStarted.getAction().getAction();
        actionStarted.getAction().start();


        BBS.report(action, 0, 0, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ActionFinished actionFinished) {
        BBS.report(actionFinished.getAction().getAction(), 1, (actionFinished.getAction().getRunTime()) / 1000, null);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void message(FloatWindowAction floatWindowAction) {
        FloatWindow floatWindow = floatWindowAction.getWindow();
        switch (floatWindowAction) {
            case ADD:
                if (hasWindow(floatWindow)) {
                    //已存在
                    closeWindow(floatWindow);
                    addWindow(floatWindow);
                } else {
                    addWindow(floatWindow);
                }
                break;
            case UPDATE:
                if (hasWindow(floatWindow)) {
                    windowManager.updateViewLayout(floatWindow.getView(), floatWindow.getParams());
                }
                break;
            case SHOW:
                showWindow(floatWindow);
                break;
            case HIDE:
                hideWindow(floatWindow);
                break;
            case CLOSE:
                if (hasWindow(floatWindow)) {
                    closeWindow(floatWindow);
                }
                break;
        }
    }

    private boolean hasWindow(FloatWindow floatWindow) {
        return windowMap.containsKey(floatWindow.getId());
    }

    public boolean hasWindow(String floatWindowId) {
        return windowMap.containsKey(floatWindowId);
    }

    public FloatWindow findWindow(String id) {
        return windowMap.get(id);
    }

    private void hideWindow(FloatWindow floatWindow) {
        if (floatWindow != null) {
            floatWindow.getView().setVisibility(View.GONE);
        }
    }

    private void showWindow(FloatWindow floatWindow) {
        if (floatWindow != null) {
            floatWindow.getView().setVisibility(View.VISIBLE);
        }
    }

    private void addWindow(FloatWindow floatWindow) {
        if (floatWindow != null) {
            try {
                if (floatWindow.isAllowMove()) {
                    floatWindow.getView().setOnTouchListener(new MoveTounListener(floatWindow));
                } else {
                    floatWindow.getView().setOnTouchListener(null);
                }
                windowManager.addView(floatWindow.getView(), floatWindow.getParams());
                windowMap.put(floatWindow.getId(), floatWindow);
            } catch (Exception e) {
                RuntimeLog.log("addFloatView error ：" + e.toString());
            }
        }
        Log.e("floating", windowMap.toString());
    }

    public void closeWindow(FloatWindow floatWindow) {
        if (floatWindow != null) {
            try {
                floatWindow = findWindow(floatWindow.getId());
                if (floatWindow == null) {
                    return;
                }
                windowManager.removeView(floatWindow.getView());
                windowMap.remove(floatWindow.getId());
            } catch (Exception e) {
                RuntimeLog.log("removeFloatView error ：" + e.toString());
            }
        }
    }

    public void closeWindow(String floatWindowId) {
        try {
            FloatWindow floatWindow = findWindow(floatWindowId);
            if (floatWindow == null) {
                return;
            }
            windowManager.removeView(floatWindow.getView());
            windowMap.remove(floatWindow.getId());
        } catch (Exception e) {
            RuntimeLog.log("removeFloatView error ：" + e.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RuntimeLog.log("FloattingService is destroied!");
    }


    public class MoveTounListener implements View.OnTouchListener {
        private FloatWindow floatWindow;

        public MoveTounListener(FloatWindow floatWindow) {
            this.floatWindow = floatWindow;
        }

        int lastX, lastY, dx, dy;
        int paramX, paramY;
        boolean isClick = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    paramX = floatWindow.getParams().x;
                    paramY = floatWindow.getParams().y;
                    if (floatWindow.getMoveListener() != null) {
                        floatWindow.getMoveListener().startMove(floatWindow, paramX, paramY);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!floatWindow.isAllowMove()) {
                        return false;
                    }
                    dx = (int) event.getRawX() - lastX;
                    dy = (int) event.getRawY() - lastY;
                    floatWindow.getParams().x = paramX + dx;
                    floatWindow.getParams().y = paramY + dy;
                    try {
                        windowManager.updateViewLayout(v, floatWindow.getParams());// 更新悬浮按钮位置
                    } catch (Exception e) {
                        RuntimeLog.log("moveFloat error :" + e.toString());
                    }
                    if (floatWindow.getMoveListener() != null) {
                        floatWindow.getMoveListener().onMove(floatWindow, dx, dy);
                    }
                    break;
                case MotionEvent.ACTION_UP://被弹起
                    if (Math.abs(dx) < 10 && Math.abs(dy) < 10) {
                        try {
                            View.OnClickListener clickListener = ReflectUtils.reflect(floatWindow.getView()).field("mListenerInfo").field("mOnClickListener").get();
                            clickListener.onClick(floatWindow.getView());
                        } catch (Exception e) {
                            RuntimeLog.log("window click error:" + e);
                        }
                        return true;
                    }
                    if (floatWindow.getMoveListener() != null) {
                        floatWindow.getMoveListener().endMove(floatWindow, floatWindow.getParams().x, floatWindow.getParams().y);
                    }
                    dx = 0;
                    dy = 0;
                    break;
            }
            return true;
        }
    }
}
