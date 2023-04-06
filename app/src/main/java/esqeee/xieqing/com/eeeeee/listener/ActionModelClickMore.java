package esqeee.xieqing.com.eeeeee.listener;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.bean.ActionGroup;
import esqeee.xieqing.com.eeeeee.bean.RefreshAction;

import static com.xieqing.codeutils.util.ActivityUtils.startActivity;

public class ActionModelClickMore implements View.OnClickListener,PopupMenu.OnMenuItemClickListener {
    private Action action;
    private ActionGroup actionGroup;
    private Context context;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    public ActionModelClickMore(Action action, ActionGroup actionGroup, Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter){
        this.action = action;
        this.actionGroup = actionGroup;
        this.context = context;
        this.adapter = adapter;
    }
    @Override
    public void onClick(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenu().add("添加到我的指令");
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        action.save();
        EventBus.getDefault().post(RefreshAction.INTANSCE);
        return false;
    }
}
