package com.liyi.flow.adapter;

import androidx.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseFlowHolder {
    private View convertView;
    private int mViewType;

    public BaseFlowHolder(final View convertView) {
        this.convertView = convertView;
    }

    public View getConvertView() {
        return convertView;
    }

    public <T extends View> T findViewById(@IdRes int viewId) {
        T view = (T) convertView.findViewById(viewId);
        return view;
    }

    public ImageView getImageView(@IdRes int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(@IdRes int viewId) {
        return findViewById(viewId);
    }

    public void setConvertView(View convertView) {
        this.convertView = convertView;
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int viewType) {
        this.mViewType = viewType;
    }
}
