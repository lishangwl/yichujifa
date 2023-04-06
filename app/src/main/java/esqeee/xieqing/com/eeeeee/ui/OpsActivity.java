package esqeee.xieqing.com.eeeeee.ui;

import android.Manifest;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieqing.codeutils.util.NoticUtil;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.SettingsCompat;
import com.xieqing.codeutils.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.captrue.IntentExtras;
import esqeee.xieqing.com.eeeeee.library.image.ScreenCaptruer;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;
import esqeee.xieqing.com.eeeeee.receiver.ScreenOffAdminReceiver;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

import static com.xieqing.codeutils.util.ActivityUtils.startActivity;


public class OpsActivity extends BaseActivity{

    @BindView(R.id.recylerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<Item> ops = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportToolBarWithBack(R.id.toolbar);

        ops.add(new SDCard());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ops.add(new CapScreen());
        }
        ops.add(new Window());
        ops.add(new ReadNotification());
        ops.add(new Admin());
        ops.add(new Root());
        if (PermissionUtils.hasUsageOption()){
            ops.add(new App());
        }
        ops.add(new Phone());
        ops.add(new PhoneState());
        ops.add(new Camara());
        ops.add(new Wifi());
        ops.add(new Blueteech());
        ops.add(new SMS());
        ops.add(new Notification());


        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        recyclerView.setAdapter(new Adapter());
        recyclerView.setLayoutManager(new MyLinearLayoutManager(self()));
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_ops;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!swipeRefreshLayout.isRefreshing()){
            refresh();
        }
    }

    void refresh(){
        if (!swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(true);
        }
        new Thread(()->{
            for (Item item : ops){
                item.setOpsGrengreted(item.checkOps());
            }
            runOnUiThread(()->{
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.getAdapter().notifyDataSetChanged();
            });
        }).start();
    }

    abstract class Item{
        boolean isOpsGrengreted = false;

        public void setOpsGrengreted(boolean opsGrengreted) {
            isOpsGrengreted = opsGrengreted;
        }

        public boolean isOpsGrengreted() {
            return isOpsGrengreted;
        }

        abstract String getName();
        abstract String getDescription();
        abstract @DrawableRes int getIcon();
        abstract boolean checkOps();
        abstract void request();
    }

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(self()).inflate(R.layout.ops_item,parent,false)) {};
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Item item = ops.get(position);
            if (item.isOpsGrengreted()){
                holder.itemView.setAlpha(0.2f);
                holder.itemView.setOnClickListener(null);
            }else{
                holder.itemView.setAlpha(1f);
                holder.itemView.setOnClickListener(v->{item.request();});
            }

            ((ImageView)holder.itemView.findViewById(R.id.image)).setImageResource(item.getIcon());
            ((TextView)holder.itemView.findViewById(R.id.title)).setText(item.getName());
            ((TextView)holder.itemView.findViewById(R.id.content)).setText(item.getDescription());

        }

        @Override
        public int getItemCount() {
            return ops.size();
        }
    }







    void requestPermission(String... permission){
        ActivityCompat.requestPermissions(self(),permission,20001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!swipeRefreshLayout.isRefreshing()){
            refresh();
        }
    }

    class Phone extends Item{

        @Override
        String getName() {
            return "电话";
        }

        @Override
        String getDescription() {
            return "用于拨打电话，不使用该动作则不需要";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_phone;
        }

        @Override
        boolean checkOps() {
            return checkPermissionsIsGentereted(Manifest.permission.CALL_PHONE);
        }

        @Override
        void request() {
            requestPermission(Manifest.permission.CALL_PHONE);
        }
    }

    PackageManager pm ;
    protected boolean checkPermissionsIsGentereted(String... permission) {
        if (pm == null){
            pm = getPackageManager();
        }
        for (String per : permission){
            boolean result = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(per, getPackageName()));
            if (!result) {
                //2.没有权限，弹出对话框申请
                return false;
            }
        }
        return true;
    }

    class SMS extends Item{

        @Override
        String getName() {
            return "发送读取短信";
        }

        @Override
        String getDescription() {
            return "用于发送读取短信，不使用该类动作则不需要";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_sms;
        }

        @Override
        boolean checkOps() {
            return checkPermissionsIsGentereted(Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS);
        }

        @Override
        void request() {
            requestPermission(Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS);
        }
    }

    class SDCard extends Item{

        @Override
        String getName() {
            return "文件读写";
        }

        @Override
        String getDescription() {
            return "用于SD卡文件读写，是软件基本必须权限";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_sdcard;
        }

        @Override
        boolean checkOps() {
            return checkPermissionsIsGentereted(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        @Override
        void request() {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    class Window extends Item{

        @Override
        String getName() {
            return "悬浮窗";
        }

        @Override
        String getDescription() {
            return "用于显示悬浮窗，是软件基本必须权限";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_window;
        }

        @Override
        boolean checkOps() {
            return SettingsCompat.canDrawOverlays(self());
        }

        @Override
        void request() {
            PermissionUtils.openAps(self());
        }
    }

    class Admin extends Item{

        @Override
        String getName() {
            return "设备管理器";
        }

        @Override
        String getDescription() {
            return "用于锁屏命令，开启之后可以加强后台能力";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_admin;
        }

        @Override
        boolean checkOps() {
            DevicePolicyManager policyManager = (DevicePolicyManager) Utils.getApp().getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName adminReceiver = new ComponentName(Utils.getApp(), ScreenOffAdminReceiver.class);
            return policyManager.isAdminActive(adminReceiver);
        }

        @Override
        void request() {
            ComponentName adminReceiver = new ComponentName(Utils.getApp(), ScreenOffAdminReceiver.class);
            Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
            intent.putExtra("android.app.extra.DEVICE_ADMIN", adminReceiver);
            intent.putExtra("android.app.extra.ADD_EXPLANATION", R.string.app_name);
            startActivity(intent);
        }
    }

    class Root extends Item{

        @Override
        String getName() {
            return "Root";
        }

        @Override
        String getDescription() {
            return "授权之后可以常驻后台，自动开启无障碍";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_root;
        }

        @Override
        boolean checkOps() {
            return Shell.getShell().su();
        }

        @Override
        void request() {
            Shell.getShell().su();
        }
    }

    class Camara extends Item{

        @Override
        String getName() {
            return "摄像头";
        }

        @Override
        String getDescription() {
            return "用于扫一扫二维码";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_camara;
        }

        @Override
        boolean checkOps() {
            return checkPermissionsIsGentereted(Manifest.permission.CAMERA);
        }

        @Override
        void request() {
            requestPermission(Manifest.permission.CAMERA);
        }
    }

    class CapScreen extends Item{

        @Override
        String getName() {
            return "截取屏幕";
        }

        @Override
        String getDescription() {
            return "用于截取实时屏幕";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_camara;
        }

        @Override
        boolean checkOps() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return ScreenCaptruer.mMediaProjection != null;
            }
            return false;
        }

        @Override
        void request() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent(OpsActivity.this, ScreenRequestActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                IntentExtras.newExtras()
                        .put("callback", new ActivityResultListener() {
                            @Override
                            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                if (requestCode == 4364){
                                    if (resultCode == Activity.RESULT_OK){
                                        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
                                        ScreenCaptruer.mMediaProjection = mediaProjectionManager.getMediaProjection(resultCode,data);
                                    }else{
                                        RuntimeLog.i("获取权限失败，请允许截屏！");
                                    }
                                }
                            }
                        })
                        .putInIntent(intent);
                startActivity(intent);
            }
        }
    }

    class PhoneState extends Item{

        @Override
        String getName() {
            return "手机信息";
        }

        @Override
        String getDescription() {
            return "用于采集一些必要信息，方便更好地完善软件增加体验感";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_phonestate;
        }

        @Override
        boolean checkOps() {
            return checkPermissionsIsGentereted(Manifest.permission.READ_PHONE_STATE);
        }

        @Override
        void request() {
            requestPermission(Manifest.permission.READ_PHONE_STATE);
        }
    }

    class Blueteech extends Item{

        @Override
        String getName() {
            return "蓝牙";
        }

        @Override
        String getDescription() {
            return "用于开关蓝牙，不使用此类动作可以不开启";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_bluetooch;
        }

        @Override
        boolean checkOps() {
            return checkPermissionsIsGentereted(Manifest.permission.BLUETOOTH);
        }

        @Override
        void request() {
            requestPermission(Manifest.permission.BLUETOOTH);
        }
    }

    class Wifi extends Item{

        @Override
        String getName() {
            return "Wifi";
        }

        @Override
        String getDescription() {
            return "用于开关wfif，不使用此类动作可以不开启";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_wifi;
        }

        @Override
        boolean checkOps() {
            return checkPermissionsIsGentereted(Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.CHANGE_WIFI_STATE);
        }

        @Override
        void request() {
            requestPermission(Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.CHANGE_WIFI_STATE);
        }
    }

    class Notification extends Item{

        @Override
        String getName() {
            return "发送通知栏";
        }

        @Override
        String getDescription() {
            return "用于将软件信息显示在通知栏之上";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_noti;
        }

        @Override
        boolean checkOps() {
            return NoticUtil.isNotificationEnabled(self());
        }

        @Override
        void request() {
            NoticUtil.toSetting(self());
        }
    }
    class ReadNotification extends Item{

        @Override
        String getName() {
            return "监听通知栏";
        }

        @Override
        String getDescription() {
            return "用于监听通知栏信息";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_noti;
        }

        @Override
        boolean checkOps() {
            return NoticUtil.isNotificationEnabled2(self());
        }

        @Override
        void request() {
            NoticUtil.toSetting2(self());
        }
    }
    class App extends Item{

        @Override
        String getName() {
            return "获取应用信息";
        }

        @Override
        String getDescription() {
            return "用于获取应用信息";
        }

        @Override
        int getIcon() {
            return R.mipmap.ic_ops_phonestate;
        }

        @Override
        boolean checkOps() {
            return PermissionUtils.isUsage();
        }

        @Override
        void request() {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }
}
