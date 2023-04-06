package esqeee.xieqing.com.eeeeee.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends ImageView {
    private int defaultHeight = 0;
    private int defaultWidth = 0;
    private int mBorderOutsideColor = 0;
    private int mBorderThickness = 0;
    private Context mContext;

    public RoundImageView(Context context) {
        super(context);
        this.mContext = context;
    }

    public RoundImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
    }

    public RoundImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
    }

    public void setCustomAttributes(int i, int i2) {
        this.mBorderThickness = i;
        this.mBorderOutsideColor = i2;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i;
        Drawable drawable = getDrawable();
        if (drawable != null && getWidth() != 0 && getHeight() != 0) {
            measure(0, 0);
            if (drawable.getClass() != NinePatchDrawable.class) {
                Bitmap copy = ((BitmapDrawable) drawable).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
                if (this.defaultWidth == 0) {
                    this.defaultWidth = getWidth();
                }
                if (this.defaultHeight == 0) {
                    this.defaultHeight = getHeight();
                }
                if (this.mBorderThickness > 0) {
                    i = ((this.defaultWidth < this.defaultHeight ? this.defaultWidth : this.defaultHeight) / 2) - this.mBorderThickness;
                    drawCircleBorder(canvas, (this.mBorderThickness / 2) + i, this.mBorderOutsideColor);
                } else {
                    i = (this.defaultWidth < this.defaultHeight ? this.defaultWidth : this.defaultHeight) / 2;
                }
                canvas.drawBitmap(getCroppedRoundBitmap(copy, i), (float) ((this.defaultWidth / 2) - i), (float) ((this.defaultHeight / 2) - i), (Paint) null);
            }
        }
    }

    public Bitmap getCroppedRoundBitmap(Bitmap bitmap, int i) {
        int i2 = i * 2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (height > width) {
            bitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
        } else if (height < width) {
            bitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
        }
        if (!(bitmap.getWidth() == i2 && bitmap.getHeight() == i2)) {
            bitmap = Bitmap.createScaledBitmap(bitmap, i2, i2, true);
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle((float) (bitmap.getWidth() / 2), (float) (bitmap.getHeight() / 2), (float) (bitmap.getWidth() / 2), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    private void drawCircleBorder(Canvas canvas, int i, int i2) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(i2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) this.mBorderThickness);
        canvas.drawCircle((float) (this.defaultWidth / 2), (float) (this.defaultHeight / 2), (float) i, paint);
    }
}