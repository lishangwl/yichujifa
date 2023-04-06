package uidesign.project.inflater;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.SPUtils;

import java.util.ArrayList;
import java.util.Set;

import esqeee.xieqing.com.eeeeee.R;
import java.io.File;

import uidesign.project.ViewLayoutInflater;
import uidesign.project.exception.LayoutInflaterException;
import uidesign.project.inflater.listener.UITouch;
import uidesign.project.inflater.widget.AttrListAdapter;
import uidesign.project.inflater.widget.ViewListAdapter;
import uidesign.project.model.Attr;

import static uidesign.project.inflater.BaseLayoutInflater.ATTR_NAME;


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
    RecyclerView viewList;

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

    public UIView findSelectView() {
        for (UIView uiView:views){
            if (uiView.touch.isSelected()){
                return uiView;
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

    Attr activityAttr;

    public void setActivityAttr(Attr activityAttr) {
        this.activityAttr = activityAttr;
    }

    public Attr getActivityAttr() {
        return activityAttr;
    }

    File file;

    public void setUIDesignViews(RecyclerView viewList,RecyclerView recyclerView, DrawerLayout drawerLayout,Toolbar toolbar, final ViewGroup viewGroup,View selectedView,File file) {
        this.recyclerView = recyclerView;
        this.recyclerView.setLayoutManager(new LinearLayoutManager(viewGroup.getContext()));
        this.recyclerView.setAdapter(new AttrListAdapter(viewGroup.getContext(),new Attr()));
        this.drawerLayout = drawerLayout;
        this.toolbar = toolbar;
        this.viewList = viewList;
        this.viewList.setLayoutManager(new LinearLayoutManager(viewGroup.getContext()));
        this.viewList.setAdapter(new ViewListAdapter(viewGroup.getContext(),views));
        this.selectedView = selectedView;
        this.file = file;
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

                                if (SPUtils.getInstance("ui").getBoolean("showDpTip",true)){
                                    new AlertDialog.Builder(GboalViewHolder.this.viewGroup.getContext())
                                            .setTitle("提示")
                                            .setMessage("什么是dp、px、-1、-2？"+"\n\n\n"
                                            +"px代表像素点，1px代表着一个像素点的大小。\n\n"
                                            +"dp是不固定大小,分辨率越大的设备上,dp代表的值越大,用于适用不同分辨率的设备。\n\n"
                                            +"-1代表着最大值，会在屏幕内跟屏幕宽度和高度一样大。\n\n"
                                                    +"-2代表着自动调整大小，组件会自己智能调整大小。\n\n")
                                    .setPositiveButton("知道了",null)
                                    .setNeutralButton("不再提示",(d,i)->{
                                        SPUtils.getInstance("ui").put("showDpTip",false);
                                    }).show();
                                }
                            }
                            break;
                        case "窗口":
                            Set<String> base = ViewLayoutInflater.activityInflater.baseAttr.keySet();
                            Set<String> view = activityAttr.keySet();
                            for (String string : base){
                                if (!view.contains(string)){
                                    activityAttr.put(string,ViewLayoutInflater.activityInflater.baseAttr.getString(string));
                                }
                            }
                            GboalViewHolder.getInstance()
                                    .openAttrEditLayout(activityAttr,
                                            new GboalViewHolder.AttrEditedCallBack() {
                                                @Override
                                                public void onCompleted() {
                                                    try {
                                                        ViewLayoutInflater.activityInflater.inflate((Activity) GboalViewHolder.this.viewGroup.getContext(),
                                                                GboalViewHolder.this.viewGroup,activityAttr);
                                                    }catch (Exception e){
                                                        new AlertDialog.Builder(GboalViewHolder.this.viewGroup.getContext())
                                                                .setTitle("错误")
                                                                .setMessage(e.getMessage())
                                                                .setPositiveButton("确定",null).show();
                                                    }
                                                }
                                            });
                            break;
                        case "添加":
                            final String[] views = ViewLayoutInflater.inflaterMap.keySet().toArray(new String[]{});
                            new AlertDialog.Builder(GboalViewHolder.this.viewGroup.getContext())
                                    .setTitle("添加组件")
                                    .setItems(views, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                BaseLayoutInflater layoutInflater = (BaseLayoutInflater) ViewLayoutInflater.inflaterMap.get(views[which]);
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
                            UIView uiView = findSelectView();
                            clearFoucs();
                            GboalViewHolder.this.views.remove(uiView);
                            ((ViewGroup)uiView.view.getParent()).removeView(uiView.view);
                            break;
                        case "保存":
                        /*new AlertDialog.Builder(GboalViewHolder.this.drawerLayout.getContext())
                                .setTitle("xml代码")
                                .setMessage(toXml())
                                .setPositiveButton("确定",null).show();*/
                            /*GboalViewHolder.this.viewGroup.getContext().startActivity(new
                                    Intent(GboalViewHolder.this.viewGroup.getContext()
                                    , MainActivity.class).putExtra("layout",toXml()));*/

                            FileIOUtils.writeFileFromString(file,toXml());
                            break;
                    }
                    return false;
                }
            });
        }
    }

    private String toXml() {
        StringBuilder stringBuilder = new StringBuilder("<界面");
        activityAttr.toXml(stringBuilder);
        stringBuilder.append(">\n");
        for (UIView uiView : views){
            stringBuilder.append("<"+uiView.layoutInflater.getName());
            uiView.touch.getAttr().toXml(stringBuilder);
            stringBuilder.append("/>").append("\n");
        }
        stringBuilder.append("</界面>");
        return stringBuilder.toString();
    }


    private void showMenu(final View v, final Attr attr, final BaseLayoutInflater layoutInflater) {
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
                                    layoutInflater.bind(v,attr,true);
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

    public UIView findViewFromView(View view) {
        for (UIView uiView : views){
            if (uiView.view == view){
                return uiView;
            }
        }
        return null;
    }

    public ArrayList<UIView> getViews() {
        return views;
    }

    public void reFreshViewList() {
        viewList.getAdapter().notifyDataSetChanged();
    }

    public void closeDrawableLayout(int start) {
        this.drawerLayout.closeDrawer(start);
    }


    public static class UIView{
        public UIView(UITouch touch, View view, BaseLayoutInflater layoutInflater){
            this.touch = touch;
            this.view = view;
            this.layoutInflater = layoutInflater;
        }
        UITouch touch;
        View view;
        BaseLayoutInflater layoutInflater;

        public View getView() {
            return view;
        }

        public UITouch getTouch() {
            return touch;
        }

        public BaseLayoutInflater getLayoutInflater() {
            return layoutInflater;
        }
    }

    public interface AttrEditedCallBack{
        void onCompleted();
    }
}
