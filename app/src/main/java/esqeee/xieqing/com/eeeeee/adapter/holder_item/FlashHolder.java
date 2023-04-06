package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import com.xieqing.codeutils.util.PhoneUtils;

import butterknife.BindView;
import ch.ielse.view.SwitchView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class FlashHolder extends BaseHolder{
    @BindView(R.id.item_switch)
    SwitchView aSwitch;

    public FlashHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_flash,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        aSwitch.setOpened(jsonBean.getInt("actionType") == 0);
        aSwitch.setOnClickListener(v->{
            PhoneUtils.vibrateShort();
            jsonBean.put("actionType",aSwitch.isOpened()?0:1);
        });
    }



    @Override
    public void initView() {
    }

    @Override
    public int getIcon() {
        return R.drawable.holder_0_1;
    }

    @Override
    public String getName() {
        return "手电筒";
    }
}
