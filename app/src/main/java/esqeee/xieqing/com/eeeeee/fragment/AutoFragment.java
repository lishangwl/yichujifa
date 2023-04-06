package esqeee.xieqing.com.eeeeee.fragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.listener.OnActionAddListener;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class AutoFragment extends BaseFragment{
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<JSONBean> variables;
    private JSONArrayBean actions;

    public MyAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private OnActionAddListener actionAddListener = new OnActionAddListener() {
        @Override
        public void addAction(JSONBean jsonObject) {
            if (adapter.getOnMenuSelectPosition() == -1 || adapter.getOnMenuSelectPosition() >= actions.length()){
                actions.put(jsonObject);
            }else{
                actions.add(adapter.getOnMenuSelectPosition()+1,jsonObject);
            }
            adapter.setOnMenuSelectPosition(-1);
            adapter.notifyDataSetChanged();recyclerView.setVisibility(actions.length() == 0?View.GONE:View.VISIBLE);
            tip.setVisibility(adapter.getCopy()!=null?View.GONE:(actions.length() == 0?View.VISIBLE:View.GONE));
            paste.setVisibility(adapter.getCopy()!=null?View.VISIBLE:View.GONE);
        }
    };

    public OnActionAddListener getActionAddListener() {
        return actionAddListener;
    }

    private LinearLayout linearLayout;
    private TextView tip;
    private Button paste;
    private View menu;
    private View item_copy;
    private View item_delete;
    private View item_cannel;
    private View item_cut;
    @Override
    public View getContentView(LayoutInflater inflater) {
        menu = inflater.inflate(R.layout.adapter_bottom_menu,null);
        menu.setVisibility(View.GONE);
        //menu.setBackgroundColor(Color.RED);
        item_cannel = menu.findViewById(R.id.item_cannel);
        item_delete = menu.findViewById(R.id.item_delete);
        item_copy = menu.findViewById(R.id.item_copy);
        item_cut = menu.findViewById(R.id.item_cut);
        item_cannel.setOnClickListener(this::menuClicked);
        item_delete.setOnClickListener(this::menuClicked);
        item_copy.setOnClickListener(this::menuClicked);
        item_cut.setOnClickListener(this::menuClicked);
        recyclerView = new RecyclerView(inflater.getContext());
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout = new LinearLayout(inflater.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(android.graphics.Color.parseColor("#E4E4E4"));
        linearLayout.addView(recyclerView,new LinearLayout.LayoutParams(-1,-1,1));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setHorizontalGravity(LinearLayout.VERTICAL);
        tip = new TextView(inflater.getContext());
        tip.setText("这里还没有任何动作，点击准心添加动作");
        tip.setGravity(Gravity.CENTER);
        paste = new Button(inflater.getContext());
        paste.setText("粘贴动作");
        linearLayout.addView(tip,new LinearLayout.LayoutParams(-1,-1));
        linearLayout.addView(paste);
        linearLayout.addView(menu,new LinearLayout.LayoutParams(-1,-2,0));

        paste.setOnClickListener(v->{
            paste.setVisibility(View.GONE);
            tip.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.add();
        });
        return linearLayout;
    }

    public static AutoFragment create(JSONArrayBean actions, List<JSONBean> variables, BaseActivity baseActivity){
        AutoFragment autoFragment = new AutoFragment();
        autoFragment.actions = actions;
        autoFragment.variables = variables;
        return autoFragment;
    }

    @Override
    protected void onFragmentInit() {
        if (actions == null){
            return;
        }
        recyclerView.setLayoutManager(new MyLinearLayoutManager(getBaseActivity()));
        adapter = new MyAdapter(getBaseActivity(),actions,variables,recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.getItemTouchHelper().attachToRecyclerView(recyclerView);
        recyclerView.setVisibility(actions.length() == 0?View.GONE:View.VISIBLE);
        tip.setVisibility(adapter.getCopy()!=null?View.GONE:(actions.length() == 0?View.VISIBLE:View.GONE));
        paste.setVisibility(adapter.getCopy()!=null?View.VISIBLE:View.GONE);
    }

    @Override
    public boolean disptachMenuSelected(int itemId) {
        if (itemId == R.id.action_moreChooose){
            adapter.setMoreSelection(!adapter.isMoreSelection());
            adapter.notifyItemRangeChanged(0,adapter.getItemCount());
            menu.setVisibility(adapter.isMoreSelection()?View.VISIBLE:View.GONE);
            return true;
        }
        return super.disptachMenuSelected(itemId);
    }

    public void menuClicked(View view){
        switch (view.getId()){
            case R.id.item_cannel:
                break;
            case R.id.item_copy:
                adapter.setCopy(adapter.getSelected());
                break;
            case R.id.item_delete:
                adapter.deleteItems();
                break;
            case R.id.item_cut:
                adapter.setCopy(adapter.deleteItems());
                break;
        }
        disptachMenuSelected(R.id.action_moreChooose);
    }
}
