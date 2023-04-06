package com.xieqing.uidesign.project.inflater;

import android.content.Context;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.xieqing.uidesign.project.inflater.widget.UIButton;
import com.xieqing.uidesign.project.model.Attr;

public class CheckBoxInflater extends BaseLayoutInflater<AppCompatCheckBox>{
    @Override
    public boolean bindView(AppCompatCheckBox view, Attr attrs) {
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
    public AppCompatCheckBox createViewInstance(Context context,Attr attr) {
        return new AppCompatCheckBox(context);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-2");
        attr.put("标题","选择框");
        attr.put("选中","假");
        attr.put("字体颜色","#000000");
        attr.put(ATTR_NAME,getOneName("选择框"));
        return attr;
    }

    @Override
    public String getName() {
        return "选择框";
    }
}
