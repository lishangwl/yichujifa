package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class VarCondition extends RelativeLayout {
    @BindView(R.id.value) ValotionEdittext value;
    @BindView(R.id.assign) Spinner assign;
    @BindView(R.id.variable_name) TextView variable_name;


    private Rect rectRect;
    BaseHolder holder;
    public VarCondition(@NonNull Context context, JSONBean condition, BaseHolder holder) {
        super(context);
        this.holder = holder;
        addView(View.inflate(context,R.layout.holder_condition_var,null));
        ButterKnife.bind(this,this);

        JSONBean param = condition.getJson("param");

        value.bindChangeString(param,"value");
        value.setText(holder.replace(param.getString("value")));
        String var = param.getString("var");
        if (var.equals("")){
            var = "点击选择";
        }
        variable_name.setText(var);

        findViewById(R.id.variable).setOnClickListener(v->{
            List<JSONBean> strings = holder.getVariables();
            String[] strings1 = new String[strings.size()];
            for (int i = 0 ; i<strings.size() ;i++){
                strings1[i] = strings.get(i).getString("name");
            }
            new AlertDialog.Builder(getContext()).setTitle("选择变量").setItems(strings1,(DialogInterface dialogInterface, int i) ->{
                param.put("var",strings1[i]);
                variable_name.setText(param.getString("var"));
            }).create().show();
        });

        assign.setSelection(param.getInt("assgin",0));
        assign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                param.put("assgin",i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
