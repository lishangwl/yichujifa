package com.yicu.yichujifa.ui.colorpicker.views.color;

import android.annotation.TargetApi;
import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;


public class HorizontalSmoothColorView extends SmoothColorView {

    public HorizontalSmoothColorView(Context context) {
        super(context);
    }

    public HorizontalSmoothColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalSmoothColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public HorizontalSmoothColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = (int) (getMeasuredWidth() * 0.5625);
        setMeasuredDimension(getMeasuredWidth(), height);
    }
}
