package esqeee.xieqing.com.eeeeee.fragment;

import android.graphics.Color;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyViewHolder;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.Variable;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.helper.ViewModel;
import esqeee.xieqing.com.eeeeee.listener.SwipeDontScollRecylerListener;
import esqeee.xieqing.com.eeeeee.listener.VariableClickMore;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class VariableTableFragment extends BaseFragment {
    public static List<Variable> finalList = new ArrayList<>();
    private final int FINAL_BG_COLOR = Color.parseColor("#50f3f8b9");
    static {
        finalList.add(new Variable("屏幕宽",VariableType.INT,ScreenUtils.getScreenWidth()));
        finalList.add(new Variable("屏幕高",VariableType.INT,ScreenUtils.getScreenHeight()));
        finalList.add(new Variable("存储卡路径",VariableType.STRING,Environment.getExternalStorageDirectory().getAbsolutePath()));
    }
    private List<JSONBean> variables;
    public static VariableTableFragment create(BaseActivity baseActivity, List<JSONBean> variables){
        VariableTableFragment tableFragment = new VariableTableFragment();
        tableFragment.variables = variables;
        return tableFragment;
    }

    public synchronized static Variable queryFinal(String name){
        for (Variable variable :finalList){
            if (variable.getName().equals(name)){
                return variable;
            }
        }
        return null;
    }


    @BindView(R.id.variable_add)
    View add;

    @BindView(R.id.variable_close)
    View close;

    @BindView(R.id.recylerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView.Adapter adapter;

    @Override
    public View getContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.variable,null);
    }

    private JSONBean queryVariable(String name) {
        for (JSONBean v : variables){
            if (v.getString("name").equals(name)){
                return v;
            }
        }
        return null;
    }

    @Override
    protected void onFragmentInit() {
        close.setOnClickListener(view -> {
            getBaseActivity().getSupportFragmentManager().popBackStack();
        });
        add.setOnClickListener(view -> {
            /*new InputTextDialog(getBaseActivity()).setTitle("添加变量")
                    .addInputLine(new InputTextLine("变量名",""))
                    .addInputLine(new InputTextLine("初始值","").setCanInputNull(true))
                    .setInputTextListener(i->{
                        String name = i[0].getResult().toString().trim();
                        String v = i[1].getResult().toString();
                        if (queryFinal(name)!=null){
                            ToastUtils.showShort("变量名不能与常量名相同！");
                            return;
                        }
                        if (queryVariable(name)!=null){
                            ToastUtils.showShort("该变量已存在！");
                            return;
                        }

                        variables.add(new JSONBean()
                                .put("name",name)
                                .put("value",v)
                                .put("type",v));
                        refresh();
                    }).show();*/
            ViewModel viewModel = ViewModel.from(getBaseActivity(),R.layout.add_variable,null);
            new AlertDialog.Builder(getBaseActivity())
                    .setTitle("添加变量")
                    .setView(viewModel.getView())
                    .setPositiveButton("确定",(dialog, which) -> {
                        String name = viewModel.getText(R.id.variable_name);
                        String value = viewModel.getText(R.id.variable_value);
                        int type = viewModel.getSpinnerSelected(R.id.variable_type);
                        if (queryFinal(name)!=null){
                            ToastUtils.showShort("变量名不能与常量名相同！");
                            return;
                        }
                        if (queryVariable(name)!=null){
                            ToastUtils.showShort("该变量已存在！");
                            return;
                        }
                        variables.add(new JSONBean()
                                .put("name",name)
                                .put("value",value)
                                .put("type",type));
                        refresh();
                    })
                    .setNegativeButton("取消",null).show();

        });

        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(getBaseActivity()));
        recyclerView.setOnTouchListener(new SwipeDontScollRecylerListener(swipeRefreshLayout));
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyViewHolder(View.inflate(getBaseActivity(),R.layout.variable_item,null),true);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                View view=holder.itemView;
                TextView variable = view.findViewById(R.id.variable_name);
                Spinner spinner = view.findViewById(R.id.spinner);
                if (position < finalList.size()){
                    //bind Final variables
                    variable.setBackgroundColor(FINAL_BG_COLOR);
                    variable.setText(finalList.get(holder.getAdapterPosition()).getName());
                    variable.setOnClickListener(null);
                    spinner.setSelection(finalList.get(holder.getAdapterPosition()).getType().ordinal());
                    spinner.setEnabled(false);
                    ((TextView)view.findViewById(R.id.defalutView)).setText(finalList.get(holder.getAdapterPosition()).getString("value"));
                    view.findViewById(R.id.defaultValue).setOnClickListener((View v)->{
                            new AlertDialog.Builder(getActivity()).setTitle(finalList.get(holder.getAdapterPosition()).getName()+"的值为:")
                                    .setMessage(finalList.get(holder.getAdapterPosition()).getString("value"))
                                    .setPositiveButton("确定",null).create().show();
                    });
                }else{
                    JSONBean jsonBean = variables.get(holder.getAdapterPosition() - finalList.size());
                    ((TextView)view.findViewById(R.id.defalutView)).setText(jsonBean.getString("value"));
                    variable.setText(jsonBean.getString("name"));
                    variable.setBackgroundColor(Color.WHITE);
                    spinner.setSelection(jsonBean.getInt("type",0));

                    variable.setOnClickListener(new VariableClickMore(variables,getBaseActivity(),this,holder));
                    spinner.setEnabled(true);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                variables.get(holder.getAdapterPosition() - finalList.size()).put("type",i);
                            }catch (ArrayIndexOutOfBoundsException e){

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner.setVisibility(View.VISIBLE);

                    view.findViewById(R.id.defaultValue).setOnClickListener((View v)->{
                        new InputTextDialog(getBaseActivity(),false)
                                .addInputLine(new InputTextLine("",jsonBean.getString("value")))
                                .setTitle("初始值")
                                .setInputTextListener(result -> {
                                    jsonBean.put("value",result[0].getResult().toString());
                                    notifyItemChanged(holder.getAdapterPosition());
                                }).show();
                    });
                }
            }
            @Override
            public int getItemCount() {
                return finalList.size() + variables.size();
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void refresh() {
        recyclerView.getAdapter().notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
