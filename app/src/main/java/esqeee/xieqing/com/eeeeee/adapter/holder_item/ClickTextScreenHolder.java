package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;

import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhonjson.dialoglib.BottomListDialog;
import com.sevenheaven.segmentcontrol.SegmentControl;
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
import esqeee.xieqing.com.eeeeee.helper.RectFloatHelper;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class ClickTextScreenHolder extends BaseHolder{
    @BindView(R.id.t) ValotionEdittext t;
    @BindView(R.id.ocr) SegmentControl segmentControl;
    @BindView(R.id.action) SegmentControl action;
    @BindView(R.id.rect) TextView rect;
    @BindView(R.id.show_rect) View view;

    private JSONBean jsonBean;
    private int currentIndex = 0;
    private Rect rectRect;

    @BindView(R.id.assign)
    ViewGroup assign;
    public ClickTextScreenHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_click_text,adapter);
    }
    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        int type = jsonBean.getInt("actionType");
        this.jsonBean = jsonBean;
        JSONBean param = jsonBean.getJson("param");

        t.bindChangeString(param,"text");
        t.setText(replace(param.getString("text")));

        segmentControl.setSelectedIndex(param.getInt("scanType",0));
        currentIndex = param.getBoolean("assign",false)?2:(type == 3 || type == 49)?0:1;
        initAssignView();
        assign.setVisibility(param.getBoolean("assign",false)?View.VISIBLE:View.GONE);
        action.setSelectedIndex(currentIndex);
        rectRect = getRect(param);ClickTextScreenHolder.this.rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));

        action.setOnSegmentControlClickListener((i)->{
            currentIndex = i;
            changedType(param);
            param.put("assign",i == 2);
            assign.setVisibility(param.getBoolean("assign",false)?View.VISIBLE:View.GONE);
            initAssignView();
        });
        segmentControl.setOnSegmentControlClickListener((i)->{
            param.put("scanType",i);
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
                                ClickTextScreenHolder.this.rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));
                            }).create().show();
                        }else{
                            rectRect = null;
                            clearRect(param);
                            ClickTextScreenHolder.this.rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));
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
        ThemeManager.attachTheme(segmentControl,action);
    }

    private void selectRect(JSONBean param) {

        ToastUtils.showLong("再次点击准心，即可重新选取");
        ((AddActivity)getContext()).setHookListener(v->{
            new MakeSizeDialog(getContext())
                    .setTip("请选择区域")
                    .setOnMakeSizeRectListener((rect)->{
                        rectRect = rect;
                        setRect(rect,param);
                        ClickTextScreenHolder.this.rect.setText(rectRect == null ? "选取区域" :rectRect.toShortString());
                    }).show();
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
                jsonBean.put("actionType",49);
            }else{
                jsonBean.put("actionType",62);
            }
        }else{
            clearRect(param);
            if (currentIndex == 0){
                jsonBean.put("actionType",3);
            }else{
                jsonBean.put("actionType",41);
            }
        }
    }



    @Override
    public void initView() {
        t.bindFoucsView(findViewById(R.id.item_t));
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_zhantie;
    }

    @Override
    public String getName() {
        return "识别文字";
    }

}
