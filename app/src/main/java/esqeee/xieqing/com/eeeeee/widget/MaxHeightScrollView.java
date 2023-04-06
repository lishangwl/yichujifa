package esqeee.xieqing.com.eeeeee.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

public class MaxHeightScrollView extends NestedScrollView {
    public MaxHeightScrollView(@NonNull Context context) {
        super(context);
    }
    public MaxHeightScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
    }
    public MaxHeightScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            //heightMeasureSpec = MeasureSpec.makeMeasureSpec(SizeUtils.dp2px(200), MeasureSpec.AT_MOST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //重新计算控件高、宽
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
