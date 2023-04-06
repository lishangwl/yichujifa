package com.xieqing.uidesign.project.inflater;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.xieqing.uidesign.project.inflater.widget.UIButton;
import com.xieqing.uidesign.project.model.Attr;

public class ButtonInflater extends BaseLayoutInflater<UIButton>{
    @Override
    public boolean bindView(UIButton view, Attr attrs) {
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
    public UIButton createViewInstance(Context context,Attr attr) {
        return new com.xieqing.uidesign.project.inflater.widget.UIButton(context,attr);
    }

    @Override
    public String getName() {
        return "按钮";
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-2");
        attr.put("标题","按钮");
        attr.put("字体颜色","#000000");

        attr.put(ATTR_NAME,getOneName("按钮"));
        return attr;
    }
}
