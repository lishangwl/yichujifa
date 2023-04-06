//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package esqeee.xieqing.com.eeeeee.ui.floatmenu;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public interface WindowBridge {
    int getX();

    int getY();

    void updatePosition(int var1, int var2);

    int getWidth();

    int getHeight();

    void updateMeasure(int var1, int var2);

    int getScreenWidth();

    int getScreenHeight();

    public static class DefaultImpl implements WindowBridge {
        DisplayMetrics mDisplayMetrics;
        private LayoutParams mWindowLayoutParams;
        private WindowManager mWindowManager;
        private View mWindowView;

        public DefaultImpl(LayoutParams windowLayoutParams, WindowManager windowManager, View windowView) {
            this.mWindowLayoutParams = windowLayoutParams;
            this.mWindowManager = windowManager;
            this.mWindowView = windowView;
        }

        public int getX() {
            return this.mWindowLayoutParams.x;
        }

        public int getY() {
            return this.mWindowLayoutParams.y;
        }

        public void updatePosition(int x, int y) {
            this.mWindowLayoutParams.x = x;
            this.mWindowLayoutParams.y = y;
            this.mWindowManager.updateViewLayout(this.mWindowView, this.mWindowLayoutParams);
        }

        public int getWidth() {
            return this.mWindowView.getWidth();
        }

        public int getHeight() {
            return this.mWindowView.getHeight();
        }

        public void updateMeasure(int width, int height) {
            this.mWindowLayoutParams.width = width;
            this.mWindowLayoutParams.height = height;
            this.mWindowManager.updateViewLayout(this.mWindowView, this.mWindowLayoutParams);
        }

        public int getScreenWidth() {
            this.ensureDisplayMetrics();
            return this.mDisplayMetrics.widthPixels;
        }

        public int getScreenHeight() {
            this.ensureDisplayMetrics();
            return this.mDisplayMetrics.heightPixels;
        }

        private void ensureDisplayMetrics() {
            if (this.mDisplayMetrics == null) {
                this.mDisplayMetrics = new DisplayMetrics();
                this.mWindowManager.getDefaultDisplay().getMetrics(this.mDisplayMetrics);
            }

        }
    }
}
