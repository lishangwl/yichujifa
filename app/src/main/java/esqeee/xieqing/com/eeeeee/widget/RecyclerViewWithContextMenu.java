package esqeee.xieqing.com.eeeeee.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;

public class RecyclerViewWithContextMenu extends RecyclerView {
    private final static String TAG = "RVWCM";
 
    private RecyclerViewContextInfo mContextInfo = new RecyclerViewContextInfo();
 
    public RecyclerViewWithContextMenu(Context context) {
        super(context);
    }
 
    public RecyclerViewWithContextMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
 
    public RecyclerViewWithContextMenu(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    @Override
    public boolean showContextMenuForChild(View originalView, float x, float y) {
        LayoutManager layoutManager = getLayoutManager();
        if(layoutManager != null) {
            int position = layoutManager.getPosition(originalView);
            Log.d(TAG,"showContextMenuForChild position = " + position);
            mContextInfo.mPosition = position;
        }
 
        return super.showContextMenuForChild(originalView, x, y);
    }
 
    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return mContextInfo;
    }
 
    public static class RecyclerViewContextInfo implements ContextMenu.ContextMenuInfo {
        private int mPosition = -1;
 
        public int getPosition() {
            return mPosition;
        }
    }
}