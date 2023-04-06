package uidesign.project.inflater;

import android.content.Context;
import androidx.appcompat.widget.Toolbar;

import uidesign.project.model.Attr;

public class ToolbarInflater extends BaseLayoutInflater<Toolbar> {
    @Override
    public boolean bindView(Toolbar view, Attr attrs) {
        for (String key:attrs.keySet()){
            switch (key){
                case "标题":
                    view.setTitle(attrs.getString(key));
                    break;
                case "字体颜色":
                    view.setTitleTextColor(attrs.getColor(key));
                    break;
            }
        }
        return true;
    }

    @Override
    public Toolbar createViewInstance(Context context, Attr attr) {
        return new Toolbar(context);
    }

    @Override
    public String getName() {
        return "标题栏";
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-1");
        attr.put("标题","我的应用");
        attr.put("字体颜色","#ffffff");
        attr.put(ATTR_ELEVATION,"4dp");
        attr.put(ATTR_BACKGROUND_COLOR,"#ff008b8b");
        attr.put(ATTR_NAME,getOneName("标题栏"));
        return attr;
    }
}
