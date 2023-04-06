package com.xieqing.uidesign.project.inflater;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;

import com.xieqing.uidesign.project.inflater.widget.UIButton;
import com.xieqing.uidesign.project.model.Attr;

public class TextViewInflater extends BaseLayoutInflater<AppCompatTextView>{
    @Override
    public String getName() {
        return "标签";
    }
    @Override
    public boolean bindView(AppCompatTextView view, Attr attrs) {
        for (String key:attrs.keySet()){
            switch (key){
                case "标题":
                    view.setText(attrs.getString(key));
                    break;
                case "字体颜色":
                    view.setTextColor(attrs.getColor(key));
                    break;
            }
        }
        return true;
    }

    @Override
    public AppCompatTextView createViewInstance(Context context,Attr attr) {
        return new AppCompatTextView(context);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-2");
        attr.put("标题","标签");
        attr.put("字体颜色","#000000");
        attr.put(ATTR_NAME,getOneName("标签"));
        return attr;
    }
}
