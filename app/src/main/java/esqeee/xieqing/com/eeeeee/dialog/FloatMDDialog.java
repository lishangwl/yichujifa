package esqeee.xieqing.com.eeeeee.dialog;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;

public class FloatMDDialog{
    private Context context;
    private String title;

    private String confirm;
    private String cannel;
    private View.OnClickListener onConfirmListener;
    private View.OnClickListener onCannelListener;


    private FloatWindow floatWindow;

    private FloatMDDialog(Context context){
        this.context = context;
        floatWindow = new FloatWindow.FloatWindowBuilder()
                .id("FloatMDDialog["+System.currentTimeMillis()+"]")
                .move(false)
                .with(null)
                .withClick(null)
                .param(new FloatWindow.FloatWindowLayoutParamBuilder()
                        .flags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
                        .format(PixelFormat.TRANSLUCENT)
                        .height(-1)
                        .width(-1)
                        .type(MyApp.getFloatWindowType())
                        .build())
                .build();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCannel(String cannel) {
        this.cannel = cannel;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public void setOnCannelListener(View.OnClickListener onCannelListener) {
        this.onCannelListener = onCannelListener;
    }

    public void setOnConfirmListener(View.OnClickListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }


    public void show(){

    }

    public static class Builder{
        private FloatMDDialog dialog;
        public Builder(Context context){
            dialog = new FloatMDDialog(context);
        }

        public Builder setTitle(String title){
            dialog.setTitle(title);
            return this;
        }

        public Builder setConfirm(String title, View.OnClickListener listener){
            dialog.setConfirm(title);
            dialog.setOnConfirmListener(listener);
            return this;
        }

        public Builder setCannel(String title, View.OnClickListener listener){
            dialog.setCannel(title);
            dialog.setOnCannelListener(listener);
            return this;
        }

        public FloatMDDialog build(){
            return dialog;
        }
    }
}
