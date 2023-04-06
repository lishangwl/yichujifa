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
import esqeee.xieqing.com.eeeeee.action.ActionRun;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.action.ActionStarted;
import esqeee.xieqing.com.eeeeee.adapter.MyRunActionAdapter;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.listener.SwipeDontScollRecylerListener;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class RunActionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyRunActionAdapter actionAdapter;
    private List<ActionRun> runList = new ArrayList<>();
    @Override
    public View getContentView(LayoutInflater inflater) {
        swipeRefreshLayout = new SwipeRefreshLayout(inflater.getContext());
        recyclerView = new RecyclerView(inflater.getContext());
        recyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
        actionAdapter = new MyRunActionAdapter(getActivity(),runList);
        recyclerView.setAdapter(actionAdapter);
        recyclerView.setOnTouchListener(new SwipeDontScollRecylerListener(swipeRefreshLayout));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.addView(recyclerView);
        return swipeRefreshLayout;
    }

    @Override
    protected void onFragmentInit() {

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
        if (runList.size() == 0){
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
        runList.clear();
        actionAdapter.notifyDataSetChanged();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runList.addAll(ActionRunHelper.getAllRunning());
                }catch (Exception e){
                    RuntimeLog.e("refresh run auto error : "+e.getMessage());
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout!=null){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        actionAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
