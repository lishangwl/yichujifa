package com.yicu.yichujifa.ui.colorpicker.views.picker;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Parcelable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yicu.yichujifa.ui.colorpicker.utils.ColorUtils;
import com.yicu.yichujifa.ui.widget.SeekBarBackgroundDrawable;
import com.yicu.yichujifa.ui.widget.SeekBarDrawable;

import java.util.Locale;

import esqeee.xieqing.com.eeeeee.R;

public class HSVPickerView extends PickerView {

    private AppCompatSeekBar hue, saturation, brightness;
    private TextView hueInt, saturationInt, brightnessInt;
    private boolean isTrackingTouch;

    public HSVPickerView(Context context) {
        super(context);
    }

    public HSVPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HSVPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public HSVPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        inflate(getContext(), R.layout.colorpicker_layout_hsv_picker, this);
        hue = findViewById(R.id.hue);
        hueInt = findViewById(R.id.hueInt);
        saturation = findViewById(R.id.saturation);
        saturationInt = findViewById(R.id.saturationInt);
        brightness = findViewById(R.id.brightness);
        brightnessInt = findViewById(R.id.brightnessInt);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.getId() == R.id.hue) {
                    hueInt.setText(String.format("%s", i));
                } else if (seekBar.getId() == R.id.saturation) {
                    saturationInt.setText(String.format(Locale.getDefault(), "%.2f", i / 255f));
                } else if (seekBar.getId() == R.id.brightness) {
                    brightnessInt.setText(String.format(Locale.getDefault(), "%.2f", i / 255f));
                }
                onColorPicked();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = false;
            }
        };

        hue.setOnSeekBarChangeListener(listener);
        saturation.setOnSeekBarChangeListener(listener);
        brightness.setOnSeekBarChangeListener(listener);
    }

    @Override
    protected SavedState newState(@Nullable Parcelable parcelable) {
        return new SavedState(parcelable);
    }

    @Override
    public void setColor(int color, boolean animate) {
        super.setColor(color, animate);
        SeekBar[] bars = new SeekBar[]{hue, saturation, brightness};
        float[] values = new float[3];
        Color.colorToHSV(color, values);
        values[1] *= 255;
        values[2] *= 255;

        for (int i = 0; i < bars.length; i++) {
            if (animate && !isTrackingTouch) {
                ObjectAnimator animator = ObjectAnimator.ofInt(bars[i], "progress", (int) values[i]);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.start();
            } else {
                bars[i].setProgress((int) values[i]);
            }
        }

        updateProgressBars();
    }

    @Override
    public int getColor() {
        int color = Color.HSVToColor(new float[]{hue.getProgress(), saturation.getProgress() / 255f, brightness.getProgress() / 255f});
        return (getColorAlpha() << 24) | (color & 0x00ffffff);
    }

    @NonNull
    @Override
    public String getName() {
        return getContext().getString(R.string.colorPickerDialog_hsv);
    }

    @Override
    public boolean isTrackingTouch() {
        return true;
    }

    @Override
    protected void onColorPicked() {
        super.onColorPicked();
        updateProgressBars();
    }
    public static void setProgressBarDrawable(AppCompatSeekBar seekbar, @NonNull Drawable drawable, @ColorInt int handleColor) {
        Drawable background = new SeekBarBackgroundDrawable(drawable.mutate().getConstantState().newDrawable());
        background.setAlpha(127);
        LayerDrawable layers = new LayerDrawable(new Drawable[]{new SeekBarDrawable(drawable), background});
        layers.setId(0, 16908301);
        layers.setId(1, 16908288);
        seekbar.setProgressDrawable(layers);
        if (Build.VERSION.SDK_INT >= 16) {
            seekbar.getThumb().setColorFilter(handleColor, PorterDuff.Mode.SRC_IN);
        }

    }
    private void updateProgressBars() {
        int neutralColor = ColorUtils.fromAttr(getContext(), R.attr.neutralColor,
                ColorUtils.fromAttrRes(getContext(), android.R.attr.textColorPrimary, R.color.colorPickerDialog_neutral));
        setProgressBarDrawable(hue, new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                ColorUtils.getColorWheelArr(saturation.getProgress() / 255f, brightness.getProgress() / 255f)
        ), neutralColor);

        setProgressBarDrawable(saturation, new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        Color.HSVToColor(new float[]{hue.getProgress(), 0, brightness.getProgress() / 255f}),
                        Color.HSVToColor(new float[]{hue.getProgress(), 1, brightness.getProgress() / 255f})
                }
        ), neutralColor);

        setProgressBarDrawable(brightness, new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        Color.HSVToColor(new float[]{hue.getProgress(), saturation.getProgress() / 255f, 0}),
                        Color.HSVToColor(new float[]{hue.getProgress(), saturation.getProgress() / 255f, 1})
                }
        ), neutralColor);
    }
}
