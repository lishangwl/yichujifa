package esqeee.xieqing.com.eeeeee.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.xieqing.codeutils.util.LogUtils;

import java.util.List;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.bean.ActionGroup;
import esqeee.xieqing.com.eeeeee.listener.ChooseGroupListener;

public class ChooseGroupDialog extends AlertDialog.Builder {
    private Action action;
    private Dialog dialog;
    public ChooseGroupDialog(Action action,Context context) {
        super(context);
        this.action = action;
    }
    public void showDialog(ChooseGroupListener listener){
        /*int index = 0;
        List<ActionGroup> groups = Action.getAllGroupsAction();
        String[] groupNames = new String[groups.size()];
        for (int i = 0 ; i<groups.size() ; i++){
            groupNames[i] = groups.get(i).getName();
            Log.d("ChooseGroupDialog",action.getGroup()+"="+groups.get(i).getGroupId());
            if (action.getGroup().equals(groups.get(i).getGroupId())){
                index = i;
            }
        }
        setTitle("选择分组");
        setSingleChoiceItems(groupNames, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Action.moveGroup(action,groups.get(i));
                listener.onChoose(groups.get(i),action);
                dialog.dismiss();
            }
        });
        dialog = create();

        dialog.show();*/
    }
}
