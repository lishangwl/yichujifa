package esqeee.xieqing.com.eeeeee.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyViewHolder;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.listener.SwipeDontScollRecylerListener;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class AppsFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    @BindView(R.id.searchView) SearchView searchView;
    @BindView(R.id.recylerView) RecyclerView recyclerView;
    @BindView( R.id.swipe) SwipeRefreshLayout swipeRefreshLayout;

    private List<AppUtils.AppInfo> appInfos = new ArrayList<>();
    private List<AppUtils.AppInfo> appInfos_search = new ArrayList<>();
    private RecyclerView.Adapter adapter;



    @Override
    public View getContentView(LayoutInflater inflater) {
        View in = inflater.inflate(R.layout.fragment_apps,null);
        return in;
    }



    @Override
    protected void onFragmentInit() {
        //refresh();
        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        searchView.setOnQueryTextListener(this);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(getBaseActivity()));
        recyclerView.setOnTouchListener(new SwipeDontScollRecylerListener(swipeRefreshLayout));
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyViewHolder(View.inflate(getBaseActivity(),R.layout.list_item,null),true);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                View view=holder.itemView;
                try {
                    AppUtils.AppInfo info = searchString.equals("")?appInfos.get(position):appInfos_search.get(position);
                    ((TextView)view.findViewById(R.id.title)).setText(info.getName());
                    ((ImageView)view.findViewById(R.id.image)).setImageDrawable(info.getIcon());
                    ((TextView)view.findViewById(R.id.creatTime)).setText(info.getPackageName());

                    view.setOnClickListener(view1 -> {
                        if (onAppsSelectedListener!=null){
                            onAppsSelectedListener.select(info);
                        }
                    });
                }catch (Exception e){
                    RuntimeLog.e("appFragment onBindViewHolder:"+e.getMessage());
                }
            }

            @Override
            public int getItemCount() {
                return searchString.equals("")?appInfos.size():appInfos_search.size();
            }
        };
        recyclerView.setAdapter(adapter);
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerView!=null){
            if (recyclerView.getChildCount() == 0){
                refresh();
            }
        }
    }

    private List<AppUtils.AppInfo> filterSystemApp(List<AppUtils.AppInfo> apps){
        List<AppUtils.AppInfo> user_apps = new ArrayList<>();
        for (int i = 0;i<apps.size();i++){
            if (apps.get(i).isSystem()){
                continue;
            }
            user_apps.add(apps.get(i));
        }
        apps.clear();
        apps = null;
        return user_apps;
    }

    private void refresh() {
        try {
            swipeRefreshLayout.setRefreshing(true);
            appInfos.clear();
            adapter.notifyDataSetChanged();
            new Thread(() -> {
                try {
                    appInfos.addAll(AppUtils.getAppsInfo());
                }catch (ArrayIndexOutOfBoundsException a){
                    RuntimeLog.e(a.getMessage());
                }
                ThreadUtils.runOnUiThread(()->{
                    swipeRefreshLayout.setRefreshing(false);
                    recyclerView.getAdapter().notifyDataSetChanged();
                });
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private String searchString = "";
    private void search(String string) {
        searchString = string;
        appInfos_search.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
        for (int i=0;i<appInfos.size();i++){
            if (appInfos.get(i).getName().contains(string)||appInfos.get(i).getName().toUpperCase().contains(string.toUpperCase())){
                appInfos_search.add(appInfos.get(i));
            }
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            search(newText);
        }catch (Exception e){
            RuntimeLog.e("appSearch:"+e.toString());
        }
        return false;
    }

    private OnAppsSelectedListener onAppsSelectedListener;

    public void setOnAppsSelectedListener(OnAppsSelectedListener onAppsSelectedListener) {
        this.onAppsSelectedListener = onAppsSelectedListener;
    }

    public interface OnAppsSelectedListener{
        void select(AppUtils.AppInfo appInfo);
    }
}
