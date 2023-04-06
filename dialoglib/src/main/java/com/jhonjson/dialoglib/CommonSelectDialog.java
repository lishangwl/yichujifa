package com.jhonjson.dialoglib;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 定制的通用选择弹出对话框，使应用内对话框风格统一
 * Created by jhonsjoin on 18/6/7.
 */
public class CommonSelectDialog extends Dialog {
    private CommonDialogController mController = null;

    public CommonSelectDialog(Context context) {
        super(context, R.style.common_dialog);
        mController = new CommonDialogController();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_select_dialog);
        installView();
    }

    private void installView() {
        mController.mTitleView = (TextView) findViewById(R.id.dialog_title);
        mController.mContentView = (TextView) findViewById(R.id.dialog_content_message);

        mController.mLayoutTwoButtons = (RelativeLayout) findViewById(R.id.dialog_two_button);
        mController.mLeftButton = (TextView) findViewById(R.id.dialog_left_button);
        mController.mRightButton = (TextView) findViewById(R.id.dialog_right_button);

        mController.mLayoutOneButtons = (RelativeLayout) findViewById(R.id.dialog_one_button);
        mController.mOneButton = (TextView) findViewById(R.id.dialog_confirm_button);

        mController.installView();
    }


    public static class Builder {
        private CommonDialogParams mParams = null;
        private CommonSelectDialog dialog = null;

        public Builder(Context context) {
            mParams = new CommonDialogParams(context);
        }

        /**
         * 设置标题
         */
        public Builder setTitle(String title) {
            mParams.mTitle = title;
            return this;
        }

        /**
         * 设置内容
         */
        public Builder setContent(String content) {
            mParams.mContent = content;
            return this;
        }

        /**
         * 设置内容
         */
        public Builder setContent(Spanned spanned) {
            mParams.mSpanned = spanned;
            return this;
        }

        /**
         * 设置位置
         */
        public Builder setContentGravity(int gravity) {
            mParams.mGravity = gravity;
            return this;
        }

        /**
         * 设置内容 字体大小
         */
        public Builder setContentSize(int size) {//此参数请直接写sp的数值即可
            mParams.mSize = size;
            return this;
        }

        /**
         * 设置一个按钮 点击监听
         */
        public Builder setOneButtonInterface(String buttonText, View.OnClickListener btn) {
            mParams.mOneButtonMsg = buttonText;
            mParams.mOneButtonInterface = btn;
            return this;
        }

        /**
         * 设置两个按钮 点击取消监听
         */
        public Builder setLeftButtonInterface(String leftButtonText, View.OnClickListener left) {
            mParams.mLeftButtonMsg = leftButtonText;
            mParams.mLeftButtonInterface = left;
            return this;
        }

        /**
         * 设置两个按钮 点击确认监听
         */
        public Builder setRightButtonInterface(String rightButtonText, View.OnClickListener right) {
            mParams.mRightButtonMsg = rightButtonText;
            mParams.mRightButtonInterface = right;
            return this;
        }

        /**
         * Dialog show
         */
        public CommonSelectDialog show() {
            final CommonSelectDialog dialog = new CommonSelectDialog(mParams.mContext);
            mParams.apply(dialog.mController);
            dialog.show();
            return dialog;
        }

        /**
         * Dialog dismiss
         */
        public void dismiss() {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }

        /**
         * 点击其他区域
         */
        public Builder setTouchAble(boolean touchAble) {
            mParams.mTouchAble = touchAble;
            return this;
        }


        private static class CommonDialogParams {
            public String mTitle = "";
            public String mContent = "";
            public Spanned mSpanned;
            public int mGravity;
            public int mSize;
            public String mLeftButtonMsg = "";
            public String mRightButtonMsg = "";
            public String mOneButtonMsg = "";
            public View.OnClickListener mLeftButtonInterface = null;
            public View.OnClickListener mRightButtonInterface = null;
            public View.OnClickListener mOneButtonInterface = null;
            public Context mContext = null;
            public boolean mTouchAble = false;

            public CommonDialogParams(Context context) {
                mContext = context;
            }

            public void apply(CommonDialogController controller) {
                controller.mTitle = mTitle;
                controller.mContent = mContent;
                controller.mSpanned = mSpanned;
                controller.mGravity = mGravity;
                controller.mSize = mSize;
                controller.mOneButtonMsg = mOneButtonMsg;
                controller.mLeftButtonMsg = mLeftButtonMsg;
                controller.mRightButtonMsg = mRightButtonMsg;
                controller.mLeftButtonInterface = mLeftButtonInterface;
                controller.mRightButtonInterface = mRightButtonInterface;
                controller.mOneButtonInterface = mOneButtonInterface;
                controller.mTouchable = mTouchAble;
            }
        }
    }

    private class CommonDialogController {
        public String mTitle = "";
        public String mContent = "";
        public Spanned mSpanned;
        public int mGravity;
        public int mSize;
        public String mLeftButtonMsg = "";
        public String mRightButtonMsg = "";
        public String mOneButtonMsg = "";
        public boolean mNeedComment = false;
        public View.OnClickListener mLeftButtonInterface = null;
        public View.OnClickListener mRightButtonInterface = null;
        public View.OnClickListener mOneButtonInterface = null;
        private TextView mTitleView = null;
        private TextView mContentView = null;
        private TextView mOneButton = null;
        private TextView mLeftButton = null;
        private TextView mRightButton = null;
        private EditText mComment = null;

        private RelativeLayout mLayoutOneButtons = null;
        private RelativeLayout mLayoutTwoButtons = null;

        private boolean mTouchable = false;


        private String getComment() {
            if (mComment != null && mComment.getText() != null) {
                return mComment.getText().toString();
            }
            return "";
        }

        private void installView() {
            if (mTitleView != null) { //标题栏
                if (mTitle == null || "".equalsIgnoreCase(mTitle)) {
                    mTitleView.setVisibility(View.GONE);
                } else {
                    mTitleView.setVisibility(View.VISIBLE);
                    mTitleView.setText(mTitle);
                }
            }
            if (mContentView != null) {  //对话框内容
                if (TextUtils.isEmpty(mContent) && mSpanned == null) {
                    mContentView.setVisibility(View.GONE);
                } else {
                    mContentView.setVisibility(View.VISIBLE);
                    if (mSpanned == null) {
                        mContentView.setText(mContent);
                    } else {
                        mContentView.setText(mSpanned);
                    }

                    if (mGravity > 0) {
                        mContentView.setGravity(mGravity);
                    }
                    if (mSize > 0) {
                        mContentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSize);
                    }
                }
            }

            if (mOneButton != null && !TextUtils.isEmpty(mOneButtonMsg)) {
                mLayoutOneButtons.setVisibility(View.VISIBLE);
                mLayoutTwoButtons.setVisibility(View.GONE);
                mOneButton.setText(mOneButtonMsg);
                mOneButton.setOnClickListener(mOneButtonInterface);
            } else {
                mLayoutTwoButtons.setVisibility(View.VISIBLE);
                mLayoutOneButtons.setVisibility(View.GONE);
                if (mLeftButton != null) { //左侧按钮
                    if (mLeftButtonMsg == null || "".equalsIgnoreCase(mLeftButtonMsg)
                            || mLeftButtonInterface == null) {
                        mLeftButton.setVisibility(View.GONE);
                    } else {
                        mLeftButton.setText(mLeftButtonMsg);
                        mLeftButton.setOnClickListener(mLeftButtonInterface);
                    }
                }
                if (mRightButton != null) {  //右侧按钮
                    if (mRightButtonMsg == null || "".equalsIgnoreCase(mRightButtonMsg)
                            || mRightButtonInterface == null) {
                        mRightButton.setVisibility(View.GONE);
                    } else {
                        mRightButton.setText(mRightButtonMsg);
                        mRightButton.setOnClickListener(mRightButtonInterface);
                    }
                }
            }
            setCanceledOnTouchOutside(mTouchable);
            setCancelable(mTouchable);
        }
    }
}
