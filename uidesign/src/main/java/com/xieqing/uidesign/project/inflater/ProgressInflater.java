package com.xieqing.uidesign.project.inflater;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.xieqing.uidesign.project.model.Attr;

public class ProgressInflater extends BaseLayoutInflater<ProgressBar>{
    @Override
    public String getName() {
        return "加载圈";
    }
    @Override
    public boolean bindView(ProgressBar view, Attr attrs) {
        for (String key:attrs.keySet()){
            switch (key){
            }
        }
        return true;
    }

    @Override
    public ProgressBar createViewInstance(Context context,Attr attr) {
        return new ProgressBar(context);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-2");
        attr.put("高度","-2");
        attr.put(ATTR_NAME,getOneName("加载圈"));
        return attr;
    }
}
