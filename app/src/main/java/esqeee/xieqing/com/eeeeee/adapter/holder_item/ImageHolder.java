package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jhonjson.dialoglib.BottomListDialog;
import com.sevenheaven.segmentcontrol.SegmentControl;
import com.xieqing.codeutils.util.EncryptUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.dialog.MakeSizeDialog;
import esqeee.xieqing.com.eeeeee.helper.RectFloatHelper;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;
import esqeee.xieqing.com.eeeeee.listener.OnMakeSizeRectListener;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

import static android.app.Activity.RESULT_OK;

public class ImageHolder extends BaseHolder{
    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.imageVar)
    TextView var;

    @BindView(R.id.accetrue)
    SeekBar seekBar;
    @BindView(R.id.action) SegmentControl action;
    @BindView(R.id.rect) TextView rect;
    @BindView(R.id.show_rect) View view;


    private JSONBean jsonBean;
    private int currentIndex = 0;
    private Rect rectRect;

    @BindView(R.id.assign)
    ViewGroup assign;

    private FloatWindow floatWindow= null;
    public ImageHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_click_image,adapter);
    }

    @Override
    public void onDelete() {
        super.onDelete();
        //FileUtils.delete(getParam().getString("fileName"));
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        int type = jsonBean.getInt("actionType");
        this.jsonBean = jsonBean;
        JSONBean param = jsonBean.getJson("param");

        rectRect = getRect(param);
        View.OnClickListener choose = view1 -> {
            new BottomListDialog.Builder(getContext())
                    .addMenuListItem(new String[]{"截图图片","从相册选取","选择变量"},position -> {
                        if(position == 1){
                            ((BaseActivity)getContext()).addActivityResultListener(new ActivityResultListener() {
                                @Override
                                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                    ((BaseActivity)getContext()).removeActivityResultListener(this);
                                    if (requestCode == 5481 && resultCode == RESULT_OK) {
                                        List<String> result = Matisse.obtainPathResult(data);
                                        if (result.size()>0){
                                            param.put("fileName",result.get(0));
                                            imageView.setVisibility(View.VISIBLE);
                                            var.setVisibility(View.GONE);
                                            imageView.setImageBitmap(ImageUtils.scale(ImageUtils.getBitmap(param.getString("fileName")),200,200,true));
                                        }
                                    }
                                }
                            });
                            Matisse.from(((BaseActivity)getContext()))
                                    .choose(MimeType.ofImage())
                                    .countable(true)
                                    .maxSelectable(1)
                                    .isCrop(false)
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new GlideEngine())
                                    .forResult(5481);
                        }else if (position == 2){
                            List<JSONBean> strings = queryVariableByType(0);
                            String[] strings1 = new String[strings.size()];
                            for (int i = 0 ; i<strings.size() ;i++){
                                strings1[i] = strings.get(i).getString("name");
                            }
                            strings.clear();
                            new AlertDialog.Builder(getContext()).setTitle("选择["+ VariableType.values()[0].getTypeName()+"]变量").setItems(strings1,(DialogInterface dialogInterface, int i) ->{
                                var.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.GONE);
                                param.put("fileName","{"+strings1[i]+"}");
                                var.setText(strings1[i]);
                            }).create().show();
                        }else{
                            ToastUtils.showLong("再次点击准心，即可重新选取");
                            ((AddActivity)getContext()).setHookListener(v->{
                                new MakeSizeDialog(getContext()).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
                                    @Override
                                    public void onConfirnm(Rect rect) {
                                        if (!RecordAutoCaptruer.getIntance().isRequestPermission()) {
                                            RecordAutoCaptruer.getIntance().request((BaseActivity) getContext());
                                            return;
                                        }
                                        Bitmap bitmap  = ImageUtils.cutBitmap(RecordAutoCaptruer.getIntance().captrueScreen(),rect);
                                        if (bitmap == null){
                                            ToastUtils.showLong("截取失败");
                                        }else{
                                            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(System.currentTimeMillis()));
                                            File localFile = new File(ActionHelper.workSpaceImageDir, "img_" + EncryptUtils.encryptMD5ToString(time) + ".dddbbb");
                                            ImageUtils.save(bitmap,localFile, Bitmap.CompressFormat.PNG);
                                            param.put("fileName",localFile.getAbsolutePath());

                                            imageView.setVisibility(View.VISIBLE);
                                            var.setVisibility(View.GONE);
                                            imageView.setImageBitmap(ImageUtils.scale(ImageUtils.getBitmap(param.getString("fileName")),200,200,true));
                                        }
                                    }
                                }).show();
                            });

                        }
                    }).show();
        };

        if (param.getString("fileName").startsWith("{") && param.getString("fileName").endsWith("}")){
            var.setText(param.getString("fileName").replace("{","").replace("}",""));
            var.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }else{
            imageView.setImageBitmap(ImageUtils.scale(ImageUtils.getBitmap(param.getString("fileName")),200,200,true));

            imageView.setVisibility(View.VISIBLE);
            var.setVisibility(View.GONE);
        }
        var.setOnClickListener(choose);
        imageView.setOnClickListener(choose);


        seekBar.setProgress(param.getInt("accetrue",80));


        currentIndex = param.getBoolean("assign",false)?2:(type == 30 || type == 50)?0:1;
        assign.setVisibility(param.getBoolean("assign",false)?View.VISIBLE:View.GONE);
        initAssignView();
        action.setSelectedIndex(currentIndex);
        ImageHolder.this.rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));

        action.setOnSegmentControlClickListener((i)->{
            currentIndex = i;
            changedType(param);
            param.put("assign",i == 2);
            assign.setVisibility(param.getBoolean("assign",false)?View.VISIBLE:View.GONE);
            initAssignView();
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    param.put("accetrue",i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ThemeManager.attachTheme(seekBar,action);

        rect.setOnClickListener(v->{
            if (rectRect == null){
                selectRect(param);
                return;
            }
            new BottomListDialog.Builder(getContext())
                    .addMenuListItem(new String[]{"清除(即全屏)", "选取区域","选取矩阵变量"},position -> {
                        if (position == 1){
                            param.remove("rectVar");
                            selectRect(param);
                        }else if (position ==2){
                            List<JSONBean> strings = queryVariableByType(7);
                            String[] strings1 = new String[strings.size()];
                            for (int i = 0 ; i<strings.size() ;i++){
                                strings1[i] = strings.get(i).getString("name");
                            }
                            strings.clear();
                            strings = null;
                            new AlertDialog.Builder(getContext()).setTitle("选择["+ VariableType.values()[7].getTypeName()+"]变量").setItems(strings1,(DialogInterface dialogInterface, int i) ->{
                                rectRect = null;
                                clearRect(param);
                                param.put("rectVar",strings1[i]);
                                ImageHolder.this.rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));
                            }).create().show();
                        }else{
                            rectRect = null;
                            clearRect(param);
                            ImageHolder.this.rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));
                        }
                    }).show();
        });

        view.setOnClickListener(v->{
            showRect();
        });
        view.setOnLongClickListener(v->{
            showRect();
            return true;
        });
    }

    private void initAssignView() {
        if (assign.getChildCount() == 0){
            assign.addView(getVarView("横坐标",1,"x"));
            assign.addView(getVarView("纵坐标",1,"y"));
            assign.addView(getVarView("宽度",1,"w"));
            assign.addView(getVarView("高度",1,"h"));
        }
    }

    private void selectRect(JSONBean param) {
        ToastUtils.showLong("再次点击准心，即可重新选取");
        ((AddActivity)getContext()).setHookListener(v->{
            new MakeSizeDialog(getContext())
                    .setTip("请选择区域")
                    .setOnMakeSizeRectListener((rect)->{
                        rectRect = rect;
                        setRect(rect,param);
                        ImageHolder.this.rect.setText(rectRect == null ? "选取区域" :rectRect.toShortString());
                    }).show();
        });

    }



    @Override
    public JSONBean copy() {
        JSONBean jsonBean = new JSONBean(getJsonBean());
        String file = jsonBean.getJson("param").getString("fileName");

        File y = new File(file);
        File t = new File(y.getParent(),FileUtils.getFileNameNoExtension(y)+"_copy."+FileUtils.getFileExtension(y));
        FileUtils.copyFile(y,t);
        jsonBean.getJson("param").put("fileName",t.getAbsoluteFile());
        return jsonBean;
    }


    private void showRect() {
        if (rectRect == null){
            return;
        }
        RectFloatHelper.getHelper(getContext()).showRectView(rectRect);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                RectFloatHelper.getHelper(getContext()).removeRectView();
            }
        },500);
    }

    private void changedType(JSONBean param) {
        if (rectRect != null){
            if (currentIndex == 0){
                jsonBean.put("actionType",50);
            }else{
                jsonBean.put("actionType",61);
            }
        }else{
            clearRect(param);
            if (currentIndex == 0){
                jsonBean.put("actionType",30);
            }else{
                jsonBean.put("actionType",60);
            }
        }
    }



    @Override
    public void initView() {

    }

    @Override
    public int getIcon() {
        return R.drawable.ic_shitu;
    }

    @Override
    public String getName() {
        return "识别图片";
    }

}
