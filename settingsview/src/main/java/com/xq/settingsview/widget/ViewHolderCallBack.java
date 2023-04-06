package com.xq.settingsview.widget;

import androidx.recyclerview.widget.RecyclerView;

public interface ViewHolderCallBack<T extends RecyclerView.ViewHolder> {
    void onPreLoadBind(T t);
    void onChanged(T t);
}
