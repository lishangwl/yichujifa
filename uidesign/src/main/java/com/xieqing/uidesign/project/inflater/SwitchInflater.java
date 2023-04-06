package com.xieqing.uidesign.project.inflater;

import android.content.Context;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import android.widget.Switch;

import com.xieqing.uidesign.project.model.Attr;

public class SwitchInflater extends BaseLayoutInflater<Switch>{
    @Override
    public String getName() {
        return "开关";
    }
    @Override
    public boolean bindView(Switch view, Attr attrs) {
        for (String key:attrs.keySet()){
            switch (key){
                case "标题":
                    view.setText(attrs.getString(key));
                    break;
                case "选中":
                    view.setChecked(attrs.getBoolean(key));
                    break;
                case "字体颜色":
                    view.setTextColor(attrs.getColor(key));
                    break;
            }
        }
        return true;
    }

    @Override
    public Switch createViewInstance(Context context,Attr attr) {
        return new Switch(context);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-2");
        attr.put("标题","开关");
        attr.put("选中","假");
        attr.put("字体颜色","#000000");
        attr.put(ATTR_NAME,getOneName("开关"));
        return attr;
    }
}
