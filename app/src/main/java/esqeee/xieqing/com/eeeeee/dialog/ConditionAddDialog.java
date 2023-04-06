package esqeee.xieqing.com.eeeeee.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xieqing.codeutils.util.EncryptUtils;
import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextListener;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.listener.OnActionAddListener;
import esqeee.xieqing.com.eeeeee.listener.OnMakeSizeRectListener;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

public class ConditionAddDialog implements View.OnClickListener {
    private static ConditionAddDialog conditionAddDialog;
    private FloatWindow floatWindow;
    private Context context;
    private OnActionAddListener onActionAddListener;

    public static ConditionAddDialog getConditionAddDialog(Context context) {
        conditionAddDialog = new ConditionAddDialog();
        conditionAddDialog.setContext(context);
        return conditionAddDialog;
    }

    public void setContext(Context context) {
        this.context=context;
        if (floatWindow == null){
            String[] conditions = new String[]{
                    "是否包含文字（全屏）",
                    "是否包含文字（区域）",
                    "是否包含图片（全屏）",
                    "是否包含图片（区域）",
                    "是否包含颜色（全屏）",
                    "是否包含颜色（区域）",
                    "变量比较",
                    "取消"
            };
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(LinearLayout.VERTICAL);
            linearLayout2.setBackgroundColor(Color.parseColor("#20000000"));
            for (String condition : conditions){
                Button button2 = new Button(context);
                button2.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
                button2.setText(condition);
                button2.setOnClickListener(this);
                button2.setTextSize(10);
                linearLayout2.addView(button2);
            }
            floatWindow = new FloatWindow.FloatWindowBuilder()
                    .id("condition")
                    .with(linearLayout2)
                    .param(new FloatWindow.FloatWindowLayoutParamBuilder()
                            .height(-2).width(-2)
                            .format(-3)
                            .flags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
                            .type(MyApp.getFloatWindowType())
                        .build()
                    )
                    .move(true)
                    .build();
        }
    }

    public void addAction(JSONBean jsonObject){
        if (onActionAddListener == null){
            return;
        }
        onActionAddListener.addAction(jsonObject);
    }

    private void addCondition_rect_text(final int left, final int top, final int right, final int bottom) {
        InputTextDialog.getDialog(context).setMessage("当区域内含有该文字时符合条件")
                .addInputLine(new InputTextLine())
                .setInputTextListener(new InputTextListener() {
                    @Override
                    public void onConfirm(InputLine[] result) throws Exception{
                        JSONBean param = new JSONBean()
                                .put("text",result[0].getResult().toString())
                                .put("left",left)
                                .put("top",top)
                                .put("right",right)
                                .put("bottom",bottom);
                        JSONBean jsonObject = new JSONBean()
                                    .put("actionType",45)
                                    .put("param",param);
                        addAction(jsonObject);
                    }
                }).show();
    }

    @Override
    public void onClick(View v){
        floatWindow.close();
        new Handler().postDelayed(()->{
            if (!RecordAutoCaptruer.getIntance().isRequestPermission()) {
                RecordAutoCaptruer.getIntance().request((BaseActivity) context);
                return;
            }
            bitmap = RecordAutoCaptruer.getIntance().captrueScreen();
            onClick(((Button)v).getText().toString());
        },200);
    }


    private Bitmap bitmap;

