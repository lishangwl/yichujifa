//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thl.filechooser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filechooser.R;

import java.util.ArrayList;

public class SdCardAdapter extends BaseAdapter {
    ArrayList<String> arrayList = new ArrayList();
    Context context;
    public int itemViewWidth;

    public SdCardAdapter(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public int getCount() {
        return this.arrayList.size();
    }

    public Object getItem(int position) {
        return this.arrayList.get(position);
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public int getItemViewWidth() {
        if (this.itemViewWidth == 0) {
            this.itemViewWidth = 380;
        }

        return this.itemViewWidth;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(this.context, R.layout.item_file_path, (ViewGroup)null);
        TextView textView = (TextView)convertView.findViewById(R.id.fileName);
        View line = convertView.findViewById(R.id.divider);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.fileIcon);
        imageView.getLayoutParams().width = this.dp2px(26.0F);
        imageView.getLayoutParams().height = this.dp2px(30.0F);
        if (position == 0) {
            imageView.setImageResource(R.drawable.phone);
        } else {
            imageView.setImageResource(R.drawable.sdcard);
        }

        if (this.arrayList != null && this.arrayList.size() > 0 && position != this.arrayList.size() - 1) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
        }

        textView.setText((CharSequence)this.arrayList.get(position));
        convertView.measure(-2, -2);
        this.itemViewWidth = convertView.getMeasuredWidth();
        return convertView;
    }

    public int dp2px(float dpValue) {
        float scale = this.context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }
}
