package com.yicu.yichujifa.LayoutHierarchy.widget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import com.xieqing.codeutils.util.Utils;
import com.yicu.yichujifa.GlobalContext;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.widget.FDialog;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Stardust on 2017/3/10.
 */

public class NodeInfoView extends RecyclerView {

    private Map<String,CharSequence> mData = new HashMap<>();
    public NodeInfoView(Context context) {
        super(context);
        init();
    }

    public NodeInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NodeInfoView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private String[] keys = new String[]{
            "id",
            "矩阵",
            "说明",
            "类名",
            "包名",
            "内容",
            "索引",
            "绘制顺序",
            "是否选择",
            "是否选中",
            "是否可以点击",
            "是否可以长按",
            "是否可以滑动",
            "是否可以编辑",
            "是否可用",
            "是否可以获取焦点",
            "是否已获取焦点"
    };

    public void setNodeInfo(AccessibilityNodeInfo nodeInfo,int indexInParent) {
        mData.clear();
        if (nodeInfo == null){
            return;
        }
        String packageName = nodeInfo.getPackageName().toString();
        String id = nodeInfo.getViewIdResourceName();
        if (id!=null && !id.contains(":id/")){
            id = packageName+":id/"+id;
        }
        mData.put("id",id);
        Rect bounds = new Rect();
        nodeInfo.getBoundsInScreen(bounds);
        mData.put("矩阵",bounds.toShortString());
        mData.put("说明",nodeInfo.getContentDescription());
        mData.put("类名",nodeInfo.getClassName());
        mData.put("包名",packageName);
        mData.put("内容",nodeInfo.getText());
        mData.put("索引",String.valueOf(indexInParent));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mData.put("绘制顺序",String.valueOf(nodeInfo.getDrawingOrder()));
        }else{
            mData.put("绘制顺序",String.valueOf(0));
        }
        mData.put("是否选择",getBoolean(nodeInfo.isChecked()));
        mData.put("是否选中",getBoolean(nodeInfo.isSelected()));
        mData.put("是否可以点击",getBoolean(nodeInfo.isClickable()));
        mData.put("是否可以长按",getBoolean(nodeInfo.isLongClickable()));
        mData.put("是否可以滑动",getBoolean(nodeInfo.isScrollable()));
        mData.put("是否可以编辑",getBoolean(nodeInfo.isEditable()));
        mData.put("是否可用",getBoolean(nodeInfo.isEnabled()));
        mData.put("是否可以获取焦点",getBoolean(nodeInfo.isFocusable()));
        mData.put("是否已获取焦点",getBoolean(nodeInfo.isFocused()));
        getAdapter().notifyDataSetChanged();
    }
    public void setNodeInfo(NodeInfo nodeInfo,int indexInParent) {
        mData.clear();
        if (nodeInfo == null){
            return;
        }
        String packageName = nodeInfo.packageName;
        String id = nodeInfo.id;
        if (id!=null && !id.contains(":id/")){
            id = packageName+":id/"+id;
        }
        mData.put("id",id);
        mData.put("矩阵",nodeInfo.getBoundsInScreen().toShortString());
        mData.put("说明",nodeInfo.desc);
        mData.put("类名",nodeInfo.className);
        mData.put("包名",nodeInfo.packageName);
        mData.put("内容",nodeInfo.text);
        mData.put("索引",String.valueOf(nodeInfo.indexInParent));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mData.put("绘制顺序",String.valueOf(nodeInfo.drawingOrder));
        }else{
            mData.put("绘制顺序",String.valueOf(0));
        }
        mData.put("是否选择",getBoolean(nodeInfo.checked));
        mData.put("是否选中",getBoolean(nodeInfo.selected));
        mData.put("是否可以点击",getBoolean(nodeInfo.clickable));
        mData.put("是否可以长按",getBoolean(nodeInfo.longClickable));
        mData.put("是否可以滑动",getBoolean(nodeInfo.scrollable));
        mData.put("是否可以编辑",getBoolean(nodeInfo.editable));
        mData.put("是否可用",getBoolean(nodeInfo.enabled));
        mData.put("是否可以获取焦点",getBoolean(nodeInfo.focusable));
        mData.put("是否已获取焦点",getBoolean(nodeInfo.focused));
        getAdapter().notifyDataSetChanged();
    }

    private CharSequence getBoolean(boolean checked) {
        return checked?"真":"假";
    }

    private void init() {
        setAdapter(new Adapter());
        setLayoutManager(new LinearLayoutManager(getContext()));
        addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(0x1e000000)
                .size(2)
                .build());
    }


    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        final int VIEW_TYPE_HEADER = 0;
        final int VIEW_TYPE_ITEM = 1;


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.node_info_view_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.attrName.setText(keys[position]);
            holder.attrValue.setText(mData.get(keys[position]));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.name)
        TextView attrName;

        @BindView(R.id.value)
        TextView attrValue;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Optional
        @OnClick(R.id.item)
        void onItemClick() {
            int pos = getAdapterPosition();
            if (pos < 0 || pos >= mData.size())
                return;
            new FDialog(GlobalContext.getContext())
                    .setTitle(keys[pos])
                    .setMessage(mData.get(keys[pos]))
                    .setCanfirm("复制",(v)->{
                        ((ClipboardManager) Utils.getApp().getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("一触即发--剪贴板",mData.get(keys[pos])));
                    }).setCannel("确定",null).show();

        }
    }

}