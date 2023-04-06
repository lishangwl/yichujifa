package com.xieqing.uidesign.project.inflater.listener;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;

import com.xieqing.uidesign.R;
import com.xieqing.uidesign.project.Utils.Dimetion;
import com.xieqing.uidesign.project.inflater.BaseLayoutInflater;
import com.xieqing.uidesign.project.inflater.GboalViewHolder;
import com.xieqing.uidesign.project.model.Attr;

public class UITouch implements View.OnTouchListener {
    private boolean isSelected = false;
    private Attr attr;
    private BaseLayoutInflater layoutInflater;
    private View view;
    private Drawable defultDrawable;
    public boolean isSelected() {
        return isSelected;
    }
    public void clearSelect(){
        isSelected = false;
    }
    boolean isDesgin = true;
    public Drawable getDefultDrawable(){
        return defultDrawable;
    }

    public UITouch(View view,Attr attrs,BaseLayoutInflater layoutInflater,boolean isDesgin){
        this.attr = attrs;
        this.layoutInflater = layoutInflater;
        this.view = view;
        this.isDesgin = isDesgin;
        defultDrawable = view.getBackground();
    }

    public Attr getAttr() {
        return attr;
    }

    public BaseLayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public View getView() {
        return view;
    }

    float lastX, lastY,dx,dy;
    float paramX, paramY;
    boolean isOutSide = false;
    long touchTime = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isDesgin){
            return false;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                paramX = view.getX();
                paramY = view.getY();
                isOutSide = false;
                touchTime = System.currentTimeMillis();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isSelected){
                    dx = event.getRawX() - lastX;
                    dy = event.getRawY() - lastY;
                    view.setX(paramX + dx);
                    view.setY(paramY + dy);
                    GboalViewHolder.getInstance().getSelectedView().setX(paramX + dx);
                    GboalViewHolder.getInstance().getSelectedView().setY(paramY + dy);

                    attr.put(BaseLayoutInflater.ATTR_LEFT, Dimetion.toPx(view.getX()));
                    attr.put(BaseLayoutInflater.ATTR_TOP,Dimetion.toPx(view.getY()));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isSelected() && !isOutSide && isDesgin){
                    select();
                    GboalViewHolder.getInstance().select(this);
                }
                break;
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                break;
        }

        return true;
    }


    public void select() {
        isSelected = true;
    }
}
