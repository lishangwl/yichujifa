package com.yicu.yichujifa.apk.bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import esqeee.xieqing.com.eeeeee.R;

public class XmlViewHolder {
    private View view;
    private Context context;

    public TextView name;
    public TextView path;
    public XmlViewHolder(Context context){
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.apk_xml_item,null);
        this.name = view.findViewById(R.id.name);

        this.path = view.findViewById(R.id.path);
    }


    public View getView() {
        return view;
    }


}
