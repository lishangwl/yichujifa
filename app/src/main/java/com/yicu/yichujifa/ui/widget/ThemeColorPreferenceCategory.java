//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yicu.yichujifa.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yicu.yichujifa.ui.theme.ThemeManager;

public class ThemeColorPreferenceCategory extends PreferenceCategory {
    private TextView mTitleTextView;
    private int mColor = 0;

    @TargetApi(21)
    public ThemeColorPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    public ThemeColorPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public ThemeColorPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public ThemeColorPreferenceCategory(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        ThemeManager.attachTheme(this);
    }

    public void setTitleTextColor(int titleTextColor) {
        this.mColor = titleTextColor;
        if (this.mTitleTextView != null) {
            this.mTitleTextView.setTextColor(titleTextColor);
        }

    }

    public View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);
        this.mTitleTextView = (TextView) view.findViewById(android.R.id.title);
        if (this.mColor != 0) {
            this.mTitleTextView.setTextColor(this.mColor);
        }

        return view;
    }
}
