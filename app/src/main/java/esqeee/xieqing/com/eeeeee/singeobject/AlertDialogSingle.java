package esqeee.xieqing.com.eeeeee.singeobject;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class AlertDialogSingle {
    private static androidx.appcompat.app.AlertDialog alertDialog;
    private static androidx.appcompat.app.AlertDialog.Builder builder;

    public static AlertDialog getAlertDialog(Context context, String title, String message,String ok,DialogInterface.OnClickListener okbtn,String cannel,DialogInterface.OnClickListener cannelBtn) {
        builder = new AlertDialog.Builder(context);
        alertDialog = builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(ok,okbtn)
                .setNegativeButton(cannel,cannelBtn).create();
        return alertDialog;
    }
}
