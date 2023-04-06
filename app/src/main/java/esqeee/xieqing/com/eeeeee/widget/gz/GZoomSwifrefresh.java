package esqeee.xieqing.com.eeeeee.widget.gz; /**
 * Created by gzoom on 2017/2/4.
 */
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 *
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ScrollView;


/**
 * The GZoomSwifrefresh should be used whenever the user can refresh the
 * contents of a view via a vertical swipe gesture. The activity that
 * instantiates this view should add an OnRefreshListener to be notified
 * whenever the swipe to refresh gesture is completed. The GZoomSwifrefresh
 * will notify the listener each and every time the gesture is completed again;
 * the listener is responsible for correctly determining when to actually
 * initiate a refresh of its content. If the listener determines there should
 * not be a refresh, it must call setRefreshing(false) to cancel any visual
 * indication of a refresh. If an activity wishes to show just the progress
 * animation, it should call setRefreshing(true). To disable the gesture and
 * progress animation, call setEnabled(false) on the view.
 * <p>
 * This layout should be made the parent of the view that will be refreshed as a
 * result of the gesture and can only support one direct child. This view will
 * also be made the target of the gesture and will be forced to match both the
 * width and the height supplied in this layout. The GZoomSwifrefresh does not
 * provide accessibility events; instead, a menu item must be provided to allow
 * refresh of the content wherever this gesture is used.
 * </p>
 */
