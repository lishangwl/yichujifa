package esqeee.xieqing.com.eeeeee.bean;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.yicu.yichujifa.GlobalContext;

import org.greenrobot.eventbus.EventBus;

import esqeee.xieqing.com.eeeeee.MyApp;

public class FloatWindow {
    private View view;
    private boolean allowMove = true;
    private WindowManager.LayoutParams params;
    private String id = "";
    private View.OnClickListener clickListener;
    private MoveListener moveListener;
    WindowManager windowManager;

    public FloatWindow() {
        windowManager = (WindowManager) GlobalContext.getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    public void setMoveListener(MoveListener moveListener) {
        this.moveListener = moveListener;
    }

    @Override
    public String toString() {
        return "[ id = " + getId() + " ]";
    }

    public boolean isHide() {
        return view.getVisibility() == View.GONE;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        view.setOnClickListener(clickListener);
    }

    public MoveListener getMoveListener() {
        return moveListener;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setParams(WindowManager.LayoutParams params) {
        this.params = params;
    }

    public WindowManager.LayoutParams getParams() {
        return params;
    }

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void setAllowMove(boolean allowMove) {
        this.allowMove = allowMove;
    }

    public boolean isAllowMove() {
        return allowMove;
    }

    public void add() {
        EventBus.getDefault().post(FloatWindowAction.ADD.setWindow(this));
    }

    public void close() {
        EventBus.getDefault().post(FloatWindowAction.CLOSE.setWindow(this));
    }

    public void show() {
        view.setVisibility(View.VISIBLE);
    }

    public void hide() {
        view.setVisibility(View.GONE);
    }

    public FloatWindow x(int x) {
        params.x = x;
        return this;
    }

    public FloatWindow y(int y) {
        params.y = y;
        return this;
    }

    public void update() {
        //EventBus.getDefault().post(FloatWindowAction.UPDATE.setWindow(this));
        windowManager.updateViewLayout(getView(), getParams());
    }

    public static class FloatWindowBuilder {
        private FloatWindow floatWindow;

        public FloatWindowBuilder() {
            floatWindow = new FloatWindow();
        }

        public FloatWindowBuilder id(String id) {
            floatWindow.setId(id);
            return this;
        }

        public FloatWindowBuilder with(View view) {
            floatWindow.setView(view);
            return this;
        }

        public FloatWindowBuilder move(MoveListener listener) {
            floatWindow.setMoveListener(listener);
            return this;
        }

        public FloatWindowBuilder move(boolean allowMove) {
            floatWindow.setAllowMove(allowMove);
            return this;
        }

        public FloatWindowBuilder withClick(View.OnClickListener click) {
            floatWindow.setClickListener(click);
            return this;
        }

        public FloatWindowBuilder param(WindowManager.LayoutParams layoutParams) {
            floatWindow.setParams(layoutParams);
            return this;
        }

        public FloatWindow build() {
            return floatWindow;
        }
    }

    public static WindowManager.LayoutParams DEFAULT = new WindowManager.LayoutParams(-2, -2, MyApp.getFloatWindowType(), WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            , -3);

    public static class FloatWindowLayoutParamBuilder {
        private WindowManager.LayoutParams layoutParams;

        public FloatWindowLayoutParamBuilder() {
            layoutParams = new WindowManager.LayoutParams();
        }

        public FloatWindowLayoutParamBuilder height(int height) {
            layoutParams.height = height;
            return this;
        }

        public FloatWindowLayoutParamBuilder width(int width) {
            layoutParams.width = width;
            return this;
        }

        public FloatWindowLayoutParamBuilder type(int type) {
            layoutParams.type = type;
            return this;
        }

        public FloatWindowLayoutParamBuilder flags(int flags) {
            layoutParams.flags = flags;
            return this;
        }

        public FloatWindowLayoutParamBuilder format(int format) {
            layoutParams.format = format;
            return this;
        }

        public FloatWindowLayoutParamBuilder x(int x) {
            layoutParams.x = x;
            return this;
        }

        public FloatWindowLayoutParamBuilder y(int y) {
            layoutParams.y = y;
            return this;
        }

        public WindowManager.LayoutParams build() {
            return layoutParams;
        }
    }

    public interface MoveListener {
        void startMove(FloatWindow floatWindow, int x, int y);

        void onMove(FloatWindow floatWindow, int toXValue, int toYValue);

        void endMove(FloatWindow floatWindow, int x, int y);
    }
}
