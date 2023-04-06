package esqeee.xieqing.com.eeeeee.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xieqing.codeutils.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;

public class XQPathView extends View implements View.OnTouchListener{
    private List<CustomPath> paths = new ArrayList<>();
    private Paint paint;
    private Paint lineCircle;
    private int color = Color.WHITE;
    private boolean isLinePath = false;

    private boolean isEnable = true;

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public XQPathView(Context context) {
        super(context);
        init();
    }

    public void setLinePath(boolean linePath) {
        isLinePath = linePath;
    }

    public XQPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        lineCircle = new Paint();
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        lineCircle.setStrokeWidth(5);
        lineCircle.setStyle(Paint.Style.FILL);
        lineCircle.setColor(getResources().getColor(R.color.colorAccent));
        lineCircle.setAntiAlias(true);
        setBackgroundColor(Color.parseColor("#50000000"));
        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        for (CustomPath path : paths){
            canvas.drawPath(path,paint);
            if (isLinePath && path.getDownX()!=0 && path.getDownY() != 0){
                //如果画直线，就再画两个圆点
                canvas.drawCircle(path.getDownX(),path.getDownY(),10,lineCircle);
            }
            if (isLinePath && path.getTempX()!=0 && path.getTempY() != 0){
                //如果画直线，就再画两个圆点
                canvas.drawCircle(path.getTempX(),path.getTempY(),10,lineCircle);
            }
        }
    }

    public float getDownX() {
        return downX;
    }

    public float getDownY() {
        return downY;
    }

    private float downX,downY,tempX,tempY;

    public List<CustomPath> getPaths() {
        return paths;
    }

    public void reset() {
        paths.clear();
        paths = null;
        //System.gc();
        paths = new ArrayList<>();
        currentLineCount = -1;
        invalidate();
    }

    public interface OnGestureListener{
        void onStart();
        void onFinished(CustomPath path,float endX,float endY);
    }
    private OnGestureListener onGestureListener;

    public void setOnGestureListener(OnGestureListener onGestureListener) {
        this.onGestureListener = onGestureListener;
    }

    public OnGestureListener getOnGestureListener() {
        return onGestureListener;
    }
    public float resolveX(float x){
        if (x < 0){
            return 0;
        }
        if (x > ScreenUtils.getScreenWidth()){
            return ScreenUtils.getScreenWidth();
        }
        return x;
    }
    public float resolveY(float y){
        if (y < 0){
            return 0;
        }
        if (y > ScreenUtils.getScreenHeight()){
            return ScreenUtils.getScreenHeight();
        }
        return y;
    }

    private int currentLineCount = -1;
    @Override
    public boolean onTouch(View view,MotionEvent event) {
        if(!isEnable){
            return super.onTouchEvent(event);
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = resolveX(event.getRawX());
                downY = resolveY(event.getRawY());
                //customPath = null;
                CustomPath customPath = new CustomPath(downX,downY);
                paths.add(customPath);
                currentLineCount++;
                customPath.moveTo(downX,downY);
                customPath.start();
                invalidate();
                if (getOnGestureListener()!=null){
                    getOnGestureListener().onStart();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(currentLineCount == -1){
                    return false;
                }
                float moveX = resolveX(event.getRawX());
                float moveY = resolveY(event.getRawY());


                if (isLinePath){
                    paths.get(currentLineCount).reset();
                    paths.get(currentLineCount).moveTo(paths.get(currentLineCount).getDownX(),paths.get(currentLineCount).getDownY());
                    paths.get(currentLineCount).lineTo(moveX,moveY);
                    Log.d("XQPath","move->"+paths.get(currentLineCount).getDownX()+","+paths.get(currentLineCount).getDownY());
                }else{
                    paths.get(currentLineCount).quadTo(paths.get(currentLineCount).getTempX(),paths.get(currentLineCount).getTempY(),moveX,moveY);
                }
                paths.get(currentLineCount).setTempX(moveX);
                paths.get(currentLineCount).setTempY(moveY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                paths.get(currentLineCount).end();
                invalidate();
                if (getOnGestureListener()!=null){
                    getOnGestureListener().onFinished(paths.get(currentLineCount),event.getX(),event.getY());
                }
                break;

        }
        return true;
    }

    public JSONArrayBean toJsonArray(){
        JSONArrayBean jsonArrayBean = new JSONArrayBean();
        for (CustomPath path : paths){
            jsonArrayBean.put(path.toBean());
        }
        return jsonArrayBean;
    }
}