public class GZoomSwifrefresh extends ViewGroup implements NestedScrollingParent,
        NestedScrollingChild {
    // Maps to ProgressBar.Large style
    public static final int LARGE = MaterialProgressDrawable.LARGE;
    // Maps to ProgressBar default style
    public static final int DEFAULT = MaterialProgressDrawable.DEFAULT;

    @VisibleForTesting
    static final int CIRCLE_DIAMETER = 40;
    @VisibleForTesting
    static final int CIRCLE_DIAMETER_LARGE = 56;

    private static final String LOG_TAG = GZoomSwifrefresh.class.getSimpleName();

    private static final int MAX_ALPHA = 255;
    private static final int STARTING_PROGRESS_ALPHA = (int) (.3f * MAX_ALPHA);

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = .5f;

    // Max amount of circle that can be filled by progress during swipe gesture,
    // where 1.0 is a full circle
    private static final float MAX_PROGRESS_ANGLE = .8f;

    private static final int SCALE_DOWN_DURATION = 150;

    private static final int ALPHA_ANIMATION_DURATION = 300;

    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;

    private static final int ANIMATE_TO_START_DURATION = 200;

    // Default background for the progress spinner
    private static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
    // Default offset in dips from the top of the view to where the progress spinner should stop
    private static final int DEFAULT_CIRCLE_TARGET = 64;

    /**
     * 底部view上拉最大的大小
     */
    private final int mBottomSpinnerOffsetEnd;

    private View mTarget; // the target of the gesture
    OnRefreshListener mListener;

    OnBottomRefreshListener mListenerBottom;

    public boolean mRefreshing = false;

    //boolean mRefreshingBottom = false;

    boolean mRefreshingBottom = false;

    ScrollView s;

    private int mTouchSlop;
    /**
     * 初始化也是64dp,作为一个界定点，最终的刷新动画需要停在这个点上，超过这个点会减缓滑动速度
     */
    private float mTotalDragDistance = -1;

    // If nested scrolling is enabled, the total amount that needed to be
    // consumed by this as the nested scrolling parent is used in place of the
    // overscroll determined by MOVE events in the onTouch handler
    /**
     * 顶部没有被消费的
     */
    private float mTotalUnconsumed;
    /**
     * 底部滑动没有被消费的
     */
    private float mTotalUnconsumedBottom;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];
    private boolean mNestedScrollInProgress;

    private int mMediumAnimationDuration;
    int mCurrentTargetOffsetTop;
    /**
     * 目前底部circle的底部
     */
    int mCurrentTargetOffsetBottom;

    private float mInitialMotionY;
    private float mInitialDownY;
    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;
    // Whether this item is scaled up rather than clipped
    /**
     * 如果在z轴它的上面没有东西，那么为true
     */
    boolean mScale;

    // Target is returning to its start offset because it was cancelled or a
    // refresh was triggered.
    private boolean mReturningToStart;
    /**
     * 是否正在返回底部
     */
    private boolean mReturningToEnd;
    private final DecelerateInterpolator mDecelerateInterpolator;
    private static final int[] LAYOUT_ATTRS = new int[]{
            android.R.attr.enabled
    };

    CircleImageView mCircleView;


    CircleImageView mCircleViewBottom;
    private int mCircleViewIndex = -1;
    /**
     * bottomCircle在视图中的顺序
     */
    private int mCircleViewBottomIndex = -1;

    protected int mFrom;

    float mStartingScale;
    /**
     * 原始顶部
     */
    protected int mOriginalOffsetTop;
    /**
     * 原始底部
     */
    protected int mOriginalOffsetBottom;
    /**
     * 默认64dp
     */
    int mSpinnerOffsetEnd;
    /**
     * 底部spinner可以上拉到最大高度
     */
    int mSpinnerBottomOffsetEnd;
    MaterialProgressDrawable mProgress;
    /**
     * 底部Circle的drawable
     */
    MaterialProgressDrawable mProgressBottom;

    private Animation mScaleAnimation;

    private Animation mScaleDownAnimation;
    /**
     * 顶部circleView的透明度启动动画
     */
    private Animation mAlphaStartAnimation;
    /**
     * 底部 bottom view的透明度启动动画
     */
    private Animation mAlphaStartAnimationBottom;
    /**
     * 顶部circleView的透明度最大动画
     */
    private Animation mAlphaMaxAnimation;
    /**
     * 底部 bottom view的透明度最大动画
     */
    private Animation mAlphaMaxAnimationBottom;

    private Animation mScaleDownToStartAnimation;
    /**
     * top circle标志位
     */
    boolean mNotify;

    /**
     * bottom circle view 标志位
     */
    boolean mNotifyBottom;

    /**
     * circle的初始上限
     */
    private int mCircleDiameter;

    // Whether the client has set a custom starting position;
    boolean mUsingCustomStart;

    private OnChildScrollUpCallback mChildScrollUpCallback;


    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("fish", "getMessageName=" + msg.what);
            reset();
            super.handleMessage(msg);
        }
    };

    //add
    private boolean mBottomIsScrolling;
    /**
     * 第一次测量标志位
     */
    private boolean firstMeasure;

    /**
     * 刷新动画监听，在动画结束的时候判断，如果还在刷新就继续显示circle，
     */
    private AnimationListener mRefreshListenerBottom = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mRefreshingBottom) {
                // Make sure the progress view is fully visible
                mProgressBottom.setAlpha(MAX_ALPHA);
                mProgressBottom.start();
                if (mNotifyBottom) {
                    if (mListenerBottom != null) {
                        mListenerBottom.onBottomRefresh();
                        Log.e("fish", "mListenerBottom!= null");
                    } else {
                        Log.e("fish", "mListenerBottom == null");
                    }
                }
                mCurrentTargetOffsetBottom = mCircleViewBottom.getBottom();
            } else {
                reset();
            }
        }
    };

    /**
     * 刷新动画监听，在动画结束的时候判断，如果还在刷新就继续显示circle，
     */
    private AnimationListener mRefreshListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mRefreshing) {
                mProgress.setAlpha(MAX_ALPHA);
                mProgress.start();
                if (mNotify) {
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                }
                mCurrentTargetOffsetTop = mCircleView.getTop();
            } else {
                reset();
            }
        }
    };


    /**
     * 重设状态，将circle隐藏起来
     */
    void reset() {
        mCircleView.clearAnimation();

        mCircleViewBottom.clearAnimation();

        mProgress.stop();
        mCircleView.setVisibility(View.GONE);
        mProgressBottom.stop();
        mCircleViewBottom.setVisibility(View.GONE);

        setColorViewAlpha(MAX_ALPHA);
        if (mScale) {
            setAnimationProgress(0 /* animation complete and view is hidden */);
        } else {
            setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCurrentTargetOffsetTop,//往回推
                    true /* requires update */);
            setTargetOffsetTopAndBottomForBottom(mOriginalOffsetBottom - mCurrentTargetOffsetBottom, true);
        }
        mCurrentTargetOffsetTop = mCircleView.getTop();
        mCurrentTargetOffsetBottom = mCircleViewBottom.getBottom();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            reset();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    /**
     * 设置透明度，包括circleView和透明度
     */
    private void setColorViewAlpha(int targetAlpha) {
        mCircleView.getBackground().setAlpha(targetAlpha);
        mProgress.setAlpha(targetAlpha);


        mCircleViewBottom.getBackground().setAlpha(targetAlpha);
        mProgressBottom.setAlpha(targetAlpha);
    }

    /**
     * The refresh indicator starting and resting position is always positioned
     * near the top of the refreshing content. This position is a consistent
     * location, but can be adjusted in either direction based on whether or not
     * there is a toolbar or actionbar present.
     * <p>
     * <strong>Note:</strong> Calling this will reset the position of the refresh indicator to
     * <code>start</code>.
     * </p>
     *
     * @param scale Set to true if there is no view at a higher z-order than where the progress
     *              spinner is set to appear. Setting it to true will cause indicator to be scaled
     *              up rather than clipped.
     * @param start The offset in pixels from the top of this view at which the
     *              progress spinner should appear.
     * @param end   The offset in pixels from the top of this view at which the
     *              progress spinner should come to rest after a successful swipe
     *              gesture.
     *              <p>这个方法就是自己设置circle的起始结束位置</p>
     */
    public void setProgressViewOffset(boolean scale, int start, int end) {
        mScale = scale;
        mOriginalOffsetTop = start;
        mSpinnerOffsetEnd = end;
        mUsingCustomStart = true;
        reset();
        mRefreshing = false;
    }

    /**
     * @return The offset in pixels from the top of this view at which the progress spinner should
     * appear.
     */
    public int getProgressViewStartOffset() {
        return mOriginalOffsetTop;
    }

    /**
     * @return The offset in pixels from the top of this view at which the progress spinner should
     * come to rest after a successful swipe gesture.
     */
    public int getProgressViewEndOffset() {
        return mSpinnerOffsetEnd;
    }

    /**
     * The refresh indicator resting position is always positioned near the top
     * of the refreshing content. This position is a consistent location, but
     * can be adjusted in either direction based on whether or not there is a
     * toolbar or actionbar present.
     *
     * @param scale Set to true if there is no view at a higher z-order than where the progress
     *              spinner is set to appear. Setting it to true will cause indicator to be scaled
     *              up rather than clipped.
     * @param end   The offset in pixels from the top of this view at which the
     *              progress spinner should come to rest after a successful swipe
     *              gesture.
     */
    public void setProgressViewEndTarget(boolean scale, int end) {
        mSpinnerOffsetEnd = end;
        mScale = scale;
        mCircleView.invalidate();
        mCircleViewBottom.invalidate();
    }

    /**
     * One of DEFAULT, or LARGE.
     * 设置progressBar的size大小，他是固定的，只有大和一般两种大小
     */
    public void setSize(int size) {
        if (size != MaterialProgressDrawable.LARGE && size != MaterialProgressDrawable.DEFAULT) {
            return;
        }
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        if (size == MaterialProgressDrawable.LARGE) {
            mCircleDiameter = (int) (CIRCLE_DIAMETER_LARGE * metrics.density);
        } else {
            mCircleDiameter = (int) (CIRCLE_DIAMETER * metrics.density);
        }
        // force the bounds of the progress circle inside the circle view to
        // update by setting it to null before updating its size and then
        // re-setting it
        mCircleView.setImageDrawable(null);
        mCircleViewBottom.setImageDrawable(null);
        mProgress.updateSizes(size);
        mCircleView.setImageDrawable(mProgress);
        mProgressBottom.updateSizes(size);
        //  mProgressBottom.showArrow(true);
        // mProgressBottom.setRotation(0.5f);
        mCircleViewBottom.setImageDrawable(mProgressBottom);

    }

    /**
     * Simple constructor to use when creating a GZoomSwifrefresh from code.
     *
     * @param context
     */
    public GZoomSwifrefresh(Context context) {
        this(context, null);
    }

    /**
     * Constructor that is called when inflating GZoomSwifrefresh from XML.
     *
     * @param context
     * @param attrs
     */
    public GZoomSwifrefresh(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mMediumAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);
        /**因为要绘制小圈圈，所以是需要绘制的*/
        setWillNotDraw(false);
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mCircleDiameter = (int) (CIRCLE_DIAMETER * metrics.density);

        createProgressView();
        //按照顺序绘制
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        // the absolute offset has to take into account that the circle starts at an offset
        mSpinnerOffsetEnd = (int) (DEFAULT_CIRCLE_TARGET * metrics.density);
        //这是错的，只是先初始化
        mBottomSpinnerOffsetEnd = mSpinnerOffsetEnd;

        mTotalDragDistance = mSpinnerOffsetEnd;
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);

        mOriginalOffsetTop = mCurrentTargetOffsetTop = -mCircleDiameter;
        moveToStart(1.0f);
        //这个也是错的，只是先初始化一下
        mOriginalOffsetBottom = mCircleDiameter;
        mCurrentTargetOffsetBottom = mCircleDiameter;


        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();

        firstMeasure = true;
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int windowHeight = wm.getDefaultDisplay().getHeight();
        Log.e("fish", "init+-+windowHeight==" + windowHeight);
        mOriginalOffsetBottom = windowHeight - mCircleDiameter / 2;
        Log.e("fish", "init+-+mOriginalOffsetBottom==" + mOriginalOffsetBottom);

        mCurrentTargetOffsetBottom = mOriginalOffsetBottom;
        mSpinnerBottomOffsetEnd = mOriginalOffsetBottom - mSpinnerOffsetEnd;


    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mCircleViewIndex < 0) {
            return i;
        } else if (i == childCount - 1) {
            // Draw the selected child last
            return mCircleViewIndex;
        } else if (i >= mCircleViewIndex) {
            // Move the children after the selected child earlier one
            return i + 1;
        } else {
            // Keep the children before the selected child the same
            return i;
        }
        //return super.getChildDrawingOrder(childCount,i);
    }

    /**
     * 创造ProgressView，给他们设置ImageDrawable
     */
    private void createProgressView() {
        mCircleView = new CircleImageView(getContext(), CIRCLE_BG_LIGHT);

        mCircleViewBottom = new CircleImageView(getContext(), CIRCLE_BG_LIGHT);

        mProgress = new MaterialProgressDrawable(getContext(), this);
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        mCircleView.setImageDrawable(mProgress);
        mCircleView.setVisibility(View.GONE);
        addView(mCircleView);

        mProgressBottom = new MaterialProgressDrawable(getContext(), this);
        mProgressBottom.setBackgroundColor(CIRCLE_BG_LIGHT);
        mCircleViewBottom.setImageDrawable(mProgressBottom);
        //  mProgressBottom.showArrow(true);
        //  mProgressBottom.setRotation(0.5f);
        //  mProgressBottom.start();
        mCircleViewBottom.setVisibility(View.GONE);
        addView(mCircleViewBottom);
        //addView(mCircleView,2);
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe
     * gesture.
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }


    public void setOnBottomRefreshListenrer(OnBottomRefreshListener listener) {
        mListenerBottom = listener;
    }


    /**
     * Pre API 11, alpha is used to make the progress circle appear instead of scale.
     * 这里是判断机型版本有没有大于安卓3.0
     * 小于返回true
     */
    private boolean isAlphaUsedForScale() {
        return android.os.Build.VERSION.SDK_INT < 11;//11 是Android3.0
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     * 这个方法是暴露给外部手动停止滑动的
     *
     * @param refreshing Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && mRefreshing != refreshing) {
            // scale and show
            mRefreshing = refreshing;
            int endTarget = 0;
            if (!mUsingCustomStart) {
                endTarget = mSpinnerOffsetEnd + mOriginalOffsetTop;
            } else {
                endTarget = mSpinnerOffsetEnd;
            }
            setTargetOffsetTopAndBottom(endTarget - mCurrentTargetOffsetTop,
                    true /* requires update */);
            mNotify = false;
            mNotifyBottom = false;
            startScaleUpAnimation(mRefreshListener);
        } else {
            setRefreshing(refreshing, false /* notify */);
        }
    }

    public void setBottomRefreshing(boolean refreshing) {
        if (refreshing && mRefreshingBottom != refreshing) {
            // scale and show
            mRefreshingBottom = refreshing;
            int endTarget = 0;
            if (!mUsingCustomStart) {
                endTarget = (int) (mOriginalOffsetBottom - mSpinnerOffsetEnd * 1.5);
            } else {
                endTarget = mSpinnerOffsetEnd;
            }
            setTargetOffsetTopAndBottomForBottom(endTarget - mCurrentTargetOffsetBottom,
                    true /* requires update */);
            mNotifyBottom = false;
            //  mNotifyBottom = false;
            startScaleUpAnimationBottom(mRefreshListenerBottom);
        } else {
            setRefreshingBottom(refreshing, false /* notify */);
        }
    }


    /**
     * 开始顶部 CircleView的动画
     */
    private void startScaleUpAnimation(AnimationListener listener) {
        mCircleView.setVisibility(View.VISIBLE);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            // Pre API 11, alpha is used in place of scale up to show the
            // progress circle appearing.
            // Don't adjust the alpha during appearance otherwise.
            mProgress.setAlpha(MAX_ALPHA);
        }
        mScaleAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setAnimationProgress(interpolatedTime);//interpolatedTime 从小到大，0.0-1
            }
        };
        mScaleAnimation.setDuration(mMediumAnimationDuration);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleAnimation);
    }

    /**
     * 开始底部 CircleView的动画
     */
    private void startScaleUpAnimationBottom(AnimationListener listener) {
        mCircleViewBottom.setVisibility(View.VISIBLE);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            // Pre API 11, alpha is used in place of scale up to show the
            // progress circle appearing.
            // Don't adjust the alpha during appearance otherwise.
            mProgressBottom.setAlpha(MAX_ALPHA);
        }
        mScaleAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setAnimationProgressBottom(interpolatedTime);//interpolatedTime 从小到大，0.0-1
            }
        };
        mScaleAnimation.setDuration(mMediumAnimationDuration);
        if (listener != null) {
            mCircleViewBottom.setAnimationListener(listener);
        }
        mCircleViewBottom.clearAnimation();
        mCircleViewBottom.startAnimation(mScaleAnimation);
    }


    /**
     * Pre API 11, this does an alpha animation.
     *
     * @param progress 设置CircleView的进程
     */
    void setAnimationProgress(float progress) {
        if (isAlphaUsedForScale()) {
            setColorViewAlpha((int) (progress * MAX_ALPHA));
            Log.e(LOG_TAG, "setAnimationProgress,setColorViewAlpha");
        } else {
            //不出所料，我走的是这个方法
            ViewCompat.setScaleX(mCircleView, progress);
            ViewCompat.setScaleY(mCircleView, progress);
            Log.e(LOG_TAG, "setAnimationProgress,ViewCompat.setScale，progress==" + progress);
        }
    }


    void setAnimationProgressBottom(float progress) {
        if (isAlphaUsedForScale()) {
            setColorViewAlpha((int) (progress * MAX_ALPHA));
            Log.e(LOG_TAG, "setAnimationProgress,setColorViewAlpha");
        } else {
            //不出所料，我走的是这个方法
            ViewCompat.setScaleX(mCircleViewBottom, progress);
            ViewCompat.setScaleY(mCircleViewBottom, progress);
            Log.e(LOG_TAG, "setAnimationProgressBottom,ViewCompat.setScale，progress==" + progress);
        }
    }

    /**
     * 设置refreshing的状态，如果不相应就相应的改变，应该是为了设置刷不刷新
     */
    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {//如果标志位正确，就启动刷新动画，将circle一步步移到中间那个位置
                animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, mRefreshListener);
            } else {
                startScaleDownAnimation(mRefreshListener);
            }
        }
    }


    /**
     * 设置refreshing的状态，如果不相应就相应的改变，应该是为了设置刷不刷新
     */
    private void setRefreshingBottom(boolean refreshing, final boolean notify) {
        if (mRefreshingBottom != refreshing) {
            mNotifyBottom = notify;
            ensureTarget();
            mRefreshingBottom = refreshing;
            if (mRefreshingBottom) {//如果标志位正确，就启动刷新动画，将circle一步步移到中间那个位置
                animateOffsetToCorrectPositionBottom(mCurrentTargetOffsetBottom, mRefreshListenerBottom);//
                //add
                //reset();
                //不行，加上去之后转都不转
            } else {
                startScaleDownAnimationBottom(mRefreshListenerBottom);
            }
        }
    }


    /**
     * 开始mScaleDownAnimation动画，还有对于circle进程的改变
     * 就是在你手指拖动的过程中改变
     *
     * <p>说白了就是对circleBottom进行倒着转动画</p>
     */
    void startScaleDownAnimation(AnimationListener listener) {
        mScaleDownAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setAnimationProgress(1 - interpolatedTime);//倒着转？
            }
        };
        mScaleDownAnimation.setDuration(SCALE_DOWN_DURATION);
        mCircleView.setAnimationListener(listener);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleDownAnimation);
    }

    /**
     * 开始mScaleDownAnimation动画，还有对于circle进程的改变
     * 就是在你手指拖动的过程中改变
     * <p>
     * bottomn
     */
    void startScaleDownAnimationBottom(AnimationListener listener) {
        mScaleDownAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setAnimationProgressBottom(1 - interpolatedTime);//倒着转？
            }
        };
        mScaleDownAnimation.setDuration(SCALE_DOWN_DURATION);
        mCircleViewBottom.setAnimationListener(listener);
        mCircleViewBottom.clearAnimation();
        mCircleViewBottom.startAnimation(mScaleDownAnimation);
    }

    /**
     * 开始透明度的变化，最终76
     * <p>for top CircleView</p>
     */
    private void startProgressAlphaStartAnimation() {
        mAlphaStartAnimation = startAlphaAnimation(mProgress.getAlpha(), STARTING_PROGRESS_ALPHA);
    }

    /**
     * 开始透明度的变化，最终76
     * <p>for bottom CircleView</p>
     */
    private void startProgressAlphaStartAnimationBottom() {
        mAlphaStartAnimationBottom = startAlphaAnimationBottom(mProgress.getAlpha(), STARTING_PROGRESS_ALPHA);
    }


    /**
     * 开启到最大的透明度，255
     * <p>for top CircleView</p>
     */
    private void startProgressAlphaMaxAnimation() {
        mAlphaMaxAnimation = startAlphaAnimation(mProgress.getAlpha(), MAX_ALPHA);
    }

    /**
     * 开启到最大的透明度，255
     * <p>for top CircleView</p>
     */
    private void startProgressAlphaMaxAnimationBottom() {
        mAlphaMaxAnimationBottom = startAlphaAnimationBottom(mProgressBottom.getAlpha(), MAX_ALPHA);
    }


    /**
     * 透明度动画。两个参数分别是开始透明度和结束，这样可能效果不会太突兀？
     * <p>for top circle view</p>
     */
    private Animation startAlphaAnimation(final int startingAlpha, final int endingAlpha) {
        // Pre API 11, alpha is used in place of scale. Don't also use it to
        // show the trigger point.
        if (mScale && isAlphaUsedForScale()) {
            return null;
        }
        Animation alpha = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                mProgress.setAlpha(
                        (int) (startingAlpha + ((endingAlpha - startingAlpha) * interpolatedTime)));
            }
        };
        alpha.setDuration(ALPHA_ANIMATION_DURATION);
        // Clear out the previous animation listeners.
        mCircleView.setAnimationListener(null);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(alpha);
        return alpha;
    }


    /**
     * 透明度动画。两个参数分别是开始透明度和结束，这样可能效果不会太突兀？
     * <p>for top circle view</p>
     */
    private Animation startAlphaAnimationBottom(final int startingAlpha, final int endingAlpha) {
        // Pre API 11, alpha is used in place of scale. Don't also use it to
        // show the trigger point.
        if (mScale && isAlphaUsedForScale()) {
            return null;
        }
        Animation alpha = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                mProgress.setAlpha(
                        (int) (startingAlpha + ((endingAlpha - startingAlpha) * interpolatedTime)));
            }
        };
        alpha.setDuration(ALPHA_ANIMATION_DURATION);
        // Clear out the previous animation listeners.
        mCircleViewBottom.setAnimationListener(null);
        mCircleViewBottom.clearAnimation();
        mCircleViewBottom.startAnimation(alpha);
        return alpha;
    }


    /**
     * @deprecated Use {@link #setProgressBackgroundColorSchemeResource(int)}
     */
    @Deprecated
    public void setProgressBackgroundColor(int colorRes) {
        setProgressBackgroundColorSchemeResource(colorRes);
    }

    /**
     * Set the background color of the progress spinner disc.
     *
     * @param colorRes Resource id of the color.
     */
    public void setProgressBackgroundColorSchemeResource(@ColorRes int colorRes) {
        setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getContext(), colorRes));
    }

    /**
     * Set the background color of the progress spinner disc.
     *
     * @param color
     */
    public void setProgressBackgroundColorSchemeColor(@ColorInt int color) {
        mCircleView.setBackgroundColor(color);
        mProgress.setBackgroundColor(color);
    }

    /**
     * @deprecated Use {@link #setColorSchemeResources(int...)}
     */
    @Deprecated
    public void setColorScheme(@ColorInt int... colors) {
        setColorSchemeResources(colors);
    }

    /**
     * Set the color resources used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.
     *
     * @param colorResIds
     */
    public void setColorSchemeResources(int... colorResIds) {//这里被我删了注解
        final Context context = getContext();
        int[] colorRes = new int[colorResIds.length];
        for (int i = 0; i < colorResIds.length; i++) {
            colorRes[i] = ContextCompat.getColor(context, colorResIds[i]);
        }
        setColorSchemeColors(colorRes);
    }

    /**
     * Set the colors used in the progress animation. The first
     * color will also be the color of the bar that grows in response to a user
     * swipe gesture.
     *
     * @param colors
     */
    public void setColorSchemeColors(@ColorInt int... colors) {
        ensureTarget();
        mProgress.setColorSchemeColors(colors);
    }

    /**
     * 设置底部circle加载颜色
     */
    public void setBottomColorSchemeColors(int... colors) {
        ensureTarget();
        mProgressBottom.setColorSchemeColors(colors);
    }


    /**
     * @return Whether the SwipeRefreshWidget is actively showing refresh
     * progress.
     */
    public boolean isRefreshing() {
        return mRefreshing;
    }

    /**
     * 这个就是要找到滑动的目标，也就是mTarget
     * ，他就是找到在子view中接着上面刷新圆环的第一个，也就是我们放在格局中的第一个
     */
    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {//手势目标
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mCircleView) && !child.equals(mCircleViewBottom)) {
                    mTarget = child;
                    Log.e(LOG_TAG, "ensureTarget,mTarget==" + mTarget.toString());
                    break;
                }
            }
        }
    }

    /**
     * Set the distance to trigger a sync in dips
     * 设置拖动的极限距离
     *
     * @param distance
     */
    public void setDistanceToTriggerSync(int distance) {
        mTotalDragDistance = distance;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        final View child = mTarget;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        Log.e(LOG_TAG, "onLayout,childTop==" + childTop + ",childHeight" + childHeight);
        //草，就是因为这个，才会滚不回去，在重绘的过程中会进行新一次的onLayout
       /* if(firstMeasure) {//其实已经没有必要摆在这里了
            //  mOriginalOffsetBottom = height + mCircleDiameter;
            //mOriginalOffsetBottom = 1250;
            mCurrentTargetOffsetBottom = mOriginalOffsetBottom;
            mSpinnerBottomOffsetEnd = mOriginalOffsetBottom - mSpinnerOffsetEnd;
            firstMeasure = false;
            Log.e("fish", "firstMeasure,mCurrentTargetOffsetBottom==" + mCurrentTargetOffsetBottom + ",mSpinnerBottomOffsetEnd==" + mSpinnerBottomOffsetEnd);
            Log.e("fish", "firstMeasure,mOriginalOffsetBottom==" + mOriginalOffsetBottom);
        }*/

        int circleWidth = mCircleView.getMeasuredWidth();
        int circleHeight = mCircleView.getMeasuredHeight();
        mCircleView.layout((width / 2 - circleWidth / 2), mCurrentTargetOffsetTop,
                (width / 2 + circleWidth / 2), mCurrentTargetOffsetTop + circleHeight);
//        mCircleView.layout((width / 2 - circleWidth / 2), childHeight/2,
//                     (width / 2 + circleWidth / 2), childHeight/2 + circleHeight);

        // mCurrentTargetOffsetBottom = mOriginalOffsetBottom;
        int circleBottomWidth = mCircleViewBottom.getMeasuredWidth();
        int circleBottomHeight = mCircleViewBottom.getMeasuredHeight();
        mCircleViewBottom.setVisibility(View.VISIBLE);
        mCircleViewBottom.layout((width / 2 - circleBottomWidth / 2), mCurrentTargetOffsetBottom - circleBottomHeight,
                (width / 2 + circleBottomWidth / 2), mCurrentTargetOffsetBottom);
        //  mProgressBottom.start();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        mTarget.measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
        mCircleView.measure(MeasureSpec.makeMeasureSpec(mCircleDiameter, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mCircleDiameter, MeasureSpec.EXACTLY));
        mCircleViewIndex = -1;
        // Get the index of the circleview.
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mCircleView) {
                mCircleViewIndex = index;
                Log.e(LOG_TAG, "mCircleViewIndex==" + mCircleViewIndex);
                break;
            }
        }

        mCircleViewBottom.measure(MeasureSpec.makeMeasureSpec(mCircleDiameter, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mCircleDiameter, MeasureSpec.EXACTLY));
        mCircleViewBottomIndex = -1;
        // Get the index of the circleview.
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mCircleViewBottom) {
                mCircleViewBottomIndex = index;
                Log.e(LOG_TAG, "mCircleViewBottomIndex==" + mCircleViewBottomIndex);
                break;
            }
        }


    }

    /**
     * Get the diameter of the progress circle that is displayed as part of the
     * swipe to refresh layout.
     *
     * @return Diameter in pixels of the progress circle view.
     * <p>获取圆环直径</p>
     */
    public int getProgressCircleDiameter() {
        return mCircleDiameter;
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (mChildScrollUpCallback != null) {
            Log.e("fish", "canChildScrollUp;mChildScrollUpCallback != null");
            return mChildScrollUpCallback.canChildScrollUp(this, mTarget);

        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            Log.e("fish", "canChildScrollUp;Build.VERSION.SDK_INT < 14");
            if (mTarget instanceof AbsListView) {
                Log.e("fish", "canChildScrollUp;mTarget instanceof AbsListView");
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                Log.e("fish", "canChildScrollUp;mTarget ！instanceof AbsListView");
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            // Log.e("fish","canChildScrollUp;android.os.Build.VERSION.SDK_INT > 14");
            //这个可以用，判断是否可以向下拉
            // boolean re = ViewCompat.canScrollVertically(mTarget,1);
            //Log.e("fish","canChildScrollDown?"+re);
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }


    //自己写的方法

    /**
     * 判断是否而已向下拉
     */
    public boolean canChildScrollDown() {
        return ViewCompat.canScrollVertically(mTarget, 1);
    }


    /**
     * Set a callback to override {@link GZoomSwifrefresh#canChildScrollUp()} method. Non-null
     * callback will return the value provided by the callback and ignore all internal logic.
     *
     * @param callback Callback that should be called when canChildScrollUp() is called.
     */
    public void setOnChildScrollUpCallback(@Nullable OnChildScrollUpCallback callback) {
        mChildScrollUpCallback = callback;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //先确定滑动的目标对象
        ensureTarget();

        final int action = MotionEventCompat.getActionMasked(ev);
        int pointerIndex;
        //点击事件开始，将标志位清空
        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }
        /**isEnabled 可用
         *
         * canChildScrollUp 子view还可以向上拉
         * */
        if (!isEnabled() || mReturningToStart || canChildScrollUp()
                || mRefreshing || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e("fish", "onInterceptTouchEvent,ACTION_DOWN");
                setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCircleView.getTop(), true);
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                mInitialDownY = ev.getY(pointerIndex);
                break;

            case MotionEvent.ACTION_MOVE://这个move基本不触发
                Log.e("fish", "onInterceptTouchEvent,ACTION_MOVE");
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
                startDragging(y);
                break;

            case MotionEventCompat.ACTION_POINTER_UP:
                Log.e("fish", "onInterceptTouchEvent,MotionEventCompat.ACTION_POINTER_UP");
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
                Log.e("fish", "onInterceptTouchEvent,motionEvent.ACTION_UP");
            case MotionEvent.ACTION_CANCEL:
                Log.e("fish", "onInterceptTouchEvent,MotionEvent.ACTION_CANCEL");
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // if this is a List < L or another view that doesn't support nested
        // scrolling, ignore this request so that the vertical scroll event
        // isn't stolen
        if ((android.os.Build.VERSION.SDK_INT < 21 && mTarget instanceof AbsListView)
                || (mTarget != null && !ViewCompat.isNestedScrollingEnabled(mTarget))) {
            // Nope.
        } else {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    // NestedScrollingParent

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //target 发起滑动的字view，可以不是当前view的直接子view
        //child 包含target的直接子view
        //返回true表示要与target配套滑动，为true则下面的accepted也会被调用
        //mReturningToStart是为了配合onTouchEvent的，这里我们不扩展
        return isEnabled() && !mReturningToStart &&  !mRefreshing &&!mRefreshingBottom //没有在刷新和返回途中
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;//竖直方向
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.e(LOG_TAG, "onNestedScrollAccepted,axes=" + axes);
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);//调用自己child接口的方法，实现向上传递
        mTotalUnconsumed = 0;
        //
        mTotalUnconsumedBottom = 0;
        mNestedScrollInProgress = true;

    }

    /**
     * 在child开始滑动之前会通知parent的这个方法，看看能不能滑动dx dy这么多
     * 这里是回调，parent可以先于child进行滑动
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        Log.e(LOG_TAG, "onNestedPreScroll,dy=" + dy + ",consume[1]==" + consumed[1]);
        if (dy > 0 && mTotalUnconsumed > 0 && !mRefreshingBottom) {//向下拖dy小于0，所以这是为了处理拖circle到一半然后又缩回去的情况
            if (dy > mTotalUnconsumed) {//拖动的很多，大于未消费的
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {//拖动一点，我们全部用给自己
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            moveSpinner(mTotalUnconsumed);//move 到这个位置
        }

        //处理底部的,圆圈已经出来了之后它又向下拖
        if (dy < 0 && mTotalUnconsumedBottom > 0 && !mRefreshing) {
            Log.e("fish", "dy<0 && mTotalUnconsumedBottom > 0+++dy==" + dy + ",mTotalUnconsumedBottom==" + mTotalUnconsumedBottom);
            if (-dy > mTotalUnconsumedBottom)//如果拖动的很多，就先给圆圈，然后还给子控件
            {
                consumed[1] = -(int) mTotalUnconsumedBottom;
                mTotalUnconsumedBottom = 0;
                mBottomIsScrolling = false;
            } else {//否则，先给父控件
                mTotalUnconsumedBottom += dy;
                consumed[1] = dy;//原来传回去的是正数，结果越滑越快。。。改成负数之后就对了，开心
            }
            moveBottomSpinner(mTotalUnconsumedBottom);
        }


        // If a client layout is using a custom start position自定义开始位置 for the circle
        // view, they mean to hide it again before scrolling the child view
        // If we get back to mTotalUnconsumed == 0 and there is more to go, hide
        // the circle so it isn't exposed if its blocking content is moved
        if (mUsingCustomStart && dy > 0 && mTotalUnconsumed == 0
                && Math.abs(dy - consumed[1]) > 0) {
            mCircleView.setVisibility(View.GONE);
        }

        // Now let our nested parent consume the leftovers
        /**计算它的父层的消耗，加上去*/
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {//父控件处理了才会返回true
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        Log.e(LOG_TAG, "onStopNestedScroll,mTotalUnconsumed=" + mTotalUnconsumed);
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0 && !mRefreshingBottom) {
            finishSpinner(mTotalUnconsumed);
            mTotalUnconsumed = 0;
        }
        if (mTotalUnconsumedBottom > 0 && !mRefreshing) {
            Log.e("fish", "onStopNestedScroll,mTotalUnconsumedBottom > 0");
            finishSpinnerBottom(mTotalUnconsumedBottom);
            mTotalUnconsumedBottom = 0;
            mBottomIsScrolling = false;
        }
        // Dispatch up our nested parent
        stopNestedScroll();
    }

    /**
     * 这里是后于child的滑动
     */
    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);
        Log.e(LOG_TAG, "onNestedScroll,dyUnconsumed=" + dyUnconsumed + "mParentOffsetInWindow[1]" + mParentOffsetInWindow[1]);
        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && !canChildScrollUp() && !mRefreshingBottom) {//向下拉
            mTotalUnconsumed += Math.abs(dy);
            moveSpinner(mTotalUnconsumed);
        } else if (dy > 0 && !canChildScrollDown() && !mRefreshing) //向上拉
        {
            mTotalUnconsumedBottom += dy;
            moveBottomSpinner(mTotalUnconsumedBottom);
            mBottomIsScrolling = true;
        }
    }

    // NestedScrollingChild

    /**
     * 子,设置嵌套滑动是否可用
     */
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    /**
     * 嵌套滑动是否可用
     */
    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    /**
     * 开始嵌套滑动（子）
     * axes表示方向，在ViewCompat.SCROLL_AXIS_HORIZONTAL横向
     * 还有纵向
     * <p>
     * 在这里面调用了mNestedScrollingChildHelper的startNestedScroll
     */
    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    /**
     * 子，停止嵌套滑动
     */
    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    /**
     * 是否有父view支持嵌套滑动
     */
    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    /**
     * 在处理滑动之后调用
     *
     * @param dxConsumed     x轴上 被消费的距离
     * @param dyConsumed     y轴上 被消费的距离
     * @param dxUnconsumed   x轴上 未被消费的距离
     * @param dyUnconsumed   y轴上 未被消费的距离
     * @param offsetInWindow view 的移动距离
     */
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        //事先拦截

        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    /**
     * 一般在滑动之前调用, 在ontouch 中计算出滑动距离, 然后 调用改 方法, 就给支持的嵌套的父View 处理滑动事件
     *
     * @param dx             x 轴上滑动的距离, 相对于上一次事件, 不是相对于 down事件的 那个距离
     * @param dy             y 轴上滑动的距离
     * @param consumed       一个数组, 可以传 一个空的 数组,  表示 x 方向 或 y 方向的事件 是否有被消费
     * @param offsetInWindow 支持嵌套滑动到额父View 消费 滑动事件后 导致 本 View 的移动距离
     * @return 支持的嵌套的父View 是否处理了 滑动事件
     */
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        Log.e("fish", "父：dispatchNestedPreScroll,dy=" + dy);
        //先拦截
        if (mBottomIsScrolling && mTotalUnconsumedBottom > 0 && dy < 0)//设置成功！但是子view还在动
        {
            Log.e("fish", "父：dispatchNestedPreScroll,mTotalUnconsumedBottom=" + mTotalUnconsumedBottom + ",dy==" + dy);
            if (-dy >= mTotalUnconsumedBottom)//向下拖动很大
            {
                moveBottomSpinner(mTotalUnconsumedBottom);
            } else {
                moveBottomSpinner(-dy);
                mTotalUnconsumedBottom -= dy;
                dy = 0;
            }
        }
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    private boolean isAnimationRunning(Animation animation) {
        return animation != null && animation.hasStarted() && !animation.hasEnded();
    }


    /**
     * 进行实际circle的滑动绘制，包括颜色等设置
     *
     * @param overscrollTop 滑动的绝对值
     */
    private void moveBottomSpinner(float overscrollTop) {
        mProgressBottom.showArrow(true);
        float originalDragPercent = overscrollTop / mTotalDragDistance;
        float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
        float adjustedPercent = (float) Math.max(dragPercent - .4, 0) * 5 / 3;//这个不理解
        float extraOS = Math.abs(overscrollTop) - mTotalDragDistance;//这样不就是负的吗//就是负的//可以是正的
        float slingshotDist = mUsingCustomStart ? mSpinnerOffsetEnd - mOriginalOffsetTop
                : mSpinnerOffsetEnd;///mSpinnerOffsetEnd 默认是拉到最底的可能位置 ，和mTotalDragDistance一开始初始化是相同的
        float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2)
                / slingshotDist);
        float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                (tensionSlingshotPercent / 4), 2)) * 2f;//tensionSlingshotPercent = x ,x/4-(x/4)^2,再*2，最多0.5
        float extraMove = (slingshotDist) * tensionPercent * 2;//这个是模拟后来的拖动，最多slingshotDist

        int targetY = mOriginalOffsetBottom - (int) ((slingshotDist * dragPercent) + extraMove);

        // where 1.0f is a full circle
        if (mCircleViewBottom.getVisibility() != View.VISIBLE) {
            mCircleViewBottom.setVisibility(View.VISIBLE);
        }
        if (!mScale) {
            ViewCompat.setScaleX(mCircleViewBottom, 1f);
            ViewCompat.setScaleY(mCircleViewBottom, 1f);
            Log.e("param", "mScale==false,in moveBottomSpinner");
        }

        if (mScale) {
            setAnimationProgress(Math.min(1f, overscrollTop / mTotalDragDistance));
        }
        if (overscrollTop < mTotalDragDistance) {
            if (mProgressBottom.getAlpha() > STARTING_PROGRESS_ALPHA
                    && !isAnimationRunning(mAlphaStartAnimationBottom)) {
                // Animate the alpha
                startProgressAlphaStartAnimationBottom();
            }
        } else {
            if (mProgressBottom.getAlpha() < MAX_ALPHA && !isAnimationRunning(mAlphaMaxAnimationBottom)) {
                // Animate the alpha
                startProgressAlphaMaxAnimationBottom();
            }
        }
        float strokeStart = adjustedPercent * .8f;
        mProgressBottom.setStartEndTrim(0f, Math.min(MAX_PROGRESS_ANGLE, strokeStart));//Trim 修剪
        mProgressBottom.setArrowScale(Math.min(1f, adjustedPercent));

        float rotation = (-0.25f + .4f * adjustedPercent + tensionPercent * 2) * .5f;
        mProgressBottom.setProgressRotation(rotation);
        setTargetOffsetTopAndBottomForBottom(targetY - mCurrentTargetOffsetBottom, true /* requires update */);


    }


    /**
     * 进行实际circle的滑动绘制，包括颜色等设置
     */
    private void moveSpinner(float overscrollTop) {
        Log.e("moveSpinner", "+++++++++++++++++++++++moveSpinner--" + overscrollTop);
        //overscrollTop = -overscrollTop;
        mProgress.showArrow(true);
        float originalDragPercent = overscrollTop / mTotalDragDistance;
        Log.e("moveSpinner", "moveSpinner--originalDragPercent==" + originalDragPercent);
        float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
        Log.e("moveSpinner", "moveSpinner--dragPercent==" + dragPercent);
        float adjustedPercent = (float) Math.max(dragPercent - .4, 0) * 5 / 3;//这个不理解
        Log.e("moveSpinner", "moveSpinner--adjustedPercent==" + adjustedPercent);
        float extraOS = Math.abs(overscrollTop) - mTotalDragDistance;//这样不就是负的吗//就是负的//可以是正的
        Log.e("moveSpinner", "moveSpinner--extraOS==" + extraOS);
        float slingshotDist = mUsingCustomStart ? mSpinnerOffsetEnd - mOriginalOffsetTop
                : mSpinnerOffsetEnd;///mSpinnerOffsetEnd 默认是拉到最底的可能位置 ，和mTotalDragDistance一开始初始化是相同的
        Log.e("moveSpinner", "moveSpinner--slingshotDist==" + slingshotDist + ",mSpinnerOffsetEnd==" + mSpinnerOffsetEnd);
        float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2)
                / slingshotDist);
        Log.e("moveSpinner", "moveSpinner--tensionSlingshotPercent==" + tensionSlingshotPercent);
        float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                (tensionSlingshotPercent / 4), 2)) * 2f;//tensionSlingshotPercent = x ,x/4-(x/4)^2,再*2  最大1/2
        Log.e("moveSpinner", "moveSpinner--tensionPercent==" + tensionPercent);
        float extraMove = (slingshotDist) * tensionPercent * 2;//我理解了，这个是模拟后来的拖动，如果
        Log.e("moveSpinner", "moveSpinner--extraMove==" + extraMove);
        int targetY = mOriginalOffsetTop + (int) ((slingshotDist * dragPercent) + extraMove);//最大可以达到 +2 slingshotDist
        Log.e("moveSpinner", "moveSpinner--targetY==" + targetY);


        // where 1.0f is a full circle
        if (mCircleView.getVisibility() != View.VISIBLE) {
            mCircleView.setVisibility(View.VISIBLE);
        }
        if (!mScale) {
            ViewCompat.setScaleX(mCircleView, 1f);
            ViewCompat.setScaleY(mCircleView, 1f);
        }

        if (mScale) {
            setAnimationProgress(Math.min(1f, overscrollTop / mTotalDragDistance));
        }
        if (overscrollTop < mTotalDragDistance) {
            if (mProgress.getAlpha() > STARTING_PROGRESS_ALPHA
                    && !isAnimationRunning(mAlphaStartAnimation)) {
                // Animate the alpha
                startProgressAlphaStartAnimation();
            }
        } else {
            if (mProgress.getAlpha() < MAX_ALPHA && !isAnimationRunning(mAlphaMaxAnimation)) {
                // Animate the alpha
                startProgressAlphaMaxAnimation();
            }
        }
        float strokeStart = adjustedPercent * .8f;
        mProgress.setStartEndTrim(0f, Math.min(MAX_PROGRESS_ANGLE, strokeStart));
        mProgress.setArrowScale(Math.min(1f, adjustedPercent));

        float rotation = (-0.25f + .4f * adjustedPercent + tensionPercent * 2) * .5f;
        mProgress.setProgressRotation(rotation);
        setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop, true /* requires update */);
        Log.e("moveSpinner", "+++++++++++++++++++++++moveSpinner--move==" + (targetY - mCurrentTargetOffsetTop));
        // setTargetOffsetTopAndBottom(10, true /* requires update */);
        //  Log.e(LOG_TAG,"mCurrentTargetOffsetTop=="+mCurrentTargetOffsetTop+",targetY=="+targetY);
    }

    private void finishSpinner(float overscrollTop) {
        Log.e("fish", "finishSpinner--" + overscrollTop);
        if (overscrollTop > mTotalDragDistance) {//大于这个核心点就升上去
            setRefreshing(true, true /* notify */);
            Log.e("fish", "finishSpinner-->>>overscrollTop > mTotalDragDistance");
        } else {
            // cancel refresh
            mRefreshing = false;
            mProgress.setStartEndTrim(0f, 0f);
            AnimationListener listener = null;
            if (!mScale) {
                listener = new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!mScale) {
                            startScaleDownAnimation(null);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                };
            }
            animateOffsetToStartPosition(mCurrentTargetOffsetTop, listener);
            mProgress.showArrow(false);
        }
    }

    /**
     * 结束下半部的spinner
     */
    private void finishSpinnerBottom(float overscrollTop) {//在小于mTotalDragDistance的时候处理的不好，会在中间停止
        Log.e("fish", "finishSpinnerBottom--" + overscrollTop);
        if (overscrollTop > mTotalDragDistance) {
            setRefreshingBottom(true, true);
            Log.e("fish", "finishSpinnerBottom-->>>overscrollTop > mTotalDragDistance");
        } else {
            // cancel refresh
            mRefreshingBottom = false;
            mProgressBottom.setStartEndTrim(0f, 0f);
            AnimationListener listener = null;
            if (!mScale) {
                listener = new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!mScale) {
                            // startScaleDownAnimation(null);
                            startScaleDownAnimationBottom(null);//倒着转
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                };
            }
            // animateOffsetToCorrectPositionBottom(mCurrentTargetOffsetBottom,listener);
            animateOffsetToStartPositionBottom(mCurrentTargetOffsetBottom, listener);
            mProgressBottom.showArrow(false);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        int pointerIndex = -1;

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }
        Log.e("fish", "-onTouchEvent-;ACTION==" + ev.getAction());
        if (!isEnabled() || mReturningToStart || canChildScrollUp()
                || mRefreshing || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e("fish", "-onTouchEvent-;ACTION_DOWN");
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                Log.e("fish", "-onTouchEvent-;ACTION_MOVE");
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = ev.getY(pointerIndex);
                startDragging(y);

                if (mIsBeingDragged) {
                    final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                    if (overscrollTop > 0 && !mRefreshingBottom) {
                        moveSpinner(overscrollTop * 2);
                    } else {
                        return false;
                    }
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                Log.e("fish", "-onTouchEvent-;MotionEventCompat.ACTION_POINTER_DOWN");
                pointerIndex = MotionEventCompat.getActionIndex(ev);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG,
                            "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                mActivePointerId = ev.getPointerId(pointerIndex);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                Log.e("fish", "-onTouchEvent-;MotionEventCompat.ACTION_POINTER_UP");
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP: {
                Log.e("fish", "-onTouchEvent-;MotionEvent.ACTION_UP");
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }

                if (mIsBeingDragged && !mRefreshingBottom) {
                    final float y = ev.getY(pointerIndex);
                    final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                    mIsBeingDragged = false;
                    finishSpinner(overscrollTop);
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
            case MotionEvent.ACTION_CANCEL:
                return false;
        }

        return true;
    }

    private void startDragging(float y) {
        final float yDiff = y - mInitialDownY;
        if (yDiff > mTouchSlop && !mIsBeingDragged) {
            mInitialMotionY = mInitialDownY + mTouchSlop;
            mIsBeingDragged = true;
            mProgress.setAlpha(STARTING_PROGRESS_ALPHA);
        }
    }

    /**
     * 启动关于mCircleView的动画mAnimateToCorrectPosition，相应的更改mFrom的值为
     * param:from
     */
    private void animateOffsetToCorrectPosition(int from, AnimationListener listener) {
        mFrom = from;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(ANIMATE_TO_TRIGGER_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mAnimateToCorrectPosition);
    }


    /**
     * 启动关于mCircleView的动画mAnimateToCorrectPosition，相应的更改mFrom的值为
     * param:from
     */
    private void animateOffsetToCorrectPositionBottom(int from, AnimationListener listener) {
        mFrom = from;
        mAnimateToCorrectPositionBottom.reset();
        mAnimateToCorrectPositionBottom.setDuration(ANIMATE_TO_TRIGGER_DURATION);
        mAnimateToCorrectPositionBottom.setInterpolator(mDecelerateInterpolator);
        if (listener != null) {
            mCircleViewBottom.setAnimationListener(listener);
        }
        mCircleViewBottom.clearAnimation();
        mCircleViewBottom.startAnimation(mAnimateToCorrectPositionBottom);
    }


    private void animateOffsetToStartPosition(int from, AnimationListener listener) {
        if (mScale) {
            // Scale the item back down
            startScaleDownReturnToStartAnimation(from, listener);
        } else {
            mFrom = from;
            mAnimateToStartPosition.reset();
            mAnimateToStartPosition.setDuration(ANIMATE_TO_START_DURATION);
            mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
            if (listener != null) {
                mCircleView.setAnimationListener(listener);
            }
            mCircleView.clearAnimation();
            mCircleView.startAnimation(mAnimateToStartPosition);
        }
    }


    private void animateOffsetToStartPositionBottom(int from, AnimationListener listener) {
        if (mScale) {
            // Scale the item back down
            startScaleDownReturnToStartAnimation(from, listener);
        } else {
            mFrom = from;
            mAnimateToStartPositionBottom.reset();
            mAnimateToStartPositionBottom.setDuration(ANIMATE_TO_START_DURATION);
            mAnimateToStartPositionBottom.setInterpolator(mDecelerateInterpolator);
            if (listener != null) {
                mCircleViewBottom.setAnimationListener(listener);
            }
            mCircleViewBottom.clearAnimation();
            mCircleViewBottom.startAnimation(mAnimateToStartPositionBottom);
        }
    }


    /**
     * 刷新的动画，将circle从mFrom动画移动到mSpinnerOffsetEnd
     */
    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop = 0;
            int endTarget = 0;
            if (!mUsingCustomStart) {
                endTarget = mSpinnerOffsetEnd - Math.abs(mOriginalOffsetTop);
            } else {
                endTarget = mSpinnerOffsetEnd;
            }
            targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));//一开始我这里有疑惑，但是后来才想到interpolatedTime是0.x的。也就相当于百分比
            int offset = targetTop - mCircleView.getTop();
            setTargetOffsetTopAndBottom(offset, false /* requires update */);
            mProgress.setArrowScale(1 - interpolatedTime);
        }
    };


    /**
     * 刷新的动画，将circle从mFrom移动到
     * 中间然后滑动
     * Bottom
     */
    private final Animation mAnimateToCorrectPositionBottom = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop = 0;
            int endTarget = 0;
            if (!mUsingCustomStart) {//false
                endTarget = (int) (mOriginalOffsetBottom - mSpinnerOffsetEnd * 1.5);//*2 - mCircleDiameter;//mOriginalOffsetTop初始是负直径
            } else {
                endTarget = mSpinnerOffsetEnd;
            }
            targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));//一开始我这里有疑惑，但是后来才想到interpolatedTime是0.x的。也就相当于百分比
            int offset = targetTop - mCircleViewBottom.getBottom();
            // Lo
            //  setTargetOffsetTopAndBottom(offset, false /* requires update */);
            setTargetOffsetTopAndBottomForBottom(offset, false);
            mProgress.setArrowScale(1 - interpolatedTime);
        }
    };

    /**
     * 回到顶部
     *
     * @param interpolatedTime 动画时间
     * @return 这里是根据当前位置 mFrom 回到 mOriginalOffsetTop
     */
    void moveToStart(float interpolatedTime) {
        int targetTop = 0;
        targetTop = (mFrom + (int) ((mOriginalOffsetTop - mFrom) * interpolatedTime));
        int offset = targetTop - mCircleView.getTop();
        setTargetOffsetTopAndBottom(offset, false /* requires update */);
    }

    /**
     * 回到顶部
     *
     * @param interpolatedTime 动画时间
     * @return 这里是根据当前位置 mFrom 回到 mOriginalOffsetTop
     */
    void moveToEnd(float interpolatedTime) {
        int targetTop = 0;
        targetTop = (mFrom + (int) ((mOriginalOffsetBottom - mFrom) * interpolatedTime));
        int offset = targetTop - mCircleViewBottom.getBottom();
        setTargetOffsetTopAndBottomForBottom(offset, false /* requires update */);
    }


    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private final Animation mAnimateToStartPositionBottom = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToEnd(interpolatedTime);
        }
    };


    private void startScaleDownReturnToStartAnimation(int from,
                                                      AnimationListener listener) {
        mFrom = from;
        if (isAlphaUsedForScale()) {
            mStartingScale = mProgress.getAlpha();
        } else {
            mStartingScale = ViewCompat.getScaleX(mCircleView);
        }
        mScaleDownToStartAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                float targetScale = (mStartingScale + (-mStartingScale * interpolatedTime));
                setAnimationProgress(targetScale);
                moveToStart(interpolatedTime);
            }
        };
        mScaleDownToStartAnimation.setDuration(SCALE_DOWN_DURATION);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleDownToStartAnimation);
    }


    /**
     * 首先，提升circle的视图，然后将ciecle向上推offset个距离
     * 其中用到的是ViewCompat.offsetTopAndBottom(View,offset);正数向下 负数向上
     * 当requiresUpdate为true时，会进行整个的重绘，如果sdk小于11
     * <p>注意，每次移动的时候都会更新mCircleViewBottom</p>
     */
    void setTargetOffsetTopAndBottomForBottom(int offset, boolean requiresUpdate) {
        mCircleViewBottom.bringToFront();//改变circle在俯视图中的位置即z坐标，将他提到最上面来
        //通过offset直接改变坐标，正数向下负数向上
        ViewCompat.offsetTopAndBottom(mCircleViewBottom, offset);
        mCurrentTargetOffsetBottom = mCircleViewBottom.getBottom();
        mCircleViewBottom.invalidate();
        if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
            invalidate();
        }
    }


    /**
     * 首先，提升circle的视图，然后将ciecle向下推offset个距离
     * 其中用到的是ViewCompat.offsetTopAndBottom(View,offset);正数向下负数向上
     * 当requiresUpdate为true时，会进行整个的重绘，如果sdk小于11
     */
    void setTargetOffsetTopAndBottom(int offset, boolean requiresUpdate) {
        Log.e(LOG_TAG, "setTargetOffsetTopAndBottom,offset==" + offset);
        mCircleView.bringToFront();//改变circle在俯视图中的位置即z坐标，将他提到最上面来
        //通过offset直接改变坐标，正数向下负数向上
        ViewCompat.offsetTopAndBottom(mCircleView, offset);
        mCurrentTargetOffsetTop = mCircleView.getTop();
        mCircleView.invalidate();
        if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
            invalidate();
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    public interface OnRefreshListener {
        /**
         * Called when a swipe gesture triggers a refresh.
         */
        void onRefresh();
    }

    /**
     * 自建的下部刷新接口
     */
    public interface OnBottomRefreshListener {
        void onBottomRefresh();
    }

    /**
     * Classes that wish to override {@link GZoomSwifrefresh#canChildScrollUp()} method
     * behavior should implement this interface.
     */
    public interface OnChildScrollUpCallback {
        /**
         * Callback that will be called when {@link GZoomSwifrefresh#canChildScrollUp()} method
         * is called to allow the implementer to override its behavior.
         *
         * @param parent GZoomSwifrefresh that this callback is overriding.
         * @param child  The child view of GZoomSwifrefresh.
         * @return Whether it is possible for the child view of parent layout to scroll up.
         */
        boolean canChildScrollUp(GZoomSwifrefresh parent, @Nullable View child);
    }

}