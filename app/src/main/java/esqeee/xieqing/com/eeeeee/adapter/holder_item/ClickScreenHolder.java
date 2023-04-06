package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class ClickScreenHolder extends BaseHolder{
    @BindView(R.id.x)
    ValotionEdittext x;
    @BindView(R.id.y)
    ValotionEdittext y;

    @BindView(R.id.c)
    ValotionEdittext c;

    public ClickScreenHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_click,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        JSONBean param = jsonBean.getJson("param");

        checkInputMathExpression(x);
        checkInputMathExpression(y);
        checkInputMathExpression(c);
        x.bindChangeString(param,"x");
        y.bindChangeString(param,"y");
        c.bindChangeString(param,"count");

        x.setText(replace(param.getString("x")));
        y.setText(replace(param.getString("y")));
        c.setText(replace(param.getString("count")));
    }



    @Override
    public void initView() {
        x.bindFoucsView(findViewById(R.id.item_x));
        y.bindFoucsView(findViewById(R.id.item_y));
        c.bindFoucsView(findViewById(R.id.item_c));
    }

    @Override
    public int getIcon() {
        return R.drawable.holder_2;
    }

    @Override
    public String getName() {
        return "点击屏幕";
    }

}
