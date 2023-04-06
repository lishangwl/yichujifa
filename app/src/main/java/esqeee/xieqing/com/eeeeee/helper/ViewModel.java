package esqeee.xieqing.com.eeeeee.helper;

import android.content.Context;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewModel {
    private View view;

    public static ViewModel from(Context context, @LayoutRes int layoutId, ViewGroup viewGroup) {
        return new ViewModel(View.inflate(context, layoutId, viewGroup));
    }

    private ViewModel(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }


    public String getText(@IdRes int viewId) {
        View textView = view.findViewById(viewId);
        if (textView instanceof TextView) {
            if (textView != null) {
                return ((TextView) textView).getText().toString();
            }
        }
        return "";
    }

    public ViewModel setText(@IdRes int viewId, String text) {
        View textView = view.findViewById(viewId);
        if (textView instanceof TextView) {
            if (textView != null) {
                ((TextView) textView).setText(text);
            }
        }
        return this;
    }

    public int getSpinnerSelected(@IdRes int viewId) {
        View spinnerView = view.findViewById(viewId);
        if (spinnerView instanceof Spinner) {
            if (spinnerView != null) {
                return ((Spinner) spinnerView).getSelectedItemPosition();
            }
        }
        return 0;
    }

    public ViewModel onClick(@IdRes int viewId, View.OnClickListener listener) {
        View view = this.view.findViewById(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    public ViewModel onClicks(View.OnClickListener listener, @IdRes int... viewIds) {
        for (int i = 0; i < viewIds.length; i++) {
            View view = this.view.findViewById(viewIds[i]);
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
        return this;
    }

    public ViewModel addView(@IdRes int viewId, View child) {
        View groupView = view.findViewById(viewId);
        if (groupView instanceof ViewGroup) {
            if (groupView != null) {
                ((ViewGroup) groupView).addView(child);
            }
        }
        return this;
    }
}
