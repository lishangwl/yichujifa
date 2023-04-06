package uidesign.project.inflater;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Html;

import com.yicu.yichujifa.GlobalContext;

import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import uidesign.project.model.Attr;

public class TextViewInflater extends BaseLayoutInflater<AppCompatTextView> {
    @Override
    public String getName() {
        return "标签";
    }
    @Override
    public boolean bindView(AppCompatTextView view, Attr attrs) {
        for (String key:attrs.keySet()){
            switch (key){
                case "标题":
                    view.setText(attrs.getBoolean(ATTR_HTML)? Html.fromHtml(attrs.getString(key)) :attrs.getString(key));
                    break;
                case "字体颜色":
                    view.setTextColor(attrs.getColor(key));
                    break;
                case "字体大小":
                    view.setTextSize(attrs.getValue(key));
                    break;
                case "对齐方式":
                    view.setGravity(attrs.getGravity(key));
                    break;
                case ATTR_CLICK:
                    view.setOnClickListener(v->{
                        String value = attrs.getString(ATTR_CLICK);
                        if (!value.equals("") &&!value.equals("无")){
                            ActionRunHelper.startAction(GlobalContext.getContext(),value.trim());
                        }
                    });
                    break;
            }
        }
        return true;
    }

    @Override
    public AppCompatTextView createViewInstance(Context context, Attr attr) {
        return new AppCompatTextView(context);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-2");
        attr.put("标题","标签");
        attr.put("字体颜色","#000000");
        attr.put("字体大小","14");
        attr.put(ATTR_NAME,getOneName("标签"));
        attr.put("对齐方式","0");
        attr.put(ATTR_CLICK,"无");
        attr.put(ATTR_HTML,"假");
        return attr;
    }
}
