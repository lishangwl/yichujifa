package esqeee.xieqing.com.eeeeee.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.xieqing.codeutils.util.NoticUtil;
import com.xieqing.codeutils.util.PermissionUtils;

import esqeee.xieqing.com.eeeeee.R;

public class PersmissionDialog extends AlertDialog implements View.OnClickListener{
    private static PersmissionDialog persmissionDialog;
    private View view ;

    public static PersmissionDialog getPersmissionDialog(Context context) {
        if (persmissionDialog!=null){
            persmissionDialog.cancel();
            persmissionDialog = null;
        }
        persmissionDialog = new PersmissionDialog(context);
        return persmissionDialog;
    }

    public PersmissionDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public PersmissionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public void init(){
        Window window =this.getWindow();
        //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setTitle("您需要开启以下权限");
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        view = View.inflate(getContext(),R.layout.dialog_permission,null);
        setView(view);
    }

    public PersmissionDialog reset(){
        //判断通知栏
        if (NoticUtil.isNotificationEnabled(getContext())){
            view.findViewById(R.id.item_1).setOnClickListener(null);
            view.findViewById(R.id.item_1).setAlpha(0.2f);
        }else{
            view.findViewById(R.id.item_1).setAlpha(1);
            view.findViewById(R.id.item_1).setOnClickListener(this);
        }

        //判断通知栏2
        if (NoticUtil.isNotificationEnabled2(getContext())){
            view.findViewById(R.id.item_2).setOnClickListener(null);
            view.findViewById(R.id.item_2).setAlpha(0.2f);
        }else{
            view.findViewById(R.id.item_2).setAlpha(1);
            view.findViewById(R.id.item_2).setOnClickListener(this);
        }

        //应用使用情况
        if (PermissionUtils.isUsage()||!PermissionUtils.hasUsageOption()){
            view.findViewById(R.id.item_4).setOnClickListener(null);
            view.findViewById(R.id.item_4).setAlpha(0.2f);
        }else{
            view.findViewById(R.id.item_4).setAlpha(1);
            view.findViewById(R.id.item_4).setOnClickListener(this);
        }

        //悬浮窗
        if (PermissionUtils.getAppOps(getContext())){
            view.findViewById(R.id.item_3).setOnClickListener(null);
            view.findViewById(R.id.item_3).setAlpha(0.2f);
        }else{
            view.findViewById(R.id.item_3).setAlpha(1);
            view.findViewById(R.id.item_3).setOnClickListener(this);
        }

        dismiss();
        return this;
    }

    public boolean showDialog() {
        if (NoticUtil.isNotificationEnabled(getContext()) &&
                NoticUtil.isNotificationEnabled2(getContext())&&
                (PermissionUtils.isUsage()||!PermissionUtils.hasUsageOption())&&
                PermissionUtils.getAppOps(getContext())){
            dismiss();
            return true;
        }
        super.show();
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_4:
                try {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    getContext().startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.item_1:
                NoticUtil.toSetting(getContext());
                break;
            case R.id.item_2:
                NoticUtil.toSetting2(getContext());
                break;
            case R.id.item_3:
                PermissionUtils.openAps(getContext());
                break;
        }
    }
}
