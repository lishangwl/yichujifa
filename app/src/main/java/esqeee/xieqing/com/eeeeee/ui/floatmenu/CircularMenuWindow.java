package esqeee.xieqing.com.eeeeee.ui.floatmenu;

import android.content.Context;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;

import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.SizeUtils;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class CircularMenuWindow{

    protected CircularMenuFloaty mFloaty;
    protected WindowManager mWindowManager;
    protected CircularActionMenu mCircularActionMenu;
    protected View mCircularActionView;
    protected BounceDragGesture mDragGesture;
    protected OrientationAwareWindowBridge mActionViewWindowBridge;
    protected WindowBridge mMenuWindowBridge;
    protected WindowManager.LayoutParams mActionViewWindowLayoutParams;
    protected WindowManager.LayoutParams mMenuWindowLayoutParams;
    protected View.OnClickListener mActionViewOnClickListener;
    protected float mKeepToSideHiddenWidthRadio;
    protected float mActiveAlpha = 1.0F;
    protected float mInactiveAlpha = 0.4F;
    private Context mContext;
    private OrientationEventListener mOrientationEventListener;

    public CircularMenuWindow(Context context, CircularMenuFloaty floaty) {
        this.mFloaty = floaty;
        mContext = context;

        onCreate((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE));
    }

    public void onCreate(WindowManager windowManager) {
        this.mWindowManager = windowManager;
        this.mActionViewWindowLayoutParams = this.createWindowLayoutParams();
        this.mActionViewWindowLayoutParams.width = SizeUtils.dp2px(40);
        this.mActionViewWindowLayoutParams.height = SizeUtils.dp2px(40);
        this.mMenuWindowLayoutParams = this.createWindowLayoutParams();
        this.inflateWindowViews();
        this.initWindowBridge();
        this.initGestures();
        this.setListeners();
        this.setInitialState();
        mOrientationEventListener = new OrientationEventListener(mContext) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (mActionViewWindowBridge.isOrientationChanged(mContext.getResources().getConfiguration().orientation)) {
                    keepToSide();
                }
            }
        };
        if (mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }
    }

    private void keepToSide() {
        mDragGesture.keepToEdge();
    }

    private void setInitialState() {
        keepToSide();
    }

    private void initGestures() {
        this.mDragGesture = new BounceDragGesture(this.mActionViewWindowBridge, this.mCircularActionView);
        this.mDragGesture.setKeepToSideHiddenWidthRadio(this.mKeepToSideHiddenWidthRadio);
        this.mDragGesture.setPressedAlpha(this.mActiveAlpha);
        this.mDragGesture.setUnpressedAlpha(this.mInactiveAlpha);
    }

    private void initWindowBridge() {
        this.mActionViewWindowBridge = new OrientationAwareWindowBridge(this.mActionViewWindowLayoutParams, this.mWindowManager, this.mCircularActionView, mContext);
        this.mMenuWindowBridge = new WindowBridge.DefaultImpl(this.mMenuWindowLayoutParams, this.mWindowManager, this.mCircularActionMenu);
    }

    public void setKeepToSideHiddenWidthRadio(float keepToSideHiddenWidthRadio) {
        this.mKeepToSideHiddenWidthRadio = keepToSideHiddenWidthRadio;
        if (this.mDragGesture != null) {
            this.mDragGesture.setKeepToSideHiddenWidthRadio(this.mKeepToSideHiddenWidthRadio);
        }

    }

    private WindowManager.LayoutParams createWindowLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2,-2,MyApp.getFloatWindowType(),520,-3);

        layoutParams.gravity = 51;
        return layoutParams;
    }

    private void setListeners() {
        this.setOnActionViewClickListener(v -> {
            if (isExpanded()) {
                collapse();
            } else {
                expand();
            }

        });
        if (this.mActionViewOnClickListener != null) {
            this.mDragGesture.setOnDraggedViewClickListener(this.mActionViewOnClickListener);
        }

        this.mCircularActionMenu.addOnStateChangeListener(new CircularActionMenu.OnStateChangeListenerAdapter() {
            public void onCollapsed(CircularActionMenu menu) {
                mCircularActionView.setAlpha(mInactiveAlpha);
            }

            public void onExpanded(CircularActionMenu menu) {
                mCircularActionView.setAlpha(mActiveAlpha);
            }
        });
    }

    public void setOnActionViewClickListener(View.OnClickListener listener) {
        if (this.mDragGesture == null) {
            this.mActionViewOnClickListener = listener;
        } else {
            this.mDragGesture.setOnDraggedViewClickListener(listener);
        }
    }

    public void expand() {
        this.mDragGesture.setEnabled(false);
        this.setMenuPositionAtActionView();
        //this.mMenuWindowLayoutParams.y = this.mActionViewWindowLayoutParams.y - this.mCircularActionMenu.getHeight()/2;
        //this.mMenuWindowLayoutParams.x = this.mActionViewWindowLayoutParams.x - this.mCircularActionMenu.getWidth()/2;
        //this.mWindowManager.updateViewLayout(this.mCircularActionMenu,this.mMenuWindowLayoutParams);
        if (this.mActionViewWindowBridge.getX() > this.mActionViewWindowBridge.getScreenWidth() / 2) {
            //this.mCircularActionMenu.setX(this.mCircularActionMenu.getWidth()/2 - this.mCircularActionView.getWidth()/2);
            this.mCircularActionMenu.expand(3);
        } else {
            //this.mCircularActionMenu.setX(-(this.mCircularActionMenu.getWidth()/2) + this.mCircularActionView.getWidth()/2);
            this.mCircularActionMenu.expand(5);
        }

    }

    public void setActiveAlpha(float activeAlpha) {
        this.mActiveAlpha = activeAlpha;
        if (this.mDragGesture != null) {
            this.mDragGesture.setPressedAlpha(activeAlpha);
        }

    }

    public void setInactiveAlpha(float inactiveAlpha) {
        this.mInactiveAlpha = inactiveAlpha;
        if (this.mDragGesture != null) {
            this.mDragGesture.setUnpressedAlpha(this.mInactiveAlpha);
        }

    }

    public void collapse() {
        this.mDragGesture.setEnabled(true);
        this.setMenuPositionAtActionView();
        this.mCircularActionMenu.collapse();
        this.mCircularActionView.setAlpha(this.mDragGesture.getUnpressedAlpha());
    }

    public boolean isExpanded() {
        return this.mCircularActionMenu.isExpanded();
    }

    private void setMenuPositionAtActionView() {
        int y = this.mActionViewWindowBridge.getY() - this.mCircularActionMenu.getMeasuredHeight() / 2 + this.mCircularActionView.getMeasuredHeight() / 2;
        int x;
        if (this.mActionViewWindowBridge.getX() > this.mActionViewWindowBridge.getScreenWidth() / 2) {
            x = this.mActionViewWindowBridge.getX() - this.mCircularActionMenu.getExpandedWidth() + this.mCircularActionView.getMeasuredWidth() / 2;
        } else {
            x = this.mActionViewWindowBridge.getX() - this.mCircularActionMenu.getExpandedWidth() + this.mCircularActionView.getMeasuredWidth();
        }

        this.mMenuWindowBridge.updatePosition(x, y);
    }

    private void inflateWindowViews() {
        this.mCircularActionMenu = this.mFloaty.inflateMenuItems(this);
        this.mCircularActionView = this.mFloaty.inflateActionView(this);
        this.mCircularActionMenu.setVisibility(View.GONE);
        this.mWindowManager.addView(this.mCircularActionMenu, this.mActionViewWindowLayoutParams);
        this.mWindowManager.addView(this.mCircularActionView, this.mMenuWindowLayoutParams);
    }


    public void close() {
        mOrientationEventListener.disable();
        try {
            this.mWindowManager.removeView(this.mCircularActionMenu);
            this.mWindowManager.removeView(this.mCircularActionView);
        }catch (Exception e){

        }
    }

}
