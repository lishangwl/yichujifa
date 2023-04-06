package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;

public class SwipHolder extends BaseHolder{

    public SwipHolder(Context context, MyAdapter adapter) {
        super(context,-1,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        int actionType = jsonBean.getInt("actionType");
        switch (DoActionBean.getBeanFromType(actionType)){
            case SWIP_LEFT:
                setHead("左滑",R.drawable.holder_0_1);
                break;
            case SWIP_RIGHT:
                setHead("右滑",R.drawable.holder_0_1);
                break;
            case SWIP_TOP:
                setHead("上滑",R.drawable.holder_0_1);
                break;
            case SWIP_BOTTOM:
                setHead("下滑",R.drawable.holder_0_1);
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
