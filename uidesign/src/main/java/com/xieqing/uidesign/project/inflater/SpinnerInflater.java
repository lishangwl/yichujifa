package com.xieqing.uidesign.project.inflater;

import android.content.Context;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import com.xieqing.uidesign.project.inflater.widget.ArrayAdapter;
import com.xieqing.uidesign.project.model.Attr;

public class SpinnerInflater extends BaseLayoutInflater<AppCompatSpinner>{
    @Override
    public String getName() {
        return "下拉框";
    }
    @Override
    public boolean bindView(AppCompatSpinner view, Attr attrs) {
        for (String key:attrs.keySet()){
            switch (key){
                case "项目":
                    view.setAdapter(new ArrayAdapter<CharSequence>(view.getContext(),attrs.getString(key).split(",")));
                    break;
                case "当前选中项":
                    view.setSelection(attrs.getInt(key));
                    break;
            }
        }
        return true;
    }

    @Override
    public AppCompatSpinner createViewInstance(Context context,Attr attr) {
        return new AppCompatSpinner(context);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-2");
        attr.put("项目","");
        attr.put("当前选中项","0");
        attr.put(ATTR_NAME,getOneName("下拉框"));
        return attr;
    }
}
