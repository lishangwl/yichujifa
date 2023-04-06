//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package esqeee.xieqing.com.eeeeee.ui.floatmenu;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

public class BounceDragGesture extends DragGesture {
    private long mBounceDuration = 300L;
    private static final int MIN_DY_TO_SCREEN_BOTTOM = 100;
    private static final int MIN_DY_TO_SCREEN_TOP = 0;
    private BounceInterpolator mBounceInterpolator;

    public BounceDragGesture(WindowBridge windowBridge, View view) {
        super(windowBridge, view);
        this.setAutoKeepToEdge(true);
        this.mBounceInterpolator = new BounceInterpolator();
    }

    public void setBounceDuration(long bounceDuration) {
        this.mBounceDuration = bounceDuration;
    }

    public boolean onDown(MotionEvent event) {
        return super.onDown(event);
    }

    public void keepToEdge() {
        int y = Math.min(this.mWindowBridge.getScreenHeight() - this.mView.getHeight() - 100, Math.max(0, this.mWindowBridge.getY()));
        int x = this.mWindowBridge.getX();
        int hiddenWidth = (int)(this.getKeepToSideHiddenWidthRadio() * (float)this.mView.getWidth());
        if (x > this.mWindowBridge.getScreenWidth() / 2) {
            this.bounce(x, this.mWindowBridge.getScreenWidth() - this.mView.getWidth() + hiddenWidth, y);
        } else {
            this.bounce(x, -hiddenWidth, y);
        }

    }

    protected void bounce(int fromX, int toX, final int y) {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{(float)fromX, (float)toX});
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                BounceDragGesture.this.mWindowBridge.updatePosition((int)(float)animation.getAnimatedValue(), y);
            }
        });
        animator.setDuration(this.mBounceDuration);
        animator.setInterpolator(this.mBounceInterpolator);
        animator.start();
    }
}
