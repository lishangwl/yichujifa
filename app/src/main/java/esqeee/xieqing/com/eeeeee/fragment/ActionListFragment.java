package esqeee.xieqing.com.eeeeee.fragment;

import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.adapter.ActionListAdapter;
import esqeee.xieqing.com.eeeeee.bean.RefreshAction;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextListener;
import esqeee.xieqing.com.eeeeee.event.AddScriptEvent;
import esqeee.xieqing.com.eeeeee.event.RefreshActionEvent;
import esqeee.xieqing.com.eeeeee.library.record.AccessbilityRecorder;
import esqeee.xieqing.com.eeeeee.listener.FragmentOnKeyListener;
import esqeee.xieqing.com.eeeeee.listener.SwipeDontScollRecylerListener;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.widget.FloatingActionMenu;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

import static esqeee.xieqing.com.eeeeee.action.ActionHelper.actionToString;
import static esqeee.xieqing.com.eeeeee.action.ActionHelper.isRootWorkDir;

public class ActionListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,FragmentOnKeyListener {
    @BindView(R.id.listView)
    RecyclerView listView ;
    @BindView(R.id.swipe) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fabMenu) FloatingActionMenu fab;
    @BindView(R.id.fab) FloatingActionButton add;
    @BindView(R.id.fab2) FloatingActionButton about;


    ActionListAdapter adapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(RefreshAction refreshAction){
        onRefresh();
    }

    @Override
    public View getContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.content_list,null);
    }

    public File getCurrentDir() {
        return currentDir;
    }

    @Override
    public void onRefresh() {
        refreshAction();
    }


    public void addScript(){
        new InputTextDialog(getActivity(),false).setTitle("添加自动化脚本")
                .addInputLine(new InputTextLine("名称"))
                .setInputTextListener(result->{
                    String name = result[0].getResult().toString()+".ycf";
                    File file = new File(currentDir,name);
                    if (file.exists()){
                        ToastUtils.showShort("该脚本已经存在");
                    }else {
                        if (file.createNewFile()){
                            Action action = Action.newAction();
                            action.setData("");
                            if ( FileIOUtils.writeFileFromString(file,actionToString(action))){
                                onRefresh();
                            }else{
                                ToastUtils.showShort("创建失败，请查看是否给了软件存储权限");
                            }
                        }else{
                            ToastUtils.showShort("创建脚本失败！请检查权限");
                        }
                    }
                }).show();
    }

    /*
     * 悬浮按钮被点击
     * */
    private void fabTap(FloatingActionButton button, int pos) {
        switch (pos){
            case 3:
                addScript();
                break;
            case 2:
                new InputTextDialog(getActivity(),false).setTitle("添加界面")
                        .addInputLine(new InputTextLine("名称"))
                        .setInputTextListener(result->{
                            String name = result[0].getResult().toString()+".ycfml";
                            File file = new File(currentDir,name);
                            if (file.exists()){
                                ToastUtils.showShort("该界面已经存在");
                            }else {
                                if (file.createNewFile()){
                                    if (FileIOUtils.writeFileFromString(file,"<界面></界面>")){
                                        onRefresh();
                                    }else{
                                        ToastUtils.showShort("创建失败，请查看是否给了软件存储权限");
                                    }
                                }else{
                                    ToastUtils.showShort("创建界面失败！请检查权限");
                                }
                            }
                        }).show();
                break;
            case 0:
                new InputTextDialog(getActivity(),false).setTitle("添加文件夹")
                        .addInputLine(new InputTextLine("名称"))
                        .setInputTextListener(new InputTextListener() {
                            @Override
                            public void onConfirm(InputLine[] result) throws Exception{
                                String name = result[0].getResult().toString();
                                File file = new File(currentDir,name);
                                if (file.exists() && file.isDirectory()){
                                    ToastUtils.showShort("该文件夹已经存在");
                                }else {
                                    file.mkdir();
                                    onRefresh();
                                }
                            }
                        }).show();
                break;
            case 1:
                if (!PermissionUtils.getAppOps(getBaseActivity())){
                    ToastUtils.showShort("您需要开启悬浮窗权限，才能使用录制功能");
                    PermissionUtils.openAps(getBaseActivity());
                    return;
                }
                if (!AccessbilityUtils.isAccessibilitySettingsOn(getBaseActivity())){
                    AccessbilityUtils.toSetting();
                    return;
                }
                getActivity().moveTaskToBack(false);
                AccessbilityRecorder.getRecorder().setListener(b->{
                    AccessbilityRecorder.getRecorder().close();
                    AppUtils.resumeApp();

                    new InputTextDialog(getActivity()).setTitle("保存")
                            .addInputLine(new InputTextLine("名称",""))
                            .setInputTextListener(result->{
                                Action action = Action.newAction();
                                action.setData(b.buildString());

                                if (FileIOUtils.writeFileFromString(new File(currentDir,result[0].getResult().toString()+".ycf"),actionToString(action))){
                                    onRefresh();
                                }else{
                                    ToastUtils.showShort("创建失败，请查看是否给了软件存储权限");
                                }
                            }).show();
                });
                AccessbilityRecorder.getRecorder().startRecord(getActivity());
                break;
        }
    }


    private List<File> ycfList;
    private File currentDir = ActionHelper.workSpaceDir;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshActionEvent(RefreshActionEvent event){
        refreshAction();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddScriptEvent(AddScriptEvent event){
        addScript();
    }

    /*
     * 刷新所有的指令
     * */
    public synchronized void refreshAction() {

        swipeRefreshLayout.setRefreshing(true);
        new Thread(()->{
            if (currentDir == null){
                currentDir = new File(Environment.getExternalStorageDirectory().getPath()+"/YiChuJiFaProject/workSpace");
            }
            ycfList.clear();
            if (!isRootWorkDir(currentDir)) {
                ycfList.add(currentDir);
                adapter.setInRoot(false);
            }else{
                adapter.setInRoot(true);
            }
            ycfList.addAll(ActionHelper.searchAllActionAndDirectoryAndXml(currentDir));
            //adapter.setYcfList(ycfList);
            ThreadUtils.runOnUiThread(()->{
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.postDelayed(()->{
                    adapter.notifyItemRangeChanged(0,adapter.getItemCount());
                    if(getView() != null){
                        getView().requestLayout();
                    }
                },200);
            });

        }).start();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TheActionListFragment","onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void onFragmentInit() {
        ycfList = new ArrayList<>();
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter=new ActionListAdapter(getActivity(),ycfList);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(new SwipeDontScollRecylerListener(swipeRefreshLayout));
        listView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
        fab.setOnFloatingActionButtonClickListener(this::fabTap);
        adapter.setDirectoryInto(this::into);
        fab.stup(add);
        about.setOnClickListener(v -> {

        });

    }


    public void backDirectory(View view){
        this.currentDir = this.currentDir.getParentFile();
        onRefresh();
    }

    public void into(File directory) {
        this.currentDir = directory;
        onRefresh();
    }
    @Override
    public void onResume() {
        super.onResume();
        checkDirectory();
        onRefresh();
    }

    @Override
    public void onChangTheme() {
        super.onChangTheme();
        ThemeManager.attachTheme(add,fab,listView);
    }

    private void checkDirectory() {
        FileUtils.createOrExistsDir(ActionHelper.projectDir
                ,ActionHelper.workSpaceDir
                ,ActionHelper.workSpaceImageDir
                ,ActionHelper.saveDir
                ,ActionHelper.shareDir);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (!isRootWorkDir(currentDir)){
                backDirectory(null);
                return true;
            }
        }
        return false;
    }
}
