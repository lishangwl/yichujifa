package com.yicu.yichujifa.ui.theme;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.Preference;

import androidx.annotation.ColorInt;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.sevenheaven.segmentcontrol.SegmentControl;
import com.xieqing.codeutils.util.SPUtils;
import com.yicu.yichujifa.ui.widget.ThemeColorPreferenceCategory;
import com.yicu.yichujifa.ui.widget.ThemeColorSwitchPreference;

import esqeee.xieqing.com.eeeeee.widget.FloatingActionMenu;

public class ThemeManager {
    public enum Theme{
        DEFULT
    }
    private static int colorPrimary = 0xFF008B8B;

    static {
        colorPrimary = SPUtils.getInstance("theme").getInt("colorPrimary",0xFF008B8B);
    }
    public static void attachTheme(View... views){
        for (View view : views){
            attachTheme(view);
        }
    }

    public static void attachTheme(Preference preference){
        if (preference == null){
            return;
        }
        if (preference instanceof  ThemeColorPreferenceCategory){
            ((ThemeColorPreferenceCategory)preference).setTitleTextColor(colorPrimary);
        }else if (preference instanceof ThemeColorSwitchPreference){

        }
    }

    public static void setColorPrimary(int colorPrimary) {
        ThemeManager.colorPrimary = colorPrimary;
        SPUtils.getInstance("theme").put("colorPrimary",colorPrimary);
    }

    public static int getColorPrimary() {
        return colorPrimary;
    }

    public static void attachTheme(View view){
        if (view == null){return;}
        if (view instanceof FloatingActionMenu){
            ((FloatingActionMenu)view).setBackgroundColor(colorPrimary);
            return;
        }else if (view instanceof Toolbar){
            view.setBackgroundColor(colorPrimary);
            ((Toolbar) view).setTitleTextColor(isColorDark(colorPrimary)?Color.WHITE:Color.BLACK);
            return;
        }else if (view instanceof FloatingActionButton){
            ((FloatingActionButton)view).setBackgroundTintList(ColorStateList.valueOf(colorPrimary));
            return;
        }else if (view instanceof SegmentControl){
            ((SegmentControl)view).setColors(new ColorStateList(new int[][]{new int[]{-android.R.attr.state_selected},new int[]{android.R.attr.state_selected}},
                    new int[]{Color.WHITE,colorPrimary}));
            ((SegmentControl)view).setSelectedTextColors(new ColorStateList(new int[][]{new int[]{-android.R.attr.state_selected},new int[]{android.R.attr.state_selected}},
                    new int[]{colorPrimary,Color.WHITE}));
            return;
        }else if (view instanceof SwitchCompat){
            DrawableCompat.setTintList(((SwitchCompat)view).getThumbDrawable(), new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            colorPrimary,
                            0xfff1f1f1
                    }));
            DrawableCompat.setTintList(((SwitchCompat)view).getTrackDrawable(), new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            Color.argb(100,Color.red(colorPrimary),Color.green(colorPrimary),Color.blue(colorPrimary)),
                            0xffe4e4e4
                    }));
            return;
        }else if (view instanceof Switch){
            DrawableCompat.setTintList(((Switch)view).getThumbDrawable(), new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            colorPrimary,
                            0xfff1f1f1
                    }));
            DrawableCompat.setTintList(((Switch)view).getTrackDrawable(), new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            Color.argb(200,Color.red(colorPrimary),Color.green(colorPrimary),Color.blue(colorPrimary)),
                            0xffe4e4e4
                    }));
            return;
        }else if (view instanceof SeekBar){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DrawableCompat.setTintList(((SeekBar)view).getProgressDrawable(), new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_checked},
                                new int[]{}
                        },
                        new int[]{
                                colorPrimary,
                                colorPrimary
                        }));
                DrawableCompat.setTintList(((SeekBar)view).getThumb(), new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_checked},
                                new int[]{}
                        },
                        new int[]{
                                colorPrimary,
                                colorPrimary
                        }));
            }
            return;
        }else if (view instanceof Button){
            ((Button)view).setTextColor(Color.WHITE);
            ((Button)view).setBackgroundColor(colorPrimary);
            return;
        }else if (view instanceof ImageView){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((ImageView)view).setImageTintList(ColorStateList.valueOf(colorPrimary));
            }
            return;
        }else if (view instanceof RecyclerView){
            RecyclerView.Adapter adapter = ((RecyclerView)view).getAdapter();
            if (adapter!=null){
                adapter.notifyDataSetChanged();
            }
            return;
        }else if (view instanceof TextView){
            ((TextView)view).setTextColor(colorPrimary);
            return;
        }else{
            view.setBackgroundColor(colorPrimary);
            return;
        }
    }

    public static void attachTheme(TabLayout view){
        attachTheme(view,false);
    }

    public static void attachTheme(ActionBar actionBar){
        if (actionBar!=null){
            actionBar.setBackgroundDrawable(new ColorDrawable(colorPrimary));
        }
    }

    public static void attachTheme(TabLayout view,boolean isDark){
        if (!isDark){
            view.setTabTextColors(Color.BLACK,colorPrimary);
            view.setBackgroundColor(Color.WHITE);
            view.setSelectedTabIndicatorColor(colorPrimary);
        }else{
            view.setTabTextColors(Color.GRAY,Color.WHITE);
            view.setBackgroundColor(colorPrimary);
            view.setSelectedTabIndicatorColor(Color.WHITE);
        }
    }

    public static void attachTheme(Activity view){
        float[] HSV = new float[3];
        Color.colorToHSV(colorPrimary, HSV);
        HSV[2] = HSV[2] - 0.2f;
        if (HSV[2]<0){
            HSV[2] = 0;
        }
        setStatusBarColor(view,Color.HSVToColor(HSV));
        setNavigationBarColor(view,Color.HSVToColor(HSV));
    }

    public static boolean isColorDark(int color) {
        return getColorDarkness(color) > 0.4;
    }

    private static double getColorDarkness(int color) {
        if (color == Color.BLACK) return 1.0;
        else if (color == Color.WHITE || color == Color.TRANSPARENT) return 0.0;
        return (1 - (0.259 * Color.red(color) + 0.667 * Color.green(color) + 0.074 * Color.blue(color)) / 255);
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(-2147483648);
            window.setStatusBarColor(color);
        }
    }

    public static void setNavigationBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(-2147483648);
            window.setNavigationBarColor(color);
        }
    }
}
