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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.xieqing.codeutils.util.HttpUtils;
import com.xieqing.codeutils.util.IntentUtils;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xq.wwwwwxxxxx.paydialog.PayDialog;
import com.xq.wwwwwxxxxx.xqapppay.wx.WXPay;
import com.xq.wwwwwxxxxx.xqapppay.wx.WXPayReq;
import com.xq.wwwwwxxxxx.xqapppay.wx.listener.PayListener;
import com.xq.wwwwwxxxxx.xqapppay.wx.listener.PayResult;
import com.yicu.yichujifa.ui.colorpicker.dialogs.ColorPickerDialog;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bbs.NetTask;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.fragment.DetailActivity;
import esqeee.xieqing.com.eeeeee.fragment.ListFragment;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.listener.FragmentOnKeyListener;
import esqeee.xieqing.com.eeeeee.manager.MessageManager;
import esqeee.xieqing.com.eeeeee.manager.SensorManager;
import esqeee.xieqing.com.eeeeee.plugin.update.Updater;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.ui.AboutActivity;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.ui.OpsActivity;
import esqeee.xieqing.com.eeeeee.ui.Settings;
import esqeee.xieqing.com.eeeeee.ui.UserActivity;
import esqeee.xieqing.com.eeeeee.ui.floatmenu.CircularMenu;
import esqeee.xieqing.com.eeeeee.user.User;

public class SplashActivity extends BaseActivity implements PayListener, NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {


