package esqeee.xieqing.com.eeeeee.widget.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;
import android.util.Log;

import com.xieqing.codeutils.util.SizeUtils;

import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class IconTextSpan extends ReplacementSpan {
    private float mBgHeight;  //Icon背景高度
    private float mBgWidth = -1;  //Icon背景宽度
    private float mRadius;  //Icon圆角半径
    private float mMargin; //边距
    private float mTextSize; //文字大小

    private float mBorderSize;//边框大小
    private int mBorderColor;//边框颜色

    private int mTextColor = Color.DKGRAY; //文字颜色

    private Paint mBgPaint; //icon背景画笔
    private Paint mBorderPaint; //边框画笔
    private Paint mTextPaint; //icon文字画笔
    private int[] COLORS = new int[]{Color.parseColor("#d0fbbc"),Color.parseColor("#d0fbbc"),Color.parseColor("#d0fbbc"),Color.parseColor("#d0fbbc")};


    public IconTextSpan() {
        //初始化默认数值
        initDefaultValue();
        //初始化画笔
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //初始化背景画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(COLORS[0]);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);

        //初始化文字画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);


        this.mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStyle(Paint.Style.FILL);


    }

    /**
     * 初始化默认数值

     */
    private void initDefaultValue() {
        this.mBgHeight = SizeUtils.dp2px(17);
        this.mMargin = SizeUtils.dp2px(2);
        this.mRadius = SizeUtils.dp2px(50);
        this.mTextSize = SizeUtils.dp2px(10);
        this.mBorderColor = Color.parseColor("#20696969");
        this.mBorderSize = 2;
    }

    /**
     * 计算icon背景宽度
     *
     * @param text icon内文字
     */
    private float caculateBgWidth(CharSequence text) {
        if (text.length() > 1) {
            //多字，宽度=文字宽度+padding
            Rect textRect = new Rect();
            Paint paint = new Paint();
            paint.setTextSize(mTextSize);
            paint.getTextBounds(text.toString(), 0, text.length(), textRect);
            float padding = SizeUtils.dp2px(4);
            return textRect.width() + padding * 2;
        } else {
            //单字，宽高一致为正方形
            return mBgHeight;
        }
    }

    /**
     * 设置右边距
     *
     * @param rightMarginDpValue
     */
    public void setRightMarginDpValue(int rightMarginDpValue) {
        this.mMargin = SizeUtils.dp2px(rightMarginDpValue);
    }

    /**
     * 设置宽度，宽度=背景宽度+右边距 + 边框
     */
    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        if (mBgWidth == -1){
            //计算背景的宽度
            this.mBgWidth = caculateBgWidth(text.subSequence(start,end));
        }
        return (int) (mBgWidth + mMargin * 2 + mBorderSize *2) ;
    }

    /**
     * draw
     *
     * @param text   完整文本
     * @param start  setSpan里设置的start
     * @param end    setSpan里设置的start
     * @param x
     * @param top    当前span所在行的上方y
     * @param y      y其实就是metric里baseline的位置
     * @param bottom 当前span所在行的下方y(包含了行间距)，会和下一行的top重合
     * @param paint  使用此span的画笔
     */
    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {


        Log.d("xxxxxxxxxxxxx","t:"+text+",start:"+start+",end:"+end);

        Paint.FontMetrics metrics = paint.getFontMetrics();

        float textHeight = metrics.descent - metrics.ascent;
        //算出背景开始画的y坐标
        float bgStartY = y + (textHeight - mBgHeight) / 2 + metrics.ascent;

        //画背景
        RectF border = new RectF(x + mMargin, bgStartY, x + mBgWidth, bgStartY + mBgHeight);
        RectF bgRect = new RectF(x + mMargin + mBorderSize, bgStartY + mBorderSize, x + mBgWidth - mBorderSize, bgStartY + mBgHeight - mBorderSize);

        canvas.drawRoundRect(border, mRadius, mRadius, mBorderPaint);
        canvas.drawRoundRect(bgRect, mRadius, mRadius, mBgPaint);
        //Bitmap bitmap = ResourceUtils.getBitmap(R.drawable.tag_let);
        //Rect srcRect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        //canvas.drawBitmap(bitmap,srcRect,bgRect,new Paint());

        //把字画在背景中间
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);  //这个只针对x有效
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textRectHeight = fontMetrics.bottom - fontMetrics.top;
        try {
            canvas.drawText(text.subSequence(start + 1,end - 1).toString(), x + mBgWidth / 2, bgStartY + (mBgHeight - textRectHeight) / 2 - fontMetrics.top, textPaint);
        }catch (Exception e){
            RuntimeLog.log(e.getMessage());
        }
    }


    public static CharSequence getSpan(CharSequence text){
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new IconTextSpan(),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}