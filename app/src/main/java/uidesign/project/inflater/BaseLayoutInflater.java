package uidesign.project.inflater;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import uidesign.MainActivity;
import uidesign.project.Utils.Dimetion;
import uidesign.project.exception.LayoutInflaterException;
import uidesign.project.inflater.listener.UITouch;
import uidesign.project.model.Attr;

public abstract class BaseLayoutInflater<V extends View> implements LayoutInflater {
    private static final String TAG = "BaseLayoutInflater";
    public static final String ATTR_LEFT = "左边";
    public static final String ATTR_TOP = "顶边";
    public static final String ATTR_WIDTH = "宽度";
    public static final String ATTR_HEIGHT = "高度";
    public static final String ATTR_NAME = "名称";
    public static final String ATTR_IMAGE = "图片";
    public static final String ATTR_GRAVITY = "对齐方式";
    public static final String ATTR_CLICK="被点击";
    public static final String ATTR_HTML="加载超文本";
    public static final String ATTR_ON_CREATE="被创建时";
    public static final String ATTR_ON_DES="被销毁时";
    public static final String ATTR_BACKGROUND_COLOR = "背景颜色";
    public static final String ATTR_BACKGROUND_IMAGE = "背景图片";

    public static final String ATTR_STATUS_BAR_COLOR = "状态栏颜色";
    public static final String ATTR_NAV_BAR_COLOR = "导航栏颜色";
    public static final String ATTR_ELEVATION = "Z轴";
    public static final String ATTR_VISIBILITY = "可视";
    public static final String ATTR_MARGIN_LEFT = "左外边距";
    public static final String ATTR_MARGIN_RIGHT = "右外边距";
    public static final String ATTR_MARGIN_TOP = "上外边距";
    public static final String ATTR_MARGIN_BOTTOM = "下外边距";
    public static final String ATTR_TYPE = "类型";

    public Attr baseAttr = getBaseAttr();

    public Attr getBaseAttr(){
        Attr baseAttr = new Attr();
        {
            baseAttr.put(ATTR_LEFT,"0dp");
            baseAttr.put(ATTR_TOP,"0dp");
            baseAttr.put(ATTR_WIDTH,"-2");
            baseAttr.put(ATTR_HEIGHT,"-2");
            baseAttr.put(ATTR_BACKGROUND_COLOR,"");
            baseAttr.put(ATTR_ELEVATION,"0dp");
            baseAttr.put(ATTR_VISIBILITY,"真");
        }
        return baseAttr;
    }

    @Override
    public View getView(Context context, Attr attrs, boolean isDesgin) {
        View view = createViewInstance(context,attrs);
        view.setTag(attrs);
        Log.d("layoutIn",attrs.toString());
        bindTheView(view,attrs,isDesgin);
        return view;
    }

    private void bindTheView(View view, Attr attrs, boolean isDesgin) {
        bindDefault(view,attrs,isDesgin);
        bind(view,attrs,isDesgin);
        if (!bindView((V) view,attrs)){
            throw new LayoutInflaterException("error: unknow");
        }
    }


    public void bind(View view, Attr attrs,boolean isDesgin) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        for (String key:attrs.keySet()){
            switch (key){
                case ATTR_NAME:
                    if (TextUtils.isEmpty(attrs.getString(key).trim())){
                        attrs.put(ATTR_NAME,getOneName("组件"));
                    }
                    if (isDesgin){
                        GboalViewHolder.UIView uiView = GboalViewHolder.getInstance().findViewFromName(attrs.getString(key));
                        if (uiView!=null&&uiView.layoutInflater != this){
                            throw new LayoutInflaterException("名称:"+attrs.getString(key)+" 已经存在了！");
                        }
                    }
                    break;
                case ATTR_WIDTH:
                    layoutParams.width = getIntValue(attrs.getString(key));
                    break;
                case ATTR_HEIGHT:
                    layoutParams.height = getIntValue(attrs.getString(key));
                    break;
                case ATTR_LEFT:
                    view.setX(getIntValue(attrs.getString(key)));
                    break;
                case ATTR_TOP:
                    view.setY(getIntValue(attrs.getString(key)));
                    break;
                case ATTR_ELEVATION:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.setElevation(getIntValue(attrs.getString(key)));
                    }
                    break;
                case ATTR_BACKGROUND_COLOR:
                    if (!TextUtils.isEmpty(attrs.getString(key))){
                        view.setBackgroundColor(attrs.getColor(key));
                    }
                    break;
                case ATTR_VISIBILITY:
                    view.setVisibility(attrs.getBoolean(key)?View.VISIBLE:View.GONE);
                    break;
            }
        }
        view.setLayoutParams(layoutParams);
    }




    private void bindDefault(View view, Attr attr, boolean isDesgin) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null){
            layoutParams = new ViewGroup.LayoutParams(-2,-2);
            view.setLayoutParams(layoutParams);
        }
        view.setX(0);
        view.setY(0);

        if (isDesgin){
            view.setFocusable(false);
            view.setClickable(false);
        }else{
            view.setFocusable(true);
            view.setClickable(true);
        }
        bindTouchClick(view,attr,isDesgin);
        //bind(view,baseAttr,isDesgin);
    }




    private void bindTouchClick(View view, Attr attr, boolean isDesgin) {
        UITouch mUiTouch = new UITouch(view, attr,this,isDesgin);
        if (isDesgin){
            GboalViewHolder.getInstance().put(new GboalViewHolder.UIView(mUiTouch,view,this));
        }else{
            if (MainActivity.currentActivity !=null){
                MainActivity.currentActivity.getViewHolder().put(new GboalViewHolder.UIView(mUiTouch,view,this));
            }
        }
        view.setOnTouchListener(mUiTouch);


    }



    public int getIntValue(String string) {
        if (string.equals("-1")){
            return ViewGroup.LayoutParams.MATCH_PARENT;
        }else if (string.equals("-2")){
            return ViewGroup.LayoutParams.WRAP_CONTENT;
        }else{
            return Dimetion.parseToPixel(string);
        }
    }


    public String getOneName(String target){
        int i = 1;
        while (GboalViewHolder.getInstance().hasViewFromName(target+i)){
            i++;
        }
        return target+i;
    }



    public abstract boolean bindView(V view, Attr attrs);


    public abstract V createViewInstance(Context context, Attr attrs);

    public abstract String getName();
}
