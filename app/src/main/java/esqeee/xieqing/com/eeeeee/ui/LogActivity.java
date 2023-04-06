package esqeee.xieqing.com.eeeeee.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

import com.xieqing.codeutils.util.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.Log;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class LogActivity extends BaseActivity {
    @BindView(R.id.recylerView)
    RecyclerView recylerView;

    private SearchView searchView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Handler timer = new Handler();
    Runnable timerRunnable = new Runnable(){
        @Override
        public void run() {
            timer.removeCallbacks(this);
            try{
                refresh();
            }catch (Exception e){
                e.printStackTrace();
            }
            timer.postDelayed(this,200);
        }
    };

    private void refresh() {
        int oldSize = logs.size();
        List<esqeee.xieqing.com.eeeeee.bean.Log> log = RuntimeLog.getLogs();
        synchronized (log) {
            final int size = log.size();
            if (size == 0) {
                return;
            }
            if (oldSize >= size) {
                return;
            }
            if (oldSize == 0) {
                logs.addAll(log);
            } else {
                for (int i = oldSize; i < size; i++) {
                    logs.add(log.get(i));
                    if (isSearch()){
                        if (log.get(i).time.contains(searchText) || log.get(i).content.toString().contains(searchText)){
                            logs_search.add(log.get(i));
                        }
                    }
                }
            }
            try {
                recylerView.getAdapter().notifyItemRangeInserted(oldSize, size - 1);
                recylerView.scrollToPosition(size - 1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    List<esqeee.xieqing.com.eeeeee.bean.Log> logs = new ArrayList<>();
    List<esqeee.xieqing.com.eeeeee.bean.Log> logs_search = new ArrayList<>();

    @Override
    public int getContentLayout() {
        return R.layout.activity_log;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(view ->{
                finish();
            });
            timer.post(timerRunnable);
            recylerView.setLayoutManager(new MyLinearLayoutManager(this));
            recylerView.setAdapter(new RecyclerView.Adapter() {
                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new RecyclerView.ViewHolder(new TextView(LogActivity.this)) {
                        @Override
                        public String toString() {
                            return super.toString();
                        }
                    };
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                    esqeee.xieqing.com.eeeeee.bean.Log log = isSearch()?logs_search.get(position):logs.get(position);
                    ((TextView)holder.itemView).setText("\t"+log.time+" " +log.content);
                    ((TextView)holder.itemView).setTextSize(13);
                    switch (log.level){
                        case WARN:
                            ((TextView)holder.itemView).setTextColor(Color.parseColor("#FFD39B"));
                            break;
                        case VERBOSE:
                            ((TextView)holder.itemView).setTextColor(Color.parseColor("#A9A9A9"));
                            break;
                        case DEBUG:
                            ((TextView)holder.itemView).setTextColor(Color.parseColor("#000000"));
                            break;
                        case ERROR:
                            ((TextView)holder.itemView).setTextColor(Color.RED);
                            break;
                    }
                }

                @Override
                public int getItemCount() {
                    return isSearch()?logs_search.size():logs.size();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outPersistentState.clear();
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private String searchText = "";

    private boolean isSearch(){
        return !searchText.equals("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_log,menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (searchText.equals(newText)){
                            //500毫秒后没有输入了
                            fillter();
                        }
                    }
                },300);
                return false;
            }
        });
        return true;
    }

    private void fillter() {
        new Thread(()->{
            logs_search.clear();
            for (int i = 0 ;i<logs.size();i++){
                try{
                    Log log = logs.get(i);
                    if (log.time.contains(searchText) || log.content.toString().contains(searchText)){
                        logs_search.add(log);
                    }
                }catch (Exception e){}
            }
            runOnUiThread(()->{
                recylerView.getAdapter().notifyDataSetChanged();
            });
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_clear:
                RuntimeLog.clear();
                FileUtils.delete(Environment.getExternalStorageDirectory().getPath()+"/com.yichujifa.log");
                logs.clear();
                recylerView.getAdapter().notifyDataSetChanged();
                //System.gc();
                break;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.removeCallbacks(timerRunnable);
    }
}
