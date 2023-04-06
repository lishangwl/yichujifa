package com.xieqing.uidesign.project.inflater;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.xieqing.uidesign.project.Utils.Dimetion;
import com.xieqing.uidesign.project.exception.LayoutInflaterException;
import com.xieqing.uidesign.project.inflater.listener.UITouch;
import com.xieqing.uidesign.project.model.Attr;
import java.util.Set;

public abstract class BaseLayoutInflater<V extends View> implements LayoutInflater{
    private static final String TAG = "BaseLayoutInflater";
    public static final String ATTR_LEFT = "左边";
    public static final String ATTR_TOP = "顶边";
    public static final String ATTR_WIDTH = "宽度";
    public static final String ATTR_HEIGHT = "高度";
    public static final String ATTR_NAME = "名称";
    public static final String ATTR_BACKGROUND_COLOR = "背景颜色";


    public Attr baseAttr = getBaseAttr();

    public Attr getBaseAttr(){
        Attr baseAttr = new Attr();
        {
            baseAttr.put(ATTR_LEFT,"0dp");
            baseAttr.put(ATTR_TOP,"0dp");
            baseAttr.put(ATTR_WIDTH,"-2");
            baseAttr.put(ATTR_HEIGHT,"-2");
            baseAttr.put(ATTR_BACKGROUND_COLOR,"");
        }
        return baseAttr;
    }

    @Override
    public View getView(Context context, Attr attrs,boolean isDesgin) {
        View view = createViewInstance(context,attrs);
        bindTheView(view,attrs,isDesgin);
        return view;
    }

    private void bindTheView(View view, Attr attrs,boolean isDesgin) {
        bindDefault(view,attrs,isDesgin);
        bind(view,attrs);
        if (!bindView((V) view,attrs)){
            throw new LayoutInflaterException("error: unknow");
        }
    }


    public void bind(View view, Attr attrs) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        for (String key:attrs.keySet()){
            switch (key){
                case "名称":
                    GboalViewHolder.UIView uiView = GboalViewHolder.getInstance().findViewFromName(attrs.getString(key));
                    if (uiView!=null&&uiView.layoutInflater != this){
                        throw new LayoutInflaterException("名称:"+attrs.getString(key)+" 已经存在了！");
                    }
                    break;
                case "宽度":
                    layoutParams.width = getIntValue(attrs.getString(key));
                    Log.d(TAG,key+"="+layoutParams.width);
                    break;
                case "高度":
                    layoutParams.height = getIntValue(attrs.getString(key));
                    Log.d(TAG,key+"="+layoutParams.height);
                    break;
                case ATTR_LEFT:
                    view.setX(getIntValue(attrs.getString(key)));
                    break;
                case ATTR_TOP:
                    view.setY(getIntValue(attrs.getString(key)));
                    break;
                case ATTR_BACKGROUND_COLOR:
                    if (!TextUtils.isEmpty(attrs.getString(key))){
                        view.setBackgroundColor(attrs.getColor(key));
                    }
                    break;
            }
        }
        view.setLayoutParams(layoutParams);
    }




    private void bindDefault(View view,Attr attr,boolean isDesgin) {
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
        bind(view,baseAttr);
    }




    private void bindTouchClick(View view,Attr attr,boolean isDesgin) {
        UITouch mUiTouch = new UITouch(view, attr,this,isDesgin);
        if (isDesgin){
            GboalViewHolder.getInstance().put(new GboalViewHolder.UIView(mUiTouch,view,this));
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



    public abstract boolean bindView(V view,Attr attrs);


    public abstract V createViewInstance(Context context, Attr attrs);

    public abstract String getName();
}
