package com.liyi.flow.adapter;


import androidx.annotation.LayoutRes;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


public abstract class QuickFlowAdapter<T, VH extends BaseFlowHolder> extends BaseFlowAdapter<VH> {
    private static final int LAYOUT_NOT_FOUND = -404;

    private LayoutInflater mLayoutInflater;
    private SparseIntArray mLayoutTypes;
    protected List<T> mData;


    public QuickFlowAdapter() {

    }

    public void setData(List<T> list) {
        this.mData = list;
    }

    public List<T> getData() {
        return mData;
    }

    public void updateData(List<T> list) {
        setData(list);
        notifyDataSetChanged();
    }

    /**
     * 添加不同类型的 item 布局
     *
     * @param type     此处的 type 必须与 getItemViewType() 中返回的 type 对应
     * @param layoutId
     */
    public void addItemType(int type, @LayoutRes int layoutId) {
        if (mLayoutTypes == null) {
            mLayoutTypes = new SparseIntArray();
        }
        mLayoutTypes.put(type, layoutId);
    }

    @Override
    public int getItemViewType(int position) {
        return onHandleViewType(position);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        View convertView = getItemView(getLayoutId(viewType), parent);
        VH holder = createBaseViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onHandleViewHolder(holder, position, mData.get(position));
    }

    public abstract int onHandleViewType(int position);

    public abstract void onHandleViewHolder(VH holder, int position, T item);

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    protected View getItemView(int layoutResId, ViewGroup parent) {
        if (layoutResId != LAYOUT_NOT_FOUND) {
            return mLayoutInflater.inflate(layoutResId, parent, false);
        }
        return null;
    }

    private int getLayoutId(int vieType) {
        return mLayoutTypes.get(vieType, LAYOUT_NOT_FOUND);
    }

    protected VH createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        VH vh;
        // 泛型擦除会导致 z 为 null
        if (z == null) {
            vh = (VH) new BaseFlowHolder(view);
        } else {
            vh = createGenericKInstance(z, view);
        }
        return vh != null ? vh : (VH) new BaseFlowHolder(view);
    }

    @SuppressWarnings("unchecked")
    private VH createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseFlowHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && BaseFlowHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }
}
