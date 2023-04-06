package esqeee.xieqing.com.eeeeee.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xieqing.codeutils.util.ScreenUtils;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.library.MarkSizeView;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.listener.OnMakeSizeRectListener;

public class MakeSizeDialog extends Dialog {

    private AppCompatTextView mTip;
    private MarkSizeView markSizeView;
    private OnMakeSizeRectListener onMakeSizeRectListener;



    public MakeSizeDialog setOnMakeSizeRectListener(OnMakeSizeRectListener onMakeSizeRectListener) {
        this.onMakeSizeRectListener = onMakeSizeRectListener;
        return this;
    }
    public MakeSizeDialog setTip(String tip){
        if (mTip!=null){
            mTip.setText(tip);
        }
        return this;
    }
    View view;
    public MakeSizeDialog(@NonNull Context context) {
        super(context,R.style.MyScreenTheme);
        Window window =this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        window.setAttributes(lp);
        window.setType(MyApp.getFloatWindowType());
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        view = View.inflate(getContext(), R.layout.dialog_mark_size,null);
        setContentView(view);


        this.markSizeView = view.findViewById(R.id.mark_size);
        this.mTip = view.findViewById(R.id.capture_tips);

        this.markSizeView.setmOnClickListener(new MarkSizeView.onClickListener() {
            public void onConfirm(Rect m) {
                RuntimeLog.log(m);
                int[] ints = new int[2];
                markSizeView.getLocationOnScreen(ints);
                RuntimeLog.log(ints);
                if (ints[0]>0){
                    m.left = m.left + ints[0];
                    m.right = m.right + ints[0];
                }
                if (ints[1]>0){
                    m.top = m.top + ints[1];
                    m.bottom = m.bottom + ints[0];
                }
                RuntimeLog.log(m);
                mTip.setVisibility(View.GONE);
                dismiss();
                if (m.left < 0 ){
                    m.left = 0;
                }
                if (m.top < 0){
                    m.top = 0;
                }
                if (m.bottom > ScreenUtils.getScreenHeight()){
                    m.bottom = ScreenUtils.getScreenHeight();
                }
                if (m.right > ScreenUtils.getScreenWidth()){
                    m.right = ScreenUtils.getScreenWidth();
                }
                onMakeSizeRectListener.onConfirnm(new Rect(m));
            }

            @Override
            public void onConfirm(MarkSizeView.GraphicPath path) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onTouch() {
                mTip.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void show() {

        super.show();
    }
}
