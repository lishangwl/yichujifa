//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yicu.yichujifa.ui.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;

import com.xieqing.codeutils.util.SizeUtils;

public class SeekBarDrawable extends ClipDrawable {
    private float height = (float) SizeUtils.dp2px(2.0F);
    private Rect rect;

    public SeekBarDrawable(Drawable drawable) {
        super(drawable, 8388611, 1);
    }

    public void draw(Canvas canvas) {
        if (this.rect == null) {
            Rect bounds = this.getBounds();
            this.setBounds(this.rect = new Rect(bounds.left, (int)((float)bounds.centerY() - this.height / 2.0F), bounds.right, (int)((float)bounds.centerY() + this.height / 2.0F)));
        }

        super.draw(canvas);
    }

    public int getOpacity() {
        return -3;
    }
}
