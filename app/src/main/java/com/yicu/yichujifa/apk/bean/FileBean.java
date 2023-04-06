package com.yicu.yichujifa.apk.bean;

import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;

public abstract class FileBean {
    public abstract int size();
    public abstract String name(int position);
    public abstract Drawable icon(int position);
    public abstract String path(int position);
    public abstract boolean exits(String path);
    public abstract void delete(int position);
    public abstract void add(Object item);
}
