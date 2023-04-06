package esqeee.xieqing.com.eeeeee.fragment;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.HttpUtils;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.Utils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.listener.SwipeDontScollRecylerListener;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

import static com.xieqing.codeutils.util.ActivityUtils.startActivity;

public class NetAutoFragment extends BaseFragment {

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recylerView_cates)
    RecyclerView recylerView_cates;

    @BindView(R.id.recylerView_items)
    RecyclerView recylerView_items;

    private List<NetAutoGroup> groups = new ArrayList<>();
    private int current_cate_selected = 0;
    @Override
    public View getContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_net_auto,null);
    }

    @Override
    protected void onFragmentInit() {
        recylerView_items.setOnTouchListener(new SwipeDontScollRecylerListener(swipeRefreshLayout));
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        recylerView_cates.setAdapter(new NetAutoCateAdapter());
        recylerView_items.setAdapter(new NetAutoItemAdapter());

        recylerView_items.setLayoutManager(new MyLinearLayoutManager(getBaseActivity()));
        recylerView_cates.setLayoutManager(new MyLinearLayoutManager(getBaseActivity()));
        //onRefresh();
    }

    @Override
    public void onChangTheme() {
        super.onChangTheme();
        recylerView_items.getAdapter().notifyDataSetChanged();
    }

    public void onRefresh(){
        if (!swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(true);
        }
        new Thread(()->{
            groups.clear();
            groups.addAll(fromJson(HttpUtils.Get("http://www.baidu.com/esqeee.xieqing.com.eeeeee/autos.json")));
            ThreadUtils.runOnUiThread(()->{
                try {
                    if (swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    recylerView_cates.getAdapter().notifyDataSetChanged();
                    recylerView_items.getAdapter().notifyDataSetChanged();
                }catch (Exception e){

                }
            });
        }).start();
    }

    private List<NetAutoGroup> fromJson(String json){
        List<NetAutoGroup> baseGroupedItems = new ArrayList<>();
        JSONArrayBean jsonArrayBean = new JSONArrayBean(json);
        for (int i = 0; i<jsonArrayBean.length();i++){
            JSONBean item = jsonArrayBean.getJson(i);
            JSONArrayBean items = item.getArray("items");


            String title = item.getString("title");
            NetAutoItem[] netAutoItems = new NetAutoItem[items.length()];
            NetAutoGroup autoItem = new NetAutoGroup(title,netAutoItems);
            for (int b = 0; b<items.length();b++){
                JSONBean info = items.getJson(b);
                netAutoItems[b] = new NetAutoItem();
                netAutoItems[b].setContent(info.getString("content"));
                netAutoItems[b].setUrl(info.getString("url"));
                netAutoItems[b].setSize(info.getString("size"));
                netAutoItems[b].setTitle(info.getString("title"));
            }
            baseGroupedItems.add(autoItem);
        }
        return baseGroupedItems;
    }

    public void onSelected(int pos){
        current_cate_selected = pos;
        recylerView_items.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (groups.size() == 0){
            onRefresh();
        }
    }

    private class NetAutoCateAdapter extends RecyclerView.Adapter{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(getBaseActivity()).inflate(R.layout.adapter_net_auto_permiery,parent,false)) {};
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((TextView)holder.itemView.findViewById(R.id.tv_group)).setText(groups.get(position).title);
            if (position == current_cate_selected){
                ((TextView)holder.itemView.findViewById(R.id.tv_group)).setTextColor(Color.BLACK);
                ((TextView)holder.itemView.findViewById(R.id.tv_group)).setBackgroundColor(Color.WHITE);
                holder.itemView.setOnClickListener(null);
            }else{
                ((TextView)holder.itemView.findViewById(R.id.tv_group)).setTextColor(Color.GRAY);
                ((TextView)holder.itemView.findViewById(R.id.tv_group)).setBackgroundColor(Color.parseColor("#E4E4E4"));
                holder.itemView.setOnClickListener(v->{
                    onSelected(holder.getAdapterPosition());
                    notifyDataSetChanged();
                });
            }

        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

    }

    private class NetAutoItemAdapter extends RecyclerView.Adapter{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(getBaseActivity()).inflate(R.layout.adapter_net_auto,parent,false)) {};
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            NetAutoItem item = groups.get(current_cate_selected).items[position];
            ((TextView)holder.itemView.findViewById(R.id.auto_title)).setText(item.getTitle());
            ((TextView)holder.itemView.findViewById(R.id.auto_content)).setText(item.getContent());
            ((TextView)holder.itemView.findViewById(R.id.auto_size)).setText(item.getSize());

            Button button = holder.itemView.findViewById(R.id.download);
            if (!ActionHelper.simpleDir.isDirectory()){
                ActionHelper.simpleDir.mkdir();
            }
            ThemeManager.attachTheme(button);
            File file = new File(ActionHelper.simpleDir,item.getTitle()+".ycf");
            if (file.exists()){
                button.setEnabled(true);
                button.setText("打开");
                button.setOnClickListener(v->{
                    if (!PermissionUtils.getAppOps(Utils.getApp())){
                        ToastUtils.showShort("请先开启悬浮窗权限");
                        PermissionUtils.openAps(Utils.getApp());
                        return;
                    }
                    Intent intent = new Intent(button.getContext(),AddActivity.class);
                    intent.putExtra("path",file.getAbsolutePath());
                    startActivity(intent);
                });
            }else{
                button.setEnabled(true);
                button.setText("下载");
                holder.itemView.findViewById(R.id.download).setOnClickListener(v->{
                    ((Button)v).setEnabled(false);
                    ((Button)v).setText("正在下载");
                    new Thread(()->{
                        //ThreadUtils.sleep(3000);
                        try {
                            HttpUtils.Response response = HttpUtils.GetToResponse(item.getUrl());;
                            if (response.code() == 200){
                                FileIOUtils.writeFileFromBytesByStream(file,response.body().bytes());
                                ThreadUtils.runOnUiThread(()->{
                                    button.setEnabled(true);
                                    button.setText("打开");
                                    ToastUtils.showLong("已保存到：使用示例/"+file.getName());
                                    button.setOnClickListener(v2->{
                                        AddActivity.open(getBaseActivity(),file.getAbsolutePath());
                                    });
                                });
                            }else {
                                ThreadUtils.runOnUiThread(()->{
                                    button.setEnabled(true);
                                    button.setText("重新下载");
                                    ToastUtils.showLong("下载失败："+response.message());
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ThreadUtils.runOnUiThread(()->{
                                button.setEnabled(true);
                                button.setText("重新下载");
                                ToastUtils.showLong("下载失败："+e.getMessage());
                            });
                        }

                    }).start();
                });
            }


        }

        @Override
        public int getItemCount() {
            try {
                return groups.get(current_cate_selected).items.length;
            }catch (Exception e){

            }
            return 0;
        }

    }

    public class NetAutoItem{
        private String size;
        private String content;
        private String url;
        private String title;
        public NetAutoItem() {

        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContent() {
            return content;
        }

        public String getSize() {
            return size;
        }

        public String getUrl() {
            return url;
        }
    }


    public class NetAutoGroup {
        private String title;
        private NetAutoItem[] items;
        public NetAutoGroup(String title,NetAutoItem[] items) {
            this.title = title;
            this.items = items;
        }
    }
}
