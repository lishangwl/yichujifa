package uidesign.project.inflater;

import android.content.Context;
import androidx.appcompat.widget.AppCompatSeekBar;

import uidesign.project.model.Attr;

public class SeekBarInflater extends BaseLayoutInflater<AppCompatSeekBar> {
    @Override
    public String getName() {
        return "进度条";
    }
    @Override
    public boolean bindView(AppCompatSeekBar view, Attr attrs) {
        for (String key:attrs.keySet()){
            switch (key){
                case "最大值":
                    view.setMax(attrs.getInt(key));
                    break;
                case "进度值":
                    view.setProgress(attrs.getInt(key));
                    break;
            }
        }
        return true;
    }

    @Override
    public AppCompatSeekBar createViewInstance(Context context, Attr attr) {
        return new AppCompatSeekBar(context);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-1");
        attr.put("最大值","100");
        attr.put("进度值","20");
        attr.put(ATTR_NAME,getOneName("进度条"));
        return attr;
    }
}
