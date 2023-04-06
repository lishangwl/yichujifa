package esqeee.xieqing.com.eeeeee.listener;

import android.animation.Animator;
import android.animation.ValueAnimator;

import com.xieqing.codeutils.util.ScreenUtils;

import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.tilefloat.TileFloat;

/*
    *   磁贴效果
    * */
 public class TileMoveListener implements FloatWindow.MoveListener{
        boolean isLeft = true;
        public TileMoveListener(){

        }
        @Override
        public void startMove(FloatWindow floatWindow,int x, int y) {
            floatWindow.getView().setX(0);
            floatWindow.getView().setAlpha(1f);
        }

        @Override
        public void onMove(FloatWindow floatWindow,int toXValue, int toYValue) {

        }

        @Override
        public void endMove(FloatWindow floatWindow,int x, int y) {
            if(!SettingsPreference.canTileFloatMenu()){
                return;
            }
            int centerX = x + floatWindow.getView().getWidth()/2;
            move(floatWindow,y,x,centerX < ScreenUtils.getScreenWidth()/2);
        }

        private void move(FloatWindow floatWindow, int y,int x,boolean isLeft) {
            this.isLeft = isLeft;
            int viewWidth = floatWindow.getView().getWidth();
            int maxX = ScreenUtils.getScreenWidth()-viewWidth;
            ValueAnimator valueAnimator = isLeft?ValueAnimator.ofInt(x,0):ValueAnimator.ofInt(x,maxX);
            valueAnimator.setDuration(200);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int currentX = (int) valueAnimator.getAnimatedValue();
                    floatWindow.x(currentX).y(y).update();
                }
            });
            valueAnimator.start();


            //磁弹效果往回
            valueAnimator = isLeft?ValueAnimator.ofInt(0,100):ValueAnimator.ofInt(maxX,maxX-100);
            valueAnimator.setDuration(100);
            valueAnimator.setStartDelay(200);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int currentX = (int) valueAnimator.getAnimatedValue();
                    floatWindow.x(currentX).y(y).update();
                }
            });
            valueAnimator.start();

            //磁弹效果往回
            valueAnimator = isLeft?ValueAnimator.ofInt(100,0):ValueAnimator.ofInt(maxX-100,maxX);
            valueAnimator.setDuration(100);
            valueAnimator.setStartDelay(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int currentX = (int) valueAnimator.getAnimatedValue();
                    floatWindow.x(currentX).y(y).update();
                }
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    floatWindow.getView().setAlpha(0.5f);
                    floatWindow.getView().setX(isLeft?-viewWidth/2:viewWidth/2);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            valueAnimator.start();
        }
    }