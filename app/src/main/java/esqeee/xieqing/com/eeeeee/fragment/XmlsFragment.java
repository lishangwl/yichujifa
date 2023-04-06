package esqeee.xieqing.com.eeeeee.fragment;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

import static esqeee.xieqing.com.eeeeee.action.ActionHelper.isRootWorkDir;

public class XmlsFragment extends BaseFragment{
    @BindView(R.id.searchView) SearchView searchView;
    @BindView(R.id.recylerView) RecyclerView recyclerView;
    @BindView(R.id.swipe) SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView.Adapter adapter;



    @Override
    public View getContentView(LayoutInflater inflater) {
        View in = inflater.inflate(R.layout.fragment_apps,null);
        return in;
    }



    @Override
    protected void onFragmentInit() {
        searchView.setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(getBaseActivity()));
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = null;
                switch (viewType){
                    case 0:
                        itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_group,parent,false);
                        break;
                    case 1:
                        itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item3,parent,false);
                        break;
                }
                return  new RecyclerView.ViewHolder(itemView){};
            }

            @Override
            public int getItemViewType(int position) {
                if (!getData(position).isDirectory()){
                    return 1;
                }else {
                    return 0;
                }
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                File dir = getData(position);
                if (position == 0 && !isRootWorkDir(currentDir) && holder.itemView.findViewById(R.id.group_name)!=null){
                    ((TextView)holder.itemView.findViewById(R.id.group_name)).setText(".../返回上一级");
                    ThemeManager.attachTheme((ImageView)holder.itemView.findViewById(R.id.group_icon));
                    holder.itemView.setOnClickListener(v->{
                        holder.itemView.postDelayed(()->{
                            currentDir = (dir.getParentFile());
                            refresh();
                        },300);
                    });
                    return;
                }

                View convertView = holder.itemView;
                if (getItemViewType(position) == 0){
                    ((TextView)convertView.findViewById(R.id.group_name)).setText(dir.getName());
                    ThemeManager.attachTheme((ImageView)holder.itemView.findViewById(R.id.group_icon));
                    convertView.setOnClickListener(v->{
                        convertView.postDelayed(()->{
                            currentDir = dir;
                            refresh();
                        },300);
                    });
                }else{
                    ((ImageView)holder.itemView.findViewById(R.id.image)).setImageResource(R.drawable.jb_256);
                    TextView tilte = convertView.findViewById(R.id.title);
                    TextView creatTime = convertView.findViewById(R.id.creatTime);
                    convertView.findViewById(R.id.item_run).setVisibility(View.GONE);
                    convertView.findViewById(R.id.item_edit).setVisibility(View.GONE);
                    convertView.findViewById(R.id.item_more).setVisibility(View.GONE);

                    tilte.setText(FileUtils.getFileNameNoExtension(dir));
                    creatTime.setText(FileUtils.getFileSize(dir));

                    convertView.setOnClickListener(v->{
                        convertView.postDelayed(()->{
                            if (onActionSelectedListener!=null){
                                onActionSelectedListener.select(getData(holder.getAdapterPosition()));
                            }
                        },300);
                    });
                }
            }

            @Override
            public int getItemCount() {
                return ycfList.size();
            }
        };
        recyclerView.setAdapter(adapter);
        refresh();
    }

    private List<File> ycfList = new ArrayList<>();
    private File currentDir = ActionHelper.workSpaceDir;

    public File getData(int position){
        return ycfList.get(position);
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        ycfList.clear();
        adapter.notifyDataSetChanged();
        new Thread(() -> {
                if (!isRootWorkDir(currentDir)) {
                    ycfList.add(currentDir);
                }
                ycfList.addAll(ActionHelper.searchAllXmlAndDirectory(currentDir));
                ThreadUtils.runOnUiThread(()->{
                    //RuntimeLog.e(ycfList);
                    swipeRefreshLayout.setRefreshing(false);
                    recyclerView.getAdapter().notifyDataSetChanged();
                });
        }).start();
    }

    private OnActionSelectedListener onActionSelectedListener;

    public XmlsFragment setOnActionSelectedListener(OnActionSelectedListener onActionSelectedListener) {
        this.onActionSelectedListener = onActionSelectedListener;
        return this;
    }

    public interface OnActionSelectedListener{
        void select(File action);
    }
}
