package esqeee.xieqing.com.eeeeee.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import esqeee.xieqing.com.eeeeee.R;

/**
 * 简单适配器
 * 
 * @Project App_View
 * @Package com.android.view.tree
 * @author chenlin
 * @version 1.0
 * @Date 2014年6月4日
 * @param <T>
 */
public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {

    public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
    }


    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_tree_item, parent, false);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        // 如果图标不存在，就隐藏
        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }
        viewHolder.label.setText(node.getName());
        return convertView;
    }

    static class ViewHolder {
        ImageView icon;
        TextView label;

        public static ViewHolder getHolder(View view) {
            Object tag = view.getTag();
            if (tag != null) {
                return (ViewHolder) tag;
            } else {
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.icon = (ImageView) view.findViewById(R.id.id_treenode_icon);
                viewHolder.label = (TextView) view.findViewById(R.id.id_treenode_label);
                view.setTag(viewHolder);
                return viewHolder;
            }
        }
    }

}