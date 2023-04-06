package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.widget.TextView;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class FastHolder extends BaseHolder{
    @BindView(R.id.input)
    ValotionEdittext input;


    @BindView(R.id.t)
    TextView t;


    public FastHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_line,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        JSONBean param = jsonBean.getJson("param");

        input.bindChangeString(param,"text");
        input.setText(replace(param.getString("text")));


        switch (jsonBean.getInt("actionType")){
            case 38:
                t.setText("联系人");
                setHead("拨打电话",R.drawable.ic_phone);
                break;
            case 39:
                t.setText("链接");
                setHead("打开网址",R.drawable.ic_dakaiwangzhi);
                break;
            case 40:
                t.setText("QQ号码");
                setHead("与指定人聊天",R.drawable.ic_holder_qq);
                break;
        }
    }



    @Override
    public void initView() {
        input.bindFoucsView(findViewById(R.id.item_x));
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
