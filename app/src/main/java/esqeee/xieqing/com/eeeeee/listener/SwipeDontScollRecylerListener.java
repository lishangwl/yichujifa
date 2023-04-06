package esqeee.xieqing.com.eeeeee.listener;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.MotionEvent;
import android.view.View;

public class SwipeDontScollRecylerListener implements View.OnTouchListener {
    private SwipeRefreshLayout swipeRefreshLayout;

    public SwipeDontScollRecylerListener(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return swipeRefreshLayout.isRefreshing();
    }
}
