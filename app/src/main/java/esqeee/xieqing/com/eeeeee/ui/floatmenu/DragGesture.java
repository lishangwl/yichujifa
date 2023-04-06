//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package esqeee.xieqing.com.eeeeee.ui.floatmenu;

import androidx.core.view.GestureDetectorCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class DragGesture extends SimpleOnGestureListener {
    protected WindowBridge mWindowBridge;
    protected View mView;
    private float mKeepToSideHiddenWidthRadio = 0.5F;
    private int mInitialX;
    private int mInitialY;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private OnClickListener mOnClickListener;
    private boolean mAutoKeepToEdge;
    private float mPressedAlpha = 1.0F;
    private float mUnpressedAlpha = 0.4F;
    private boolean mEnabled = true;

    public DragGesture(WindowBridge windowBridge, View view) {
        this.mWindowBridge = windowBridge;
        this.mView = view;
        this.setupView();
    }

    private void setupView() {
        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(this.mView.getContext(), this);
        this.mView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                if (event.getAction() == 1) {
                    DragGesture.this.mView.setAlpha(DragGesture.this.mUnpressedAlpha);
                    if (!DragGesture.this.onTheEdge() && DragGesture.this.mAutoKeepToEdge) {
                        DragGesture.this.keepToEdge();
                    }
                }

                return true;
            }
        });
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    protected boolean onTheEdge() {
        int dX1 = Math.abs(this.mWindowBridge.getX());
        int dX2 = Math.abs(this.mWindowBridge.getX() - this.mWindowBridge.getScreenWidth());
        return Math.min(dX1, dX2) < 5;
    }

    public float getPressedAlpha() {
        return this.mPressedAlpha;
    }

    public void setPressedAlpha(float pressedAlpha) {
        this.mPressedAlpha = pressedAlpha;
    }

    public float getUnpressedAlpha() {
        return this.mUnpressedAlpha;
    }

    public void setUnpressedAlpha(float unpressedAlpha) {
        this.mUnpressedAlpha = unpressedAlpha;
    }

    public void setAutoKeepToEdge(boolean autoKeepToEdge) {
        this.mAutoKeepToEdge = autoKeepToEdge;
    }

    public boolean isAutoKeepToEdge() {
        return this.mAutoKeepToEdge;
    }

    public void setKeepToSideHiddenWidthRadio(float keepToSideHiddenWidthRadio) {
        this.mKeepToSideHiddenWidthRadio = keepToSideHiddenWidthRadio;
    }

    public float getKeepToSideHiddenWidthRadio() {
        return this.mKeepToSideHiddenWidthRadio;
    }

    public boolean onDown(MotionEvent event) {
        this.mInitialX = this.mWindowBridge.getX();
        this.mInitialY = this.mWindowBridge.getY();
        this.mInitialTouchX = event.getRawX();
        this.mInitialTouchY = event.getRawY();
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!this.mEnabled) {
            return false;
        } else {
            this.mWindowBridge.updatePosition(this.mInitialX + (int)(e2.getRawX() - this.mInitialTouchX), this.mInitialY + (int)(e2.getRawY() - this.mInitialTouchY));
            this.mView.setAlpha(this.mPressedAlpha);
            Log.d("DragGesture", "onScroll");
            return false;
        }
    }

    public void keepToEdge() {
        int x = this.mWindowBridge.getX();
        int hiddenWidth = (int)(this.mKeepToSideHiddenWidthRadio * (float)this.mView.getWidth());
        if (x > this.mWindowBridge.getScreenWidth() / 2) {
            this.mWindowBridge.updatePosition(this.mWindowBridge.getScreenWidth() - this.mView.getWidth() + hiddenWidth, this.mWindowBridge.getY());
        } else {
            this.mWindowBridge.updatePosition(-hiddenWidth, this.mWindowBridge.getY());
        }

    }

    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (this.mOnClickListener != null) {
            this.mOnClickListener.onClick(this.mView);
        }

        return super.onSingleTapConfirmed(e);
    }

    public void setOnDraggedViewClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}
