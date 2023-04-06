package com.yicu.yichujifa.ui.colorpicker.interfaces;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public interface OnColorPickedListener<T> {
    void onColorPicked(@Nullable T pickerView, @ColorInt int color);
}
