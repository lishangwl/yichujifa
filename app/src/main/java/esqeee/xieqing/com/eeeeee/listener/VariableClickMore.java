package esqeee.xieqing.com.eeeeee.listener;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;

import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.fragment.VariableTableFragment;

import static com.xieqing.codeutils.util.ActivityUtils.startActivity;

public class VariableClickMore implements View.OnClickListener,PopupMenu.OnMenuItemClickListener {
    private List<JSONBean> variables;
    private Context context;
    private RecyclerView.ViewHolder holder;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    public VariableClickMore(List<JSONBean> variables, Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter,RecyclerView.ViewHolder holder){
        this.variables = variables;
        this.context = context;
        this.holder = holder;
        this.adapter = adapter;
    }
    @Override
    public void onClick(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_var);
        popupMenu.setOnMenuItemClickListener(this);


        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete:
                try {
                    variables.remove(holder.getAdapterPosition() - VariableTableFragment.finalList.size());
                    adapter.notifyItemRemoved(holder.getAdapterPosition());
                }catch (Exception e){}
                break;
        }
        return false;
    }
}
