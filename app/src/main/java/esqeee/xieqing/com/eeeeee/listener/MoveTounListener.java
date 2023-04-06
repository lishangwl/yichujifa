package esqeee.xieqing.com.eeeeee.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class MoveTounListener implements View.OnTouchListener{
    public MoveTounListener(){
    }

    float lastX, lastY;
    float paramX, paramY;
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                paramX = v.getX();
                paramY = v.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx =  event.getRawX() - lastX;
                float dy =  event.getRawY() - lastY;
                v.setX(paramX + dx);
                v.setY(paramY + dy);
                break;
            case MotionEvent.ACTION_UP://被弹起

                break;
        }
        return false; //此处必须返回false，否则OnClickListener获取不到监听
    }
}
