package uidesign.project.inflater;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;

import uidesign.project.inflater.widget.UIEditText;
import uidesign.project.model.Attr;

public class EditTextInflater extends BaseLayoutInflater<UIEditText> {
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
                case "字体大小":
                    view.setTextSize(attrs.getValue(key));
                    break;
                case "对齐方式":
                    view.setGravity(attrs.getGravity(key));
                    break;
                case "输入方式":
                    int type = attrs.getInputType(key);
                    if (type == -2){
                        view.setInputType(1);
                        view.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }else{
                        view.setInputType(type);
                        view.setTransformationMethod(null);
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public UIEditText createViewInstance(Context context, Attr attr) {
        return new UIEditText(context,attr);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-1");
        attr.put("提示","编辑框");
        attr.put("字体颜色","#000000");
        attr.put("内容","");
        attr.put("字体大小","14");
        attr.put(ATTR_NAME,getOneName("编辑框"));
        attr.put("对齐方式","0");
        attr.put("输入方式","0");
        return attr;
    }
    @Override
    public String getName() {
        return "编辑框";
    }
}
