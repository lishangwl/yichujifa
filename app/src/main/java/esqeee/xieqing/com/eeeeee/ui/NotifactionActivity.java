package esqeee.xieqing.com.eeeeee.ui;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieqing.codeutils.util.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.manager.MessageManager;
import esqeee.xieqing.com.eeeeee.sql.model.Message;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class NotifactionActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recylerView)
    RecyclerView recyclerView;


    List<Message> datas = new ArrayList<>();
    Adapter adapter = new Adapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportToolBarWithBack(toolbar);

        stup();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newMessage(Message Message){
        datas.add(0,Message);
        adapter.notifyItemInserted(0);
    }

    void stup() {
        recyclerView.setLayoutManager(new MyLinearLayoutManager(self()));
        recyclerView.setAdapter(adapter);
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MessageManager.getManager().readNew();
    }

    void refresh(){
        datas.clear();
        datas.addAll(MessageManager.getManager().queryList());
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_notifaction;
    }


    class Adapter extends RecyclerView.Adapter<BaseViewHolder>{

        @NonNull
        @Override
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int target) {
            switch (target){

                case 1:
                    return new ReplyViewHolder(LayoutInflater.from(self()).inflate(R.layout.message_reply,parent,false));
                case 2:
                    return new NoHolder(LayoutInflater.from(self()).inflate(R.layout.message_no,parent,false));
            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {
            return datas.get(position).target;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            holder.bind(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }


    abstract class BaseViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.message_user)
        ImageView imageView;

        @BindView(R.id.message_nick)
        TextView nick;

        @BindView(R.id.message_time)
        TextView time;

        @BindView(R.id.message_title)
        TextView title;

        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        public void bind(Message message){
            BBS.attachIcon(imageView,message.fromUid);
            nick.setText(message.fromUserNick);
            time.setText(TimeUtils.getFriendlyTimeSpanByNow(message.time));
            title.setText(message.title);
            if (TextUtils.isEmpty(message.title)){
                title.setVisibility(View.GONE);
            }
            onBind(message,new JSONBean(message.message));

            itemView.setOnLongClickListener(v->{
                new AlertDialog.Builder(self()).setItems(new String[]{"删除"},(d,i)->{
                    if (i == 0){
                        message.delete();
                        datas.remove(getAdapterPosition());
                        adapter.notifyItemRemoved(getAdapterPosition());
                    }
                }).show();
                return true;
            });
        }


        abstract void onBind(Message message,JSONBean message2);
    }

    class ReplyViewHolder extends BaseViewHolder{

        @BindView(R.id.message_reply_content)
        TextView content;


        @BindView(R.id.message_reply_user)
        TextView user;

        public ReplyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onBind(Message msg,JSONBean message) {
            content.setText(message.getString("reply_content"));
            user.setText("@"+msg.toUserNick);

            itemView.setOnClickListener(v->{
                startActivity(new Intent(self(),AcrtleActivity.class).putExtra("aid",message.getInt("acrtle_id")));
            });
        }
    }

    class NoHolder extends BaseViewHolder{

        public NoHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onBind(Message msg,JSONBean message) {
        }
    }
}
