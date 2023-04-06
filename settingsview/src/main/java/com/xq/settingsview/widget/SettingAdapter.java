package com.xq.settingsview.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.xq.settingsview.bean.SwtichBean;
import com.xq.settingsview.holder.SwitchViewHolder;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<BaseHolder> {
    List<BaseBean> viewHolders;
    Context context;
    public SettingAdapter(List<BaseBean> viewHolders, Context context){
        this.viewHolders = viewHolders;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseBean baseBean = viewHolders.get(viewType);
        BaseHolder holder = null;
        if (baseBean instanceof SwtichBean){
            holder = new SwitchViewHolder(context,parent, (SwtichBean) baseBean);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        holder.onBind();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        Log.d("xxxxxx",viewHolders.size()+"");
        return viewHolders.size();
    }

}
