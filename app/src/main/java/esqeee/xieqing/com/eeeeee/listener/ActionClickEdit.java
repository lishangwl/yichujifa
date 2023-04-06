package esqeee.xieqing.com.eeeeee.listener;

import android.content.Intent;
import android.view.View;

import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.Utils;

import java.io.File;

import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.action.Action;

import static com.xieqing.codeutils.util.ActivityUtils.startActivity;

public class ActionClickEdit implements View.OnClickListener {
    private File action;
    public ActionClickEdit(File action){
        this.action = action;
    }
    @Override
    public void onClick(View view) {
        if (!PermissionUtils.getAppOps(Utils.getApp())){
            ToastUtils.showShort("请先开启悬浮窗权限");
            PermissionUtils.openAps(Utils.getApp());
            return;
        }
        Intent intent = new Intent(view.getContext(),AddActivity.class);
        intent.putExtra("path",action.getAbsolutePath());
        startActivity(intent);
    }
}
