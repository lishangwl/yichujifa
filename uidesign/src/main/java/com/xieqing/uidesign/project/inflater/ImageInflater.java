package com.xieqing.uidesign.project.inflater;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.Log;

import com.xieqing.uidesign.R;
import com.xieqing.uidesign.project.inflater.widget.UIButton;
import com.xieqing.uidesign.project.model.Attr;

public class ImageInflater extends BaseLayoutInflater<AppCompatImageView>{
    @Override
    public String getName() {
        return "图片";
    }
    @Override
    public boolean bindView(AppCompatImageView view, Attr attrs) {

        for (String key:attrs.keySet()){
            switch (key){
                case "图片":
                    Bitmap bitmap = attrs.getBitmap(key);
                    Log.d("AttrTag","getBitmap :"+bitmap);
                    if (bitmap == null){
                        view.setImageResource(R.mipmap.ic_image);
                    }else{
                        view.setImageBitmap(bitmap);
                    }
                    break;

            }
        }
        return true;
    }

    @Override
    public AppCompatImageView createViewInstance(Context context,Attr attr) {
        return new AppCompatImageView(context);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-2");
        attr.put("图片","");
        attr.put(ATTR_NAME,getOneName("图片"));
        return attr;
    }
}
