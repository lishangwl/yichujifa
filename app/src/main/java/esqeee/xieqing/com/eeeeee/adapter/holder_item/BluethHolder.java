package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import com.xieqing.codeutils.util.PhoneUtils;

import butterknife.BindView;
import ch.ielse.view.SwitchView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class BluethHolder extends BaseHolder{
    @BindView(R.id.item_switch)
    SwitchView aSwitch;

    public BluethHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_blueth,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        aSwitch.setOpened(jsonBean.getInt("actionType") == 20);
        aSwitch.setOnClickListener(v->{
            PhoneUtils.vibrateShort();
            jsonBean.put("actionType",aSwitch.isOpened()?20:21);
        });
    }



    @Override
    public void initView() {
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_open_lanya;
    }

    @Override
    public String getName() {
        return "蓝牙";
    }
}
