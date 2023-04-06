package esqeee.xieqing.com.eeeeee.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import androidx.core.widget.NestedScrollView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xieqing.codeutils.util.SizeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.listener.OnItemClickListener;

public class FDialog {

    private FloatWindow floatWindow;
    private View view;

    @BindView(R.id.fd_title)
    TextView title;

    @BindView(R.id.fd_message)
    TextView message;

    @BindView(R.id.fd_sure)
    TextView canfirm;

    @BindView(R.id.fd_cannel)
    TextView cannel;

    @BindView(R.id.fd_content)
    ViewGroup content;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    public FDialog fillContent(boolean b) {
        scrollView.setFillViewport(b);
        return this;
    }

    public FDialog setMinimumHeight(int setMinimumHeight) {
        scrollView.setMinimumHeight(setMinimumHeight);
        return this;
    }

    private View.OnClickListener canfirm_click;
    private View.OnClickListener cannel_click;

    private DialogInterface.OnDismissListener dismissListener;

    public FDialog setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
        return this;
    }

    private Context context;

    public Context getContext() {
        return context;
    }

    public FDialog(Context context) {
        this.context = context;
        view = View.inflate(context, R.layout.float_dialog, null);
        LinearLayout linearLayout = new LinearLayout(context) {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    if (dismissListener != null) {
                        dismissListener.onDismiss(null);
                    }
                    dissmis();
                    return true;
                }
                return super.dispatchKeyEvent(event);
            }
        };
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        linearLayout.addView(view);
        ButterKnife.bind(this, view);

        floatWindow = new FloatWindow.FloatWindowBuilder()
                .id("FDialog-" + System.currentTimeMillis())
                .move(false)
                .with(linearLayout)
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

    public void dissmis() {
        floatWindow.close();
    }

    public void show() {
        floatWindow.add();
    }

    @OnClick(R.id.fd_sure)
    public void canfirm(View view) {
        dissmis();
        if (canfirm_click != null) {
            canfirm_click.onClick(view);
        }
    }

    public FDialog addView(View view) {
        content.addView(view);
        return this;
    }

    @OnClick(R.id.fd_cannel)
    public void cannel(View view) {
        dissmis();
        if (cannel_click != null) {
            cannel_click.onClick(view);
        }
    }


    public FDialog setItems(CharSequence[] item, OnItemClickListener listener) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < item.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(-1, SizeUtils.dp2px(55)));
            textView.setText(item[i]);
            textView.setTextSize(17);
            textView.setTextColor(Color.BLACK);
            textView.setPadding(SizeUtils.dp2px(16), 0, 0, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setBackgroundResource(R.drawable.click_item);
            int finalI = i;
            textView.setOnClickListener(v -> {
                dissmis();
                if (listener != null) {
                    listener.select(finalI);
                }
            });
            linearLayout.addView(textView);
        }
        addView(linearLayout);
        return this;
    }

    public FDialog setTitle(String title) {
        this.title.setText(title);
        this.title.setVisibility(title.equals("") ? View.GONE : View.VISIBLE);
        return this;
    }

    public FDialog setMessage(String message) {
        this.message.setVisibility(message.equals("") ? View.GONE : View.VISIBLE);
        this.message.setText(message);
        return this;
    }

    public FDialog setMessage(CharSequence message) {
        if (message == null) {
            setMessage("");
        } else {
            setMessage(message.toString());
        }
        return this;
    }

    public FDialog setCannel(String text, View.OnClickListener clickListener) {
        this.cannel.setVisibility(text.equals("") ? View.GONE : View.VISIBLE);
        this.cannel.setText(text);
        this.cannel_click = clickListener;
        return this;
    }

    public FDialog setCanfirm(String text, View.OnClickListener clickListener) {
        this.canfirm.setVisibility(text.equals("") ? View.GONE : View.VISIBLE);
        this.canfirm.setText(text);
        this.canfirm_click = clickListener;
        return this;
    }
}
