//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package esqeee.xieqing.com.eeeeee.ui.floatmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import esqeee.xieqing.com.eeeeee.R;

public class CircularActionMenu extends FrameLayout {
    private PointF[] mItemExpandedPositionOffsets;
    private CopyOnWriteArrayList<CircularActionMenu.OnStateChangeListener> mOnStateChangeListeners = new CopyOnWriteArrayList();
    private boolean mExpanded;
    private boolean mExpanding = false;
    private boolean mCollapsing = false;
    private float mRadius = 200.0F;
    private float mAngle = (float)Math.toRadians(90.0D);
    private long mDuration = 200L;
    private int mExpandedHeight = -1;
    private int mExpandedWidth = -1;
    private final Interpolator mInterpolator = new FastOutSlowInInterpolator();

    public CircularActionMenu(@NonNull Context context) {
        super(context);
        this.init((AttributeSet)null);
    }

    public CircularActionMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public CircularActionMenu(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.CircularActionMenu);
            this.mRadius = (float)a.getDimensionPixelSize(R.styleable.CircularActionMenu_cam_radius, (int)this.mRadius);
            int angleInDegree = a.getInt(R.styleable.CircularActionMenu_cam_angle, 0);
            if (angleInDegree != 0) {
                this.mAngle = (float)Math.toRadians((double)angleInDegree);
            }

            for(int i = 0; i < this.getItemCount(); ++i) {
                View v = this.getItemAt(i);
                LayoutParams params = (LayoutParams)v.getLayoutParams();
                params.gravity = 8388627;
                this.updateViewLayout(v, params);
            }

