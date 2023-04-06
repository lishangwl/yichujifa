//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yicu.yichujifa.ui.colorpicker.utils;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build.VERSION;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.yicu.yichujifa.ui.widget.SeekBarBackgroundDrawable;
import com.yicu.yichujifa.ui.widget.SeekBarDrawable;

public class SeekBarUtils {
    public SeekBarUtils() {
    }

    public static void setProgressBarColor(AppCompatSeekBar seekbar, @ColorInt int color) {
        seekbar.getProgressDrawable().setColorFilter(color, Mode.SRC_IN);
        if (VERSION.SDK_INT >= 16) {
            seekbar.getThumb().setColorFilter(color, Mode.SRC_IN);
        }

    }

    public static void setProgressBarDrawable(AppCompatSeekBar seekbar, @NonNull Drawable drawable, @ColorInt int handleColor) {
        Drawable background = new SeekBarBackgroundDrawable(drawable.mutate().getConstantState().newDrawable());
        background.setAlpha(127);
        LayerDrawable layers = new LayerDrawable(new Drawable[]{new SeekBarDrawable(drawable), background});
        layers.setId(0, 16908301);
        layers.setId(1, 16908288);
        seekbar.setProgressDrawable(layers);
        if (VERSION.SDK_INT >= 16) {
            seekbar.getThumb().setColorFilter(handleColor, Mode.SRC_IN);
        }

    }
}
