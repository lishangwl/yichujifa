package esqeee.xieqing.com.eeeeee.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xieqing.codeutils.util.FileUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.bean.DeleteAction;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.sql.model.NotificationItem;
import esqeee.xieqing.com.eeeeee.sql.model.NotificationItem_Table;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationManager{
    private static NotificationManager manager;
    private Context context;
    private android.app.NotificationManager notificationManager;
    public static final String CHANNEL_ACTION_ID = "yichujifa.action";
    public NotificationManager(Context context) {
        this.context = context;
        notificationManager = (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //创建一个场景业务
            NotificationChannel channel = new NotificationChannel(CHANNEL_ACTION_ID, "自动化通知栏显示", android.app.NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        EventBus.getDefault().register(this);

        IntentFilter intentFilter = new IntentFilter("delete.notification");
        intentFilter.addAction("xieqing.doAction");
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("delete.notification")){
                    int id = intent.getIntExtra("id",0);
                    NotificationItem notificationItem = queryItem(id);
                    if (notificationItem!=null){
                        notificationItem.delete();
                    }
                    notificationManager.cancel(id);
                }else{
                    ActionRunHelper.startAction(context,intent.getStringExtra("actionID"));
                }
            }
        },intentFilter);
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onDelete(DeleteAction action){
        NotificationItem task = queryItem(action.getAction());
        if (task != null){
            task.delete();
        }
    }
    public void requirAllNotification() {
        List<NotificationItem> items = getAll();
        Log.d("notificationManager",items.toString());
        for (NotificationItem item : items){
            //cannelNotification(new File(item.actionPath));
            postNotification(item);
        }
    }


    public List<NotificationItem> getAll(){
        return SQLite.select().from(NotificationItem.class).queryList();
    }

    public static NotificationManager getManager(Context context) {
        if (manager == null){
            manager = new NotificationManager(context);
        }
        return manager;
    }

    public long getCount(){
        return SQLite.select().from(NotificationItem.class).count();
    }

    public void cannelNotification(File actionFile){
        try {
            List<NotificationItem> items = queryAllItem(actionFile);
            for (NotificationItem item : items){
                notificationManager.cancel(item.index);
                Log.d("notificationManager","cancel = >" +item.index);
                item.delete();
            }
        }catch (Exception e){
            RuntimeLog.log(e);
        }
    }
    public List<NotificationItem> queryAllItem(File actionFile){
        return SQLite
                .select().from(NotificationItem.class)
                .where(NotificationItem_Table.actionPath.eq(actionFile.getAbsolutePath())).queryList();
    }
    public NotificationItem queryItem(File actionFile){
        return SQLite
                .select().from(NotificationItem.class)
                .where(NotificationItem_Table.actionPath.eq(actionFile.getAbsolutePath())).querySingle();
    }

    public NotificationItem queryItem(int id){
        return SQLite
                .select().from(NotificationItem.class)
                .where(NotificationItem_Table.index.eq(id)).querySingle();
    }

    public boolean actionIsShow(File actionFile){
        return SQLite
                .select().from(NotificationItem.class)
                .where(NotificationItem_Table.actionPath.eq(actionFile.getAbsolutePath())).count()>0;
    }

    public int postNotification(NotificationItem item){
        File actionFile = new File(item.actionPath);
        if (actionFile == null){
            return -1;
        }
        int noticID = item.index;
        Log.d("notificationManager","post = >" +item.index);
        String name = FileUtils.getFileNameNoExtension(actionFile);
        postNotification(noticID,name,name,buildRemoteView(noticID,actionFile));
        return noticID;
    }
    public int postNotification(File actionFile){
        if (actionFile == null){
            return -1;
        }
        String name = FileUtils.getFileNameNoExtension(actionFile);

        NotificationItem notificationItem = new NotificationItem();
        notificationItem.actionPath = actionFile.getAbsolutePath();
        notificationItem.insert();
        int noticID = notificationItem.index;

        postNotification(noticID,name,name,buildRemoteView(noticID,actionFile));
        return noticID;
    }
    public void postNotification(int id,String title, String content, @Nullable RemoteViews remoteViews){
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(context.getApplicationInfo().icon)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setContent(remoteViews)
                .setAutoCancel(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setChannelId(CHANNEL_ACTION_ID);
        }
        Notification notification = builder.build();
        notification.flags |= 32;
        notificationManager.notify(id, notification);
    }

    public RemoteViews buildRemoteView(int id,File actionFile){
        Intent doActionIntent = new Intent("xieqing.doAction");
        doActionIntent.putExtra("actionID", actionFile.getAbsolutePath());
        PendingIntent doAction = PendingIntent.getBroadcast(context, id, doActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent editIntent = new Intent(context, AddActivity.class);
        editIntent.putExtra("path", actionFile.getAbsolutePath());
        PendingIntent doEdit = PendingIntent.getActivity(context, id, editIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent doClose = PendingIntent.getBroadcast(context,id,new Intent("delete.notification").putExtra("id",id), PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_notic);
        remoteViews.setImageViewResource(R.id.id_img, R.drawable.icon);
        remoteViews.setTextViewText(R.id.id_name, FileUtils.getFileNameNoExtension(actionFile));

        remoteViews.setOnClickPendingIntent(R.id.id_auto_run, doAction);
        remoteViews.setOnClickPendingIntent(R.id.id_auto_edit, doEdit);
        remoteViews.setOnClickPendingIntent(R.id.id_auto_close, doClose);
        return remoteViews;
    }

}
