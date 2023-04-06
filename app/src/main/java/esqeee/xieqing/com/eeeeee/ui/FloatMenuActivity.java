package esqeee.xieqing.com.eeeeee.ui;

import android.os.Build;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.PhoneUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.adapter.MyViewHolder;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.SelectActionDialog;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.fragment.ActionsFragment;
import esqeee.xieqing.com.eeeeee.fragment.BaseFragment;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.tilefloat.TileFloat;
import esqeee.xieqing.com.eeeeee.listener.OnActionAddListener;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class FloatMenuActivity extends BaseActivity {

    @BindView(R.id.item_move)
    View item_move;

    @BindView(R.id.item_tile)
    View item_tile;

    @BindView(R.id.item_signle)
    View item_signle;

    @BindView(R.id.switch1)
    Switch switch1;

    @BindView(R.id.switch2)
    Switch switch2;

    @BindView(R.id.switch3)
    Switch switch3;

    @BindView(R.id.recylerView)
    RecyclerView recyclerView;

    private List<JSONBean> items = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getContentLayout() {
        return R.layout.activity_float_menu;
    }


    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //拖拽时支持的方向向上向下
            int dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN;
            //滑动的时候支持的方向为左右
            int swipeFlags=ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags,swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            move(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //不处理左右滑动
            if (direction == ItemTouchHelper.RIGHT){
                items.remove(viewHolder.getAdapterPosition());
                recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                SettingsPreference.saveFloatMenuItems(items);
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            PhoneUtils.vibrateShort();
        }
    });
    public  void move(int from, int to){
        JSONBean move= items.remove(from);
        items.add(to,move);
        recyclerView.getAdapter().notifyItemMoved(from,to);
        SettingsPreference.saveFloatMenuItems(items);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar.setNavigationOnClickListener((v)->{
            finish();
        });

        items = SettingsPreference.getFloatMenuItems();

        recyclerView.setLayoutManager(new MyLinearLayoutManager(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyViewHolder(View.inflate(FloatMenuActivity.this,R.layout.fast_item,null),true);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                View view = holder.itemView;
                ImageView imageView = view.findViewById(R.id.fast_icon);
                TextView textView = view.findViewById(R.id.fast_title);

                JSONBean bean = items.get(position);
                JSONBean param = bean.getJson("param");
                int type = bean.getInt("actionType");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setImageTintList(null);
                }
                if (type == DoActionBean.EXCE_ACTION.getActionType()){
                    Action action = ActionHelper.from(param.getString("actionId"));
                    if (action != null){
                        imageView.setImageDrawable(action.getDrawable());
                        textView.setText(action.getTitle());
                    }
                }else if (type == 12){
                    AppUtils.AppInfo appInfo = AppUtils.getAppInfo(param.getString("packName"));
                    if(appInfo!=null){
                        imageView.setImageDrawable(appInfo.getIcon());
                        textView.setText("打开\t"+appInfo.getName());
                    }
                }else{
                    DoActionBean doActionBean = DoActionBean.getBeanFromType(type);
                    imageView.setImageDrawable(doActionBean.getDrawable());
                    textView.setText(doActionBean.getActionName());
                }

                view.setOnClickListener(view1 -> {

                });
            }

            @Override
            public int getItemCount() {
                return items.size();
            }
        });

        ThemeManager.attachTheme(toolbar,findViewById(R.id.title1),findViewById(R.id.add)
                ,findViewById(R.id.title2)
                    ,switch1,switch2,switch3);
    }


    @Override
    protected void onResume() {
        super.onResume();
        switch1.setChecked(!SettingsPreference.canMoveFloatMenu());
        switch2.setChecked(SettingsPreference.canTileFloatMenu());
        switch3.setChecked(SettingsPreference.useSignleFloat());
        item_move.setOnClickListener(v->{
            switch1.setChecked(!switch1.isChecked());
            SettingsPreference.setCanMoveFloatMenu(!switch1.isChecked());
            TileFloat.getTileFloat(this).updateSetting();
        });
        item_tile.setOnClickListener(v->{
            switch2.setChecked(!switch2.isChecked());
            SettingsPreference.setCanTileFloatMenu(switch2.isChecked());
            TileFloat.getTileFloat(this).updateSetting();
        });
        item_signle.setOnClickListener(v->{
            switch3.setChecked(!switch3.isChecked());
            SettingsPreference.setUseSignleFloat(switch3.isChecked());
        });
    }

    private SelectActionDialog selectActionDialog;
    private OnActionAddListener addListener = (json)->{

    };
    private BaseFragment actionsFragment;
    public void addFloatMenu(View view) {
        if (selectActionDialog == null){
            //selectActionDialog = new SelectActionDialog(this,addListener,false);
        }

        if (!PermissionUtils.getAppOps(this)){
            PermissionUtils.openAps(this);
            return;
        }

        //selectActionDialog.show();
        actionsFragment = new ActionsFragment()
                .setOnActionSelectedListener(action->{
                    items.add(JSONBean.optInt("actionType",17)
                            .put("param",new JSONBean()
                                    .put("actionName",FileUtils.getFileNameNoExtension(action))
                                    .put("actionId",action.getAbsolutePath())
                            ));
                    recyclerView.getAdapter().notifyItemInserted(items.size());
                    SettingsPreference.saveFloatMenuItems(items);
                    actionsFragment.dismiss();
                });
        actionsFragment.show(getSupportFragmentManager(),"addMenu");
    }
}
