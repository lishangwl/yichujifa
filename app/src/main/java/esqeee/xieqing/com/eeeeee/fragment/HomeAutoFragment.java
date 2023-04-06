package esqeee.xieqing.com.eeeeee.fragment;

import static android.app.Activity.RESULT_OK;
import static butterknife.OnPageChange.Callback.PAGE_SCROLLED;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.thl.filechooser.FileChooser;
import com.thl.filechooser.FileInfo;
import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.HttpUtil;
import com.xieqing.codeutils.util.LogUtils;
import com.xieqing.codeutils.util.StringUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.inputborad.SettingsActivity;
import com.yicu.yichujifa.ui.theme.ThemeManager;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnPageChange;
import esqeee.xieqing.com.eeeeee.BroswerActivity;
import esqeee.xieqing.com.eeeeee.HomeActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bean.Log;
import esqeee.xieqing.com.eeeeee.bean.RefreshAction;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;
import esqeee.xieqing.com.eeeeee.listener.FragmentOnKeyListener;
import esqeee.xieqing.com.eeeeee.manager.SensorManager;
import esqeee.xieqing.com.eeeeee.service.SeriveManager;
import esqeee.xieqing.com.eeeeee.singeobject.AlertDialogSingle;
import esqeee.xieqing.com.eeeeee.sql.model.Message;
import esqeee.xieqing.com.eeeeee.ui.LogActivity;
import esqeee.xieqing.com.eeeeee.ui.NotifactionActivity;
import esqeee.xieqing.com.eeeeee.user.User;
import esqeee.xieqing.com.eeeeee.widget.BadgeActionProvider;
import esqeee.xieqing.com.eeeeee.widget.viewPager.LazyFragmentPagerAdapter;
import esqeee.xieqing.com.eeeeee.widget.viewPager.LazyViewPager;

