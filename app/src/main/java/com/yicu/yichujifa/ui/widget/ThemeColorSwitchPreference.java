//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yicu.yichujifa.ui.widget;

import android.content.Context;
import android.preference.SwitchPreference;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.Switch;

import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.lang.reflect.Field;

public class ThemeColorSwitchPreference extends SwitchPreference{
    private View mCheckableView;

    @RequiresApi(
        api = 21
    )
    public ThemeColorSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    public ThemeColorSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public ThemeColorSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public ThemeColorSwitchPreference(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        ThemeManager.attachTheme(this);
    }

    protected void onBindView(View view) {
        super.onBindView(view);
        this.mCheckableView = view.findViewById(this.getSwitchWidgetId());
        this.applyColor();
    }

    private int getSwitchWidgetId() {
        try {
            Class c = Class.forName("com.android.internal.R$id");
            Field field = c.getField("switch_widget");
            return (Integer)field.get((Object)null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return 0;
        }
    }

    public void applyColor() {
        if (this.mCheckableView != null && this.mCheckableView instanceof Checkable) {
            if (this.mCheckableView instanceof Switch) {
                Switch switchView = (Switch)this.mCheckableView;
                ThemeManager.attachTheme(switchView);
            }

            if (this.mCheckableView instanceof SwitchCompat) {
                SwitchCompat switchView = (SwitchCompat)this.mCheckableView;
                ThemeManager.attachTheme(switchView);
            }
        }

    }
}
