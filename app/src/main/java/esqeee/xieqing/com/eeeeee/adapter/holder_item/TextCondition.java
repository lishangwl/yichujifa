package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhonjson.dialoglib.BottomListDialog;
import com.sevenheaven.segmentcontrol.SegmentControl;
import com.xieqing.codeutils.util.ToastUtils;

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
import esqeee.xieqing.com.eeeeee.helper.RectFloatHelper;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class TextCondition extends RelativeLayout {
    @BindView(R.id.t) ValotionEdittext t;
    @BindView(R.id.ocr) SegmentControl segmentControl;
    @BindView(R.id.rect) TextView rect;
    @BindView(R.id.show_rect) View view;


    private Rect rectRect;
    BaseHolder holder;
    public TextCondition(@NonNull Context context, JSONBean condition,BaseHolder holder) {
        super(context);
        this.holder = holder;
        addView(View.inflate(context,R.layout.holder_condition_text,null));
        ButterKnife.bind(this,this);

        JSONBean param = condition.getJson("param");

        t.bindChangeString(param,"text");
        t.setText(holder.replace(param.getString("text")));
        segmentControl.setSelectedIndex(param.getInt("scanType",0));
        rectRect = holder.getRect(param);
        rect.setText(param.has("rectVar")?param.getString("rectVar"):(rectRect == null ? "选取区域" :rectRect.toShortString()));
        segmentControl.setOnSegmentControlClickListener((i)->{
            param.put("scanType",i);
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
                            TextCondition.this.rect.setText(rectRect == null ? "选取区域" :rectRect.toShortString());
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
        ((AddActivity)getContext()).setHookListener(v-> {
            new MakeSizeDialog(getContext())
                    .setTip("请选择区域")
                    .setOnMakeSizeRectListener((rect)->{
                        rectRect = rect;
                        holder.setRect(rect,param);
                        TextCondition.this.rect.setText(rectRect == null ? "选取区域" :rectRect.toShortString());
                    }).show();
        });

    }

}
