package esqeee.xieqing.com.eeeeee.adapter.ViewHolder;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.ActionListAdapter;
import esqeee.xieqing.com.eeeeee.bean.RefreshAction;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;

public class ActionDirectoryViewHolder extends ListViewHolder{
    private ActionListAdapter actionListAdapter;
    public ActionDirectoryViewHolder(View itemView,ActionListAdapter actionListAdapter) {
        super(itemView);
        this.actionListAdapter = actionListAdapter;
    }

    @Override
    public void bindView(File ycfProject) {

        Log.d("DirectoryViewHolder",getAdapterPosition()+"bind->"+ycfProject.getName());
        View convertView = itemView;
        ((TextView)convertView.findViewById(R.id.group_name)).setText(ycfProject.getName());

        ThemeManager.attachTheme((ImageView)convertView.findViewById(R.id.group_icon));

        convertView.setOnClickListener(v->{
            actionListAdapter.onDirectoryClick(ycfProject);
        });


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(actionListAdapter.getContext()).setTitle("对该分组进行下列操作")
                        .setItems(new String[]{"重命名", "删除"}, (DialogInterface dialogInterface, int i) ->{
                                if (i==0){
                                    new InputTextDialog(actionListAdapter.getContext(),false)
                                            .setTitle("重命名文件夹")
                                            .addInputLine(new InputTextLine("名称",ycfProject.getName()))
                                            .setInputTextListener((InputLine[] result)->{
                                                    String name = result[0].getResult().toString();
                                                    if (FileUtils.isFileExists(name)){
                                                        ToastUtils.showShort("该文件夹已经存在");
                                                    }else {
                                                        ycfProject.renameTo(new File(ycfProject.getParent(),name));
                                                        EventBus.getDefault().post(RefreshAction.INTANSCE);
                                                    }
                                            }).show();
                                }else{
                                    new AlertDialog.Builder(actionListAdapter.getContext()).setTitle("删除分组").setMessage("该操作将会把文件夹以及该文件夹下所有的脚本都删除，确定继续删除吗？")
                                            .setPositiveButton("确定",(DialogInterface d, int i2)-> {
                                                FileUtils.deleteDir(ycfProject);
                                                EventBus.getDefault().post(RefreshAction.INTANSCE);
                                            }).setNegativeButton("取消",null).create().show();
                                }
                        }).show();
                return false;
            }
        });
    }
}
