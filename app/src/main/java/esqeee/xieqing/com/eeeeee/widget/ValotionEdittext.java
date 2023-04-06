package esqeee.xieqing.com.eeeeee.widget;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class ValotionEdittext extends androidx.appcompat.widget.AppCompatEditText {
    public ValotionEdittext(Context context) {
        super(context);
        init();
    }
    public ValotionEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public ValotionEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        clearFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setLetterSpacing(0.05f);
        }
        //setMaxHeight(SizeUtils.dp2px(250));
    }
    public void bindFoucsView(View viewById) {
        viewById.setOnClickListener(v->{
            requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(ValotionEdittext.this,0);
        });
    }

    private TextWatcher textWatcher;
    public void bindChangeString(JSONBean param, String name) {
        if (textWatcher!=null){
            removeTextChangedListener(textWatcher);
        }
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                param.put(name,editable.toString());
            }
        };
        addTextChangedListener(textWatcher);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
    }
}
