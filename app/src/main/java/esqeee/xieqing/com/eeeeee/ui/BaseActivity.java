package esqeee.xieqing.com.eeeeee.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.PermissionChecker;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.fragment.BaseFragment;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class BaseActivity extends AppCompatActivity {


    private Map<String, Object> prepotey = new HashMap<>();
    private View mContentView;

    public void setTag(String key, Object tag) {
        if (tag == null) {
            prepotey.remove(key);
        } else {
            prepotey.put(key, tag);
        }

        //System.gc();
    }

    public void setStatusBarTheme(int statusBarColor, int navigationBarColor, boolean isLightTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            getWindow().setStatusBarColor(statusBarColor);
        }
        setStatusBarTextColor(isLightTheme);
        setNavigationBarBackgroundColor(navigationBarColor);
    }

    public void setStatusBarTheme(int statusBarColor, boolean isLightTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            getWindow().setStatusBarColor(statusBarColor);
        }
        setStatusBarTextColor(isLightTheme);
        setNavigationBarBackgroundColor(statusBarColor);
    }

    public void setStatusBarTextColor(boolean isLightTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            if (isLightTheme) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }
    }

    public void setNavigationBarBackgroundColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(color);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (Exception e) {
            RuntimeLog.i(e.getMessage());
        }
    }

    public BaseActivity self() {
        return this;
    }

    public Object getTag(String key) {
        return prepotey.get(key);
    }

    public synchronized Fragment getTopFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() == 0) {
            return null;
        }
        return (Fragment) fragments.get(fragments.size() - 1);
    }

    public void supportToolBarWithBack(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        ThemeManager.attachTheme(toolbar);
    }

    public void supportToolBarWithBack(@IdRes int toolbar) {
        supportToolBarWithBack((Toolbar) findViewById(toolbar));
    }

    public synchronized int getFragmentSize() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        return fragments.size();
    }

    private ProgressDialog progressDialog;

    public void showProgress(String message) {
        showProgress("加载中", message);
    }

    public void showProgress(String title, String message) {
        progressDialog = null;
        ThreadUtils.runOnUiThread(() -> {
            progressDialog = ProgressDialog.show(this, title, message);
        });
    }

    public void disProgress() {
        ThreadUtils.runOnUiThread(() -> {
            progressDialog.dismiss();
        });
    }

    public View getContentView() {
        return mContentView;
    }

    public int getContentLayout() {
        return 0;
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            fixOrientation();
        }
        super.onCreate(savedInstanceState);

        attachView();
        ThemeManager.attachTheme(this);
    }


    public void attachView() {
        if (getContentLayout() != 0) {
            //setContentView(getContentLayout());
            mContentView = LayoutInflater.from(this).inflate(getContentLayout(), null);
            setContentView(mContentView);
        } else {
            mContentView = getContentView();
            if (mContentView != null)
                setContentView(mContentView);
        }
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeManager.attachTheme(getSupportActionBar());
    }

    public void addFragment(int id, BaseFragment fragment) {
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.auto_in, R.anim.auto_out, R.anim.auto_in, R.anim.auto_out)
                    .add(
                            id,
                            fragment
                    ).addToBackStack(null).commit();
        } catch (Exception e) {
            //
        }
    }


    /*
     *   注册快捷方式
     * */
    protected void resgiterShortCut() {
        if (Build.VERSION.SDK_INT >= 25) {
            ShortcutManager mShortcutManager = getSystemService(ShortcutManager.class);
            List<ShortcutInfo> infos = new ArrayList<>();


            ShortcutInfo info = new ShortcutInfo.Builder(this, "shortcut-1")
                    .setShortLabel("自动化设置")
                    .setIcon(Icon.createWithResource(this, R.drawable.icon))
                    .setIntent(new Intent(this, Settings.class).setAction("setting"))
                    .build();
            infos.add(info);
            mShortcutManager.setDynamicShortcuts(infos);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String[] getRequestPermissions(String[] permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (checkSelfPermission(permission) == PERMISSION_DENIED) {
                list.add(permission);
            }
        }
        return list.toArray(new String[list.size()]);
    }


    protected static final int PERMISSION_REQUEST_CODE = 11186;


    protected void checkPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] requestPermissions = getRequestPermissions(permissions);
            if (requestPermissions.length > 0) {
                requestPermissions(requestPermissions, PERMISSION_REQUEST_CODE);
            }
        } else {
            int[] grantResults = new int[permissions.length];
            Arrays.fill(grantResults, PERMISSION_GRANTED);
            onRequestPermissionsResult(PERMISSION_REQUEST_CODE, permissions, grantResults);
        }
    }


    private AlertDialog noPersmssionDialog;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result != PERMISSION_GRANTED) {
                if (noPersmssionDialog == null) {
                    noPersmssionDialog = new AlertDialog.Builder(this).setTitle("获取权限失败！")
                            .setMessage("软件需要最基本的权限来正常运行，为了能让您有更好的体验，请允许给软件必要的权限！")
                            .setNegativeButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checkPermissions(permissions);
                                }
                            }).setPositiveButton("退出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AppUtils.exitApp();
                                }
                            }).create();
                }
                noPersmssionDialog.show();
            }
        }
    }


    private List<ActivityResultListener> listeners = new ArrayList<>();

    public void addActivityResultListener(ActivityResultListener listener) {
        listeners.add(listener);
    }

    public void removeActivityResultListener(ActivityResultListener listener) {
        listeners.remove(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            for (ActivityResultListener listener : listeners) {
                listener.onActivityResult(requestCode, resultCode, data);
            }
        } catch (ConcurrentModificationException exception) {
            RuntimeLog.i(exception.getMessage());
        }
    }

    public List<OnBackPressed> backPresseds = new ArrayList<>();

    public void addBackPressedListener(OnBackPressed backPressed) {
        backPresseds.add(backPressed);
    }

    public interface OnBackPressed {
        boolean back();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        for (OnBackPressed pressed : backPresseds) {
            if (pressed.back()) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    protected boolean checkPermission(String permission) {
        switch (permission) {
            case "window":
                if (!PermissionUtils.getAppOps(this)) {
                    Toast.makeText(this, "您需要开启悬浮窗权限，才能正常使用！", Toast.LENGTH_LONG).show();
                    PermissionUtils.openAps(this);
                    return false;
                }
                break;
            default:
                int permission1 = ActivityCompat.checkSelfPermission(self(), permission);
                if (permission1 != PermissionChecker.PERMISSION_GRANTED) {
                    //2.没有权限，弹出对话框申请
                    return false;
                    //ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_CODE_STORAGE);
                } else {
                    return true;
                }
        }
        return true;
    }

    protected boolean checkPermission(String... permission) {
        for (String per : permission) {
            int permission1 = ActivityCompat.checkSelfPermission(self(), per);
            if (permission1 != PermissionChecker.PERMISSION_GRANTED) {
                //2.没有权限，弹出对话框申请
                return false;
            }
        }
        return true;
    }

}
