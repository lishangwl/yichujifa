package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class AssginHolder extends BaseHolder{
    @BindView(R.id.assign)
    ValotionEdittext assign;
    @BindView(R.id.novariable)
    View novariable;
    @BindView(R.id.variable)
    View variable;
    @BindView(R.id.http_choose_varibale)
    View choose;

    @BindView(R.id.variable_name)
    TextView variable_name;

    public AssginHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_assgin,adapter);
    }

    private JSONArrayBean headerBean;
    private JSONArrayBean dataBean;
    TextWatcher watcher;
    JSONBean param;
    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(false);


        setTooltips("将所要赋值的数据传递给已创建的变量");

        param = jsonBean.getJson("param");

        assign.setText(replace(param.getString("value")));

        choose.setOnClickListener(v->{
            List<JSONBean> strings = getVariables();
            String[] strings1 = new String[strings.size()];
            for (int i = 0 ; i<strings.size() ;i++){
                strings1[i] = strings.get(i).getString("name");
            }
            new AlertDialog.Builder(getContext()).setTitle("选择变量").setItems(strings1,(DialogInterface dialogInterface, int i) ->{
                param.put("var",strings1[i]);
                bindVar(param.getString("var"));
            }).create().show();
        });
        bindVar(param.getString("var"));
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changed();
            }
        };
        assign.addTextChangedListener(watcher);
    }

    private void changed() {
        assign.setTextColor(android.graphics.Color.BLACK);
        JSONBean jsonBean = queryVariable(variable_name.getText().toString());


        int type = jsonBean == null?0:jsonBean.getInt("type");
        String value = assign.getText().toString();

        param.put("value",value);
        if (type == 0){
            //string
        }else if (type == 1){
            //int
            checkInputMathExpression(assign);
        }else if (type == 3){
            //bool
            String[] variables = findVariables(value);
            JSONBean var = null;
            for (String v:variables){
                if (var != null){
                    assign.setError("布尔型的变量只能赋值 真 或者 假 或者 布尔型的单一变量");
                    return;
                }
                var = queryVariable(v);
            }
            if (var != null && var.getInt("type")!=VariableType.BOOL.ordinal()){
                assign.setError("布尔型的变量只能赋值 真 或者 假 或者 布尔型的单一变量");
                return;
            }

            value = getString(value);
            if (!value.trim().equals("真") && !value.trim().equals("假")&& !value.trim().equals("Null")){
                assign.setError("布尔型的变量只能赋值 真 或者 假 或者 布尔型的单一变量");
                return;
            }
            assign.setTextColor(android.graphics.Color.BLUE);
        }else{
            assign.setError("不支持赋值的变量 ["+variable_name.getText().toString()+"]  type:"+type+"");
        }
    }

    private void bindVar(String varName) {
        JSONBean var = queryVariable(varName);
        if (var == null){
            noVariable();
            return;
        }
        novariable.setVisibility(View.GONE);
        variable.setVisibility(View.VISIBLE);
        variable_name.setText(varName);
    }

    private void noVariable() {
        variable.setVisibility(View.GONE);
        novariable.setVisibility(View.VISIBLE);
    }



    @Override
    public void initView() {

    }

    @Override
    public int getIcon() {
        return R.drawable.ic_dakaiwangzhi;
    }

    @Override
    public String getName() {
        return "赋值变量";
    }


}
