package esqeee.xieqing.com.eeeeee.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.io.File;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.adapter.ViewHolder.ActionDirectoryViewHolder;
import esqeee.xieqing.com.eeeeee.adapter.ViewHolder.ActionListViewHolder;
import esqeee.xieqing.com.eeeeee.adapter.ViewHolder.ListViewHolder;
import esqeee.xieqing.com.eeeeee.adapter.ViewHolder.XmlListViewHolder;

public class ActionListAdapter extends RecyclerView.Adapter {
    private Context context;
    private boolean isInRoot = true;

    public void setInRoot(boolean inRoot) {
        isInRoot = inRoot;
    }

    public Context getContext() {
        return context;
    }

    private List<File> ycfList;
    private DirectoryInto directoryInto;

    public void setDirectoryInto(DirectoryInto directoryInto) {
        this.directoryInto = directoryInto;
    }

    public ActionListAdapter(Context context, List<File> ycfList){
        this.ycfList = ycfList;
        this.context = context;
    }

    public void setYcfList(List<File> ycfList) {
        this.ycfList = ycfList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                return new ActionDirectoryViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_group,parent,false),this);
            case 1:
                return new ActionListViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item3,parent,false),this);
            case 2:
                return new XmlListViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item4,parent,false),this);
        }
        return null;
    }

    public void onDirectoryClick(File ycfDirectory){
         if (directoryInto!=null){
             directoryInto.into(ycfDirectory);
         }
    }

    @Override
    public int getItemViewType(int position) {
        if (ActionHelper.isActionFile(getData(position))){
            return 1;
        }else if (ActionHelper.isXmlFile(getData(position))){
            return 2;
        }else{
            return 0;
        }
    }

    public File getData(int position){
        return ycfList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        File dir = getData(position);
        if (position == 0 && !isInRoot && holder instanceof ActionDirectoryViewHolder){
            ((TextView)holder.itemView.findViewById(R.id.group_name)).setText(".../返回上一级");
            ThemeManager.attachTheme((ImageView)holder.itemView.findViewById(R.id.group_icon));
            holder.itemView.setOnLongClickListener(null);
            holder.itemView.setOnClickListener(v->{
                if (dir.getParent()==null){
                    return;
                }
                onDirectoryClick(new File(dir.getParent()));
            });
            return;
        }
        ((ListViewHolder)holder).bindView(dir);
    }

    @Override
    public int getItemCount() {
        return ycfList.size();
    }


    public interface DirectoryInto{
       void into(File directory);
    }
}
