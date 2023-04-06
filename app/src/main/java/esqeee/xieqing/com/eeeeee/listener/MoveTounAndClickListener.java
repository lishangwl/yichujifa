package esqeee.xieqing.com.eeeeee.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class MoveTounAndClickListener implements View.OnTouchListener{
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    public MoveTounAndClickListener(WindowManager windowManager,WindowManager.LayoutParams layoutParams){
        this.windowManager = windowManager;
        this.layoutParams = layoutParams;
    }

    int lastX, lastY;
    int paramX, paramY;
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                paramX = layoutParams.x;
                paramY = layoutParams.y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                layoutParams.x = paramX + dx;
                layoutParams.y = paramY + dy;
                try {
                    windowManager.updateViewLayout(v, layoutParams);// 更新悬浮按钮位置
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP://被弹起

                break;
        }
        return false; //此处必须返回false，否则OnClickListener获取不到监听
    }
}
