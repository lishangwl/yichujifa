package com.xieqing.uidesign.project.inflater;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xieqing.uidesign.MainActivity;
import com.xieqing.uidesign.R;
import com.xieqing.uidesign.UiDesignActivity;
import com.xieqing.uidesign.project.ViewLayoutInflater;
import com.xieqing.uidesign.project.exception.LayoutInflaterException;
import com.xieqing.uidesign.project.inflater.listener.UITouch;
import com.xieqing.uidesign.project.inflater.widget.AttrListAdapter;
import com.xieqing.uidesign.project.inflater.widget.UIEditText;
import com.xieqing.uidesign.project.model.Attr;

import java.util.ArrayList;
import java.util.Set;

import static com.xieqing.uidesign.project.inflater.BaseLayoutInflater.ATTR_NAME;

public class GboalViewHolder{
    private static GboalViewHolder getInstance = new GboalViewHolder();

    public static GboalViewHolder getInstance() {
        return getInstance;
    }

    ArrayList<UIView> views = new ArrayList<>();
    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ViewGroup viewGroup;
    View selectedView;

    public void reset(){
        views.clear();
    }

    public void put(UIView view){
        views.add(view);
    }

    public void removeByView(View view){
        for (UIView uiView:views){
            if (uiView.view == view){
                views.remove(uiView);
                viewGroup.removeView(view);
                return;
            }
        }
    }

    public View getSelectedView() {
        return selectedView;
    }

    public void select(UITouch touch){
        for (UIView uiView:views){
            if (uiView.touch == touch){
                touch.select();
                selectedView.setVisibility(View.VISIBLE);
                selectedView.setX(touch.getView().getX());
                selectedView.setY(touch.getView().getY());
                ViewGroup.LayoutParams layoutParams = selectedView.getLayoutParams();
                layoutParams.width = touch.getView().getWidth();
                layoutParams.height = touch.getView().getHeight();
                selectedView.setLayoutParams(layoutParams);
            }else{
                uiView.touch.clearSelect();
            }
        }
        toolbar.getMenu().findItem(R.id.action_preprotry).setVisible(true);
        toolbar.getMenu().findItem(R.id.action_delete).setVisible(true);
    }

    public UITouch findTouchByView(View view) {
        for (UIView uiView:views){
            if (uiView.view == view){
                return uiView.touch;
            }
        }
        return null;
    }

    public UITouch findSelect() {
        for (UIView uiView:views){
            if (uiView.touch.isSelected()){
                return uiView.touch;
            }
        }
        return null;
    }

    public void clearFoucs() {
        for (UIView uiView:views){
            uiView.touch.clearSelect();
        }
        selectedView.setVisibility(View.GONE);
        toolbar.getMenu().findItem(R.id.action_preprotry).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
    }

    public void setUIDesignViews(RecyclerView recyclerView, DrawerLayout drawerLayout,Toolbar toolbar, final ViewGroup viewGroup,View selectedView) {
        this.recyclerView = recyclerView;
        this.recyclerView.setLayoutManager(new LinearLayoutManager(viewGroup.getContext()));
        this.recyclerView.setAdapter(new AttrListAdapter(viewGroup.getContext(),new Attr()));
        this.drawerLayout = drawerLayout;
        this.toolbar = toolbar;
        this.selectedView = selectedView;
        this.viewGroup = viewGroup;
        if (drawerLayout != null){
            this.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(@NonNull View drawerView) {

                }

                @Override
                public void onDrawerClosed(@NonNull View drawerView) {
                    if (attrEditedCallBack!=null){
                        attrEditedCallBack.onCompleted();
                    }
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });
        }

