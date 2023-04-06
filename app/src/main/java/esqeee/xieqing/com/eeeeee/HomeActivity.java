package esqeee.xieqing.com.eeeeee;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SwitchCompat;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xq.wwwwwxxxxx.xqapppay.wx.listener.PayListener;
import com.xq.wwwwwxxxxx.xqapppay.wx.listener.PayResult;
import com.yicu.yichujifa.ui.theme.ThemeManager;
import com.yicu.yichujifa.ui.widget.ThemeBottomNavigationView;
import com.yicu.yichujifa.ui.widget.ThemeFloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.fragment.HomeAutoFragment;
import esqeee.xieqing.com.eeeeee.fragment.HomeCommunityFragment;
import esqeee.xieqing.com.eeeeee.fragment.HomeNavigationFragment;
import esqeee.xieqing.com.eeeeee.fragment.HomeSettingsFragmeng;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.listener.FragmentOnKeyListener;
import esqeee.xieqing.com.eeeeee.manager.MessageManager;
import esqeee.xieqing.com.eeeeee.manager.SensorManager;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.ui.UserActivity;
import esqeee.xieqing.com.eeeeee.ui.floatmenu.CircularMenu;
import esqeee.xieqing.com.eeeeee.user.User;
import esqeee.xieqing.com.eeeeee.widget.menu.BottomMenu;
import esqeee.xieqing.com.eeeeee.widget.viewPager.FragmentPagerAdapter;
import esqeee.xieqing.com.eeeeee.widget.viewPager.NoSwipeViewPager;

public class HomeActivity extends BaseActivity implements PayListener {


    private String TAG = HomeActivity.class.getSimpleName();


    private FragmentOnKeyListener fragmentOnKeyListener;

    public void setFragmentOnKeyListener(FragmentOnKeyListener fragmentOnKeyListener) {
        this.fragmentOnKeyListener = fragmentOnKeyListener;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (fragmentOnKeyListener != null && fragmentOnKeyListener.onKeyDown(keyCode, event)) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            //finish();
        }
        return false;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_home;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //检测是否重新实例化的软件，避免重复打开
        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action == null && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        MyApp.setSplashActivity(this);

        if (SettingsPreference.isShowFloatWindow()) {
            CircularMenu.getCircularMenu(this).show();
        }

        initView();

        //创建shortCut
        resgiterShortCut();
        if (SettingsPreference.appLuanchGetRoot()) {
            Shell.getShell().su();
        }
        if (SettingsPreference.useRootOpenAccess()) {
            AccessbilityUtils.open();
        }

        if (!SPUtils.getInstance("config").getBoolean("agreeUserPer", false)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("声明")
                    .setCancelable(false)
                    .setMessage("为保证本软件能正常运行以及用户的更好体验，软件需要获取一些必要权限以及采集您的一些必要的设备信息，在使用本软件之前，请认真阅读《用户协议》、《隐私政策》")
                    .setNeutralButton("用户协议", (d, i) -> {
                        BroswerActivity.luanchResource(self(), "app_use_agree.html");
                    }).setNegativeButton("隐私政策", (d, i) -> {
                BroswerActivity.luanchResource(self(), "app_use_xy.html");
            }).setPositiveButton("我已阅读并同意", (d, i) -> {
                SPUtils.getInstance("config").put("agreeUserPer", true);
            }).create().show();
        }
    }

    private void initView() {
        setStatusBarTheme(Color.WHITE, true);

        NoSwipeViewPager viewPager = findViewById(R.id.viewpager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeNavigationFragment());
        fragments.add(new HomeAutoFragment());
        fragments.add(new HomeCommunityFragment());
        fragments.add(new HomeSettingsFragmeng());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setCanSwipe(false);

        ThemeBottomNavigationView bottomNavigationView = findViewById(R.id.bnve);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int index = getIndex(menuItem.getItemId());
                if (index == 0) {
                    setStatusBarTheme(Color.WHITE, true);
                } else {
                    setStatusBarTheme(getResources().getColor(R.color.colorPrimary), Color.WHITE, false);
                }
                if (index != -1) {
                    viewPager.setCurrentItem(index, false);
                    return true;
                } else {
                    return false;
                }
            }
        });

        ThemeFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            new BottomMenu(HomeActivity.this).showAtLocation(getContentView(), Gravity.NO_GRAVITY, 0, 0);
        });
    }

    private int getIndex(@IdRes int id) {
        switch (id) {
            case R.id.id_navigation:
                return 0;
            case R.id.id_auto:
                return 1;
            case R.id.id_community:
                return 2;
            case R.id.id_setting:
                return 3;
            default:
                return -1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                if (listFragment == null) {
//                    //初始化底部导航
//                    initBottomNavgivation();
//                }
            } else {
                try {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1545);
                } catch (ActivityNotFoundException e) {
                    ToastUtils.showLong(e.getMessage());
                }
            }
        } else {
//            if (listFragment == null) {
//                //初始化底部导航
//                initBottomNavgivation();
//            }
        }


        MessageManager.getManager().readNew();
    }


    private void showPayDialog() {
        BroswerActivity.luanchResource(self(), "vip/index.html");
    }

    private void showComiPayDialog() {
        EditText editText = new EditText(self());
        editText.setHint("请输入卡密");
        new AlertDialog.Builder(self())
                .setTitle("卡密激活")
                .setView(editText)
                .setNegativeButton("取消", null)
                .setNeutralButton("购买", (d, i) -> {
                    BroswerActivity.luanch(self(), SPUtils.getInstance().getString("comiaddress"));
                })
                .setPositiveButton("激活", (d, i) -> {
                    if (editText.getText().toString().trim().isEmpty()) {
                        ToastUtils.showLong("请输入卡密");
                        return;
                    }
                    showProgress("激活中");
                    BBS.useCard(editText.getText().toString(), new CallBack() {
                        @Override
                        public void callBack(CallResult result) {
                            disProgress();
                            ToastUtils.showLong(result.getMessage());
                            EventBus.getDefault().post(User.getUser());
                        }
                    });
                })
                .show();
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void login(AddActivity addActivity) {
        setTag("payMoney", 10);
        new AlertDialog.Builder(self())
                .setTitle("当前功能需要开通会员")
                .setMessage("开通会员后可以使用一触即发完整功能，账号不绑定手机，您可以在任何手机登录，但不能同时登陆。\n请保管好账号不要借给他人，防止被盗，我们不接受任何理由来帮你找回密码\n" +
                        "当前会员功能包括但不限于：分享加密文件、使用常用命令代码、二维码分享脚本文件、创建界面文件、使用控件操作命令、程序操作命令等高阶命令、触发配置条件等 无限制使用软件任意功能等。\n" +
                        "后续将增更多功能。\n")
                .setNegativeButton("卡密开通", (d, i) -> {
                    showComiPayDialog();
                })
                .setPositiveButton("支付开通", (d, i) -> {
                    showPayDialog();
                })
                .show();
    }

    @Override
    public void onResult(PayResult result, String message) {
        ToastUtils.showLong(message);
        EventBus.getDefault().post(User.getUser());
    }
}
