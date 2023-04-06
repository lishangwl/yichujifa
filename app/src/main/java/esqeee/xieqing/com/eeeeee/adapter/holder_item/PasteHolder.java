package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class PasteHolder extends BaseHolder{
    @BindView(R.id.x)
    ValotionEdittext x;
    @BindView(R.id.y)
    ValotionEdittext y;
    public PasteHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_click,adapter);
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

    }



    @Override
    public void initView() {
        x.bindFoucsView(findViewById(R.id.item_x));
        y.bindFoucsView(findViewById(R.id.item_y));
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_zhantie;
    }

    @Override
    public String getName() {
        return "在此处粘贴";
    }

}
