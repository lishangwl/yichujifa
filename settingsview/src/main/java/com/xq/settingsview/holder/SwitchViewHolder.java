package com.xq.settingsview.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.xq.settingsview.R;
import com.xq.settingsview.bean.SwtichBean;
import com.xq.settingsview.widget.BaseHolder;

public class SwitchViewHolder extends BaseHolder {

    SwtichBean bean;
    public SwitchViewHolder(Context context,ViewGroup parent,SwtichBean bean){
        super(LayoutInflater.from(context).inflate(R.layout.holder_switch,parent,false));
        this.bean = bean;
    }


    @Override
    public void onBind() {
        ((TextView)itemView.findViewById(R.id.holder_title)).setText(bean.getTitle());
        ((TextView)itemView.findViewById(R.id.holder_summay)).setText(bean.getSummay());
        ((Switch)itemView.findViewById(R.id.holder_switch)).setChecked(bean.isChecked());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.setChecked(!bean.isChecked());
                bean.getCallBack().onChanged(bean);
                ((Switch)itemView.findViewById(R.id.holder_switch)).setChecked(bean.isChecked());
            }
        });
    }
}
