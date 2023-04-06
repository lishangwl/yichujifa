package esqeee.xieqing.com.eeeeee.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.MenuItemCompat;

import com.xieqing.codeutils.util.IntentUtils;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.ui.colorpicker.dialogs.ColorPickerDialog;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.BroswerActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.SplashActivity;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.manager.SensorManager;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.ui.AboutActivity;
import esqeee.xieqing.com.eeeeee.ui.OpsActivity;
import esqeee.xieqing.com.eeeeee.ui.Settings;
import esqeee.xieqing.com.eeeeee.ui.UserActivity;
import esqeee.xieqing.com.eeeeee.ui.floatmenu.CircularMenu;
import esqeee.xieqing.com.eeeeee.user.User;


public class HomeSettingsFragmeng extends BaseFragment {

    private View mView;

    private SwitchCompat powerOptimize;
    private SwitchCompat floatWindow;
    private SwitchCompat sensor;
    private SwitchCompat accessbility;

    @Override
    public View getContentView(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.frgament_home_settings, null, false);
        return mView;
    }

    @Override
    protected void onFragmentInit() {
        initView(mView);
    }

    private void initView(View view) {
        initHeader(view);
        initBottom(view);
        initConfig();
    }

    private void initConfig() {
        if (SettingsPreference.isSensorOpen()) {
            SensorManager.getManager().register();
        }

        floatWindow.post(() -> {
            if (SettingsPreference.isShowFloatWindow()) {
                CircularMenu.getCircularMenu(getContext()).show();
            }
        });

        loginStateChanged(User.getUser());

        changedTheme(null);
    }

    private void initBottom(View view) {
        view.findViewById(R.id.user_vip).setOnClickListener(v -> {

        });
        view.findViewById(R.id.user_detail).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DetailActivity.class));
        });
        view.findViewById(R.id.user_acrtle).setOnClickListener(v -> {
            UserActivity.luanch(getContext(), User.getUser().getUid(), "我的动态");
        });
        view.findViewById(R.id.user_collect).setOnClickListener(v -> {
            UserActivity.luanch2(getContext(), User.getUser().getUid(), "我的收藏");
        });
        view.findViewById(R.id.theme).setOnClickListener(v -> {
            new ColorPickerDialog()
                    .withColor(Color.BLACK) // the default / initial color
                    .withPresets(new int[]{0xFFF44336, 0xFFE91E63, 0xFF9C27B0, 0xFF673AB7
                            , 0xFF3F51B5, 0xFF2196F3, 0xFF03A9F4, 0xFF00BCD4, 0xFF009688
                            , 0xFF4CAF50, 0xFF8BC34A, 0xFFCDDC39,
                            0xFFFFEB3B, 0xFFFFC107, 0xFFFF9800, 0xFFFF5722, 0xFF795548, 0xFF9E9E9E, 0xFF607D8B})
                    .withListener((@Nullable ColorPickerDialog dialog, int color) -> {
                        ThemeManager.setColorPrimary(color);
                        ThemeManager.attachTheme(getActivity());
                        EventBus.getDefault().post(ThemeManager.Theme.DEFULT);
                    })
                    .show(getChildFragmentManager(), "颜色选择器");
        });
        view.findViewById(R.id.permission).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), OpsActivity.class));
        });
        view.findViewById(R.id.nav_video).setOnClickListener(v -> {
            startActivity(IntentUtils.getUrlIntent("http://www.baidu.com/esqeee.xieqing.com.eeeeee/teach/help.html"));
        });
        view.findViewById(R.id.nav_log).setOnClickListener(v -> {
            BroswerActivity.luanch(getContext(), "http://www.baidu.com/esqeee.xieqing.com.eeeeee/updatelog/index.html");
        });
        view.findViewById(R.id.nav_share).setOnClickListener(v -> {
            Intent intent = IntentUtils.getShareTextIntent("一触即发是一款快捷指令自动化操作的软件，可以释放你得双手，完成简单重复的动作，点击下载https://www.coolapk.com/apk/" + getContext().getPackageName(), true);
            startActivity(intent);
        });
        view.findViewById(R.id.nav_send).setOnClickListener(v -> {

        });
        view.findViewById(R.id.nav_agree).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AboutActivity.class));
        });

        accessbility = view.findViewById(R.id.access);
        powerOptimize = view.findViewById(R.id.youhua);
        floatWindow = view.findViewById(R.id.floatwindow);
        sensor = view.findViewById(R.id.sensor);

        powerOptimize.setVisibility(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? View.VISIBLE : View.GONE);

        accessbility.setOnClickListener(this::accessbility);
        powerOptimize.setOnClickListener(this::powerOptimize);
        floatWindow.setOnClickListener(this::stupFloatWindow);
        sensor.setOnClickListener(this::sensor);

        accessbility.setChecked(AccessbilityUtils.isAccessibilitySettingsOn(getContext()));
        powerOptimize.setChecked(PermissionUtils.isBatteryOptimization());
        floatWindow.setChecked(SettingsPreference.isShowFloatWindow());
        sensor.setChecked(SettingsPreference.isSensorOpen());

    }

    @Override
    public void onResume() {
        super.onResume();
        accessbility.setChecked(AccessbilityUtils.isAccessibilitySettingsOn(getContext()));
        powerOptimize.setChecked(PermissionUtils.isBatteryOptimization());
        floatWindow.setChecked(SettingsPreference.isShowFloatWindow());
        sensor.setChecked(SettingsPreference.isSensorOpen());
    }

    public void accessbility(View v) {
        boolean isOpen = accessbility.isChecked();

        if (isOpen) {
            if (!AccessbilityUtils.isAccessibilitySettingsOn(getContext())) {
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

    public void powerOptimize(View v) {
        if (!powerOptimize.isChecked()) {
            return;
        }
        if (!PermissionUtils.isBatteryOptimization()) {
            new AlertDialog.Builder(getContext()).setTitle("关于后台运行")
                    .setMessage("为了让一触即发能够在后台长久运行，而不会被系统频繁的杀死，我们需要您把一触即发加入电池优化的白名单。")
                    .setPositiveButton("拒绝", (DialogInterface dialogInterface, int i) -> {
                        powerOptimize.setChecked(false);
                    }).setNegativeButton("确定", (DialogInterface dialogInterface, int i) -> {
                        PermissionUtils.batteryOptimization();
                    }).create().show();
        }
    }

    public void stupFloatWindow(View v) {
        if (!PermissionUtils.getAppOps(getContext())) {
            floatWindow.setChecked(false);
            ToastUtils.showShort("请先开启悬浮窗权限");
            PermissionUtils.openAps(getContext());
            return;
        }
        boolean isOpen = floatWindow.isChecked();
        SettingsPreference.setShowFloatWindow(isOpen);

        if (isOpen) {
            CircularMenu.getCircularMenu(getContext()).show();
        } else {
            CircularMenu.getCircularMenu(getContext()).close();
        }
    }

    public void sensor(View v) {
        boolean isOpen = sensor.isChecked();
        SettingsPreference.setSensorOpen(isOpen);

        if (isOpen) {
            SensorManager.getManager().register();
        } else {
            SensorManager.getManager().unregister();
        }
    }

    private void initHeader(View view) {
        view.findViewById(R.id.tosetting).setOnClickListener(v -> {
            Intent intent2 = new Intent(getContext(), Settings.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent2);

        });
        ((TextView) view.findViewById(R.id.textView)).setText("点击登录/注册");
        view.findViewById(R.id.drawablelayout_header).setOnClickListener(v -> {

        });
        EventBus.getDefault().post(User.getUser());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginStateChanged(User user) {

        mView.findViewById(R.id.App).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.User).setVisibility(View.GONE);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changedTheme(ThemeManager.Theme theme) {
        ThemeManager.attachTheme(accessbility, sensor, floatWindow, powerOptimize);
    }

}
