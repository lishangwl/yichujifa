package com.yicu.yichujifa.ui.adapter.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
/**
 * @author: WelliJohn
 * @time: 2018/8/3-15:32
 * @email: wellijohn1991@gmail.com
 * @desc:
 */
public abstract class BaseTreeRVAdapter<M extends Tree,H extends RecyclerView.ViewHolder> extends BaseRVAdapterV2<RecyclerView.ViewHolder, M> {

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            getLevel(position, mDatas, new PosBean());
        } catch (StopMsgException ex) {
            int type = Integer.parseInt(ex.getMessage());
            M tree = ex.getTree();
            onBindViewHolder(type, tree, (H)holder);
        }
    }


    @Override
    public int getItemViewType(int position) {
        try {
            getLevel(position, mDatas, new PosBean());
        } catch (StopMsgException ex) {
            return Integer.parseInt(ex.getMessage());
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return getTotal(mDatas);
    }

    public abstract void onBindViewHolder(int type, M tree,H holder);

    private Integer getTotal(List<M> paramDatas) {
        int total = 0;
        for (int i = 0, size = paramDatas == null ? 0 : paramDatas.size(); i < size; i++) {
            M tree = paramDatas.get(i);
            if (tree.isExpand()) {
                total++;
                total = getTotal(tree.getChilds()) + total;
            } else {
                total++;
            }
        }
        return total;
    }


    private void getLevel(int position, List<M> paramDatas, PosBean posBean) throws StopMsgException {
        if (paramDatas == null) return;
        for (M tree : paramDatas) {
            posBean.setIndex(posBean.getIndex() + 1);
            if (position + 1 == posBean.getIndex())
                throw new StopMsgException(String.valueOf(tree.getLevel())).setTree(tree);
            if (tree.isExpand()) {
                getLevel(position, tree.getChilds(), posBean);
            }
        }
    }


    private static class PosBean {
        int index = 0;

        int getIndex() {
            return index;
        }

        void setIndex(int index) {
            this.index = index;
        }
    }
}