    public void onClick(String position) {
        switch (position){
            case "是否包含文字（全屏）":
                addCondition_text();
                break;
            case "是否包含文字（区域）":
                new MakeSizeDialog(context).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
                    @Override
                    public void onConfirnm(Rect rect) {
                        addCondition_rect_text(rect.left,rect.top,rect.right,rect.bottom);
                    }
                }).setTip("请先选择区域").show();
                break;
            case "是否包含图片（全屏）":
                addCondition_image();
                break;
            case "是否包含图片（区域）":
                new MakeSizeDialog(context).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
                    @Override
                    public void onConfirnm(Rect rect) {
                        addCondition_rect_image(rect.left,rect.top,rect.right,rect.bottom);
                    }
                }).setTip("请先选择区域").show();
                break;
            case "是否包含颜色（全屏）":
                new SelectColorDialog((BaseActivity) context, bitmap, (int color,String h) ->{
                    int red = Color.red(color);
                    int green = Color.green(color);
                    int blue = Color.blue(color);
                    addAction(new JSONBean().put("actionType",54)
                            .put("witeTime",1000)
                            .put("param",new JSONBean()
                                    .put("blue",blue)
                                    .put("red",red)
                                    .put("green",green)));
                }).show();
                break;
            case "是否包含颜色（区域）":
                new MakeSizeDialog(context).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
                    @Override
                    public void onConfirnm(Rect rect) {
                        new SelectColorDialog((BaseActivity) context, bitmap, (int color,String h) ->{
                            int red = Color.red(color);
                            int green = Color.green(color);
                            int blue = Color.blue(color);
                            addAction(new JSONBean().put("actionType",63)
                                    .put("witeTime",1000)
                                    .put("param",new JSONBean()
                                            .put("left",rect.left)
                                            .put("top",rect.top)
                                            .put("right",rect.right)
                                            .put("bottom",rect.bottom)
                                            .put("blue",blue)
                                            .put("red",red)
                                            .put("green",green)));
                        }).show();
                    }
                }).setTip("请先选择区域").show();
                break;
            case "变量比较":
                addAction(new JSONBean().put("actionType",72).put("param",new JSONBean()));
                break;
            case "取消":
                //addAction(new JSONBean().put("actionType",72).put("param",new JSONBean()));
                break;
        }
    }
    private void addCondition_rect_image(final int left, final int top, final int right, final int bottom) {
        new MakeSizeDialog(context).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
            @Override
            public void onConfirnm(Rect rect) {
                bitmap  = ImageUtils.cutBitmap(bitmap,rect);
                if (bitmap == null){
                    ToastUtils.showLong("截取失败");
                }else{
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(System.currentTimeMillis()));
                    File localFile = new File(ActionHelper.workSpaceImageDir, "img_" + EncryptUtils.encryptMD5ToString(time) + ".dddbbb");
                    ImageUtils.save(bitmap,localFile, Bitmap.CompressFormat.PNG);
                    JSONBean param = new JSONBean()
                            .put("fileName",localFile.getAbsolutePath())
                            .put("left",left)
                            .put("top",top)
                            .put("right",right)
                            .put("bottom",bottom);
                    JSONBean jsonObject = new JSONBean()
                            .put("actionType",48)
                            .put("param",param);
                    addAction(jsonObject);
                }
            }
        }).show();
    }
    private void addCondition_image() {
        new MakeSizeDialog(context).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
            @Override
            public void onConfirnm(Rect rect) {
                bitmap  = ImageUtils.cutBitmap(bitmap,rect);
                if (bitmap == null){
                    ToastUtils.showLong("截取失败");
                }else{
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(System.currentTimeMillis()));
                    File localFile = new File(ActionHelper.workSpaceImageDir, "img_" + EncryptUtils.encryptMD5ToString(time) + ".dddbbb");
                    ImageUtils.save(bitmap,localFile, Bitmap.CompressFormat.PNG);
                    addAction(new JSONBean().put("actionType",47)
                                .put("witeTime",1000)
                                .put("param",new JSONBean().put("fileName",localFile.getAbsolutePath())));
                }
            }
        }).show();
    }
    private void addCondition_text() {
        InputTextDialog.getDialog(context).setMessage("当屏幕含有该文字时符合条件")
                .addInputLine(new InputTextLine())
                .setInputTextListener(new InputTextListener() {
                    @Override
                    public void onConfirm(InputLine[] result) throws Exception{
                        JSONBean jsonObject = new JSONBean();
                        JSONBean param = new JSONBean();
                        jsonObject.put("actionType",44);
                        param.put("text",result[0].getResult().toString());
                        jsonObject.put("param",param);
                        addAction(jsonObject);
                    }
                }).show();
    }

    public void show(OnActionAddListener actionAddListener) {
        this.onActionAddListener = actionAddListener;
        floatWindow.add();
    }


}