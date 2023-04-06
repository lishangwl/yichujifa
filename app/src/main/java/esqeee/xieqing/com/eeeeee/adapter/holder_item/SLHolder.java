package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class SLHolder extends BaseHolder{

    public SLHolder(Context context, MyAdapter adapter) {
        super(context,-1,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);


        JSONBean param = jsonBean.getJson("param");
        setContentXML(R.xml.swipeline);
    }



    @Override
    public void initView() {
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_huadong_zhongdiana;
    }

    @Override
    public String getName() {
        return "直线滑动";
    }

}