package esqeee.xieqing.com.eeeeee.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.thl.filechooser.FileChooser;
import com.thl.filechooser.FileInfo;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.GlobalContext;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import esqeee.xieqing.com.eeeeee.ListActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bean.ActionGroup;
import esqeee.xieqing.com.eeeeee.bean.RefreshAction;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.ui.Apk;
import esqeee.xieqing.com.eeeeee.utils.ActivationUtils;

import static com.xieqing.codeutils.util.ActivityUtils.startActivity;

public class XmlClickMore implements View.OnClickListener,PopupMenu.OnMenuItemClickListener {
    private File action;
    private ActionGroup actionGroup;
    private Context context;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    public XmlClickMore(File action, Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter){
        this.action = action;
        this.context = context;
        this.adapter = adapter;
    }
    @Override
    public void onClick(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_list_item_xml);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (!action.exists()){
            EventBus.getDefault().post(RefreshAction.INTANSCE);
            return true;
        }
        switch (item.getItemId()){
            case R.id.item_move:
                FileChooser fileChooser = new FileChooser(context, new FileChooser.FileChoosensListener() {
                    @Override
                    public void onFileChoosens(String[] var1) {
                        if (var1 == null || var1.length == 0){
                            return;
                        }
                        File file = new File(var1[0]);
                        if (file.exists() && file.isDirectory()){
                            FileUtils.moveFile(action,new File(file,action.getName()));
                            EventBus.getDefault().post(RefreshAction.INTANSCE);
                        }
                    }
                });
                fileChooser.setChooseType(FileInfo.FILE_TYPE_FOLDER);
                fileChooser.setThemeColor(R.color.colorAccent);
                fileChooser.setTitle("移动到");
                fileChooser.setCurrentPath(ActionHelper.workSpaceDir.getPath());
                fileChooser.open();
                break;
            case R.id.item_rename:
                new InputTextDialog(context,false).setTitle("重命名")
                        .addInputLine(new InputTextLine("名称",FileUtils.getFileNameNoExtension(action)))
                        .setInputTextListener(result -> {
                            String name = result[0].getResult().toString()+".ycfml";
                            File file = new File(action.getParent(),name);
                            if (file.exists()){
                                ToastUtils.showShort("已经存在");
                            }else {
                                FileUtils.rename(action,name);
                                EventBus.getDefault().post(RefreshAction.INTANSCE);
                            }
                        }).show();
                break;
            case R.id.item_copy:
                new Thread(()->{
                    File copy = new File(action.getParent(),FileUtils.getFileNameNoExtension(action)+"_副本.ycfml");
                    boolean is = FileUtils.copyFile(action,copy);
                    EventBus.getDefault().post(RefreshAction.INTANSCE);
                }).start();
                break;
            case R.id.item_share:
                startActivity(new Intent(context, ListActivity.class).putExtra("path",action.getAbsolutePath()));
                break;
            case R.id.item_delete:
                new AlertDialog.Builder(context).setTitle("提示").setMessage("删除将会永久无法恢复，您确定要删除吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", (DialogInterface dialogInterface, int i2) ->{
                                new Thread(()->{
                                    action.delete();
                                    EventBus.getDefault().post(RefreshAction.INTANSCE);
                                }).start();
                        }).create().show();
                break;
            case R.id.item_apk:
                if (!ActivationUtils.isActivation()){
                ActivationUtils.showActivationDialog(context);
                break;
            }
                startActivity(new Intent(GlobalContext.getContext(),Apk.class).putExtra("id",action.getAbsolutePath()));
                break;
        }
        return false;
    }
}
