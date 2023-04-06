package esqeee.xieqing.com.eeeeee.widget.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.style.ReplacementSpan;

public class ValotionSpan extends ReplacementSpan {
    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fontMetricsInt) {
        paint.setTextSize(paint.getTextSize() - 20);
        //mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
        //我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
        //可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
        return (int) (paint.measureText(text, start, end)) + 20;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        RectF oval = new RectF(x, y + paint.ascent() + 5, x + getSize(paint,text,start,end,null), y + paint.descent() + 5);
        canvas.drawRoundRect(oval,150,150,paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(text,start,end,x + 10,y - 5,paint);
    }
}