    private String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    private FragmentTransaction fragmentTransaction;
    private ListFragment listFragment;


    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }


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
        return R.layout.activity_splash;
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

        EventBus.getDefault().register(this);

        //MyApp.setSplashActivity(this);//TODO 如果用到需要解除屏蔽
        //初始化侧滑栏
        initDrawber();


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

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (listFragment == null) {
                    //初始化底部导航
                    initBottomNavgivation();
                }
            } else {
                try {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1545);
                } catch (ActivityNotFoundException e) {
                    ToastUtils.showLong(e.getMessage());
                }
            }
        } else {
            if (listFragment == null) {
                //初始化底部导航
                initBottomNavgivation();
            }
        }


        MessageManager.getManager().readNew();
    }


    private SwitchCompat powerOptimize;
    private SwitchCompat floatWindow;
    private SwitchCompat sensor;
    private SwitchCompat accessbility;
    View drawablelayout_header;
    NavigationView navigationView;

    private void initDrawber() {
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(this);

        drawablelayout_header = navigationView.getHeaderView(0);
        navigationView.getMenu().findItem(R.id.youhua).setVisible(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
        powerOptimize = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.youhua));
        floatWindow = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.floatwindow));
        sensor = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.sensor));
        accessbility = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.access));

        navigationView.getHeaderView(0).findViewById(R.id.tosetting).setOnClickListener(view -> {
            drawerLayout.closeDrawer(Gravity.START);
            drawerLayout.postDelayed(() -> {
                Intent intent2 = new Intent(SplashActivity.this, Settings.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
            }, 300);
        });

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.textView)).setText("点击登录/注册");
        navigationView.getHeaderView(0).setOnClickListener(v -> {

        });
        powerOptimize.setOnClickListener(this::powerOptimize);
        floatWindow.setOnClickListener(this::stupFloatWindow);
        sensor.setOnClickListener(this::sensor);
        accessbility.setOnClickListener(this::accessbility);

        powerOptimize.setChecked(PermissionUtils.isBatteryOptimization());
        floatWindow.setChecked(SettingsPreference.isShowFloatWindow());
        sensor.setChecked(SettingsPreference.isSensorOpen());
        accessbility.setChecked(AccessbilityUtils.isAccessibilitySettingsOn(self()));

        if (SettingsPreference.isSensorOpen()) {
            SensorManager.getManager().register();
        }

        floatWindow.post(() -> {
            if (SettingsPreference.isShowFloatWindow()) {
                CircularMenu.getCircularMenu(this).show();
            }
        });


        loginStateChanged(User.getUser());
        changedTheme(null);
    }

    public void sensor(View c) {
        boolean isOpen = sensor.isChecked();
        SettingsPreference.setSensorOpen(isOpen);

        if (isOpen) {
            SensorManager.getManager().register();
        } else {
            SensorManager.getManager().unregister();
        }
    }


    public void accessbility(View c) {
        boolean isOpen = accessbility.isChecked();

        if (isOpen) {
            if (!AccessbilityUtils.isAccessibilitySettingsOn(this)) {
                AccessbilityUtils.toSetting();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (AccessbilityUtils.getService() == null) {
                    return;
                }
                AccessbilityUtils.getService().disableSelf();
            } else {
                AccessbilityUtils.toSetting();
            }
        }
    }

    public void stupFloatWindow(View c) {
        if (!PermissionUtils.getAppOps(this)) {
            floatWindow.setChecked(false);
            ToastUtils.showShort("请先开启悬浮窗权限");
            PermissionUtils.openAps(this);
            return;
        }
        boolean isOpen = floatWindow.isChecked();
        SettingsPreference.setShowFloatWindow(isOpen);

        if (isOpen) {
            CircularMenu.getCircularMenu(this).show();
        } else {
            CircularMenu.getCircularMenu(this).close();
        }
    }


    public void powerOptimize(View compoundButton) {
        if (!powerOptimize.isChecked()) {
            return;
        }
        if (!PermissionUtils.isBatteryOptimization()) {
            new AlertDialog.Builder(this).setTitle("关于后台运行")
                    .setMessage("为了让一触即发能够在后台长久运行，而不会被系统频繁的杀死，我们需要您把一触即发加入电池优化的白名单。")
                    .setPositiveButton("拒绝", (DialogInterface dialogInterface, int i) -> {
                        powerOptimize.setChecked(false);
                    }).setNegativeButton("确定", (DialogInterface dialogInterface, int i) -> {
                        PermissionUtils.batteryOptimization();
                    }).create().show();
        }
    }


    private void initBottomNavgivation() {
        listFragment = new ListFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.rootView, listFragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(Gravity.START);
        drawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (item.getItemId()) {
                    case R.id.nav_share:
                        Intent intent = IntentUtils.getShareTextIntent("一触即发是一款快捷指令自动化操作的软件，可以释放你得双手，完成简单重复的动作，点击下载https://www.coolapk.com/apk/" + getPackageName(), true);
                        startActivity(intent);
                        break;
                    case R.id.nav_send:

                        break;
                    case R.id.nav_video:

                        break;
                    case R.id.nav_log:

                        break;
                    case R.id.sensor:
                        startActivity(new Intent(SplashActivity.this, Main4Activity.class));
                        break;
                    case R.id.permission:
                        startActivity(new Intent(SplashActivity.this, OpsActivity.class));
                        break;
                    case R.id.user_acrtle:
                        UserActivity.luanch(SplashActivity.this, User.getUser().getUid(), "我的动态");
                        break;
                    case R.id.user_collect:
                        UserActivity.luanch2(SplashActivity.this, User.getUser().getUid(), "我的收藏");
                        break;
                    case R.id.user_detail:
                        startActivity(new Intent(SplashActivity.this, DetailActivity.class));
                        break;
                    case R.id.user_vip:

                        break;
                    case R.id.nav_agree:
                        startActivity(new Intent(SplashActivity.this, AboutActivity.class));
                        break;
                    case R.id.theme:
                        new ColorPickerDialog()
                                .withColor(Color.BLACK) // the default / initial color
                                .withPresets(new int[]{0xFFF44336, 0xFFE91E63, 0xFF9C27B0, 0xFF673AB7
                                        , 0xFF3F51B5, 0xFF2196F3, 0xFF03A9F4, 0xFF00BCD4, 0xFF009688
                                        , 0xFF4CAF50, 0xFF8BC34A, 0xFFCDDC39,
                                        0xFFFFEB3B, 0xFFFFC107, 0xFFFF9800, 0xFFFF5722, 0xFF795548, 0xFF9E9E9E, 0xFF607D8B})
                                .withListener((@Nullable ColorPickerDialog dialog, int color) -> {
                                    ThemeManager.setColorPrimary(color);
                                    ThemeManager.attachTheme(self());
                                    EventBus.getDefault().post(ThemeManager.Theme.DEFULT);
                                })
                                .show(getSupportFragmentManager(), "颜色选择器");
                        break;
                }
            }
        }, 500);

        return true;
    }

    public void openVip() {
        setTag("payMoney", 10);
        new AlertDialog.Builder(self())
                .setTitle("开通会员")
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

    private void showPayChooseDialog() {
        new PayDialog(self())
                .setData(Double.parseDouble(getTag("payMoney").toString()), 0)//输入支付金额，余额
                .haveBalance(false)
                .setListener(new PayDialog.OnPayClickListener() {
                    @Override
                    public void onPayClick(int payType) {
                        switch (payType) {
                            case PayDialog.ALI_PAY:
                                alipay();
                                break;
                            case PayDialog.WX_PAY:
                                wxPay();
                                break;
                        }
                    }
                }).show();
    }

    private void alipay() {
        showProgress("正在生成订单");
        BBS.hackPay(getTag("payMoney").toString(), new CallBack() {
            @Override
            public void callBack(CallResult result) {
                disProgress();
                if (result.getCode() == 0) {
                    BroswerActivity.luanch(self(), result.getString("data"));
                } else {
                    ToastUtils.showLong("下单失败");
                }
            }
        });
    }


    private void wxPay() {
        showProgress("正在生成订单");
        ThreadUtils.executeByCached(new NetTask<JSONBean>() {
            @Nullable
            @Override
            public JSONBean doInBackground() throws Throwable {
                return new JSONBean(HttpUtils.Get("http://bbs.yicuba.com/public/index.php/user/order?fee=" + String.valueOf(((int) getTag("payMoney")) * 100) + "&attach=" + User.getUser().getUid() + "&body=一触即发会员"));
            }

            @Override
            public void onSuccess(@Nullable JSONBean result) {
                disProgress();
                WXPayReq wxPayReq = new WXPayReq();
                wxPayReq.setBody("一触即发会员");
                wxPayReq.setPrice(String.valueOf(((int) getTag("payMoney")) * 100));//价格
                wxPayReq.setPrepayId(result.getString("prepayId"));
                WXPay.getWxPay().payWithPrepayId(self(), wxPayReq, SplashActivity.this::onResult);
            }
        });
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
    public void changedTheme(ThemeManager.Theme theme) {
        ThemeManager.attachTheme(drawablelayout_header
                , accessbility, sensor, floatWindow, powerOptimize);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginStateChanged(User user) {

        navigationView.getHeaderView(0).findViewById(R.id.App).setVisibility(View.VISIBLE);
        navigationView.getHeaderView(0).findViewById(R.id.User).setVisibility(View.GONE);

    }


    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        EventBus.getDefault().post(User.getUser());
        accessbility.setChecked(AccessbilityUtils.getService() != null);
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        EventBus.getDefault().post(User.getUser());
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }


    @Override
    public void onResult(PayResult result, String message) {
        ToastUtils.showLong(message);
        EventBus.getDefault().post(User.getUser());
    }
}
