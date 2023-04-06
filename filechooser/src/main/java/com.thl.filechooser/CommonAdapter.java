//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thl.filechooser;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonAdapter<T> extends Adapter {
    public Context context;
    protected ArrayList<T> dataList = new ArrayList();
    private int resId;
    private OnItemClickListener itemClickListener;

    public CommonAdapter(Context context, ArrayList<T> dataList, int resId) {
        this.context = context;
        this.resId = resId;
        this.dataList = dataList;
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e("filechooser",position+"=>"+dataList.get(position));
        this.bindView(holder, this.dataList.get(position), position);
        if (this.itemClickListener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    CommonAdapter.this.itemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

    }

    public abstract void bindView(ViewHolder var1, T var2, int var3);

    public int getItemCount() {
        return this.dataList.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommonViewHolder(LayoutInflater.from(this.context).inflate(this.resId, parent, false));
    }

    public void add(T t) {
        this.dataList.add(t);
        this.notifyDataSetChanged();
    }

    public void add(T t, int index) {
        this.dataList.add(index, t);
        this.notifyDataSetChanged();
    }

    public void remove(T t) {
        this.dataList.remove(t);
        this.notifyDataSetChanged();
    }

    public void remove(int index) {
        this.dataList.remove(index);
        this.notifyDataSetChanged();
    }

    public void setData(List<T> dataList) {
        this.dataList = (ArrayList)dataList;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View var1, int var2);
    }
}
