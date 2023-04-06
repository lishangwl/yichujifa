package esqeee.xieqing.com.eeeeee.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ScreenOffAdminReceiver extends DeviceAdminReceiver {
    private void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context,"设备管理器权限可用");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "设备管理器权限不可用");
    }
}