        if (toolbar !=null){
            this.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getTitle().toString()){
                        case "属性":
                            UITouch uiTouch = findSelect();
                            if (uiTouch!=null){
                                showMenu(uiTouch.getView(),uiTouch.getAttr(),uiTouch.getLayoutInflater());
                            }
                            break;
                        case "添加":
                            final String[] views = ViewLayoutInflater.inflaterMap.keySet().toArray(new String[]{});
                            new AlertDialog.Builder(GboalViewHolder.this.viewGroup.getContext())
                                    .setTitle("添加组件")
                                    .setItems(views, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                BaseLayoutInflater layoutInflater = ViewLayoutInflater.inflaterMap.get(views[which]);
                                                viewGroup.addView(layoutInflater.getView(GboalViewHolder.this.viewGroup.getContext(),layoutInflater.getBaseAttr(),true));
                                            }catch (Exception e){
                                                new AlertDialog.Builder(GboalViewHolder.this.viewGroup.getContext())
                                                        .setTitle("错误")
                                                        .setMessage(e.getMessage())
                                                        .setPositiveButton("确定",null).show();
                                            }
                                        }
                                    }).show();
                            break;
                        case "删除":
                            UITouch uiTouch1 = findSelect();
                            clearFoucs();
                            removeByView(uiTouch1.getView());
                            break;
                        case "保存":
                        /*new AlertDialog.Builder(GboalViewHolder.this.drawerLayout.getContext())
                                .setTitle("xml代码")
                                .setMessage(toXml())
                                .setPositiveButton("确定",null).show();*/
                            GboalViewHolder.this.viewGroup.getContext().startActivity(new
                                    Intent(GboalViewHolder.this.viewGroup.getContext()
                                    , MainActivity.class).putExtra("layout",toXml()));
                            break;
                    }
                    return false;
                }
            });
        }
    }

    private String toXml() {
        StringBuilder stringBuilder = new StringBuilder("<界面>\n");
        for (UIView uiView : views){
            stringBuilder.append("<"+uiView.layoutInflater.getName());
            uiView.touch.getAttr().toXml(stringBuilder);
            stringBuilder.append("/>").append("\n");
        }
        stringBuilder.append("</界面>");
        return stringBuilder.toString();
    }


    private void showMenu(final View v, final Attr attr,final BaseLayoutInflater layoutInflater) {
        Set<String> base = layoutInflater.baseAttr.keySet();
        Set<String> view = attr.keySet();
        StringBuilder attrsString = new StringBuilder();
        for (String string : base){
            if (!view.contains(string)){
                attr.put(string,layoutInflater.baseAttr.getString(string));
            }
        }
        GboalViewHolder.getInstance()
                .openAttrEditLayout(attr,
                        new GboalViewHolder.AttrEditedCallBack() {
                            @Override
                            public void onCompleted() {
                                try {
                                    layoutInflater.bind(v,attr);
                                    if (!layoutInflater.bindView(v,attr)){
                                        throw new LayoutInflaterException("error: unknow");
                                    }
                                    //GboalViewHolder.getInstance().closeAttrEditLayout();
                                    v.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            selectedView.setX(attr.getValue(BaseLayoutInflater.ATTR_LEFT));
                                            selectedView.setY(attr.getValue(BaseLayoutInflater.ATTR_TOP));
                                            ViewGroup.LayoutParams layoutParams = selectedView.getLayoutParams();
                                            layoutParams.width = v.getWidth();
                                            layoutParams.height = v.getHeight();
                                            selectedView.setLayoutParams(layoutParams);
                                        }
                                    },200);
                                }catch (Exception e){
                                    //Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                    new AlertDialog.Builder(v.getContext())
                                            .setTitle("错误")
                                            .setMessage(e.getMessage())
                                            .setPositiveButton("确定",null).show();
                                }
                            }
                        });
    }



    public void openDrawableLayout(int gravity){
        drawerLayout.openDrawer(gravity);
    }

    private AttrEditedCallBack attrEditedCallBack;
    public void openAttrEditLayout(Attr attrs, final AttrEditedCallBack callBack){
        attrEditedCallBack = callBack;
        ((AttrListAdapter)this.recyclerView.getAdapter()).setAttr(attrs);
        drawerLayout.openDrawer(Gravity.END);
    }


    public void closeAttrEditLayout() {
        drawerLayout.closeDrawer(Gravity.END);
    }

    public boolean hasViewFromName(String s) {
        for (UIView uiView : views){
            if (uiView.touch.getAttr().has(ATTR_NAME)){
                if (uiView.touch.getAttr().getString(ATTR_NAME).equals(s)){
                    return true;
                }
            }else {
                if (uiView.layoutInflater.baseAttr.getString(ATTR_NAME).equals(s)){
                    return true;
                }
            }

        }
        return false;
    }

    public UIView findViewFromName(String s) {
        for (UIView uiView : views){
            if (uiView.touch.getAttr().has(ATTR_NAME)){
                if (uiView.touch.getAttr().getString(ATTR_NAME).equals(s)){
                    return uiView;
                }
            }else {
                if (uiView.layoutInflater.baseAttr.getString(ATTR_NAME).equals(s)){
                    return uiView;
                }
            }

        }
        return null;
    }


    public static class UIView{
        public UIView(UITouch touch,View view,BaseLayoutInflater layoutInflater){
            this.touch = touch;
            this.view = view;
            this.layoutInflater = layoutInflater;
        }
        UITouch touch;
        View view;
        BaseLayoutInflater layoutInflater;
    }

    public interface AttrEditedCallBack{
        void onCompleted();
    }
}
