package esqeee.xieqing.com.eeeeee.adapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.jhonjson.dialoglib.interfaces.OnClickPositionListener;
import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.ToastUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.AppHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.ArrayHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.AssginHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.AutoHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.BaseHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.BluethHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.ClickScreenHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.ClickTextScreenHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.ColorHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.DescHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.DialogHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.EncryptHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.FastHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.FileHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.FlashHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.ForHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.GesnterHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.HTTPHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.IfHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.ImageHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.InputTextHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.KeyHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.LongClickScreenHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.NodeHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.PasteHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.RandomSleepHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.SLHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.SettingHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.StringHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.SwipHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.SwipLineHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.SystemHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.ToastHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.UIHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.WhileHolder;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.WifiHolder;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;


public class MyAdapter extends RecyclerView.Adapter implements OnClickPositionListener {
    private JSONArrayBean jsonObjects;
    private Activity context;
    private int selectedPosition = -1;
    private List<JSONBean> variables;
    private int onMenuSelectPosition = -1;
    private RecyclerView recyclerView;
    public static List<JSONBean> jsonBean = null;

    public static void setCopy(JSONBean json) {
        jsonBean = new ArrayList<>();
        jsonBean.add(json);
    }

    public static void setCopy(Collection<JSONBean> json) {
        jsonBean = new ArrayList<>();
        jsonBean.addAll(json);
    }

    public static List<JSONBean> getCopy() {
        return jsonBean;
    }

    public void paste(int position){
        if (jsonBean != null){
            for (JSONBean jsonBean1 : jsonBean){
                jsonObjects.add(position+1,jsonBean1);
                notifyItemInserted(position+1);
                position++;
            }
            jsonBean = null;
        }
    }

    public void add(){
        if (jsonBean != null){
            for (JSONBean jsonBean1 : jsonBean){
                jsonObjects.add(jsonBean1);
                notifyItemInserted(jsonObjects.length() - 1);
            }
            jsonBean = null;
        }
    }
    public int getOnMenuSelectPosition() {
        return onMenuSelectPosition;
    }

    public void setOnMenuSelectPosition(int onMenuSelectPosition) {
        this.onMenuSelectPosition = onMenuSelectPosition;
    }


    public MyAdapter(Activity context, JSONArrayBean jsonObjects, List<JSONBean> variables,RecyclerView recyclerView){
        this.context = context;
        this.jsonObjects = jsonObjects;
        this.variables = variables;
        this.recyclerView = recyclerView;
    }


    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void move(int from, int to){
        JSONBean move= jsonObjects.getJson(from);
        jsonObjects.remove(from);

        jsonObjects.add(to,move);
        notifyItemMoved(from,to);
    }

    public List<JSONBean> deleteItems() {
        List<JSONBean> selected = new ArrayList<>();
        for (int i = 0 ;i < jsonObjects.length() ;i++){
            JSONBean jsonBean  = jsonObjects.getJson(i);
            if (jsonBean.getBoolean("selected",false)){
                selected.add(jsonBean);
            }
        }
        for (JSONBean jsonBean :selected){
            jsonObjects.remove(jsonBean.toJson());
        }
        return selected;
    }

    public boolean hasChecked(Integer adapterPosition) {
        return jsonObjects.getJson(adapterPosition).getBoolean("selected",false);
    }

    public List<JSONBean> getSelected() {
        List<JSONBean> selected = new ArrayList<>();
        for (int i = 0 ;i < jsonObjects.length() ;i++){
            JSONBean jsonBean  = jsonObjects.getJson(i);
            if (jsonBean.getBoolean("selected",false)){
                selected.add(new JSONBean(jsonBean));
            }
        }
        return selected;
    }


    private class BottomMenu extends PopupWindow{
        private BottomMenu(){
            super(View.inflate(context,R.layout.adapter_bottom_menu,null),-1,-2,true);
            setBackgroundDrawable(new ColorDrawable(0));
            setOutsideTouchable(false);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 56){
            view = View.inflate(context, R.layout.action_item_desc,null);
        }else{
            view = View.inflate(context, R.layout.action_item,null);
        }

