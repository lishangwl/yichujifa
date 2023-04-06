package esqeee.xieqing.com.eeeeee.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieqing.codeutils.util.FileUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.manager.TimerTaskManager;
import esqeee.xieqing.com.eeeeee.sql.model.App;
import esqeee.xieqing.com.eeeeee.sql.model.AutoDo;
import esqeee.xieqing.com.eeeeee.sql.model.NotificationTextCheck;
import esqeee.xieqing.com.eeeeee.sql.model.System;
import esqeee.xieqing.com.eeeeee.sql.model.TextCheck;
import esqeee.xieqing.com.eeeeee.sql.model.TimeTask;

public class MyTimerActionAdapter extends RecyclerView.Adapter{
    private List<AutoDo> runs;
    private Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public MyTimerActionAdapter setRuns(List<AutoDo> runs) {
        this.runs = runs;
        return this;
    }

    public MyTimerActionAdapter(Context context, List<AutoDo> runs){
        this.runs = runs;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(context,R.layout.list_item2,null),true);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View convertView = holder.itemView;
        TextView tilte = ((TextView)convertView.findViewById(R.id.title));
        TextView creatTime = ((TextView)convertView.findViewById(R.id.creatTime));
        ImageView image = ((ImageView)convertView.findViewById(R.id.image));
        AutoDo task = runs.get(holder.getAdapterPosition());
        if (task == null){
            return;
        }

        if (task instanceof TimeTask){
            tilte.setText(FileUtils.getFileNameNoExtension(((TimeTask)task).path));
            image.setImageResource(R.drawable.action_2);
            creatTime.setText("下次运行："+dateFormat.format(new Date(getNextDayRealTime(((TimeTask)task).hour,((TimeTask)task).min))));
        }else if (task instanceof NotificationTextCheck){
            tilte.setText(FileUtils.getFileNameNoExtension(((NotificationTextCheck)task).path));
            image.setImageResource(R.drawable.action_3);
            creatTime.setText("检测通知栏文字："+Arrays.toString(((NotificationTextCheck)task).keys.split(",.split.,")));
        }else if (task instanceof TextCheck){
            tilte.setText(FileUtils.getFileNameNoExtension(((TextCheck)task).path));
            image.setImageResource(R.drawable.action_3);
            creatTime.setText("检测屏幕文字："+Arrays.toString(((TextCheck)task).keys.split(",.split.,")));
        }else if (task instanceof App){
            tilte.setText(FileUtils.getFileNameNoExtension(((App)task).path));
            image.setImageResource(R.drawable.action_1);
            creatTime.setText("打开窗口["+((App)task).activityName+"]");
        }else if (task instanceof System){
            tilte.setText(FileUtils.getFileNameNoExtension(((System)task).path));
            image.setImageResource(R.drawable.action_1);
            creatTime.setText(((System)task).actionName);
        }


        convertView.findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task instanceof TimeTask){
                    TimerTaskManager.getManager().deleteTask((TimeTask) task);
                    runs.remove(task);
                    notifyItemRemoved(holder.getAdapterPosition());
                    return;
                }
                task.delete();
                runs.remove(task);
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }
    public long getNextDayRealTime(int hour,int min){
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(Calendar.HOUR_OF_DAY,hour);
        gregorianCalendar.set(Calendar.MINUTE,min);
        gregorianCalendar.set(Calendar.SECOND,0);
        gregorianCalendar.set(Calendar.MILLISECOND,0);
        long nextTime = gregorianCalendar.getTimeInMillis();
        if (nextTime < java.lang.System.currentTimeMillis()) {
            //如果时间已经过  选第二天吧
            gregorianCalendar.add(Calendar.DATE, 1);
            nextTime = gregorianCalendar.getTimeInMillis();
        }
        return nextTime;
    }

    @Override
    public int getItemCount() {
        return runs.size();
    }
}
