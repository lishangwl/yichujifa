package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jhonjson.dialoglib.BottomListDialog;
import com.sevenheaven.segmentcontrol.SegmentControl;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.dialog.MakeSizeDialog;
import esqeee.xieqing.com.eeeeee.dialog.SelectColorDialog;
import esqeee.xieqing.com.eeeeee.helper.RectFloatHelper;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

public class ColorHolder extends BaseHolder{
    @BindView(R.id.image)
    View imageView;
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
    public ColorHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_click_color,adapter);
    }
    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        int type = jsonBean.getInt("actionType");
        this.jsonBean = jsonBean;
        JSONBean param = jsonBean.getJson("param");
        int color = Color.parseColor(param.getString("color"));
        imageView.setBackgroundColor(color);
        imageView.setOnClickListener(view1 -> {
            new BottomListDialog.Builder(getContext())
                    .addMenuListItem(new String[]{"重新选取颜色"},position -> {
                        choose();
                        if (SPUtils.getInstance().getBoolean("showColorChoose",true)){
                            new AlertDialog.Builder(getContext()).setTitle("提示")
                                    .setMessage("再次点击准心，即可重新选取")
                                    .setPositiveButton("知道了",(d,i)->{
                                    })
                                    .setNeutralButton("不再提示", (d,i)->{
                                        SPUtils.getInstance().put("showColorChoose",false);
                                    }).create().show();
                        }
                    }).show();
        });

        seekBar.setProgress(255 - param.getInt("accetrue",5));


        currentIndex = param.getBoolean("assign",false)?2:((type == 58 || type == 59)?0:1);
        initAssignView();
        assign.setVisibility(param.getBoolean("assign",false)?View.VISIBLE:View.GONE);
        action.setSelectedIndex(currentIndex);
        rectRect = getRect(param);
        ColorHolder.this.rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));

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
                                ColorHolder.this.rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));
                            }).create().show();
                        }else{
                            rectRect = null;
                            clearRect(param);
                            ColorHolder.this.rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));
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

    private void choose(){
        ((AddActivity)getContext()).setHookListener(v->{
            if (!RecordAutoCaptruer.getIntance().isRequestPermission()) {
                RecordAutoCaptruer.getIntance().request((BaseActivity) getContext());
                return;
            }
            new SelectColorDialog(((BaseActivity)getContext()),RecordAutoCaptruer.getIntance().captrueScreen(), new SelectColorDialog.SelectColor() {
                @Override
                public void select(int color,String colorHex) {
                    imageView.setBackgroundColor(color);
                    getParam().put("color",colorHex);
                }
            }).show();
        });
    }

    private void initAssignView() {
        if (assign.getChildCount() == 0){
            assign.addView(getVarView("横坐标",1,"xV"));
            assign.addView(getVarView("纵坐标",1,"yV"));
        }
    }
    @Override
    public Rect getRect(JSONBean param) {
        int left = param.getInt("x",-1);
        int top = param.getInt("y",-1);
        int right = param.getInt("width",-1) + left;
        int bottom = param.getInt("height",-1) + top;
        if (left == -1 || top == -1 || right == -1 || bottom == -1){
            return null;
        }
        return new Rect(left,top,right,bottom);
    }

    @Override
    public void clearRect(JSONBean param) {
        param.remove("x");
        param.remove("y");
        param.remove("width");
        param.remove("height");
    }

    @Override
    public void setRect(Rect rect, JSONBean param) {
        param.put("x",rect.left);
        param.put("y",rect.top);
        param.put("width",rect.width());
        param.put("height",rect.height());
    }

    private void selectRect(JSONBean param) {
        ToastUtils.showLong("再次点击准心，即可重新选取");
        ((AddActivity)getContext()).setHookListener(v->{
            new MakeSizeDialog(getContext())
                    .setTip("请选择区域")
                    .setOnMakeSizeRectListener((rect)->{
                        rectRect = rect;
                        setRect(rect,param);
                        ColorHolder.this.rect.setText(rectRect == null ? "选取区域" :rectRect.toShortString());
                    }).show();
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

    private void changedType(JSONBean param) {
        if (rectRect != null){
            if (currentIndex == 0){
                jsonBean.put("actionType",59);
            }else{
                jsonBean.put("actionType",68);
            }
        }else{
            clearRect(param);
            if (currentIndex == 0){
                jsonBean.put("actionType",58);
            }else{
                jsonBean.put("actionType",67);
            }
        }
    }



    @Override
    public void initView() {

    }

    @Override
    public int getIcon() {
        return R.drawable.holder_2;
    }

    @Override
    public String getName() {
        return "识别颜色";
    }

}
