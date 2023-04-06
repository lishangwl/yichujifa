//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yicu.yichujifa.ui.colorpicker.utils;

public class AnimatedInteger extends AnimatedValue<Integer> {
    public AnimatedInteger(int value) {
        super(value);
    }

    public Integer nextVal(long start, long duration) {
        int difference = (int)((double)((Integer)this.getTarget() - (Integer)this.val()) * Math.sqrt((double)(System.currentTimeMillis() - start) / (double)duration));
        return Math.abs((Integer)this.getTarget() - (Integer)this.val()) > 1 && System.currentTimeMillis() - start < duration ? (Integer)this.val() + ((Integer)this.getTarget() < (Integer)this.val() ? Math.min(difference, -1) : Math.max(difference, 1)) : (Integer)this.getTarget();
    }
}
