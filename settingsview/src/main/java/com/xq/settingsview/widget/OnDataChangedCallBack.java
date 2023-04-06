package com.xq.settingsview.widget;

public interface OnDataChangedCallBack<T  extends BaseBean>{
        void onChanged(T t);
    }