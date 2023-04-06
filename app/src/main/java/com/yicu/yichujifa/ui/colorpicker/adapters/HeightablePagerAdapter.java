package com.yicu.yichujifa.ui.colorpicker.adapters;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.ViewGroup;

import com.yicu.yichujifa.ui.colorpicker.views.HeightableViewPager;

public abstract class HeightablePagerAdapter extends PagerAdapter implements HeightableViewPager.Heightable {

    private int position = -1;

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);

        if (position != this.position) {
            this.position = position;
            container.requestLayout();
        }
    }
}
