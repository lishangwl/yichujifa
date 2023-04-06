package esqeee.xieqing.com.eeeeee.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PathMeasure;
import android.util.Log;
import android.view.View;

import esqeee.xieqing.com.eeeeee.widget.CustomPath;

public class PathAnimation {
    private ValueAnimator animator;

    public PathAnimation(View view, CustomPath customPath, long duration) {
        PathMeasure pathMeasure = new PathMeasure(customPath, false);
        float length = pathMeasure.getLength();
        int viewWidth = view.getLayoutParams().width;
        int viewHeight = view.getLayoutParams().height;
        animator = ObjectAnimator.ofFloat(0, 1f);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float currentValue = (float) valueAnimator.getAnimatedValue() * length;
                float[] pos = new float[2];
                float[] tan = new float[2];
                pathMeasure.getPosTan(currentValue, pos, tan);

                float x = pos[0] - viewWidth / 2;
                float y = pos[1] - viewHeight / 2;


                Log.d("vvvvvv", currentValue + "[" + x + "," + y + "]");
                view.setX(x);
                view.setY(y);
            }
        });
    }

    public void start() {
        animator.start();
    }
}
