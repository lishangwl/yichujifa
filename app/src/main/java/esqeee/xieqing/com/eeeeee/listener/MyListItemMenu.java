package esqeee.xieqing.com.eeeeee.listener;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.View;

public class MyListItemMenu implements View.OnCreateContextMenuListener{
    private Activity activity;
    public MyListItemMenu(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

    }
}
