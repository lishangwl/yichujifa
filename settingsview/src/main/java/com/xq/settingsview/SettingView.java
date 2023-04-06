package com.xq.settingsview;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import com.xq.settingsview.widget.BaseBean;
import com.xq.settingsview.widget.SettingAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingView extends RecyclerView {


    public SettingView(Context context) {
        super(context);
        init();
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    List<BaseBean> viewHolders;

    private void init() {
        viewHolders = new ArrayList<>();
        setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public void onLayoutChildren(Recycler recycler, State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                }catch (Exception e){
                    //防止bug崩溃
                }
            }
        });
        setAdapter(new SettingAdapter(viewHolders,getContext()));
    }

    public void refresh(){
        ((SettingAdapter)getAdapter()).notifyDataSetChanged();
    }

    public SettingView addItem(BaseBean baseBean){
        viewHolders.add(baseBean);
        return this;
    }


    public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }




}
