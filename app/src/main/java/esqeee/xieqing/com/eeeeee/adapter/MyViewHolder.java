package esqeee.xieqing.com.eeeeee.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public MyViewHolder(View itemView) {
            this(itemView,false);
        }

    public MyViewHolder(View itemView,boolean use) {
        super(itemView);
        if (use){
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            if (layoutParams==null){
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            layoutParams.width = -1;
            layoutParams.height = -2;
            itemView.setLayoutParams(layoutParams);
        }
    }
    }