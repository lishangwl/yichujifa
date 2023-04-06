package esqeee.xieqing.com.eeeeee.adapter.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.io.File;


public abstract class ListViewHolder extends RecyclerView.ViewHolder {
    public ListViewHolder(View itemView) {
        super(itemView);
    }
    public abstract void bindView(File ycfProject);
}
