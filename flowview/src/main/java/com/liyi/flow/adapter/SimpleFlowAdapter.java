package com.liyi.flow.adapter;

import android.widget.TextView;

import com.liyi.view.R;

import java.util.List;

public class SimpleFlowAdapter<T, VH extends BaseFlowHolder> extends QuickFlowAdapter<T, VH> {
    private LoadData<T> mLoadData;

    public SimpleFlowAdapter() {

    }

    public SimpleFlowAdapter(List<T> list) {
        setData(list);
        addItemType(0, R.layout.flow_view_item_simple_text);
    }

    public void setLoadData(LoadData<T> loadData) {
        this.mLoadData = loadData;
    }

    @Override
    public int onHandleViewType(int position) {
        return 0;
    }

    @Override
    public void onHandleViewHolder(VH holder, int position, T item) {
        if (mLoadData != null) {
            mLoadData.onLoadData(position, item, holder.getTextView(R.id.tv_flow_view_simple_text));
        }
    }

    public interface LoadData<V> {
        void onLoadData(int position, V item, TextView textView);
    }
}
