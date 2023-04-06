package esqeee.xieqing.com.eeeeee.dialog;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.R;

public class WxDialog {

    public static void show(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_wx, null);
        new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton("复制微信号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ClipboardManager) Utils.getApp().getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("一触即发--剪贴板","Ming75336"));
                        ToastUtils.showShort("复制成功");
                    }
                })
                .setNegativeButton("确定", null)
                .show();
    }

}
