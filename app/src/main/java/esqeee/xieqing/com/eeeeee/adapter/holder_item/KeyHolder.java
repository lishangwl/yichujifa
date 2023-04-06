package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.xieqing.codeutils.util.PhoneUtils;

import butterknife.BindView;
import ch.ielse.view.SwitchView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;

public class KeyHolder extends BaseHolder{

    public KeyHolder(Context context, MyAdapter adapter) {
        super(context,-1,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        findViewById(R.id.holder_expland).setVisibility(View.GONE);
        int actionType = jsonBean.getInt("actionType");
        switch (DoActionBean.getBeanFromType(actionType)){
            case KEY_BACK:
                setHead("返回键",R.drawable.ic_fanhui);
                break;
            case KEY_HOME:
                setHead("主页键",R.drawable.ic_zhuye);
                break;
            case KEY_TASK:
                setHead("任务键",R.drawable.ic_zhuye);
                break;
            case KEY_LONG_POWER:
                setHead("长按电源",R.drawable.ic_dianyuanjian);
                break;
            case SYSTEM_WKKEUP:
                setHead("唤醒屏幕",R.drawable.ic_huanxing);
                break;
        }
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
        return "KEY";
    }
}
