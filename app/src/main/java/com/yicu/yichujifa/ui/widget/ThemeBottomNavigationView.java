package com.yicu.yichujifa.ui.widget;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.util.AttributeSet;



public class ThemeBottomNavigationView extends BottomNavigationView {
    public ThemeBottomNavigationView(@NonNull Context context) {
        super(context);
        applyColor();
    }
    public ThemeBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyColor();
    }
    public ThemeBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyColor();
    }

    public void applyColor() {
        //TODO 没写
//        setItemIconTintList(new ColorStateList(
//                new int[][]{
//                        new int[]{android.R.attr.state_checked},
//                        new int[]{}
//                },
//                new int[]{
//                        Theme.getDarkColor(colorPrimary),
//                        colorPrimary
//                }));
//        setItemTextColor(getItemIconTintList());
    }
}
