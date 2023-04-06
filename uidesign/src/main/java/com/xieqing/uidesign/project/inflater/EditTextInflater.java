package com.xieqing.uidesign.project.inflater;

import android.content.Context;

import com.xieqing.uidesign.project.inflater.widget.UIButton;
import com.xieqing.uidesign.project.inflater.widget.UIEditText;
import com.xieqing.uidesign.project.model.Attr;

public class EditTextInflater extends BaseLayoutInflater<UIEditText>{
    @Override
    public boolean bindView(UIEditText view, Attr attrs) {
        for (String key:attrs.keySet()){
            switch (key){
                case "提示":
                    view.setHint(attrs.getString(key));
                    break;
                case "内容":
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
    public UIEditText createViewInstance(Context context,Attr attr) {
        return new UIEditText(context,attr);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-1");
        attr.put("提示","编辑框");
        attr.put("字体颜色","#000000");
        attr.put("内容","");
        attr.put(ATTR_NAME,getOneName("编辑框"));
        return attr;
    }
    @Override
    public String getName() {
        return "编辑框";
    }
}
