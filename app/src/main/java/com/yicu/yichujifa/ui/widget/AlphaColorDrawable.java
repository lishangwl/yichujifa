//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yicu.yichujifa.ui.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import com.xieqing.codeutils.util.SizeUtils;

public class AlphaColorDrawable extends Drawable {
    private Paint bitmapPaint;
    private Paint paint;
    public static Bitmap tile;

    public AlphaColorDrawable(@ColorInt int color) {
        int size = SizeUtils.dp2px(8.0F);
        this.bitmapPaint = new Paint();
        this.bitmapPaint.setColor(-3355444);
        if (tile == null || tile.isRecycled()) {
            tile = Bitmap.createBitmap(size * 4, size * 4, Config.RGB_565);
            Canvas canvas = new Canvas(tile);
            canvas.drawColor(-1);

            for(int x = 0; x < canvas.getWidth(); x += size) {
                for(int y = x % (size * 2) == 0 ? 0 : size; y < canvas.getWidth(); y += size * 2) {
                    canvas.drawRect((float)x, (float)y, (float)(x + size), (float)(y + size), this.bitmapPaint);
                }
            }
        }

        this.paint = new Paint();
        this.paint.setColor(color);
    }

    public void draw(@NonNull Canvas canvas) {
        Rect b = this.getBounds();
        if (this.paint.getAlpha() < 255) {
            for(int x = b.left; x < b.right; x += tile.getWidth()) {
                for(int y = b.top; y < b.bottom; y += tile.getHeight()) {
                    canvas.drawBitmap(tile, (float)x, (float)y, this.bitmapPaint);
                }
            }
        }

        canvas.drawRect((float)b.left, (float)b.top, (float)b.right, (float)b.bottom, this.paint);
    }

    public void setAlpha(int alpha) {
        this.bitmapPaint.setColor(ColorUtils.setAlphaComponent(this.bitmapPaint.getColor(), alpha));
    }

    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    public int getOpacity() {
        return -3;
    }
}
