package com.liyi.flow;


import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.liyi.flow.adapter.BaseFlowAdapter;
import com.liyi.flow.adapter.BaseFlowHolder;
import com.liyi.view.R;

import java.util.ArrayList;


/**
 * 流布局控件
 */
public class FlowView extends ViewGroup {
    /**
     * 默认值
     */
    // 默认流布局横向对齐方式
    private final int DEF_FlOW_HORIZONTAL_ALIGN = FlowConfig.FLOW_HORIZONTAL_ALIGN_LEFT;
    // 默认流布局纵向对齐方式
    private final int DEF_FlOW_VERTICAL_ALIGN = FlowConfig.FLOW_VERTICAL_ALIGN_MIDDLE;
    // 默认流布局的行高
    private final int DEF_FLOW_HEIGHT = FlowConfig.INVALID_VAL;
    // 默认流布局最大显示行数
    private final int DEF_FLOW_MAX_ROW = FlowConfig.INVALID_VAL;
    // 默认流布局的横向间距
    private final int DEF_FlOW_HORIZONTAL_SPACE = 10;
    // 默认流布局的纵向间距
    private final int DEF_FlOW_VERTICAL_SPACE = 10;

    /**
     * 变量
     */
    // 流布局的横向排列方式
    private int mHorizontalAlign;
    // 流布局的纵向排列方式
    private int mVerticalAlign;
    // 流布局的行高
    private float mFlowHeight;
    // 流布局的最大显示行数
    private int mMaxRow;
    // 流布局的 item 之间的横向间距
    private float mHorizontalSpace;
    // 流布局的 item 之间的纵向间距
    private float mVerticalSpace;
    // 记录流布局的行信息 ===> item 的个数、最后一个 item 的序号、行高度
    private ArrayList<float[]> mRowInfoList;

    private int mOldSize;
    private BaseFlowAdapter mAdapter;
    private AdapterObserver mDataSetObserver;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;


    public FlowView(Context context) {
        super(context);
        init(context, null);
    }

