package esqeee.xieqing.com.eeeeee.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xieqing.codeutils.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import esqeee.xieqing.com.eeeeee.SplashActivity;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.bean.DeleteAction;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.sql.model.Message;
import esqeee.xieqing.com.eeeeee.sql.model.Message_Table;
import esqeee.xieqing.com.eeeeee.sql.model.TimeTask;
import esqeee.xieqing.com.eeeeee.sql.model.TimeTask_Table;
import esqeee.xieqing.com.eeeeee.user.User;


public class MessageManager {
    private static MessageManager manager;


    public static MessageManager getManager() {
        if (manager == null) {
            manager = new MessageManager();
        }
        return manager;
    }


    public void delete(long messageId){
        Message task = query(messageId);
        if (task!=null){
            task.delete();
        }
    }

    public List<Message> queryList(){
        return SQLite
                .select()
                .from(Message.class)
                .orderBy(Message_Table.time.desc())
                .queryList();
    }


    public Message query(long messageId){
        return SQLite
                .select().from(Message.class)
                .where(Message_Table.messageId.eq(messageId))
                .querySingle();
    }

    public void readNew(){
        BBS.get(BBS.host + "message?uid=" + User.getUser().getUid() + "&token=" + URLEncoder.encode(User.getUser().getToken()), new CallBack() {
            @Override
            public void callBack(CallResult result) {
                if (result.getCode() == 0){
                    JSONArrayBean data = result.getArray("data");
                    if (data!=null){
                        for (int i = 0;i<data.length();i++){
                            JSONBean item = data.getJson(i);
                            Message message = new Message();
                            message.messageId = item.getInt("id");
                            message.target = item.getInt("target");
                            message.fromUid = item.getInt("fromUid");
                            message.toUid = item.getInt("fromUid");
                            message.fromUserNick = item.getString("fromUserNick");
                            message.toUserNick = item.getString("toUserNick");
                            message.title = item.getString("title");
                            message.message = item.getString("message");
                            message.time = Long.parseLong(item.getString("time"));
                            message.insert();
                            EventBus.getDefault().post(message);
                        }
                    }
                }
            }
        });
    }
}
