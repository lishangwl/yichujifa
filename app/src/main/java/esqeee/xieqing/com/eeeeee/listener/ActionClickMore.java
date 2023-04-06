package esqeee.xieqing.com.eeeeee.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.thl.filechooser.FileChooser;
import com.thl.filechooser.FileInfo;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.SizeUtils;
import com.xieqing.codeutils.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import esqeee.xieqing.com.eeeeee.ListActivity;
import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.bean.ActionGroup;
import esqeee.xieqing.com.eeeeee.bean.DeleteAction;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.bean.RefreshAction;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.manager.NotificationManager;
import esqeee.xieqing.com.eeeeee.service.FloattingService;
import esqeee.xieqing.com.eeeeee.ui.ShortCutIntentActivity;
import esqeee.xieqing.com.eeeeee.utils.ActivationUtils;
import esqeee.xieqing.com.eeeeee.widget.CircleImageView;

import static com.xieqing.codeutils.util.ActivityUtils.startActivity;

public class ActionClickMore implements View.OnClickListener,PopupMenu.OnMenuItemClickListener {
    private File action;
    private ActionGroup actionGroup;
    private Context context;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    public ActionClickMore(File action, Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter){
        this.action = action;
        this.context = context;
        this.adapter = adapter;
    }
    @Override
    public void onClick(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_list_item);
        popupMenu.setOnMenuItemClickListener(this);

        MenuItem item_notic = popupMenu.getMenu().findItem(R.id.item_notic);
        MenuItem item_float = popupMenu.getMenu().findItem(R.id.item_float);

        if (NotificationManager.getManager(context).actionIsShow(action)){
            item_notic.setTitle("取消通知栏显示");
        }else{
            item_notic.setTitle("在通知栏显示");
        }
        item_float.setTitle(FloattingService.getService() == null ?"添加悬浮窗":(FloattingService.getService().hasWindow(action.getPath())?"关闭悬浮窗":"添加悬浮窗"));
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (!action.exists()){
            EventBus.getDefault().post(RefreshAction.INTANSCE);
            return true;
        }
        switch (item.getItemId()){
            case R.id.item_notic:
                if (!ActivationUtils.isActivation()){
                    ActivationUtils.showActivationDialog(context);
                    break;
                }
                if (NotificationManager.getManager(context).actionIsShow(action)){
                    NotificationManager.getManager(context).cannelNotification(action);
                }else{
                    NotificationManager.getManager(context).postNotification(action);
                }
                break;
            case R.id.item_shortcut:
                PhoneUtils.addShortCut(MyApp.getSplashActivity(),FileUtils.getFileNameNoExtension(action),R.drawable.action_1,
                        new Intent(context,ShortCutIntentActivity.class)
                                .setAction("doAction")
                                .putExtra("actionID",action.getPath()));
                break;
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
                            String name = result[0].getResult().toString()+".ycf";
                            File file = new File(action.getParent(),name);
                            if (file.exists()){
                                ToastUtils.showShort("该脚本已经存在");
                            }else {
                                FileUtils.rename(action,name);
                                EventBus.getDefault().post(RefreshAction.INTANSCE);
                            }
                        }).show();
                break;
            case R.id.item_copy:
                new Thread(()->{
                    File copy = new File(action.getParent(),FileUtils.getFileNameNoExtension(action)+"_副本.ycf");
                    boolean is = FileUtils.copyFile(action,copy);
                    if (!is){return;}
                    Action action = ActionHelper.from(copy);
                    if (action != null){
                        String data = action.getData();
                        if (data == null){
                            data = "";
                        }
                        List<String> images = ActionHelper.getAllImages(action);
                        for (String image:images){
                            File y = new File(image);
                            File t = new File(y.getParent(),FileUtils.getFileNameNoExtension(y)+"_copy."+FileUtils.getFileExtension(y));
                            RuntimeLog.i(y.getAbsolutePath());
                            RuntimeLog.i(t.getAbsolutePath());
                            FileUtils.copyFile(y,t);
                            data = data.replace(y.getName(),t.getName());
                        }
                        action.title = FileUtils.getFileNameNoExtension(ActionClickMore.this.action)+"_副本";
                        action.setData(data);
                        action.save();
                    }
                    EventBus.getDefault().post(RefreshAction.INTANSCE);
                }).start();
                break;
            case R.id.item_share:
                Action action1 = ActionHelper.from(this.action);
                //如果脚本是加密的脚本，继续加密分享
                if (action1.isEncrypt()){
                    startActivity(new Intent(context, ListActivity.class)
                            .putExtra("encrypt",true)
                            .putExtra("path", this.action.getAbsolutePath()));

                }else{
                    new AlertDialog.Builder(context).setTitle("是否加密")
                            .setMessage("以加密形式分享，对方只能运行，无法编辑脚本")
                            .setPositiveButton("加密",(d,i)->{
                                startActivity(new Intent(context, ListActivity.class)
                                        .putExtra("encrypt",true)
                                        .putExtra("path", this.action.getAbsolutePath()));
                            }).setNegativeButton("不加密",(d,i)->{
                        startActivity(new Intent(context, ListActivity.class)
                                .putExtra("encrypt",false)
                                .putExtra("path", this.action.getAbsolutePath()));
                    }).setNeutralButton("取消",null).show();
                }
                break;
            case R.id.item_delete:
                new AlertDialog.Builder(context).setTitle("提示").setMessage("删除脚本将会永久无法恢复，您确定要删除吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", (DialogInterface dialogInterface, int i2) ->{
                                new Thread(()->{
                                    List<String> images = ActionHelper.getAllImages(ActionHelper.from(this.action));
                                    for (String image : images){
                                        FileUtils.delete(image);
                                    }
                                    images.clear();
                                    this.action.delete();
                                    if (FloattingService.getService().hasWindow(this.action.getPath())){
                                        FloattingService.getService().closeWindow(this.action.getPath());
                                    }


                                    EventBus.getDefault().post(new DeleteAction(this.action));
                                    EventBus.getDefault().post(RefreshAction.INTANSCE);
                                }).start();
                        }).create().show();
                break;
            case R.id.item_float:
                if (!ActivationUtils.isActivation()){
                    ActivationUtils.showActivationDialog(context);
                    break;
                }
                if (!PermissionUtils.getAppOps(context)){
                    ToastUtils.showShort("请先开启悬浮窗权限");
                    PermissionUtils.openAps(context);
                    break;
                }
                boolean hasExits = FloattingService.getService().hasWindow(this.action.getPath());
                if (hasExits){
                    FloattingService.getService().closeWindow(this.action.getPath());
                }else{
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2,-2,MyApp.getFloatWindowType(),WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,-3);

                    layoutParams.width = SizeUtils.dp2px(40);
                    layoutParams.height = SizeUtils.dp2px(40);
                    layoutParams.x = 0;
                    layoutParams.gravity = Gravity.LEFT|Gravity.TOP;
                    layoutParams.y = ScreenUtils.getScreenHeight()/3;

                    CircleImageView imageView = new CircleImageView(context);
                    imageView.setBorderColor(Color.WHITE);
                    imageView.setBorderWidth(5);
                    imageView.setImageDrawable(ActionHelper.from(this.action).getDrawable());

                    new FloatWindow.FloatWindowBuilder()
                            .id(this.action.getPath())
                            .move(true)
                            .with(imageView)
                            .withClick(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActionRunHelper.startAction(context, ActionClickMore.this.action);
                                }
                            })
                            .param(layoutParams)
                            .build().add();
                }
                break;
        }
        return false;
    }
}
