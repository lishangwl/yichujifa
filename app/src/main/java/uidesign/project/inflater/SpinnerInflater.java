package uidesign.project.inflater;

import android.content.Context;
import androidx.appcompat.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;

import uidesign.project.model.Attr;

public class SpinnerInflater extends BaseLayoutInflater<AppCompatSpinner> {
    @Override
    public String getName() {
        return "下拉框";
    }
    @Override
    public boolean bindView(AppCompatSpinner view, Attr attrs) {
        for (String key:attrs.keySet()){
            switch (key){
                case "项目":
                    ArrayAdapter adapter = new ArrayAdapter(view.getContext(),
                            android.R.layout.simple_spinner_item,attrs.getString(key).split(","));

                    //设置下拉样式
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    view.setAdapter(adapter);
                    break;
                case "当前选中项":
                    view.postDelayed(()->{
                        view.setSelection(attrs.getInt(key),true);
                        view.invalidate();
                        view.requestLayout();
                    },300);
                    break;
            }
        }
        return true;
    }

    @Override
    public AppCompatSpinner createViewInstance(Context context, Attr attr) {
        return new AppCompatSpinner(context);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-2");
        attr.put("项目","下拉项1,下拉项2,下拉项3");
        attr.put("当前选中项","0");
        attr.put(ATTR_NAME,getOneName("下拉框"));
        return attr;
    }
}
