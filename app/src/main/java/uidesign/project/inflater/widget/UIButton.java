package uidesign.project.inflater.widget;

import android.content.Context;
import android.util.AttributeSet;

import uidesign.project.model.Attr;

public class UIButton extends androidx.appcompat.widget.AppCompatButton {
    public UIButton(Context context) {
        super(context);
    }
    public UIButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public UIButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Attr attr;
    public UIButton(Context context, Attr attr){
        super(context);
        this.attr = attr;
    }
}
