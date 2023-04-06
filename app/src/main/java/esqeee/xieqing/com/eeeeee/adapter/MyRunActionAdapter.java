package esqeee.xieqing.com.eeeeee.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionRun;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;

public class MyRunActionAdapter extends RecyclerView.Adapter{
    private List<ActionRun> runs;
    private Context context;
    public MyRunActionAdapter setRuns(List<ActionRun> runs) {
        this.runs = runs;
        return this;
    }

    public MyRunActionAdapter(Context context,List<ActionRun> runs){
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
        ActionRun run = runs.get(holder.getAdapterPosition());
        if (run == null){
            return;
        }
        ActionRun actionRun = runs.get(holder.getAdapterPosition());
        Action action = actionRun.getAction();
        tilte.setText(action.getTitle());
        image.setImageDrawable(action.getDrawable());
        creatTime.setText("已经运行"+runs.get(holder.getAdapterPosition()).getRunTime()/1000 +"秒");
        convertView.findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (runs.size() <= holder.getAdapterPosition()){
                    return;
                }
                runs.remove(run);
                ActionRunHelper.stopAction(actionRun);
            }
        });

    }

    @Override
    public int getItemCount() {
        return runs.size();
    }
}
