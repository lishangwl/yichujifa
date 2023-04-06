package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jhonjson.dialoglib.BottomListDialog;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.dialog.MakeSizeDialog;
import esqeee.xieqing.com.eeeeee.dialog.SelectColorDialog;
import esqeee.xieqing.com.eeeeee.helper.RectFloatHelper;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

public class ColorCondition extends RelativeLayout {
    @BindView(R.id.color_header_show)
    View color_header_show;


    @BindView(R.id.accetrue)
    SeekBar seekBar;

    @BindView(R.id.show_rect)
    View view;

    @BindView(R.id.image)
    View imageView;

    @BindView(R.id.rect)
    TextView rect;


    Rect rectRect;

    public ColorCondition(@NonNull Context context, JSONBean condition,BaseHolder holder) {
        super(context);
        addView(View.inflate(context,R.layout.holder_condition_color,null));
        ButterKnife.bind(this,this);

        JSONBean param = condition.getJson("param");
        int color = Color.argb(255,param.getInt("red"),param.getInt("green"),param.getInt("blue"));
        color_header_show.setBackgroundColor(color);
        seekBar.setProgress(255 - param.getInt("accetrue",5));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    param.put("accetrue",255 - i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        imageView.setBackgroundColor(color);
        imageView.setOnClickListener(view1 -> {
            new BottomListDialog.Builder(getContext())
                    .addMenuListItem(new String[]{"重新选取颜色"},position -> {

                        ToastUtils.showLong("再次点击准心，即可重新选取");
                        ((AddActivity)getContext()).setHookListener(v->{
                            if (!RecordAutoCaptruer.getIntance().isRequestPermission()) {
                                RecordAutoCaptruer.getIntance().request((BaseActivity) context);
                                return;
                            }
                            new SelectColorDialog(((BaseActivity)getContext()), RecordAutoCaptruer.getIntance().captrueScreen(), new SelectColorDialog.SelectColor() {
                                @Override
                                public void select(int color,String colorHex) {
                                    imageView.setBackgroundColor(color);
                                    color_header_show.setBackgroundColor(color);
                                    int red = Color.red(color);
                                    int green = Color.green(color);
                                    int blue = Color.blue(color);
                                    param.put("blue",blue)
                                            .put("red",red)
                                            .put("green",green);

                                }
                            }).show();
                        });

                    }).show();
        });

        rectRect = getRect(param);
        rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));
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
                            clearRect(param);
                            ColorCondition.this.rect.setText(rectRect == null ? "选取区域" :rectRect.toShortString());
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
        ThemeManager.attachTheme(seekBar);
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
                        setRect(rect,param);
                        ColorCondition.this.rect.setText(rectRect == null ? "选取区域" :rectRect.toShortString());
                    }).show();
        });

    }

    public Rect getRect(JSONBean param){
        int left = param.getInt("left",-1);
        int top = param.getInt("top",-1);
        int right = param.getInt("right",-1);
        int bottom = param.getInt("bottom",-1);
        if (left == -1 || top == -1 || right == -1 || bottom == -1){
            return null;
        }
        return new Rect(left,top,right,bottom);
    }

    public void setRect(Rect rect,JSONBean param){
        param.put("left",rect.left);
        param.put("top",rect.top);
        param.put("right",rect.right);
        param.put("bottom",rect.bottom);
    }

    public void clearRect(JSONBean param){
        param.remove("left");
        param.remove("top");
        param.remove("right");
        param.remove("bottom");
    }
}