    public FlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        initData();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowView);
            if (a != null) {
                mHorizontalAlign = a.getInt(R.styleable.FlowView_flow_horizontalAlign, mHorizontalAlign);
                mVerticalAlign = a.getInt(R.styleable.FlowView_flow_verticalAlign, mVerticalAlign);
                mFlowHeight = a.getDimension(R.styleable.FlowView_flow_height, mFlowHeight);
                mHorizontalSpace = a.getDimension(R.styleable.FlowView_flow_horizontalSpace, mHorizontalSpace);
                mVerticalSpace = a.getDimension(R.styleable.FlowView_flow_verticalSpace, mVerticalSpace);
                mMaxRow = a.getInt(R.styleable.FlowView_flow_maxRow, mMaxRow);
                a.recycle();
            }
        }
    }

    private void initData() {
        mFlowHeight = DEF_FLOW_HEIGHT;
        mMaxRow = DEF_FLOW_MAX_ROW;
        mHorizontalSpace = DEF_FlOW_HORIZONTAL_SPACE;
        mVerticalSpace = DEF_FlOW_VERTICAL_SPACE;
        mHorizontalAlign = DEF_FlOW_HORIZONTAL_ALIGN;
        mVerticalAlign = DEF_FlOW_VERTICAL_ALIGN;
        mRowInfoList = new ArrayList<float[]>();
    }

    public void setAdapter(BaseFlowAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            // 删除已经存在的观察者
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        removeAllViews();
        mAdapter = adapter;
        if (mAdapter != null) {
            mDataSetObserver = new AdapterObserver();
            // 注册观察者
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mOldSize = mAdapter.getItemCount();
            if (mOldSize > 0) {
                addItemViews();
            }
        }
    }

    /**
     * 通知刷新
     */
    private void notifyChanged() {
        if (mAdapter == null) return;
        int newSize = mAdapter.getItemCount();
        if (newSize == 0) {
            if (mOldSize == 0) {
                return;
            } else {
                removeAllViews();
            }
        } else {
            if (mOldSize == 0) {
                addItemViews();
            } else {
                for (int i = 0; i < newSize; i++) {
                    if (mOldSize > i) {
                        View itemView = getChildAt(i);
                        BaseFlowHolder holder = (BaseFlowHolder) itemView.getTag();
                        if (holder != null && mAdapter.getItemViewType(i) == holder.getViewType()) {
                            mAdapter.getView(i, itemView, this);
                        } else {
                            removeViewAt(i);
                            addView(createItemView(i), i);
                        }
                    } else {
                        addView(createItemView(i));
                    }
                }
                // 删除多余的 item
                int diff = mOldSize - newSize;
                for (int i = 0; i < diff; i++) {
                    removeViewAt(newSize + i);
                }
            }
        }
    }

    /**
     * 添加 itemView
     */
    private void addItemViews() {
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            addView(createItemView(i));
        }
    }

    private View createItemView(int position) {
        View itemView = mAdapter.getView(position, null, this);
        addItemClickListener(itemView, position);
        addItemLongClickListener(itemView, position);
        return itemView;
    }

    /**
     * 为 item 添加点击事件
     */
    private void addItemClickListener(View view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position, v);
                }
            }
        });
    }

    /**
     * 为 item 添加长按事件
     */
    private void addItemLongClickListener(View view, final int position) {
        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemLongClickListener != null) {
                    return mItemLongClickListener.onItemLongClick(position, v);
                }
                return false;
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (mAdapter == null || getChildCount() == 0) {
            setMeasuredDimension(measureWidth, heightMode == MeasureSpec.EXACTLY ? measureHeight : getPaddingTop() + getPaddingBottom());
            return;
        }
        int width = measureWidth - getPaddingLeft() - getPaddingRight();
        int childCount = getChildCount();
        // child 的横坐标
        int childX = 0;
        // child 的纵坐标
        int childY = 0;
        // 每行 item 的个数
        int itemCountInRow = 0;
        // 流布局中 child 的总高度（多加了一个 mVerticalSpace ）
        float totalHeight = 0;
        mRowInfoList.clear();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 宽：将 width 作为 child 可以使用的最大宽
            // 高：不指定 child 的高，child 想要多高，就给它多高
            child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), MeasureSpec.UNSPECIFIED);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            childX += childWidth;
            // 判断 child 是否放入本行（小于 width ，则 child 放入本行，否则转入下行）
            if (childX <= width) {
                itemCountInRow++;
                // 取最大的值作为 child 的高度
                childY = childY < childHeight ? childHeight : childY;
                // 判断是否有可能放入下个 child
                if (childX <= (width - mHorizontalSpace)) {
                    childX += mHorizontalSpace;
                    if (i == (childCount - 1)) {
                        /** 提交最后一行的信息 */
                        // 如果没有设置 child 的高度，则采用测量出的高度 y
                        if (mFlowHeight == FlowConfig.INVALID_VAL) {
                            mRowInfoList.add(new float[]{itemCountInRow, i, childY});
                            totalHeight = totalHeight + childY + mVerticalSpace;
                        } else {
                            mRowInfoList.add(new float[]{itemCountInRow, i, mFlowHeight});
                            totalHeight = totalHeight + mFlowHeight + mVerticalSpace;
                        }
                    }
                }
                // 下个 child 换行，提交本行 child 的信息
                else {
                    /** 提交本行信息 */
                    // 如果没有设置 child 的高度，则采用测量出的高度 y
                    if (mFlowHeight == FlowConfig.INVALID_VAL) {
                        mRowInfoList.add(new float[]{itemCountInRow, i, childY});
                        totalHeight = totalHeight + childY + mVerticalSpace;
                    } else {
                        mRowInfoList.add(new float[]{itemCountInRow, i, mFlowHeight});
                        totalHeight = totalHeight + mFlowHeight + mVerticalSpace;
                    }
                    // 下个 item 转入下行前，将记录 item 宽度的 x 和记录行高度的 y 重置
                    childX = 0;
                    childY = 0;
                    itemCountInRow = 0;
                }
            }
            // child 已转入下行
            else {
                /** 提交上一行信息 */
                // 如果没有设置 child 的高度，则采用测量出的高度 y
                if (mFlowHeight == FlowConfig.INVALID_VAL) {
                    mRowInfoList.add(new float[]{itemCountInRow, i - 1, childY});
                    totalHeight = totalHeight + childY + mVerticalSpace;
                } else {
                    mRowInfoList.add(new float[]{itemCountInRow, i - 1, mFlowHeight});
                    totalHeight = totalHeight + mFlowHeight + mVerticalSpace;
                }
                // 转入下行后的第一个 child 的宽和高
                childX = (int) (childWidth + mHorizontalSpace);
                childY = childHeight;
                itemCountInRow = 1;
                /** 如果是最后一个 child 需要换行，还需再提交最后一行信息 */
                if (i == (childCount - 1)) {
                    // 如果没有设置 child 的高度，则采用测量出的高度 y
                    if (mFlowHeight == FlowConfig.INVALID_VAL) {
                        mRowInfoList.add(new float[]{itemCountInRow, i, childY});
                        totalHeight = totalHeight + childHeight + mVerticalSpace;
                    } else {
                        mRowInfoList.add(new float[]{itemCountInRow, i, mFlowHeight});
                        totalHeight = totalHeight + mFlowHeight + mVerticalSpace;
                    }
                }
            }
        }
        // 如果设置了最大显示行数
        if (mMaxRow != FlowConfig.INVALID_VAL && mMaxRow >= 0) {
            // 如果测量的行数已经大于最大显示行数
            if (mRowInfoList.size() > mMaxRow) {
                float tempH = 0;
                // 取最大显示行数的总高度
                for (int i = 0; i < mMaxRow; i++) {
                    tempH += mRowInfoList.get(i)[2] + mVerticalSpace;
                }
                totalHeight = tempH;
            }
        }
        int height = (int) (totalHeight - mVerticalSpace + getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(measureWidth, heightMode == MeasureSpec.EXACTLY ? measureHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mAdapter == null || getChildCount() == 0) {
            return;
        }
        // child 的横坐标
        float childX;
        // child 的纵坐标
        float childY = getPaddingTop();
        for (int i = 0; i < mRowInfoList.size(); i++) {
            // 如果最大显示行数的属性设置有效
            if (mMaxRow != FlowConfig.INVALID_VAL && mMaxRow >= 0) {
                // 如果已经到达最大显示行数，则接下来的逻辑不执行
                if (mMaxRow < i + 1) {
                    return;
                }
            }
            float[] rowInfo = mRowInfoList.get(i);
            // 每行的 item 个数
            int itemCountInRow = (int) rowInfo[0];
            // 一行中最后一个 item 的编号
            int lastIndex = (int) rowInfo[1];
            // 本行的高度
            float rowHeight = rowInfo[2];
            // 如果 child 在一行中是横向左对齐
            if (mHorizontalAlign == FlowConfig.FLOW_HORIZONTAL_ALIGN_LEFT) {
                childX = getPaddingLeft();
            } else {
                int tempWidth = 0;
                for (int j = 0; j < itemCountInRow; j++) {
                    View child = getChildAt(j + lastIndex + 1 - itemCountInRow);
                    int cw = child.getMeasuredWidth();
                    tempWidth += cw;
                }
                tempWidth += (itemCountInRow - 1) * mHorizontalSpace;
                // 如果 child 在一行中是横向居中对齐
                if (mHorizontalAlign == FlowConfig.FLOW_HORIZONTAL_ALIGN_MIDDLE) {
                    childX = (r - l - tempWidth) / 2.0f;
                }
                // child 在一行中是横向右对齐
                else {
                    childX = r - getPaddingRight() - tempWidth;
                }
            }
            for (int j = 0; j < itemCountInRow; j++) {
                View child = getChildAt(j + lastIndex + 1 - itemCountInRow);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                // 如果没有设置 child 的高度
                if (mFlowHeight == FlowConfig.INVALID_VAL) {
                    // 默认 child 在一行中是纵向顶部对齐
                    float cy = childY;
                    if (childHeight < rowHeight) {
                        // 如果 child 在一行中是纵向居中对齐
                        if (mVerticalAlign == FlowConfig.FLOW_VERTICAL_ALIGN_MIDDLE) {
                            cy = childY + (rowHeight - childHeight) / 2f;
                        }
                        // 如果 child 在一行中是纵向底部对齐
                        else if (mVerticalAlign == FlowConfig.FLOW_VERTICAL_ALIGN_BOTTOM) {
                            cy = childY + rowHeight - childHeight;
                        }
                    }
                    child.layout((int) childX, (int) cy, (int) childX + childWidth, (int) (cy + childHeight));
                } else {
                    child.layout((int) childX, (int) childY, (int) childX + childWidth, (int) (childY + rowHeight));
                }
                childX += childWidth + mHorizontalSpace;
            }
            childY += rowHeight + mVerticalSpace;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    public void setFlowHeight(float flowHeight) {
        this.mFlowHeight = flowHeight;
    }

    public float getFlowHeight() {
        return mFlowHeight;
    }

    public void setMaxRow(int maxRow) {
        this.mMaxRow = maxRow;
    }

    public int getFlowMaxRow() {
        return mMaxRow;
    }

    public void setHorizontalAlign(@FlowConfig int align) {
        this.mHorizontalAlign = align;
    }

    public int getHorizontalAlign() {
        return mHorizontalAlign;
    }

    /**
     * 注：当设置了 flow_height 后，此属性无效
     *
     * @param align
     */
    public void setVerticalAlign(@FlowConfig int align) {
        this.mVerticalAlign = align;
    }

    public int getVerticalAlign() {
        return mVerticalAlign;
    }

    public void setHorizontalSpace(float space) {
        this.mHorizontalSpace = space;
        ;
    }

    public float getHorizontalSpace() {
        return mHorizontalSpace;
    }

    public void setVerticalSpace(float space) {
        this.mVerticalSpace = space;
    }

    public float getVerticalSpace() {
        return mVerticalSpace;
    }


    private class AdapterObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mAdapter != null) {
            if (mDataSetObserver != null) {
                // 删除已经存在的观察者
                mAdapter.unregisterDataSetObserver(mDataSetObserver);
            }
            mAdapter = null;
        }
        mItemClickListener = null;
        mItemLongClickListener = null;
        super.onDetachedFromWindow();
    }


}
