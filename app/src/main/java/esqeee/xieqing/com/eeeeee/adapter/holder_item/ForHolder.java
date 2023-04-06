package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.yicu.yichujifa.ui.theme.ThemeManager;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.fragment.AutoFragment;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class ForHolder extends BaseHolder{
    @BindView(R.id.count)
    ValotionEdittext count;
    @BindView(R.id.item_edit)
    View edit;
    public ForHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_for,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(false);
        JSONBean param = jsonBean.getJson("param");

        int countNum = jsonBean.getInt("condition",-1);
        Log.d("xxxxxxxxxxxxxxxxxx",param.toString());
        if (countNum != -1){
            jsonBean.remove("condition");
            param.put("condition",countNum+"");
        }

        checkInputMathExpression(count);
        count.bindChangeString(param,"condition");
        count.setText(replace(param.getString("condition")));

        edit.setOnClickListener(view -> {
            JSONArrayBean trueDo = jsonBean.getJson("trueDo").getArray("actions");
            if (trueDo == null){
                trueDo = new JSONArrayBean();
                jsonBean.getJson("trueDo").put("actions",trueDo);
            }

            ((BaseActivity)getContext()).addFragment(R.id.listView, AutoFragment.create(trueDo,getVariables(),(BaseActivity)getContext()));
        });
        ThemeManager.attachTheme(((ViewGroup)edit).getChildAt(1));
    }



    @Override
    public void initView() {
        count.bindFoucsView(findViewById(R.id.item_count));
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_for;
    }

    @Override
    public String getName() {
        return "计次循环";
    }

}
