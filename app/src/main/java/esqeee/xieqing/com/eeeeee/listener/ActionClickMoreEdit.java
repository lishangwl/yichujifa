package esqeee.xieqing.com.eeeeee.listener;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;

import android.view.MenuItem;
import android.view.View;

import com.xieqing.codeutils.util.ToastUtils;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.BaseHolder;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;

import static com.xieqing.codeutils.util.ActivityUtils.startActivity;

public class ActionClickMoreEdit implements View.OnClickListener,PopupMenu.OnMenuItemClickListener {
    private JSONBean action;
    private Context context;
    private MyAdapter adapter;
    BaseHolder holder;


    public ActionClickMoreEdit(JSONBean action, Context context, MyAdapter adapter, BaseHolder holder){
        this.action = action;
        this.context = context;
        this.holder = holder;
        this.adapter = adapter;
    }


    @Override
    public void onClick(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_action_edit);
        popupMenu.setOnMenuItemClickListener(this);


        MenuItem item_enable = popupMenu.getMenu().findItem(R.id.item_enable);
        MenuItem item_paste = popupMenu.getMenu().findItem(R.id.item_paste);

        boolean status = action.has("status") ? action.getBoolean("status"):true;
        item_enable.setTitle(status?"禁止动作":"激活动作");
        item_paste.setVisible(adapter.getCopy() != null);

        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete:
                holder.onDelete();
                adapter.deleteItem(holder.getAdapterPosition());
                break;
            case R.id.item_enable:
                if (action.has("status")){
                    action.put("status",!action.getBoolean("status"));
                }else{
                    action.put("status",false);
                }
                adapter.notifyItemChanged(holder.getAdapterPosition());
                break;
            case R.id.item_add:
                adapter.setOnMenuSelectPosition(holder.getAdapterPosition());
                ToastUtils.showShort("下一次动作将添加在该动作后面");
                break;
            case R.id.item_edit:
                JSONBean param = action.getJson("param");
                new InputTextDialog(context).setTitle("输入备注")
                        .addInputLine(new InputTextLine("备注信息",param.has("desc") && !param.getString("desc").equals("")?param.getString("desc"):"").setCanInputNull(true))
                        .setInputTextListener((InputLine[] result)->{
                            param.put("desc",result[0].getResult());
                            adapter.notifyItemChanged(holder.getAdapterPosition());
                        }).show();
                break;
            case R.id.item_copy:
                adapter.setCopy(holder.copy());
                break;
            case R.id.item_cut:
                holder.onDelete();
                adapter.deleteItem(holder.getAdapterPosition());
                adapter.setCopy(holder.copy());
                break;
            case R.id.item_paste:
                adapter.paste(holder.getAdapterPosition());
                break;
        }
        return true;
    }
}
