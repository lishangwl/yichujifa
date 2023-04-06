package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class ToastHolder extends BaseHolder{
    @BindView(R.id.input)
    ValotionEdittext input;
    public ToastHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_texteare,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        JSONBean param = jsonBean.getJson("param");

        input.bindChangeString(param,"text");

        input.setText(replace(param.getString("text")));

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
        return "弹出提示";
    }

}
