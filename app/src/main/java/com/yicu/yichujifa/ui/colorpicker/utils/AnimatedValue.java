//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yicu.yichujifa.ui.colorpicker.utils;


import androidx.annotation.Nullable;

public abstract class AnimatedValue<T> {
    public static final long DEFAULT_ANIMATION_DURATION = 400L;
    private T targetValue;
    private T drawnValue;
    @Nullable
    private T defaultValue;
    private long start;

    public AnimatedValue(T value) {
        this.targetValue = this.drawnValue = value;
    }

    public void set(T value) {
        this.drawnValue = value;
    }

    public void setDefault(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setCurrent(T value) {
        this.drawnValue = this.targetValue = value;
    }

    public T val() {
        return this.drawnValue;
    }

    public T nextVal() {
        return this.nextVal(400L);
    }

    public T nextVal(long duration) {
        return this.nextVal(this.start, duration);
    }

    abstract T nextVal(long var1, long var3);

    public T getTarget() {
        return this.targetValue;
    }

    public T getDefault() {
        return this.defaultValue != null ? this.defaultValue : this.targetValue;
    }

    public boolean isTarget() {
        return this.drawnValue == this.targetValue;
    }

    public boolean isDefault() {
        return this.defaultValue != null && this.drawnValue == this.defaultValue;
    }

    public boolean isTargetDefault() {
        return this.defaultValue != null && this.targetValue == this.defaultValue;
    }

    public void toDefault() {
        if (this.defaultValue != null) {
            this.to(this.defaultValue);
        }

    }

    public void to(T value) {
        this.targetValue = value;
        this.start = System.currentTimeMillis();
    }

    public void next(boolean animate) {
        this.next(animate, 400L);
    }

    public void next(boolean animate, long duration) {
        this.drawnValue = animate ? this.nextVal(duration) : this.targetValue;
    }
}
