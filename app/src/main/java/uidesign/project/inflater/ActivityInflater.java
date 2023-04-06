package uidesign.project.inflater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.ui.AcrtleActivity;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import uidesign.project.model.Attr;

import static uidesign.project.inflater.BaseLayoutInflater.ATTR_BACKGROUND_COLOR;
import static uidesign.project.inflater.BaseLayoutInflater.ATTR_BACKGROUND_IMAGE;
import static uidesign.project.inflater.BaseLayoutInflater.ATTR_NAV_BAR_COLOR;
import static uidesign.project.inflater.BaseLayoutInflater.ATTR_ON_CREATE;
import static uidesign.project.inflater.BaseLayoutInflater.ATTR_ON_DES;
import static uidesign.project.inflater.BaseLayoutInflater.ATTR_STATUS_BAR_COLOR;

public class ActivityInflater{
    public void inflate(Activity activity, ViewGroup root, Attr attrs) {
        for (String key:attrs.keySet()) {
            switch (key) {
                case ATTR_BACKGROUND_COLOR:
                    if (!TextUtils.isEmpty(attrs.getString(key))) {
                        root.setBackgroundColor(attrs.getColor(key));
                    }
                    break;
                case ATTR_STATUS_BAR_COLOR:
                    if (!TextUtils.isEmpty(attrs.getString(key))) {
                        ThemeManager.setStatusBarColor(activity, attrs.getColor(key));
                    }
                    break;
                case ATTR_NAV_BAR_COLOR:
                    if (!TextUtils.isEmpty(attrs.getString(key))) {
                        ThemeManager.setNavigationBarColor(activity, attrs.getColor(key));
                    }
                    break;
                case ATTR_BACKGROUND_IMAGE:

                    if (TextUtils.isEmpty(attrs.getString(key))){
                        root.setBackground(null);
                    }else{
                        Glide.with(activity)
                                .load(attrs.getString(key))
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        root.setBackground(new BitmapDrawable(resource));
                                    }
                                });
                    }
                    break;
            }
        }
    }
    public static Attr baseAttr = getAttr();
    public static Attr getAttr(){
        Attr baseAttr = new Attr();
        {
            baseAttr.put(ATTR_BACKGROUND_COLOR,"");
            baseAttr.put(ATTR_STATUS_BAR_COLOR,"");
            baseAttr.put(ATTR_NAV_BAR_COLOR,"");
            baseAttr.put(ATTR_ON_CREATE,"无");
            baseAttr.put(ATTR_ON_DES,"无");
            baseAttr.put(ATTR_BACKGROUND_IMAGE,"");
        }
        return baseAttr;
    }
}
