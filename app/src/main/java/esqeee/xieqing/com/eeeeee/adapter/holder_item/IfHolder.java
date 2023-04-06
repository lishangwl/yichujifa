package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.yicu.yichujifa.ui.theme.ThemeManager;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.fragment.AutoFragment;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.widget.ExpandableLayout;

public class IfHolder extends BaseHolder{

    @BindView(R.id.condition)
    ExpandableLayout expandableLayout;

    @BindView(R.id.condition_image)
    ImageView expland;
    @BindView(R.id.item_edit)
    View edit;
    @BindView(R.id.item_edit2)
    View edit2;
    private ConditionHolder conditionHolder;

    public IfHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_if,adapter);
    }

    @Override
    public void onDelete() {
        super.onDelete();
        conditionHolder.onDelete();
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(false);
        JSONBean param = jsonBean.getJson("param");

        JSONBean condition = jsonBean.getJson("condition");
        JSONArrayBean conditions = jsonBean.getArray("conditions");
        if (conditions == null){
            conditions = new JSONArrayBean();
            jsonBean.put("conditions",conditions);
        }

        if (condition != null){
            conditions.put(condition);
            jsonBean.remove("condition");
        }


        edit.setOnClickListener(view -> {
            JSONArrayBean trueDo = jsonBean.getJson("trueDo").getArray("actions");
            if (trueDo == null){
                trueDo = new JSONArrayBean();
                jsonBean.getJson("trueDo").put("actions",trueDo);
            }
            ((BaseActivity)getContext()).addFragment(R.id.listView, AutoFragment.create(trueDo,getVariables(),(BaseActivity)getContext()));
        });


        edit2.setOnClickListener(view -> {
            JSONArrayBean falseDo = jsonBean.getJson("falseDo").getArray("actions");
            if (falseDo == null){
                falseDo = new JSONArrayBean();
                jsonBean.getJson("falseDo").put("actions",falseDo);
            }
            ((BaseActivity)getContext()).addFragment(R.id.listView, AutoFragment.create(falseDo,getVariables(),(BaseActivity)getContext()));
        });

        ThemeManager.attachTheme(((ViewGroup)edit).getChildAt(1),((ViewGroup)edit2).getChildAt(1));
        conditionHolder.onBind(conditions);
    }



    @Override
    public void initView() {
        conditionHolder = new ConditionHolder(findViewById(R.id.condition_content),this);
        expandableLayout.setListener(isOpened -> {
            Animation animation = new RotateAnimation(isOpened?0:180,isOpened?180:0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(expandableLayout.getDuration());
            animation.setFillAfter(true);
            expland.startAnimation(animation);
        });
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_if;
    }

    @Override
    public String getName() {
        return "条件判断";
    }
}
