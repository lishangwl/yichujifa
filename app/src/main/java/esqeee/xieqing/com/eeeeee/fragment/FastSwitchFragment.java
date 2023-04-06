package esqeee.xieqing.com.eeeeee.fragment;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyViewHolder;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class FastSwitchFragment extends BaseFragment{
    private RecyclerView listView;
    private RecyclerView.Adapter adapter;
    private List<DoActionBean> keys = new ArrayList<>();
    @Override
    public View getContentView(LayoutInflater inflater) {
        listView = new RecyclerView(getBaseActivity());
        return listView;
    }

    @Override
    protected void onFragmentInit() {
        addActionBean();
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyViewHolder(View.inflate(getBaseActivity(),R.layout.list_item,null),true);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                View view=holder.itemView;
                ((TextView)view.findViewById(R.id.title)).setText(keys.get(position).getActionName());
                ((ImageView)view.findViewById(R.id.image)).setImageDrawable(keys.get(position).getDrawable());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((ImageView)view.findViewById(R.id.image)).setImageTintList(null);
                }
                ((ImageView)view.findViewById(R.id.image)).setPadding(10,10,10,10);
                ((TextView)view.findViewById(R.id.creatTime)).setVisibility(View.GONE);

                view.setOnClickListener(view1 -> {
                    if (selectedListener!=null){
                        selectedListener.select(keys.get(position));
                    }
                });
            }

            @Override
            public int getItemCount() {
                return keys.size();
            }
        };

        listView.setAdapter(adapter);
        listView.setLayoutManager(new MyLinearLayoutManager(getBaseActivity()));
    }

    private void addActionBean() {
        keys.clear();
        keys.add(DoActionBean.OPEN_FLASH);
        keys.add(DoActionBean.CLOSE_FLASH);
        keys.add(DoActionBean.SYS_CLOSE_WIFI);
        keys.add(DoActionBean.SYS_OPEN_WIFI);
        keys.add(DoActionBean.SYS_CLOSE_BLUETOOTH);
        keys.add(DoActionBean.SYS_OPEN_BLUETOOTH);
        keys.add(DoActionBean.SYS_VIBRATE);
        keys.add(DoActionBean.SYS_NOT_VIBRATE);
        keys.add(DoActionBean.SYS_CLOSE_NOTIC);
        keys.add(DoActionBean.SYS_OPEN_NOTIC);
        keys.add(DoActionBean.SYS_SETTING);
        keys.add(DoActionBean.SYS_STROGE);
        keys.add(DoActionBean.SYS_APPMANGER);
        keys.add(DoActionBean.SYS_WIFISETTING);
        keys.add(DoActionBean.VBRITE);
        keys.add(DoActionBean.VBRITE_LONG);
        keys.add(DoActionBean.NOTICAFACTION);
    }

    private OnSelectedListener selectedListener;

    public void setSelectedListener(OnSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    public interface OnSelectedListener{
        void select(DoActionBean actionBean);
    }
}
