package com.yicu.yichujifa.LayoutHierarchy;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.SizeUtils;
import com.xieqing.codeutils.util.Utils;
import com.yicu.yichujifa.GlobalContext;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.listener.TileMoveListener;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.widget.CircleImageView;
import esqeee.xieqing.com.eeeeee.widget.FDialog;

import static android.content.Context.CLIPBOARD_SERVICE;

public class LayoutHierarchy implements View.OnClickListener{
    private static LayoutHierarchy instance = new LayoutHierarchy();
    public static LayoutHierarchy getInstance() {
        return instance;
    }

    public static void open(){
        instance.floatWindow.add();
    }
    public static void close(){
        instance.floatWindow.close();
    }



    FloatWindow floatWindow;
    FDialog chooseDialog;
    ListLayoutHierarchy listLayoutHierarchy;
    BoundsLayoutHierarchy boundsLayoutHierarchy;

    private LayoutHierarchy(){
        initFloaty();
        initDialog();
    }

    public void showDialog(){
        chooseDialog.show();
    }
    private void initDialog() {
        String[] chooes = new String[]{"分析布局(列表)","分析布局(矩形)","查看当前窗口类名"};
        chooseDialog = new FDialog(GlobalContext.getContext())
                .setTitle("选择操作")
                .setCanfirm("",null)
                .setDismissListener(d->{

                }).setItems(chooes,i->{
                    new Handler().postDelayed(()->{
                        onSelectAction(i);
                    },500);
                });
    }


    private void onSelectAction(int index){
        switch (index){
            case 0:
                showListLayoutHierarchy();
                break;
            case 1:
                showBoundsLayoutHierarchy();
                break;
            case 2:
                showActivity();
                break;
        }
    }

    private void showActivity() {
        if (!AccessbilityUtils.isAccessibilitySettingsOn(GlobalContext.getContext())){
            AccessbilityUtils.toSetting();
            return;
        }
        String activity = AccessbilityUtils.getService().getCurrentWindowClassName();
        new FDialog(GlobalContext.getContext())
                .setTitle("当前窗口类名")
                .setMessage(activity)
                .setCanfirm("复制",v->{
                    ((ClipboardManager) Utils.getApp().getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("一触即发--剪贴板",activity));
                })
                .setCannel("确定",null).show();
    }

    private void showBoundsLayoutHierarchy() {
        if (!AccessbilityUtils.isAccessibilitySettingsOn(GlobalContext.getContext())){
            AccessbilityUtils.toSetting();
            return;
        }
        if (boundsLayoutHierarchy == null){
            boundsLayoutHierarchy = new BoundsLayoutHierarchy();
        }

        boundsLayoutHierarchy.add();
    }

    private void showListLayoutHierarchy() {
        if (!AccessbilityUtils.isAccessibilitySettingsOn(GlobalContext.getContext())){
            AccessbilityUtils.toSetting();
            return;
        }
        if (listLayoutHierarchy == null){
            listLayoutHierarchy = new ListLayoutHierarchy();
        }

        listLayoutHierarchy.add();
    }

    private void initFloaty() {
        floatWindow = new FloatWindow.FloatWindowBuilder()
                .id("LayoutHierarchy")
                .move(true)
                .with(getIconView())
                .withClick(this)
                .move(new TileMoveListener())
                .param(getLayoutParam())
                .build();
    }

    private View getIconView() {
        CircleImageView imageView = new CircleImageView(GlobalContext.getContext());
        imageView.setBorderColor(Color.WHITE);
        imageView.setBorderWidth(5);
        imageView.setImageResource(R.drawable.jb_256);
        imageView.setAlpha(0.5f);
        return imageView;
    }

    private WindowManager.LayoutParams getLayoutParam(){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2,-2, MyApp.getFloatWindowType(),WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,-3);
        layoutParams.width = SizeUtils.dp2px(40);
        layoutParams.height = SizeUtils.dp2px(40);
        layoutParams.x = 0;
        layoutParams.gravity = Gravity.LEFT|Gravity.TOP;
        layoutParams.y = ScreenUtils.getScreenHeight()/3;
        return layoutParams;
    }



    @Override
    public void onClick(View view) {
        chooseDialog.show();
    }

}
