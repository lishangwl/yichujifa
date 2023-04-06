
package com.jhonjson.dialoglib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhonjson.dialoglib.interfaces.OnClickPositionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 定制的通用中部弹出对话框，使应用内对话框风格统一
 * Created by jhonsjoin on 18/5/18.
 */
public class MiddleListDialog extends Dialog {
    public static Context mContext;
    private static List<MiddleListMenuItem> btnMenu;

    public static class MiddleListMenuItem {
        private String content;
        private OnClickPositionListener clickListener;
        private int color = Color.parseColor("#4a4a4a");
        private int textsize = 0;
        /**
         * @param content 显示内容
         * @param clickListener   监听回调
         * @param color   字体颜色
         * */
        public MiddleListMenuItem(String content, OnClickPositionListener clickListener, int color) {
            this.content = content;
            this.clickListener = clickListener;
            this.color = color;
        }
        /**
         * @param content 显示内容
         * @param clickListener   监听回调
         * @param color   字体颜色
         * @param textsize  字体大小
         * */
        public MiddleListMenuItem(String content, OnClickPositionListener clickListener, int color, int textsize) {
            this.content = content;
            this.clickListener = clickListener;
            this.color = color;
            this.textsize = textsize;
        }
        /**
         * @param content 显示内容
         * @param color   字体颜色
         * @param textsize  字体大小
         * */
        public MiddleListMenuItem(String content, int color, int textsize) {
            this.content = content;
            this.color = color;
            this.textsize = textsize;
        }
        /**
         * @param content 显示内容
         * @param clickListener   监听回调
         * */
        public MiddleListMenuItem(String content, OnClickPositionListener clickListener) {
            this.content = content;
            this.clickListener = clickListener;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public OnClickPositionListener getClickListener() {
            return clickListener;
        }

        public void setClickListener(OnClickPositionListener clickListener) {
            this.clickListener = clickListener;
        }
    }

    protected MiddleListDialog(Context context) {
        super(context, R.style.common_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        win.setWindowAnimations(R.style.listDialogWindowAnim);
        win.setAttributes(lp);

        setContentView(R.layout.common_middle_list_dialog);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        initView();
    }

    /*初始化*/
    private void initView() {
        findViewById(R.id.menu_base_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.menu_base_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        LinearLayout lyContents = (LinearLayout) findViewById(R.id.menu_content);
        if (btnMenu != null && btnMenu.size() > 0) {
            for (int i = 0; i < btnMenu.size(); i++) {
                final int index = i;
                View v = View.inflate(mContext, R.layout.common_middle_list_dialog_item, null);
                TextView mTvContent = (TextView) v.findViewById(R.id.mTv_show);
                mTvContent.setText(btnMenu.get(i).getContent());
                mTvContent.setTextColor(btnMenu.get(i).color);
                mTvContent.setPadding(20, 0, 20, 0);
                mTvContent.setGravity(Gravity.CENTER);
                if (btnMenu.get(i).textsize != 0) {
                    mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, btnMenu.get(i).textsize);
                }
                final OnClickPositionListener mOnClickPositionListener = btnMenu.get(i).getClickListener();
                mTvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mOnClickPositionListener) {
                            mOnClickPositionListener.onClickPosition(index);
                        }
                        dismiss();
                    }
                });
                lyContents.addView(v);
            }
        }
    }

    public static class Builder {

        public Builder(Context context) {
            mContext = context;
            btnMenu = new ArrayList<MiddleListMenuItem>();
        }
        /**
         * @param item 对象
         * */
        public Builder addMenuItem(MiddleListMenuItem item) {
            btnMenu.add(item);
            return this;
        }
        /**
         * @param mReportList 数组
         * @param clickListener  item回调
         * */
        public Builder addMenuListItem(String[] mReportList, OnClickPositionListener clickListener) {
            MiddleListMenuItem item = null;
            for (int i = 0; i < mReportList.length; i++) {
                item = new MiddleListMenuItem(mReportList[i], clickListener);
                btnMenu.add(item);
            }
            return this;
        }

        public MiddleListDialog show() {
            if (null != mContext && (!(mContext instanceof Activity) || !((Activity) mContext).isFinishing())) {
                MiddleListDialog dialog = new MiddleListDialog(mContext);
                dialog.show();
                return dialog;
            }
            return null;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        btnMenu = null;
        mContext = null;
    }
}