public class HomeAutoFragment extends BaseFragment implements ActivityResultListener, Toolbar.OnMenuItemClickListener, FragmentOnKeyListener, SearchView.OnQueryTextListener{
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewPager)
    LazyViewPager viewPager;

    @Override
    public void onChangTheme() {
        super.onChangTheme();
        ThemeManager.attachTheme(toolbar,tabLayout);
    }

    private BaseFragment[] fragments;
    @Override
    public View getContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_list,null);
    }
    @OnPageChange(value = R.id.viewPager,callback = PAGE_SCROLLED)
    void onScroll(int position){
        if (fragments[position] instanceof FragmentOnKeyListener){
            if (((HomeActivity)getBaseActivity())!=null)
                ((HomeActivity)getBaseActivity()).setFragmentOnKeyListener((FragmentOnKeyListener)fragments[position]);
        }else{
            if (((HomeActivity)getBaseActivity())!=null)
                ((HomeActivity)getBaseActivity()).setFragmentOnKeyListener(null);
        }

        if (fragments[position] instanceof SearchView.OnQueryTextListener){
            searchItem.setVisible(true);
        }else {
            searchItem.setVisible(false);
        }
    }

    MenuItem searchItem;
    SearchView searchItemView;
    MenuItem messageItem;
    BadgeActionProvider mMessageBadgeActionProvider;
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (fragments[viewPager.getCurrentItem()] instanceof SearchView.OnQueryTextListener){
            ((SearchView.OnQueryTextListener)fragments[viewPager.getCurrentItem()]).onQueryTextSubmit(query);
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (fragments[viewPager.getCurrentItem()] instanceof SearchView.OnQueryTextListener){
            ((SearchView.OnQueryTextListener)fragments[viewPager.getCurrentItem()]).onQueryTextChange(newText);
            return true;
        }
        return false;
    }

    /*
     * 初始化
     * */
    @Override
    protected void onFragmentInit() {
        fragments = new BaseFragment[]{
                ((ActionListFragment) Fragment.instantiate(getActivity(),ActionListFragment.class.getName()))
                ,((RunActionFragment) Fragment.instantiate(getActivity(),RunActionFragment.class.getName()))
                ,((TimerActionFragment) Fragment.instantiate(getActivity(),TimerActionFragment.class.getName()))
                ,((NetAutoFragment) Fragment.instantiate(getActivity(),NetAutoFragment.class.getName())).setBaseActivity(getBaseActivity())
        };
        viewPager.setAdapter(new LazyFragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            protected Fragment getItem(ViewGroup container, int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText("自动化");
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText("运行中");
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText("管理");
        Objects.requireNonNull(tabLayout.getTabAt(3)).setText("示例");
        toolbar.inflateMenu(R.menu.menu_list);
        toolbar.setOnMenuItemClickListener(this);
        searchItem = toolbar.getMenu().findItem(R.id.action_search);
        searchItemView = (SearchView) searchItem.getActionView();
        searchItemView.setOnQueryTextListener(this);
        messageItem = toolbar.getMenu().findItem(R.id.action_notifcation);
        mMessageBadgeActionProvider = (BadgeActionProvider) MenuItemCompat.getActionProvider(messageItem);
        mMessageBadgeActionProvider.setIcon(R.mipmap.ic_notif);
        mMessageBadgeActionProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageBadgeActionProvider.setmTvBadge(View.GONE);
                onMenuItemClick(messageItem);
            }
        });
        ((HomeActivity)getActivity()).addActivityResultListener(this);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSucess(User user){

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newMessage(Message Message){
        if (mMessageBadgeActionProvider == null){
            return;
        }
        mMessageBadgeActionProvider.setmTvBadge(View.VISIBLE);
    }



    /*
     * 右上角menu菜单被单击
     * */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_scan){
            requestCarmara();
        }else if (item.getItemId() == R.id.action_log){
            Intent intent = new Intent(getActivity(), LogActivity.class);
            getActivity().startActivity(intent);
        }else if (item.getItemId() == R.id.action_exit){
            exit();
        }else if (item.getItemId() == R.id.action_import){
            doImportFile();
        }else if (item.getItemId() == R.id.action_ime){
            startActivity(new Intent(getBaseActivity(), SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }else if (item.getItemId() == R.id.action_notifcation){
            startActivity(new Intent(getBaseActivity(), NotifactionActivity.class));
        }
        return false;
    }


    /*
     *   选择导入脚本
     *
     * */
    private void doImportFile() {
        FileChooser fileChooser = new FileChooser(getActivity(), new FileChooser.FileChoosensListener() {
            @Override
            public void onFileChoosens(String[] filePaths) {
                AlertDialogSingle.getAlertDialog(getBaseActivity(),"导入脚本","是否导入脚本？\n脚本数："+filePaths.length+"个","导入",
                        (DialogInterface dialogInterface, int i) ->{
                            new Thread(()->{
                                getBaseActivity().showProgress("正在导入");
                                for (String file : filePaths){
                                    ActionHelper.importTo(file,((ActionListFragment)fragments[0]).getCurrentDir().getAbsolutePath());
                                }
                                EventBus.getDefault().post(RefreshAction.INTANSCE);
                                getBaseActivity().disProgress();
                            }).start();
                        },"取消",null).show();
            }
        });
        fileChooser.setBackIconRes(R.drawable.ic_back);
        fileChooser.setTitle("选择导入的脚本");
        fileChooser.setDoneText("确定");
        fileChooser.setThemeColor(R.color.colorPrimary);
        fileChooser.setChooseType(FileInfo.FILE_TYPE_ALL);
        fileChooser.showFile(true);  //是否显示文件
        fileChooser.open();
    }

    /*
     *   完全退出App
     * */
    private void exit() {
        getActivity().finish();
        RuntimeLog.log("exitApp","finish");
        SeriveManager.getSeriveManager(getActivity()).reslese();
        EventBus.getDefault().unregister(this);
        RecordAutoCaptruer.getIntance().release();
        SensorManager.getManager().unregister();

        AppUtils.exitApp();
    }

    /*
     *   获取照相机权限
     * */
    private void requestCarmara() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},1);
            return;
        }
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        getActivity().startActivityForResult(intent, 15462);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.e("xieqing","onActivityResult");
        // 扫描二维码/条码回传
        if (requestCode == 15462 && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                if (content.contains("www.baidu.com/esqeee.xieqing.com.eeeeee/share.php")){
                    scanShareAction(content);
                }else if (content.startsWith("http://")||content.startsWith("https://")){
                    BroswerActivity.luanch(getActivity(),content);
                }
            }
        }
    }


    private boolean scanShareActionFinish = true;

    /*
     *   扫描到分享的脚本
     * */
    private void scanShareAction(String content) {
        if (!scanShareActionFinish){
            return;
        }
        scanShareActionFinish = false;
        String path = "";
        String id = content.substring(content.indexOf("id=")+3);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(id.substring(0,13))));
        boolean isActionFile = content.contains("share.php");
        if (isActionFile){
            path = "http://www.baidu.com/esqeee.xieqing.com.eeeeee/"+today +"/"+id+".ycf";
        }else{
            path = "http://www.baidu.com/esqeee.xieqing.com.eeeeee/"+today +"-ycfml/"+id+".ycfml";
        }
        HttpUtil.get(path,new HashMap<>(),new HttpUtil.HttpCall(){
            @Override
            public void onSuccess(String string) throws PackageManager.NameNotFoundException {
                super.onSuccess(string);
                scanShareActionFinish = true;
                if (isActionFile){
                    android.util.Log.d("scanShareAction","string " + string);
                    String title = StringUtils.getSubString(string,"<title>","</title>");
                    if (TextUtils.isEmpty(title)){
                        if (string.contains("encrypt=a")) {
                            String a = ActionHelper.decrypt(string);
                            title = StringUtils.getSubString(a,"<title>","</title>");
                        }
                    }
                    final Action action = ActionHelper.stringToAction(string,true);
                    if (action!=null){
                        String finalTitle = title;
                        AlertDialogSingle.getAlertDialog(getActivity(),"导入脚本","是否导入:"+title+"\n动作个数:"+action.getCount()+"\n重复次数:"+action.getRepeat()+"\n\n温馨提示:如需做到所有手机均可使用，有关于坐标的命令请使用屏幕变量使用数学表达式表达.\n","导入", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                action.setPath(((ActionListFragment)fragments[0]).getCurrentDir().getAbsolutePath()+"/"+ finalTitle +".ycf");
                                action.save();
                                EventBus.getDefault().post(RefreshAction.INTANSCE);
                            }
                        },"取消",null).show();
                    }else{
                        ToastUtils.showShort("导入快捷指令失败");
                    }
                }else{
                    new AlertDialog.Builder(getActivity()).setTitle("是否导入界面文件？")
                            .setNegativeButton("确定",(dialog, which) -> {
                                FileIOUtils.writeFileFromString(((ActionListFragment)fragments[0]).getCurrentDir().getAbsolutePath()+"/"+id+".ycf",content);
                            }).setPositiveButton("取消",null).show();

                }
            }

            @Override
            public void onError(int code, String string) {
                super.onError(code, string);
                scanShareActionFinish = true;
                ToastUtils.showShort("导入失败，检测你的网络重试");
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
