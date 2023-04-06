package com.yicu.yichujifa.LayoutHierarchy;

import android.graphics.Color;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieqing.codeutils.util.SizeUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.GlobalContext;
import com.yicu.yichujifa.LayoutHierarchy.widget.NodeInfoView;
import com.yicu.yichujifa.ui.adapter.Adapter.BaseTreeRVAdapter;
import com.yicu.yichujifa.ui.adapter.Adapter.Tree;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.helper.RectFloatHelper;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.widget.FDialog;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class ListLayoutHierarchy extends FloatWindow {

    private RecyclerView rootView;
    private NodeAdapter nodeAdapter;
    public ListLayoutHierarchy(){
        rootView = new RecyclerView(GlobalContext.getContext()){
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    close();
                    return true;
                }
                return false;
            }
        };
        nodeAdapter = new NodeAdapter();
        rootView.setAdapter(nodeAdapter);
        rootView.setBackgroundColor(Color.parseColor("#CCFFFFFF"));
        rootView.setLayoutManager(new MyLinearLayoutManager(GlobalContext.getContext()));
        setId("ListLayoutHierarchy");
        setAllowMove(false);
        setParams(new WindowManager.LayoutParams(-1,-1, MyApp.getFloatWindowType(),WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,-3));
        setView(rootView);
    }


    FDialog nodeInfo;
    NodeInfoView nodeInfoView;

    public void setNode(List<Node> data){
        nodeAdapter.setDatas(data);
    }

    @Override
    public void close() {
        super.close();
        NodeHelper.recyle(rootNode);
        RectFloatHelper.getHelper(GlobalContext.getContext()).removeRectView();
    }

    private AccessibilityNodeInfo rootNode;
    @Override
    public void add() {
        super.add();
        rootNode = AccessbilityUtils.getService().getRootNode();
        if (rootNode == null){
            close();
            ToastUtils.showLong("分析屏幕控件失败，请重新切换页面再尝试");
        }else{
            List<Node> nodes = new ArrayList<>();
            nodes.add(new Node(rootNode));
            setNode(nodes);
        }
    }

    private class NodeAdapter extends BaseTreeRVAdapter<Node,NodeHolder>{
        @Override
        public void onBindViewHolder(int type, Node tree, NodeHolder holder) {
            holder.bindView(type,tree);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            NodeHolder holder = null;
            switch (viewType){
                case 0:
                    holder = new ChildNodeHolder(LayoutInflater.from(GlobalContext.getContext()).inflate(R.layout.layout_hierarchy_child,parent,false));
                    break;
                case 1:
                    holder = new ParentNodeHolder(LayoutInflater.from(GlobalContext.getContext()).inflate(R.layout.layout_hierarchy_root,parent,false));
                    break;
            }
            return holder;
        }
    }



    private class ChildNodeHolder extends NodeHolder<Node>{

        public ChildNodeHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(int type, Node node) {
            TextView textView = itemView.findViewById(R.id.name);
            AccessibilityNodeInfo nodeInfo = node.getNode();
            textView.setText(simplifyClassName(nodeInfo.getClassName()));
            itemView.setPadding(SizeUtils.dp2px(8) * node.getLevelNumber(),0,0,0);
            itemView.setOnClickListener(v->{
                RectFloatHelper.getHelper(GlobalContext.getContext()).removeRectView();
                if (nodeInfo != null){
                    Rect rect = new Rect();
                    nodeInfo.getBoundsInScreen(rect);
                    RectFloatHelper.getHelper(GlobalContext.getContext()).showRectView(rect);
                }
            });

            itemView.setOnLongClickListener(v->{
                showInfo(node.getNode(),node.getIndex());
                return true;
            });
        }
    }

    private void showInfo(AccessibilityNodeInfo node, int index) {
        if (nodeInfo == null){
            nodeInfoView = new NodeInfoView(GlobalContext.getContext());
            nodeInfo = new FDialog(GlobalContext.getContext())
                    .setTitle("控件属性(点击复制)")
                    .setCanfirm("",null)
                    .addView(nodeInfoView);
        }
        nodeInfoView.setNodeInfo(node,index);
        nodeInfo.show();
    }

    private class ParentNodeHolder extends NodeHolder<Node>{

        public ParentNodeHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(int type, Node node) {
            itemView.setPadding(SizeUtils.dp2px(8) * node.getLevelNumber(),0,0,0);
            TextView textView = itemView.findViewById(R.id.name);
            ImageView icon = itemView.findViewById(R.id.icon);
            AccessibilityNodeInfo nodeInfo = node.getNode();
            textView.setText(simplifyClassName(nodeInfo.getClassName()));
            icon.setImageResource(!node.isExpand() ? R.mipmap.ic_expland_more : R.mipmap.ic_expland_less);
            itemView.setOnClickListener(v->{
                node.setExpand(!node.isExpand());
                nodeAdapter.notifyDataSetChanged();
                RectFloatHelper.getHelper(GlobalContext.getContext()).removeRectView();
                if (nodeInfo != null){
                    Rect rect = new Rect();
                    nodeInfo.getBoundsInScreen(rect);
                    RectFloatHelper.getHelper(GlobalContext.getContext()).showRectView(rect);
                }
            });

            itemView.setOnLongClickListener(v->{
                showInfo(node.getNode(),node.getIndex());
                return true;
            });
        }
    }
    private abstract class NodeHolder<N extends Tree> extends RecyclerView.ViewHolder{

        public NodeHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindView(int type,N tree);

        public String simplifyClassName(CharSequence className) {
            if (className == null)
                return "null";
            String[] split = className.toString().split("\\.");
            return split[split.length - 1];
        }
    }
}