        switch (viewType){
            case 3:
            case 41:
            case 49:
            case 62:
                return new ClickTextScreenHolder(context,this);
            case 4:
            case 5:
            case 6:
            case 11:
            case 14:
                return new KeyHolder(context,this);
            case 12:
                return new AppHolder(context,this);
            case 13:
                return new SwipLineHolder(context,this);
            case 7:
            case 8:
            case 9:
            case 10:
                return new SwipHolder(context,this);
            case 15:
                return new InputTextHolder(context,this);
            case 17:
                return new AutoHolder(context,this);
            case 42:
                return new GesnterHolder(context,this);
            case 58:
            case 59:
            case 67:
            case 68:
                return new ColorHolder(context,this);
            case 30:
            case 50:
            case 60:
            case 61:
                return new ImageHolder(context,this);
            case 2:
                return new ClickScreenHolder(context,this);
            case 0:
            case 1:
                return new FlashHolder(context,this);
            case 20:
            case 21:
                return new BluethHolder(context,this);
            case 18:
            case 19:
                return new WifiHolder(context,this);
            case 16:
                return new LongClickScreenHolder(context,this);
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 31:
            case 34:
            case 35:
            case 36:
            case 37:
            case 69:
            case 57:
            case 64:
            case 65:
            case 66:
            case 76:
            case 82:
                return new SettingHolder(context,this);
            case 38:
            case 39:
            case 40:
                return new FastHolder(context,this);
            case 43:
                return new IfHolder(context,this);
            case 46:
                return new PasteHolder(context,this);
            case 51:
                return new RandomSleepHolder(context,this);
            case 55:
                return new ToastHolder(context,this);
            case 56:
                return new DescHolder(context,this);
            case 52:
                return new WhileHolder(context,this);
            case 53:
                return new ForHolder(context,this);
            case 70:
                return new HTTPHolder(context,this);
            case 71:
                return new AssginHolder(context,this);
            case 73:
                return new StringHolder(context,this);
            case 74:
                return new FileHolder(context,this);
            case 75:
                return new EncryptHolder(context,this);
            case 77:
                return new SLHolder(context,this);
            case 78:
                return new ArrayHolder(context,this);
            case 79:
                return new NodeHolder(context,this);
            case 80:
                return new DialogHolder(context,this);
            case 81:
                return new UIHolder(context,this);
            case 83:
                return new SystemHolder(context,this);
        }

        return new MyViewHolder(view,true);
    }

    @Override
    public int getItemViewType(int position) {
        return jsonObjects.getJson(position).getInt("actionType");
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseHolder){
            ((BaseHolder)holder).setVariables(variables).setJsonBean(jsonObjects.getJson(holder.getAdapterPosition()))
                    .onBind(jsonObjects.getJson(holder.getAdapterPosition()));
            return;
        }
    }



    @Override
    public int getItemCount() {
        return jsonObjects.length();
    }

    @Override
    public void onClickPosition(int position) {

    }

    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //拖拽时支持的方向向上向下
            int dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN;
            //滑动的时候支持的方向为左右
            return makeMovementFlags(dragFlags,0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            move(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //不处理左右滑动
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            PhoneUtils.vibrateShort();
        }
    });

    public ItemTouchHelper getItemTouchHelper() {
        return itemTouchHelper;
    }

    public void deleteItem(int adapterPosition) {
        jsonObjects.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        recyclerView.setVisibility(jsonObjects.length() == 0?View.GONE:View.VISIBLE);
    }

    boolean moreSelection = false;
    public void setMoreSelection(boolean b) {
        this.moreSelection = b;
        for (int i = 0 ;i < jsonObjects.length() ;i++){
            jsonObjects.getJson(i).put("selected",false);
        }
    }

    public boolean isMoreSelection() {
        return moreSelection;
    }




    public void selectedChanged(JSONBean jsonBean, BaseHolder position, boolean isSelected){
        jsonBean.put("selected",isSelected);
        ToastUtils.showLong("选中："+position.getAdapterPosition()+","+isSelected);
    }
}