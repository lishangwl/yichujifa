package uidesign.project.inflater.widget;

import android.content.Context;
import android.util.AttributeSet;

import uidesign.project.model.Attr;

public class UIEditText extends androidx.appcompat.widget.AppCompatEditText {
    public UIEditText(Context context) {
        super(context);
    }
    public UIEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public UIEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Attr attr;
    public UIEditText(Context context, Attr attr){
        super(context);
        this.attr = attr;
    }
}
