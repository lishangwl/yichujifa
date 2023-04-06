package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import androidx.appcompat.widget.PopupMenu;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sevenheaven.segmentcontrol.SegmentControl;
import com.xieqing.codeutils.util.FileUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.ConditionAddDialog;

public class ConditionHolder {
    JSONArrayBean conditions;
    View content;
    BaseHolder holder;

    @BindView(R.id.add_condition)
    View add;

    @BindView(R.id.condition_conditionView)
    View conditionView;

    @BindView(R.id.conditions)
    ViewGroup conditionsView;

    @BindView(R.id.condition_condition)
    SegmentControl condition_condition;


    private static JSONBean copy = null;


    public ConditionHolder(View content, BaseHolder holder){
        this.content = content;
        this.holder = holder;

        ButterKnife.bind(this,content);

        add.setOnClickListener(view -> {
            ConditionAddDialog.getConditionAddDialog(content.getContext())
                    .show((JSONBean json) ->{
                            if (json == null){
                                return;
                            }
                            conditions.put(json);
                            conditionsView.addView(new ConditionItem(conditionsView.getContext(),json,conditions.length() - 1));
                            conditionView.setVisibility(conditions.length()>1?View.VISIBLE:View.GONE);
                    });
        });

        condition_condition.setOnSegmentControlClickListener(i->{
            holder.getJsonBean().put("AllOrOne",i);
        });
        ThemeManager.attachTheme(condition_condition);
    }

    public void onBind(JSONArrayBean conditions){
        this.conditions = conditions;
        conditionView.setVisibility(conditions.length()>1?View.VISIBLE:View.GONE);

        bindView();
    }

    private void bindView() {
        condition_condition.setSelectedIndex(holder.getJsonBean().getInt("AllOrOne",0));
        conditionsView.removeAllViews();
        //System.gc();
        for (int i =0;i<conditions.length();i++){
            JSONBean c =conditions.getJson(i);
            if (c!=null)
                conditionsView.addView(new ConditionItem(conditionsView.getContext(),c,i));
        }
    }

    private void delete(int pos){
        Object remove = conditions.remove(pos);
        if (! (remove instanceof JSONObject)){
            return;
        }
        JSONBean jsonBean = new JSONBean((JSONObject)remove);
        if (jsonBean.getInt("actionType") == 47 || jsonBean.getInt("actionType") == 48){
            FileUtils.delete(jsonBean.getJson("param").getString("fileName"));
        }
        onBind(conditions);
    }

    public void onDelete() {
        for (int i =0;i<conditions.length();i++){
            JSONBean c =conditions.getJson(i);
            if (c!=null){
                if (c.getInt("actionType") == 47 || c.getInt("actionType") == 48){
                    //FileUtils.delete(c.getJson("param").getString("fileName"));
                }
            }
        }
    }


    class ConditionItem extends FrameLayout implements PopupMenu.OnMenuItemClickListener{
        @BindView(R.id.condition_more)
        View condition_more;

        @BindView(R.id.condition_item_contianor)
        ViewGroup contianor;

        int position = -1;
        JSONBean condition = null;
        private ConditionItem(Context context,JSONBean condition,int position){
            super(context);
            View view = View.inflate(context,R.layout.holder_condition_item,null);
            addView(view);
            ButterKnife.bind(this,this);


            PopupMenu popupMenu = new PopupMenu(view.getContext(), condition_more);
            popupMenu.inflate(R.menu.condition_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.getMenu().findItem(R.id.action_paste).setVisible(copy != null);
            condition_more.setOnClickListener(V->{
                this.position = position;
                this.condition = condition;
                popupMenu.show();
            });


            switch (condition.getInt("actionType")){
                case 54:
                case 63:
                    contianor.addView(new ColorCondition(context,condition,holder));
                    break;
                case 44:
                case 45:
                    contianor.addView(new TextCondition(context,condition,holder));
                    break;

                case 47:
                case 48:
                    contianor.addView(new ImageCondition(context,condition,holder));
                    break;

                case 72:
                    contianor.addView(new VarCondition(context,condition,holder));
                    break;
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_delete:
                    delete(position);
                    break;
                case R.id.action_copy:
                    copy = new JSONBean(condition);
                    onBind(conditions);
                    break;
                case R.id.action_paste:
                    conditions.add(copy);
                    copy = null;
                    onBind(conditions);
                    break;
            }
            return false;
        }
    }
}
