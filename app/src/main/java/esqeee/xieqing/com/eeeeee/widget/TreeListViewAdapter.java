package esqeee.xieqing.com.eeeeee.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * 树形结构适配器
 * 
 * @Project App_View
 * @Package com.android.view.tree
 * @author chenlin
 * @version 1.0
 * @Date 2014年6月4日
 * @Note TODO
 */
public abstract class TreeListViewAdapter<T> extends BaseAdapter {
    protected ListView mListView;
    protected Context mContext;
    /** 存储所有可见的Node */
    protected List<Node> mNodes;
    /** 存储所有的Node */
    protected List<Node> mAllNodes;
    protected LayoutInflater mInflater;

    /*********************************************************************************
     * 点击的回调接口
     */
    private OnTreeNodeClickListener onTreeNodeClickListener;

    public interface OnTreeNodeClickListener {
        void onClick(Node node, int position);
    }

    public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener) {
        this.onTreeNodeClickListener = onTreeNodeClickListener;
    }
    /************************************************************************************/

    public TreeListViewAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        // 对所有的Node进行排序
        this.mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
        // 过滤出可见的Node
        this.mNodes = TreeHelper.filterVisibleNode(mAllNodes);

        // 点击item事件
        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击时展开或关闭item
                expandOrCollapse(position);

                //使用回调函数
                if (onTreeNodeClickListener != null) {
                    onTreeNodeClickListener.onClick(mNodes.get(position), position);
                }
            }
        });
    }

    /**
     * 点击时展开或关闭item
     * @param position
     */
    protected void expandOrCollapse(int position) {
        Node node = mNodes.get(position);
        if (node!= null) {
            if (!node.isLeaf()) {
                //表示如果关闭的就打开，如果打开的就关闭
                node.setExpend(!node.isExpend());
                //重新赋值
                mNodes = TreeHelper.filterVisibleNode(mAllNodes);
                //通知视图改变了
                notifyDataSetChanged();
            }

        }
    }

    @Override
    public int getCount() {
        return mNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mNodes.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        // 设置内边距, 层级越大，则离左边的距离越大
        convertView.setPadding(node.getLevel() * 30, 3, 3, 3);

        return convertView;
    }

    public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);

}