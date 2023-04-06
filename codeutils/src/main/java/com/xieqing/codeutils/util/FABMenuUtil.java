package com.xieqing.codeutils.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

/**
 * Created by rahuliyer95 on 25/02/15.
 * Utility to create FloatingActionButton menu
 */
public class FABMenuUtil {

    private static AnimatorSet menuOpenAnimation, menuCloseAnimation;
    private static boolean isShowing;
    private static float baseY = -1, baseHeight = -1;
    private static int[] menuItems;

    /**
     * Create a menu for the floating action button to perform other actions based on the menu items
     *
     * @param menuBase              The floating action button to be used to trigger the menu
     * @param menuItemDrawables     The various images to be shown as menu items. Also determines the number of menu items
     * @param menuItemClickListener The click listener where all menu item clicks are redirected to. The id for each menu item is its index in the menuItemDrawables array.
     */
    public static void setupFABMenu(final FloatingActionButton menuBase,
                                    int[] menuItemDrawables,
                                    View.OnClickListener menuItemClickListener) {

        if (baseY == menuBase.getY())
            return;
        LogUtils.eTag("FABUtils",menuBase.getY());

        baseY = menuBase.getY();
        baseHeight = menuBase.getHeight();

        menuItems = menuItemDrawables;

        menuOpenAnimation = new AnimatorSet();
        menuCloseAnimation = new AnimatorSet();


        menuOpenAnimation.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                menuBase.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowing = true;
                menuBase.setClickable(true);
            }
        });
        menuCloseAnimation.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                menuBase.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowing = false;
                menuBase.setClickable(true);
            }
        });

        menuOpenAnimation.setInterpolator(new OvershootInterpolator(2.5f));
        menuCloseAnimation.setInterpolator(new OvershootInterpolator(2.5f));

        menuOpenAnimation.setDuration(300);
        menuCloseAnimation.setDuration(300);

        for (int i = 0; i < menuItemDrawables.length; i++) {
            FloatingActionButton menuItem =
                    setupMenuItem(menuBase,
                                  i,
                                  menuItemDrawables[i],
                                  menuItemClickListener);

            setupAnimation(menuItem);
        }

        menuOpenAnimation.playTogether(ObjectAnimator.ofFloat(menuBase, View.ROTATION, 0, 45));
        menuCloseAnimation.playTogether(ObjectAnimator.ofFloat(menuBase, View.ROTATION, 45, 0));

    }

    /**
     * Show/Hide the based on the current status of the menu's visibility
     */
    public static void triggerMenu() {
        if (isShowing)
            menuCloseAnimation.start();
        else
            menuOpenAnimation.start();
    }

    public static void onSaveInstanceState(Bundle outState) {
        outState.putIntArray("drawables", menuItems);
        outState.putBoolean("isShowing", isShowing);
    }

    public static void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        isShowing = savedInstanceState.getBoolean("isShowing");
        menuItems = savedInstanceState.getIntArray("drawables");
    }
    private static Handler handler = new Handler();
    private static FloatingActionButton setupMenuItem(FloatingActionButton menuBase, int id,
                                                      int drawable,
                                                      final View.OnClickListener menuItemClickListener) {
        Context context = menuBase.getContext();
        int color;

        FloatingActionButton button = new FloatingActionButton(menuBase.getContext(), null);
        button.setTag(id);
        button.setId(id);

        button.setImageResource(drawable);
        button.setLayoutParams(menuBase.getLayoutParams());
        button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                FABMenuUtil.triggerMenu();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        menuItemClickListener.onClick(view);
                    };
                },100);

            }
        });
        ((ViewGroup) menuBase.getParent()).addView(button);
        return button;
    }

    private static void setupAnimation(final FloatingActionButton menuItem) {
        AnimatorSet openSet = new AnimatorSet();
        openSet.playTogether(
                ObjectAnimator.ofFloat(menuItem,
                        View.Y,
                        baseY,
                        baseY - baseHeight * ((int)menuItem.getTag() + 1) -
                                32 * ((int)menuItem.getTag() + 1)),
                ObjectAnimator.ofFloat(menuItem, View.ALPHA, 0, 1)
        );
        openSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                menuItem.setClickable(false);
                menuItem.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuItem.setClickable(true);
            }
        });

        AnimatorSet closeSet = new AnimatorSet();
        closeSet.playTogether(
                ObjectAnimator.ofFloat(menuItem,
                        View.Y,
                        baseY - baseHeight * ((int)menuItem.getTag() +1) -
                                32 * ((int)menuItem.getTag() + 1),
                        baseY),
                ObjectAnimator.ofFloat(menuItem, View.ALPHA, 1, 0)
        );
        closeSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                menuItem.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationStart(animation);
                menuItem.setVisibility(View.GONE);
                menuItem.setClickable(true);
            }
        });

        menuOpenAnimation.playTogether(openSet);
        menuCloseAnimation.playTogether(closeSet);

    }

}