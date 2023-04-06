package com.jhonjson.dialoglib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.jhonjson.dialoglib.interfaces.OnClickPositionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 定制的通用底部弹出对话框，使应用内对话框风格统一
 * Created by jhonsjoin on 18/5/16.
 */
public class BottomListDialog extends Dialog implements View.OnClickListener {

    public static Context mContext;

    private static List<BottomListMenuItem> btnMenu;

    public BottomListDialog(Context mContext, List<BottomListMenuItem> btnMenu) {
        super(mContext);
        this.btnMenu = btnMenu;
    }

    public List<BottomListMenuItem> getBtnMenu() {
        return btnMenu;
    }

    public static class BottomListMenuItem {
        private String content;
        private OnClickPositionListener clickListener;
        private int color = Color.parseColor("#4a4a4a");
        private int textsize = 0;
        private Drawable drawable;

        /**
         * @param content 显示内容
         * @param clickListener   监听回调
         * @param color   字体颜色
         * */
        public BottomListMenuItem(String content, OnClickPositionListener clickListener, int color) {
            this.content = content;
            this.clickListener = clickListener;
            this.color = color;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public BottomListMenuItem setDrawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        /**
         * @param content 显示内容
         * @param clickListener   监听回调
         * @param color   字体颜色
         * @param textsize  字体大小
         * */
        public BottomListMenuItem(String content, OnClickPositionListener clickListener, int color, int textsize) {
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
        public BottomListMenuItem(String content, int color, int textsize) {
            this.content = content;
            this.color = color;
            this.textsize = textsize;
        }


        /**
         * @param content 显示内容
         * @param clickListener   监听回调
         * */
        public BottomListMenuItem(String content, OnClickPositionListener clickListener) {
            this.content = content;
            this.clickListener = clickListener;
        }

        /**
         * @param content 显示内容
         * @param clickListener   监听回调
         * */
        public BottomListMenuItem(String content, Drawable drawable,OnClickPositionListener clickListener) {
            this.content = content;
            this.clickListener = clickListener;
            this.drawable = drawable;
        }


        public BottomListMenuItem(String content) {
            this.content = content;
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

    public BottomListDialog(@NonNull Context context) {
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

        win.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        win.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        win.setWindowAnimations(R.style.listDialogWindowAnim);

        win.setAttributes(lp);
        setContentView(R.layout.common_bottom_list_dialog);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        initView();
    }


    private void initView() {

        LinearLayout lyContents = (LinearLayout) findViewById(R.id.menu_content);
        if (btnMenu != null && btnMenu.size() > 0) {
            for (int i = 0; i < btnMenu.size(); i++) {
                final int index = i;
                View v = View.inflate(mContext, R.layout.common_bottom_list_dialog_item, null);
                if (i == 0) {
                    v.findViewById(R.id.menu_line).setVisibility(View.GONE);
                    //v.setBackgroundResource(R.drawable.dialog_top_radius_bg);
                    v.setBackgroundColor(mContext.getResources().getColor(R.color.com_font_color_FFFFF));
                } else {
                    v.setBackgroundColor(mContext.getResources().getColor(R.color.com_font_color_FFFFF));
                }

                if (btnMenu.get(i).getDrawable()!=null){
                    ((ImageView)v.findViewById(R.id.src_icon)).setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ((ImageView)v.findViewById(R.id.src_icon)).setImageTintList(ColorStateList.valueOf(Color.parseColor("#008B8B")));
                    }
                    ((ImageView)v.findViewById(R.id.src_icon)).setImageDrawable(btnMenu.get(i).getDrawable());
                }

                TextView mTvContent = (TextView) v.findViewById(R.id.menu_button);
                mTvContent.setText(btnMenu.get(i).getContent());
                mTvContent.setTextColor(btnMenu.get(i).color);
                mTvContent.setPadding(20, 0, 20, 0);
                mTvContent.setGravity(Gravity.CENTER);
                if (btnMenu.get(i).textsize != 0) {
                    mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, btnMenu.get(i).textsize);
                }
                final OnClickPositionListener mOnClickPositionListener = btnMenu.get(i).getClickListener();
                v.setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.menu_base_content).setOnClickListener(this);
        findViewById(R.id.menu_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.menu_cancel || i == R.id.menu_base_content) {
            dismiss();
        }

    }

    public static class Builder {
        public Builder(Context context) {
            mContext = context;
            btnMenu = new ArrayList<BottomListMenuItem>();
        }

        /**
         * @param item 对象
         * */
        public Builder addMenuItem(BottomListMenuItem item) {
            btnMenu.add(item);
            return this;
        }

        /**
         * @param mReportList 数组
         * @param clickListener  item回调
         * */
        public Builder addMenuListItem(String[] mReportList, OnClickPositionListener clickListener) {
            BottomListMenuItem item = null;
            for (int i = 0; i < mReportList.length; i++) {
                item = new BottomListMenuItem(mReportList[i], clickListener);
                btnMenu.add(item);
            }
            return this;
        }

        /**
         * @param mReportList 数组
         * @param clickListener  item回调
         * */
        public Builder addMenuListItem(String[] mReportList,Drawable[] drawables,OnClickPositionListener clickListener) {
            BottomListMenuItem item = null;
            for (int i = 0; i < mReportList.length; i++) {
                item = new BottomListMenuItem(mReportList[i], clickListener);
                item.setDrawable(drawables.length > i?drawables[i]:null);
                btnMenu.add(item);
            }
            return this;
        }
        /**
         * @param mReportList 数组
         * @param clickListener  item回调
         * */
        public Builder addMenuListItem(String[] mReportList,int[] drawables,OnClickPositionListener clickListener) {
            BottomListMenuItem item = null;
            for (int i = 0; i < mReportList.length; i++) {
                item = new BottomListMenuItem(mReportList[i], clickListener);
                item.setDrawable(drawables.length > i?mContext.getResources().getDrawable(drawables[i]):null);
                btnMenu.add(item);
            }
            return this;
        }
        public BottomListDialog create(){
            BottomListDialog dialog = new BottomListDialog(mContext);
            return dialog;
        }

        public BottomListDialog show() {
            if (null != mContext && (!(mContext instanceof Activity) || !((Activity) mContext).isFinishing())) {
                BottomListDialog dialog = new BottomListDialog(mContext);
                dialog.show();
                return dialog;
            }
            return null;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //btnMenu = null;
        //mContext = null;
    }
}
