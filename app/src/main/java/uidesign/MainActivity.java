package uidesign;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import uidesign.project.ViewLayoutInflater;
import uidesign.project.exception.LayoutInflaterException;
import uidesign.project.inflater.ActivityInflater;
import uidesign.project.inflater.BaseLayoutInflater;
import uidesign.project.inflater.GboalViewHolder;
import uidesign.project.model.Attr;

import static uidesign.project.inflater.BaseLayoutInflater.ATTR_ON_CREATE;
import static uidesign.project.inflater.BaseLayoutInflater.ATTR_ON_DES;

public class MainActivity extends BaseActivity {
    FrameLayout absoluteLayout;

    public static MainActivity currentActivity;

    private GboalViewHolder viewHolder;
    private Attr attr = ActivityInflater.getAttr();

    public Attr getAttr() {
        return attr;
    }

    public void setAttr(Attr attr) {
        this.attr = attr;
    }

    public GboalViewHolder getViewHolder() {
        if (viewHolder == null){
            viewHolder = new GboalViewHolder();
        }
        return viewHolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = this;

        absoluteLayout = new FrameLayout(this);
        setContentView(absoluteLayout);

        try {
            ViewLayoutInflater.from(this,false)
                    .inflate(absoluteLayout,getIntent().getStringExtra("layout"),true,false);
        } catch (LayoutInflaterException e) {
            e.printStackTrace();
            new AlertDialog.Builder(this)
                    .setTitle("错误")
                    .setMessage(e.getMessage())
                    .setPositiveButton("确定",null).show();
        }
        attchOnCreteEvent();
    }

    private void attchOnCreteEvent() {
        try {
            String value = attr.getString(ATTR_ON_CREATE);
            if (!value.equals("") &&!value.equals("无")){
                ActionRunHelper.startAction(this,value.trim());
            }
        }catch (Exception e){
            //RuntimeLog.log(e);
            e.printStackTrace();
        }
    }

    private void attchOnDestoryEvent() {

        try {
            String value = attr.getString(ATTR_ON_DES);
            if (!value.equals("") &&!value.equals("无")){
                ActionRunHelper.startAction(this,value.trim());
            }
        }catch (Exception e){
            //RuntimeLog.log(e);
            e.printStackTrace();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        attchOnDestoryEvent();
        currentActivity = null;
    }

    public View findViewByTag(String name){
        return findViewByTag(absoluteLayout,name);
    }

    public View findViewByTag(View parentView,String name){
        if (parentView.getTag() instanceof Attr){
            if (((Attr)parentView.getTag()).getString(BaseLayoutInflater.ATTR_NAME).equals(name)){
                return parentView;
            }
        }
        if (parentView instanceof ViewGroup){
            for (int i = 0; i<((ViewGroup)parentView).getChildCount();i++){
                View view = findViewByTag(((ViewGroup)parentView).getChildAt(i),name);
                if (view!=null){
                    return view;
                }
            }
        }
        return null;
    }

    public void applyView(View view,Attr attr){
        //RuntimeLog.log("applyView["+attr);
        BaseLayoutInflater layoutInflater =
                getViewHolder().findViewFromName(attr.getString(BaseLayoutInflater.ATTR_NAME))
                        .getLayoutInflater();
        if (layoutInflater != null){
            try {
                view.setTag(attr);
                layoutInflater.bind(view,attr,false);
                layoutInflater.bindView(view,attr);
            }catch (Exception e){
                new AlertDialog.Builder(this)
                        .setTitle("错误")
                        .setMessage(e.getMessage())
                        .setPositiveButton("确定",null).show();
            }
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("错误")
                    .setMessage("err："+attr.getString(BaseLayoutInflater.ATTR_NAME))
                    .setPositiveButton("确定",null).show();
        }
    }

    public void addView(View view) {
        absoluteLayout.addView(view);
    }

    public void deleteView(View name) {

        RuntimeLog.log(name);
        for (int i = 0;i<absoluteLayout.getChildCount();i++){
            absoluteLayout.removeView(name);
            GboalViewHolder.UIView uiView = getViewHolder().findViewFromView(name);
            getViewHolder().getViews().remove(uiView);
        }
    }
}
