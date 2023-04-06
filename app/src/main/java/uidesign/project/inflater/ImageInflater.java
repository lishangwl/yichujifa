package uidesign.project.inflater;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextUtils;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.yicu.yichujifa.GlobalContext;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import uidesign.project.model.Attr;

public class ImageInflater extends BaseLayoutInflater<AppCompatImageView> {
    @Override
    public String getName() {
        return "图片";
    }
    @Override
    public boolean bindView(AppCompatImageView view, Attr attrs) {

        for (String key:attrs.keySet()){
            switch (key){
                case "图片":
                    if (!TextUtils.isEmpty(attrs.getString(key))){
                        Glide.with(view.getContext())
                                .load(attrs.getString(key))
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        view.setImageBitmap(resource);
                                    }
                                });
                    }else{
                        view.setImageResource(R.mipmap.ic_image);
                    }
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
    public AppCompatImageView createViewInstance(Context context, Attr attr) {
        return new AppCompatImageView(context);
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","-2");
        attr.put("图片","");
        attr.put(ATTR_CLICK,"无");
        attr.put(ATTR_NAME,getOneName("图片"));
        return attr;
    }
}
