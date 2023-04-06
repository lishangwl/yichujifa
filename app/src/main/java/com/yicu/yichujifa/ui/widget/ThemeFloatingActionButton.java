package com.yicu.yichujifa.ui.widget;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.util.AttributeSet;

public class ThemeFloatingActionButton extends FloatingActionButton {
    public ThemeFloatingActionButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyColor();
    }
    public ThemeFloatingActionButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyColor();
    }
    public ThemeFloatingActionButton(@NonNull Context context) {
        super(context);
        applyColor();
    }

    public void applyColor() {
        //TODO 没写
//        setBackgroundTintList( new ColorStateList(
//                new int[][]{
//                        new int[]{android.R.attr.state_checked},
//                        new int[]{}
//                },
//                new int[]{
//                        colorPrimaryDark,
//                        colorPrimary
//                }));
//        setRippleColor(colorPrimaryDark);
//        setSupportImageTintList(ColorStateList.valueOf(Theme.isColorDark(colorPrimary)? Color.WHITE : Color.DKGRAY));
    }
}
