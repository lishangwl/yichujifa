package esqeee.xieqing.com.eeeeee.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.listener.OnActionAddListener;
import esqeee.xieqing.com.eeeeee.widget.CustomPath;
import esqeee.xieqing.com.eeeeee.widget.XQPathView;

public class SwipePathDialog extends Dialog {

    private long time = System.currentTimeMillis();
    public float endX,endY;
    public SwipePathDialog(@NonNull Context context, final OnActionAddListener listener) {
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

        View view = View.inflate(getContext(), R.layout.dialog_genter,null);
        setContentView(view);

        XQPathView xqPathView = view.findViewById(R.id.xq);
        xqPathView.setLinePath(true);

        xqPathView.setOnGestureListener(new XQPathView.OnGestureListener() {
            @Override
            public void onStart() {
                view.findViewById(R.id.onok).setVisibility(View.GONE);
                time = System.currentTimeMillis();
            }

            @Override
            public void onFinished(CustomPath path, float endX, float endY) {
                view.findViewById(R.id.onok).setVisibility(View.VISIBLE);
                SwipePathDialog.this.endX = endX;
                SwipePathDialog.this.endY = endY;

                time = System.currentTimeMillis() - time;
            }
        });

        view.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                xqPathView.reset();
                view.findViewById(R.id.onok).setVisibility(View.GONE);
            }
        });
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONBean param = new JSONBean()
                        .put("start_x",(int)xqPathView.getDownX())
                        .put("start_y",(int)xqPathView.getDownY())
                        .put("end_x",(int)endX)
                        .put("end_y",(int)endY)
                        .put("path",xqPathView.toJsonArray());
                JSONBean json = new JSONBean()
                        .put("actionType",13)
                        .put("witeTime",1000)
                        .put("param",param);
                listener.addAction(json);
                dismiss();
            }
        });
    }
}
