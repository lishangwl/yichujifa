package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class RandomSleepHolder extends BaseHolder{
    @BindView(R.id.max)
    ValotionEdittext max;
    @BindView(R.id.min)
    ValotionEdittext min;


    public RandomSleepHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_sleep,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(false);
        JSONBean param = jsonBean.getJson("param");

        checkInputMathExpression(max);
        checkInputMathExpression(min);
        max.bindChangeString(param,"max");
        min.bindChangeString(param,"min");

        max.setText(replace(param.getString("max")));
        min.setText(replace(param.getString("min")));

    }



    @Override
    public void initView() {

    }

    @Override
    public int getIcon() {
        return R.drawable.ic_yanchi;
    }

    @Override
    public String getName() {
        return "随机延时";
    }

}
