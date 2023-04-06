package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.view.View;

import com.sevenheaven.segmentcontrol.SegmentControl;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class LongClickScreenHolder extends BaseHolder{
    @BindView(R.id.x)
    ValotionEdittext x;
    @BindView(R.id.y)
    ValotionEdittext y;
    @BindView(R.id.time)
    ValotionEdittext time;
    @BindView(R.id.t)
    SegmentControl t;

    public LongClickScreenHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_longclick,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        JSONBean param = jsonBean.getJson("param");
        checkInputMathExpression(x);
        checkInputMathExpression(y);
        x.bindChangeString(param,"x");
        y.bindChangeString(param,"y");

        x.setText(replace(param.getString("x")));
        y.setText(replace(param.getString("y")));

        t.setSelectedIndex(param.getInt("touchTime",1));

        time.setVisibility(param.getInt("touchTime",1) == 3? View.VISIBLE:View.GONE);
        time.bindChangeString(param,"onTouchTime");
        time.setText(replace(param.getString("onTouchTime")));

        t.setOnSegmentControlClickListener((index) ->{
            param.put("touchTime",index);
            time.setVisibility(index == 3? View.VISIBLE:View.GONE);
        });
        ThemeManager.attachTheme(t);
    }



    @Override
    public void initView() {
        x.bindFoucsView(findViewById(R.id.item_x));
        y.bindFoucsView(findViewById(R.id.item_y));
    }

    @Override
    public int getIcon() {
        return R.drawable.holder_2;
    }

    @Override
    public String getName() {
        return "长按屏幕";
    }

}
