package com.xieqing.codeutils.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Transformation;

public class AnimationUtils {
    public static void circle(View view, Animator.AnimatorListener animatorListener){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(view,view.getLayoutParams().width/2,view.getLayoutParams().height/2,view.getLayoutParams().width/2,ScreenUtils.getScreenHeight()/2);
                animator.setDuration(300);
                animator.addListener(animatorListener);
                animator.start();
            }else{
                animatorListener.onAnimationEnd(null);
            }
    }

    public static void translate(final View view, float from, float to, Animator.AnimatorListener listener) {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from,to);
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        valueAnimator.addListener(listener);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                view.setY(animatedValue);
            }
        });
        valueAnimator.start();
    }

    public static void rotation(final View view, float from, float to, Animator.AnimatorListener listener){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from,to);
        valueAnimator.setDuration(300);
        valueAnimator.addListener(listener);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                view.setRotation(animatedValue);
            }
        });
        valueAnimator.start();
    }

    public static void alpha(final View view, float from, float to, Animator.AnimatorListener listener){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from,to);
        valueAnimator.setDuration(300);
        valueAnimator.addListener(listener);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                view.setAlpha(animatedValue);
            }
        });
        valueAnimator.start();
    }

    public static void toggle(final View view,final int realHeight, final boolean isExpend){
        ValueAnimator heightAnimation = !isExpend ? ValueAnimator.ofInt(0, realHeight) : ValueAnimator.ofInt(realHeight, 0);
        heightAnimation.setDuration(100);
        LogUtils.eTag("realHeight",realHeight);
        heightAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                final ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = realHeight;
                view.requestLayout();
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                final ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = realHeight;
                view.requestLayout();
                view.setVisibility(isExpend ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (int) animation.getAnimatedValue();
                final ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = val;
                view.requestLayout();
                LogUtils.eTag("heightAnimation",val);
            }
        });

        heightAnimation.start();
    }





}
