//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thl.filechooser;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.example.filechooser.R.id;

import java.io.File;
import java.util.ArrayList;

public class CurrentFileAdapter extends CommonAdapter<File> {
    public CurrentFileAdapter(Context context, ArrayList<File> dataList, int resId) {
        super(context, dataList, resId);
    }

    public void bindView(ViewHolder holder, File data, int position) {
        TextView textView = (TextView)holder.itemView.findViewById(id.fileName);
        textView.setText(data.getName());
        if (position == this.dataList.size() - 1) {
            holder.itemView.findViewById(id.icon).setVisibility(View.GONE);
        } else {
            holder.itemView.findViewById(id.icon).setVisibility(View.VISIBLE);
        }

    }
}