            this.requestLayout();
        }
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
    }

    public float getAngle() {
        return this.mAngle;
    }

    public void setAngle(float angle) {
        this.mAngle = angle;
    }

    public void expand(int direction) {
        this.setVisibility(VISIBLE);
        this.mExpanding = true;
        AnimatorListener listener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                CircularActionMenu.this.mExpanding = false;
                CircularActionMenu.this.mExpanded = true;
                Iterator var2 = CircularActionMenu.this.mOnStateChangeListeners.iterator();

                while(var2.hasNext()) {
                    CircularActionMenu.OnStateChangeListener l = (CircularActionMenu.OnStateChangeListener)var2.next();
                    l.onExpanded(CircularActionMenu.this);
                }

            }
        };
        ScaleAnimation scaleAnimation = this.createScaleAnimation(0.0F, 1.0F);
        direction = direction == 5 ? 1 : -1;

        for(int i = 0; i < this.getItemCount(); ++i) {
            View item = this.getItemAt(i);
            item.animate().translationXBy((float)direction * this.mItemExpandedPositionOffsets[i].x)
                    .translationYBy(this.mItemExpandedPositionOffsets[i].y)
                    .setListener(listener)
                    .setDuration(this.mDuration).start();
            item.startAnimation(scaleAnimation);
        }

        Iterator var6 = this.mOnStateChangeListeners.iterator();

        while(var6.hasNext()) {
            CircularActionMenu.OnStateChangeListener l = (CircularActionMenu.OnStateChangeListener)var6.next();
            l.onExpanding(this);
        }

    }

    private ScaleAnimation createScaleAnimation(float fromScale, float toScale) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromScale, toScale, fromScale, toScale, 1, 0.0F, 1, 0.5F);
        scaleAnimation.setDuration(this.mDuration);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setInterpolator(this.mInterpolator);
        return scaleAnimation;
    }

    public View getItemAt(int i) {
        return this.getChildAt(i);
    }

    public void collapse() {
        AnimatorListener listener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                CircularActionMenu.this.mCollapsing = false;
                CircularActionMenu.this.mExpanded = false;
                CircularActionMenu.this.setVisibility(8);
                Iterator var2 = CircularActionMenu.this.mOnStateChangeListeners.iterator();

                while(var2.hasNext()) {
                    CircularActionMenu.OnStateChangeListener l = (CircularActionMenu.OnStateChangeListener)var2.next();
                    l.onCollapsed(CircularActionMenu.this);
                }

            }
        };
        this.mCollapsing = true;
        ScaleAnimation scaleAnimation = this.createScaleAnimation(1.0F, 0.0F);

        for(int i = 0; i < this.getItemCount(); ++i) {
            View item = this.getItemAt(i);
            item.animate().translationX(0.0F).translationY(0.0F).setListener(listener).setDuration(this.mDuration).setInterpolator(this.mInterpolator).start();
            item.startAnimation(scaleAnimation);
        }

        Iterator var5 = this.mOnStateChangeListeners.iterator();

        while(var5.hasNext()) {
            CircularActionMenu.OnStateChangeListener l = (CircularActionMenu.OnStateChangeListener)var5.next();
            l.onCollapsing(this);
        }

    }

    public void addOnStateChangeListener(CircularActionMenu.OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeListeners.add(onStateChangeListener);
    }

    public boolean removeOnStateChangeListener(CircularActionMenu.OnStateChangeListener listener) {
        return this.mOnStateChangeListeners.remove(listener);
    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    public boolean isExpanding() {
        return this.mExpanding;
    }

    public boolean isCollapsing() {
        return this.mCollapsing;
    }

    public int getItemCount() {
        return this.getChildCount();
    }

    private void calcExpandedPositions() {
        this.mItemExpandedPositionOffsets = new PointF[this.getItemCount()];
        double averageAngle = (double)(this.mAngle / (float)(this.getItemCount() - 1));

        for(int i = 0; i < this.getItemCount(); ++i) {
            double angle = (double)(-this.mAngle / 2.0F) + (double)i * averageAngle;
            this.mItemExpandedPositionOffsets[i] = new PointF((float)((double)this.mRadius * Math.cos(angle)), (float)((double)this.mRadius * Math.sin(angle)));
        }

    }

    private void calcExpandedSize() {
        int maxX = 0;
        int maxY = 0;
        int minY = 2147483647;
        int maxWidth = 0;

        for(int i = 0; i < this.getItemCount(); ++i) {
            View item = this.getItemAt(i);
            maxWidth = Math.max(item.getMeasuredWidth(), maxWidth);
            maxX = Math.max((int)(this.mItemExpandedPositionOffsets[i].x + (float)item.getMeasuredWidth()), maxX);
            maxY = Math.max((int)(this.mItemExpandedPositionOffsets[i].y + (float)item.getMeasuredHeight()), maxY);
            minY = Math.min((int)(this.mItemExpandedPositionOffsets[i].y - (float)item.getMeasuredHeight()), minY);
        }

        this.mExpandedWidth = maxX;
        this.mExpandedHeight = maxY - minY;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.measureChildren(widthMeasureSpec, heightMeasureSpec);
        this.calcExpandedPositions();
        if (this.mExpandedHeight == -1 || this.mExpandedWidth == -1) {
            this.calcExpandedSize();
        }

        this.setMeasuredDimension(2 * this.mExpandedWidth, this.mExpandedHeight);
        Iterator var3 = this.mOnStateChangeListeners.iterator();

        while(var3.hasNext()) {
            CircularActionMenu.OnStateChangeListener listener = (CircularActionMenu.OnStateChangeListener)var3.next();
            listener.onMeasured(this);
        }

    }

    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        for(int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }

    }

    public int getExpandedHeight() {
        return this.mExpandedHeight;
    }

    public int getExpandedWidth() {
        return this.mExpandedWidth;
    }

    public static class OnStateChangeListenerAdapter implements CircularActionMenu.OnStateChangeListener {
        public OnStateChangeListenerAdapter() {
        }

        public void onExpanding(CircularActionMenu menu) {
        }

        public void onExpanded(CircularActionMenu menu) {
        }

        public void onCollapsing(CircularActionMenu menu) {
        }

        public void onCollapsed(CircularActionMenu menu) {
        }

        public void onMeasured(CircularActionMenu menu) {
        }
    }

    public interface OnStateChangeListener {
        void onExpanding(CircularActionMenu var1);

        void onExpanded(CircularActionMenu var1);

        void onCollapsing(CircularActionMenu var1);

        void onCollapsed(CircularActionMenu var1);

        void onMeasured(CircularActionMenu var1);
    }
}
