package com.xq.settingsview.bean;

import com.xq.settingsview.widget.BaseBean;
import com.xq.settingsview.widget.OnDataChangedCallBack;

public class SwtichBean extends BaseBean {
    private String title,summay;
    private boolean isChecked;
    OnDataChangedCallBack<SwtichBean> callBack;


    public SwtichBean(String title, String summay, boolean isChecked, OnDataChangedCallBack<SwtichBean> callBack){
        this.title = title;
        this.isChecked = isChecked;
        this.summay = summay;
        this.callBack = callBack;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSummay(String summay) {
        this.summay = summay;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public OnDataChangedCallBack<SwtichBean> getCallBack() {
        return callBack;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public String getSummay() {
        return summay;
    }
}
