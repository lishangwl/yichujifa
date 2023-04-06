package esqeee.xieqing.com.eeeeee.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.xieqing.codeutils.util.ThreadUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.action.ActionFinished;
import esqeee.xieqing.com.eeeeee.action.ActionStarted;
import esqeee.xieqing.com.eeeeee.adapter.MyTimerActionAdapter;
import esqeee.xieqing.com.eeeeee.listener.SwipeDontScollRecylerListener;
import esqeee.xieqing.com.eeeeee.manager.AppActivityCheckManager;
import esqeee.xieqing.com.eeeeee.manager.SystemActionManager;
import esqeee.xieqing.com.eeeeee.sql.model.AutoDo;
import esqeee.xieqing.com.eeeeee.manager.CheckTextManager;
import esqeee.xieqing.com.eeeeee.manager.TimerTaskManager;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class TimerActionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyTimerActionAdapter actionAdapter;
    private List<AutoDo> actions = new ArrayList<>();
    @Override
    public View getContentView(LayoutInflater inflater) {
        swipeRefreshLayout = new SwipeRefreshLayout(inflater.getContext());
        recyclerView = new RecyclerView(inflater.getContext());
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.addView(recyclerView);

        recyclerView.setOnTouchListener(new SwipeDontScollRecylerListener(swipeRefreshLayout));
        return swipeRefreshLayout;
    }

    @Override
    protected void onFragmentInit() {
        recyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
        actionAdapter = new MyTimerActionAdapter(getActivity(),actions);
        recyclerView.setAdapter(actionAdapter);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onRefresh() {
        refresh();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (actions.size() == 0){
            onRefresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ActionFinished actionFinished){
        refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ActionStarted actionStarted){
        refresh();
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                actions.clear();
                actions.addAll(TimerTaskManager.getManager().queryList());
                actions.addAll(CheckTextManager.getManager().queryList());
                actions.addAll(CheckTextManager.getManager().queryNotificationList());
                actions.addAll(AppActivityCheckManager.getManager().queryList());
                actions.addAll(SystemActionManager.getManager().queryList());
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        actionAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
