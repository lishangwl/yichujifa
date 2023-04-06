package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jhonjson.dialoglib.BottomListDialog;
import com.xieqing.codeutils.util.EncryptUtils;
import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.dialog.MakeSizeDialog;
import esqeee.xieqing.com.eeeeee.helper.RectFloatHelper;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;
import esqeee.xieqing.com.eeeeee.listener.OnMakeSizeRectListener;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

import static android.app.Activity.RESULT_OK;

public class ImageCondition extends RelativeLayout {

    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.accetrue) SeekBar seekBar;
    @BindView(R.id.rect) TextView rect;
    @BindView(R.id.show_rect) View view;

    private Rect rectRect;


    @BindView(R.id.image_header_show)
    ImageView image_header_show;
    BaseHolder holder;
    public ImageCondition(@NonNull Context context, JSONBean condition, BaseHolder holder) {
        super(context);
        this.holder = holder;
        addView(View.inflate(context,R.layout.holder_condition_image,null));
        ButterKnife.bind(this,this);

        JSONBean param = condition.getJson("param");

        imageView.setImageBitmap(ImageUtils.scale(ImageUtils.getBitmap(param.getString("fileName")),100,100,true));
        image_header_show.setImageDrawable(imageView.getDrawable());
        imageView.setOnClickListener(view1 -> {
            new BottomListDialog.Builder(getContext())
                    .addMenuListItem(new String[]{"截图图片","从相册选取"},position -> {
                        if(position == 1){
                            ((BaseActivity)getContext()).addActivityResultListener(new ActivityResultListener() {
                                @Override
                                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                    ((BaseActivity)getContext()).removeActivityResultListener(this);
                                    if (requestCode == 5481 && resultCode == RESULT_OK) {
                                        List<String> result = Matisse.obtainPathResult(data);
                                        if (result.size()>0){
                                            param.put("fileName",result.get(0));
                                            imageView.setImageBitmap(ImageUtils.scale(ImageUtils.getBitmap(param.getString("fileName")),200,200,true));
                                        }
                                    }
                                }
                            });
                            Matisse.from(((BaseActivity)getContext()))
                                    .choose(MimeType.ofImage())
                                    .countable(true)
                                    .maxSelectable(1)
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new GlideEngine())
                                    .forResult(5481);
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
                                            imageView.setImageBitmap(ImageUtils.scale(ImageUtils.getBitmap(param.getString("fileName")),200,200,true));
                                        }
                                    }
                                }).show();
                            });

                        }
                    }).show();
        });

        seekBar.setProgress(param.getInt("accetrue",80));

        rectRect = holder.getRect(param);
        rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));

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


        rect.setOnClickListener(v->{
            if (rectRect == null){
                selectRect(param);
                return;
            }
            new BottomListDialog.Builder(getContext())
                    .addMenuListItem(new String[]{"清除(即全屏)", "重新选取","选取矩阵变量"},position -> {
                        if (position == 1){
                            selectRect(param);
                        }else if (position ==2){
                            List<JSONBean> strings = holder.queryVariableByType(7);
                            String[] strings1 = new String[strings.size()];
                            for (int i = 0 ; i<strings.size() ;i++){
                                strings1[i] = strings.get(i).getString("name");
                            }
                            strings.clear();
                            strings = null;
                            new AlertDialog.Builder(getContext()).setTitle("选择["+ VariableType.values()[7].getTypeName()+"]变量").setItems(strings1,(DialogInterface dialogInterface, int i) ->{
                                rectRect = null;
                                holder.clearRect(param);
                                param.put("rectVar",strings1[i]);
                                rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));
                            }).create().show();
                        }else{
                            rectRect = null;
                            holder.clearRect(param);
                            ImageCondition.this.rect.setText(rectRect == null ? "选取区域" :rectRect.toShortString());
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

    private void selectRect(JSONBean param) {
        ToastUtils.showLong("再次点击准心，即可重新选取");
        ((AddActivity)getContext()).setHookListener(v->{
            new MakeSizeDialog(getContext())
                    .setTip("请选择区域")
                    .setOnMakeSizeRectListener((rect)->{
                        rectRect = rect;
                        holder.setRect(rect,param);
                        ImageCondition.this.rect.setText(rectRect == null ? "选取区域" :rectRect.toShortString());
                    }).show();
        });

    }

}
