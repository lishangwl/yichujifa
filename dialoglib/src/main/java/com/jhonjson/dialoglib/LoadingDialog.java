package com.jhonjson.dialoglib;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * 定制的通用loading对话框，使应用内对话框风格统一
 * Created by jhonsjoin on 18/8/28.
 */
public class LoadingDialog extends Dialog {
    private boolean isTouchCancel = true;
    private ImageView mImageView;
    private static AnimationDrawable mAniDrawable;

    public LoadingDialog(Context context) {
        super(context, R.style.common_dialog);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_loading);
        mImageView = (ImageView) findViewById(R.id.progress_image);
    }

    /**
     * 是否可以取消
     *
     * @param cancel 是否可以取消
     * @return this
     */
    public LoadingDialog setTouchCanceled(boolean cancel) {
        isTouchCancel = cancel;
        return this;
    }

    @Override
    public void show() {
        super.show();
        if (null != mImageView) {
            mImageView.setImageResource(R.drawable.progress_loading_ani);
            mAniDrawable = (AnimationDrawable) mImageView.getDrawable();
            if (null != mAniDrawable) {
                mAniDrawable.start();
            }
        }
        setCanceledOnTouchOutside(isTouchCancel);
    }

    @Override
    public void dismiss() {
        try {
            if (null != mAniDrawable) {
                if (mAniDrawable.isRunning()) {
                    mAniDrawable.stop();
                }
                mAniDrawable = null;
            }
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}